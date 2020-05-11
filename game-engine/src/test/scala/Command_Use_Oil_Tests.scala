import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class Command_Use_Oil_Tests extends FunSuite{
  private val nothingCommandText = "NOTHING"
  private val useOilCommandText = "USE_OIL"
  private var commandFactory: CommandFactory = null
  private var nothingCommand: RawCommand = null
  private var useOilCommand: RawCommand = null

  def initialise() = {
    Config.loadDefault();
    commandFactory = new CommandFactory;
    nothingCommand = commandFactory.makeCommand(nothingCommandText)

    useOilCommand = commandFactory.makeCommand(useOilCommandText)
    useOilCommand.setCommand(useOilCommandText)
  }

  test("Given player with no oil when USE_OIL command then nothing happens") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];

    val carGameMap = gameMap.asInstanceOf[CarGameMap];
    val playerPositionBeforeCommand = carGameMap.getPlayerBlockPosition(testCarGamePlayer1.getGamePlayerId());

    TestHelper.processRound(gameMap, useOilCommand, useOilCommand)

    val blockWherePlayerUsedToBe = carGameMap.blocks.find(x => x.getPosition().getLane() == playerPositionBeforeCommand.getLane() && x.getPosition().getBlockNumber() == playerPositionBeforeCommand.getBlockNumber()).get;
    assert(blockWherePlayerUsedToBe.mapObject != Config.OIL_SPILL_MAP_OBJECT);
  }

  test("Given player with oil when USE_OIL command then number of held oil powerups is reduced by 1") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];

    testCarGamePlayer1.pickupOilItem();
    testCarGamePlayer1.pickupOilItem();
    testCarGamePlayer1.pickupOilItem();
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, useOilCommand, useOilCommand)

    assert(testCarGamePlayer1.getPowerups().count(x => x == Config.OIL_POWERUP_ITEM) == 2);
  }

  test("Given player with oil when USE_OIL command then oil spill is left beneath the player") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];

    testCarGamePlayer1.pickupOilItem();
    val carGameMap = gameMap.asInstanceOf[CarGameMap];
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    val playerPositionBeforeCommand = carGameMap.getPlayerBlockPosition(testCarGamePlayer1.getGamePlayerId());
    TestHelper.processRound(gameMap, useOilCommand, useOilCommand)

    val blockWherePlayerUsedToBe = carGameMap.blocks.find(x => x.getPosition().getLane() == playerPositionBeforeCommand.getLane() && x.getPosition().getBlockNumber() == playerPositionBeforeCommand.getBlockNumber()).get;
    assert(blockWherePlayerUsedToBe.mapObject == Config.OIL_SPILL_MAP_OBJECT);
  }

  test("Given player with oil when USE_OIL command then player is not affected by their own oil spill") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];

    testCarGamePlayer1.pickupOilItem();
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, useOilCommand, useOilCommand)

    assert(testCarGamePlayer1.getState() == Config.USED_POWERUP_OIL_PLAYER_STATE);
    assert(testCarGamePlayer1.getSpeed() == Config.INITIAL_SPEED);
  }

  test("Given player with oil and enemy in block right behind when use oil then enemy is affected by oil") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    testCarGamePlayer1.speed = Config.SPEED_STATE_2 //should be 6
    val newLaneMidRacePlayer1 = 2
    val newBlockNumberMidRacePlayer1 = 35
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRacePlayer1, newBlockNumberMidRacePlayer1)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2();
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_2 //should be 6
    val newLaneMidRacePlayer2 = 2
    val newBlockNumberMidRacePlayer2 = 34
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

    testCarGamePlayer1.pickupOilItem();
    TestHelper.processRound(gameMap, useOilCommand, nothingCommand)

    assert(testCarGamePlayer2.getState() == Config.HIT_OIL_PLAYER_STATE);
    assert(testCarGamePlayer2.getSpeed() == Config.SPEED_STATE_1);
  }

}
