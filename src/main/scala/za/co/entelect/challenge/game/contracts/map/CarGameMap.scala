package za.co.entelect.challenge.game.contracts.map

import java.util

import za.co.entelect.challenge.game.contracts.common.RefereeMessage
import za.co.entelect.challenge.game.contracts.game.{CarGamePlayer, GamePlayer}
import za.co.entelect.challenge.game.contracts.player.Player

import scala.collection.JavaConverters._

class CarGameMap(list: util.List[Player]) extends GameMap {

  override def getCurrentRound: Int = {
    return 0;
  }

  override def setCurrentRound(i: Int): Unit = {
    println("setCurrentRound");
  }

  override def getWinningPlayer: GamePlayer = {

    var firstPlayer = list.get(0);
    var firstGamePlayer = firstPlayer.getGamePlayer();

    return firstGamePlayer;
  }

  override def getRefereeIssues: RefereeMessage = {
    return new RefereeMessage(true, List("", "", "").asJava)
  }
}
