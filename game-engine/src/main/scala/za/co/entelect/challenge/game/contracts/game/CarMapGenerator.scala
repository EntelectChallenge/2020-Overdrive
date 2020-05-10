package za.co.entelect.challenge.game.contracts.game

import java.util

import za.co.entelect.challenge.game.contracts.map.GameMap
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.map.Block
import za.co.entelect.challenge.game.contracts.map.BlockPosition
import za.co.entelect.challenge.game.contracts.map.BlockObjectCreator
import za.co.entelect.challenge.game.contracts.player.Player
import za.co.entelect.challenge.game.contracts.Config.Config

class CarMapGenerator(seed: Int) extends GameMapGenerator {

    override def generateGameMap(players: util.List[Player]): GameMap = {

        val registeredPlayers = registerGamePlayers(players);

        val intialisationRound = 0;
        
        val randomNumberGenerator = new scala.util.Random(seed);
        val blocks = generateBlocks(Config.NUMBER_OF_LANES, Config.TRACK_LENGTH, randomNumberGenerator);

        val carGameMap = new CarGameMap(
            players, 
            seed, 
            Config.NUMBER_OF_LANES, 
            Config.TRACK_LENGTH, 
            blocks, 
            intialisationRound
        );

        return carGameMap;
    }

    def registerGamePlayers(players: util.List[Player]): util.List[Player] = {
        val playerSortMode = Config.RACER_PLACEMENT_SORTING;
        var sortedPlayers = new Array[Player](players.size());
        for (i <- 0 to (players.size() - 1)) {
            sortedPlayers(i) = players.get(i);
        }

        playerSortMode match {
            case -1  => sortedPlayers
            case 0 => sortedPlayers = sortedPlayers.sortBy(x => x.getName().split("-")(1));
            case 1 => sortedPlayers = sortedPlayers.sortBy(x => x.getName().split("-")(1)).reverse;
            case invalidSortMode  => throw new Exception("Invalid racer placement sort mode: " + invalidSortMode.toString())
        }

        for (i <- 0 to (sortedPlayers.length - 1)) {
            val currentPlayer = sortedPlayers(i);
            val playerId = i + 1;
            val newGamePlayer = new CarGamePlayer(Config.DEFAULT_HEALTH, Config.DEFAULT_SCORE, playerId, Config.INITIAL_SPEED); 
            newGamePlayer.getReady();
            currentPlayer.playerRegistered(newGamePlayer);
        }

        return players;
    }

    def generateBlocks(lanes: Int, trackLength: Int, randomNumberGenerator: scala.util.Random): Array[Block] = {
        var blocks = new Array[Block](lanes*trackLength);
        val blockObjectCreator = new BlockObjectCreator;

        for ( i <- 0 to (blocks.length - 1)) {
            val lane = i/trackLength + 1;
            val blockNumber = i%trackLength + 1;
            val position = new BlockPosition(lane, blockNumber);

            var generatedMapObject = Config.EMPTY_MAP_OBJECT;
            val finalBlockForGeneratedMapObject = trackLength;
            if(blockNumber >= Config.STARTING_BLOCK_FOR_GENERATED_MAP_OBJECTS && blockNumber < Config.TRACK_LENGTH) {
                generatedMapObject = blockObjectCreator.generateMapObject(randomNumberGenerator);
            } else if (blockNumber == finalBlockForGeneratedMapObject) {
                generatedMapObject = Config.FINISH_LINE_MAP_OBJECT;
            }
           
            blocks(i) = new Block(position, generatedMapObject, Config.EMPTY_PLAYER);
        }

        setPlayerOneStartPosition(blocks);
        setPlayerTwoStartPosition(blocks);

        return blocks;
    }

    private def setPlayerOneStartPosition(blocks: Array[Block]) = {
        setStartPosition(blocks, Config.PLAYER_ONE_START_LANE, Config.PLAYER_ONE_START_BLOCK, 1);
    }

    private def setPlayerTwoStartPosition(blocks: Array[Block]) = {
        setStartPosition(blocks, Config.PLAYER_TWO_START_LANE, Config.PLAYER_TWO_START_BLOCK, 2);
    }

    private def setStartPosition(blocks: Array[Block], startLane: Int, startBlock: Int, playerId: Int) = {
        var playerStartBlock = blocks.find(block => block.getPosition().getLane() == startLane && block.getPosition().getBlockNumber() == startBlock);
        if (playerStartBlock.isEmpty) 
        {
            throw new Exception("Failed to initialise map: " + "lane " + startLane + " block" + startBlock + " could not be found");
        }
        playerStartBlock.get.occupy(playerId);
    }


}
