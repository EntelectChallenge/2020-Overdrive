package za.co.entelect.challenge.game.contracts.game

import java.util

import za.co.entelect.challenge.game.contracts.map.{CarGameMap, GameMap}
import za.co.entelect.challenge.game.contracts.player.Player

class CarMapGenerator extends GameMapGenerator {
    override def generateGameMap(list: util.List[Player]): GameMap = {
        val defaultHealth = 0;
        val defaultScore = 0;
        list.forEach(x => x.playerRegistered(new CarGamePlayer(defaultHealth,defaultScore)));
        return new CarGameMap(list);
    }
}
