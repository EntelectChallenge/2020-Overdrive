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

}
