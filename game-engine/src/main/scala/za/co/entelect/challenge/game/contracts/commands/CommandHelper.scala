package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.BlockPosition

object CommandHelper {

  def getFuturePosition(carGamePlayer: CarGamePlayer, currentPlayerPosition: BlockPosition): BlockPosition = {
    val futureBlockNumber = currentPlayerPosition.getBlockNumber() + carGamePlayer.getSpeed()
    new BlockPosition(currentPlayerPosition.getLane(), futureBlockNumber)
  }

  def getBlockToPlacePowerUp(carGamePlayer: CarGamePlayer, currentPlayerPosition: BlockPosition): BlockPosition = {
    val blockToPlacePowerup = currentPlayerPosition.getBlockNumber() - 1
    new BlockPosition(currentPlayerPosition.getLane(), blockToPlacePowerup)
  }

}
