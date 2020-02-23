package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.GameMap
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.BlockPosition

class NothingCommand extends RawCommand {

    override def performCommand(gameMap: GameMap, player: GamePlayer) = {
        val carGameMap = gameMap.asInstanceOf[CarGameMap];
        var carGamePlayer = player.asInstanceOf[CarGamePlayer];
        val gamePlayerId = carGamePlayer.getGamePlayerId();
        val currentPosition = carGameMap.getPlayerBlockPosition(gamePlayerId);

        carGameMap.vacateBlock(currentPosition);

        val futureBlockNumber = currentPosition.getBlockNumber() + carGamePlayer.getSpeed();
        val futurePosition = new BlockPosition(currentPosition.getLane(), futureBlockNumber);
        carGameMap.occupyBlock(futurePosition, gamePlayerId);

        val playerHitMud = carGameMap.pathIncludesMud(currentPosition, futurePosition);
        if(playerHitMud) {
            carGamePlayer.reduceSpeed();
        }
    }

}