package za.co.entelect.challenge.entities;

import com.google.gson.annotations.SerializedName;
import za.co.entelect.challenge.enums.State;

public class Car {
    @SerializedName("id")
    public int id;

    @SerializedName("position")
    public Position position;

    @SerializedName("speed")
    public int speed;

    @SerializedName("state")
    public State state;
}
