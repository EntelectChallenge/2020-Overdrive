package za.co.entelect.challenge.botrunners;

import java.io.IOException;

import za.co.entelect.challenge.config.BotMetaData;

public class GolangBotRunner extends BotRunner {

    public GolangBotRunner(BotMetaData botMetaData, int timoutInMilis) {
        super(botMetaData, timoutInMilis);
    }

    @Override
    protected void runBot() throws IOException {
        String line = "\"" + this.getBotDirectory() + "/" + this.getBotFileName() + "\"";
        runSimpleCommandLineCommand(line, 0);
    }

}
