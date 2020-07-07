package za.co.entelect.challenge.engine.runner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import za.co.entelect.challenge.engine.exceptions.InvalidOperationException;
import za.co.entelect.challenge.game.contracts.command.RawCommand;
import za.co.entelect.challenge.game.contracts.game.GamePlayer;
import za.co.entelect.challenge.game.contracts.game.GameRoundProcessor;
import za.co.entelect.challenge.game.contracts.map.GameMap;
import za.co.entelect.challenge.game.contracts.player.Player;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RunnerRoundProcessor {
    private static final Logger log = LogManager.getLogger(RunnerRoundProcessor.class);

    private final GameMap gameMap;
    private final GameRoundProcessor gameRoundProcessor;

    private boolean roundProcessed;
    private final Map<GamePlayer, List<RawCommand>> commandsToProcess;

    RunnerRoundProcessor(GameMap gameMap, GameRoundProcessor gameRoundProcessor) {
        this.gameMap = gameMap;
        this.gameRoundProcessor = gameRoundProcessor;

        commandsToProcess = new ConcurrentHashMap<>();
    }

    public void processRound() throws InvalidOperationException {
        if (roundProcessed) {
            throw new InvalidOperationException("This round has already been processed!");
        }

        gameRoundProcessor.processRound(gameMap, commandsToProcess);

        List<String> errorList = gameRoundProcessor.getErrorList(gameMap);
        for (String error : errorList) {
            log.error(error);
        }

        roundProcessed = true;
    }

    public void addPlayerCommand(Player player, List<RawCommand> command) {
        final List<RawCommand> previousValue = commandsToProcess.putIfAbsent(player.getGamePlayer(), command);
        if (previousValue != null) {
            log.error("Player already has a command registered for this round, wait for the next round before sending a new command");
        }
    }
}
