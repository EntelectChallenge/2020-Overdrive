package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.Terrain;

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
        this.myCar = gameState.player;
        this.opponent = gameState.opponent;

        directionList.add(-1);
        directionList.add(1);
    }

    public Command run() {
        List<Object> blocks = getBlocksInFront(myCar.position.lane, myCar.position.block);
        if (blocks.contains(Terrain.MUD)) {
            int i = random.nextInt(directionList.size());
            return new ChangeLaneCommand(directionList.get(i));
        }
        return new AccelerateCommand();
    }

    /**
     * Returns map of blocks and the objects in the for the current lanes, returns the amount of blocks that can be
     * traversed at max speed.
     **/
    private List<Object> getBlocksInFront(int lane, int block) {
        List<Lane[]> map = gameState.lanes;
        List<Object> blocks = new ArrayList<> ();
        int startBlock = map.get(0)[0].position.block;

        Lane[] laneList = map.get(lane-1);
        for (int i = block - startBlock; i <= block - startBlock + Bot.maxSpeed; i++) {
            if (laneList[i] == null) {
                break;
            }
            blocks.add(laneList[i].terrain);

        }
        return blocks;
    }

}
