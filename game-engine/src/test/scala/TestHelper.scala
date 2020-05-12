package test

import java.util

import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.game.{CarGamePlayer, CarGameRoundProcessor, CarMapGenerator, GamePlayer}
import za.co.entelect.challenge.game.contracts.map.{BlockPosition, CarGameMap, GameMap}
import za.co.entelect.challenge.game.contracts.player.Player

import scala.collection.JavaConverters._

object TestHelper {
  private val seed = 12
  private var carMapGenerator = new CarMapGenerator(seed)

  private val testPlayer1 = new TestPlayer("A - Test Player Number B")
  private val testPlayer2 = new TestPlayer("B - Test Player Number A")

  def putPlayerSomewhereOnTheTrack(carGameMap: CarGameMap, testCarGamePlayerId: Int, newLane: Int, newBlockNumber: Int) = {
    val player1Position = carGameMap.getPlayerBlockPosition(testCarGamePlayerId)
    carGameMap.vacateBlock(player1Position)
    val newPositionMidRace = new BlockPosition(newLane, newBlockNumber)
    carGameMap.occupyFinalMapPositionForPlayer(testCarGamePlayerId, newPositionMidRace)
  }

  def getTestGamePlayer2(): GamePlayer = {
    if(testPlayer2.getGamePlayer().asInstanceOf[CarGamePlayer].getGamePlayerId() == 2)
      return testPlayer2.getGamePlayer()
    else return testPlayer1.getGamePlayer()
  }

  def getTestGamePlayer1(): GamePlayer = {
    if(testPlayer1.getGamePlayer().asInstanceOf[CarGamePlayer].getGamePlayerId() == 1)
      return testPlayer1.getGamePlayer()
    else return testPlayer2.getGamePlayer()
  }

  def initialiseGameWithNoMapObjects(): GameMap = {
    val testPlayers = new Array[Player](2)
    testPlayers(0) = testPlayer1
    testPlayers(1) = testPlayer2
    val testPlayersJava = testPlayers.toList.asJava
    val gameMap = carMapGenerator.generateGameMap(testPlayersJava)
    gameMap.asInstanceOf[CarGameMap].makeAllBlocksEmpty()

    return gameMap
  }

  def initialiseGameWithMapObjectAt(lane: Int, blockNumber: Int, mapObject: Int): GameMap = {
    val testPlayers = new Array[Player](2)
    testPlayers(0) = testPlayer1
    testPlayers(1) = testPlayer2
    val testPlayersJava = testPlayers.toList.asJava
    val gameMap = carMapGenerator.generateGameMap(testPlayersJava)
    gameMap.asInstanceOf[CarGameMap].makeAllBlocksEmpty()
    gameMap.asInstanceOf[CarGameMap].placeObjectAt(lane, blockNumber, mapObject)

    return gameMap
  }

  def initialiseGameWithMultipleSameMapObjectsAt(lane: Int, blockNumbers: Array[Int], mapObject: Int): GameMap = {
    val testPlayers = new Array[Player](2)
    testPlayers(0) = testPlayer1
    testPlayers(1) = testPlayer2
    val testPlayersJava = testPlayers.toList.asJava
    val gameMap = carMapGenerator.generateGameMap(testPlayersJava)
    gameMap.asInstanceOf[CarGameMap].makeAllBlocksEmpty()
    for (blockNumber <- blockNumbers) {
      gameMap.asInstanceOf[CarGameMap].placeObjectAt(lane, blockNumber, mapObject)
    }

    return gameMap
  }

  def initialiseMapWithSeed(seed: Int): GameMap =
  {
    val testPlayers = new Array[Player](2)
    testPlayers(0) = testPlayer1
    testPlayers(1) = testPlayer2
    val testPlayersJava = testPlayers.toList.asJava
    carMapGenerator = new CarMapGenerator(seed)
    val gameMap = carMapGenerator.generateGameMap(testPlayersJava)

    return gameMap.asInstanceOf[CarGameMap]
  }

  def processRound(gameMap: GameMap, player1Command: RawCommand, player2Command: RawCommand) = {

    var commandsToProcess = collection.mutable.Map[GamePlayer, util.List[RawCommand]]()

    var player1Commands = List[RawCommand]()
    player1Commands = player1Commands.appended(player1Command)
    commandsToProcess.addOne(getTestGamePlayer1(), player1Commands.asJava)

    var player2Commands = List[RawCommand]()
    player2Commands = player2Commands.appended(player2Command)
    commandsToProcess.addOne(getTestGamePlayer2(), player2Commands.asJava)

    val javaCommandsToProcess = commandsToProcess.asJava

    val carGameRoundProccessor: CarGameRoundProcessor = new CarGameRoundProcessor
    carGameRoundProccessor.processRound(gameMap, javaCommandsToProcess)
  }
}
