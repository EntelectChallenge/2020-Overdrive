package za.co.entelect.challenge.game.contracts.map

class MapFragmentPlayer(id: Int, position: BlockPosition, speed: Int, state: String, powerups: Array[String],
                        boosting: Boolean, boostCounter: Int) {
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

    override def toString(): String = {
        val stringRepresentation =
            "id:" + id +
              " position:" + position.toString() +
              " speed:" + speed +
              " state:" + state +
              "\n" +
              " boosting:" + boosting +
              " boost-counter:" + boostCounter +
              " powerups: " + powerups.groupBy(p => p)
              .map(kv => s"${kv._1}:${kv._2.length}")
              .mkString(", ")

        stringRepresentation
    }
}
