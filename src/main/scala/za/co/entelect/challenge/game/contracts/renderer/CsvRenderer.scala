package za.co.entelect.challenge.game.contracts.renderer

import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.GameMap

class CsvRenderer extends GameMapRenderer {
    def render(gameMap: GameMap, gamePlayer: GamePlayer): String = {
        throw new NotImplementedError("Csv renderer render");
    }

    def commandPrompt(gamePlayer: GamePlayer): String = {
        throw new NotImplementedError("Csv renderer command prompt");
    }
}