package za.co.entelect.challenge.botrunners.local;

import za.co.entelect.challenge.config.BotMetadata;

import java.io.IOException;

public class GolangBotRunner extends LocalBotRunner {

    public GolangBotRunner(BotMetadata botMetaData, int timoutInMilis) {
        super(botMetaData, timoutInMilis);
    }

    @Override
    protected void runBot() throws IOException {
        String line = "\"" + this.getBotDirectory() + "/" + this.getBotFileName() + "\"";
        runSimpleCommandLineCommand(line, 0);
    }

}
