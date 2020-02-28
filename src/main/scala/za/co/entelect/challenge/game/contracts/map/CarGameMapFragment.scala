package za.co.entelect.challenge.game.contracts.map

class CarGameMapFragment(currentRound: Int, player: MapFragmentPlayer, lanes: Array[Block]) {
    def getCurrentRound(): Int = {
        return currentRound;
    }

    def getPlayer(): MapFragmentPlayer = {
        return player;
    }

    def getLanes(): Array[Block] = {
        return lanes;
    }
    
    override def toString() : String = { 
        var lanesAsString = "[";
        for(x <- lanes.filter(x => x.getPosition().getLane() == 1)) {
            lanesAsString += x.toString();
        }
        lanesAsString += "]\r\n[";
        for(x <- lanes.filter(x => x.getPosition().getLane() == 2)) {
            lanesAsString += x.toString();
        }
        lanesAsString += "]\r\n[";
        for(x <- lanes.filter(x => x.getPosition().getLane() == 3)) {
            lanesAsString += x.toString();
        }
        lanesAsString += "]\r\n[";
        for(x <- lanes.filter(x => x.getPosition().getLane() == 4)) {
            lanesAsString += x.toString();
        }
        lanesAsString += "]"

        val stringRepresentation = 
            "======================================================================================================" + "\r\n" +
            "PLAYER INFO: " +
            "current round: " + currentRound + 
            " player: { " + player.toString() + " }" + "\r\n" +
            lanesAsString + "\r\n" +
            "======================================================================================================"; 
        return stringRepresentation;
    } 
}