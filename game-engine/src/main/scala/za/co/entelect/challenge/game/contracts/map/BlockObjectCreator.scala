package za.co.entelect.challenge.game.contracts.map

import za.co.entelect.challenge.game.contracts.Config.Config

import scala.collection.mutable
import scala.util.Random

class BlockObjectCreator() {

  def generateMapObject(randomNumberGenerator: Random): Int = {
    val mapObjects = mutable.SortedMap[Double, Int]()
    mapObjects.put(0.0, Config.EMPTY_MAP_OBJECT)
    mapObjects.put(Config.MUD_GENERATION_PERCENTAGE * 0.01, Config.MUD_MAP_OBJECT)
    mapObjects.put(mapObjects.lastKey + (Config.BOOST_GENERATION_PERCENTAGE * 0.01), Config.BOOST_MAP_OBJECT)
    mapObjects.put(mapObjects.lastKey + (Config.OIL_ITEM_GENERATION_PERCENTAGE * 0.01), Config.OIL_ITEM_MAP_OBJECT)
    mapObjects.put(1.0, Config.EMPTY_MAP_OBJECT)

    val (_, value) = mapObjects.minAfter(Math.random).get
    value
  }

}
