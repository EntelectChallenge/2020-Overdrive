package za.co.entelect.challenge.botrunners.local;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import za.co.entelect.challenge.config.BotMetadata;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PythonBotRunner extends LocalBotRunner {

    private static final Logger log = LogManager.getLogger(PythonBotRunner.class);
    private static final List<String> PYTHON_COMMANDS = Collections.unmodifiableList(Arrays.asList(
            "python3", "py -3", "python", "py"
    ));

    private final String pythonCommand;

    public PythonBotRunner(BotMetadata botMetaData, int timeoutInMilliseconds) {
        super(botMetaData, timeoutInMilliseconds);
        pythonCommand = resolvePythonCommand();
    }

    @Override
    protected void runBot() throws IOException {
        String line = String.format("%s \"%s\"", pythonCommand, this.getBotFileName());
        runSimpleCommandLineCommand(line, 0);
    }

    private String resolvePythonCommand() {

        //We don't need to worry about the output from the following commands. We can safely dispose the output.
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(null);

        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(pumpStreamHandler);

        for (String command : PYTHON_COMMANDS) {
            try {
                CommandLine cmdLine = CommandLine.parse(String.format("%s --version", command));
                executor.execute(cmdLine);

                log.info("Successfully resolved command: {}", command);
                return command;

            } catch (IOException e) {
                log.warn("Failed to resolve command: {}", command);
            }
        }

        throw new IllegalStateException("Failed to resolve python command. Please ensure you have Python 3 installed");
    }
}