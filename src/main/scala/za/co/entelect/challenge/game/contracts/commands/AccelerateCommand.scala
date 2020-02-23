package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.GameMap

class AccelerateCommand extends RawCommand {

      override def performCommand(gameMap: GameMap, player: GamePlayer) = {
        throw new NotImplementedError("No command perform command");
    }

}