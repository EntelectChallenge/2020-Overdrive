package test

import org.scalatest.FunSuite
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.{CarGamePlayer}
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class Command_Turn_Left_Tests extends FunSuite{
  private val commandText = "TURN_LEFT";
  private var commandFactory: CommandFactory = null
  private var turnLeftCommand: RawCommand = null

  def initialise() = {
    Config.loadDefault();
    commandFactory = new CommandFactory;
    turnLeftCommand = commandFactory.makeCommand(commandText)
    turnLeftCommand.setCommand(commandText)
  }

  test("Given start of race when TURN_LEFT command then player turns left and incurs change lane penalty") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();

    val testGamePlayer2 = TestHelper.getTestGamePlayer2();

    TestHelper.processRound(gameMap, turnLeftCommand, turnLeftCommand)

    val carGameMap = gameMap.asInstanceOf[CarGameMap];
    val player2Position = carGameMap.getPlayerBlockPosition(testGamePlayer2.asInstanceOf[CarGamePlayer].getGamePlayerId());

    assert((player2Position.getLane() == Config.PLAYER_TWO_START_LANE-1) && (player2Position.getBlockNumber() == Config.PLAYER_TWO_START_BLOCK + Config.INITIAL_SPEED-1));
  }

  test("Given player in lane 1 when TURN_LEFT command player stays in lane 1 and incurs change lane penalty") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();

    val testGamePlayer1 = TestHelper.getTestGamePlayer1();

    TestHelper.processRound(gameMap, turnLeftCommand, turnLeftCommand)

    val carGameMap = gameMap.asInstanceOf[CarGameMap];
    val player1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1.asInstanceOf[CarGamePlayer].getGamePlayerId());

    assert((player1Position.getLane() == Config.PLAYER_ONE_START_LANE) && (player1Position.getBlockNumber() == Config.PLAYER_ONE_START_BLOCK + Config.INITIAL_SPEED-1));
  }

  test("Given player during a race when TURN_LEFT then player turns left and incurs change lane penalty") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newLaneMidRace = 2;
    val newBlockNumberMidRace = 56;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace);

    val speedBeforeProcessingCommand = testCarGamePlayer1.getSpeed();

    TestHelper.processRound(gameMap, turnLeftCommand, turnLeftCommand)

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace-1) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace + speedBeforeProcessingCommand - 1));
  }

  test ("Given player near finish line when TURN_LEFT then player stops at finish line") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newLaneMidRace = 2;
    val newBlockNumberMidRace = Config.TRACK_LENGTH - 3;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace);

    val speedBeforeProcessingCommand = testCarGamePlayer1.getSpeed();

    TestHelper.processRound(gameMap, turnLeftCommand, turnLeftCommand)

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace-1) && (newPlayer1PositionAfterCommand.getBlockNumber() == Config.TRACK_LENGTH));
  }

  test("Given player that is stopped when TURN_LEFT then player does not move") {
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

    TestHelper.processRound(gameMap, turnLeftCommand, turnLeftCommand)

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace));
  }

  test("Given player that is boosting when TURN_LEFT command then player changes lanes, incurs change lane penalty and moves at boost speed") {
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

    TestHelper.processRound(gameMap, turnLeftCommand, turnLeftCommand)

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert((newPlayer1PositionAfterCommand.getLane() == newLaneMidRace-1) && (newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace + Config.BOOST_SPEED - 1));
    assert(testCarGamePlayer1.isBoosting() == true);
  }
}
