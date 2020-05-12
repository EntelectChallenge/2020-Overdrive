package za.co.entelect.challenge.game.contracts.map

class MapFragmentPlayer(id: Int, position: BlockPosition, speed: Int, state: String, powerups: Array[String],
                        boosting: Boolean, boostCounter: Int, score: Int) {
    def getId(): Int = {
        return id
    }

    def getPosition(): BlockPosition = {
        return position
    }

    def getSpeed(): Int = {
        return speed
    }

    def getState(): String = {
        return state
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

    def getScore(): Int = {
        return score
    }

    def toString(limited: Boolean): String = {
        var stringRepresentation =
            "id:" + id +
              " position: " + position.toString() +
              " speed:" + speed
        if(!limited)
        {
            stringRepresentation +=
              " state:" + state +
                " boosting:" + boosting +
                " boost-counter:" + boostCounter +
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
