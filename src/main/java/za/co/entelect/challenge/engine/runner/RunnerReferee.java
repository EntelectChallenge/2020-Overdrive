package za.co.entelect.challenge.engine.runner;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import za.co.entelect.challenge.config.GameRunnerConfig;
import za.co.entelect.challenge.game.contracts.common.RefereeMessage;
import za.co.entelect.challenge.game.contracts.game.GameReferee;
import za.co.entelect.challenge.game.contracts.map.GameMap;
import za.co.entelect.challenge.game.contracts.player.Player;
import za.co.entelect.challenge.player.entity.BotExecutionContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunnerReferee implements GameReferee {

    private static final Logger LOGGER = LogManager.getLogger(RunnerReferee.class);

    private final GameReferee gameEngineReferee;
    private final GameRunnerConfig gameRunnerConfig;

    private final Map<Player, List<Integer>> playerTimeouts;
    private final Map<Player, List<Integer>> playerExceptions;

    RunnerReferee(GameReferee gameEngineReferee, GameRunnerConfig gameRunnerConfig, List<Player> players) {
        this.gameEngineReferee = gameEngineReferee;
        this.gameRunnerConfig = gameRunnerConfig;

        this.playerTimeouts = new HashMap<>();
        this.playerExceptions = new HashMap<>();

        for (Player player : players) {
            this.playerTimeouts.put(player, new ArrayList<>());
            this.playerExceptions.put(player, new ArrayList<>());
        }
    }

    @Override
    public RefereeMessage isMatchValid(GameMap gameMap) {
        LOGGER.info("Checking if match is valid");

        List<String> runnerErrorList = new ArrayList<>();
        for (Map.Entry<Player, List<Integer>> entry : playerTimeouts.entrySet()) {
            Player basePlayer = entry.getKey();
            List<Integer> timeouts = entry.getValue();

            // Compare the current round to the previous
            for (int i = 1; i < timeouts.size(); i++) {
                int previousRound = timeouts.get(i - 1);
                int currentRound = timeouts.get(i);
                if (previousRound == currentRound - 1) {
                    final String errorMessage = String.format("Player %s -> Consecutive timeouts on rounds %d and %d", basePlayer.getName(), previousRound, currentRound);
                    LOGGER.warn(errorMessage);
                    runnerErrorList.add(errorMessage);
                }
            }
        }

        for (Map.Entry<Player, List<Integer>> entry : playerExceptions.entrySet()) {
            Player player = entry.getKey();
            List<Integer> exceptions = entry.getValue();

            // Compare the current round to the previous
            for (int i = 1; i < exceptions.size(); i++) {
                int previousRound = exceptions.get(i - 1);
                int currentRound = exceptions.get(i);
                if (previousRound == currentRound - 1) {
                    final String errorMessage = String.format("Player %s -> Consecutive exceptions on rounds %d and %d", player.getName(), previousRound, currentRound);
                    LOGGER.warn(errorMessage);
                    runnerErrorList.add(errorMessage);
                }
            }
        }

        RefereeMessage gameEngineCheck = gameEngineReferee.isMatchValid(gameMap);
        gameEngineCheck.reasons.forEach(LOGGER::warn);

        final boolean isValidOverall = gameEngineCheck.isValid && runnerErrorList.isEmpty();
        List<String> overallErrorList = new ArrayList<>(gameEngineCheck.reasons);
        overallErrorList.addAll(runnerErrorList);

        return new RefereeMessage(isValidOverall, overallErrorList);
    }

    void trackExecution(Player player, BotExecutionContext botExecutionContext) {
        trackTimeouts(player, botExecutionContext);
        trackExceptions(player, botExecutionContext);
    }

    private void trackTimeouts(Player player, BotExecutionContext botExecutionContext) {
        if (botExecutionContext.executionTime >= gameRunnerConfig.maximumBotRuntimeMilliSeconds) {
            List<Integer> timeouts = playerTimeouts.get(player);
            timeouts.add(botExecutionContext.round);
        }
    }

    private void trackExceptions(Player player, BotExecutionContext botExecutionContext) {
        if (!StringUtils.isEmpty(botExecutionContext.exception)) {
            List<Integer> exceptions = playerExceptions.get(player);
            exceptions.add(botExecutionContext.round);
        }
    }
}
