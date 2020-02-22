package za.co.entelect.challenge.game.contracts.renderer

import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.GameMap
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer

class ConsoleRenderer extends GameMapRenderer {
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
        val mapFragmentAsString = mapFragment.toString();
        return mapFragmentAsString;
    }
    
    def renderVisualiserMap(gameMap: CarGameMap) : String = {
        throw new NotImplementedError("Console renderer render visualiser map");
    }

    def commandPrompt(gamePlayer: GamePlayer): String = {
        throw new NotImplementedError("Console renderer command prompt");
    }
}