import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class Command_Use_Emp_Tests extends FunSuite{
  private val nothingCommandText = "NOTHING"
  private var commandFactory: CommandFactory = null
  private var nothingCommand: RawCommand = null
  private val useEmpCommandText = "USE_EMP"
  private var useEmpCommand: RawCommand = null
  private val accelerateCommandText = "ACCELERATE"
  private var accelerateCommand: RawCommand = null


  def initialise() = {
    Config.loadDefault()
    commandFactory = new CommandFactory
    nothingCommand = commandFactory.makeCommand(nothingCommandText)

    useEmpCommand = commandFactory.makeCommand(useEmpCommandText)
    useEmpCommand.setCommand(useEmpCommandText)

    accelerateCommand = commandFactory.makeCommand(accelerateCommandText)
    accelerateCommand.setCommand(accelerateCommandText)
  }

  test("Given player with no emp when USE_EMP command then nothing happens") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 3, 40)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 3, 100)

    testCarGamePlayer1.speed = Config.SPEED_STATE_3
    testCarGamePlayer2.speed = Config.SPEED_STATE_3

    TestHelper.processRound(gameMap, useEmpCommand, nothingCommand)

    assert(testCarGamePlayer2.speed == Config.SPEED_STATE_3)
  }

  test("Given player during race with one emp power-up when player uses emp then player has no emp power-ups left")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer1.pickupEmp()
    TestHelper.processRound(gameMap, useEmpCommand, nothingCommand)

    assert(testCarGamePlayer1.getPowerups().count(x => x == Config.EMP_POWERUP_ITEM) == 0)
  }

  test ("Given player during race with multiple tweet power-ups when player uses tweet then number of tweet power-ups is reduced by one")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer1.pickupEmp()
    testCarGamePlayer1.pickupEmp()
    TestHelper.processRound(gameMap, useEmpCommand, nothingCommand)

    assert(testCarGamePlayer1.getPowerups().count(x => x == Config.EMP_POWERUP_ITEM) == 1)
  }


  test("Given player1 with emp behind player2 when USE_EMP command player2 slows down") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 3, 100)

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 3, 40)

    testCarGamePlayer1.speed = Config.SPEED_STATE_3
    testCarGamePlayer2.speed = Config.SPEED_STATE_3

    testCarGamePlayer1.pickupEmp()
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, useEmpCommand, nothingCommand)

    assert(testCarGamePlayer2.speed == Config.SPEED_STATE_1)
  }


  test("Given player2 with emp in front of player1 when USE_EMP command nothing happens") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 3, 40)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 3, 100)

    testCarGamePlayer1.speed = Config.SPEED_STATE_3
    testCarGamePlayer2.speed = Config.SPEED_STATE_3

    testCarGamePlayer2.pickupEmp()
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, useEmpCommand, nothingCommand)

    assert(testCarGamePlayer1.speed == Config.SPEED_STATE_3)
  }

  test("Given player2 with emp behind player1 when USE_EMP command player1 slows down") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 3, 80)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 3, 40)

    testCarGamePlayer1.speed = Config.SPEED_STATE_3
    testCarGamePlayer2.speed = Config.SPEED_STATE_3

    testCarGamePlayer2.pickupEmp()
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, nothingCommand, useEmpCommand)

    assert(testCarGamePlayer1.speed == Config.SPEED_STATE_1)
  }

  test("Given player1 with emp behind player2, one lane left, when USE_EMP command player2 slows down") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 3, 40)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 2, 100)

    testCarGamePlayer1.speed = Config.SPEED_STATE_3
    testCarGamePlayer2.speed = Config.SPEED_STATE_3

    testCarGamePlayer1.pickupEmp()
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, useEmpCommand, nothingCommand)

    assert(testCarGamePlayer2.speed == Config.SPEED_STATE_1)
  }


  test("Given player1 with emp behind player2, one lane away, when USE_EMP command player2 slows down") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 3, 40)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 4, 100)

    testCarGamePlayer1.speed = Config.SPEED_STATE_3
    testCarGamePlayer2.speed = Config.SPEED_STATE_3

    testCarGamePlayer1.pickupEmp()
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, useEmpCommand, nothingCommand)

    assert(testCarGamePlayer2.speed == Config.SPEED_STATE_1)


    testCarGamePlayer1.pickupEmp()
    TestHelper.processRound(gameMap, nothingCommand, accelerateCommand)
    TestHelper.processRound(gameMap, useEmpCommand, accelerateCommand)

    assert(testCarGamePlayer2.speed == Config.SPEED_STATE_2)
  }


  test("Given player1 with emp behind player2, two lanes away, when USE_EMP command nothing happens") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 3, 40)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 1, 100)

    testCarGamePlayer1.speed = Config.SPEED_STATE_3
    testCarGamePlayer2.speed = Config.SPEED_STATE_3

    testCarGamePlayer1.pickupEmp()
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, useEmpCommand, nothingCommand)

    assert(testCarGamePlayer2.speed == Config.SPEED_STATE_3)
  }

  test("Given player2 with emp next to player1, one lanes left, when USE_EMP command nothing happens") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 3, 40)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 2, 40)

    testCarGamePlayer1.speed = Config.SPEED_STATE_3
    testCarGamePlayer2.speed = Config.SPEED_STATE_3

    testCarGamePlayer2.pickupEmp()
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, nothingCommand, useEmpCommand)

    assert(testCarGamePlayer1.speed == Config.SPEED_STATE_3)
  }

  test("Given player1 with emp next to player2, one lanes left, when USE_EMP command nothing happens") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 1, 40)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 2, 40)

    testCarGamePlayer1.speed = Config.SPEED_STATE_3
    testCarGamePlayer2.speed = Config.SPEED_STATE_3

    testCarGamePlayer1.pickupEmp()
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, nothingCommand, useEmpCommand)

    assert(testCarGamePlayer2.speed == Config.SPEED_STATE_3)
  }


  test("Given player1 with emp config distance behind to player2, one lane away, when USE_EMP command player2 slows down") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 1, 40)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 2, 41 + Config.EMP_BLOCK_GAP)

    testCarGamePlayer1.speed = Config.SPEED_STATE_3
    testCarGamePlayer2.speed = Config.SPEED_STATE_3

    testCarGamePlayer1.pickupEmp()
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, useEmpCommand, nothingCommand)

    assert(testCarGamePlayer2.speed == Config.SPEED_STATE_1)
  }

}