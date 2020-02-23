package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.GameMap
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.BlockPosition

class AccelerateCommand extends BaseCarGameCommand {

    override def getFuturePositionAfterAdditionalProcessingOfCommand(carGameMap: CarGameMap, carGamePlayer: CarGamePlayer, currentPlayerPosition: BlockPosition): BlockPosition = {
        val futureBlockNumber = currentPlayerPosition.getBlockNumber() + carGamePlayer.getSpeed();
        val futurePosition = new BlockPosition(currentPlayerPosition.getLane(), futureBlockNumber);
        carGamePlayer.increaseSpeed();
        return futurePosition;
    }
    
}