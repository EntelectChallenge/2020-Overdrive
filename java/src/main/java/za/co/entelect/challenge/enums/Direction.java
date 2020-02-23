package za.co.entelect.challenge.enums;

public enum Direction {

    Front(0, 1),
    Back(0,-1),
    Left(-1, 0),
    Right(1,0);

    public final int lane;
    public final int block;

    Direction(int lane, int block) {
        this.lane = lane;
        this.block = block;
    }
}
