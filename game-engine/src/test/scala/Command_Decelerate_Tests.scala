import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class Command_Decelerate_Tests extends FunSuite{
  private val commandText = "DECELERATE";
  private var commandFactory: CommandFactory = null;
  private var decelerateCommand: RawCommand = null

  def initialise() = {
    Config.loadDefault();
    commandFactory = new CommandFactory;
    decelerateCommand = commandFactory.makeCommand(commandText)
  }

  test("Given player at start of race when DECELERATE command that player moves at speed state 1") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    decelerateCommand.performCommand(gameMap, testGamePlayer1);

    val carGameMap = gameMap.asInstanceOf[CarGameMap];
    val player1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1.asInstanceOf[CarGamePlayer].getGamePlayerId());

    assert(player1Position.getLane() == Config.PLAYER_ONE_START_LANE && player1Position.getBlockNumber() == Config.PLAYER_ONE_START_BLOCK + Config.SPEED_STATE_1);
  }

  test("Given player that is stopped when DECELERATE command then player does not move") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newLaneMidRace = 2;
    val newBlockNumberMidRace = 56;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace);

    testCarGamePlayer1.speed = 0; //stop player
    decelerateCommand.performCommand(gameMap, testGamePlayer1);

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace));
  }

  test("Given player that is boosting when DECELERATE command then player moves at maximum speed and is no longer boosting") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    testCarGamePlayer1.useBoost();

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newLaneMidRace = 2;
    val newBlockNumberMidRace = 56;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace);

    decelerateCommand.performCommand(gameMap, testGamePlayer1);

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace + Config.MAXIMUM_SPEED));
    assert(testCarGamePlayer1.isBoosting() == false);
  }

  test("Given player during the race when DECELERATE command then player moves at previous speed state") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newLaneMidRace = 2;
    val newBlockNumberMidRace = 56;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace);

    testCarGamePlayer1.speed = Config.SPEED_STATE_2;
    decelerateCommand.performCommand(gameMap, testGamePlayer1);

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace + Config.SPEED_STATE_1));
  }

  test("Given player moving at slowest speed when DECELERATE command then player stops") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newLaneMidRace = 2;
    val newBlockNumberMidRace = 56;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace);

    testCarGamePlayer1.speed = Config.SPEED_STATE_1;
    decelerateCommand.performCommand(gameMap, testGamePlayer1);

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace));
  }
}
