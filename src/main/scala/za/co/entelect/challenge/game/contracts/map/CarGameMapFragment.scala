package za.co.entelect.challenge.game.contracts.map

class CarGameMapFragment(currentRound: Int, player: MapFragmentPlayer, lanes: Array[Block]) {
    def getCurrentRound(): Int = {
        return currentRound;
    }

    def getPlayer(): MapFragmentPlayer = {
        return player;
    }

    def getBlocks(): Array[Block] = {
        return lanes;
    }

    override def toString(): String = {
        val lanesAsString = lanes.groupBy { b => b.getPosition().getLane() }
          .map { kv =>
              kv._2.sortBy(b => b.getPosition().getBlockNumber())
                .map { b => b.toString() }
                .mkString("[", "", "]")
          }
          .mkString("\r\n")

        "======================================================================================================" + "\r\n" +
          "round:" + currentRound + " player:" + player.toString + "" + "\r\n" +
          lanesAsString + "\r\n" +
          "======================================================================================================"
    }
}
