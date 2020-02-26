package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.Object;

import java.util.*;

public class Bot {

    private static final int maxSpeed = 9;
    private List<Integer> directionList = new ArrayList<>();

    private Random random;
    private GameState gameState;
    private Car opponent;
    private Car myCar;

    public Bot(Random random, GameState gameState) {
        this.random = random;
        this.gameState = gameState;
        this.myCar = getMyCar(gameState);

        directionList.add(-1);
        directionList.add(0);
        directionList.add(1);
    }

    private Car getMyCar(GameState gameState) {
        return gameState.player;
    }

    public Command run() {
        Map<Integer, Object> blocks = getBlocksInFront(myCar.position.lane, myCar.position.block, maxSpeed);

        if (blocks.containsValue(Object.MUD)) {
            int i = random.nextInt(directionList.size());
            if (i == 0) {
                return new DecelerateCommand();
            }
            return new ChangeLaneCommand(directionList.get(random.nextInt(directionList.size())));
        }
        return new AccelerateCommand();
    }

    /**
     * Returns map of blocks and the objects in the for the current lanes, returns the amount of blocks that can be
     * traversed at max speed.
     **/
    private Map<Integer, Object> getBlocksInFront(int lane, int block, int maxSpeed) {
        Lane[] lanes = gameState.map;
        Map<Integer, Object> blocks = new Hashtable<Integer, Object>();
        if (lane - 1 == 0) {
            for (int i = block; i <= block + maxSpeed + (26 * (lane - 1)); i++) {
                blocks.put(lanes[i].position.block, lanes[i].object);
            }
        } else if (lane + 1 == 5) {
            for (int i = block + (26 * (lane - 1)); i <= block + maxSpeed + (26 * (lane - 1)); i++) {
                blocks.put(lanes[i].position.block, lanes[i].object);
            }
        } else {
            for (int i = block; i <= block + maxSpeed; i++) {
                blocks.put(lanes[i + (26 * (lane - 1))].position.block, lanes[i + (26 * (lane - 1))].object);
            }
        }

        return blocks;
    }

}
