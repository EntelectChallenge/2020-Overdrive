package za.co.entelect.challenge.game.contracts.map

import za.co.entelect.challenge.game.contracts.common.RefereeMessage
import za.co.entelect.challenge.game.contracts.game.{CarGamePlayer, GamePlayer}

import scala.collection.JavaConverters._

class CarGameMap extends GameMap {
  override def getCurrentRound: Int = {
    return 0;
  }

  override def setCurrentRound(i: Int): Unit = {
    println("setCurrentRound");
  }

  override def getWinningPlayer: GamePlayer = {
    return new GamePlayer {
        override def getHealth: Int = ???

        override def getScore: Int = ???
    };
  }

  override def getRefereeIssues: RefereeMessage = {
    return new RefereeMessage(true, List("", "", "").asJava)
  }
}
