package za.co.entelect.challenge.game.contracts.map

class Block(position: BlockPosition, mapObject: Int, var occupiedByPlayerWithId: Int) {
    override def toString() : String = { 
        val stringRepresentation = 
            "lane: " + position.getLane() + 
            " blockNumber: " + position.getBlockNumber() + 
            " object: " + mapObject + 
            " occupiedByPlayerWithId: " + occupiedByPlayerWithId;
        return stringRepresentation;
    } 

    def getPosition(): BlockPosition = {
        return position;
    }
}