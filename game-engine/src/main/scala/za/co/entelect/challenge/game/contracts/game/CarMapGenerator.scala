package za.co.entelect.challenge.game.contracts.game

import java.util

import za.co.entelect.challenge.game.contracts.map.{Block, BlockObjectCreator, BlockPosition, CarGameMap, GameMap, Noise}
import za.co.entelect.challenge.game.contracts.player.Player
import za.co.entelect.challenge.game.contracts.Config.Config

import scala.collection.mutable
import scala.util.Random

class CarMapGenerator(seed: Int) extends GameMapGenerator {

    override def generateGameMap(players: util.List[Player]): GameMap = {

        val registeredPlayers = registerGamePlayers(players)

        val intialisationRound = 0
        
        val blocks = generateBlocks(Config.NUMBER_OF_LANES, Config.TRACK_LENGTH, seed)

        val carGameMap = new CarGameMap(
            players, 
            seed, 
            Config.NUMBER_OF_LANES, 
            Config.TRACK_LENGTH, 
            blocks, 
            intialisationRound
        )

        return carGameMap
    }

    def registerGamePlayers(players: util.List[Player]): util.List[Player] = {
        val playerSortMode = Config.RACER_PLACEMENT_SORTING
        var sortedPlayers = new Array[Player](players.size())
        for (i <- 0 to (players.size() - 1)) {
            sortedPlayers(i) = players.get(i)
        }

        playerSortMode match {
            case -1  => sortedPlayers
            case 0 => sortedPlayers = sortedPlayers.sortBy(x => x.getName().split("-")(1))
            case 1 => sortedPlayers = sortedPlayers.sortBy(x => x.getName().split("-")(1)).reverse
            case invalidSortMode  => throw new Exception("Invalid racer placement sort mode: " + invalidSortMode.toString())
        }

        for (i <- 0 to (sortedPlayers.length - 1)) {
            val currentPlayer = sortedPlayers(i)
            val playerId = i + 1
            val newGamePlayer = new CarGamePlayer(Config.DEFAULT_HEALTH, Config.DEFAULT_SCORE, playerId, Config.INITIAL_SPEED)
            newGamePlayer.getReady()
            currentPlayer.playerRegistered(newGamePlayer)
        }

        return players
    }

    def generateBlocks(lanes: Int, trackLength: Int, seed:Int): Array[Block] = {
        var blocks = new Array[Block](lanes*trackLength)
        val blockObjectCreator = new BlockObjectCreator

        Noise.seed = seed
        Noise.init()

        val mapObjects = mutable.SortedMap[Double, Int]()
        mapObjects.put(0.0, Config.EMPTY_MAP_OBJECT)
        mapObjects.put(Config.MUD_GENERATION_PERCENTAGE * 0.01 * 0.35, Config.MUD_MAP_OBJECT)
        mapObjects.put(mapObjects.lastKey + Config.BOOST_GENERATION_PERCENTAGE * 0.01, Config.BOOST_MAP_OBJECT)
        mapObjects.put(mapObjects.lastKey + Config.OIL_ITEM_GENERATION_PERCENTAGE * 0.01, Config.OIL_ITEM_MAP_OBJECT)
        mapObjects.put(mapObjects.lastKey + Config.WALL_GENERATION_PERCENTAGE * 0.01, Config.WALL_MAP_OBJECT)
        mapObjects.put(mapObjects.lastKey + Config.LIZARD_GENERATION_PERCENTAGE * 0.01, Config.LIZARD_MAP_OBJECT)
        mapObjects.put(mapObjects.lastKey + Config.TWEET_GENERATION_PERCENTAGE * 0.01, Config.TWEET_MAP_OBJECT)
        mapObjects.put(mapObjects.lastKey + Config.EMP_GENERATION_PERCENTAGE * 0.01, Config.EMP_MAP_OBJECT)
        mapObjects.put(1.0, Config.EMPTY_MAP_OBJECT)

        //generate empty map
        for ( i <- 0 to (blocks.length - 1)) {
            val lane = i/trackLength + 1
            val blockNumber = i%trackLength + 1
            val position = new BlockPosition(lane, blockNumber)
            val generatedMapObject = Config.EMPTY_MAP_OBJECT
            blocks(i) = new Block(position, generatedMapObject, Config.EMPTY_PLAYER)
        }

        blockObjectCreator.placeObjectOnTrack(blocks, mapObjects, Config.MUD_MAP_OBJECT, 0.1)
        blockObjectCreator.placeObjectOnTrack(seed, blocks, mapObjects, Config.WALL_MAP_OBJECT)

        blockObjectCreator.placeObjectOnTrack(seed, blocks, mapObjects, Config.BOOST_MAP_OBJECT)
        blockObjectCreator.placeObjectOnTrack(seed, blocks, mapObjects, Config.OIL_ITEM_MAP_OBJECT)
        blockObjectCreator.placeObjectOnTrack(seed, blocks, mapObjects, Config.LIZARD_MAP_OBJECT)
        blockObjectCreator.placeObjectOnTrack(seed, blocks, mapObjects, Config.TWEET_MAP_OBJECT)
        blockObjectCreator.placeObjectOnTrack(seed, blocks, mapObjects, Config.EMP_MAP_OBJECT)

        setPlayerOneStartPosition(blocks)
        setPlayerTwoStartPosition(blocks)

        return blocks
    }

    private def setPlayerOneStartPosition(blocks: Array[Block]) = {
        setStartPosition(blocks, Config.PLAYER_ONE_START_LANE, Config.PLAYER_ONE_START_BLOCK, 1)
    }

    private def setPlayerTwoStartPosition(blocks: Array[Block]) = {
        setStartPosition(blocks, Config.PLAYER_TWO_START_LANE, Config.PLAYER_TWO_START_BLOCK, 2)
    }

    private def setStartPosition(blocks: Array[Block], startLane: Int, startBlock: Int, playerId: Int) = {
        var playerStartBlock = blocks.find(block => block.getPosition().getLane() == startLane && block.getPosition().getBlockNumber() == startBlock)
        if (playerStartBlock.isEmpty) 
        {
            throw new Exception("Failed to initialise map: " + "lane " + startLane + " block" + startBlock + " could not be found")
        }
        playerStartBlock.get.occupy(playerId)
    }


}
