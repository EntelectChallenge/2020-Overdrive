package example

import za.co.entelect.challenge.game.contracts.game.GameEngine
import za.co.entelect.challenge.game.contracts.map.GameMap

class CarGameEngine extends GameEngine {

    override def isGameComplete(gameMap: GameMap): Boolean = {
        return true;
    }
}
