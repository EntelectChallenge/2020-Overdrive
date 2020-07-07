package za.co.entelect.challenge.botrunners.local;

import za.co.entelect.challenge.config.BotMetadata;

import java.io.IOException;

public class DotNetCoreBotRunner extends LocalBotRunner {

    public DotNetCoreBotRunner(BotMetadata botMetaData, int timeoutInMilliseconds) {
        super(botMetaData, timeoutInMilliseconds);
    }

    @Override
    protected void runBot() throws IOException {
        String line = "dotnet " + this.getBotFileName();
        runSimpleCommandLineCommand(line, 0);
    }

}
