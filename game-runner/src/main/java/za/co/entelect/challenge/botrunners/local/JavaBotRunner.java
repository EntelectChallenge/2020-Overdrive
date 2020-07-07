package za.co.entelect.challenge.botrunners.local;

import za.co.entelect.challenge.config.BotMetadata;

import java.io.IOException;

public class JavaBotRunner extends LocalBotRunner {
    public JavaBotRunner(BotMetadata botMetaData, int timeoutInMilliseconds) {
        super(botMetaData, timeoutInMilliseconds);
    }

    @Override
    protected void runBot() throws IOException {
        String line = "java -jar \"" + this.getBotFileName() + "\"";
        runSimpleCommandLineCommand(line, 0);
    }

}
