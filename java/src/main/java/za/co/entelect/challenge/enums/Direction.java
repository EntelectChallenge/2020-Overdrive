package za.co.entelect.challenge.enums;

public enum Direction {

    Forward(0, 1, "Forward"),
    Backward(0, -1, "Backward"),
    Left(-1, 0, "Left"),
    Right(1, 0, "Right");

    public final int lane;
    public final int block;
    public final String label;

    Direction(int lane, int block, String label) {
        this.lane = lane;
        this.block = block;
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

}
