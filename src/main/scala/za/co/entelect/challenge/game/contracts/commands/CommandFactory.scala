package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.command.RawCommand

class CommandFactory {
    private val NO_COMMAND = "No Command";
    private val NOTHING = "Nothing";

    private val CHANGE_LANE = "Change Lane";
    private val LEFT = "Left";
    private val RIGHT = "Right";

    private val ACCELERATE = "Accelerate";
    private val DECELERATE = "Decelerate";

    def makeCommand(commandText: String): RawCommand = {
        val splitCommand = commandText.split(":");
        val commandHeader = splitCommand(0);
        commandHeader match {
          case NO_COMMAND  => return defaultToNothingCommand("Bot did nothing");
          case NOTHING => return makeNothingCommand();
          case CHANGE_LANE => return makeChangeLaneCommand(splitCommand(1));
          case ACCELERATE => return makeAccelerateCommand();
          case DECELERATE => return makeDecelerateCommand();
          case invalidCommandType  => return defaultToNothingCommand("Bot sent invalid command: " + invalidCommandType.toString())
      }
    }

    private def defaultToNothingCommand(reasonForDefault: String): RawCommand = {
        //TODO: record reason for default as part of error list
        return new NothingCommand;
    }

    private def makeNothingCommand(): RawCommand = {
        return new NothingCommand;
    }

    private def makeChangeLaneCommand(direction: String): RawCommand = {
        val left = true;
        val right = false;
        direction match {
            case LEFT => return new ChangeLaneCommand(left);
            case RIGHT => return new ChangeLaneCommand(right);
            case invalidDirection => return defaultToNothingCommand("Invalid direction given for change lane command: " + invalidDirection.toString());
        }
    } 

    private def makeAccelerateCommand(): RawCommand = {
        return new AccelerateCommand;
    }

    private def makeDecelerateCommand(): RawCommand = {
        return new DecelerateCommand;
    }
}