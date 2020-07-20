package za.co.entelect.challenge.game.contracts.map

import za.co.entelect.challenge.game.contracts.Config.Config

import scala.collection.mutable
import scala.util.Random

class BlockObjectCreator() {

  def placeObjectOnTrack(blocks: Array[Block], mapObjects: mutable.SortedMap[Double, Int], mapObjectOfInterest: Int, zIndex:Double) = {
    val trackLength = Config.TRACK_LENGTH
    for ( i <- 0 to (blocks.length - 1)) {
      val lane = i/trackLength + 1
      val blockNumber = i%trackLength + 1
      val position = new BlockPosition(lane, blockNumber)
      var generatedMapObject = blocks(i).getMapObject()
      if(generatedMapObject == Config.EMPTY_MAP_OBJECT) {
        if(blockNumber >= Config.STARTING_BLOCK_FOR_GENERATED_MAP_OBJECTS && blockNumber < trackLength) {
          val randomNumber = Math.abs(Noise.noise(blockNumber * 0.1, lane * 0.1, zIndex))
          val (_, value) = mapObjects.minAfter(randomNumber).get
          if(value == mapObjectOfInterest) {
            generatedMapObject = mapObjectOfInterest
          }
        } else if (blockNumber == trackLength) {
          generatedMapObject = Config.FINISH_LINE_MAP_OBJECT
        }

        blocks(i) = new Block(position, generatedMapObject, Config.EMPTY_PLAYER)
      }
    }
  }

  def placeObjectOnTrack(seed: Int, blocks: Array[Block], mapObjects: mutable.SortedMap[Double, Int], mapObjectOfInterest: Int) = {
    val randomNumberGenerator = new Random(seed)
    val trackLength = Config.TRACK_LENGTH
    for ( i <- 0 to (blocks.length - 1)) {
      val lane = i/trackLength + 1
      val blockNumber = i%trackLength + 1
      val position = new BlockPosition(lane, blockNumber)
      var generatedMapObject = blocks(i).getMapObject()
      if(blockNumber >= Config.STARTING_BLOCK_FOR_GENERATED_MAP_OBJECTS && blockNumber < trackLength) {
        val randomNumber = randomNumberGenerator.nextDouble()
        val (_, value) = mapObjects.minAfter(randomNumber).get
        if(value == mapObjectOfInterest) {
          generatedMapObject = mapObjectOfInterest
        }
      } else if (blockNumber == trackLength) {
        generatedMapObject = Config.FINISH_LINE_MAP_OBJECT
      }

        blocks(i) = new Block(position, generatedMapObject, Config.EMPTY_PLAYER)
    }
  }

  def ensureThereIsAlwaysAPathThroughObstacles(seed: Int, blocks: Array[Block]): Unit = {
    val randomNumberGenerator = new Random(seed)

    val blockWindow = 15
    var blockNumber = 1 + blockWindow

    do {
      val blocksInWindow = blocks.filter(x => x.getPosition().getBlockNumber() >= blockNumber - blockWindow && x.getPosition().getBlockNumber() <= blockNumber)

      val isMudToRemove = //goal is don't want solid block of mud over window
        (blocksInWindow.exists(x => x.getPosition().getBlockNumber() >= blockNumber - blockWindow && x.getPosition().getBlockNumber() <= blockNumber && x.getPosition().getLane() == 1 && (x.getMapObject() == Config.MUD_MAP_OBJECT || x.getMapObject() == Config.WALL_MAP_OBJECT))) &&
        (blocksInWindow.exists(x => x.getPosition().getBlockNumber() >= blockNumber - blockWindow && x.getPosition().getBlockNumber() <= blockNumber && x.getPosition().getLane() == 2 && (x.getMapObject() == Config.MUD_MAP_OBJECT || x.getMapObject() == Config.WALL_MAP_OBJECT))) &&
        (blocksInWindow.exists(x => x.getPosition().getBlockNumber() >= blockNumber - blockWindow && x.getPosition().getBlockNumber() <= blockNumber && x.getPosition().getLane() == 3 && (x.getMapObject() == Config.MUD_MAP_OBJECT || x.getMapObject() == Config.WALL_MAP_OBJECT))) &&
        (blocksInWindow.exists(x => x.getPosition().getBlockNumber() >= blockNumber - blockWindow && x.getPosition().getBlockNumber() <= blockNumber && x.getPosition().getLane() == 4 && (x.getMapObject() == Config.MUD_MAP_OBJECT || x.getMapObject() == Config.WALL_MAP_OBJECT)))

      if(isMudToRemove) {
        val blocksBehindWhereObstacleCouldBeRemoved = blocksInWindow.filter(x => x.getPosition().getBlockNumber() == blockNumber - blockWindow && (x.getMapObject() != Config.MUD_MAP_OBJECT && x.getMapObject() != Config.WALL_MAP_OBJECT))
        if(blocksBehindWhereObstacleCouldBeRemoved.length > 0) {
          var indexOfBlockBehindBlocksToRemoveMudFrom = 0
          if(blocksBehindWhereObstacleCouldBeRemoved.length > 1) {
            indexOfBlockBehindBlocksToRemoveMudFrom = randomNumberGenerator.nextInt(blocksBehindWhereObstacleCouldBeRemoved.length - 1)
          }
          val blockBehindWhereMudShouldBeRemoved = blocksBehindWhereObstacleCouldBeRemoved(indexOfBlockBehindBlocksToRemoveMudFrom)
          val blockNumberOfInterest = blockBehindWhereMudShouldBeRemoved.getPosition().getBlockNumber()
          val laneOfInterest = blockBehindWhereMudShouldBeRemoved.getPosition().getLane()
          val blocksToRemoveMudFrom = blocks.filter(x => x.getPosition().getBlockNumber() >= blockNumberOfInterest && x.getPosition().getBlockNumber() <= blockNumberOfInterest + blockWindow && x.getPosition().getLane() == laneOfInterest)
          blocksToRemoveMudFrom.foreach(x => {
            if(x.getMapObject() == Config.MUD_MAP_OBJECT || x.getMapObject() == Config.WALL_MAP_OBJECT) {
              x.setMapObject(Config.EMPTY_MAP_OBJECT)
            }
          })
        }
      }

      blockNumber = blockNumber + 1
    } while(blockNumber < Config.TRACK_LENGTH)
  }

}
