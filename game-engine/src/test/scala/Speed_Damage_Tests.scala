import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class Speed_Damage_Tests extends FunSuite {

  def initialise() = {
    Config.loadDefault()
  }

  test("Given a player has 0 damage, when the player accelerates, the player should be able to reach a speed of 9") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    carGameMap.setCurrentRound(Config.MAX_ROUNDS)

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.speed = Config.SPEED_STATE_1
    testCarGamePlayer1.setDamage(0)
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    assert(testCarGamePlayer1.speed == Config.MAXIMUM_SPEED, "The player has 0 damage and can therefore can accelerate to a speed of 9")
  }

  test("Given a player has 1 damage, when the player accelerates, the player should be able to reach a speed of 9") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    carGameMap.setCurrentRound(Config.MAX_ROUNDS)

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.speed = Config.SPEED_STATE_1
    testCarGamePlayer1.setDamage(1)
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    assert(testCarGamePlayer1.speed == Config.MAXIMUM_SPEED, "The player has 1 damage and can therefore accelerate to a speed of 9")
  }

  test("Given a player has 2 damage, when the player accelerates, the player should be able to reach a speed of 8") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    carGameMap.setCurrentRound(Config.MAX_ROUNDS)

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.speed = Config.SPEED_STATE_1
    testCarGamePlayer1.setDamage(2)
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    assert(testCarGamePlayer1.speed == Config.SPEED_STATE_3, "The player has 2 damage and can therefore accelerate to a speed of 8")
  }

  test("Given a player has 3 damage, when the player accelerates, the player should be able to reach a speed of 6") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    carGameMap.setCurrentRound(Config.MAX_ROUNDS)

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.speed = Config.SPEED_STATE_1
    testCarGamePlayer1.setDamage(3)
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    assert(testCarGamePlayer1.speed == Config.SPEED_STATE_2, "The player has 3 damage and can therefore accelerate to a speed of 6")
  }

  test("Given a player has 4 damage, when the player accelerates, the player should be able to reach a speed of 3") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    carGameMap.setCurrentRound(Config.MAX_ROUNDS)

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.speed = Config.SPEED_STATE_1
    testCarGamePlayer1.setDamage(4)
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    assert(testCarGamePlayer1.speed == Config.SPEED_STATE_1, "The player has 3 damage and can therefore accelerate to a speed of 3")
  }

  test("Given a player has 5 damage, when the player accelerates, the player should be able to reach a speed of 0") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    carGameMap.setCurrentRound(Config.MAX_ROUNDS)

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.speed = Config.SPEED_STATE_1
    testCarGamePlayer1.setDamage(5)
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    testCarGamePlayer1.accelerate()
    assert(testCarGamePlayer1.speed == Config.MINIMUM_SPEED, "The player has 5 damage and can therefore accelerate to a speed of 0")
  }

}