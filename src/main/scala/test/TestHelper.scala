package test

import za.co.entelect.challenge.game.contracts.game.{CarMapGenerator, GamePlayer}
import za.co.entelect.challenge.game.contracts.map.{BlockPosition, CarGameMap, GameMap}
import za.co.entelect.challenge.game.contracts.player.Player
import scala.collection.JavaConverters._

object TestHelper {
  private val seed = 12;
  private val carMapGenerator = new CarMapGenerator(seed);

  private val testPlayer1 = new TestPlayer("Test Player A");
  private val testPlayer2 = new TestPlayer("Test Player B");

  def putPlayerSomewhereOnTheTrack(carGameMap: CarGameMap, testCarGamePlayerId: Int, newLane: Int, newBlockNumber:Int) = {
    val player1Position = carGameMap.getPlayerBlockPosition(testCarGamePlayerId);
    carGameMap.vacateBlock(player1Position);
    val newPositionMidRace = new BlockPosition(newLane, newBlockNumber);
    carGameMap.occupyBlock(newPositionMidRace, testCarGamePlayerId);
  }

  def getTestGamePlayer2(): GamePlayer = {
    return testPlayer2.getGamePlayer();
  }

  def getTestGamePlayer1(): GamePlayer = {
    return testPlayer1.getGamePlayer();
  }

   def initialiseGame(): GameMap = {
    val testPlayers = new Array[Player](2);
    testPlayers(0) = testPlayer1;
    testPlayers(1) = testPlayer2;
    val testPlayersJava = testPlayers.toList.asJava;
    val gameMap = carMapGenerator.generateGameMap(testPlayersJava);
    return gameMap;
  }
}
