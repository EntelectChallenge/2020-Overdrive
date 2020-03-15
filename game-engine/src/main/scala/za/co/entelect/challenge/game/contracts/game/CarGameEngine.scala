package za.co.entelect.challenge.game.contracts.game

import za.co.entelect.challenge.game.contracts.map.GameMap

class CarGameEngine extends GameEngine {

    override def isGameComplete(gameMap: GameMap): Boolean = {
        val winner = gameMap.getWinningPlayer();
        val gameIsComplete = winner != null;
        return gameIsComplete;
    }
}
