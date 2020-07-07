import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.renderer.CarGameRendererFactory

class Winning_State_Tests extends FunSuite {

  private val nothingCommandText = "NOTHING"

  private var commandFactory: CommandFactory = null
  private var nothingCommand: RawCommand = null

  def initialise() = {
    Config.loadDefault()
    commandFactory = new CommandFactory

    nothingCommand = commandFactory.makeCommand(nothingCommandText)
    nothingCommand.setCommand(nothingCommandText)
  }

  test("Given the match ends due to max rounds reached player 1 needs to win because of being furthest on the track") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    carGameMap.setCurrentRound(Config.MAX_ROUNDS)

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 1, 5)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 2, 2)

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    val winner = carGameMap.getWinningPlayer.asInstanceOf[CarGamePlayer]
    assert(testCarGamePlayer1.getGamePlayerId().equals(winner.getGamePlayerId()), "Player 1 is the furthest on the track")
  }

  test("Given the match ends due to max rounds reached player 1 needs to win because of highest speed") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    carGameMap.setCurrentRound(Config.MAX_ROUNDS)

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 1, 3)
    testCarGamePlayer1.accelerate()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 2, 3)

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    val winner = carGameMap.getWinningPlayer.asInstanceOf[CarGamePlayer]
    assert(testCarGamePlayer1.getGamePlayerId().equals(winner.getGamePlayerId()), "Player 1 was traveling at the fastest speed")
  }

  test("Given the match ends due to max rounds reached player 1 needs to win because of highest score") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    carGameMap.setCurrentRound(Config.MAX_ROUNDS)

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 1, 3)
    testCarGamePlayer1.score = 10

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 2, 3)

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    val winner = carGameMap.getWinningPlayer.asInstanceOf[CarGamePlayer]
    assert(testCarGamePlayer1.getGamePlayerId().equals(winner.getGamePlayerId()), "Player 1 has the highest score")
  }

  test("Given the match ends due to max rounds reached player 1 needs to win because of furthest on track, fastest speed and highest score") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    carGameMap.setCurrentRound(Config.MAX_ROUNDS)

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 1, 10)
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.score = 10

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 2, 3)

    TestHelper.processRound(gameMap,nothingCommand, nothingCommand)

    val winner = carGameMap.getWinningPlayer.asInstanceOf[CarGamePlayer]
    assert(testCarGamePlayer1.getGamePlayerId().equals(winner.getGamePlayerId()), "Player 1 has the highest score")
  }
}
