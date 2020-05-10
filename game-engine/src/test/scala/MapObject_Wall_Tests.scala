import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class MapObject_Wall_Tests extends FunSuite{
  private val commandText = "NOTHING";
  private val turnRightCommandText = "TURN_RIGHT";
  private var commandFactory: CommandFactory = null;
  private var nothingCommand: RawCommand = null;
  private var turnRightCommand: RawCommand = null

  def initialise() = {
    Config.loadDefault();
    commandFactory = new CommandFactory;
    nothingCommand = commandFactory.makeCommand(commandText)
    nothingCommand.setCommand(commandText)
    turnRightCommand = commandFactory.makeCommand(turnRightCommandText)
    turnRightCommand.setCommand(turnRightCommandText)
  }

  test("Given players during race when player hits a wall then speed is reduced to 3") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.WALL_MAP_OBJECT);
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();

    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer];
    testCarGamePlayer.speed = Config.SPEED_STATE_3;

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    assert(testCarGamePlayer.speed == Config.SPEED_STATE_1);
  }

  test("Given players during race when player hits a wall twice then speed should be 3 again.") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(3,4), Config.WALL_MAP_OBJECT);
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();

    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer];
    testCarGamePlayer.speed = Config.SPEED_STATE_2;

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    assert(testCarGamePlayer.speed == Config.SPEED_STATE_1);
  }

  test("Given player that is boosting during race when player hits a wall then speed is reduced and player is no longer boosting") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.WALL_MAP_OBJECT);
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();

    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer];
    testCarGamePlayer.pickupBoost();
    testCarGamePlayer.useBoost();

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    assert(testCarGamePlayer.speed == Config.SPEED_STATE_1);
    assert(testCarGamePlayer.isBoosting() == false);
  }

  test("Given player that is going slowest speed when player hits wall then player continues going slowest speed") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.WALL_MAP_OBJECT);
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();

    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer];
    testCarGamePlayer.speed = Config.SPEED_STATE_1;

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    assert(testCarGamePlayer.speed == Config.SPEED_STATE_1);
  }

  test("Given a player that is turning onto a wall block continues going slowest speed") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMapObjectAt(2, 1, Config.WALL_MAP_OBJECT);
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();

    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer];
    testCarGamePlayer.speed = Config.SPEED_STATE_2;

    TestHelper.processRound(gameMap, turnRightCommand, nothingCommand)

    val carGameMap = gameMap.asInstanceOf[CarGameMap];
    val player1PositionEnd = carGameMap.getPlayerBlockPosition(testGamePlayer1.asInstanceOf[CarGamePlayer].getGamePlayerId());

    assert(testCarGamePlayer.speed == Config.SPEED_STATE_1);
    assert((player1PositionEnd.getLane() == Config.PLAYER_ONE_START_LANE+1));
    assert((player1PositionEnd.getBlockNumber() == 6));
  }
}