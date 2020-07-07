package za.co.entelect.challenge.botrunners.local;

import za.co.entelect.challenge.config.BotMetadata;

import java.io.IOException;

public class HaskellBotRunner extends LocalBotRunner {

    public HaskellBotRunner(BotMetadata botMetaData, int timeoutInMilliseconds) {
        super(botMetaData, timeoutInMilliseconds);
    }

    @Override
    protected void runBot() throws IOException {
        String runTimeArguments;
        if (this.getArguments() != null) {
            int coreCount = this.getArguments().getCoreCount();
            runTimeArguments = " +RTS -N" + coreCount + " -RTS";
        } else {
            runTimeArguments = "";
        }

        String line;

        if(System.getProperty("os.name").contains("Windows")) {
            line = "cmd /c \"" + this.getBotFileName() + runTimeArguments + "\"";
        } else {
            line = "./" + this.getBotFileName() + runTimeArguments;
        }

        runSimpleCommandLineCommand(line, 0);
    }

}
