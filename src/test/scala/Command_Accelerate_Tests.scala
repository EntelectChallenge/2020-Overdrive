import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class Command_Accelerate_Tests extends FunSuite{
  private val commandText = "ACCELERATE";
  private val commandFactory = new CommandFactory;
  private val accelerateCommand = commandFactory.makeCommand(commandText);

  test("Given player at start of race when ACCELERATE command that player moves at speed state 2") {
    val gameMap = TestHelper.initialiseGame();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    accelerateCommand.performCommand(gameMap, testGamePlayer1);

    val carGameMap = gameMap.asInstanceOf[CarGameMap];
    val player1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1.asInstanceOf[CarGamePlayer].getGamePlayerId());

    assert(player1Position.getLane() == Config.PLAYER_ONE_START_LANE && player1Position.getBlockNumber() == Config.PLAYER_ONE_START_BLOCK + Config.SPEED_STATE_2);
  }

  test("Given player that is stopped when ACCELERATE command then player moves at speed state 1") {
    val gameMap = TestHelper.initialiseGame();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newLaneMidRace = 2;
    val newBlockNumberMidRace = 56;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace);

    testCarGamePlayer1.speed = 0; //stop player
    accelerateCommand.performCommand(gameMap, testGamePlayer1);

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace + Config.SPEED_STATE_1));
  }

  test("Given player that is boosting when ACCELERATE command then player continues to move at boost speed") {
    val gameMap = TestHelper.initialiseGame();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    testCarGamePlayer1.useBoost();

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newLaneMidRace = 2;
    val newBlockNumberMidRace = 56;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace);

    accelerateCommand.performCommand(gameMap, testGamePlayer1);

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace + Config.BOOST_SPEED));
    assert(testCarGamePlayer1.isBoosting() == true);
  }

  test("Given player during the race when ACCELERATE command then player moves at next speed state") {
    val gameMap = TestHelper.initialiseGame();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newLaneMidRace = 2;
    val newBlockNumberMidRace = 56;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace);

    testCarGamePlayer1.speed = Config.SPEED_STATE_2;
    accelerateCommand.performCommand(gameMap, testGamePlayer1);

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace + Config.SPEED_STATE_3));
  }

  test("Given player moving at max speed when ACCELERATE command then player continues to move at max speed") {
    val gameMap = TestHelper.initialiseGame();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newLaneMidRace = 2;
    val newBlockNumberMidRace = 56;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace);

    testCarGamePlayer1.speed = Config.MAXIMUM_SPEED;
    accelerateCommand.performCommand(gameMap, testGamePlayer1);

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace + Config.MAXIMUM_SPEED));
  }
}
