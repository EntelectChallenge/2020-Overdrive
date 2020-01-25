package example

import za.co.entelect.challenge.game.contracts.common.RefereeMessage
import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.GameMap

class CarGameMap extends GameMap {
    override def getCurrentRound: Int = {
        return 0;
    }

    override def setCurrentRound(i: Int): Unit = {
        println("setCurrentRound");
    }

    override def getWinningPlayer: GamePlayer = ???

    override def getRefereeIssues: RefereeMessage = ???
}
