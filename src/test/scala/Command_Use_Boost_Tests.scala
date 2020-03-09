import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer

class Command_Use_Boost_Tests extends FunSuite{
  private val commandText = "USE_BOOST";
  private val commandFactory = new CommandFactory;
  private val useBoostCommand = commandFactory.makeCommand(commandText);

  test("Given player with no boost when USE_BOOST command then nothing happens") {
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];

    useBoostCommand.performCommand(gameMap, testGamePlayer1);

    assert(testCarGamePlayer1.isBoosting() == false);
    assert(testCarGamePlayer1.speed != Config.BOOST_SPEED);
  }

  test("Given player with 3 boosts when USE_BOOST command then player has 2 boosts left") {
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];

    testCarGamePlayer1.pickupBoost();
    testCarGamePlayer1.pickupBoost();
    testCarGamePlayer1.pickupBoost();
    useBoostCommand.performCommand(gameMap, testGamePlayer1);

    assert(testCarGamePlayer1.isBoosting() == true);
    assert(testCarGamePlayer1.getPowerups().count(x => x == Config.BOOST_POWERUP_ITEM) == 2);
  }

  test("Given player with a boost when USE_BOOST command then player moves at boost speed") {
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];

    testCarGamePlayer1.pickupBoost();
    useBoostCommand.performCommand(gameMap, testGamePlayer1);

    assert(testCarGamePlayer1.isBoosting() == true);
    assert(testCarGamePlayer1.speed == Config.BOOST_SPEED);
  }

  test("Given player with a boost when USE_BOOST command then boost counter ticks down after each command") {
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];

    testCarGamePlayer1.pickupBoost();
    useBoostCommand.performCommand(gameMap, testGamePlayer1);

    commandFactory.makeCommand(Config.NOTHING_COMMAND).performCommand(gameMap, testCarGamePlayer1);
    assert(testCarGamePlayer1.getBoostCounter() == Config.BOOST_DURATION - 1);
  }

  test("Given player that is boosting when boost counter reaches 0 then player is no longer boosting and speed is at most maximum speed") {
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];

    testCarGamePlayer1.pickupBoost();
    useBoostCommand.performCommand(gameMap, testGamePlayer1);

    testCarGamePlayer1.tickBoost();
    testCarGamePlayer1.tickBoost();
    testCarGamePlayer1.tickBoost();
    testCarGamePlayer1.tickBoost();
    testCarGamePlayer1.tickBoost();
    commandFactory.makeCommand(Config.NOTHING_COMMAND).performCommand(gameMap, testCarGamePlayer1);

    assert(testCarGamePlayer1.isBoosting() == false);
    assert(testCarGamePlayer1.speed != Config.BOOST_SPEED);
  }
}
