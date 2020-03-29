package za.co.entelect.challenge.game.contracts.map

import java.util

import za.co.entelect.challenge.game.contracts.common.RefereeMessage
import za.co.entelect.challenge.game.contracts.game.{CarGamePlayer, GamePlayer}
import za.co.entelect.challenge.game.contracts.player.Player

import scala.collection.JavaConverters._
import za.co.entelect.challenge.game.contracts.Config.Config
import scala.collection.mutable

class CarGameMap(players: util.List[Player], mapGenerationSeed: Int, lanes: Int, trackLength: Int, var blocks: Array[Block], var round: Int) extends GameMap {

  var stagedFuturePositions: List[StagedPosition] = List[StagedPosition]()

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
    val score = gameplayer.getScore();
    val player = new MapFragmentPlayer(gamePlayerId, playerBlockPosition, playerSpeed, playerState, playerPowerups,
      isBoosting, playerBoostCounter, score);

    val lanes = blocks.filter(block =>
      (((playerBlockPosition.getBlockNumber() >= block.getPosition().getBlockNumber()) && (scala.math.abs(playerBlockPosition.getBlockNumber() - block.getPosition().getBlockNumber()) <= Config.BACKWARD_VISIBILITY))
        || ((playerBlockPosition.getBlockNumber() <= block.getPosition().getBlockNumber()) && (scala.math.abs(block.getPosition().getBlockNumber() - playerBlockPosition.getBlockNumber()) <= Config.FORWARD_VISIBILITY))));

    val opponentGamePlayer = getCarGamePlayers().find(p => p.getGamePlayerId() != gameplayer.getGamePlayerId()).get
    val opponentGamePlayerId = opponentGamePlayer.getGamePlayerId();
    val opponentBlock = getPlayerBlockPosition(opponentGamePlayerId);

    val opponent = new MapFragmentPlayer(opponentGamePlayerId, opponentBlock, opponentGamePlayer.getSpeed(), opponentGamePlayer.getState(),
      opponentGamePlayer.getPowerups(), opponentGamePlayer.isBoosting(), opponentGamePlayer.getBoostCounter(), opponentGamePlayer.getScore);

    val carGameMapFragment = new CarGameMapFragment(round, player, opponent, lanes);
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
    val blockNumberOfInterest = position.getBlockNumber();
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

  def mudCountInPath(startPosition: BlockPosition, endPosition: BlockPosition): Int =
    numberOfMapObjectsInPath(startPosition, endPosition, Config.MUD_MAP_OBJECT);

  def oilSpillCountInPath(startPosition: BlockPosition, endPosition: BlockPosition): Int =
    numberOfMapObjectsInPath(startPosition, endPosition, Config.OIL_SPILL_MAP_OBJECT)

  def oilItemCountInPath(startPosition: BlockPosition, endPosition: BlockPosition): Int =
    numberOfMapObjectsInPath(startPosition, endPosition, Config.OIL_ITEM_MAP_OBJECT)

  def boostCountInPath(startPosition: BlockPosition, endPosition: BlockPosition): Int =
    numberOfMapObjectsInPath(startPosition, endPosition, Config.BOOST_MAP_OBJECT);

  def applyOilToBlock(position: BlockPosition) =
    getBlock(position).setMapObject(Config.OIL_SPILL_MAP_OBJECT)

  private def pathIncludesMapObject(startPosition: BlockPosition, endPosition: BlockPosition, mapObject: Int): Boolean = {
    val startLane = startPosition.getLane();
    val startBlockNumber = startPosition.getBlockNumber();
    val endLane = endPosition.getLane();
    val endBlockNumber = endPosition.getBlockNumber();
    val blocksWithObject =
      blocks.find(x =>
        (x.getPosition().getLane() == endLane && x.getPosition().getBlockNumber() >= startBlockNumber && x.getPosition().getBlockNumber() <= endBlockNumber)
            &&
          x.getMapObject() == mapObject
      );
    val pathIncludesObject = blocksWithObject.isDefined;
    return pathIncludesObject;
  }

  private def numberOfMapObjectsInPath(startPosition: BlockPosition, endPosition: BlockPosition, mapObject: Int): Int = {
    val startLane = startPosition.getLane();
    val startBlockNumber = startPosition.getBlockNumber();
    val endLane = endPosition.getLane();
    val endBlockNumber = endPosition.getBlockNumber();
    val blocksWithObject = blocks.count(b => (b.getPosition().getLane() == endLane 
                                               && b.getPosition().getBlockNumber() >= startBlockNumber 
                                               && b.getPosition().getBlockNumber() <= endBlockNumber)
                                           && b.getMapObject() == mapObject
      );
    return blocksWithObject;
  }

  def getBlocks(): Array[Block] = {
    return blocks;
  }

  def makeAllBlocksEmpty() = {
    val newBlocks = blocks.map(x => {
      new Block(new BlockPosition(x.getPosition().getLane(), x.getPosition().getBlockNumber()), Config.EMPTY_MAP_OBJECT, x.occupiedByPlayerWithId)
    });
    blocks = newBlocks;
  }

  def placeObjectAt(lane: Int, blockNumber: Int, mapObject: Int) = {
    blocks.find(x => x.getPosition().getLane() == lane && x.getPosition().getBlockNumber() == blockNumber).get.mapObject = mapObject;
  }

  def stageFuturePosition(stagedPosition: StagedPosition) ={
    stagedFuturePositions = stagedFuturePositions.appended(stagedPosition);
  }

