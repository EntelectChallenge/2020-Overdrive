package za.co.entelect.challenge.game.contracts.map

import java.util

import za.co.entelect.challenge.game.contracts.common.RefereeMessage
import za.co.entelect.challenge.game.contracts.game.{CarGamePlayer, GamePlayer}
import za.co.entelect.challenge.game.contracts.player.Player

import scala.collection.JavaConverters._

class CarGameMap(players: util.List[Player], mapGenerationSeed: Int, lanes: Int, trackLength: Int, blocks: Array[Block], var round: Int) extends GameMap {

  override def getCurrentRound: Int = {
    return round;
  }

  override def setCurrentRound(currentRound: Int): Unit = {
    round = currentRound;
  }

  override def getWinningPlayer: GamePlayer = {

    var firstPlayer = players.get(0);
    var firstGamePlayer = firstPlayer.getGamePlayer();

    return firstGamePlayer;
  }

  override def getRefereeIssues: RefereeMessage = {
    return new RefereeMessage(true, List("", "", "").asJava)
  }
}
