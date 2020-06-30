package za.co.entelect.challenge.game.contracts.renderer

import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.GameMap
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer

abstract class BaseMapRenderer extends GameMapRenderer {

    override def render(gameMap: GameMap, gamePlayer: GamePlayer): String = {
        val carGameMap: CarGameMap = gameMap.asInstanceOf[CarGameMap]

        val shouldRenderFragment = gamePlayer != null
        if(shouldRenderFragment)
        {
            val carGamePlayer: CarGamePlayer = gamePlayer.asInstanceOf[CarGamePlayer]
            return renderFragment(carGameMap, carGamePlayer)
        }
        else
        {
            return renderVisualiserMap(carGameMap)
        }
    }

    def renderFragment(carGameMap: CarGameMap, carGamePlayer: CarGamePlayer): String
    def renderVisualiserMap(carGameMap: CarGameMap): String

    override def commandPrompt(gamePlayer: GamePlayer): String = {
        var commandPrompt = "===============================================================================================================\r\n"
        commandPrompt += "Please enter your command of choice:\r\n"
        commandPrompt += "Instructions are presented in the following format: {command} : description of the effect\r\n"
        commandPrompt += "DO_NOTHING    : move forward at current speed\r\n"
        commandPrompt += "TURN_LEFT     : move to the lane above you and forward at (speed-1)\r\n"
        commandPrompt += "TURN_RIGHT    : move to the lane below you and forward at (speed-1)\r\n"
        commandPrompt += "ACCELERATE    : increase speed and move forward at new speed\r\n"
        commandPrompt += "DECELERATE    : decrease speed and move forward at new speed\r\n"
        commandPrompt += "USE_BOOST     : use boost powerup if you have one in your possession\r\n"
        commandPrompt += "USE_OIL       : drop oil barrel if you have one in your possession\r\n"
        commandPrompt += "USE_LIZARD    : use lizard powerup to jump for this round avoiding all obstacles, pickups and players until you land\r\n"
        commandPrompt += "USE_TWEET X Y : spawn a cyber at lane X and blocknumber \r\n"
        commandPrompt += "USE_EMP       : use EMP powerup to slow down car infront\r\n"
        commandPrompt += "FIX           : use FIX command to repair car \r\n"
        commandPrompt += "====================================================================================================================="

        return commandPrompt
    }
}