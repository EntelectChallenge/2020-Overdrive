package za.co.entelect.challenge.game.contracts.game

import za.co.entelect.challenge.game.contracts.common.RefereeMessage
import za.co.entelect.challenge.game.contracts.map.GameMap

import scala.collection.JavaConverters._

class CarReferee extends GameReferee {
  override def isMatchValid(gameMap: GameMap): RefereeMessage = {
    //TODO: determine what would invalidate a match
    val matchIsValid = true;
    val reasonsForDecision = new Array[String](0).toList.asJava;
    val refereeMessage = new RefereeMessage(matchIsValid, reasonsForDecision)
    return refereeMessage;
  }
}
