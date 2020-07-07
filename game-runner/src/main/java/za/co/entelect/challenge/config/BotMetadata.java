package za.co.entelect.challenge.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import za.co.entelect.challenge.enums.BotLanguage;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class BotMetadata {

    @SerializedName("author")
    private String author;

    @SerializedName("email")
    private String email;

    @SerializedName("nickName")
    private String nickName;

    @SerializedName("botLanguage")
    private BotLanguage botLanguage;

    @SerializedName("botLocation")
    private String botLocation;

    @SerializedName("botFileName")
    private String botFileName;

    @SerializedName("arguments")
    private BotArguments arguments;

    public BotMetadata(BotLanguage language, String botLocation, String botFileName) {
        this.botLanguage = language;
        this.botLocation = botLocation;
        this.botFileName = botFileName;
    }

    public String getAuthor() {
        return author;
    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickName;
    }

    public String getBotLocation() {
        return this.botLocation;
    }

    public void setRelativeBotLocation(String relativeLocation) {
        this.botLocation = relativeLocation + this.botLocation;
    }

    public String getBotFileName() {
        return this.botFileName;
    }

    public BotLanguage getBotLanguage() {
        return this.botLanguage;
    }

    public String getBotDirectory() {
        return Paths.get(getBotLocation()).toAbsolutePath().normalize().toString();
    }

    public BotArguments getArguments() {
        return this.arguments;
    }

    public static BotMetadata load(String botLocation) throws IOException {
        Path botMetaPath;

        try (final Stream<Path> files = Files.walk(Paths.get(botLocation))) {
            botMetaPath = files.filter(path -> path.endsWith("bot.json"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Failed to find bot meta data from location: " + botLocation));
        }

        try (FileReader fileReader = new FileReader(botMetaPath.toFile())) {
            Gson gson = new GsonBuilder().create();

            BotMetadata botMeta = gson.fromJson(fileReader, BotMetadata.class);
            botMeta.setRelativeBotLocation(botLocation);

            return botMeta;
        }
    }
}
