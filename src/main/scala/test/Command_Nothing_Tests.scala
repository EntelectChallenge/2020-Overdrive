package test

import org.scalatest.FunSuite
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.{CarGamePlayer, CarMapGenerator, GameMapGenerator}
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.player.Player

import scala.collection.JavaConverters._

class Command_Nothing_Tests extends FunSuite{
  private val seed = 12;
  private val carMapGenerator = new CarMapGenerator(seed);

  private val commandText = "NOTHING";
  private val commandFactory = new CommandFactory;
  private val nothingCommand = commandFactory.makeCommand(commandText);

  test("Given player at start of race when NOTHING command given then player moves forward 5 blocks") {
    val testPlayer1 = new TestPlayer("Test Player A");
    val testPlayer2 = new TestPlayer("Test Player B");
    val testPlayers = new Array[Player](2);
    testPlayers(0) = testPlayer1;
    testPlayers(1) = testPlayer2;
    val testPlayersJava = testPlayers.toList.asJava;
    val gameMap = carMapGenerator.generateGameMap(testPlayersJava);

    val testGamePlayer1 = testPlayer1.getGamePlayer();
    nothingCommand.performCommand(gameMap, testGamePlayer1);

    val carGameMap = gameMap.asInstanceOf[CarGameMap];
    val player1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1.asInstanceOf[CarGamePlayer].getGamePlayerId());

    assert(player1Position.getLane() == 1 && player1Position.getBlockNumber() == 6);
  }
}
