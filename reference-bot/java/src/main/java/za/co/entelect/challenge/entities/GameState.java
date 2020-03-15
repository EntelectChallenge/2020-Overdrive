package za.co.entelect.challenge.entities;

import com.google.gson.annotations.SerializedName;
import za.co.entelect.challenge.enums.PowerUps;
import za.co.entelect.challenge.enums.State;

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

}
