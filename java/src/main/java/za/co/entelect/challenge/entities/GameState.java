package za.co.entelect.challenge.entities;

import com.google.gson.annotations.SerializedName;
import za.co.entelect.challenge.enums.PowerUps;

import java.util.List;

public class GameState {

    @SerializedName("currentRound")
    public int currentRound;

    @SerializedName("maxRounds")
    public int maxRounds;

    @SerializedName("player")
    public Car player;

    @SerializedName("opponent")
    public Car opponent;

    @SerializedName("worldMap")
    public List<Lane[]> lanes;

    @SerializedName("powerups")
    public PowerUps[] powerups;

    @SerializedName("boosting")
    public Boolean boosting;

    @SerializedName("boost-counter")
    public int boostCounter;
}
