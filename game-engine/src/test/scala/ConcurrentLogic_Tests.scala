import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.{CarGamePlayer, CarGameRoundProcessor, GamePlayer}
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import scala.collection.JavaConverters._
import java.util

class ConcurrentLogic_Tests extends FunSuite{
  private var carGameRoundProcessor: CarGameRoundProcessor = null

  private var nothingCommandText: String = null;
  private var turnLeftCommandText: String = null;
  private var turnRightCommandText: String = null;

  private var commandFactory: CommandFactory = null

  private var nothingCommand: RawCommand = null
  private var turnLeftCommand: RawCommand = null
  private var turnRightCommand: RawCommand = null


  def initialise() = {
    Config.loadDefault();

    carGameRoundProcessor = new CarGameRoundProcessor

    nothingCommandText = Config.NOTHING_COMMAND
    turnLeftCommandText = Config.TURN_LEFT_COMMAND
    turnRightCommandText = Config.TURN_RIGHT_COMMAND

    commandFactory = new CommandFactory
    nothingCommand = commandFactory.makeCommand(nothingCommandText)
    nothingCommand.setCommand(nothingCommandText)
    turnLeftCommand = commandFactory.makeCommand(turnLeftCommandText)
    turnLeftCommand.setCommand(turnLeftCommandText)
    turnRightCommand = commandFactory.makeCommand(turnRightCommandText)
    turnRightCommand.setCommand(turnRightCommandText)
  }

  test("Given players during a race when player 1 turns into player 2 from the left then player 1 position is correctly updated to resolve the collision")
  {
    //Arrange: setup players for a collision
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    testCarGamePlayer1.speed = Config.SPEED_STATE_2 //should be 6
    val newLaneMidRacePlayer1 = 2
    val newBlockNumberMidRacePlayer1 = 35
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRacePlayer1, newBlockNumberMidRacePlayer1)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2();
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_3 //should be 8
    val newLaneMidRacePlayer2 = 3
    val newBlockNumberMidRacePlayer2 = 32
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

    //Act: process commands that would cause a collision
    var commandsToProcess = collection.mutable.Map[GamePlayer, util.List[RawCommand]]()

    var player1Commands = List[RawCommand]()
    player1Commands = player1Commands.appended(turnRightCommand)
    commandsToProcess.addOne(testGamePlayer1, player1Commands.asJava)

    var player2Commands = List[RawCommand]()
    player2Commands = player2Commands.appended(nothingCommand)
    commandsToProcess.addOne(testGamePlayer2, player2Commands.asJava)

    val javaCommandsToProcess = commandsToProcess.asJava;

    carGameRoundProcessor.processRound(gameMap, javaCommandsToProcess)

