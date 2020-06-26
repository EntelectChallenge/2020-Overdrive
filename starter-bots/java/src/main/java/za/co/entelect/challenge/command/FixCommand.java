package za.co.entelect.challenge.command;

public class FixCommand implements Command {

    @Override
    public String render() {
        return String.format("FIX");
    }
}
