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

  test("Given player 1 is ahead and both players moving at same speed, when the max round limit is reached, player 1 should win") {
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

  test("Given player 1 is ahead and player 2 is moving at greater speed, when the max round limit is reached, player 1 should win") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    carGameMap.setCurrentRound(Config.MAX_ROUNDS)

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 1, 50)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    testCarGamePlayer2.speed = Config.BOOST_SPEED
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 2, 2)

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    val winner = carGameMap.getWinningPlayer.asInstanceOf[CarGamePlayer]
    assert(testCarGamePlayer1.getGamePlayerId() == winner.getGamePlayerId(), "Player 1 is the furthest on the track, therefore player 1 needs to be declared the winner")
  }

  test("Given both players finish in same round then player with highest speed should win (edge case)") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupBoost()
    testCarGamePlayer1.useBoost()
    testCarGamePlayer1.tickBoost()
    testCarGamePlayer1.tickBoost()
    testCarGamePlayer1.tickBoost()
    testCarGamePlayer1.tickBoost() //boost counter is now 1
    testCarGamePlayer1.score = 541;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 1, 1491)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    testCarGamePlayer2.pickupBoost()
    testCarGamePlayer2.useBoost()
    testCarGamePlayer2.tickBoost()
    testCarGamePlayer2.tickBoost() //boost counter is now 3
    testCarGamePlayer2.score = 447
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 3, 1499)

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    val winner = carGameMap.getWinningPlayer.asInstanceOf[CarGamePlayer]
    assert(testCarGamePlayer2.getGamePlayerId() == winner.getGamePlayerId())
  }

  test("Given both players finish in same round with the same speed then player with the highest score should win") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.score = 541;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 1, 1496)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    testCarGamePlayer2.score = 447
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 3, 1499)

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    val winner = carGameMap.getWinningPlayer.asInstanceOf[CarGamePlayer]
    assert(testCarGamePlayer1.getGamePlayerId() == winner.getGamePlayerId())
  }

  test("Given both players finish in same round then player with highest speed should win") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.score = 541;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 1, 1496)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    testCarGamePlayer2.score = 447
    testCarGamePlayer2.speed = Config.SPEED_STATE_3
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 3, 1499)

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    val winner = carGameMap.getWinningPlayer.asInstanceOf[CarGamePlayer]
    assert(testCarGamePlayer2.getGamePlayerId() == winner.getGamePlayerId())
  }
}
