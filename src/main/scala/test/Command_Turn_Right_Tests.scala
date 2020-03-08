package test

import org.scalatest.FunSuite
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class Command_Turn_Right_Tests extends FunSuite{
  private val commandText = "TURN_RIGHT";
  private val commandFactory = new CommandFactory;
  private val turnRightCommand = commandFactory.makeCommand(commandText);

  test("Given start of race when turn right command then player turns right and incurs change lane penalty") {
    val gameMap = TestHelper.initialiseGame();

    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    turnRightCommand.performCommand(gameMap, testGamePlayer1);

    val carGameMap = gameMap.asInstanceOf[CarGameMap];
    val player1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1.asInstanceOf[CarGamePlayer].getGamePlayerId());

    assert((player1Position.getLane() == Config.PLAYER_ONE_START_LANE+1) && (player1Position.getBlockNumber() == Config.PLAYER_ONE_START_BLOCK + Config.INITIAL_SPEED-1));
  }

  test("Given player in lane 4 when turn right command player stays in lane 4 and incurs change lane penalty") {
    val gameMap = TestHelper.initialiseGame();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val maxLane = 4;
    val newBlockNumberMidRace = 56;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, maxLane, newBlockNumberMidRace);

    val speedBeforeProcessingCommand = testCarGamePlayer1.getSpeed();
    turnRightCommand.performCommand(gameMap, testGamePlayer1);

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert((newPlayer1PositionAfterCommand.getLane() == maxLane) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace + speedBeforeProcessingCommand - 1));
  }

  test("Given player during a race when turn left then player turns left and incurs change lane penalty") {
    val gameMap = TestHelper.initialiseGame();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newLaneMidRace = 2;
    val newBlockNumberMidRace = 56;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace);

    val speedBeforeProcessingCommand = testCarGamePlayer1.getSpeed();
    turnRightCommand.performCommand(gameMap, testGamePlayer1);

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace+1) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace + speedBeforeProcessingCommand - 1));
  }
}
