package za.co.entelect.challenge.game.contracts.game

import za.co.entelect.challenge.game.contracts.common.RefereeMessage
import za.co.entelect.challenge.game.contracts.map.GameMap

import scala.collection.JavaConverters._

class CarReferee extends GameReferee {
  override def isMatchValid(gameMap: GameMap): RefereeMessage = {
    val refereeMessageList = List("", "", "").asJava
    return new RefereeMessage(true, refereeMessageList)
  }
}
