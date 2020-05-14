package test

import org.scalatest.FunSuite
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class Command_Turn_Right_Tests extends FunSuite{
  private val commandText = "TURN_RIGHT"
  private var commandFactory: CommandFactory = null
  private var turnRightCommand: RawCommand = null

  def initialise() = {
    Config.loadDefault()
    commandFactory = new CommandFactory
    turnRightCommand = commandFactory.makeCommand(commandText)
    turnRightCommand.setCommand(commandText)
  }

  test("Given start of race when TURN_RIGHT command then player turns right and incurs change lane penalty") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    TestHelper.processRound(gameMap, turnRightCommand, turnRightCommand)

    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    val player1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1.asInstanceOf[CarGamePlayer].getGamePlayerId())

    assert((player1Position.getLane() == Config.PLAYER_ONE_START_LANE+1) && (player1Position.getBlockNumber() == Config.PLAYER_ONE_START_BLOCK + Config.INITIAL_SPEED-1))
  }

  test("Given player in lane 4 when TURN_RIGHT command player stays in lane 4 and incurs change lane penalty") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    val maxLane = 4
    val newBlockNumberMidRace = 56
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, maxLane, newBlockNumberMidRace)

    val speedBeforeProcessingCommand = testCarGamePlayer1.getSpeed()
    TestHelper.processRound(gameMap, turnRightCommand, turnRightCommand)

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert((newPlayer1PositionAfterCommand.getLane() == maxLane) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace + speedBeforeProcessingCommand - 1))
  }

  test("Given player during a race when TURN_RIGHT then player turns right and incurs change lane penalty") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    val newLaneMidRace = 2
    val newBlockNumberMidRace = 56
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace)

    val speedBeforeProcessingCommand = testCarGamePlayer1.getSpeed()
    TestHelper.processRound(gameMap, turnRightCommand, turnRightCommand)

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace+1) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace + speedBeforeProcessingCommand - 1))
  }

  test("Given player near finish line when TURN_RIGHT then player stops at finish line") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    val newLaneMidRace = 2
    val newBlockNumberMidRace = Config.TRACK_LENGTH - 4
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace)

    TestHelper.processRound(gameMap, turnRightCommand, turnRightCommand)

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace+1) && (newPlayer1PositionAfterCommand.getBlockNumber() == Config.TRACK_LENGTH))
  }

  test("Given player that is stopped when TURN_RIGHT then player does not move") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    val newLaneMidRace = 2
    val newBlockNumberMidRace = 56
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace)

    testCarGamePlayer1.speed = 0 //stop player
    TestHelper.processRound(gameMap, turnRightCommand, turnRightCommand)

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace))
  }

  test("Given player that is boosting when TURN_RIGHT then player turns right, incurs change lane penalty and moves at boost speed") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    testCarGamePlayer1.useBoost()

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    val newLaneMidRace = 2
    val newBlockNumberMidRace = 56
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace)

    TestHelper.processRound(gameMap, turnRightCommand, turnRightCommand)

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace+1) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace + Config.BOOST_SPEED - 1))
    assert(testCarGamePlayer1.isBoosting() == true)
  }
}
