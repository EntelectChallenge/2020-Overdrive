import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class Command_Use_Oil_Tests extends FunSuite{
  private val commandText = "USE_OIL";
  private val commandFactory = new CommandFactory;
  private val useOilCommand = commandFactory.makeCommand(commandText);

  test("Given player with no oil when USE_OIL command then nothing happens") {
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];

    val carGameMap = gameMap.asInstanceOf[CarGameMap];
    val playerPositionBeforeCommand = carGameMap.getPlayerBlockPosition(testCarGamePlayer1.getGamePlayerId());
    useOilCommand.performCommand(gameMap, testGamePlayer1);

    val blockWherePlayerUsedToBe = carGameMap.blocks.find(x => x.getPosition().getLane() == playerPositionBeforeCommand.getLane() && x.getPosition().getBlockNumber() == playerPositionBeforeCommand.getBlockNumber()).get;
    assert(blockWherePlayerUsedToBe.mapObject != Config.OIL_SPILL_MAP_OBJECT);
  }

  test("Given player with oil when USE_OIL command then number of held oil powerups is reduced by 1") {
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];

    testCarGamePlayer1.pickupOilItem();
    testCarGamePlayer1.pickupOilItem();
    testCarGamePlayer1.pickupOilItem();
    useOilCommand.performCommand(gameMap, testGamePlayer1);

    assert(testCarGamePlayer1.getPowerups().count(x => x == Config.OIL_POWERUP_ITEM) == 2);
  }

  test("Given player with oil when USE_OIL command then oil spill is left where player was") {
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];

    testCarGamePlayer1.pickupOilItem();
    val carGameMap = gameMap.asInstanceOf[CarGameMap];
    val playerPositionBeforeCommand = carGameMap.getPlayerBlockPosition(testCarGamePlayer1.getGamePlayerId());
    useOilCommand.performCommand(gameMap, testGamePlayer1);

    val blockWherePlayerUsedToBe = carGameMap.blocks.find(x => x.getPosition().getLane() == playerPositionBeforeCommand.getLane() && x.getPosition().getBlockNumber() == playerPositionBeforeCommand.getBlockNumber()).get;
    assert(blockWherePlayerUsedToBe.mapObject == Config.OIL_SPILL_MAP_OBJECT);
  }

}
