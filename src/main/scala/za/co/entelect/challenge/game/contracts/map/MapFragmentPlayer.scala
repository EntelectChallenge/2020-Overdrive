package za.co.entelect.challenge.game.contracts.map

class MapFragmentPlayer(id: Int, position: BlockPosition, speed: Int, state: String) {
    override def toString() : String = { 
        val stringRepresentation = 
            "id: " + id + 
            " position: { " + position.toString() + " }" +
            " speed: " + speed + 
            " state: " + state; 
        return stringRepresentation;
    } 
}