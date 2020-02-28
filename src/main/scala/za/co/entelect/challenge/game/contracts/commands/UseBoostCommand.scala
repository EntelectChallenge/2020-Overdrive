package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.BlockPosition

class UseBoostCommand extends BaseCarGameCommand {
    override def getFuturePositionAfterAdditionalProcessingOfCommand(carGameMap: CarGameMap, carGamePlayer: CarGamePlayer, currentPlayerPosition: BlockPosition): BlockPosition = {
        val playerHasBoost = carGamePlayer.hasBoost();
        if (playerHasBoost) 
        {
            carGamePlayer.useBoost();
        }
        else
        {
            //TODO: report match issues
        }
        val futureBlockNumber = currentPlayerPosition.getBlockNumber() + carGamePlayer.getSpeed();
        val futurePosition = new BlockPosition(currentPlayerPosition.getLane(), futureBlockNumber);
        return futurePosition;
    }
}