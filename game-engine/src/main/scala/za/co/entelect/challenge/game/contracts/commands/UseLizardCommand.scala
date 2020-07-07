package za.co.entelect.challenge.game.contracts.commands
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.{BlockPosition, CarGameMap}

class UseLizardCommand extends BaseCarGameCommand {
    override def getFuturePositionAfterAdditionalProcessingOfCommand(carGameMap: CarGameMap, carGamePlayer: CarGamePlayer, currentPlayerPosition: BlockPosition): BlockPosition = {
        if (carGamePlayer.hasLizard){
            carGamePlayer.useLizard()
        }
        else {
          carGamePlayer.doNothing()
        }
        return CommandHelper.getFuturePosition(carGamePlayer, currentPlayerPosition)
    }
}
