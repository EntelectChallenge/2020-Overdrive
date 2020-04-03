import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.{CarGamePlayer}
import za.co.entelect.challenge.game.contracts.map.CarGameMap

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

  test("Given player with oil when USE_OIL command then oil spill is left one block behind current position and player is not penalized") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer1.pickupOilItem()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    val playerPositionBeforeCommand = carGameMap.getPlayerBlockPosition(testCarGamePlayer1.getGamePlayerId())

    TestHelper.processRound(gameMap, useOilCommand, useOilCommand)

    val blockBehindWherePlayerUsedToBe = carGameMap.blocks.find(x => x.getPosition().getLane() == playerPositionBeforeCommand.getLane() && x.getPosition().getBlockNumber() == playerPositionBeforeCommand.getBlockNumber()-1).get
    assert(blockBehindWherePlayerUsedToBe.mapObject == Config.OIL_SPILL_MAP_OBJECT)
  }
}
