package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.GameMap
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.BlockPosition

class ChangeLaneCommand(isLeft: Boolean) extends BaseCarGameCommand {

    private val CHANGE_LANE_PENALTY: Int = 1;

    override def getFuturePositionAfterAdditionalProcessingOfCommand(carGameMap: CarGameMap, carGamePlayer: CarGamePlayer, currentPlayerPosition: BlockPosition): BlockPosition = {
        val futureBlockNumber = currentPlayerPosition.getBlockNumber() + carGamePlayer.getSpeed() - CHANGE_LANE_PENALTY;
        var futureLane = currentPlayerPosition.getLane();
        if(isLeft)
        {
            futureLane = futureLane - 1;
        }
        else
        {
            futureLane = futureLane + 1
        }
        val futurePosition = new BlockPosition(futureLane, futureBlockNumber);
        return futurePosition;
    }

}