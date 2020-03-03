package za.co.entelect.challenge.command;

public class AccelerateCommand implements Command {

    @Override
    public String render() {
        return String.format("ACCELERATE");
    }
}
