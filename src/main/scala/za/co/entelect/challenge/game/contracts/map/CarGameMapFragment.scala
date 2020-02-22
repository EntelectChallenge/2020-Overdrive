package za.co.entelect.challenge.game.contracts.map

class CarGameMapFragment(currentRound: Int, player: MapFragmentPlayer, lanes: Array[Block]) {
    override def toString() : String = { 
        val lanesAsString = lanes.toString();
        val stringRepresentation = 
            "current round: " + currentRound + 
            " player: { " + player.toString() + " }" +
            " lanes: { " + lanesAsString + " }"; 
        return stringRepresentation;
    } 
}