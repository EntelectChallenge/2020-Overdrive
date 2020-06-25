import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer

class MapObject_Oil_Spill_Tests extends FunSuite{
  private val commandText = "NOTHING"
  private var commandFactory: CommandFactory = null
  private var nothingCommand: RawCommand = null

  def initialise() = {
    Config.loadDefault()
    commandFactory = new CommandFactory
    nothingCommand = commandFactory.makeCommand(commandText)
    nothingCommand.setCommand(commandText)
  }

  test("Given players during race when player hits oil then damage increases") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.OIL_SPILL_MAP_OBJECT)
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()

    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer.speed = Config.SPEED_STATE_2

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    assert(testCarGamePlayer.getDamage == Config.DAMAGE_OIL)
  }

  test("Given players during race when player hits oil then speed is reduced") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.OIL_SPILL_MAP_OBJECT)
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()

    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer.speed = Config.SPEED_STATE_2

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    assert(testCarGamePlayer.speed == Config.SPEED_STATE_1)
  }

  test("Given players during race when player hits oil twice then speed is reduced twice") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(3,4), Config.OIL_SPILL_MAP_OBJECT)
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()

    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer.speed = Config.SPEED_STATE_3

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    assert(testCarGamePlayer.speed == Config.SPEED_STATE_1)
    assert(testCarGamePlayer.getDamage == Config.DAMAGE_OIL * 2)
  }

  test("Given player that is boosting during race when player hits oil then speed is reduced and player is no longer boosting") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.OIL_SPILL_MAP_OBJECT)
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()

    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer.pickupBoost()
    testCarGamePlayer.useBoost()

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    assert(testCarGamePlayer.getDamage() == 1)
    assert(testCarGamePlayer.speed == Config.MAXIMUM_SPEED)
    assert(testCarGamePlayer.isBoosting() == false)
  }

  test("Given player that is going slowest speed when player hits oil then player continues going slowest speed") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.OIL_SPILL_MAP_OBJECT)
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()

    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer.speed = Config.SPEED_STATE_1

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    assert(testCarGamePlayer.speed == Config.SPEED_STATE_1)
  }
}