    //Assert: player positions have been corrected to resolve the collision
    val expectedPlayer1Lane = 2
    val expectedPlayer1BlockNumber = 39
    val actualPlayer1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert((actualPlayer1Position.getLane() == expectedPlayer1Lane) && (actualPlayer1Position.getBlockNumber() == expectedPlayer1BlockNumber), "Collision initiator (from left) final position not correct")
  }

  test("Given players during a race when player 1 turns into player 2 from the left then player 2 position is correctly updated to resolve the collision")
  {
    //Arrange: setup players for a collision
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    testCarGamePlayer1.speed = Config.SPEED_STATE_2 //should be 6
    val newLaneMidRacePlayer1 = 2
    val newBlockNumberMidRacePlayer1 = 35
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRacePlayer1, newBlockNumberMidRacePlayer1)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2();
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_3 //should be 8
    val newLaneMidRacePlayer2 = 3
    val newBlockNumberMidRacePlayer2 = 32
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

    //Act: process commands that would cause a collision
    var commandsToProcess = collection.mutable.Map[GamePlayer, util.List[RawCommand]]()

    var player1Commands = List[RawCommand]()
    player1Commands = player1Commands.appended(turnRightCommand)
    commandsToProcess.addOne(testGamePlayer1, player1Commands.asJava)

    var player2Commands = List[RawCommand]()
    player2Commands = player2Commands.appended(nothingCommand)
    commandsToProcess.addOne(testGamePlayer2, player2Commands.asJava)

    val javaCommandsToProcess = commandsToProcess.asJava;

    carGameRoundProcessor.processRound(gameMap, javaCommandsToProcess)

    val expectedPlayer2Lane = 3
    val expectedPlayer2BlockNumber = 39
    val actualPlayer2Position = carGameMap.getPlayerBlockPosition(testGamePlayer2Id);
    assert((actualPlayer2Position.getLane() == expectedPlayer2Lane) && (actualPlayer2Position.getBlockNumber() == expectedPlayer2BlockNumber), "Collision victim (from left) final position not correct")
  }

  test("Given players during a race when player 1 turns into player 2 from the right then player 1 position is updated to resolve the collision")
  {
    //Arrange: setup players for a collision
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    testCarGamePlayer1.speed = Config.SPEED_STATE_2 //should be 6
    val newLaneMidRacePlayer1 = 4
    val newBlockNumberMidRacePlayer1 = 35
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRacePlayer1, newBlockNumberMidRacePlayer1)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2();
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_3 //should be 8
    val newLaneMidRacePlayer2 = 3
    val newBlockNumberMidRacePlayer2 = 32
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

    //Act: process commands that would cause a collision
    var commandsToProcess = collection.mutable.Map[GamePlayer, util.List[RawCommand]]()

    var player1Commands = List[RawCommand]()
    player1Commands = player1Commands.appended(turnLeftCommand)
    commandsToProcess.addOne(testGamePlayer1, player1Commands.asJava)

    var player2Commands = List[RawCommand]()
    player2Commands = player2Commands.appended(nothingCommand)
    commandsToProcess.addOne(testGamePlayer2, player2Commands.asJava)

    val javaCommandsToProcess = commandsToProcess.asJava;

    carGameRoundProcessor.processRound(gameMap, javaCommandsToProcess)

    //Assert: player positions have been corrected to resolve the collision
    val expectedPlayer1Lane = 4
    val expectedPlayer1BlockNumber = 39
    val actualPlayer1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert((actualPlayer1Position.getLane() == expectedPlayer1Lane) && (actualPlayer1Position.getBlockNumber() == expectedPlayer1BlockNumber), "Collision initiator (from left) final position not correct")
  }

  test("Given players during a race when player 1 turns into player 2 from the right then player 2 position is updated to resolve the collision")
  {
    //Arrange: setup players for a collision
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    testCarGamePlayer1.speed = Config.SPEED_STATE_2 //should be 6
    val newLaneMidRacePlayer1 = 4
    val newBlockNumberMidRacePlayer1 = 35
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRacePlayer1, newBlockNumberMidRacePlayer1)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2();
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_3 //should be 8
    val newLaneMidRacePlayer2 = 3
    val newBlockNumberMidRacePlayer2 = 32
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

    //Act: process commands that would cause a collision
    var commandsToProcess = collection.mutable.Map[GamePlayer, util.List[RawCommand]]()

    var player1Commands = List[RawCommand]()
    player1Commands = player1Commands.appended(turnLeftCommand)
    commandsToProcess.addOne(testGamePlayer1, player1Commands.asJava)

    var player2Commands = List[RawCommand]()
    player2Commands = player2Commands.appended(nothingCommand)
    commandsToProcess.addOne(testGamePlayer2, player2Commands.asJava)

    val javaCommandsToProcess = commandsToProcess.asJava;

    carGameRoundProcessor.processRound(gameMap, javaCommandsToProcess)

    val expectedPlayer2Lane = 3
    val expectedPlayer2BlockNumber = 39
    val actualPlayer2Position = carGameMap.getPlayerBlockPosition(testGamePlayer2Id);
    assert((actualPlayer2Position.getLane() == expectedPlayer2Lane) && (actualPlayer2Position.getBlockNumber() == expectedPlayer2BlockNumber), "Collision victim (from left) final position not correct")
  }

  test("Given players during a race when player 1 and player 2 turn into each other then player 1 position is updated to resolve the collision")
  {
    //Arrange: setup players for a collision
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    testCarGamePlayer1.speed = Config.SPEED_STATE_2 //should be 6
    val newLaneMidRacePlayer1 = 2
    val newBlockNumberMidRacePlayer1 = 35
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRacePlayer1, newBlockNumberMidRacePlayer1)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2();
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_2 //should be 6
    val newLaneMidRacePlayer2 = 4
    val newBlockNumberMidRacePlayer2 = 35
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

    //Act: process commands that would cause a collision
    var commandsToProcess = collection.mutable.Map[GamePlayer, util.List[RawCommand]]()

    var player1Commands = List[RawCommand]()
    player1Commands = player1Commands.appended(turnRightCommand)
    commandsToProcess.addOne(testGamePlayer1, player1Commands.asJava)

    var player2Commands = List[RawCommand]()
    player2Commands = player2Commands.appended(turnLeftCommand)
    commandsToProcess.addOne(testGamePlayer2, player2Commands.asJava)

    val javaCommandsToProcess = commandsToProcess.asJava;

    carGameRoundProcessor.processRound(gameMap, javaCommandsToProcess)

    //Assert: player positions have been corrected to resolve the collision
    val expectedPlayer1Lane = 2
    val expectedPlayer1BlockNumber = 39
    val actualPlayer1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert((actualPlayer1Position.getLane() == expectedPlayer1Lane) && (actualPlayer1Position.getBlockNumber() == expectedPlayer1BlockNumber), "Collision initiator (from left) final position not correct")
  }

  test("Given players during a race when player 1 and player 2 turn into each other then player 2 position is updated to resolve the collision")
  {
    //Arrange: setup players for a collision
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    testCarGamePlayer1.speed = Config.SPEED_STATE_2 //should be 6
    val newLaneMidRacePlayer1 = 2
    val newBlockNumberMidRacePlayer1 = 35
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRacePlayer1, newBlockNumberMidRacePlayer1)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2();
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_2 //should be 6
    val newLaneMidRacePlayer2 = 4
    val newBlockNumberMidRacePlayer2 = 35
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

    //Act: process commands that would cause a collision
    var commandsToProcess = collection.mutable.Map[GamePlayer, util.List[RawCommand]]()

    var player1Commands = List[RawCommand]()
    player1Commands = player1Commands.appended(turnRightCommand)
    commandsToProcess.addOne(testGamePlayer1, player1Commands.asJava)

    var player2Commands = List[RawCommand]()
    player2Commands = player2Commands.appended(turnLeftCommand)
    commandsToProcess.addOne(testGamePlayer2, player2Commands.asJava)

    val javaCommandsToProcess = commandsToProcess.asJava;

    carGameRoundProcessor.processRound(gameMap, javaCommandsToProcess)

    //Assert: player positions have been corrected to resolve the collision
    val expectedPlayer1Lane = 4
    val expectedPlayer1BlockNumber = 39
    val actualPlayer2Position = carGameMap.getPlayerBlockPosition(testGamePlayer2Id)
    assert((actualPlayer2Position.getLane() == expectedPlayer1Lane) && (actualPlayer2Position.getBlockNumber() == expectedPlayer1BlockNumber), "Collision initiator (from left) final position not correct")
  }

  test("Given players during a race when player 1 drives into player 2 from behind then player 1 does not drive through player 2 but instead remains behind player 2")
  {
    //Arrange: setup players for a collision
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    testCarGamePlayer1.speed = Config.SPEED_STATE_2 //should be 6
    val newLaneMidRacePlayer1 = 3
    val newBlockNumberMidRacePlayer1 = 38
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRacePlayer1, newBlockNumberMidRacePlayer1)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2();
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_1 //should be 3
    val newLaneMidRacePlayer2 = 3
    val newBlockNumberMidRacePlayer2 = 40
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

    //Act: process commands that would cause a collision
    var commandsToProcess = collection.mutable.Map[GamePlayer, util.List[RawCommand]]()

    var player1Commands = List[RawCommand]()
    player1Commands = player1Commands.appended(nothingCommand)
    commandsToProcess.addOne(testGamePlayer1, player1Commands.asJava)

    var player2Commands = List[RawCommand]()
    player2Commands = player2Commands.appended(nothingCommand)
    commandsToProcess.addOne(testGamePlayer2, player2Commands.asJava)

    val javaCommandsToProcess = commandsToProcess.asJava;

    carGameRoundProcessor.processRound(gameMap, javaCommandsToProcess)

    //Assert: player positions have been corrected to resolve the collision
    val expectedPlayer1Lane = 3
    val expectedPlayer1BlockNumber = 42
    val actualPlayer1Position = carGameMap.getPlayerBlockPosition(testGamePlayer1Id)
    assert((actualPlayer1Position.getLane() == expectedPlayer1Lane) && (actualPlayer1Position.getBlockNumber() == expectedPlayer1BlockNumber), "Collision initiator (from left) final position not correct")
  }

  test ("Given a race when stages positions are committed then the history of staged positions is cleared so as not to influence future rounds")
  {
    //Arrange: setup players for a collision
    initialise()
    val gameMap = TestHelper.initialiseGameWithNoMapObjects()
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    testCarGamePlayer1.speed = Config.SPEED_STATE_2 //should be 6
    val newLaneMidRacePlayer1 = 3
    val newBlockNumberMidRacePlayer1 = 38
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRacePlayer1, newBlockNumberMidRacePlayer1)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2();
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_1 //should be 3
    val newLaneMidRacePlayer2 = 3
    val newBlockNumberMidRacePlayer2 = 40
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

    //Act: process commands that would cause a collision
    var commandsToProcess = collection.mutable.Map[GamePlayer, util.List[RawCommand]]()

    var player1Commands = List[RawCommand]()
    player1Commands = player1Commands.appended(nothingCommand)
    commandsToProcess.addOne(testGamePlayer1, player1Commands.asJava)

    var player2Commands = List[RawCommand]()
    player2Commands = player2Commands.appended(nothingCommand)
    commandsToProcess.addOne(testGamePlayer2, player2Commands.asJava)

    val javaCommandsToProcess = commandsToProcess.asJava;

    carGameRoundProcessor.processRound(gameMap, javaCommandsToProcess)

    //Assert: staged future positions have been reset to empty
    assert(carGameMap.stagedFuturePositions.length == 0, "Stages positions are not being cleared after being committed")
  }

  test("Given a race when a collision occurs then player interactions should be considered with collision corrected path")
  {
    //Arrange: setup players for a collision
    initialise()
    val gameMap = TestHelper.initialiseGameWithMapObjectAt(2, 38, Config.MUD_MAP_OBJECT)
    val carGameMap = gameMap.asInstanceOf[CarGameMap]

    val testGamePlayer1 = TestHelper.getTestGamePlayer1()
    val testCarGamePlayer1 = testGamePlayer1.asInstanceOf[CarGamePlayer]
    val testGamePlayer1Id = testCarGamePlayer1.getGamePlayerId()
    testCarGamePlayer1.speed = Config.SPEED_STATE_2 //should be 6
    val newLaneMidRacePlayer1 = 2
    val newBlockNumberMidRacePlayer1 = 35
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer1Id, newLaneMidRacePlayer1, newBlockNumberMidRacePlayer1)

    val testGamePlayer2 = TestHelper.getTestGamePlayer2();
    val testCarGamePlayer2 = testGamePlayer2.asInstanceOf[CarGamePlayer]
    val testGamePlayer2Id = testCarGamePlayer2.getGamePlayerId()
    testCarGamePlayer2.speed = Config.SPEED_STATE_2 //should be 6
    val newLaneMidRacePlayer2 = 4
    val newBlockNumberMidRacePlayer2 = 35
    TestHelper.putPlayerSomewhereOnTheTrack(carGameMap, testGamePlayer2Id, newLaneMidRacePlayer2, newBlockNumberMidRacePlayer2)

    //Act: process commands that would cause a collision
    var commandsToProcess = collection.mutable.Map[GamePlayer, util.List[RawCommand]]()

    var player1Commands = List[RawCommand]()
    player1Commands = player1Commands.appended(turnRightCommand)
    commandsToProcess.addOne(testGamePlayer1, player1Commands.asJava)

    var player2Commands = List[RawCommand]()
    player2Commands = player2Commands.appended(turnLeftCommand)
    commandsToProcess.addOne(testGamePlayer2, player2Commands.asJava)

    val javaCommandsToProcess = commandsToProcess.asJava;

    carGameRoundProcessor.processRound(gameMap, javaCommandsToProcess)

    //Assert: player positions have been corrected to resolve the collision
    assert(testCarGamePlayer1.speed == Config.SPEED_STATE_1, "Player did not interact with map object on corrected path")
  }
}
