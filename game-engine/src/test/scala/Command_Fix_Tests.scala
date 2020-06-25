import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class Command_Fix_Tests extends FunSuite{
  private val commandText = "FIX"
  private var commandFactory: CommandFactory = null
  private var fixCommand: RawCommand = null

  def initialise() = {
    Config.loadDefault()
    commandFactory = new CommandFactory
    fixCommand = commandFactory.makeCommand(commandText)
    fixCommand.setCommand(commandText)
  }

  test("Given player with two damage uses fix command then has zero damage") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer.setDamage(2);

    TestHelper.processRound(gameMap, fixCommand, fixCommand)

    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    val player1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1.asInstanceOf[CarGamePlayer].getGamePlayerId())

    assert(testCarGamePlayer.getDamage == 0)
  }

  test("Given player with speed uses fix command then does not move that round") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer.setDamage(2);

    TestHelper.processRound(gameMap, fixCommand, fixCommand)

    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    val player1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1.asInstanceOf[CarGamePlayer].getGamePlayerId())

    assert(player1Position.getLane() == Config.PLAYER_ONE_START_LANE && player1Position.getBlockNumber() == Config.PLAYER_ONE_START_BLOCK)
  }

}
