package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.{BlockPosition, CarGameMap, StagedPosition}

class UseTweetCommand(lane: Int, block: Int) extends BaseCarGameCommand {
  override def getFuturePositionAfterAdditionalProcessingOfCommand(carGameMap: CarGameMap, carGamePlayer: CarGamePlayer, currentPlayerPosition: BlockPosition): BlockPosition = {
    val futureBlockNumber = currentPlayerPosition.getBlockNumber() + carGamePlayer.getSpeed()
    val futurePosition = new BlockPosition(currentPlayerPosition.getLane(), futureBlockNumber)

    if (carGamePlayer.hasTweet()) {
      carGamePlayer.useTweet()

      val requestedCyberTruckPosition = new BlockPosition(lane, block)
      val oldCyberTruckPosition = carGamePlayer.getCurrentCyberTruckPosition()
      val stagedCyberTruckPosition = new StagedPosition(carGamePlayer, requestedCyberTruckPosition, oldCyberTruckPosition)
      carGameMap.stageCyberTruckAt(stagedCyberTruckPosition)
    }
    else {
      //TODO: report match issues
      carGamePlayer.doNothing()
    }

    return futurePosition
  }
}
