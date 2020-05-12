package za.co.entelect.challenge.game.contracts.map

import za.co.entelect.challenge.game.contracts.Config.Config

class Block(position: BlockPosition, var mapObject: Int, var occupiedByPlayerWithId: Int) {

    private var hasCyberTruck: Boolean = false

    override def toString() : String = { 
        val stringRepresentation =
            if(occupiedByPlayerWithId != Config.EMPTY_PLAYER) occupiedByPlayerWithId.toString
            else if(isOccupiedByCyberTruck()) "C"
            else if(mapObject == Config.EMPTY_MAP_OBJECT) "░"
            else if(mapObject == Config.MUD_MAP_OBJECT) "▓"
            else if (mapObject == Config.BOOST_MAP_OBJECT) "»"
            else if (mapObject == Config.FINISH_LINE_MAP_OBJECT) "║"
            else if (mapObject == Config.OIL_ITEM_MAP_OBJECT) "Φ"
            else if (mapObject == Config.OIL_SPILL_MAP_OBJECT) "█"
            else if (mapObject == Config.WALL_MAP_OBJECT) "#"
            else if (mapObject == Config.LIZARD_MAP_OBJECT) "&"
            else if (mapObject == Config.TWEET_MAP_OBJECT) "T"
            else throw new Exception("Unknown block content encountered when rendering")

        stringRepresentation
    }

    def getPosition(): BlockPosition = {
        return position
    }

    def getMapObject(): Int = {
        return mapObject
    }

    def getOccupiedByPlayerWithId(): Int = {
        return occupiedByPlayerWithId
    }

    def vacate() = {
        occupiedByPlayerWithId = Config.EMPTY_PLAYER
    }

    def occupy(gamePlayerId: Int) {
        occupiedByPlayerWithId = gamePlayerId
    }

    def setMapObject(mapObjectToAdd: Int) = {
        mapObject = mapObjectToAdd
    }

    def isOccupiedByCyberTruck(): Boolean = {
        return hasCyberTruck
    }

    def removeCyberTruck() = {
        hasCyberTruck = false
    }

    def addCyberTruck() = {
        hasCyberTruck = true
    }
}
