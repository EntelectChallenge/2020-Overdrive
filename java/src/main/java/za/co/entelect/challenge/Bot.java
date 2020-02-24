package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;

import java.util.*;

public class Bot {

    private Random random;
    private GameState gameState;
    private Car opponent;
    private Car myCar;

    public Bot(Random random, GameState gameState) {
        this.random = random;
        this.gameState = gameState;
        this.myCar = getMyCar(gameState);
    }

    private Car getMyCar(GameState gameState) {
        return gameState.player;
    }

    public Command run() {
        return new ChangeLaneCommand(1);
    }

    //TODO very convoluted, need to refactor
    private List<Lane> getNextStepBlocks(int lane, int block, int maxSpeed) {
        ArrayList<Lane> blocks = new ArrayList<>();
        if (lane - 1 == 0) {
            for (int l = lane; l <= lane + 1; l++) {
                for (int i = block; i <= block + maxSpeed + (26 * (lane - 1)); i++) {
                    blocks.add(gameState.map[i + (26 * (l - 1))]);
                }
            }
        } else if (lane + 1 == 5) {
            for (int l = lane - 1; l <= lane; l++) {
                for (int i = block + (26 * (lane - 2)); i <= block + maxSpeed + (26 * (lane - 2)); i++) {
                    blocks.add(gameState.map[i + (26 * (l - 3))]);
                }
            }
        } else {
            for (int l = lane - 1; l <= lane + 1; l++) {
                for (int i = block; i <= block + maxSpeed; i++) {
                    blocks.add(gameState.map[i + (26 * (l - 1))]);
                }
            }
        }

        return blocks;
    }

}
