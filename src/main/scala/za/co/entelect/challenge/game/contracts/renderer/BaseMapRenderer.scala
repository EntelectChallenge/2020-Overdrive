package za.co.entelect.challenge.game.contracts.renderer

import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.GameMap
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer

abstract class BaseMapRenderer extends GameMapRenderer {

    override def render(gameMap: GameMap, gamePlayer: GamePlayer): String = {
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
    
    def renderFragment(carGameMap: CarGameMap, carGamePlayer: CarGamePlayer): String
    def renderVisualiserMap(carGameMap: CarGameMap): String
    
    override def commandPrompt(gamePlayer: GamePlayer): String = {
        throw new NotImplementedError("Base map renderer => command prompt");
    }
}