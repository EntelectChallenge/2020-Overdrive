package za.co.entelect.challenge.botrunners.local;

import za.co.entelect.challenge.config.BotMetadata;
import za.co.entelect.challenge.engine.exceptions.InvalidRunnerStateException;
import za.co.entelect.challenge.enums.BotLanguage;

public class BotRunnerFactory {
    public static LocalBotRunner createBotRunner(BotMetadata botMetaData, int timeoutInMilliseconds) {
        BotLanguage botLanguage = botMetaData.getBotLanguage();

        if (botLanguage == null) {
            throw new InvalidRunnerStateException("Invalid bot language");
        }

        return botLanguage.createBotRunner(botMetaData, timeoutInMilliseconds);
    }
}
