package za.co.entelect.challenge.game.contracts.map

class MapFragmentPlayer(id: Int, position: BlockPosition, speed: Int, state: String) {
    def getId(): Int = {
        return id;
    }

    def getPosition(): BlockPosition = {
        return position;
    }

    def getSpeed(): Int = {
        return speed;
    }

    def getState(): String = {
        return state;
    }
    
    override def toString() : String = { 
        val stringRepresentation = 
            "id: " + id + 
            " position: { " + position.toString() + " }" +
            " speed: " + speed + 
            " state: " + state; 
        return stringRepresentation;
    } 
}