package za.co.entelect.challenge.game.contracts.game

import java.util

import za.co.entelect.challenge.game.contracts.map.GameMap;
import za.co.entelect.challenge.game.contracts.map.CarGameMap;
import za.co.entelect.challenge.game.contracts.map.Block;
import za.co.entelect.challenge.game.contracts.map.BlockPosition;
import za.co.entelect.challenge.game.contracts.map.BlockObjectEnum;

import za.co.entelect.challenge.game.contracts.player.Player

class CarMapGenerator(seed: Int) extends GameMapGenerator {
    override def generateGameMap(players: util.List[Player]): GameMap = {
        val registeredPlayers = registerGamePlayers(players);

        val mapGenerationSeed = seed;
        val lanes = 4;
        val trackLength = 1500;
        val intialisationRound = 0;
        
        val randomNumberGenerator = new scala.util.Random(mapGenerationSeed);
        val blocks = generateBlocks(lanes, trackLength, randomNumberGenerator, players);

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
        val initialSpeed = 5;
        val initialState = "ready";

        for (i <- 0 to (players.size() - 1)) {
            val currentPlayer = players.get(i);
            val playerId = i + 1;
            val newGamePlayer = new CarGamePlayer(defaultHealth, defaultScore, playerId, initialSpeed, initialState); 
            currentPlayer.playerRegistered(newGamePlayer);
        }

        return players;
    }

    def generateBlocks(lanes: Int, trackLength: Int, randomNumberGenerator: scala.util.Random, players: util.List[Player]): Array[Block] = {
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
            var idOfPlayerOccupyingBlock = emptyPlayer;
            if(lane == 1 && blockNumber == 1) {
                val firstPlayer = players.get(0).getGamePlayer().asInstanceOf[CarGamePlayer];
                idOfPlayerOccupyingBlock = firstPlayer.getGamePlayerId();
            } else if (lane == 3 && blockNumber == 1) {
                val secondPlayer = players.get(1).getGamePlayer().asInstanceOf[CarGamePlayer];
                idOfPlayerOccupyingBlock = secondPlayer.getGamePlayerId();
            }
            blocks(i) = new Block(position, generatedMapObject, idOfPlayerOccupyingBlock);
        }

        return blocks;
    }
}
