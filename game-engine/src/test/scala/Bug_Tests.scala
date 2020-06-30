import java.io.{BufferedWriter, File, FileWriter}

import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.renderer.CarGameRendererFactory

class Bug_Tests extends FunSuite {
  private val nothingCommandText = "NOTHING"
  private val oilCommandText = "USE_OIL"
  private val accelerateCommandText = "ACCELERATE"
  private val decelerateCommandText = "DECELERATE"
  private val boostCommandText = "USE_BOOST"

  private var commandFactory: CommandFactory = null
  private var nothingCommand: RawCommand = null
  private var useOilCommand: RawCommand = null
  private var accelerateCommand: RawCommand = null
  private var declerateCommand: RawCommand = null
  private var useBoostCommand: RawCommand = null

  def initialise() = {
    Config.loadDefault()
    commandFactory = new CommandFactory

    nothingCommand = commandFactory.makeCommand(nothingCommandText)
    nothingCommand.setCommand(nothingCommandText)

    useOilCommand = commandFactory.makeCommand(oilCommandText)
    useOilCommand.setCommand(oilCommandText)

    accelerateCommand = commandFactory.makeCommand(accelerateCommandText)
    accelerateCommand.setCommand(accelerateCommandText)

    declerateCommand = commandFactory.makeCommand(decelerateCommandText)
    declerateCommand.setCommand(decelerateCommandText)

    useBoostCommand = commandFactory.makeCommand(boostCommandText)
    useBoostCommand.setCommand(boostCommandText)
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

  test("Given players during race when player end a turn on mud then speed is reduced only once")
  {
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

    val maxLane = 4

    val carGameRendererFactory = new CarGameRendererFactory
    val jsonRenderer = carGameRendererFactory.makeJsonRenderer()
    val gameMap1Json = jsonRenderer.render(gameMap1, null)

    for (blockNumber <- 0 until Config.TRACK_LENGTH - 1) {
      for (lane <- 0 until maxLane - 1) {
        val indexOfInterest = lane * Config.TRACK_LENGTH + blockNumber
        assert(carGameMap1Blocks(indexOfInterest).occupiedByPlayerWithId == carGameMap2Blocks(indexOfInterest).occupiedByPlayerWithId, "At: " + (lane + 1) + " , " + (blockNumber + 1) + " Blocks not occupied by same player")
        assert(carGameMap1Blocks(indexOfInterest).mapObject == carGameMap2Blocks(indexOfInterest).mapObject, "At: " + (lane + 1) + " , " + (blockNumber + 1) + " Blocks don't have same map object")
      }
    }
  }

  test("Given player during race when player users lizard powerup then next round player is not lizarding")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMapObjectAt(4, 115, Config.TWEET_MAP_OBJECT)
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.speed = Config.SPEED_STATE_1
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, 4, 106)

    testCarGamePlayer1.pickupLizard()
    val useLizardCommentText = "USE_LIZARD"
    var lizardCommand = commandFactory.makeCommand(useLizardCommentText)
    lizardCommand.setCommand(useLizardCommentText)
    TestHelper.processRound(gameMap, lizardCommand, nothingCommand)

    val accelerateCommandText = "ACCELERATE"
    var accelerateCommand = commandFactory.makeCommand(accelerateCommandText)
    accelerateCommand.setCommand(accelerateCommandText)
    TestHelper.processRound(gameMap, accelerateCommand, nothingCommand)

