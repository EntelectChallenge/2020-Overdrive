package test

import org.scalatest.FunSuite
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.{CarGamePlayer, CarMapGenerator}
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class Command_Turn_Left_Tests extends FunSuite{
  private val commandText = "TURN_LEFT";
  private val commandFactory = new CommandFactory;
  private val turnLeftCommand = commandFactory.makeCommand(commandText);

  test("Given start of race when TURN_LEFT command then player turns left and incurs change lane penalty") {
    val gameMap = TestHelper.initialiseGame();

    val testGamePlayer2 = TestHelper.getTestGamePlayer2();
    turnLeftCommand.performCommand(gameMap, testGamePlayer2);

    val carGameMap = gameMap.asInstanceOf[CarGameMap];
    val player2Position = carGameMap.getPlayerBlockPosition(testGamePlayer2.asInstanceOf[CarGamePlayer].getGamePlayerId());

    assert((player2Position.getLane() == Config.PLAYER_TWO_START_LANE-1) && (player2Position.getBlockNumber() == Config.PLAYER_TWO_START_BLOCK + Config.INITIAL_SPEED-1));
  }

  test("Given player in lane 1 when TURN_LEFT command player stays in lane 1 and incurs change lane penalty") {
    val gameMap = TestHelper.initialiseGame();

    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    turnLeftCommand.performCommand(gameMap, testGamePlayer1);

    val carGameMap = gameMap.asInstanceOf[CarGameMap];
    val player1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1.asInstanceOf[CarGamePlayer].getGamePlayerId());

    assert((player1Position.getLane() == Config.PLAYER_ONE_START_LANE) && (player1Position.getBlockNumber() == Config.PLAYER_ONE_START_BLOCK + Config.INITIAL_SPEED-1));
  }

  test("Given player during a race when TURN_LEFT then player turns left and incurs change lane penalty") {
    val gameMap = TestHelper.initialiseGame();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newLaneMidRace = 2;
    val newBlockNumberMidRace = 56;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace);

    val speedBeforeProcessingCommand = testCarGamePlayer1.getSpeed();
    turnLeftCommand.performCommand(gameMap, testGamePlayer1);

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace-1) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace + speedBeforeProcessingCommand - 1));
  }

  test ("Given player near finish line when TURN_LEFT then player stops at finish line") {
    val gameMap = TestHelper.initialiseGame();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newLaneMidRace = 2;
    val newBlockNumberMidRace = Config.TRACK_LENGTH - 3;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace);

    val speedBeforeProcessingCommand = testCarGamePlayer1.getSpeed();
    turnLeftCommand.performCommand(gameMap, testGamePlayer1);

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace-1) && (newPlayer1PositionAfterCommand.getBlockNumber() == Config.TRACK_LENGTH));
  }
}
