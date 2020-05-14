package za.co.entelect.challenge.game.contracts.renderer

import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.GameMap
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer

class TextRenderer extends BaseMapRenderer {

    override def renderFragment(gameMap: CarGameMap, gamePlayer: CarGamePlayer): String = {
        val mapFragment = gameMap.getMapFragment(gamePlayer)
        val mapFragmentAsString = mapFragment.toString()
        return mapFragmentAsString
    }

    override def renderVisualiserMap(gameMap: CarGameMap) : String = {
        throw new NotImplementedError("Text renderer render visualiser map")
    }
}