package za.co.entelect.challenge.game.contracts.map

import java.util

import za.co.entelect.challenge.game.contracts.common.RefereeMessage
import za.co.entelect.challenge.game.contracts.game.{CarGamePlayer, GamePlayer}
import za.co.entelect.challenge.game.contracts.player.Player

import scala.collection.JavaConverters._
import za.co.entelect.challenge.game.contracts.Config.Config
import scala.collection.mutable

class CarGameMap(players: util.List[Player], mapGenerationSeed: Int, lanes: Int, trackLength: Int, blocks: Array[Block], var round: Int) extends GameMap {

  override def getCurrentRound: Int = {
    return round;
  }

  override def setCurrentRound(currentRound: Int): Unit = {
    round = currentRound;
  }

  override def getWinningPlayer: GamePlayer = {
    val winningPlayers = mutable.ListBuffer[GamePlayer]();
    for (i <- 0 to (players.size() - 1)) {
      val gamePlayer = players.get(i).getGamePlayer();
      val carGamePlayer = gamePlayer.asInstanceOf[CarGamePlayer];
      if (carGamePlayer.getState() == Config.FINISHED_PLAYER_STATE)
      {
        winningPlayers.addOne(gamePlayer);
      }
    }

    if (winningPlayers.size == 0) {
      return null;
    } 
    else if (winningPlayers.size == 1) {
      val firstPlayerAcrossTheLine = winningPlayers(0);
      return firstPlayerAcrossTheLine;
    } 
    else
    {
      val winnersSortedBySpeed = winningPlayers.sortBy(x => x.asInstanceOf[CarGamePlayer].getSpeed());
      val winnersWithTheHighestSpeed = winnersSortedBySpeed.filter(x => x.asInstanceOf[CarGamePlayer].getSpeed() == winnersSortedBySpeed.last.asInstanceOf[CarGamePlayer].getSpeed());
      val fastestPlayerWithHighestScore = winnersWithTheHighestSpeed.sortBy(x => x.getScore()).last;
      return fastestPlayerWithHighestScore;
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
    val playerPowerups = gameplayer.getPowerups();
    val isBoosting = gameplayer.isBoosting();
    val playerBoostCounter = gameplayer.getBoostCounter();
    val player = new MapFragmentPlayer(gamePlayerId, playerBlockPosition, playerSpeed, playerState, playerPowerups, isBoosting, playerBoostCounter);
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

  def blockIsOccupied(position: BlockPosition): Boolean = {
    val laneOfInterest = position.getLane();
    val blockNumberOfInterest = position.getLane();
    val blockOfInterest = blocks.find(x => x.getPosition().getLane() == laneOfInterest && x.getPosition().getBlockNumber() == blockNumberOfInterest);
    val hasPlayer = blockOfInterest.isDefined && (blockOfInterest.get.getOccupiedByPlayerWithId() != Config.EMPTY_PLAYER);
    return hasPlayer;
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
    val pathIncludesMud = pathIncludesMapObject(startPosition, endPosition, Config.MUD_MAP_OBJECT);
    return pathIncludesMud;
  }

  def pathIncludesOilSpill(startPosition: BlockPosition, endPosition: BlockPosition): Boolean =
    pathIncludesMapObject(startPosition, endPosition, Config.OIL_SPILL_MAP_OBJECT)

  def pathIncludesOilItem(startPosition: BlockPosition, endPosition: BlockPosition): Boolean =
    pathIncludesMapObject(startPosition, endPosition, Config.OIL_ITEM_MAP_OBJECT)

  def pathIncludesBoost(startPosition: BlockPosition, endPosition: BlockPosition): Boolean = {
    val pathIncludesBoost = pathIncludesMapObject(startPosition, endPosition, Config.BOOST_MAP_OBJECT);
    return pathIncludesBoost;
  }

  def applyOilToBlock(position: BlockPosition) =
    getBlock(position).setMapObject(Config.OIL_SPILL_MAP_OBJECT)

  private def pathIncludesMapObject(startPosition: BlockPosition, endPosition: BlockPosition, mapObject: Int): Boolean = {
    val startLane = startPosition.getLane();
    val startBlockNumber = startPosition.getBlockNumber();
    val endLane = endPosition.getLane();
    val endBlockNumber = endPosition.getBlockNumber();
    val blocksWithObject =
      blocks.find(x =>
        (x.getPosition().getLane() == endLane && x.getPosition().getBlockNumber() >= startBlockNumber && x.getPosition().getBlockNumber() <= endBlockNumber) &&
          x.getMapObject() == mapObject
      );
    val pathIncludesObject = blocksWithObject.isDefined;
    return pathIncludesObject;
  }

  def getBlocks(): Array[Block] = {
    return blocks;
  }
}
