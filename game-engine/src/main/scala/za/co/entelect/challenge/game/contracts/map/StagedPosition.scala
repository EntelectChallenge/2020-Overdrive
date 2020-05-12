package za.co.entelect.challenge.game.contracts.map

import za.co.entelect.challenge.game.contracts.game.CarGamePlayer

class StagedPosition(carGamePlayer: CarGamePlayer, var newPosition: BlockPosition, oldPosition: BlockPosition) {
  def getPlayer(): CarGamePlayer = {
    return carGamePlayer
  }

  def getNewPosition(): BlockPosition = {
    return newPosition
  }

  def setNewPosition(newNewPosition: BlockPosition) = {
    newPosition = newNewPosition
  }

  def getOldPosition(): BlockPosition = {
    return oldPosition
  }
}
