import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class Command_Use_Lizard_Tests extends FunSuite {
    private val lizardCommandText = "USE_LIZARD"
    private val nothingCommandText = "NOTHING"
    private var commandFactory: CommandFactory = _
    private var lizardCommand: RawCommand = _
    private var nothingCommand: RawCommand = _

    def initialise() = {
        Config.loadDefault()
        commandFactory = new CommandFactory
        lizardCommand = commandFactory.makeCommand(lizardCommandText)
        lizardCommand.setCommand(lizardCommandText)

        nothingCommand = commandFactory.makeCommand(nothingCommandText)
        nothingCommand.setCommand(nothingCommandText)
    }

    test("Given player uses lizard command with zero lizards then nothing happens") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.MUD_MAP_OBJECT)
        val testGamePlayer1 = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]

        testCarGamePlayer.speed = Config.SPEED_STATE_2

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand)

        assert(!testCarGamePlayer.isLizarding)
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_1)
    }

    test("Given player uses lizard command with two lizard then player has one lizards") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.MUD_MAP_OBJECT)
        val testGamePlayer = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer = testGamePlayer.asInstanceOf[CarGamePlayer]

        testCarGamePlayer.pickupLizard()
        testCarGamePlayer.pickupLizard()

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand)

        assert(testCarGamePlayer.isLizarding)
        assert(testCarGamePlayer.getPowerups().count(x => x == Config.LIZARD_POWERUP_ITEM) == 1)
    }

    test("Given player uses lizard command then is unaffected by Mud in the lane") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.MUD_MAP_OBJECT)
        val testGamePlayer = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer = testGamePlayer.asInstanceOf[CarGamePlayer]

        testCarGamePlayer.pickupLizard()
        testCarGamePlayer.speed = Config.SPEED_STATE_2

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand)

        assert(testCarGamePlayer.isLizarding)
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_2)
    }

    test("Given player uses lizard command then is affected by Mud at the end of the lane") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(6,9), Config.MUD_MAP_OBJECT)
        val testGamePlayer1 = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]

        testCarGamePlayer.pickupLizard()
        testCarGamePlayer.speed = Config.SPEED_STATE_3

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand)

        assert(testCarGamePlayer.isLizarding)
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_2)
    }

    test("Given player uses lizard command then is unaffected by OilSpill in the lane") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.OIL_SPILL_MAP_OBJECT)
        val testGamePlayer = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer = testGamePlayer.asInstanceOf[CarGamePlayer]

        testCarGamePlayer.pickupLizard()
        testCarGamePlayer.speed = Config.SPEED_STATE_2

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand)

        assert(testCarGamePlayer.isLizarding)
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_2)
    }

    test("Given player uses lizard command then is affected by OilSpill at the end of the lane") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(6,9), Config.OIL_SPILL_MAP_OBJECT)
        val testGamePlayer1 = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]

        testCarGamePlayer.pickupLizard()
        testCarGamePlayer.speed = Config.SPEED_STATE_3

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand)

        assert(testCarGamePlayer.isLizarding)
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_2)
    }

    test("Given player uses lizard command then is unaffected by Wall in the lane") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.WALL_MAP_OBJECT)
        val testGamePlayer = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer = testGamePlayer.asInstanceOf[CarGamePlayer]

        testCarGamePlayer.pickupLizard()
        testCarGamePlayer.speed = Config.SPEED_STATE_2

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand)

        assert(testCarGamePlayer.isLizarding)
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_2)
    }

    test("Given player uses lizard command then is affected by Wall at the end of the lane") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(6,9), Config.WALL_MAP_OBJECT)
        val testGamePlayer1 = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]

        testCarGamePlayer.pickupLizard()
        testCarGamePlayer.speed = Config.SPEED_STATE_3

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand)

        assert(testCarGamePlayer.isLizarding)
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_1)
    }

    test("Given player uses lizard command then does not collect Boost in the lane") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.BOOST_MAP_OBJECT)
        val testGamePlayer = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer = testGamePlayer.asInstanceOf[CarGamePlayer]

        testCarGamePlayer.pickupLizard()
        testCarGamePlayer.speed = Config.SPEED_STATE_2

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand)

        assert(testCarGamePlayer.isLizarding)
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_2)
    }

    test("Given player uses lizard command then collects Boost at the end of the lane") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(6,9), Config.BOOST_MAP_OBJECT)
        val testGamePlayer1 = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]

        testCarGamePlayer.pickupLizard()
        testCarGamePlayer.speed = Config.SPEED_STATE_3

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand)

        assert(testCarGamePlayer.isLizarding)
        assert(testCarGamePlayer.hasBoost())
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_3)
    }

    test("Given player uses lizard command then does not collect OilItem in the lane") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.OIL_ITEM_MAP_OBJECT)
        val testGamePlayer = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer = testGamePlayer.asInstanceOf[CarGamePlayer]

        testCarGamePlayer.pickupLizard()
        testCarGamePlayer.speed = Config.SPEED_STATE_2

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand)

        assert(testCarGamePlayer.isLizarding)
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_2)
    }

    test("Given player uses lizard command then collects OilItem at the end of the lane") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(6,9), Config.OIL_ITEM_MAP_OBJECT)
        val testGamePlayer1 = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]

        testCarGamePlayer.pickupLizard()
        testCarGamePlayer.speed = Config.SPEED_STATE_3

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand)

        assert(testCarGamePlayer.isLizarding)
        assert(testCarGamePlayer.hasOilItem)
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_3)
    }

    test("Given player uses lizard command then does not collect Lizard in the lane") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.LIZARD_MAP_OBJECT)
        val testGamePlayer = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer = testGamePlayer.asInstanceOf[CarGamePlayer]

        testCarGamePlayer.pickupLizard()
        testCarGamePlayer.speed = Config.SPEED_STATE_2

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand)

        assert(testCarGamePlayer.isLizarding)
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_2)
    }

    test("Given player uses lizard command then collects Lizard at the end of the lane") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(6,9), Config.LIZARD_MAP_OBJECT)
        val testGamePlayer1 = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]

        testCarGamePlayer.pickupLizard()
        testCarGamePlayer.speed = Config.SPEED_STATE_3

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand)

        assert(testCarGamePlayer.isLizarding)
        assert(testCarGamePlayer.hasLizard)
        assert(testCarGamePlayer.speed == Config.SPEED_STATE_3)
    }

    test("Given a player that is lizarding when they would land on another player in the final block then they end up behind that player") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(6,9), Config.LIZARD_MAP_OBJECT)
        val carGameMap = gameMap.asInstanceOf[CarGameMap]
        val testGamePlayer1 = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
        val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
        testCarGamePlayer1.pickupLizard()

        testCarGamePlayer1.speed = Config.SPEED_STATE_2 //should be 6
        val newLaneMidRacePlayer1 = 2
        val newBlockNumberMidRacePlayer1 = 35
        TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRacePlayer1, newBlockNumberMidRacePlayer1)

        val testGamePlayer2 = TestHelper.getTestGamePlayer2()
        val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
        val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
        testCarGamePlayer2.speed = Config.SPEED_STATE_1 //should be 3
        val newLaneMidRacePlayer2 = 2
        val newBlockNumberMidRacePlayer2 = 38
        TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

        TestHelper.processRound(gameMap, lizardCommand, nothingCommand)

        val expectedLane = 2
        val expectedPlayer2BlockNumber = 41
        val actualPlayer2PositionOnMap = carGameMap.getPlayerBlockPosition(testGamePlayer2Id)
        assert(actualPlayer2PositionOnMap.getLane() == expectedLane && actualPlayer2PositionOnMap.getBlockNumber() == expectedPlayer2BlockNumber, "non lizarding player at end of lane did not stop in their correct block")

        val expectedPlayer1BlockNumber = 40
        val actualPlayer1PositionOnMap = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
        assert(actualPlayer1PositionOnMap.getLane() == expectedLane && actualPlayer1PositionOnMap.getBlockNumber() == expectedPlayer1BlockNumber, "lizarding player did not end up one block behind player they landed on in last block")
    }

    test("Given a player that is lizarding when they would rear end another player mid path then they end up flying over that player") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(6,9), Config.LIZARD_MAP_OBJECT)
        val carGameMap = gameMap.asInstanceOf[CarGameMap]
        val testGamePlayer1 = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
        val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
        testCarGamePlayer1.pickupLizard()

        testCarGamePlayer1.speed = Config.SPEED_STATE_2 //should be 6
        val newLaneMidRacePlayer1 = 2
        val newBlockNumberMidRacePlayer1 = 35
        TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRacePlayer1, newBlockNumberMidRacePlayer1)

        val testGamePlayer2 = TestHelper.getTestGamePlayer2()
        val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
        val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
        testCarGamePlayer2.speed = Config.SPEED_STATE_1 //should be 3
        val newLaneMidRacePlayer2 = 2
        val newBlockNumberMidRacePlayer2 = 37
        TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

        TestHelper.processRound(gameMap, lizardCommand, nothingCommand)

        val expectedLane = 2
        val expectedPlayer2BlockNumber = 40
        val actualPlayer2PositionOnMap = carGameMap.getPlayerBlockPosition(testGamePlayer2Id)
        assert(actualPlayer2PositionOnMap.getLane() == expectedLane && actualPlayer2PositionOnMap.getBlockNumber() == expectedPlayer2BlockNumber)

        val expectedPlayer1BlockNumber = 41
        val actualPlayer1PositionOnMap = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
        assert(actualPlayer1PositionOnMap.getLane() == expectedLane && actualPlayer1PositionOnMap.getBlockNumber() == expectedPlayer1BlockNumber)
    }

    test("Given both players are lizarding when player 2 would rear end player 1 in final block Then maintain original order") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(6,9), Config.LIZARD_MAP_OBJECT)
        val carGameMap = gameMap.asInstanceOf[CarGameMap]

        val testGamePlayer1 = TestHelper.getTestGamePlayer1()
        val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
        val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
        testCarGamePlayer1.pickupLizard()
        testCarGamePlayer1.speed = Config.SPEED_STATE_1 //should be 3
        val newLaneMidRacePlayer1 = 2
        val newBlockNumberMidRacePlayer1 = 35 // means end of round will be x = 38
        TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRacePlayer1, newBlockNumberMidRacePlayer1)

        val testGamePlayer2 = TestHelper.getTestGamePlayer2()
        val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
        val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
        testCarGamePlayer1.pickupLizard()
        testCarGamePlayer2.speed = Config.SPEED_STATE_2 //should be 6
        val newLaneMidRacePlayer2 = 2
        val newBlockNumberMidRacePlayer2 = 32 // means end of round will be x = 38
        TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

        TestHelper.processRound(gameMap, lizardCommand, lizardCommand)

        val expectedPlayer2BlockNumber = 37
        val expectedPlayer1BlockNumber = 38
        val actualPlayer2PositionOnMap = carGameMap.getPlayerBlockPosition(testGamePlayer2Id)
        val actualPlayer1PositionOnMap = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)

        assert(expectedPlayer2BlockNumber == actualPlayer2PositionOnMap.getBlockNumber())
        assert(expectedPlayer1BlockNumber == actualPlayer1PositionOnMap.getBlockNumber())
    }
}
