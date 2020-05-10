import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer

class Command_Use_Lizard_Tests extends FunSuite {
    private val lizardCommandText = "USE_LIZARD"
    private val nothingCommandText = "NOTHING"
    private var commandFactory: CommandFactory = _
    private var lizardCommand: RawCommand = _
    private var nothingCommand: RawCommand = _

    def initialise() = {
        Config.loadDefault();
        commandFactory = new CommandFactory;
        lizardCommand = commandFactory.makeCommand(lizardCommandText)
        lizardCommand.setCommand(lizardCommandText)

        nothingCommand = commandFactory.makeCommand(nothingCommandText)
        nothingCommand.setCommand(nothingCommandText)
    }

    test("Given player uses lizard command with zero lizards then nothing happens") {
        initialise();
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.MUD_MAP_OBJECT);
        val testGamePlayer1 = TestHelper.getTestGamePlayer1();
        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer];

        testCarGamePlayer.speed = Config.SPEED_STATE_2;

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand);

        assert(!testCarGamePlayer.isLizarding);
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_1);
    }

    test("Given player uses lizard command with two lizard then player has one lizards") {
        initialise();
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.MUD_MAP_OBJECT);
        val testGamePlayer = TestHelper.getTestGamePlayer1();
        val testCarGamePlayer = testGamePlayer.asInstanceOf[CarGamePlayer];

        testCarGamePlayer.pickupLizard();
        testCarGamePlayer.pickupLizard();

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand);

        assert(testCarGamePlayer.isLizarding);
        assert(testCarGamePlayer.getPowerups().count(x => x == Config.LIZARD_POWERUP_ITEM) == 1)
    }

    test("Given player uses lizard command then is unaffected by Mud in the lane") {
        initialise();
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.MUD_MAP_OBJECT);
        val testGamePlayer = TestHelper.getTestGamePlayer1();
        val testCarGamePlayer = testGamePlayer.asInstanceOf[CarGamePlayer];

        testCarGamePlayer.pickupLizard();
        testCarGamePlayer.speed = Config.SPEED_STATE_2;

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand);

        assert(testCarGamePlayer.isLizarding);
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_2);
    }

    test("Given player uses lizard command then is affected by Mud at the end of the lane") {
        initialise();
        val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(6,9), Config.MUD_MAP_OBJECT);
        val testGamePlayer1 = TestHelper.getTestGamePlayer1();
        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer];

        testCarGamePlayer.pickupLizard();
        testCarGamePlayer.speed = Config.SPEED_STATE_3;

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand);

        assert(testCarGamePlayer.isLizarding);
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_2);
    }

    test("Given player uses lizard command then is unaffected by OilSpill in the lane") {
        initialise();
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.OIL_SPILL_MAP_OBJECT);
        val testGamePlayer = TestHelper.getTestGamePlayer1();
        val testCarGamePlayer = testGamePlayer.asInstanceOf[CarGamePlayer];

        testCarGamePlayer.pickupLizard();
        testCarGamePlayer.speed = Config.SPEED_STATE_2;

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand);

        assert(testCarGamePlayer.isLizarding);
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_2);
    }

    test("Given player uses lizard command then is affected by OilSpill at the end of the lane") {
        initialise();
        val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(6,9), Config.OIL_SPILL_MAP_OBJECT);
        val testGamePlayer1 = TestHelper.getTestGamePlayer1();
        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer];

        testCarGamePlayer.pickupLizard();
        testCarGamePlayer.speed = Config.SPEED_STATE_3;

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand);

        assert(testCarGamePlayer.isLizarding);
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_2);
    }

    test("Given player uses lizard command then is unaffected by Wall in the lane") {
        initialise();
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.WALL_MAP_OBJECT);
        val testGamePlayer = TestHelper.getTestGamePlayer1();
        val testCarGamePlayer = testGamePlayer.asInstanceOf[CarGamePlayer];

        testCarGamePlayer.pickupLizard();
        testCarGamePlayer.speed = Config.SPEED_STATE_2;

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand);

        assert(testCarGamePlayer.isLizarding);
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_2);
    }

    test("Given player uses lizard command then is affected by Wall at the end of the lane") {
        initialise();
        val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(6,9), Config.WALL_MAP_OBJECT);
        val testGamePlayer1 = TestHelper.getTestGamePlayer1();
        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer];

        testCarGamePlayer.pickupLizard();
        testCarGamePlayer.speed = Config.SPEED_STATE_3;

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand);

        assert(testCarGamePlayer.isLizarding);
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_1);
    }

    test("Given player uses lizard command then does not collect Boost in the lane") {
        initialise();
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.BOOST_MAP_OBJECT);
        val testGamePlayer = TestHelper.getTestGamePlayer1();
        val testCarGamePlayer = testGamePlayer.asInstanceOf[CarGamePlayer];

        testCarGamePlayer.pickupLizard();
        testCarGamePlayer.speed = Config.SPEED_STATE_2;

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand);

        assert(testCarGamePlayer.isLizarding);
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_2);
    }

    test("Given player uses lizard command then collects Boost at the end of the lane") {
        initialise();
        val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(6,9), Config.BOOST_MAP_OBJECT);
        val testGamePlayer1 = TestHelper.getTestGamePlayer1();
        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer];

        testCarGamePlayer.pickupLizard();
        testCarGamePlayer.speed = Config.SPEED_STATE_3;

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand);

        assert(testCarGamePlayer.isLizarding);
        assert(testCarGamePlayer.hasBoost())
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_3);
    }

    test("Given player uses lizard command then does not collect OilItem in the lane") {
        initialise();
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.OIL_ITEM_MAP_OBJECT);
        val testGamePlayer = TestHelper.getTestGamePlayer1();
        val testCarGamePlayer = testGamePlayer.asInstanceOf[CarGamePlayer];

        testCarGamePlayer.pickupLizard();
        testCarGamePlayer.speed = Config.SPEED_STATE_2;

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand);

        assert(testCarGamePlayer.isLizarding);
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_2);
    }

    test("Given player uses lizard command then collects OilItem at the end of the lane") {
        initialise();
        val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(6,9), Config.OIL_ITEM_MAP_OBJECT);
        val testGamePlayer1 = TestHelper.getTestGamePlayer1();
        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer];

        testCarGamePlayer.pickupLizard();
        testCarGamePlayer.speed = Config.SPEED_STATE_3;

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand);

        assert(testCarGamePlayer.isLizarding);
        assert(testCarGamePlayer.hasOilItem)
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_3);
    }

    test("Given player uses lizard command then does not collect Lizard in the lane") {
        initialise();
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.LIZARD_MAP_OBJECT);
        val testGamePlayer = TestHelper.getTestGamePlayer1();
        val testCarGamePlayer = testGamePlayer.asInstanceOf[CarGamePlayer];

        testCarGamePlayer.pickupLizard();
        testCarGamePlayer.speed = Config.SPEED_STATE_2;

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand);

        assert(testCarGamePlayer.isLizarding);
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_2);
    }

    test("Given player uses lizard command then collects Lizard at the end of the lane") {
        initialise();
        val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(6,9), Config.LIZARD_MAP_OBJECT);
        val testGamePlayer1 = TestHelper.getTestGamePlayer1();
        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer];

        testCarGamePlayer.pickupLizard();
        testCarGamePlayer.speed = Config.SPEED_STATE_3;

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand);

        assert(testCarGamePlayer.isLizarding);
        assert(testCarGamePlayer.hasLizard)
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_3);
    }

}
