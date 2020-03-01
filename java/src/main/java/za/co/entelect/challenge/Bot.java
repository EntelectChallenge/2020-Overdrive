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
        directionList.add(1);
    }

    private Car getMyCar(GameState gameState) {
        return gameState.player;
    }

    public Command run() {
        List<Object> blocks = getBlocksInFront(myCar.position.lane, myCar.position.block);
        if (blocks.contains(Object.MUD)) {
            int i = random.nextInt(directionList.size());
            return new ChangeLaneCommand(directionList.get(i));
        }
        return new AccelerateCommand();
    }

    private HashMap<Integer, Lane[]> getListMapStructure() {
        int mapWidth = Arrays.stream(gameState.map)
                .map(b -> b.position.block)
                .mapToInt(v -> v)
                .max().orElse(0);
        int mapHeight = Arrays.stream(gameState.map)
                .map(b -> b.position.lane)
                .mapToInt(v -> v)
                .max().orElse(0);

        mapWidth = mapWidth - gameState.map[0].position.block;

        HashMap<Integer, Lane[]> map = new HashMap<>();

        for (int lane = 1; lane <= mapHeight; lane++) {
            Lane[] blocks = new Lane[mapWidth +2];
            for (int block = 0; block <= mapWidth; block++) {
                blocks[block] = (gameState.map[((lane - 1) * mapWidth) + block]);
            }
            map.put(lane, blocks);
        }

        return map;
    }

    /**
     * Returns map of blocks and the objects in the for the current lanes, returns the amount of blocks that can be
     * traversed at max speed.
     **/
    private List<Object> getBlocksInFront(int lane, int block) {
        HashMap<Integer, Lane[]> map = getListMapStructure();
        List<Object> blocks = new ArrayList<> ();
        int startBlock = gameState.map[0].position.block;

        Lane[] laneList = map.get(lane);
        for (int i = block - startBlock; i <= block - startBlock + Bot.maxSpeed; i++) {
            if (laneList[i] == null) {
                break;
            }
            blocks.add(laneList[i].object);

        }
        return blocks;
    }

}
