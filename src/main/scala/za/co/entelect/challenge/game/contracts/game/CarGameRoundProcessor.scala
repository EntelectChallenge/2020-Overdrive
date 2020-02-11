package za.co.entelect.challenge.game.contracts.game
import java.util

import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.map.GameMap

class CarGameRoundProcessor extends GameRoundProcessor{
  override def processRound(gameMap: GameMap, commandsToProcess: util.Map[GamePlayer, util.List[RawCommand]]): Boolean = {
    return true
  }

  override def getErrorList(gameMap: GameMap): util.List[String] = ???

  override def getErrorList(gameMap: GameMap, player: GamePlayer): util.List[String] = ???
}
