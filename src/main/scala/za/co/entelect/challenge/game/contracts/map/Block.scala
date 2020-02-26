package za.co.entelect.challenge.game.contracts.map

import za.co.entelect.challenge.game.contracts.Config.Config

class Block(position: BlockPosition, mapObject: Int, var occupiedByPlayerWithId: Int) {

    override def toString() : String = { 
        val stringRepresentation = "Block: {" +
            " position: { " + position.toString() + " }" +
            " object: " + mapObject + 
            " occupiedByPlayerWithId: " + occupiedByPlayerWithId +
            " } ";
        return stringRepresentation;
    } 

    def getPosition(): BlockPosition = {
        return position;
    }

    def getMapObject(): Int = {
        return mapObject;
    }

    def getOccupiedByPlayerWithId(): Int = {
        return occupiedByPlayerWithId;
    }

    def vacate() = {
        occupiedByPlayerWithId = Config.EMPTY_PLAYER;
    }

    def occupy(gamePlayerId: Int) {
        occupiedByPlayerWithId = gamePlayerId;
    }
}