  def resolvePlayerCollisions(): Boolean = {
    val player1StagedPosition = stagedFuturePositions.find(x => x.getPlayer().getGamePlayerId() == 1).get
    val player1FuturePosition = player1StagedPosition.getNewPosition()

    val player2StagedPosition = stagedFuturePositions.find(x => x.getPlayer().getGamePlayerId() == 2).get
    val player2FuturePosition = player2StagedPosition.getNewPosition()

    val playersFuturePositionsAreSame = (player1FuturePosition.getLane() == player2FuturePosition.getLane()) && (player1FuturePosition.getBlockNumber() == player2FuturePosition.getBlockNumber())

    val player1DroveIntoPlayer2 = (player1StagedPosition.getOldPosition().getLane() == player2StagedPosition.getOldPosition().getLane()) && //started off in the same lane
      (player1StagedPosition.getOldPosition().getBlockNumber() < player2StagedPosition.getOldPosition().getBlockNumber()) && //player 1 was behind
      (player1FuturePosition.getBlockNumber() >= player2FuturePosition.getBlockNumber()) && //player 1 ended up in front
      (player1FuturePosition.getLane() == player2FuturePosition.getLane()) //in the same lane => player 2 got ridden over

    val player2DroveIntoPlayer1 = (player2StagedPosition.getOldPosition().getLane() == player1StagedPosition.getOldPosition().getLane()) && //started off in the same lane
      (player2StagedPosition.getOldPosition().getBlockNumber() < player1StagedPosition.getOldPosition().getBlockNumber()) && //player 2 was behind
      (player2FuturePosition.getBlockNumber() >= player1FuturePosition.getBlockNumber()) && //player 2 ended up in front
      (player2FuturePosition.getLane() == player1FuturePosition.getLane()) //in the same lane => player 1 got ridden over

    val isCollisionFromBehind = player1DroveIntoPlayer2 || player2DroveIntoPlayer1

    val isCollision = playersFuturePositionsAreSame || isCollisionFromBehind

    if(!isCollision)
    {
      return false
    }

    if(isCollisionFromBehind)
    {
      val stagedPositionOfPlayerInFront = if(player1DroveIntoPlayer2) player2StagedPosition
      else player1StagedPosition

      val stagedPositionOfPlayerCollidingFromBehind = stagedFuturePositions.find(x => x != stagedPositionOfPlayerInFront).get
      val correctedBlockNumber = stagedPositionOfPlayerInFront.getNewPosition().getBlockNumber() - 1;
      val correctedLane = stagedPositionOfPlayerCollidingFromBehind.getNewPosition().getLane();
      val correctedPositionOfPlayerCollidingFromBehind = new BlockPosition(correctedLane, correctedBlockNumber);
      stagedPositionOfPlayerCollidingFromBehind.setNewPosition(correctedPositionOfPlayerCollidingFromBehind)

      return true
    }

    val collisionFromTheSide = playersFuturePositionsAreSame
    if(collisionFromTheSide)
    {
      val correctedPlayer1Lane = player1StagedPosition.getOldPosition().getLane()
      val correctedPlayer1BlockNumber = player1FuturePosition.getBlockNumber() - 1
      val correctedPlayer1FuturePosition = new BlockPosition(correctedPlayer1Lane, correctedPlayer1BlockNumber)
      player1StagedPosition.setNewPosition(correctedPlayer1FuturePosition)

      val correctedPlayer2Lane = player2StagedPosition.getOldPosition().getLane()
      val correctedPlayer2BlockNumber = player2FuturePosition.getBlockNumber() - 1
      val correctedPlayer2FuturePosition = new BlockPosition(correctedPlayer2Lane, correctedPlayer2BlockNumber)
      player2StagedPosition.setNewPosition(correctedPlayer2FuturePosition)
      return true
    }

    throw new Exception("A collision occurred that was not from the side or from behind. This should not be possible since cars cannot travel in reverse");
  }

  def commitStagedPositions() = {
    stagedFuturePositions.foreach(x => handlePostCommandLogic(x.getPlayer(), x.getNewPosition(), x.getOldPosition()))
    stagedFuturePositions = List[StagedPosition]()
  }

  def handlePostCommandLogic(carGamePlayer: CarGamePlayer, newPosition: BlockPosition, oldPosition: BlockPosition) = {
    //handle collisions with map objects (obstacles => pickups)
    val playerHitMudCount = mudCountInPath(oldPosition, newPosition);
    for(a <- 0 until playerHitMudCount) {
      carGamePlayer.hitMud();
    }

    val playerHitOilCount = oilSpillCountInPath(oldPosition, newPosition)
    for (a <- 0 until playerHitOilCount) {
      carGamePlayer.hitOil()
    }

    val playerPickedUpOilItemCount = oilItemCountInPath(oldPosition, newPosition);
    for (a <- 0 until playerPickedUpOilItemCount) {
      carGamePlayer.pickupOilItem()
    }

    val playerPickedUpBoostCount = boostCountInPath(oldPosition, newPosition);
    for (a <- 0 until playerPickedUpBoostCount) {
      carGamePlayer.pickupBoost();
    }

    occupyBlock(newPosition, carGamePlayer.getGamePlayerId())

    //check win condition
    if (newPosition.getBlockNumber() == Config.TRACK_LENGTH) {
      carGamePlayer.finish();
    }
  }
}
