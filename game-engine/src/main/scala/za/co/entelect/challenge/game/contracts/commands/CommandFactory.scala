package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.Config.Config

class CommandFactory {
    private val NO_COMMAND = Config.NO_COMMAND;
    private val NOTHING = Config.NOTHING_COMMAND;

    private val TURN_LEFT = Config.TURN_LEFT_COMMAND;
    private val TURN_RIGHT = Config.TURN_RIGHT_COMMAND;

    private val ACCELERATE = Config.ACCELERATE_COMMAND;
    private val DECELERATE = Config.DECELERATE_COMMAND;

    private val USE_BOOST = Config.USE_BOOST_COMMAND;
    private val USE_OIL = Config.USE_OIL_COMMAND

    def makeCommand(commandText: String): RawCommand = {
        val commandHeader = commandText.toUpperCase();
        commandHeader match {
          case NO_COMMAND  => return defaultToNothingCommand("Bot did nothing");
          case NOTHING => return makeNothingCommand();
          case TURN_LEFT => return makeTurnLeftCommand();
          case TURN_RIGHT => return makeTurnRightCommand();
          case ACCELERATE => return makeAccelerateCommand();
          case DECELERATE => return makeDecelerateCommand();
          case USE_BOOST => return makeUseBoostCommand();
          case USE_OIL => makeUseOilCommand();
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

    private def makeTurnLeftCommand(): RawCommand = {
        val isLeft = true;
        return new ChangeLaneCommand(isLeft);
    }
    
    private def makeTurnRightCommand(): RawCommand = {
        val isLeft = false;
        return new ChangeLaneCommand(isLeft);
    }

    private def makeAccelerateCommand(): RawCommand = {
        return new AccelerateCommand;
    }

    private def makeDecelerateCommand(): RawCommand = {
        return new DecelerateCommand;
    }

    private def makeUseBoostCommand(): RawCommand = {
        return new UseBoostCommand;
    }

    private def makeUseOilCommand(): RawCommand = new UseOilCommand

}
