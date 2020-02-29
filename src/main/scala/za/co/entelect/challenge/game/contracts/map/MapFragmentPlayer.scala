package za.co.entelect.challenge.game.contracts.map

class MapFragmentPlayer(id: Int, position: BlockPosition, speed: Int, state: String, powerups: Array[String], boosting: Boolean, boostCounter: Int) {
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

    def getPowerups(): Array[String] = {
        return powerups;
    }

    def isBoosting(): Boolean = {
        return boosting;
    }

    def getBoostCounter(): Int = {
        return boostCounter;
    }
    
    override def toString() : String = { 
        var powerupAsString = "[";
        for (i <- 0 to (powerups.size - 1)) {
            if(i == 0)
            {
                powerupAsString = powerupAsString + powerups(i);
            }
            else
            {
                powerupAsString = powerupAsString + "," + powerups(i);
            }
        }
        powerupAsString += "]" 
        val stringRepresentation = 
            "id: " + id + 
            " position: { " + position.toString() + " }" +
            " speed: " + speed + 
            " state: " + state + 
            " powerups: " + powerupAsString + 
            " boosting: " + boosting + 
            " boost-counter: " + boostCounter; 
        return stringRepresentation;
    } 
}