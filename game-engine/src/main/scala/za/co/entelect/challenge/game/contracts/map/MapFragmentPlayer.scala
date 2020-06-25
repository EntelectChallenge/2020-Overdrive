package za.co.entelect.challenge.game.contracts.map

class MapFragmentPlayer(id: Int, position: BlockPosition, speed: Int, statesThatOccurredThisRound: Array[String], powerups: Array[String],
                        boosting: Boolean, boostCounter: Int, score: Int, damage: Int, lastCyberTruckPosition: BlockPosition) {
    def getId(): Int = {
        return id
    }

    def getPosition(): BlockPosition = {
        return position
    }

    def getSpeed(): Int = {
        return speed
    }

    def getState(): Array[String] = {
        return statesThatOccurredThisRound
    }

    def getPowerups(): Array[String] = {
        return powerups
    }

    def isBoosting(): Boolean = {
        return boosting
    }

    def getBoostCounter(): Int = {
        return boostCounter
    }

    def getDamage(): Int = {
        return damage
    }

    def getScore(): Int = {
        return score
    }

    def getLastCyberTruckPosition(): BlockPosition = {
        return lastCyberTruckPosition
    }

    def toString(limited: Boolean): String = {
        var stringRepresentation =
            "id:" + id +
              " position: " + position.toString() +
              " speed:" + speed
        if(!limited)
        {
            stringRepresentation +=
                " state:" + statesThatOccurredThisRound.last +
                " statesThatOccurredThisRound:" + statesThatOccurredThisRound.mkString(", ") +
                " boosting:" + boosting +
                " boost-counter:" + boostCounter +
                " damage:" + damage +
                " score:" + score +
                " powerups: " + powerups.groupBy(p => p)
                .map(kv => s"${kv._1}:${kv._2.length}")
                .mkString(", ")
        }
        else
        {
            stringRepresentation += "\n"
        }

        stringRepresentation
    }
}
