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
  private val empCommandText = "USE_EMP"

  private var commandFactory: CommandFactory = null
  private var nothingCommand: RawCommand = null
  private var useOilCommand: RawCommand = null
  private var accelerateCommand: RawCommand = null
  private var declerateCommand: RawCommand = null
  private var useBoostCommand: RawCommand = null
  private var useEmpCommand: RawCommand = null

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

    useEmpCommand = commandFactory.makeCommand(empCommandText)
    useEmpCommand.setCommand(empCommandText)
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

  test("Given a player when player has 3 damage and boosts then player cannot accelerate beyond 6 speed")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(3, Array(73,74,75), Config.MUD_MAP_OBJECT)
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    //initialise player 1 state
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.speed = Config.SPEED_STATE_1 //speed 3
    testCarGamePlayer1.pickupBoost()

    //initialise player 1 position
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    val player1StartLane = 3;
    val player1StartBlockNumber = 72;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, player1StartLane, player1StartBlockNumber)

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand) // player now has 3 damage and 3 speed and moved 3 blocks
    val expectedLaneAfter1stRound = 3
    val expectedBlockNumberAfter1stRound = 75
    val actualPlayerPositionAfter1stRound = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert(testCarGamePlayer1.speed == 3, "player speed not correct after round 1") // 3 speed because mud cannot stop you
    assert(testCarGamePlayer1.getDamage() == 3, "player damage not correct after round 1")
    assert(actualPlayerPositionAfter1stRound.getLane() == expectedLaneAfter1stRound && actualPlayerPositionAfter1stRound.getBlockNumber() == expectedBlockNumberAfter1stRound, "player position not correct after round 1")

    TestHelper.processRound(gameMap, useBoostCommand, nothingCommand) //player should now have 6 speed and move 6 blocks
    val expectedPlayerLaneAfter2ndRound = 3
    val expectedBlockNumberAfter2ndRound = expectedBlockNumberAfter1stRound + 6
    val actualPlayerPositionAfter2ndRound = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert(testCarGamePlayer1.speed == 6, "player speed not correct after round 2")
    assert(testCarGamePlayer1.getDamage() == 3, "player damage not correct after round 2")
    assert(actualPlayerPositionAfter2ndRound.getLane() == expectedPlayerLaneAfter2ndRound && actualPlayerPositionAfter2ndRound.getBlockNumber() == expectedBlockNumberAfter2ndRound, "player position not correct after round 2")

    TestHelper.processRound(gameMap, accelerateCommand, nothingCommand) //player should now have 6 speed (because of damage) and move 6 blocks
    assert(testCarGamePlayer1.speed == 6)
    val expectedPlayerLaneAfter3rdRound = 3
    val expectedBlockNumberAfter3rdRound = expectedBlockNumberAfter2ndRound + 6
    val actualPlayerPositionAfter3rdRound = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert(testCarGamePlayer1.speed == 6, "player speed not correct after round 3")
    assert(testCarGamePlayer1.getDamage() == 3, "player damage not correct after speed 3")
    assert(actualPlayerPositionAfter3rdRound.getLane() == expectedPlayerLaneAfter3rdRound && actualPlayerPositionAfter3rdRound.getBlockNumber() == expectedBlockNumberAfter3rdRound, "player position not correct after round 3")
  }

  test("Given a player when player has 2 damage and boost runs out then player moves at speed capped by damage (8)")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(3, Array(73,74), Config.MUD_MAP_OBJECT)
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    //initialise player 1 state
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.speed = Config.SPEED_STATE_1 //speed 3
    testCarGamePlayer1.pickupBoost()

    //initialise player 1 position
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    val player1StartLane = 3;
    val player1StartBlockNumber = 72;
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, player1StartLane, player1StartBlockNumber)

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand) // player now has 2 damage and 8 speed and moved 15 blocks
    val expectedLaneAfter1stRound = 3
    val expectedBlockNumberAfter1stRound = player1StartBlockNumber + 3
    val actualPlayerPositionAfter1stRound = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert(testCarGamePlayer1.speed == 3, "player speed not correct after round 1") // 3 speed because mud cannot stop you
    assert(testCarGamePlayer1.getDamage() == 2, "player damage not correct after round 1")
    assert(actualPlayerPositionAfter1stRound.getLane() == expectedLaneAfter1stRound && actualPlayerPositionAfter1stRound.getBlockNumber() == expectedBlockNumberAfter1stRound, "player position not correct after round 1")

    TestHelper.processRound(gameMap, useBoostCommand, nothingCommand) // player now has 2 damage and 8 speed and moved 8 blocks and is boosting
    val expectedLaneAfter2ndRound = 3
    val expectedBlockNumberAfter2ndRound = expectedBlockNumberAfter1stRound + 8
    val actualPlayerPositionAfter2ndRound = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert(testCarGamePlayer1.speed == 8, "player speed not correct after round 2")
    assert(testCarGamePlayer1.getDamage() == 2, "player damage not correct after round 2")
    assert(testCarGamePlayer1.isBoosting())
    assert(testCarGamePlayer1.getBoostCounter() == 5)
    assert(actualPlayerPositionAfter2ndRound.getLane() == expectedLaneAfter2ndRound && actualPlayerPositionAfter2ndRound.getBlockNumber() == expectedBlockNumberAfter2ndRound, "player position not correct after round 2")

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand) //boost counter 4
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand) //boost counter 3
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand) //boost counter 2
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand) //boost counter 1
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand) //boost runs out and player is stiil at speed 8 and moved 8 blocks

    val expectedLaneAfterLastRound = 3
    val expectedBlockNumberAfterLastRound = expectedBlockNumberAfter2ndRound + (8*5)
    val actualPlayerPositionAfterLastRound = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert(testCarGamePlayer1.speed == 8, "player speed not correct after round 2")
    assert(testCarGamePlayer1.getDamage() == 2, "player damage not correct after round 2")
    assert(!testCarGamePlayer1.isBoosting())
    assert(testCarGamePlayer1.getBoostCounter() == 0)
    assert(actualPlayerPositionAfterLastRound.getLane() == expectedLaneAfterLastRound && actualPlayerPositionAfterLastRound.getBlockNumber() == expectedBlockNumberAfterLastRound, "player position not correct after round 2")

  }

  //Exhaustive EMP tests
  test("Given P1 at speed 6 behind P2 at speed 8 when P1 uses EMP then P2 is hit by EMP and slowed down")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    //initialise player 1 state
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val testCarGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    testCarGamePlayer1.speed = Config.SPEED_STATE_2
    testCarGamePlayer1.pickupEmp()

    val player1StartLane = 1
    val player1BlockNumber = 50
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1Id, player1StartLane, player1BlockNumber)
    val p1ScoreBeforeRound = testCarGamePlayer1.getScore

    //initialise player 2 state
    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testCarGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_3

    val player2StartLane = 2
    val player2BlockNumber = 100
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2Id, player2StartLane, player2BlockNumber)
    val p2ScoreBeforeRound = testCarGamePlayer2.getScore

    TestHelper.processRound(gameMap, useEmpCommand, nothingCommand)

    //Assert P1 affected correctly
    assert(testCarGamePlayer1.getState().contains(Config.USED_POWERUP_EMP_PLAYER_STATE), "p1 state not correct")
    assert(testCarGamePlayer1.getPowerups().count(x => x.equals(Config.EMP_POWERUP_ITEM)) == 0, "p1 emp count not correct")
    val expectP1LaneAfterRound = player1StartLane
    val expectedP1BlockNumberAfterRound = player1BlockNumber + Config.SPEED_STATE_2
    val actualP1PositionAfterRound = carGameMap.getPlayerBlockPosition(testCarGamePlayer1Id)
    assert(actualP1PositionAfterRound.getLane() == expectP1LaneAfterRound && actualP1PositionAfterRound.getBlockNumber() == expectedP1BlockNumberAfterRound, "p1 did not move correct number of blocks")
    assert(testCarGamePlayer1.speed == Config.SPEED_STATE_2, "p1 speed not correct after round")
    val expectedP1ScoreAfterRound = p1ScoreBeforeRound + Config.USE_POWERUP_BONUS
    assert(testCarGamePlayer1.getScore == expectedP1ScoreAfterRound)

    assert(testCarGamePlayer2.getState().contains(Config.HIT_EMP_PLAYER_STATE), "p2 state not correct")
    val expectedP2LaneAfterRound = player2StartLane
    val expectedP2BlockNumberAfterRound = player2BlockNumber
    val actualP2PositionAfterRound = carGameMap.getPlayerBlockPosition(testCarGamePlayer2Id)
    assert(actualP2PositionAfterRound.getLane() == expectedP2LaneAfterRound && actualP2PositionAfterRound.getBlockNumber() == expectedP2BlockNumberAfterRound, "p2 did not move correct number of blocks")
    assert(testCarGamePlayer2.speed == Config.SPEED_STATE_1, "p2 speed not correct after round")
    val expectedP2ScoreAfterRound = p2ScoreBeforeRound + Config.HIT_EMP_SCORE_PENALTY
    assert(testCarGamePlayer2.getScore == expectedP2ScoreAfterRound)
  }

  test("Given P2 at speed 6 behind P1 at speed 8 when P2 uses EMP then P1 is hit by EMP and slowed down")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    //initialise player 1 state
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val testCarGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    testCarGamePlayer1.speed = Config.SPEED_STATE_3

    val player1StartLane = 2
    val player1BlockNumber = 100
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1Id, player1StartLane, player1BlockNumber)
    val p1ScoreBeforeRound = testCarGamePlayer1.getScore

    //initialise player 2 state
    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testCarGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_2
    testCarGamePlayer2.pickupEmp()

    val player2StartLane = 3
    val player2BlockNumber = 50
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2Id, player2StartLane, player2BlockNumber)
    val p2ScoreBeforeRound = testCarGamePlayer2.getScore

    TestHelper.processRound(gameMap, nothingCommand, useEmpCommand)

    //Assert P1 affected correctly
    assert(testCarGamePlayer1.getState().contains(Config.HIT_EMP_PLAYER_STATE), "p1 state not correct")
    val expectP1LaneAfterRound = player1StartLane
    val expectedP1BlockNumberAfterRound = player1BlockNumber
    val actualP1PositionAfterRound = carGameMap.getPlayerBlockPosition(testCarGamePlayer1Id)
    assert(actualP1PositionAfterRound.getLane() == expectP1LaneAfterRound && actualP1PositionAfterRound.getBlockNumber() == expectedP1BlockNumberAfterRound, "p1 did not move correct number of blocks")
    assert(testCarGamePlayer1.speed == Config.SPEED_STATE_1, "p1 speed not correct after round")
    val expectedP1ScoreAfterRound = p1ScoreBeforeRound + Config.HIT_EMP_SCORE_PENALTY
    assert(testCarGamePlayer1.getScore == expectedP1ScoreAfterRound)

    assert(testCarGamePlayer2.getState().contains(Config.USED_POWERUP_EMP_PLAYER_STATE), "p2 state not correct")
    assert(testCarGamePlayer1.getPowerups().count(x => x.equals(Config.EMP_POWERUP_ITEM)) == 0, "p2 emp count not correct")
    val expectedP2LaneAfterRound = player2StartLane
    val expectedP2BlockNumberAfterRound = player2BlockNumber + Config.SPEED_STATE_2
    val actualP2PositionAfterRound = carGameMap.getPlayerBlockPosition(testCarGamePlayer2Id)
    assert(actualP2PositionAfterRound.getLane() == expectedP2LaneAfterRound && actualP2PositionAfterRound.getBlockNumber() == expectedP2BlockNumberAfterRound, "p2 did not move correct number of blocks")
    assert(testCarGamePlayer2.speed == Config.SPEED_STATE_2, "p2 speed not correct after round")
    val expectedP2ScoreAfterRound = p2ScoreBeforeRound + Config.USE_POWERUP_BONUS
    assert(testCarGamePlayer2.getScore == expectedP2ScoreAfterRound)
  }

}
