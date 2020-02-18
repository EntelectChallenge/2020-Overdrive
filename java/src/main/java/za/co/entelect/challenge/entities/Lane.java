package za.co.entelect.challenge.entities;

import com.google.gson.annotations.SerializedName;
import za.co.entelect.challenge.enums.Object;

public class Lane {
    @SerializedName("position")
    public Position position;

    @SerializedName("object")
    public Object object;

    @SerializedName("occupied-by-player-with-id")
    public int occupiedByPlayerId;
}
