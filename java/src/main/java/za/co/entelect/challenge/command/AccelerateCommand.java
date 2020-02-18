package za.co.entelect.challenge.command;

public class AccelerateCommand implements Command {

    private final int speed;

    public AccelerateCommand(int speed) {
        this.speed = speed;
    }

    @Override
    public String render() {
        return String.format("accelerate %d ", speed);
    }
}
