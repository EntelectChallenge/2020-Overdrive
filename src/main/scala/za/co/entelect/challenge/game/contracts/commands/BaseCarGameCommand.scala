package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.map.GameMap
import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.BlockPosition

abstract class BaseCarGameCommand extends RawCommand {

    private val MIN_LANE = 1;
    private val MAX_LANE = 4;
    private val MAX_BLOCKNUMBER: Int = 1500;

    override def performCommand(gameMap: GameMap, player: GamePlayer) = {
        val carGameMap = gameMap.asInstanceOf[CarGameMap];
        var carGamePlayer = player.asInstanceOf[CarGamePlayer];
        val carGamePlayerId = carGamePlayer.getGamePlayerId();
        val currentBlockThatHasBeenVacated = getCurrentBlockThatHasBeenVacated(carGameMap, carGamePlayerId);

        val futurePosition = getFuturePositionAfterAdditionalProcessingOfCommand(carGameMap, carGamePlayer, currentBlockThatHasBeenVacated); //Override this method to process particular command

        val futurePositionWithLaneBounds = getFutuerPositionWithinBoundsOfLanes(futurePosition)
        val futurePositionWithingAllBounds = getFuturePositionWithinBlockNumberBounds(futurePositionWithLaneBounds);

        val playerHitMud = carGameMap.pathIncludesMud(currentBlockThatHasBeenVacated, futurePositionWithingAllBounds);
        if(playerHitMud) {
            carGamePlayer.reduceSpeed();
        }

        carGameMap.occupyBlock(futurePositionWithingAllBounds, carGamePlayerId);
    }

    def getFuturePositionAfterAdditionalProcessingOfCommand(carGameMap: CarGameMap, carGamePlayer: CarGamePlayer, currentPlayerPosition: BlockPosition): BlockPosition

    private def getCurrentBlockThatHasBeenVacated(carGameMap: CarGameMap, carGamePlayerId: Int): BlockPosition = {
        val currentPosition = carGameMap.getPlayerBlockPosition(carGamePlayerId);
        carGameMap.vacateBlock(currentPosition);
        return currentPosition;
    }

    private def getFutuerPositionWithinBoundsOfLanes(futurePosition: BlockPosition): BlockPosition = {
        val laneAtleastMinimum = scala.math.max(MIN_LANE, futurePosition.getLane());
        val laneAtmostMaximum = scala.math.min(MAX_LANE, laneAtleastMinimum);
        val futurePositionWithinBoundsOfLanes = new BlockPosition(laneAtmostMaximum, futurePosition.getBlockNumber());
        return futurePositionWithinBoundsOfLanes;
    }

    private def getFuturePositionWithinBlockNumberBounds(futurePosition: BlockPosition): BlockPosition = {
        val blockNumberAtmostMaximum = scala.math.min(MAX_BLOCKNUMBER, futurePosition.getBlockNumber());
        val futurePositionWithingBlockNumberBounds = new BlockPosition(futurePosition.getLane(), blockNumberAtmostMaximum);
        return futurePositionWithingBlockNumberBounds;
    }

    
}