    assert(!testCarGamePlayer1.isLizarding, "player still lizarding in subsequent round")
  }

  test("Given player during race when player users lizard powerup then next round player can pickup items in middle of path")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMapObjectAt(4, 114, Config.TWEET_MAP_OBJECT)
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.speed = Config.SPEED_STATE_1
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, 4, 106)

    testCarGamePlayer1.pickupLizard()
    val useLizardCommentText = "USE_LIZARD"
    var lizardCommand = commandFactory.makeCommand(useLizardCommentText)
    lizardCommand.setCommand(useLizardCommentText)
    TestHelper.processRound(gameMap, lizardCommand, nothingCommand)

    val accelerateCommandText = "ACCELERATE"
    var accelerateCommand = commandFactory.makeCommand(accelerateCommandText)
    accelerateCommand.setCommand(accelerateCommandText)
    TestHelper.processRound(gameMap, accelerateCommand, nothingCommand)

    assert(testCarGamePlayer1.getPowerups().count(x => x.equals(Config.TWEET_POWERUP_ITEM)) == 1, "player did not pickup item they should have")
  }

  test("Given a player with a lizard when that player is lizarding over a cyber truck in their path, but not the last block, then they are not affected by the cybertruck")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.speed = Config.SPEED_STATE_2
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, 2, 139)

    testCarGamePlayer1.pickupTweet()
    val tweetCommandText = "USE_TWEET 2 150"
    var tweetCommand = commandFactory.makeCommand(tweetCommandText)
    tweetCommand.setCommand(tweetCommandText)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)

    testCarGamePlayer1.pickupLizard()
    val useLizardCommentText = "USE_LIZARD"
    var lizardCommand = commandFactory.makeCommand(useLizardCommentText)
    lizardCommand.setCommand(useLizardCommentText)
    TestHelper.processRound(gameMap, lizardCommand, nothingCommand)

    val expectedPlayerLane = 2
    val expectedPlayerBlockNumber = 151
    val actualPlayerPosition = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert(actualPlayerPosition.getLane() == expectedPlayerLane && actualPlayerPosition.getBlockNumber() == expectedPlayerBlockNumber)
  }

  test("Given a player with a lizard when that player is not lizarding over a cyber truck in their path, but not the last block, then they are affected by the cybertruck")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.speed = Config.SPEED_STATE_2
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, 2, 139)

    testCarGamePlayer1.pickupTweet()
    val tweetCommandText = "USE_TWEET 2 150"
    var tweetCommand = commandFactory.makeCommand(tweetCommandText)
    tweetCommand.setCommand(tweetCommandText)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    val expectedPlayerLane = 2
    val expectedPlayerBlockNumber = 149
    val actualPlayerPosition = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert(actualPlayerPosition.getLane() == expectedPlayerLane && actualPlayerPosition.getBlockNumber() == expectedPlayerBlockNumber)
  }

  test("Given a player with a lizard when that player is lizarding over a cyber truck in the last block of their path then they are stuck behind the cybertruck")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.speed = Config.SPEED_STATE_2
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, 2, 138)

    testCarGamePlayer1.pickupTweet()
    val tweetCommandText = "USE_TWEET 2 150"
    var tweetCommand = commandFactory.makeCommand(tweetCommandText)
    tweetCommand.setCommand(tweetCommandText)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)

    testCarGamePlayer1.pickupLizard()
    val useLizardCommentText = "USE_LIZARD"
    var lizardCommand = commandFactory.makeCommand(useLizardCommentText)
    lizardCommand.setCommand(useLizardCommentText)
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    val expectedPlayerLane = 2
    val expectedPlayerBlockNumber = 149
    val actualPlayerPosition = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert(actualPlayerPosition.getLane() == expectedPlayerLane && actualPlayerPosition.getBlockNumber() == expectedPlayerBlockNumber)
  }

  test("Given a player during race when player decelerates while on wall then player is stopped on wall")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMapObjectAt(2, 141, Config.WALL_MAP_OBJECT)
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.speed = Config.MINIMUM_SPEED

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, 2, 138)

    TestHelper.processRound(gameMap, accelerateCommand, nothingCommand) //should now be at speed 3 and hits a wall
    TestHelper.processRound(gameMap, declerateCommand, nothingCommand) //should now be at speed 0 and still on same block as wall

    assert(testCarGamePlayer1.speed == Config.MINIMUM_SPEED, "player speed should be stopped but has a positive speed")

    val expectedPlayerLane = 2
    val expectedPlayerBlockNumber = 141
    val actualPlayerPosition = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert(actualPlayerPosition.getLane() == expectedPlayerLane && actualPlayerPosition.getBlockNumber() == expectedPlayerBlockNumber, "player not stuck on same block as wall")

  }

  test("Given a player is boosting and hits a CyberTruck, the boost powerup should be taken away and the boosttick should be set to 0 and the players speed should be set to Speed state 1")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.speed = Config.SPEED_STATE_2

    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, 1, 1)
    testCarGamePlayer1.pickupBoost()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]

    testCarGamePlayer2.pickupTweet()

    val tweetCommandText = "USE_TWEET 1 18"
    var tweetCommand = commandFactory.makeCommand(tweetCommandText)
    tweetCommand.setCommand(tweetCommandText)
    TestHelper.processRound(gameMap, useBoostCommand, tweetCommand)

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    assert(testCarGamePlayer1.getBoostCounter() == 0, "The boost counter should be 0 as collision has occurred")
    assert(testCarGamePlayer1.isBoosting() == false, "The player should not be boosting anymore")
    assert(testCarGamePlayer1.speed == Config.SPEED_STATE_1, "The player should be traveling at Speed state one after collision")

  }

}
