package za.co.entelect.challenge.game.contracts.map

import java.util

import za.co.entelect.challenge.game.contracts.common.RefereeMessage
import za.co.entelect.challenge.game.contracts.game.{CarGamePlayer, GamePlayer}
import za.co.entelect.challenge.game.contracts.player.Player

import scala.collection.JavaConverters._

class CarGameMap(players: util.List[Player], mapGenerationSeed: Int, lanes: Int, trackLength: Int, blocks: Array[Block], var round: Int) extends GameMap {

  override def getCurrentRound: Int = {
    return round;
  }

  override def setCurrentRound(currentRound: Int): Unit = {
    round = currentRound;
  }

  override def getWinningPlayer: GamePlayer = {
    if (round > 1) {
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
    val indexOfPlayerOnTrack = blocks.indexWhere(x => x.occupiedByPlayerWithId == gamePlayerId);
    val playerBlock = blocks(indexOfPlayerOnTrack);
    val playerBlockPosition = playerBlock.getPosition();
    val playerSpeed = gameplayer.getSpeed();
    val playerState = gameplayer.getState();
    val player = new MapFragmentPlayer(gamePlayerId, playerBlockPosition, playerSpeed, playerState);
    val lanes = blocks.filter(x => ((scala.math.abs(playerBlockPosition.getBlockNumber() - x.getPosition().getBlockNumber()) <= 5) || (scala.math.abs(x.getPosition().getBlockNumber() - playerBlockPosition.getBlockNumber()) <= 20)));
    val carGameMapFragment = new CarGameMapFragment(round, player, lanes);
    return carGameMapFragment;
  }
}
