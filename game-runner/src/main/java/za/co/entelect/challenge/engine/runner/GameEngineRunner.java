package za.co.entelect.challenge.engine.runner;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import za.co.entelect.challenge.config.GameRunnerConfig;
import za.co.entelect.challenge.engine.exceptions.InvalidRunnerStateException;
import za.co.entelect.challenge.game.contracts.command.RawCommand;
import za.co.entelect.challenge.game.contracts.common.RefereeMessage;
import za.co.entelect.challenge.game.contracts.exceptions.TimeoutException;
import za.co.entelect.challenge.game.contracts.game.*;
import za.co.entelect.challenge.game.contracts.map.GameMap;
import za.co.entelect.challenge.game.contracts.player.Player;
import za.co.entelect.challenge.game.contracts.renderer.GameMapRenderer;
import za.co.entelect.challenge.game.contracts.renderer.RendererType;
import za.co.entelect.challenge.player.entity.BasePlayer;
import za.co.entelect.challenge.player.entity.BotExecutionContext;
import za.co.entelect.challenge.renderer.RendererResolver;
import za.co.entelect.challenge.utils.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameEngineRunner implements LifecycleEngineRunner {

    private static final Logger LOGGER = LogManager.getLogger(GameEngineRunner.class);
    private static final String COMMAND_DELIMITER = ";";
    public static final String SECTION_DELIMITER = "=======================================";
    public static final String ROUND_DELIMITER = "******************************************************************************************************";

    private GameRunnerConfig gameRunnerConfig;

    private GameMap gameMap;
    private List<Player> players;
    private RunnerReferee referee;
    private RunnerRoundProcessor roundProcessor;

    private GameResult gameResult;
    private GameEngine gameEngine;
    private GameMapGenerator gameMapGenerator;
    private GameRoundProcessor gameRoundProcessor;

    private RendererResolver rendererResolver;
    private List<BotExecutionContext> botExecutionContexts;

    public GameResult runMatch() throws Exception {

        onGameStarting();
        while (!isGameComplete()) {
            onRoundStarting();
            onProcessRound();
            onRoundComplete();
        }
        onGameComplete();

        return gameResult;
    }

    @Override
    public void onGameStarting() throws Exception {
        if (players == null || players.isEmpty()) {
            throw new InvalidRunnerStateException("No players provided");
        }

        prepareGameMap();
        preparePlayers();

        LOGGER.info("Starting game");

        gameResult = new GameResult();
        gameResult.isComplete = false;
        gameResult.verificationRequired = false;
        gameResult.matchId = gameRunnerConfig.matchId;

        botExecutionContexts = Collections.synchronizedList(new ArrayList<>());

        if (gameRunnerConfig.isTournamentMode) {
            LOGGER.info("Delaying match start for bots to warm up");
            Thread.sleep(5000);
        }
    }

    @Override
    public void onRoundStarting() {
        gameMap.setCurrentRound(gameMap.getCurrentRound() + 1);
        LOGGER.info(ROUND_DELIMITER);
        LOGGER.info("Starting round: {}", gameMap.getCurrentRound());

        roundProcessor = new RunnerRoundProcessor(gameMap, gameRoundProcessor);

        botExecutionContexts.clear();
    }

    @Override
    public void onProcessRound() throws Exception {
        GameMapRenderer consoleRenderer = rendererResolver.resolve(RendererType.CONSOLE);

        for (Player player : players) {
            LOGGER.info("Player {}: Map View ", player.getName());
            LOGGER.info(() -> consoleRenderer.render(gameMap, player.getGamePlayer()));
            Thread thread = new Thread(() -> {
                BasePlayer currentPlayer = (BasePlayer) player;
                BotExecutionContext botExecutionContext = currentPlayer.executeBot(gameMap);

                botExecutionContexts.add(botExecutionContext);
                roundProcessor.addPlayerCommand(player, splitPlayerCommands(botExecutionContext.command));
                referee.trackExecution(player, botExecutionContext);
            });
            thread.start();
            thread.join();
        }
        roundProcessor.processRound();
        players.forEach(p -> p.roundComplete(gameMap, gameMap.getCurrentRound()));
    }

    private List<RawCommand> splitPlayerCommands(String commands) {
        return Arrays.stream(commands.split(COMMAND_DELIMITER))
                .map(String::trim)
                .filter(StringUtils::isNotEmpty)
                .map(RawCommand::new)
                .collect(Collectors.toList());
    }

    @Override
    public void onRoundComplete() {
        LOGGER.info("Completed round: {}", gameMap.getCurrentRound());

        for (BotExecutionContext botExecutionContext : botExecutionContexts) {
            try {
                botExecutionContext.saveRoundStateData(gameRunnerConfig.gameName);
            } catch (Exception e) {
                LOGGER.error("Failed to write round information", e);
            }
        }

        try {
            String roundDirectory = FileUtils.getRoundDirectory(gameMap.getCurrentRound());
            String globalStateFile = String.format("%s/%s/GlobalState.json", gameRunnerConfig.gameName, roundDirectory);

            String globalStateRender = rendererResolver.resolve(RendererType.JSON).render(gameMap, null);
            FileUtils.writeToFile(globalStateFile, globalStateRender);
        } catch (Exception e) {
            LOGGER.error("Failed to write global round information", e);
        }
    }

    @Override
    public void onGameComplete() {
        LOGGER.info(ROUND_DELIMITER);
        LOGGER.info("Game Complete");

        GamePlayer winningPlayer = gameMap.getWinningPlayer();
        BasePlayer winner = players.stream()
                .map(player -> (BasePlayer) player)
                .filter(p -> p.getGamePlayer().equals(winningPlayer))
                .findFirst().orElse(null);

        if (winner != null) {
            gameResult.winner = winner.getPlayerId();
        }

        gameResult.playerAId = gameRunnerConfig.playerAId;
        gameResult.playerBId = gameRunnerConfig.playerBId;

        for (Player player : players) {
            BasePlayer basePlayer = (BasePlayer) player;
            if (basePlayer.getPlayerId().equals(gameRunnerConfig.playerAId)) {
                gameResult.playerOnePoints = basePlayer.getGamePlayer().getScore();
            } else if (basePlayer.getPlayerId().equals(gameRunnerConfig.playerBId)) {
                gameResult.playerTwoPoints = basePlayer.getGamePlayer().getScore();
            }
        }

        gameResult.roundsPlayed = gameMap.getCurrentRound();
        gameResult.isComplete = true;
        gameResult.isSuccessful = true;

        RefereeMessage refereeMessage = referee.isMatchValid(gameMap);
        gameResult.verificationRequired = !refereeMessage.isValid;

        writeEndGameFile(winner, refereeMessage);

        for (Player player : players) {
            player.gameEnded(gameMap);
        }
        LOGGER.info(ROUND_DELIMITER);
    }

    private boolean isGameComplete() throws TimeoutException {
        return gameEngine.isGameComplete(gameMap);
    }

    private void writeEndGameFile(Player winner, RefereeMessage refereeMessage) {
        StringBuilder winnerStringBuilder = new StringBuilder();

        for (Player player : players) {
            winnerStringBuilder.append(player.getName())
                    .append(" - score:")
                    .append(player.getGamePlayer().getScore())
                    .append(" health:")
                    .append(player.getGamePlayer().getHealth())
                    .append("\n");
        }

        if (winner == null) {
            winnerStringBuilder.insert(0, "The game ended in a tie" + "\n\n");
        } else {
            winnerStringBuilder.insert(0, "The winner is: " + winner.getName() + "\n\n");
        }

        LOGGER.info(SECTION_DELIMITER);
        LOGGER.info(winnerStringBuilder);
        LOGGER.info(SECTION_DELIMITER);

        try {
            String roundLocation = String.format("%s/%s/endGameState.txt", gameRunnerConfig.gameName, FileUtils.getRoundDirectory(gameMap.getCurrentRound()));
            File endStateFile = new File(roundLocation);

            if (!endStateFile.getParentFile().exists()) {
                endStateFile.getParentFile().mkdirs();
            }

            if (!endStateFile.exists()) {
                endStateFile.createNewFile();
            }

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(endStateFile))) {

                winnerStringBuilder.insert(0, "Match seed: " + gameRunnerConfig.seed + "\n\n");

                if (!refereeMessage.isValid) {
                    winnerStringBuilder.append(SECTION_DELIMITER + "\n");
                    winnerStringBuilder.append("Referee messages\n");
                    winnerStringBuilder.append(SECTION_DELIMITER + "\n");

                    for (String reason : refereeMessage.reasons) {
                        winnerStringBuilder.append(reason);
                        winnerStringBuilder.append("\n");
                    }
                }

                bufferedWriter.write(winnerStringBuilder.toString());
                bufferedWriter.flush();
            }

        } catch (IOException e) {
            LOGGER.error("Error writing end game file", e);
        }
    }

    private void prepareGameMap() throws InvalidRunnerStateException {

        if (gameMapGenerator == null) {
            throw new InvalidRunnerStateException("No GameMapGenerator instance found");
        }

        if (players == null || players.isEmpty()) {
            throw new InvalidRunnerStateException("No players found");
        }

        gameMap = gameMapGenerator.generateGameMap(players);
    }

    private void preparePlayers() throws Exception {
        for (Player player : players) {
            ((BasePlayer) player).instantiateRenderers(rendererResolver);
            ((BasePlayer) player).gameStarted();
        }
    }

    public static class Builder {

        GameRunnerConfig gameRunnerConfig;
        GameEngine gameEngine;
        GameMapGenerator gameMapGenerator;
        GameRoundProcessor roundProcessor;
        List<Player> players;
        GameReferee referee;
        RendererResolver rendererResolver;

        public Builder setGameRunnerConfig(GameRunnerConfig gameRunnerConfig) {
            this.gameRunnerConfig = gameRunnerConfig;
            return this;
        }

        public Builder setGameEngine(GameEngine gameEngine) {
            this.gameEngine = gameEngine;
            return this;
        }

        public Builder setGameMapGenerator(GameMapGenerator gameMapGenerator) {
            this.gameMapGenerator = gameMapGenerator;
            return this;
        }

        public Builder setRoundProcessor(GameRoundProcessor roundProcessor) {
            this.roundProcessor = roundProcessor;
            return this;
        }

        public Builder setPlayers(List<Player> players) {
            this.players = players;
            return this;
        }

        public Builder setRendererResolver(RendererResolver rendererFactory) {
            this.rendererResolver = rendererFactory;
            return this;
        }

        public Builder setReferee(GameReferee referee) {
            this.referee = referee;
            return this;
        }

        public GameEngineRunner build() {
            GameEngineRunner gameEngineRunner = new GameEngineRunner();

            gameEngineRunner.gameRunnerConfig = gameRunnerConfig;
            gameEngineRunner.gameEngine = gameEngine;
            gameEngineRunner.gameMapGenerator = gameMapGenerator;
            gameEngineRunner.gameRoundProcessor = roundProcessor;
            gameEngineRunner.players = players;
            gameEngineRunner.referee = new RunnerReferee(referee, gameRunnerConfig, players);
            gameEngineRunner.rendererResolver = rendererResolver;

            return gameEngineRunner;
        }
    }
}

