import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class Bug_Tests extends FunSuite{
  private val commandText = "NOTHING";
  private var commandFactory: CommandFactory = null
  private var nothingCommand: RawCommand = null

  def initialise() = {
    Config.loadDefault();
    commandFactory = new CommandFactory;
    nothingCommand = commandFactory.makeCommand(commandText)
  }

  test("Given player during the race when next command would cause a collision then reset player position to what it was at the beggining of the round") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();

    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];

    val testGamePlayer2 = TestHelper.getTestGamePlayer2();
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer];

    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId();
    val newP1LaneMidRace = 3;
    val newP1BlockNumberMidRace = 25;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newP1LaneMidRace, newP1BlockNumberMidRace);

    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId();
    val newP2LaneMidRace = 3;
    val newP2BlockNumberMidRace = 30;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newP2LaneMidRace, newP2BlockNumberMidRace);

    nothingCommand.performCommand(gameMap, testGamePlayer1);
    nothingCommand.performCommand(gameMap, testGamePlayer2);

    val newPlayer1PositionAfterCommand = carGameMap.getPlayerBlockPosition(testGamePlayer1Id);
    assert(newPlayer1PositionAfterCommand.getLane() == newP1LaneMidRace && newPlayer1PositionAfterCommand.getBlockNumber() == newP1BlockNumberMidRace);
  }

  test("Given player during race when creating map fragment player must be visible in their own map fragment")
  {
    initialise();
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];

    val mapFragment = carGameMap.getMapFragment(testCarGamePlayer1);

    assert(mapFragment.getBlocks().find(x => x.occupiedByPlayerWithId == testCarGamePlayer1.getGamePlayerId()).isDefined);

  }
}
