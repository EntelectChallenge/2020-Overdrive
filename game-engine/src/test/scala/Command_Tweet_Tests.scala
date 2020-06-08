import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.{BlockPosition, CarGameMap}

class Command_Tweet_Tests extends FunSuite{
  private val commandText = "NOTHING"
  private val turnRightCommandText = "TURN_RIGHT"
  private val accelerateCommandText = "ACCELERATE"
  private var commandFactory: CommandFactory = null
  private var nothingCommand: RawCommand = null
  private var turnRightCommand: RawCommand = null
  private var accelerateCommand: RawCommand = null

  def initialise() = {
    Config.loadDefault()
    commandFactory = new CommandFactory
    nothingCommand = commandFactory.makeCommand(commandText)
    nothingCommand.setCommand(commandText)
    turnRightCommand = commandFactory.makeCommand(turnRightCommandText)
    turnRightCommand.setCommand(turnRightCommandText)
    accelerateCommand = commandFactory.makeCommand(accelerateCommandText)
    accelerateCommand.setCommand(accelerateCommandText)
  }

  def makeTweetCommand(laneX: Int, blockY: Int) : RawCommand =
  {
    val tweetCommandText: String = s"USE_TWEET $laneX $blockY"
    val tweetCommand = commandFactory.makeCommand(tweetCommandText)
    tweetCommand.setCommand(tweetCommandText)
    return tweetCommand
  }

  test("Given a seed when generating a map for a race then there are tweet powerups on the map")
  {
    initialise()
    val testSeed = 3045
    val gameMap1 = TestHelper.initialiseMapWithSeed(testSeed)
    val carGameMap1 = gameMap1.asInstanceOf[CarGameMap]
    val carGameMap1Blocks = carGameMap1.getBlocks()

    val numberOfTweetsOnMap = carGameMap1Blocks.count(x => x.getMapObject() == Config.TWEET_MAP_OBJECT)
    assert(numberOfTweetsOnMap > 0)
  }

