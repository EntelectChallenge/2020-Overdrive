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
        var lanesAsString = "LANE 1: ";
        for(x <- lanes.filter(x => x.getPosition().getLane() == 1)) {
            lanesAsString += x.toString();
        }
        lanesAsString += "\r\nLANE 2: ";
        for(x <- lanes.filter(x => x.getPosition().getLane() == 2)) {
            lanesAsString += x.toString();
        }
        lanesAsString += "\r\nLANE 3: ";
        for(x <- lanes.filter(x => x.getPosition().getLane() == 3)) {
            lanesAsString += x.toString();
        }
        lanesAsString += "\r\nLANE 4: ";
        for(x <- lanes.filter(x => x.getPosition().getLane() == 4)) {
            lanesAsString += x.toString();
        }

        val stringRepresentation = 
            "======================================================================================================" + "\r\n" +
            "MAP FRAGMENT: " + "\r\n" + 
            "PLAYER INFO: " +
            "current round: " + currentRound + 
            " player: { " + player.toString() + " }" + "\r\n" +
            lanesAsString + "\r\n" +
            "======================================================================================================"; 
        return stringRepresentation;
    } 
}