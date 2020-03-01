package za.co.entelect.challenge.command;

import za.co.entelect.challenge.enums.Direction;

public class ChangeLaneCommand implements Command {

    private Direction direction;

    public ChangeLaneCommand(int laneIndicator) {
        if (laneIndicator == 1) {
            this.direction = Direction.valueOf("RIGHT");
        } else {
            this.direction = Direction.valueOf("LEFT");
        }
    }

    @Override
    public String render() {
        return String.format("TURN_%s", direction.getLabel());
    }
}
