import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class Command_Use_Boost_Tests extends FunSuite{
  private val useBoostCommandText = "USE_BOOST"
  private val nothingCommandText = "NOTHING"

  private var commandFactory: CommandFactory = null

  private var useBoostCommand: RawCommand = null
  private var nothingCommand: RawCommand = null

  def initialise() = {
    Config.loadDefault()
    commandFactory = new CommandFactory

    useBoostCommand = commandFactory.makeCommand(useBoostCommandText)
    useBoostCommand.setCommand(useBoostCommandText)

    nothingCommand = commandFactory.makeCommand(nothingCommandText)
    nothingCommand.setCommand(nothingCommandText)
  }

  test("Given player with no boost when USE_BOOST command then nothing happens") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    TestHelper.processRound(gameMap, useBoostCommand, useBoostCommand)

    assert(!testCarGamePlayer1.isBoosting())
    assert(testCarGamePlayer1.speed != Config.BOOST_SPEED)
  }

  test("Given player with 3 boosts when USE_BOOST command then player has 2 boosts left") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer1.pickupBoost()
    testCarGamePlayer1.pickupBoost()
    testCarGamePlayer1.pickupBoost()

    TestHelper.processRound(gameMap, useBoostCommand, useBoostCommand)

    assert(testCarGamePlayer1.isBoosting())
    assert(testCarGamePlayer1.getPowerups().count(x => x == Config.BOOST_POWERUP_ITEM) == 2)
  }

  test("Given player with a boost when USE_BOOST command then player moves at boost speed") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer1.pickupBoost()

    TestHelper.processRound(gameMap, useBoostCommand, useBoostCommand)

    assert(testCarGamePlayer1.getDamage() == 0)
    assert(testCarGamePlayer1.isBoosting())
    assert(testCarGamePlayer1.speed == Config.BOOST_SPEED)
  }

  test("Given player with a boost when USE_BOOST command then boost counter ticks down after each command") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer1.pickupBoost()

    TestHelper.processRound(gameMap, useBoostCommand, useBoostCommand)
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    assert(testCarGamePlayer1.getBoostCounter() == Config.BOOST_DURATION - 1)
  }

  test("Given player that is boosting when boost counter reaches 0 then player is no longer boosting and speed is at most maximum speed") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer1.pickupBoost()
    TestHelper.processRound(gameMap, useBoostCommand, useBoostCommand)

    testCarGamePlayer1.tickBoost()
    testCarGamePlayer1.tickBoost()
    testCarGamePlayer1.tickBoost()
    testCarGamePlayer1.tickBoost()
    testCarGamePlayer1.tickBoost()
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    assert(!testCarGamePlayer1.isBoosting())
    assert(testCarGamePlayer1.speed != Config.BOOST_SPEED)
  }

  test("Given player with 2 damage when players boosts then players speed is 8") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(3, 4), Config.MUD_MAP_OBJECT)
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer1.pickupBoost()
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, useBoostCommand, nothingCommand)

    assert(testCarGamePlayer1.speed == Config.SPEED_STATE_3)
  }

  test("Given player with 2 damage when players boosts then players moves 8 blocks") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(3, 4), Config.MUD_MAP_OBJECT)
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer1.pickupBoost()
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, useBoostCommand, nothingCommand)

    val expectedBlockNumber = 14
    val actualBlock = gameMap.asInstanceOf[CarGameMap].getPlayerBlockPosition(testCarGamePlayer1.getGamePlayerId())
    assert(actualBlock.getBlockNumber() == expectedBlockNumber)
  }

  test("Given player when players boosts through a path that gives 2 damage then players speed is 8") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(7, 8), Config.MUD_MAP_OBJECT)
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer1.pickupBoost()
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, useBoostCommand, nothingCommand)

    assert(testCarGamePlayer1.speed == Config.SPEED_STATE_3)
  }

  test("Given player when players boosts through a path that gives 2 damage then players moves 15 blocks") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(7, 8), Config.MUD_MAP_OBJECT)
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer1.pickupBoost()
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, useBoostCommand, useBoostCommand)

    val expectedBlockNumber = 21
    val actualPosition = gameMap.asInstanceOf[CarGameMap].getPlayerBlockPosition(testCarGamePlayer1.getGamePlayerId())
    assert(actualPosition.getBlockNumber() == expectedBlockNumber)
  }

  test("Given player with 1 damage when players boosts then players speed is 9") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(3), Config.MUD_MAP_OBJECT)
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer1.pickupBoost()
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, useBoostCommand, nothingCommand)

    assert(testCarGamePlayer1.speed == Config.MAXIMUM_SPEED)
  }

  test("Given player with 0 damage when players boosts then players speed is 15") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer1.pickupBoost()
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, useBoostCommand, nothingCommand)

    assert(testCarGamePlayer1.speed == Config.BOOST_SPEED)
  }
}
