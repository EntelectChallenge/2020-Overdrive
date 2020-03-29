package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.map.{BlockPosition, CarGameMap, GameMap, StagedPosition}
import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.Config.Config

abstract class BaseCarGameCommand extends RawCommand {

    override def performCommand(gameMap: GameMap, player: GamePlayer) = {
        val carGameMap = gameMap.asInstanceOf[CarGameMap];
        var carGamePlayer = player.asInstanceOf[CarGamePlayer];

        //handle ticking powerups
        if (carGamePlayer.isBoosting()) 
        {
            carGamePlayer.tickBoost();
        }

        val carGamePlayerId = carGamePlayer.getGamePlayerId();
        val currentBlockThatHasBeenVacated = getCurrentBlockThatHasBeenVacated(carGameMap, carGamePlayerId);

        val futurePosition = getFuturePositionAfterAdditionalProcessingOfCommand(carGameMap, carGamePlayer, currentBlockThatHasBeenVacated); //Override this method to process particular command

        val futurePositionWithLaneBounds = getFutuerPositionWithinBoundsOfLanes(futurePosition)
        val futurePositionWithinAllBounds = getFuturePositionWithinBlockNumberBounds(futurePositionWithLaneBounds);

        //handle collisions with map objects (obstacles => pickups)
        val playerHitMudCount = carGameMap.mudCountInPath(currentBlockThatHasBeenVacated, futurePositionWithinAllBounds);
        for(a <- 0 until playerHitMudCount) {
            carGamePlayer.hitMud();
        }

        val playerHitOilCount = carGameMap.oilSpillCountInPath(currentBlockThatHasBeenVacated, futurePositionWithinAllBounds)
        for (a <- 0 until playerHitOilCount) {
            carGamePlayer.hitOil()
        }

        val playerPickedUpOilItemCount = carGameMap.oilItemCountInPath(currentBlockThatHasBeenVacated, futurePositionWithinAllBounds);
        for (a <- 0 until playerPickedUpOilItemCount) {
            carGamePlayer.pickupOilItem()
        }

        val playerPickedUpBoostCount = carGameMap.boostCountInPath(currentBlockThatHasBeenVacated, futurePositionWithinAllBounds);
        for (a <- 0 until playerPickedUpBoostCount) {
            carGamePlayer.pickupBoost();
        }

        //stage player future position => concurrent logic resolver will update future position if necessary before committing it
        val stagedPosition = new StagedPosition(carGamePlayer, futurePositionWithinAllBounds, currentBlockThatHasBeenVacated)
        carGameMap.stageFuturePosition(stagedPosition);

        //check win condition
        if (futurePositionWithinAllBounds.getBlockNumber() == Config.TRACK_LENGTH) {
            carGamePlayer.finish();
        }
    }

    def getFuturePositionAfterAdditionalProcessingOfCommand(carGameMap: CarGameMap, carGamePlayer: CarGamePlayer, currentPlayerPosition: BlockPosition): BlockPosition

    private def getCurrentBlockThatHasBeenVacated(carGameMap: CarGameMap, carGamePlayerId: Int): BlockPosition = {
        val currentPosition = carGameMap.getPlayerBlockPosition(carGamePlayerId);
        carGameMap.vacateBlock(currentPosition);
        return currentPosition;
    }

    private def getFutuerPositionWithinBoundsOfLanes(futurePosition: BlockPosition): BlockPosition = {
        val laneAtleastMinimum = scala.math.max(Config.MIN_LANE, futurePosition.getLane());
        val laneAtmostMaximum = scala.math.min(Config.MAX_LANE, laneAtleastMinimum);
        val futurePositionWithinBoundsOfLanes = new BlockPosition(laneAtmostMaximum, futurePosition.getBlockNumber());
        return futurePositionWithinBoundsOfLanes;
    }

    private def getFuturePositionWithinBlockNumberBounds(futurePosition: BlockPosition): BlockPosition = {
        val blockNumberAtmostMaximum = scala.math.min(Config.TRACK_LENGTH, futurePosition.getBlockNumber());
        val futurePositionWithingBlockNumberBounds = new BlockPosition(futurePosition.getLane(), blockNumberAtmostMaximum);
        return futurePositionWithingBlockNumberBounds;
    }

    
}
