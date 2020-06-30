package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.{BlockPosition, CarGameMap}

class FixCommand extends BaseCarGameCommand {

    override def getFuturePositionAfterAdditionalProcessingOfCommand(carGameMap: CarGameMap, carGamePlayer: CarGamePlayer, currentPlayerPosition: BlockPosition): BlockPosition = {
        carGamePlayer.fixDamage();
        val futurePosition = new BlockPosition(currentPlayerPosition.getLane(), currentPlayerPosition.getBlockNumber())
        return futurePosition
    }
    
}