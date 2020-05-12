import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer

class MapObject_Lizard_Tests extends FunSuite{
    private val commandText = "NOTHING"
    private var commandFactory: CommandFactory = null
    private var nothingCommand: RawCommand = null

    def initialise() = {
        Config.loadDefault()
        commandFactory = new CommandFactory
        nothingCommand = commandFactory.makeCommand(commandText)
        nothingCommand.setCommand(commandText)
    }

    test("Given players during race when player hits lizard powerup then player gains 1 lizard powerup") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.LIZARD_MAP_OBJECT)
        val testGamePlayer1 = TestHelper.getTestGamePlayer1()

        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]
        testCarGamePlayer.speed = Config.SPEED_STATE_2

        TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

        assert(testCarGamePlayer.getPowerups().count(x => x == Config.LIZARD_POWERUP_ITEM) == 1)
    }

    test("Given players during race when player hits 2 lizard power ups then player gains 2 lizard powerups") {
        initialise()
        val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(3,4), Config.LIZARD_MAP_OBJECT)
        val testGamePlayer1 = TestHelper.getTestGamePlayer1()

        val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]
        testCarGamePlayer.speed = Config.SPEED_STATE_2

        TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

        assert(testCarGamePlayer.getPowerups().count(x => x == Config.LIZARD_POWERUP_ITEM) == 2)
    }
}
