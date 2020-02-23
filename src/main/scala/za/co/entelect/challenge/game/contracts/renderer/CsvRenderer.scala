package za.co.entelect.challenge.game.contracts.renderer

import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.GameMap
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer

class CsvRenderer extends GameMapRenderer {
    def render(gameMap: GameMap, gamePlayer: GamePlayer): String = {
        val carGameMap: CarGameMap = gameMap.asInstanceOf[CarGameMap];

        val shouldRenderFragment = gamePlayer != null;
        if(shouldRenderFragment) 
        {
            val carGamePlayer: CarGamePlayer = gamePlayer.asInstanceOf[CarGamePlayer];
            return renderFragment(carGameMap, carGamePlayer);
        } 
        else 
        {
            return renderVisualiserMap(carGameMap);
        }
    }

    def renderFragment(gameMap: CarGameMap, gamePlayer: CarGamePlayer): String = {
        val mapFragment = gameMap.getMapFragment(gamePlayer);
        val csvHeaderString = "PlayerInfo,Lane1,Lane2,Lane3,Lane4";

        val currentRound = mapFragment.getCurrentRound();
        val player = mapFragment.getPlayer();
        val playerInfoString = "current round: " + currentRound + " player: { " + player.toString() + " },";

        val lanes = mapFragment.getLanes();
        var lanesAsString = "";
        for(x <- lanes.filter(x => x.getPosition().getLane() == 1)) {
            lanesAsString += x.toString();
        }
        lanesAsString += ",";
        for(x <- lanes.filter(x => x.getPosition().getLane() == 2)) {
            lanesAsString += x.toString();
        }
        lanesAsString += ",";
        for(x <- lanes.filter(x => x.getPosition().getLane() == 3)) {
            lanesAsString += x.toString();
        }
        lanesAsString += ",";
        for(x <- lanes.filter(x => x.getPosition().getLane() == 4)) {
            lanesAsString += x.toString();
        }

        return csvHeaderString + "\r\n" + playerInfoString + "," + lanesAsString + "\r\n";
    }

    def renderVisualiserMap(gameMap: CarGameMap) : String = {
        throw new NotImplementedError("Csv renderer render visualiser map");
    }

    def commandPrompt(gamePlayer: GamePlayer): String = {
        throw new NotImplementedError("Csv renderer command prompt");
    }
}