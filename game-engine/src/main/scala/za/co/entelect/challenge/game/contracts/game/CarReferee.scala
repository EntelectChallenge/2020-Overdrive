package za.co.entelect.challenge.game.contracts.game

import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.common.RefereeMessage
import za.co.entelect.challenge.game.contracts.map.GameMap

import scala.collection.JavaConverters._

class CarReferee extends GameReferee {
  override def isMatchValid(gameMap: GameMap): RefereeMessage = {
    val maxRoundsExceeded = gameMap.getCurrentRound() > Config.MAX_ROUNDS;
    if(maxRoundsExceeded)
    {
      val matchIsValid = false;
      var reasonsForDecision = new Array[String](1)
      reasonsForDecision(0) = "max rounds exceeded"
      val reasonsForDecisionJava = reasonsForDecision.toList.asJava;

      val refereeMessage = new RefereeMessage(matchIsValid, reasonsForDecisionJava)
      return refereeMessage;
    }
    val matchIsValid = true;
    val reasonsForDecision = new Array[String](0).toList.asJava;
    val refereeMessage = new RefereeMessage(matchIsValid, reasonsForDecision)
    return refereeMessage;
  }
}
