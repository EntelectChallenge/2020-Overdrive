package za.co.entelect.challenge.game.contracts.game

import java.util

import za.co.entelect.challenge.game.contracts.Config.Config

import scala.collection.JavaConverters._
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.map.{BlockPosition, CarGameMap, GameMap}
import za.co.entelect.challenge.game.contracts.commands.CommandFactory

class CarGameRoundProcessor extends GameRoundProcessor{


  override def processRound(gameMap: GameMap, commandsToProcess: util.Map[GamePlayer, util.List[RawCommand]]): Boolean = {
    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    val gamePlayers = carGameMap.getGamePlayers()
    val commandFactory = new CommandFactory

    val player1StartPositions = carGameMap.getPlayerBlockPosition(1)
    val player2StartPositions = carGameMap.getPlayerBlockPosition(2)

    var carStartPositions = Array[BlockPosition]()
    carStartPositions = carStartPositions.appended(player1StartPositions)
    carStartPositions = carStartPositions.appended(player2StartPositions)
    carGameMap.setStartRound(carStartPositions)

    //clear player states from last round
    for ( i <- gamePlayers.indices) {
      val gamePlayer = gamePlayers(i)
      gamePlayer.asInstanceOf[CarGamePlayer].clearStatesThatOccurredLastRound()
    }

    for ( i <- gamePlayers.indices) {
      val gamePlayer = gamePlayers(i)
      val commandText = commandsToProcess.get(gamePlayer).get(0).getCommand
      var playerCommand: RawCommand = commandFactory.makeCommand(commandText)
      playerCommand.performCommand(gameMap, gamePlayer)
    }

    //make sure players hit by EMP do not move
    for ( i <- gamePlayers.indices) {
      val gamePlayer = gamePlayers(i)
      val carGamePlayer = gamePlayer.asInstanceOf[CarGamePlayer]
      val hitByEmp = carGamePlayer.getState().contains(Config.HIT_EMP_PLAYER_STATE)
      if(hitByEmp) {
        val stagedPositionToUpdate = carGameMap.stagedFuturePositions.find(x => x.getPlayer().getGamePlayerId() == carGamePlayer.getGamePlayerId()).get
        stagedPositionToUpdate.setNewPosition(stagedPositionToUpdate.getOldPosition())
      }
    }

    carGameMap.resolveCyberTruckCollisions() //needs to happen first because projected path of player is used in player collisions
    carGameMap.resolvePlayerCollisions()
    carGameMap.calculateEffectsOfAndApplyStagedPositionsToPlayers()

    carGameMap.placeRequestedCyberTrucks()

    if (carGameMap.getCurrentRound >= Config.MAX_ROUNDS){
      val playerBlocks = carGameMap.blocks.filter(x => x.getOccupiedByPlayerWithId() == 1 || x.getOccupiedByPlayerWithId() == 2)
      val player1BlockNumber = playerBlocks.find(x => x.getOccupiedByPlayerWithId() == 1).get.getPosition().getBlockNumber()
      val player2BlockNumber = playerBlocks.find(x => x.getOccupiedByPlayerWithId() == 2).get.getPosition().getBlockNumber()
      val player1InFront = player1BlockNumber > player2BlockNumber
      val player2InFront = player2BlockNumber > player1BlockNumber
      val playersAreTiedForDistanceTravelled = player1BlockNumber == player2BlockNumber

      for (i <- gamePlayers.indices) {
        val player = gamePlayers(i).asInstanceOf[CarGamePlayer]

        if(player.getGamePlayerId() == 1 && (player1InFront || playersAreTiedForDistanceTravelled)) {
          player.finish()
        }
        if(player.getGamePlayerId() == 2 && (player2InFront || playersAreTiedForDistanceTravelled)) {
          player.finish()
        }
      }
    }

    return true
  }

  override def getErrorList(gameMap: GameMap): util.List[String] = {
    return new Array[String](0).toList.asJava
    //TODO: Fix error reporting
  }

  override def getErrorList(gameMap: GameMap, player: GamePlayer): util.List[String] = {
    throw new NotImplementedError("Car game round processor get error list => game map and player")
  }
}
