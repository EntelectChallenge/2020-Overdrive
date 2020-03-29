import java.util

import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.{CarGamePlayer}
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

  test("Given player during race, when creating map fragment, minimal opponent information should be available")
  {
    initialise();
    val gameMap = TestHelper.initialiseGameWithNoMapObjects();
    val carGameMap = gameMap.asInstanceOf[CarGameMap];

    val testGamePlayer1 = TestHelper.getTestGamePlayer1();
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer];

    val mapFragment = carGameMap.getMapFragment(testCarGamePlayer1);

    assert(mapFragment.getOpponent() != null);
  }
}
