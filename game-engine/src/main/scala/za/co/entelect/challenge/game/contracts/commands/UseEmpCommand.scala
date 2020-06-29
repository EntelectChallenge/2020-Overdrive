package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.{BlockPosition, CarGameMap, StagedPosition}
import za.co.entelect.challenge.game.contracts.Config.Config

class UseEmpCommand extends BaseCarGameCommand {
  override def getFuturePositionAfterAdditionalProcessingOfCommand(carGameMap: CarGameMap, carGamePlayer: CarGamePlayer, currentPlayerPosition: BlockPosition): BlockPosition = {
    val futureBlockNumber = currentPlayerPosition.getBlockNumber() + carGamePlayer.getSpeed()
    val futurePosition = new BlockPosition(currentPlayerPosition.getLane(), futureBlockNumber)

    val opponent = carGameMap.getCarGamePlayers().find(p => p.getGamePlayerId() != carGamePlayer.getGamePlayerId()).get
    val opponentPosition = carGameMap.getPlayerStartOfRoundBlockPosition(opponent.getGamePlayerId())

    if (carGamePlayer.hasEmp()) {
      carGamePlayer.useEmp()

      if (opponentInRange(currentPlayerPosition, opponentPosition)) {
        opponent.hitEmp();
      }

    }
    else {
      //TODO: report match issues
      carGamePlayer.doNothing()
    }

    return futurePosition
  }

  private def opponentInRange(currentPlayerPosition: BlockPosition, opponentPosition: BlockPosition): Boolean = {
    val empMidLane = currentPlayerPosition.getLane()
    val empBlock = currentPlayerPosition.getBlockNumber()
    val opponentLane = opponentPosition.getLane()
    val opponentBlock = opponentPosition.getBlockNumber()

    if ((opponentLane == empMidLane - 1 || opponentLane == empMidLane || opponentLane == empMidLane + 1) && opponentBlock - empBlock > Config.EMP_BLOCK_GAP) {
      return true
    }

    return false
  }
}
