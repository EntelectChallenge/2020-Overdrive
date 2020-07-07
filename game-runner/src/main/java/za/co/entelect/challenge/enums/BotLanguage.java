package za.co.entelect.challenge.enums;

import com.google.gson.annotations.SerializedName;
import za.co.entelect.challenge.botrunners.local.*;
import za.co.entelect.challenge.config.BotMetadata;

public enum BotLanguage {

    @SerializedName("java")
    JAVA {
        @Override
        public LocalBotRunner createBotRunner(BotMetadata botMetadata, int timeoutInMilliseconds) {
            return new JavaBotRunner(botMetadata, timeoutInMilliseconds);
        }
    },

    @SerializedName("dotnetcore")
    DOTNETCORE {
        @Override
        public LocalBotRunner createBotRunner(BotMetadata botMetadata, int timeoutInMilliseconds) {
            return new DotNetCoreBotRunner(botMetadata, timeoutInMilliseconds);
        }
    },

    @SerializedName("javascript")
    JAVASCRIPT {
        @Override
        public LocalBotRunner createBotRunner(BotMetadata botMetadata, int timeoutInMilliseconds) {
            return new JavaScriptBotRunner(botMetadata, timeoutInMilliseconds);
        }
    },

    @SerializedName("python3")
    PYTHON3 {
        @Override
        public LocalBotRunner createBotRunner(BotMetadata botMetadata, int timeoutInMilliseconds) {
            return new PythonBotRunner(botMetadata, timeoutInMilliseconds);
        }
    },

    @SerializedName("python3-ml")
    PYTHON3_ML {
        @Override
        public LocalBotRunner createBotRunner(BotMetadata botMetadata, int timeoutInMilliseconds) {
            return new PythonBotRunner(botMetadata, timeoutInMilliseconds);
        }
    },

    @SerializedName("cplusplus")
    CPLUSPLUS {
        @Override
        public LocalBotRunner createBotRunner(BotMetadata botMetadata, int timeoutInMilliseconds) {
            return new CPlusPlusBotRunner(botMetadata, timeoutInMilliseconds);
        }
    },

    @SerializedName("haskell")
    HASKELL {
        @Override
        public LocalBotRunner createBotRunner(BotMetadata botMetadata, int timeoutInMilliseconds) {
            return new HaskellBotRunner(botMetadata, timeoutInMilliseconds);
        }
    },

    @SerializedName("scala")
    SCALA {
        @Override
        public LocalBotRunner createBotRunner(BotMetadata botMetadata, int timeoutInMilliseconds) {
            return new ScalaBotRunner(botMetadata, timeoutInMilliseconds);
        }
    },

    @SerializedName("rust")
    RUST {
        @Override
        public LocalBotRunner createBotRunner(BotMetadata botMetadata, int timeoutInMilliseconds) {
            return new RustBotRunner(botMetadata, timeoutInMilliseconds);
        }
    },

    @SerializedName("LISP")
    LISP {
        @Override
        public LocalBotRunner createBotRunner(BotMetadata botMetadata, int timeoutInMilliseconds) {
            return new LISPBotRunner(botMetadata, timeoutInMilliseconds);
        }
    },

    @SerializedName("golang")
    GOLANG {
        @Override
        public LocalBotRunner createBotRunner(BotMetadata botMetadata, int timeoutInMilliseconds) {
            return new GolangBotRunner(botMetadata, timeoutInMilliseconds);
        }
    };

    public abstract LocalBotRunner createBotRunner(BotMetadata botMetadata, int timeoutInMilliseconds);
}
