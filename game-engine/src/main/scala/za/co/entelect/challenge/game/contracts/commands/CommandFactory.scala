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
    private val USE_LIZARD = Config.USE_LIZARD_COMMAND;
    private val USE_TWEET = Config.USE_TWEET_COMMAND;

    def makeCommand(commandText: String): RawCommand = {
        val fullCommand = commandText.toUpperCase().split(" ");
        val commandHeader = fullCommand(0);
        commandHeader match {
          case NO_COMMAND  => return defaultToNothingCommand("Bot did nothing");
          case NOTHING => return makeNothingCommand();
          case TURN_LEFT => return makeTurnLeftCommand();
          case TURN_RIGHT => return makeTurnRightCommand();
          case ACCELERATE => return makeAccelerateCommand();
          case DECELERATE => return makeDecelerateCommand();
          case USE_BOOST => return makeUseBoostCommand();
          case USE_OIL => return makeUseOilCommand();
          case USE_LIZARD => return makeUseLizardCommand();
          case USE_TWEET => return makeUseTweetCommand(fullCommand);
          case invalidCommandType  => return defaultToNothingCommand("Bot sent invalid command: " + invalidCommandType.toString());
      }
    }

    private def defaultToNothingCommand(reasonForDefault: String): RawCommand = {
        //TODO: record reason for default as part of error list
        return new NothingCommand;
    }

    private def makeNothingCommand(): RawCommand = new NothingCommand

    private def makeTurnLeftCommand(): RawCommand = {
        val isLeft = true;
        return new ChangeLaneCommand(isLeft);
    }
    
    private def makeTurnRightCommand(): RawCommand = {
        val isLeft = false;
        return new ChangeLaneCommand(isLeft);
    }

    private def makeAccelerateCommand(): RawCommand = new AccelerateCommand

    private def makeDecelerateCommand(): RawCommand = new DecelerateCommand

    private def makeUseBoostCommand(): RawCommand = new UseBoostCommand

    private def makeUseOilCommand(): RawCommand = new UseOilCommand

    private def makeUseLizardCommand(): RawCommand = new UseLizardCommand

    private def makeUseTweetCommand(fullCommand: Array[String]): RawCommand =
    {
      if(fullCommand.length < 3)
      {
        return defaultToNothingCommand("Not all arguments provided for tweet command");
      }
      var lane: Int = 0;
      var block: Int = 0;
      try {
        lane = fullCommand(1).toInt
        block = fullCommand(2).toInt
      } catch {
        case e: Exception => return defaultToNothingCommand("Invalid land and block values for tweet command");
      }

      if(lane < Config.MIN_LANE || lane > Config.MAX_LANE)
      {
        return defaultToNothingCommand("Lane for TWEET command out of bounds");
      }
      if(block < 1 || block > Config.TRACK_LENGTH)
      {
        return defaultToNothingCommand("Block for TWEET command out of bounds");
      }

      return new UseTweetCommand(lane, block);
    }

}
