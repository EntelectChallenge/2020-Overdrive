package test

import org.scalatest.FunSuite
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class Command_Nothing_Tests extends FunSuite{
  private val commandText = "NOTHING";
  private val commandFactory = new CommandFactory;
  private val nothingCommand = commandFactory.makeCommand(commandText);

  test("Given players at start of race when NOTHING command then player moves forward at initial speed") {
    val gameMap = TestHelper.initialiseGame();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    nothingCommand.performCommand(gameMap, testGamePlayer1);

    val carGameMap = gameMap.asInstanceOf[CarGameMap];
    val player1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1.asInstanceOf[CarGamePlayer].getGamePlayerId());

    assert(player1Position.getLane() == Config.PLAYER_ONE_START_LANE && player1Position.getBlockNumber() == Config.PLAYER_ONE_START_BLOCK + Config.INITIAL_SPEED);
  }

  test("Given player during the middle of a race when NOTHING command then player moves forward according to speed") {
    val gameMap = TestHelper.initialiseGame();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newLaneMidRace = 2;
    val newBlockNumberMidRace = 56;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace);

    val speedBeforeProcessingCommand = testCarGamePlayer1.getSpeed();
    nothingCommand.performCommand(gameMap, testGamePlayer1);

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert(newPlayer1PositionAfterCommand.getLane() == newLaneMidRace && newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace + speedBeforeProcessingCommand);
  }

  test("Given player near finish line when NOTHING command then player stops at finish line") {
    val gameMap = TestHelper.initialiseGame();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newLaneEndOfRace = 2;
    val newBlockNumberEndOfRace = Config.TRACK_LENGTH - 4;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneEndOfRace, newBlockNumberEndOfRace);

    nothingCommand.performCommand(gameMap, testGamePlayer1);

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert(newPlayer1PositionAfterCommand.getLane() == newLaneEndOfRace && newPlayer1PositionAfterCommand.getBlockNumber() == Config.TRACK_LENGTH);
  }

}
