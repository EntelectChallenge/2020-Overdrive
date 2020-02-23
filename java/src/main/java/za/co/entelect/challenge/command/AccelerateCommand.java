package za.co.entelect.challenge.command;

import java.util.HashMap;
import java.util.Map;

public class AccelerateCommand implements Command {

    private static final Map<Integer, Integer> speeds = new HashMap<>();
    static {
        speeds.put(3, 5);
        speeds.put(5, 7);
        speeds.put(7, 8);
        speeds.put(8, 9);
    }

    private int speed;

    public AccelerateCommand(int speed) {
        this.speed = speed;
        accelerate(speed);
    }

    @Override
    public String render() {
        return String.format("Accelerate");
    }

    private void accelerate(int intialSpeed) {
        speed = speeds.get(intialSpeed);
    }
}
