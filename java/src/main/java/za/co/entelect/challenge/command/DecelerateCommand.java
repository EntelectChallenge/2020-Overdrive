package za.co.entelect.challenge.command;

public class DecelerateCommand implements Command {

    private final int speed;

    public DecelerateCommand(int speed) {
        this.speed = speed;
    }

    @Override
    public String render() {
        return String.format("decelerate %d ", speed);
    }
}
