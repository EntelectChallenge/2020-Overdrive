import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer

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
}
