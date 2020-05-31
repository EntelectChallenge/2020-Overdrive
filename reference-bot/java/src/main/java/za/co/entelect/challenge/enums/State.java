package za.co.entelect.challenge.enums;

import com.google.gson.annotations.SerializedName;

public enum State {
    @SerializedName("ACCELERATING")
    ACCELERATING,
    @SerializedName("READY")
    READY,
    @SerializedName("NOTHING")
    NOTHING,
    @SerializedName("TURNING_RIGHT")
    TURNING_RIGHT,
    @SerializedName("TURNING_LEFT")
    TURNING_LEFT,
    @SerializedName("HIT_MUD")
    HIT_MUD,
    @SerializedName("HIT_OIL")
    HIT_OIL,
    @SerializedName("DECELERATING")
    DECELERATING,
    @SerializedName("PICKED_UP_POWERUP")
    PICKED_UP_POWERUP,
    @SerializedName("USED_BOOST")
    USED_BOOST,
    @SerializedName("USED_OIL")
    USED_OIL,
    @SerializedName("USED_LIZARD")
    USED_LIZARD,
    @SerializedName("USED_TWEET")
    USED_TWEET,
    @SerializedName("HIT_WALL")
    HIT_WALL,
    @SerializedName("HIT_CYBER_TRUCK")
    HIT_CYBER_TRUCK,
    @SerializedName("FINISHED")
    FINISHED
}



