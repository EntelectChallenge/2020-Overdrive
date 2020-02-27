package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.map.GameMap
import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.BlockPosition
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
        val futurePositionWithingAllBounds = getFuturePositionWithinBlockNumberBounds(futurePositionWithLaneBounds);

        //handle collisions with map objects (obstacles => pickups)
        val playerHitMud = carGameMap.pathIncludesMud(currentBlockThatHasBeenVacated, futurePositionWithingAllBounds);
        if(playerHitMud) {
            carGamePlayer.hitMud();
        }

        val playerHitOil = carGameMap.pathIncludesOilSpill(currentBlockThatHasBeenVacated, futurePositionWithingAllBounds)
        if (playerHitOil) {
            carGamePlayer.hitOil()
        }

        val playerPickedUpOilItem = carGameMap.pathIncludesOilItem(currentBlockThatHasBeenVacated, futurePositionWithingAllBounds);
        if(playerPickedUpOilItem) {
            carGamePlayer.pickupOilItem()
        }

        val playerPickedUpBoost = carGameMap.pathIncludesBoost(currentBlockThatHasBeenVacated, futurePositionWithingAllBounds);
        if(playerPickedUpBoost) {
            carGamePlayer.pickupBoost();
        }

        //place player in new position
        carGameMap.occupyBlock(futurePositionWithingAllBounds, carGamePlayerId);

        //check win condition
        if (futurePositionWithingAllBounds.getBlockNumber() == Config.TRACK_LENGTH) {
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
