package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.GameMap
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.BlockPosition

class ChangeLaneCommand(isLeft: Boolean) extends RawCommand {

    private val MIN_LANE = 1;
    private val MAX_LANE = 4;
    private val MAX_BLOCKNUMBER: Int = 1500;
    private val CHANGE_LANE_PENALTY: Int = 1;

    override def performCommand(gameMap: GameMap, player: GamePlayer) = {
        val carGameMap = gameMap.asInstanceOf[CarGameMap];
        var carGamePlayer = player.asInstanceOf[CarGamePlayer];
        val gamePlayerId = carGamePlayer.getGamePlayerId();
        val currentPosition = carGameMap.getPlayerBlockPosition(gamePlayerId);

        carGameMap.vacateBlock(currentPosition);
        var futureLane = currentPosition.getLane() 
        if(isLeft) 
        {
            futureLane = scala.math.max(MIN_LANE, futureLane - 1);
        }
        else 
        {   
            futureLane = scala.math.min(MAX_LANE, futureLane + 1);
        }
        val futureBlockNumber = scala.math.min(MAX_BLOCKNUMBER, currentPosition.getBlockNumber() + carGamePlayer.getSpeed() - CHANGE_LANE_PENALTY);
        val futurePosition = new BlockPosition(currentPosition.getLane(), futureBlockNumber);

        val playerHitMud = carGameMap.pathIncludesMud(currentPosition, futurePosition);
        if(playerHitMud) {
            carGamePlayer.reduceSpeed();
        }

        carGameMap.occupyBlock(futurePosition, gamePlayerId);
    }

}