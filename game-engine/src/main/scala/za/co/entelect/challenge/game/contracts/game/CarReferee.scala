package za.co.entelect.challenge.game.contracts.game

import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.common.RefereeMessage
import za.co.entelect.challenge.game.contracts.map.{CarGameMap, GameMap}

import scala.collection.JavaConverters._

class CarReferee extends GameReferee {
  override def isMatchValid(gameMap: GameMap): RefereeMessage = {
    var reasonsForDecisionList = List[String]()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    var matchIsValid = true

    val playersWithExcessiveScore = carGameMap.getCarGamePlayers().count(x => x.getScore() > 12000)
    if(playersWithExcessiveScore > 0)
    {
      matchIsValid = false;
      if(playersWithExcessiveScore == 1)
      {
        val onlyPlayerWithExcessiveScore = carGameMap.getCarGamePlayers().find(x => x.getScore() > 12000).get
        reasonsForDecisionList = reasonsForDecisionList.appended("Player with id " + onlyPlayerWithExcessiveScore.getGamePlayerId() + " has a score of " + onlyPlayerWithExcessiveScore.getScore())
      }
      else
      {
        reasonsForDecisionList = reasonsForDecisionList.appended("Both players have scores exceeding 12000 which should not be possible")
      }
    }

    val playersWithExcessivePowerupCount = carGameMap.getCarGamePlayers().count(x => x.getPowerups().length > 1500)
    if(playersWithExcessivePowerupCount > 0)
    {
      matchIsValid = false;
      if(playersWithExcessivePowerupCount == 1)
      {
        val onlyPlayerWithExcessiveScore = carGameMap.getCarGamePlayers().find(x => x.getPowerups().length > 1500).get
        reasonsForDecisionList = reasonsForDecisionList.appended("Player with id " + onlyPlayerWithExcessiveScore.getGamePlayerId() + " has a score of " + onlyPlayerWithExcessiveScore.getScore());
      }
      else
      {
        reasonsForDecisionList = reasonsForDecisionList.appended("Both players have powerup counts exceeding 1500 which should not be possible");
      }
    }

    val maxRoundsExceeded = gameMap.getCurrentRound() > Config.MAX_ROUNDS
    if(maxRoundsExceeded)
    {
      matchIsValid = false
      reasonsForDecisionList = reasonsForDecisionList.appended("max rounds exceeded");
    }
    val reasonsForDecision = reasonsForDecisionList.asJava
    val refereeMessage = new RefereeMessage(matchIsValid, reasonsForDecision)
    return refereeMessage
  }
}
