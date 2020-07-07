package za.co.entelect.challenge.botrunners.local.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Scanner;

public class BotErrorHandler implements Runnable {

    private static final Logger log = LogManager.getLogger(BotErrorHandler.class);

    private final InputStream botErrorStream;
    private String lastErrorMessage;

    public BotErrorHandler(InputStream botErrorStream) {
        this.botErrorStream = botErrorStream;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(botErrorStream);

        while (scanner.hasNextLine()) {

            lastErrorMessage = scanner.nextLine();
            log.error(lastErrorMessage);
        }
    }

    public String getLastError() {
        return lastErrorMessage;
    }
}
