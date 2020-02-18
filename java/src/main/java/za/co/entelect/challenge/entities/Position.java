package za.co.entelect.challenge.entities;

import com.google.gson.annotations.SerializedName;

public class Position {
    @SerializedName("lane")
    public int lane;

    @SerializedName("blockNumber")
    public int block;
}
