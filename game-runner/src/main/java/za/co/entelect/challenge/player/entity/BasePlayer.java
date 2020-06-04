package za.co.entelect.challenge.player.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import za.co.entelect.challenge.game.contracts.map.GameMap;
import za.co.entelect.challenge.game.contracts.player.Player;
import za.co.entelect.challenge.game.contracts.renderer.GameMapRenderer;
import za.co.entelect.challenge.game.contracts.renderer.RendererType;
import za.co.entelect.challenge.renderer.RendererResolver;

public abstract class BasePlayer extends Player {

    private static final Logger LOGGER = LogManager.getLogger(BasePlayer.class);
    protected static final String NO_COMMAND = "No Command";

    private String playerId;

    protected GameMapRenderer jsonRenderer;
    protected GameMapRenderer textRenderer;
    protected GameMapRenderer consoleRenderer;
    protected GameMapRenderer csvRenderer;

    public BasePlayer(String name) {
        super(name);
    }

    public void instantiateRenderers(RendererResolver rendererResolver) {
        jsonRenderer = rendererResolver.resolve(RendererType.JSON);
        textRenderer = rendererResolver.resolve(RendererType.TEXT);
        consoleRenderer = rendererResolver.resolve(RendererType.CONSOLE);
        csvRenderer = rendererResolver.resolve(RendererType.CSV);
    }

    public void gameStarted() throws Exception {

    }

    @Override
    public void startGame(GameMap gameMap) {
        newRoundStarted(gameMap);
    }

    @Override
    public void newRoundStarted(GameMap gameMap) {
//        @TODO Change the lifecycle in the interfaces
    }

    @Override
    public void gameEnded(GameMap gameMap) {

    }
    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public abstract void setExecutionResult(BotExecutionContext botExecutionContext) throws Exception;

    public BotExecutionContext executeBot(GameMap gameMap) {

        BotExecutionContext botExecutionContext = new BotExecutionContext();

        botExecutionContext.name = getName();
        botExecutionContext.jsonState = jsonRenderer.render(gameMap, getGamePlayer());
        botExecutionContext.textState = textRenderer.render(gameMap, getGamePlayer());
        botExecutionContext.consoleState = consoleRenderer.render(gameMap, getGamePlayer());
        botExecutionContext.csvState = csvRenderer.render(gameMap, getGamePlayer());
        botExecutionContext.round = gameMap.getCurrentRound();

        try {
            // Get a command from the bot
            // The manner in which we get the command will depend
            // on the subclasses i.e Console, File, etc.
            setExecutionResult(botExecutionContext);
            if (botExecutionContext.command == null || botExecutionContext.command.isEmpty()) {
                LOGGER.warn("Player {} - No command provided, falling back to default no command", getName());
                botExecutionContext.command = NO_COMMAND;
            }
        } catch (Exception e) {
            LOGGER.error("Player {} - Exception during bot execution, falling back to default no command", getName(), e);
            botExecutionContext.command = NO_COMMAND;
        }

        return botExecutionContext;
    }
}
