package za.co.entelect.challenge.player;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import za.co.entelect.challenge.botrunners.local.LocalBotRunner;
import za.co.entelect.challenge.game.contracts.map.GameMap;
import za.co.entelect.challenge.player.entity.BasePlayer;
import za.co.entelect.challenge.player.entity.BotExecutionContext;
import za.co.entelect.challenge.utils.FileUtils;

import java.io.IOException;

public class BotPlayer extends BasePlayer {

    private static final String BOT_STATE = "state.json";
    private static final String TEXT_MAP = "textMap.txt";

    private final LocalBotRunner botRunner;
    private final StopWatch stopWatch;

    private static final Logger log = LogManager.getLogger(BotPlayer.class);

    public BotPlayer(String name, LocalBotRunner botRunner) {
        super(name);

        this.botRunner = botRunner;
        this.stopWatch = new StopWatch();
    }

    @Override
    public void gameStarted() throws Exception {
        super.gameStarted();
        try {
            botRunner.run();
        } catch (IOException e) {
            log.error("Failed to start bot process", e);
            throw e;
        }
    }

    @Override
    public void startGame(GameMap gameMap) {
        newRoundStarted(gameMap);
    }

    @Override
    public void setExecutionResult(BotExecutionContext botExecutionContext) throws Exception {

        FileUtils.writeToFile(String.format("%s/rounds/%d/%s", botRunner.getBotDirectory(), botExecutionContext.round, BOT_STATE), botExecutionContext.jsonState);
        FileUtils.writeToFile(String.format("%s/rounds/%d/%s", botRunner.getBotDirectory(), botExecutionContext.round, TEXT_MAP), botExecutionContext.textState);

        botRunner.newRound(botExecutionContext.round);

        stopWatch.reset();
        stopWatch.start();

        botExecutionContext.command = botRunner.getLastCommand();
        stopWatch.stop();

        botExecutionContext.exception = botRunner.getLastError();
        botExecutionContext.executionTime = stopWatch.getTime();
    }

    @Override
    public void gameEnded(GameMap gameMap) {
        botRunner.shutdown();
    }
}
