package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.command.RawCommand

class CommandFactory {
    private val NO_COMMAND = "No Command";
    private val CHANGE_LANE = "Change Lane";
    private val ACCELERATE = "Accelerate";
    private val DECELERATE = "Decelerate";

    def makeCommand(commandText: String): RawCommand = {
        val splitCommand = commandText.split(" ");
        val commandHeader = splitCommand(0);
        commandHeader match {
          case NO_COMMAND  => return makeNoCommand();
          case CHANGE_LANE => return makeChangeLaneCommand(splitCommand(1));
          case ACCELERATE => return makeAccelerateCommand();
          case DECELERATE => return makeDecelerateCommand();
          case invalidCommandType  => throw new Exception("Invalid command type recieved: " + invalidCommandType.toString())
      }
    }

    private def makeNoCommand(): RawCommand = {
        return new NoCommand;
    }

    private def makeChangeLaneCommand(direction: String): RawCommand = {
        return new ChangeLaneCommand(direction);
    } 

    private def makeAccelerateCommand(): RawCommand = {
        return new AccelerateCommand;
    }

    private def makeDecelerateCommand(): RawCommand = {
        return new DecelerateCommand;
    }
}