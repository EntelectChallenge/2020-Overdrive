package za.co.entelect.challenge.enums;

import com.google.gson.annotations.SerializedName;

public enum Terrain {
    @SerializedName("0")
    EMPTY,
    @SerializedName("1")
    MUD,
    @SerializedName("2")
    OIL_SPILL,
    @SerializedName("3")
    OIL_POWER,
    @SerializedName("4")
    FINISH,
    @SerializedName("5")
    BOOST,
    @SerializedName("6")
    WALL,
    @SerializedName("7")
    LIZARD,
    @SerializedName("8")
    TWEET,
    @SerializedName("9")
    EMP
}