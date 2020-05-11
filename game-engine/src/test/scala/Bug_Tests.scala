import java.io.{BufferedWriter, File, FileWriter}

import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.renderer.CarGameRendererFactory

class Bug_Tests extends FunSuite{
  private val nothingCommandText = "NOTHING"
  private val oilCommandText = "USE_OIL"
  private var commandFactory: CommandFactory = null
  private var nothingCommand: RawCommand = null
  private var useOilCommand: RawCommand = null

  def initialise() = {
    Config.loadDefault()
    commandFactory = new CommandFactory
    nothingCommand = commandFactory.makeCommand(nothingCommandText)

    useOilCommand = commandFactory.makeCommand(oilCommandText)
    useOilCommand.setCommand(oilCommandText)
  }

  test("Given player during race when creating map fragment player must be visible in their own map fragment")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    val mapFragment = carGameMap.getMapFragment(testCarGamePlayer1)

    assert(mapFragment.getBlocks().find(x => x.occupiedByPlayerWithId == testCarGamePlayer1.getGamePlayerId()).isDefined)
  }

  test("Given player during race, when creating map fragment, minimal opponent information should be available")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    val mapFragment = carGameMap.getMapFragment(testCarGamePlayer1)

    assert(mapFragment.getOpponent() != null)
  }

  test("Given players during race when player end a turn on mud then speed is reduced only once") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 9, Config.MUD_MAP_OBJECT)
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()

    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer.speed = Config.SPEED_STATE_3

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    assert(testCarGamePlayer.speed == Config.SPEED_STATE_2)
  }

  test("Given the same seed when generating the map then the same map is generated")
  {
    initialise()

    val testSeed = 3045
    val gameMap1 = TestHelper.initialiseMapWithSeed(testSeed)
    val carGameMap1 = gameMap1.asInstanceOf[CarGameMap]
    val carGameMap1Blocks = carGameMap1.getBlocks()

    val gameMap2 = TestHelper.initialiseMapWithSeed(testSeed)
    val carGameMap2 = gameMap2.asInstanceOf[CarGameMap]
    val carGameMap2Blocks = carGameMap2.getBlocks()

    val maxLane = 4;

    val carGameRendererFactory = new CarGameRendererFactory;
    val jsonRenderer = carGameRendererFactory.makeJsonRenderer();
    val gameMap1Json = jsonRenderer.render(gameMap1, null);

    for(blockNumber <- 0 until Config.TRACK_LENGTH-1) {
        for(lane <- 0 until maxLane-1) {
            val indexOfInterest = lane*Config.TRACK_LENGTH + blockNumber;
            assert(carGameMap1Blocks(indexOfInterest).occupiedByPlayerWithId == carGameMap2Blocks(indexOfInterest).occupiedByPlayerWithId, "At: " + (lane+1) + " , " + (blockNumber+1) + " Blocks not occupied by same player")
            assert(carGameMap1Blocks(indexOfInterest).mapObject == carGameMap2Blocks(indexOfInterest).mapObject, "At: " + (lane+1) + " , " + (blockNumber+1) + " Blocks don't have same map object")
          }
      }
  }
}
