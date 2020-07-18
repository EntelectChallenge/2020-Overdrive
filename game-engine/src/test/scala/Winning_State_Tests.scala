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

  test("Given player 1 is ahead, when the max round limit is reached, player 1 should win") {
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
    assert(testCarGamePlayer1.getGamePlayerId().equals(winner.getGamePlayerId()), "Player 1 is the furthest on the track, therefore player 1 needs to be declared the winner")
  }

  test("Given player 1 and player 2 are tied and player 1 has the highest speed, when the max round limit is reached, player 1 should win") {
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
    assert(testCarGamePlayer1.getGamePlayerId().equals(winner.getGamePlayerId()), "Player 1 has the fastest speed, therefor player 1 is the winner")
  }

  test("Given player 1 and player 2 are tied on position and have the same speed, when the match ends due to max rounds reached player 1 needs to win because of highest score") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    carGameMap.setCurrentRound(Config.MAX_ROUNDS)

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 1, 3)
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.score = 10

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 2, 3)
    testCarGamePlayer2.accelerate()

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    val winner = carGameMap.getWinningPlayer.asInstanceOf[CarGamePlayer]
    assert(testCarGamePlayer1.getGamePlayerId().equals(winner.getGamePlayerId()), "Player 1 has the highest score, therefore player 1 is the winner")
  }

  test("Given player 1 is in the lead, has the fastest speed and has the highest score, when the game ends due to max rounds reached, player 1 needs to win") {
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
    assert(testCarGamePlayer1.getGamePlayerId().equals(winner.getGamePlayerId()), "Player 1 is the furthest on the track, has the fastest speed and highest score, therefore player 1 is the winner")
  }
}
