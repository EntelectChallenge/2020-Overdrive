package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.{BlockPosition, CarGameMap}

class UseOilCommand extends BaseCarGameCommand {

  override def getFuturePositionAfterAdditionalProcessingOfCommand(carGameMap: CarGameMap,
                                                                   carGamePlayer: CarGamePlayer,
                                                                   currentPlayerPosition: BlockPosition):
  BlockPosition = {
    val futurePosition: BlockPosition = CommandHelper.getFuturePosition(carGamePlayer, currentPlayerPosition)

    if (carGamePlayer.hasOilItem) {
      carGamePlayer.useOilItem()
      carGameMap.applyOilToBlock(currentPlayerPosition)
    }
    else {
      //TODO: report match issues
    }
    futurePosition
  }

}
