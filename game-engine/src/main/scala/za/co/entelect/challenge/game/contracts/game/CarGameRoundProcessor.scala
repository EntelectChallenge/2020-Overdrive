package za.co.entelect.challenge.game.contracts.game

import java.util
import scala.collection.JavaConverters._

import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.map.GameMap
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.commands.CommandFactory

class CarGameRoundProcessor extends GameRoundProcessor{
  

  override def processRound(gameMap: GameMap, commandsToProcess: util.Map[GamePlayer, util.List[RawCommand]]): Boolean = {
    val carGameMap = gameMap.asInstanceOf[CarGameMap];
    val gamePlayers = carGameMap.getGamePlayers();
    val commandFactory = new CommandFactory;
    for ( i <- 0 to (gamePlayers.length - 1)) {
      val gamePlayer = gamePlayers(i);
      val commandText = commandsToProcess.get(gamePlayer).get(0).getCommand();
      var playerCommand: RawCommand = commandFactory.makeCommand(commandText);
      playerCommand.performCommand(gameMap, gamePlayer);
    }

    carGameMap.resolvePlayerCollisions();
    carGameMap.commitStagedPositions();
    return true;
  }

  override def getErrorList(gameMap: GameMap): util.List[String] = {
    return new Array[String](0).toList.asJava;
    //TODO: Fix error reporting
  }

  override def getErrorList(gameMap: GameMap, player: GamePlayer): util.List[String] = {
    throw new NotImplementedError("Car game round processor get error list => game map and player")
  }
}
