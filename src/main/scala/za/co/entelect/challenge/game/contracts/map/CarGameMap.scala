package za.co.entelect.challenge.game.contracts.map

import java.util

import za.co.entelect.challenge.game.contracts.common.RefereeMessage
import za.co.entelect.challenge.game.contracts.game.{CarGamePlayer, GamePlayer}
import za.co.entelect.challenge.game.contracts.player.Player

import scala.collection.JavaConverters._
import za.co.entelect.challenge.game.contracts.Config.Config

class CarGameMap(players: util.List[Player], mapGenerationSeed: Int, lanes: Int, trackLength: Int, blocks: Array[Block], var round: Int) extends GameMap {

  override def getCurrentRound: Int = {
    return round;
  }

  override def setCurrentRound(currentRound: Int): Unit = {
    round = currentRound;
  }

  override def getWinningPlayer: GamePlayer = {
    if (round > 2) {
      var firstPlayer = players.get(0);
      var firstGamePlayer = firstPlayer.getGamePlayer();
      return firstGamePlayer;
    }
    else {
      return null;
    }
  }

  override def getRefereeIssues: RefereeMessage = {
    throw new NotImplementedError("Car game map get referee issues");
  }

  def getMapFragment(gameplayer: CarGamePlayer): CarGameMapFragment = {
    val gamePlayerId = gameplayer.getGamePlayerId();
    val playerBlockPosition = getPlayerBlockPosition(gamePlayerId);
    val playerSpeed = gameplayer.getSpeed();
    val playerState = gameplayer.getState();
    val player = new MapFragmentPlayer(gamePlayerId, playerBlockPosition, playerSpeed, playerState);
    val lanes = blocks.filter(block => ((scala.math.abs(playerBlockPosition.getBlockNumber() - block.getPosition().getBlockNumber()) <= Config.BACKWARDS_VISIBILITY) || (scala.math.abs(block.getPosition().getBlockNumber() - playerBlockPosition.getBlockNumber()) <= Config.FOREWARDS_VISIBILITY)));
    val carGameMapFragment = new CarGameMapFragment(round, player, lanes);
    return carGameMapFragment;
  }

  def getPlayerBlockPosition(gameplayerId: Int): BlockPosition = {
    val indexOfPlayerOnTrack = blocks.indexWhere(x => x.occupiedByPlayerWithId == gameplayerId);
    val playerBlock = blocks(indexOfPlayerOnTrack);
    val playerBlockPosition = playerBlock.getPosition();
    return playerBlockPosition;
  }

  def getGamePlayers(): Array[GamePlayer] = {
    var gamePlayers = new Array[GamePlayer](players.size());
    for ( i <- 0 to (players.size() - 1)) {
      val player = players.get(i);
      val gamePlayer = player.getGamePlayer();
      gamePlayers(i) = gamePlayer;
    };
    return gamePlayers;
  }

  def getCarGamePlayers(): Array[CarGamePlayer] = {
    var carGamePlayers = new Array[CarGamePlayer](players.size());
    for ( i <- 0 to (players.size() - 1)) {
      val player = players.get(i);
      val gamePlayer = player.getGamePlayer();
      val carGamePlayer = gamePlayer.asInstanceOf[CarGamePlayer];
      carGamePlayers(i) = carGamePlayer;
    };
    return carGamePlayers;
  }

  def vacateBlock(position: BlockPosition) = {
    val blockToVacate = getBlock(position);
    blockToVacate.vacate();
  }

  def occupyBlock(position: BlockPosition, gamePlayerId: Int) = {
    val blockToOccupy = getBlock(position);
    blockToOccupy.occupy(gamePlayerId);
  }

  private def getBlock(position: BlockPosition): Block = {
    val laneOfInterest = position.getLane();
    val blockNumberOfInterest = position.getBlockNumber();
    val indexOfBlockOfInterest = blocks.indexWhere(x => x.getPosition().getLane() == laneOfInterest && x.getPosition().getBlockNumber() == blockNumberOfInterest);
    val blockOfInterest = blocks(indexOfBlockOfInterest);
    return blockOfInterest;
  }

  def pathIncludesMud(startPosition: BlockPosition, endPosition: BlockPosition): Boolean = {
    val startLane = startPosition.getLane();
    val startBlockNumber = startPosition.getBlockNumber();
    val endLane = endPosition.getLane();
    val endBlockNumber = endPosition.getBlockNumber();
    val blocksWithMud = 
      blocks.find(x => 
        (x.getPosition().getLane() == endLane && x.getPosition().getBlockNumber() >= startBlockNumber && x.getPosition().getBlockNumber() <= endBlockNumber) &&
        x.getMapObject() == Config.MUD_MAP_OBJECT
      );
    val pathIncludesMud = blocksWithMud.isDefined;
    return pathIncludesMud;
  }

  def getBlocks(): Array[Block] = {
    return blocks;
  }
}
