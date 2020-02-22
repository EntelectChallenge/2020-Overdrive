package za.co.entelect.challenge.game.contracts.game
import java.util

import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.map.GameMap

class CarGameRoundProcessor extends GameRoundProcessor{
  override def processRound(gameMap: GameMap, commandsToProcess: util.Map[GamePlayer, util.List[RawCommand]]): Boolean = {
    throw new NotImplementedError("Car game round processor process round");
  }

  override def getErrorList(gameMap: GameMap): util.List[String] = {
    throw new NotImplementedError("Car game round processor get error list => game map only");
  }

  override def getErrorList(gameMap: GameMap, player: GamePlayer): util.List[String] = {
    throw new NotImplementedError("Car game round processor get error list => game map and player")
  }
}
