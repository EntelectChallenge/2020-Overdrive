package za.co.entelect.challenge.command;

public class ChangeLaneCommand implements Command {

    private final int lane;

    //Can change "up" or "down" assuming horizontal lane =====
    public ChangeLaneCommand(int lane) {
        this.lane = lane;
    }

    @Override
    public String render() {
        return String.format("change lane %d", lane);
    }
}
