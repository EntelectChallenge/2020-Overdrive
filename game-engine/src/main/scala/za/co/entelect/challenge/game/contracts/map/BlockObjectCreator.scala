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

  def ensureThereIsAlwaysAPathThroughMud(seed: Int, blocks: Array[Block]): Unit = {
    val randomNumberGenerator = new Random(seed)

    var blockNumber = 3
    do {
      val l1C = blocks.find(x => x.getPosition().getBlockNumber() - 2 == blockNumber && x.getPosition().getLane() == 1)
      val l2C = blocks.find(x => x.getPosition().getBlockNumber() - 2 == blockNumber && x.getPosition().getLane() == 2)
      val l3C = blocks.find(x => x.getPosition().getBlockNumber() - 2 == blockNumber && x.getPosition().getLane() == 3)
      val l4C = blocks.find(x => x.getPosition().getBlockNumber() - 2 == blockNumber && x.getPosition().getLane() == 4)
      val l1A = blocks.find(x => x.getPosition().getBlockNumber() - 1 == blockNumber && x.getPosition().getLane() == 1)
      val l2A = blocks.find(x => x.getPosition().getBlockNumber() - 1 == blockNumber && x.getPosition().getLane() == 2)
      val l3A = blocks.find(x => x.getPosition().getBlockNumber() - 1 == blockNumber && x.getPosition().getLane() == 3)
      val l4A = blocks.find(x => x.getPosition().getBlockNumber() - 1 == blockNumber && x.getPosition().getLane() == 4)
      val l1B = blocks.find(x => x.getPosition().getBlockNumber() == blockNumber && x.getPosition().getLane() == 1)
      val l2B = blocks.find(x => x.getPosition().getBlockNumber() == blockNumber && x.getPosition().getLane() == 2)
      val l3B = blocks.find(x => x.getPosition().getBlockNumber() == blockNumber && x.getPosition().getLane() == 3)
      val l4B = blocks.find(x => x.getPosition().getBlockNumber() == blockNumber && x.getPosition().getLane() == 4)

      val isMudToRemove = //goal is don't want solid block of mud over 2 lanes
        (l1A.get.getMapObject() == Config.MUD_MAP_OBJECT || l1B.get.getMapObject() == Config.MUD_MAP_OBJECT) &&
        (l2A.get.getMapObject() == Config.MUD_MAP_OBJECT || l2B.get.getMapObject() == Config.MUD_MAP_OBJECT) &&
        (l3A.get.getMapObject() == Config.MUD_MAP_OBJECT || l3B.get.getMapObject() == Config.MUD_MAP_OBJECT) &&
        (l4A.get.getMapObject() == Config.MUD_MAP_OBJECT || l4B.get.getMapObject() == Config.MUD_MAP_OBJECT)

      if(isMudToRemove) {
        var blocksWhereMudCouldBeRemoved = List[Block]()
        if(l1C.isDefined && l1C.get.getMapObject() != Config.MUD_MAP_OBJECT) {
          blocksWhereMudCouldBeRemoved = blocksWhereMudCouldBeRemoved.appended(l1A.get)
          blocksWhereMudCouldBeRemoved = blocksWhereMudCouldBeRemoved.appended(l1B.get)
        }
        if(l2C.isDefined && l2C.get.getMapObject() != Config.MUD_MAP_OBJECT) {
          blocksWhereMudCouldBeRemoved = blocksWhereMudCouldBeRemoved.appended(l2A.get)
          blocksWhereMudCouldBeRemoved = blocksWhereMudCouldBeRemoved.appended(l2B.get)
        }
        if(l3C.isDefined && l3C.get.getMapObject() != Config.MUD_MAP_OBJECT) {
          blocksWhereMudCouldBeRemoved = blocksWhereMudCouldBeRemoved.appended(l3A.get)
          blocksWhereMudCouldBeRemoved = blocksWhereMudCouldBeRemoved.appended(l3B.get)
        }
        if(l4C.isDefined && l4C.get.getMapObject() != Config.MUD_MAP_OBJECT) {
          blocksWhereMudCouldBeRemoved = blocksWhereMudCouldBeRemoved.appended(l4A.get)
          blocksWhereMudCouldBeRemoved = blocksWhereMudCouldBeRemoved.appended(l4B.get)
        }
        if(blocksWhereMudCouldBeRemoved.length > 1) {
          val indexOfBlockBehindBlockToRemoveMudFrom = randomNumberGenerator.nextInt(blocksWhereMudCouldBeRemoved.length - 1)
          val blockToRemoveMudFrom = blocksWhereMudCouldBeRemoved(indexOfBlockBehindBlockToRemoveMudFrom)
          val indexOfBlockToRemoveMudFrom = blocks.indexOf(blockToRemoveMudFrom)
          blocks(indexOfBlockToRemoveMudFrom) = new Block(blockToRemoveMudFrom.getPosition(), Config.EMPTY_MAP_OBJECT, Config.EMPTY_PLAYER)
          if(indexOfBlockBehindBlockToRemoveMudFrom % 2 == 0) {
            val otherBlockToRemoveMudFrom = blocksWhereMudCouldBeRemoved(indexOfBlockBehindBlockToRemoveMudFrom + 1)
            val otherIndexOfBlockToRemoveMudFrom = blocks.indexOf(otherBlockToRemoveMudFrom)
            blocks(otherIndexOfBlockToRemoveMudFrom) = new Block(otherBlockToRemoveMudFrom.getPosition(), Config.EMPTY_MAP_OBJECT, Config.EMPTY_PLAYER)
          } else {
            val otherBlockToRemoveMudFrom = blocksWhereMudCouldBeRemoved(indexOfBlockBehindBlockToRemoveMudFrom - 1)
            val otherIndexOfBlockToRemoveMudFrom = blocks.indexOf(otherBlockToRemoveMudFrom)
            blocks(otherIndexOfBlockToRemoveMudFrom) = new Block(otherBlockToRemoveMudFrom.getPosition(), Config.EMPTY_MAP_OBJECT, Config.EMPTY_PLAYER)
          }
        }
      }

      blockNumber = blockNumber + 1
    } while(blockNumber < Config.TRACK_LENGTH)
  }

}
