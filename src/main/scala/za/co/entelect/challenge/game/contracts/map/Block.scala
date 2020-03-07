package za.co.entelect.challenge.game.contracts.map

import za.co.entelect.challenge.game.contracts.Config.Config

class Block(position: BlockPosition, var mapObject: Int, var occupiedByPlayerWithId: Int) {

    override def toString() : String = { 
        val stringRepresentation =
            if(occupiedByPlayerWithId != Config.EMPTY_PLAYER) occupiedByPlayerWithId.toString
            else if(mapObject == Config.EMPTY_MAP_OBJECT) "░"
            else if(mapObject == Config.MUD_MAP_OBJECT) "▓"
            else if (mapObject == Config.BOOST_MAP_OBJECT) "»"
            else if (mapObject == Config.FINISH_LINE_MAP_OBJECT) "║"
            else if (mapObject == Config.OIL_ITEM_MAP_OBJECT) "Φ"
            else if (mapObject == Config.OIL_SPILL_MAP_OBJECT) "█"
            else throw new Exception("Unknown block content encountered when rendering")

        stringRepresentation
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

    def setMapObject(mapObjectToAdd: Int) = {
        mapObject = mapObjectToAdd
    }
}
