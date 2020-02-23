package za.co.entelect.challenge.command;

import java.util.HashMap;
import java.util.Map;

public class AccelerateCommand implements Command {

    public AccelerateCommand() {
    }

    @Override
    public String render() {
        return String.format("Accelerate");
    }
}