  test("Given player during race, when path includes tweet pickup then player gains a tweet power-up")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMapObjectAt(1, 3, Config.TWEET_MAP_OBJECT)
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()

    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    assert(testCarGamePlayer.getPowerups().count(x => x == Config.TWEET_POWERUP_ITEM) == 1)
  }

  test("Given player during race, when path includes 3 tweet pickups then player gains 3 tweet power-ups")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithMultipleSameMapObjectsAt(1, Array(2,3,5), Config.TWEET_MAP_OBJECT)
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()

    val testCarGamePlayer = testGamePlayer1.asInstanceOf[CarGamePlayer]

    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    assert(testCarGamePlayer.getPowerups().count(x => x == Config.TWEET_POWERUP_ITEM) == 3)
  }

  test("Given player who uses tweet when no tweets left then player does nothing") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()

    val tweetCommand = makeTweetCommand(1, 1)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)

    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    val player1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1.asInstanceOf[CarGamePlayer].getGamePlayerId())

    val expectedLane = Config.PLAYER_ONE_START_LANE
    val expectedBlock = Config.PLAYER_ONE_START_BLOCK + Config.INITIAL_SPEED

    assert(player1Position.getLane() == expectedLane && player1Position.getBlockNumber() == expectedBlock, "player movement after invalid tweet did not indicate DO_NOTHING movement")

    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    assert(testCarGamePlayer1.getState() == Config.NOTHING_PLAYER_STATE, "player state after invalid tweet did not indicate DO_NOTHING player state")
  }

  test("Given player during race with one tweet power-up when player uses tweet then player has no tweet power-ups left")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer1.pickupTweet()
    val tweetCommand = makeTweetCommand(1, 1)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)

    assert(testCarGamePlayer1.getPowerups().count(x => x == Config.TWEET_POWERUP_ITEM) == 0)
  }

  test ("Given player during race with multiple tweet power-ups when player uses tweet then number of tweet power-ups is reduced by one")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer1.pickupTweet()
    testCarGamePlayer1.pickupTweet()
    val tweetCommand = makeTweetCommand(1, 1)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)

    assert(testCarGamePlayer1.getPowerups().count(x => x == Config.TWEET_POWERUP_ITEM) == 1)
  }

  test ("Given player during race with a tweet power-up when player uses tweet then player state is updated to used tweet state")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    testCarGamePlayer1.pickupTweet()
    val tweetCommand = makeTweetCommand(1, 1)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)

    assert(testCarGamePlayer1.getState() == Config.USED_POWERUP_TWEET_PLAYER_STATE)
  }

  test ("Given player during race with a tweet power-up when player uses tweet then player gets used powerup bonus to score")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]

    val initialScore = testCarGamePlayer1.getScore()

    testCarGamePlayer1.pickupTweet()
    val tweetCommand = makeTweetCommand(1, 1)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)

    val expectedScore = initialScore + Config.PICKUP_POWERUP_BONUS + Config.USE_POWERUP_BONUS
    val actualScore = testCarGamePlayer1.getScore()
    assert(actualScore == expectedScore)
  }

  test("Given player who uses tweet when lane x is out of bounds then player does nothing") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()

    val tweetCommand = makeTweetCommand(5, 1)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)

    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    val player1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1.asInstanceOf[CarGamePlayer].getGamePlayerId())

    val expectedLane = Config.PLAYER_ONE_START_LANE
    val expectedBlock = Config.PLAYER_ONE_START_BLOCK + Config.INITIAL_SPEED

    assert(player1Position.getLane() == expectedLane && player1Position.getBlockNumber() == expectedBlock, "player movement after invalid tweet did not indicate DO_NOTHING movement")

    assert(testCarGamePlayer1.getState() == Config.NOTHING_PLAYER_STATE, "player state after invalid tweet did not indicate DO_NOTHING player state")
  }

  test("Given player who uses tweet when block y is out of bounds then player does nothing") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()

    val tweetCommand = makeTweetCommand(1, Config.TRACK_LENGTH + 1)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)

    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    val player1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1.asInstanceOf[CarGamePlayer].getGamePlayerId())

    val expectedLane = Config.PLAYER_ONE_START_LANE
    val expectedBlock = Config.PLAYER_ONE_START_BLOCK + Config.INITIAL_SPEED

    assert(player1Position.getLane() == expectedLane && player1Position.getBlockNumber() == expectedBlock, "player movement after invalid tweet did not indicate DO_NOTHING movement")

    assert(testCarGamePlayer1.getState() == Config.NOTHING_PLAYER_STATE, "player state after invalid tweet did not indicate DO_NOTHING player state")
  }

  test("Given player who uses tweet with lane x and block y then cyber truck appears at lane x and block y") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()

    val targetLane = 1
    val targetBlock = 765
    val tweetCommand = makeTweetCommand(targetLane, targetBlock)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)

    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    val blockOfInterest = carGameMap.getBlocks().find(x => x.getPosition().getLane() == targetLane && x.getPosition().getBlockNumber() == targetBlock).get
    assert(blockOfInterest.isOccupiedByCyberTruck())
  }

  test("Given player 1 who uses tweet to place cyber truck in player 2 path in current round then player 2 is not affected because cyber truck only appears next round") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_3
    val newLaneMidRacePlayer2 = 1
    val newBlockNumberMidRacePlayer2 = 760
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

    val targetLane = 1
    val targetBlock = 765
    val tweetCommand = makeTweetCommand(targetLane, targetBlock)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)

    val expectedPlayer2LaneEndOfRound = 1
    val expectedPlayer2BlockNumberEndOfRound = 760 + Config.SPEED_STATE_3
    val actualPlayer2Position = carGameMap.getPlayerBlockPosition(testGamePlayer2Id)

    assert(actualPlayer2Position.getLane() == expectedPlayer2LaneEndOfRound && actualPlayer2Position.getBlockNumber() == expectedPlayer2BlockNumberEndOfRound, "enemy player was affected by cyber truck the same round it got tweeted")
  }

  test("Given player 1 who uses tweet to place cyber truck in player 1 path in current round then player 1 is not affected because cyber truck only appears next round") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()

    val testPlayer1Id = testCarGamePlayer1.getGamePlayerId()
    testCarGamePlayer1.speed = Config.SPEED_STATE_3
    val newLaneMidRacePlayer1 = 1
    val newBlockNumberMidRacePlayer1 = 760
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testPlayer1Id, newLaneMidRacePlayer1, newBlockNumberMidRacePlayer1)

    val targetLane = 1
    val targetBlock = 765
    val tweetCommand = makeTweetCommand(targetLane, targetBlock)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)

    val expectedPlayer1LaneEndOfRound = 1
    val expectedPlayer1BlockNumberEndOfRound = 760 + Config.SPEED_STATE_3
    val actualPlayer1Position = carGameMap.getPlayerBlockPosition(testPlayer1Id)

    assert(actualPlayer1Position.getLane() == expectedPlayer1LaneEndOfRound && actualPlayer1Position.getBlockNumber() == expectedPlayer1BlockNumberEndOfRound, "player who tweeted was affected by cyber truck in same round it got tweeted")
  }

  test("Given player during race when cyber truck in path then player ends round behind cyber truck") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_3
    val newLaneMidRacePlayer2 = 1
    val newBlockNumberMidRacePlayer2 = 750
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

    val cyberTruckLane = 1
    val cyberTruckBlock = 764
    val tweetCommand = makeTweetCommand(cyberTruckLane, cyberTruckBlock)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    val expectedPlayer2LaneEndOfRound = cyberTruckLane
    val expectedPlayer2BlockNumberEndOfRound = cyberTruckBlock - 1
    val actualPlayer2Position = carGameMap.getPlayerBlockPosition(testGamePlayer2Id)

    assert(actualPlayer2Position.getLane() == expectedPlayer2LaneEndOfRound && actualPlayer2Position.getBlockNumber() == expectedPlayer2BlockNumberEndOfRound, "player who hit cyber truck did not get stuck behind cyber truck")
  }

  test("Given player during race when cyber truck in path then player ends round with speed 3") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_3
    val newLaneMidRacePlayer2 = 1
    val newBlockNumberMidRacePlayer2 = 750
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

    val cyberTruckLane = 1
    val cyberTruckBlock = 764
    val tweetCommand = makeTweetCommand(cyberTruckLane, cyberTruckBlock)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    val expectedSpeed = Config.SPEED_STATE_1 //should be 3
    assert(testCarGamePlayer2.speed == expectedSpeed, "player hit by cyber truck does not have speed reduced to 3")
  }

  test("Given player during race when cyber truck in path then player ends round in HIT_CYBER_TRUCK state") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_3
    val newLaneMidRacePlayer2 = 1
    val newBlockNumberMidRacePlayer2 = 750
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

    val cyberTruckLane = 1
    val cyberTruckBlock = 764
    val tweetCommand = makeTweetCommand(cyberTruckLane, cyberTruckBlock)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    assert(testCarGamePlayer2.getState() == Config.HIT_CYBER_TRUCK_PLAYER_STATE)
  }

  test("Given player during race when cyber truck in path then player incurs hit cybertruck score penalty") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val initialScore = testCarGamePlayer2.getScore()
    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_3
    val newLaneMidRacePlayer2 = 1
    val newBlockNumberMidRacePlayer2 = 750
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

    val cyberTruckLane = 1
    val cyberTruckBlock = 764
    val tweetCommand = makeTweetCommand(cyberTruckLane, cyberTruckBlock)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    val expectedScore = initialScore + Config.HIT_CYBERTRUCK_SCORE_PENALTY
    val actualScore = testCarGamePlayer2.getScore()
    assert(actualScore == expectedScore)
  }

  test("Given player during race when cyber truck in path then cyber truck disappears end of round") {
      initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_3
    val newLaneMidRacePlayer2 = 1
    val newBlockNumberMidRacePlayer2 = 750
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

    val cyberTruckLane = 1
    val cyberTruckBlock = 764
    val tweetCommand = makeTweetCommand(cyberTruckLane, cyberTruckBlock)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)
    TestHelper.processRound(gameMap, nothingCommand, nothingCommand)

    val oldCyberTruckBlock = carGameMap.getBlocks().find(x => x.getPosition().getLane() == cyberTruckLane && x.getPosition().getBlockNumber() == cyberTruckBlock).get
    assert(!oldCyberTruckBlock.isOccupiedByCyberTruck(), "cyber truck still exists on map after being hit")
  }

  test("Given player during race with existing cyber truck when player tweets then there is still only one cyber truck from player in next round") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()
    testCarGamePlayer1.pickupTweet()

    val targetLaneForTweet = 1
    val targetBlockForTweet = 765
    val tweetCommand = makeTweetCommand(targetLaneForTweet, targetBlockForTweet)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)

    val targetLaneForRetweet = 2
    val targetBlockForRetweet = 860
    val tweetCommandForRetweet = makeTweetCommand(targetLaneForRetweet, targetBlockForRetweet)
    TestHelper.processRound(gameMap, tweetCommandForRetweet, nothingCommand)

    val expectedNumberOfCyberTrucks = 1
    val actualNumberOfCyberTrucks = carGameMap.getBlocks().count(x => x.isOccupiedByCyberTruck())

    assert(actualNumberOfCyberTrucks == expectedNumberOfCyberTrucks, "Player tweeting more than once does not result in one cyber truck")
  }

  test("Given player during race with existing cyber truck when player tweets then cyber truck is moved to new position next round") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()
    testCarGamePlayer1.pickupTweet()

    val targetLaneForTweet = 1
    val targetBlockForTweet = 765
    val tweetCommand = makeTweetCommand(targetLaneForTweet, targetBlockForTweet)
    TestHelper.processRound(gameMap, tweetCommand, nothingCommand)

    val targetLaneForRetweet = 2
    val targetBlockForRetweet = 860
    val tweetCommandForRetweet = makeTweetCommand(targetLaneForRetweet, targetBlockForRetweet)
    TestHelper.processRound(gameMap, tweetCommandForRetweet, nothingCommand)

    val oldCyberTruckBlock = carGameMap.getBlocks().find(x => x.getPosition().getLane() == targetLaneForTweet && x.getPosition().getBlockNumber() == targetBlockForTweet).get
    assert(!oldCyberTruckBlock.isOccupiedByCyberTruck(), "cyber truck did not move from old spot")

    val newCyberTruckBlock = carGameMap.getBlocks().find(x => x.getPosition().getLane() == targetLaneForRetweet && x.getPosition().getBlockNumber() == targetBlockForRetweet).get
    assert(newCyberTruckBlock.isOccupiedByCyberTruck(), "cyber truck did not move to new spot")
  }

  test("Given a race when both players have tweeted at different locations then there are two cyber trucks on the map") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    testCarGamePlayer2.pickupTweet()

    val targetLaneForPlayer1 = 1
    val targetBlockForPlayer1 = 765
    val tweetCommandForPlayer1 = makeTweetCommand(targetLaneForPlayer1, targetBlockForPlayer1)

    val targetLaneForPlayer2 = 2
    val targetBlockForPlayer2 = 860
    val tweetCommandForPlayer2 = makeTweetCommand(targetLaneForPlayer2, targetBlockForPlayer2)

    TestHelper.processRound(gameMap, tweetCommandForPlayer1, tweetCommandForPlayer2)

    val expectedNumberOfCyberTrucks = 2
    val actualNumberOfCyberTrucks = carGameMap.getBlocks().count(x => x.isOccupiedByCyberTruck())

    assert(actualNumberOfCyberTrucks == expectedNumberOfCyberTrucks, "both players tweeted but there are not 2 cyber trucks on map")
  }

  test("Given a race when both players have tweeted at different locations then there is one cyber truck at each location on the map") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    testCarGamePlayer2.pickupTweet()

    val targetLaneForPlayer1 = 1
    val targetBlockForPlayer1 = 765
    val tweetCommandForPlayer1 = makeTweetCommand(targetLaneForPlayer1, targetBlockForPlayer1)

    val targetLaneForPlayer2 = 2
    val targetBlockForPlayer2 = 860
    val tweetCommandForPlayer2 = makeTweetCommand(targetLaneForPlayer2, targetBlockForPlayer2)

    TestHelper.processRound(gameMap, tweetCommandForPlayer1, tweetCommandForPlayer2)

    val player1CyberTruckBlock = carGameMap.getBlocks().find(x => x.getPosition().getLane() == targetLaneForPlayer1 && x.getPosition().getBlockNumber() == targetBlockForPlayer1).get
    assert(player1CyberTruckBlock.isOccupiedByCyberTruck(), "cyber truck tweeted by player 1 not on map")

    val player2CyberTruckBlock = carGameMap.getBlocks().find(x => x.getPosition().getLane() == targetLaneForPlayer2 && x.getPosition().getBlockNumber() == targetBlockForPlayer2).get
    assert(player2CyberTruckBlock.isOccupiedByCyberTruck(), "cyber truck tweeted by player 2 not on map")
  }

  test("Given a race when both players have tweeted at the same location on the map then there are no cyber trucks on map") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    testCarGamePlayer2.pickupTweet()

    val targetLaneForPlayer1 = 1
    val targetBlockForPlayer1 = 765
    val tweetCommandForPlayer1 = makeTweetCommand(targetLaneForPlayer1, targetBlockForPlayer1)

    val targetLaneForPlayer2 = 1
    val targetBlockForPlayer2 = 765
    val tweetCommandForPlayer2 = makeTweetCommand(targetLaneForPlayer2, targetBlockForPlayer2)

    TestHelper.processRound(gameMap, tweetCommandForPlayer1, tweetCommandForPlayer2)

    val expectedNumberOfCyberTrucks = 0
    val actualNumberOfCyberTrucks = carGameMap.getBlocks().count(x => x.isOccupiedByCyberTruck())

    assert(actualNumberOfCyberTrucks == expectedNumberOfCyberTrucks, "both players tweeted at same location but there is a cyber truck there")
  }

  test("Given a race when both players have tweeted at the same location on the map then there is no cyber truck at that location on the map") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    testCarGamePlayer2.pickupTweet()

    val targetLaneForPlayer1 = 1
    val targetBlockForPlayer1 = 765
    val tweetCommandForPlayer1 = makeTweetCommand(targetLaneForPlayer1, targetBlockForPlayer1)

    val targetLaneForPlayer2 = 1
    val targetBlockForPlayer2 = 765
    val tweetCommandForPlayer2 = makeTweetCommand(targetLaneForPlayer2, targetBlockForPlayer2)

    TestHelper.processRound(gameMap, tweetCommandForPlayer1, tweetCommandForPlayer2)

    val blockOfInterest = carGameMap.getBlockMatchingPosition(new BlockPosition(1, 765))

    assert(!blockOfInterest.isOccupiedByCyberTruck(), "both players tweeted at same location but there is a cyber truck there")
  }

  test("Given a race when both players have tweeted at the same location on the map then both players are refunded tweet usage") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    testCarGamePlayer2.pickupTweet()

    val targetLaneForPlayer1 = 1
    val targetBlockForPlayer1 = 765
    val tweetCommandForPlayer1 = makeTweetCommand(targetLaneForPlayer1, targetBlockForPlayer1)

    val targetLaneForPlayer2 = 1
    val targetBlockForPlayer2 = 765
    val tweetCommandForPlayer2 = makeTweetCommand(targetLaneForPlayer2, targetBlockForPlayer2)

    TestHelper.processRound(gameMap, tweetCommandForPlayer1, tweetCommandForPlayer2)

    val player1TweetCount = testCarGamePlayer1.getPowerups().count(x => x.equals(Config.TWEET_POWERUP_ITEM))
    val player2TweetCount = testCarGamePlayer2.getPowerups().count(x => x.equals(Config.TWEET_POWERUP_ITEM))

    assert(player1TweetCount == 1, "both players tweeted at same location but player 1 did not get a refund")
    assert(player2TweetCount == 1, "both players tweeted at same location but player 2 did not get a refund")
  }

  test("Given a race where player 1 has an existing cyber truck when both players have tweeted at the same location on the map then player 1 cyber truck remains in old place") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()
    val firstTweet = makeTweetCommand(1, 550)
    TestHelper.processRound(gameMap, firstTweet, nothingCommand)

    testCarGamePlayer1.pickupTweet()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    testCarGamePlayer2.pickupTweet()

    val targetLaneForPlayer1 = 1
    val targetBlockForPlayer1 = 765
    val tweetCommandForPlayer1 = makeTweetCommand(targetLaneForPlayer1, targetBlockForPlayer1)

    val targetLaneForPlayer2 = 1
    val targetBlockForPlayer2 = 765
    val tweetCommandForPlayer2 = makeTweetCommand(targetLaneForPlayer2, targetBlockForPlayer2)

    TestHelper.processRound(gameMap, tweetCommandForPlayer1, tweetCommandForPlayer2)

    val blockWithOriginalCyberTruck = carGameMap.getBlockMatchingPosition(new BlockPosition(1, 550))
    assert(blockWithOriginalCyberTruck.isOccupiedByCyberTruck(), "both players tweeted same positions but player 1 old cyber truck went missing")
  }

  test("Given a player with no existing cyber truck when player tweets cyber truck at location where there already is one then only one cyber truck exists on map")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()
    val firstTweet = makeTweetCommand(1, 550)
    TestHelper.processRound(gameMap, firstTweet, nothingCommand)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    testCarGamePlayer2.pickupTweet()

    val targetLaneForPlayer2 = 1
    val targetBlockForPlayer2 = 550
    val tweetCommandForPlayer2 = makeTweetCommand(targetLaneForPlayer2, targetBlockForPlayer2)

    TestHelper.processRound(gameMap, nothingCommand, tweetCommandForPlayer2)

    val numberOfCyberTrucksOnMap = carGameMap.getBlocks().count(x => x.isOccupiedByCyberTruck())
    assert(numberOfCyberTrucksOnMap == 1)
  }

  test("Given a player with an existing cyber truck when player tweets cyber truck at a location where there already is one then 2 cyber trucks exist on map")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()
    val firstTweet = makeTweetCommand(1, 550)
    TestHelper.processRound(gameMap, firstTweet, nothingCommand)

    testCarGamePlayer1.pickupTweet()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    testCarGamePlayer2.pickupTweet()

    val targetLaneForPlayer1 = 1
    val targetBlockForPlayer1 = 765
    val tweetCommandForPlayer1 = makeTweetCommand(targetLaneForPlayer1, targetBlockForPlayer1)

    val targetLaneForPlayer2 = 1
    val targetBlockForPlayer2 = 765
    val tweetCommandForPlayer2 = makeTweetCommand(targetLaneForPlayer2, targetBlockForPlayer2)

    TestHelper.processRound(gameMap, nothingCommand, tweetCommandForPlayer2)
    TestHelper.processRound(gameMap, tweetCommandForPlayer1, nothingCommand)

    val numberOfCyberTrucksOnMap = carGameMap.getBlocks().count(x => x.isOccupiedByCyberTruck())
    assert(numberOfCyberTrucksOnMap == 2)
  }

  test("Given a player with an existing cyber truck when player tweets cyber truck at a location where there already is one then players cyber truck remains in old location")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()
    val firstTweet = makeTweetCommand(1, 550)
    TestHelper.processRound(gameMap, firstTweet, nothingCommand)

    testCarGamePlayer1.pickupTweet()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    testCarGamePlayer2.pickupTweet()

    val targetLaneForPlayer1 = 1
    val targetBlockForPlayer1 = 765
    val tweetCommandForPlayer1 = makeTweetCommand(targetLaneForPlayer1, targetBlockForPlayer1)

    val targetLaneForPlayer2 = 1
    val targetBlockForPlayer2 = 765
    val tweetCommandForPlayer2 = makeTweetCommand(targetLaneForPlayer2, targetBlockForPlayer2)

    TestHelper.processRound(gameMap, nothingCommand, tweetCommandForPlayer2)
    TestHelper.processRound(gameMap, tweetCommandForPlayer1, nothingCommand)

    val blockWithOriginalCyberTruck = carGameMap.getBlockMatchingPosition(new BlockPosition(1, 550))
    assert(blockWithOriginalCyberTruck.isOccupiedByCyberTruck())
  }

  test("Given a race when player tweets cyber truck at location where there already is one then player is refunded tweet usage")
  {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    testCarGamePlayer1.pickupTweet()
    val firstTweet = makeTweetCommand(1, 550)
    TestHelper.processRound(gameMap, firstTweet, nothingCommand)

    testCarGamePlayer1.pickupTweet()

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    testCarGamePlayer2.pickupTweet()

    val targetLaneForPlayer1 = 1
    val targetBlockForPlayer1 = 765
    val tweetCommandForPlayer1 = makeTweetCommand(targetLaneForPlayer1, targetBlockForPlayer1)

    val targetLaneForPlayer2 = 1
    val targetBlockForPlayer2 = 765
    val tweetCommandForPlayer2 = makeTweetCommand(targetLaneForPlayer2, targetBlockForPlayer2)

    TestHelper.processRound(gameMap, nothingCommand, tweetCommandForPlayer2)
    TestHelper.processRound(gameMap, tweetCommandForPlayer1, nothingCommand)

    val player1TweetCount = testCarGamePlayer1.getPowerups().count(x => x.equals(Config.TWEET_POWERUP_ITEM))
    assert(player1TweetCount == 1)
  }

  test("Given a race when both players are in the same lane behind each other and hit a cybertruck") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 3, 36)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 3, 40)

    testCarGamePlayer1.speed = 8
    testCarGamePlayer2.speed = 8

    val blockForCyberTruck = carGameMap.getBlockMatchingPosition(new BlockPosition(3, 43))
    blockForCyberTruck.addCyberTruck();

    assert(blockForCyberTruck.isOccupiedByCyberTruck())

    TestHelper.processRound(carGameMap, accelerateCommand, accelerateCommand)

    assert(carGameMap.getPlayerBlockPosition(testCarGamePlayer1.getGamePlayerId()).getBlockNumber() == 41)
    assert(carGameMap.getPlayerBlockPosition(testCarGamePlayer2.getGamePlayerId()).getBlockNumber() == 42)
  }

  test("Given a race when both players are in different lanes and turn into in the same lane behind each other and hit a cybertruck") {
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer1.getGamePlayerId(), 2, 700)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2()
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testCarGamePlayer2.getGamePlayerId(), 1, 702)

    val blockForCyberTruck = carGameMap.getBlockMatchingPosition(new BlockPosition(2, 706))
    blockForCyberTruck.addCyberTruck();

    assert(blockForCyberTruck.isOccupiedByCyberTruck())

    TestHelper.processRound(carGameMap, nothingCommand, turnRightCommand)

    assert(carGameMap.getPlayerBlockPosition(testCarGamePlayer1.getGamePlayerId()).getBlockNumber() == 704)
    assert(carGameMap.getPlayerBlockPosition(testCarGamePlayer1.getGamePlayerId()).getLane() == 2)
    assert(carGameMap.getPlayerBlockPosition(testCarGamePlayer2.getGamePlayerId()).getBlockNumber() == 704)
    assert(carGameMap.getPlayerBlockPosition(testCarGamePlayer2.getGamePlayerId()).getLane() == 1)
  }
}
