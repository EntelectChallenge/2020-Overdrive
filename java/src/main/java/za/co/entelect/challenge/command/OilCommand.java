package za.co.entelect.challenge.command;

public class OilCommand implements Command {

    @Override
    public String render() {
        return String.format("USE_OIL");
    }
}
