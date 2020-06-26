package za.co.entelect.challenge.game.contracts.renderer

import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class CsvRenderer extends BaseMapRenderer {

    override def renderFragment(gameMap: CarGameMap, gamePlayer: CarGamePlayer): String = {
        val mapFragment = gameMap.getMapFragment(gamePlayer)
        val csvHeaderString = "Round,PlayerId,Position:Y,Position:X,Speed,State,Boosting,Boost-Counter,Damage,#Boosts,#Oil,#Lizards,LastCyberTruck:Y,LastCyberTruck:X,#Tweets,#EMPs,Score\r\n"

        val currentRound = mapFragment.getCurrentRound()
        val player = mapFragment.getPlayer()

        val lastCyberTruckPosition = player.getLastCyberTruckPosition();

        var lastCyberTruckLane = "";
        if(lastCyberTruckPosition != null) {
            lastCyberTruckLane = lastCyberTruckPosition.getLane().toString()
        }

        var lastCyberTruckBlockNumber = "";
        if(lastCyberTruckPosition != null) {
            lastCyberTruckBlockNumber = lastCyberTruckPosition.getBlockNumber().toString()
        }

        val playerInfoString =
            currentRound + "," +
            player.getId() + "," +
            player.getPosition().getLane() + "," +
            player.getPosition().getBlockNumber() + "," +
            player.getSpeed() + "," +
            player.getState().last + "," +
            player.isBoosting() + "," +
            player.getBoostCounter() + "," +
            player.getDamage() + "," +
            player.getPowerups().count(x => x == Config.BOOST_POWERUP_ITEM) + "," +
            player.getPowerups().count(x => x == Config.OIL_POWERUP_ITEM) + "," +
            player.getPowerups().count(x => x == Config.LIZARD_POWERUP_ITEM) + "," +
              lastCyberTruckLane + "," +
              lastCyberTruckBlockNumber + "," +
            player.getPowerups().count(x => x == Config.TWEET_POWERUP_ITEM) + "," +
              player.getPowerups().count(x => x == Config.EMP_POWERUP_ITEM) + "," +
            player.getScore()

        if(gameMap.getCurrentRound() == 1)
        {
            return csvHeaderString + playerInfoString;
        } else
        {
            return playerInfoString
        }
    }

    override def renderVisualiserMap(gameMap: CarGameMap) : String = {
        throw new NotImplementedError("Csv renderer render visualiser map")
    }
}
