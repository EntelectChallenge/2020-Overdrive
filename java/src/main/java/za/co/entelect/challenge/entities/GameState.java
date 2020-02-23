package za.co.entelect.challenge.entities;

import com.google.gson.annotations.SerializedName;

public class GameState {

    @SerializedName("currentRound")
    public int currentRound;

    @SerializedName("maxRounds")
    public int maxRounds;

    @SerializedName("player")
    public Car player;

    @SerializedName("lanes")
    public Lane[] map;
}
