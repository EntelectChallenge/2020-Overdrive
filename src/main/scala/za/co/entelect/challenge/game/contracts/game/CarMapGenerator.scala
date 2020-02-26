package za.co.entelect.challenge.game.contracts.game

import java.util

import za.co.entelect.challenge.game.contracts.map.GameMap;
import za.co.entelect.challenge.game.contracts.map.CarGameMap;
import za.co.entelect.challenge.game.contracts.map.Block;
import za.co.entelect.challenge.game.contracts.map.BlockPosition;
import za.co.entelect.challenge.game.contracts.map.BlockObjectCreator;

import za.co.entelect.challenge.game.contracts.player.Player
import za.co.entelect.challenge.game.contracts.Config.Config

class CarMapGenerator(seed: Int) extends GameMapGenerator {

    override def generateGameMap(players: util.List[Player]): GameMap = {
        val registeredPlayers = registerGamePlayers(players);

        val intialisationRound = 0;
        
        val randomNumberGenerator = new scala.util.Random(seed);
        val blocks = generateBlocks(Config.NUMBER_OF_LANES, Config.TRACK_LENGTH, randomNumberGenerator, players);

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

        for (i <- 0 to (players.size() - 1)) {
            val currentPlayer = players.get(i);
            val playerId = i + 1;
            val newGamePlayer = new CarGamePlayer(Config.DEFAULT_HEALTH, Config.DEFAULT_SCORE, playerId, Config.INITIAL_SPEED); 
            newGamePlayer.getReady();
            currentPlayer.playerRegistered(newGamePlayer);
        }

        return players;
    }

    def generateBlocks(lanes: Int, trackLength: Int, randomNumberGenerator: scala.util.Random, players: util.List[Player]): Array[Block] = {
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

        val playerOneId = players.get(0).getGamePlayer().asInstanceOf[CarGamePlayer].getGamePlayerId();
        setPlayerOneStartPosition(blocks, playerOneId);

        val playerTwoId = players.get(1).getGamePlayer().asInstanceOf[CarGamePlayer].getGamePlayerId();
        setPlayerTwoStartPosition(blocks, playerTwoId);

        return blocks;
    }

    private def setPlayerOneStartPosition(blocks: Array[Block], playerOneId: Int) = {
        setStartPosition(blocks, Config.PLAYER_ONE_START_LANE, Config.PLAYER_ONE_START_BLOCK, playerOneId);
    }

    private def setPlayerTwoStartPosition(blocks: Array[Block], playerTwoId: Int) = {
        setStartPosition(blocks, Config.PLAYER_TWO_START_LANE, Config.PLAYER_TWO_START_BLOCK, playerTwoId);
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
