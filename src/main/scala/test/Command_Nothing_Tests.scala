package test

import org.scalatest.FunSuite
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.{CarGamePlayer, CarMapGenerator, GameMapGenerator}
import za.co.entelect.challenge.game.contracts.map.{BlockPosition, CarGameMap, GameMap}
import za.co.entelect.challenge.game.contracts.player.Player

import scala.collection.JavaConverters._

class Command_Nothing_Tests extends FunSuite{
  private val seed = 12;
  private val carMapGenerator = new CarMapGenerator(seed);

  private val testPlayer1 = new TestPlayer("Test Player A");
  private val testPlayer2 = new TestPlayer("Test Player B");

  private val commandText = "NOTHING";
  private val commandFactory = new CommandFactory;
  private val nothingCommand = commandFactory.makeCommand(commandText);

  test("Given players at start of race when NOTHING command then player moves forward at initial speed") {
    val gameMap = initialiseGame();

    val testGamePlayer1 = testPlayer1.getGamePlayer();
    nothingCommand.performCommand(gameMap, testGamePlayer1);

    val carGameMap = gameMap.asInstanceOf[CarGameMap];
    val player1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1.asInstanceOf[CarGamePlayer].getGamePlayerId());

    assert(player1Position.getLane() == Config.PLAYER_ONE_START_LANE && player1Position.getBlockNumber() == Config.PLAYER_ONE_START_BLOCK + Config.INITIAL_SPEED);
  }

  test("Given player during the middle of a race when NOTHING command then player moves forward according to speed") {
    val gameMap = initialiseGame();
    val testGamePlayer1 = testPlayer1.getGamePlayer();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newLaneMidRace = 2;
    val newBlockNumberMidRace = 56;
    putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRace, newBlockNumberMidRace);

    val speedBeforeProcessingCommand = testCarGamePlayer1.getSpeed();
    nothingCommand.performCommand(gameMap, testGamePlayer1);

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert(newPlayer1PositionAfterCommand.getLane() == newLaneMidRace && newPlayer1PositionAfterCommand.getBlockNumber() == newBlockNumberMidRace + speedBeforeProcessingCommand);
  }

  def putPlayerSomewhereOnTheTrack(carGameMap: CarGameMap, testCarGamePlayerId: Int, newLane: Int, newBlockNumber:Int) = {
    val player1Position = carGameMap.getPlayerBlockPosition(testCarGamePlayerId);
    carGameMap.vacateBlock(player1Position);
    val newPositionMidRace = new BlockPosition(newLane, newBlockNumber);
    carGameMap.occupyBlock(newPositionMidRace, testCarGamePlayerId);
  }

  private def initialiseGame(): GameMap = {
    val testPlayers = new Array[Player](2);
    testPlayers(0) = testPlayer1;
    testPlayers(1) = testPlayer2;
    val testPlayersJava = testPlayers.toList.asJava;
    val gameMap = carMapGenerator.generateGameMap(testPlayersJava);
    return gameMap;
  }
}
