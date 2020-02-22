package za.co.entelect.challenge.game.contracts.game

import java.util

import za.co.entelect.challenge.game.contracts.map.GameMap;
import za.co.entelect.challenge.game.contracts.map.CarGameMap;
import za.co.entelect.challenge.game.contracts.map.Block;
import za.co.entelect.challenge.game.contracts.map.BlockPosition;
import za.co.entelect.challenge.game.contracts.map.BlockObjectEnum;

import za.co.entelect.challenge.game.contracts.player.Player

class CarMapGenerator extends GameMapGenerator {
    override def generateGameMap(players: util.List[Player]): GameMap = {
        val registeredPlayers = registerGamePlayers(players);

        val mapGenerationSeed = generateSeedForMapGeneration();
        val lanes = 4;
        val trackLength = 1500;
        val intialisationRound = 0;
        
        val randomNumberGenerator = new scala.util.Random(mapGenerationSeed);
        val blocks = generateBlocks(lanes, trackLength, randomNumberGenerator);

        val carGameMap = new CarGameMap(
            players, 
            mapGenerationSeed, 
            lanes, 
            trackLength, 
            blocks, 
            intialisationRound
        );
        return carGameMap;
    }

    def registerGamePlayers(players: util.List[Player]): util.List[Player] = {
        val defaultHealth = 0;
        val defaultScore = 0;
        players.forEach(x => x.playerRegistered(new CarGamePlayer(defaultHealth,defaultScore)));
        return players;
    }

    def generateSeedForMapGeneration(): Int = {
        val r = scala.util.Random;
        val randomInteger = r.nextInt(100);
        return randomInteger;
    }

    def generateBlocks(lanes: Int, trackLength: Int, randomNumberGenerator: scala.util.Random): Array[Block] = {
        var blocks = new Array[Block](lanes*trackLength);
        val blockObjectCreator = new BlockObjectEnum;

        for ( i <- 0 to (blocks.length - 1)) {
            val lane = i/trackLength + 1;
            val blockNumber = i%trackLength + 1;
            val position = new BlockPosition(lane, blockNumber);

            var generatedMapObject = blockObjectCreator.empty();
            val startingBlockForGeneratedMapObjects = 6;
            val finalBlockForGeneratedMapObject = trackLength;
            if(blockNumber >= startingBlockForGeneratedMapObjects && blockNumber > finalBlockForGeneratedMapObject) {
                generatedMapObject = blockObjectCreator.generateMapObject(randomNumberGenerator);
            } else if (blockNumber == finalBlockForGeneratedMapObject) {
                generatedMapObject = blockObjectCreator.finishLine();
            }

            val emptyPlayer = 0;
            var playerOccupyingBlock = emptyPlayer;
            if(lane == 1 && blockNumber == 1) {
                playerOccupyingBlock = 1;
            } else if (lane == 3 && blockNumber == 1) {
                playerOccupyingBlock = 2;
            }
            blocks(i) = new Block(position, generatedMapObject, playerOccupyingBlock);
        }

        return blocks;
    }
}
