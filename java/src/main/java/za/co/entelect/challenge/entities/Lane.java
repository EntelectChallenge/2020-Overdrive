package za.co.entelect.challenge.entities;

import com.google.gson.annotations.SerializedName;
import za.co.entelect.challenge.enums.Terrain;

public class Lane {
    @SerializedName("position")
    public Position position;

    @SerializedName("object")
    public Terrain terrain;

    @SerializedName("occupied-by-player-with-id")
    public int occupiedByPlayerId;
}
