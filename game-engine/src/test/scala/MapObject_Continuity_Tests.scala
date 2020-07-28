import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class MapObject_Continuity_Tests extends FunSuite {
    private var commandFactory: CommandFactory = null
    private val nothingCommandText = "NOTHING"
    private var nothingCommand: RawCommand = null
    private val useBoostCommandText = "USE_BOOST"
    private var useBoostCommand: RawCommand = null
    private val useEmpCommandText = "USE_EMP"
    private var useEmpCommand: RawCommand = null

    private val fixCommandText = "FIX"
    private var fixCommand: RawCommand = null

    def initialise() = {
        Config.loadDefault()
        commandFactory = new CommandFactory

        nothingCommand = commandFactory.makeCommand(nothingCommandText)
        nothingCommand.setCommand(nothingCommandText)

        useBoostCommand = commandFactory.makeCommand(useBoostCommandText)
        useBoostCommand.setCommand(useBoostCommandText)

        useEmpCommand = commandFactory.makeCommand(useEmpCommandText)
        useEmpCommand.setCommand(useEmpCommandText)

        fixCommand = commandFactory.makeCommand(fixCommandText)
        fixCommand.setCommand(fixCommandText)
    }

    def makeTweetCommand(laneX: Int, blockY: Int) : RawCommand =
    {
        val tweetCommandText: String = s"USE_TWEET $laneX $blockY"
        val tweetCommand = commandFactory.makeCommand(tweetCommandText)
        tweetCommand.setCommand(tweetCommandText)
        return tweetCommand
    }

    test("Given player during race when player at max speed and hits wall and four mud blocks Then player speed is 0 and does not move next round") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMultipleDifferentMapObjectsAt(1, Array(2, 3, 4, 5), Array(Config.WALL_MAP_OBJECT, Config.MUD_MAP_OBJECT, Config.MUD_MAP_OBJECT, Config.MUD_MAP_OBJECT))
        val carGameMap = gameMap.asInstanceOf[CarGameMap]
        val testGamePlayer1 = TestHelper.getTestGamePlayer1()

        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]
        testCarGamePlayer.speed = Config.MAXIMUM_SPEED

        TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

        assert(testCarGamePlayer.getSpeed() == 0, "Player speed should be zero after all object collisions");

        val currentPosition = carGameMap.getPlayerBlockPosition(testCarGamePlayer.getGamePlayerId());

        TestHelper.processRound(gameMap, nothingCommand, nothingCommand);

        val nextPosition = carGameMap.getPlayerBlockPosition(testCarGamePlayer.getGamePlayerId());
        assert(currentPosition.getBlockNumber() == nextPosition.getBlockNumber(), "Player position should be same as previous round after nothing command");
    }

    test("Given player during race when player at boost speed and hits wall and four mud blocks Then player speed is 0 and does not move next round") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMultipleDifferentMapObjectsAt(1, Array(2, 3, 4, 5), Array(Config.WALL_MAP_OBJECT, Config.MUD_MAP_OBJECT, Config.MUD_MAP_OBJECT, Config.MUD_MAP_OBJECT))
        val carGameMap = gameMap.asInstanceOf[CarGameMap]
        val testGamePlayer1 = TestHelper.getTestGamePlayer1()

        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]

        testCarGamePlayer.pickupBoost()

        TestHelper.processRound(gameMap, useBoostCommand, nothingCommand)

        assert(testCarGamePlayer.getSpeed() == 0, "Player speed should be zero after all object collisions");

        val currentPosition = carGameMap.getPlayerBlockPosition(testCarGamePlayer.getGamePlayerId());

        TestHelper.processRound(gameMap, nothingCommand, nothingCommand);

        val nextPosition = carGameMap.getPlayerBlockPosition(testCarGamePlayer.getGamePlayerId());
        assert(currentPosition.getBlockNumber() == nextPosition.getBlockNumber(), "Player position should be same as previous round after nothing command");
    }

    test("Given player hit by emp Can still use a tweet in round"){
        initialise()
        val gameMap = TestHelper.initialiseGameWithNoMapObjects()
        val carGameMap = gameMap.asInstanceOf[CarGameMap]
        val testCarGamePlayer1 = TestHelper.getTestGamePlayer1().asInstanceOf[CarGamePlayer]
        val testCarGamePlayer2 = TestHelper.getTestGamePlayer2().asInstanceOf[CarGamePlayer]

        testCarGamePlayer1.pickupTweet();
        testCarGamePlayer2.pickupEmp();

        val targetLane = 2
        val targetBlock = 45
        val tweetCommand = makeTweetCommand(targetLane, targetBlock)

        TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 2, 20);
        TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 2, 10);

        TestHelper.processRound(gameMap, tweetCommand, useEmpCommand);

        assert(testCarGamePlayer1.speed == Config.SPEED_STATE_1, "Player 1 should be affected by EMP");
        val blockOfInterest = carGameMap.getBlocks().find(x => x.getPosition().getLane() == targetLane && x.getPosition().getBlockNumber() == targetBlock).get
        assert(blockOfInterest.isOccupiedByCyberTruck(), "Player 1's tweet command should result in the placement of cybertruck")
    }

    test("Given player hit by emp Can still use a tweet next round"){
        initialise()
        val gameMap = TestHelper.initialiseGameWithNoMapObjects()
        val carGameMap = gameMap.asInstanceOf[CarGameMap]
        val testCarGamePlayer1 = TestHelper.getTestGamePlayer1().asInstanceOf[CarGamePlayer]
        val testCarGamePlayer2 = TestHelper.getTestGamePlayer2().asInstanceOf[CarGamePlayer]

        testCarGamePlayer1.pickupTweet();
        testCarGamePlayer2.pickupEmp();

        val targetLane = 2
        val targetBlock = 55
        val tweetCommand = makeTweetCommand(targetLane, targetBlock)

        TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 2, 20);
        TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 2, 10);

        TestHelper.processRound(gameMap, nothingCommand, useEmpCommand);
        assert(testCarGamePlayer1.speed == Config.SPEED_STATE_1, "Player 1 should be affected by EMP");

        TestHelper.processRound(gameMap, tweetCommand, nothingCommand);
        val blockOfInterest = carGameMap.getBlocks().find(x => x.getPosition().getLane() == targetLane && x.getPosition().getBlockNumber() == targetBlock).get
        assert(blockOfInterest.isOccupiedByCyberTruck(), "Player 1's tweet command should result in the placement of cybertruck")
    }

    test("Given player hit by emp Can still use fix in round"){
        initialise()
        val gameMap = TestHelper.initialiseGameWithNoMapObjects()
        val carGameMap = gameMap.asInstanceOf[CarGameMap]
        val testCarGamePlayer1 = TestHelper.getTestGamePlayer1().asInstanceOf[CarGamePlayer]
        val testCarGamePlayer2 = TestHelper.getTestGamePlayer2().asInstanceOf[CarGamePlayer]

        testCarGamePlayer1.setDamage(2);
        testCarGamePlayer2.pickupEmp();

        TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 2, 20);
        TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 2, 10);

        TestHelper.processRound(gameMap, fixCommand, useEmpCommand);
        assert(testCarGamePlayer1.speed == Config.SPEED_STATE_1, "Player 1 should be affected by EMP");
        assert(testCarGamePlayer1.getDamage == 0, "Player 1 should be able to fix")
    }

    test("Given player hit by emp Can still use fix next round"){
        initialise()
        val gameMap = TestHelper.initialiseGameWithNoMapObjects()
        val carGameMap = gameMap.asInstanceOf[CarGameMap]
        val testCarGamePlayer1 = TestHelper.getTestGamePlayer1().asInstanceOf[CarGamePlayer]
        val testCarGamePlayer2 = TestHelper.getTestGamePlayer2().asInstanceOf[CarGamePlayer]

        testCarGamePlayer1.setDamage(2);
        testCarGamePlayer2.pickupEmp();

        TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 2, 20);
        TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 2, 10);

        TestHelper.processRound(gameMap, nothingCommand, useEmpCommand);
        assert(testCarGamePlayer1.speed == Config.SPEED_STATE_1, "Player 1 should be affected by EMP");

        TestHelper.processRound(gameMap, fixCommand, nothingCommand);
        assert(testCarGamePlayer1.getDamage == 0, "Player 1 should be able to fix")
    }
}
