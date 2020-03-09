import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer

class MapObject_Mud_Tests extends FunSuite{
  private val commandText = "NOTHING";
  private val commandFactory = new CommandFactory;
  private val nothingCommand = commandFactory.makeCommand(commandText);

  test("Given players during race when player hits mud then speed is reduced") {
    val gameMap = TestHelper.initaliseGameWithMapObjectAt(1, 3, Config.MUD_MAP_OBJECT);
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();

    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer];
    testCarGamePlayer.speed = Config.SPEED_STATE_2;
    nothingCommand.performCommand(gameMap, testGamePlayer1);

    assert(testCarGamePlayer.speed == Config.SPEED_STATE_1);
  }

  test("Given player that is boosting during race when player hits mud then speed is reduced and player is no longer boosting") {
    val gameMap = TestHelper.initaliseGameWithMapObjectAt(1, 3, Config.MUD_MAP_OBJECT);
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();

    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer];
    testCarGamePlayer.pickupBoost();
    testCarGamePlayer.useBoost();
    nothingCommand.performCommand(gameMap, testGamePlayer1);

    assert(testCarGamePlayer.speed == Config.MAXIMUM_SPEED);
    assert(testCarGamePlayer.isBoosting() == false);
  }

  test("Given player that is going slowest speed when player hits mud then player continues going slowest speed") {
    val gameMap = TestHelper.initaliseGameWithMapObjectAt(1, 3, Config.MUD_MAP_OBJECT);
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();

    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer];
    testCarGamePlayer.speed = Config.SPEED_STATE_1;
    nothingCommand.performCommand(gameMap, testGamePlayer1);

    assert(testCarGamePlayer.speed == Config.SPEED_STATE_1);
  }
}
