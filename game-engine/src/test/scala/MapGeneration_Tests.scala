import org.scalatest.FunSuite
import test.TestHelper
import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.map.CarGameMap

class MapGeneration_Tests extends FunSuite {

  private val numberOfIterations = 10000
  private val randomNumberGeneratorSeed = 50
  private val acceptableDeviationFromStatedGenerationPercentage = 0.01

  def initialise() = {
    Config.loadDefault()
  }

  test("Given any seed when generating the map mud generation percentage is respected")
  {
      initialise()

      val totalNumberOfBlocks = Config.MAX_LANE * Config.TRACK_LENGTH;
      val maxAllowedNumberOfMudBlocks = totalNumberOfBlocks * (Config.MUD_GENERATION_PERCENTAGE/100.0);
      var actualNumberOfMudBlocksGenerated = 0

      val randomNumberGenerator = new scala.util.Random(randomNumberGeneratorSeed)
      for(i <- 0 to numberOfIterations - 1) {
        val randomSeed = randomNumberGenerator.nextInt()
        var gameMap = TestHelper.initialiseMapWithSeed(randomSeed)
        actualNumberOfMudBlocksGenerated += gameMap.asInstanceOf[CarGameMap].blocks.count(x => x.mapObject == Config.MUD_MAP_OBJECT);
      }

      val averageNumberOfMudBlocksGenerated = actualNumberOfMudBlocksGenerated/(numberOfIterations + 0.0)
      val deviationInTermsOfNumberOfObjects = acceptableDeviationFromStatedGenerationPercentage * maxAllowedNumberOfMudBlocks
      assert(Math.abs(maxAllowedNumberOfMudBlocks - averageNumberOfMudBlocksGenerated) <= deviationInTermsOfNumberOfObjects, "Mud blocks not generated not within bounds");
  }

  test("Given any seed when generating the map wall generation percentage is respected")
  {
      initialise()

      val totalNumberOfBlocks = Config.MAX_LANE * Config.TRACK_LENGTH;
      val maxAllowedNumberOfWallBlocks = totalNumberOfBlocks * (Config.WALL_GENERATION_PERCENTAGE/100.0);
      var actualNumberOfWallBlocksGenerated = 0

      val randomNumberGenerator = new scala.util.Random(randomNumberGeneratorSeed)
      for(i <- 0 to numberOfIterations - 1) {
        val randomSeed = randomNumberGenerator.nextInt()
        var gameMap = TestHelper.initialiseMapWithSeed(randomSeed)
        actualNumberOfWallBlocksGenerated += gameMap.asInstanceOf[CarGameMap].blocks.count(x => x.mapObject == Config.WALL_MAP_OBJECT);
      }

      val averageNumberOfWallBlocksGenerated = actualNumberOfWallBlocksGenerated/(numberOfIterations + 0.0)
      val deviationInTermsOfNumberOfObjects = acceptableDeviationFromStatedGenerationPercentage * maxAllowedNumberOfWallBlocks
      assert(Math.abs(maxAllowedNumberOfWallBlocks - averageNumberOfWallBlocksGenerated) <= deviationInTermsOfNumberOfObjects, "Wall blocks not generated not within bounds");
  }

  test("Given any seed when generating the map boost generation percentage is respected")
  {
      initialise()

      val totalNumberOfBlocks = Config.MAX_LANE * Config.TRACK_LENGTH;
      val maxAllowedNumberOfBoostPickupBlocks = totalNumberOfBlocks * (Config.BOOST_GENERATION_PERCENTAGE/100.0);
      var actualNumberOfBoostPickupBlocksGenerated = 0

      val randomNumberGenerator = new scala.util.Random(randomNumberGeneratorSeed)
      for(i <- 0 to numberOfIterations - 1) {
        val randomSeed = randomNumberGenerator.nextInt()
        var gameMap = TestHelper.initialiseMapWithSeed(randomSeed)
        actualNumberOfBoostPickupBlocksGenerated += gameMap.asInstanceOf[CarGameMap].blocks.count(x => x.mapObject == Config.BOOST_MAP_OBJECT);
      }

      val averageNumberOfBoostPickupBlocksGenerated = actualNumberOfBoostPickupBlocksGenerated/(numberOfIterations + 0.0)
      val deviationInTermsOfNumberOfObjects = acceptableDeviationFromStatedGenerationPercentage * maxAllowedNumberOfBoostPickupBlocks
      assert(Math.abs(maxAllowedNumberOfBoostPickupBlocks - averageNumberOfBoostPickupBlocksGenerated) <= deviationInTermsOfNumberOfObjects, "Boost pickup blocks not generated not within bounds");
  }

  test("Given any seed when generating the map oil generation percentage is respected")
  {
      initialise()

      val totalNumberOfBlocks = Config.MAX_LANE * Config.TRACK_LENGTH;
      val maxAllowedNumberOfOilPickupBlocks = totalNumberOfBlocks * (Config.OIL_ITEM_GENERATION_PERCENTAGE/100.0);
      var actualNumberOfOilPickupBlocksGenerated = 0

      val randomNumberGenerator = new scala.util.Random(randomNumberGeneratorSeed)
      for(i <- 0 to numberOfIterations - 1) {
        val randomSeed = randomNumberGenerator.nextInt()
        var gameMap = TestHelper.initialiseMapWithSeed(randomSeed)
        actualNumberOfOilPickupBlocksGenerated += gameMap.asInstanceOf[CarGameMap].blocks.count(x => x.mapObject == Config.OIL_ITEM_MAP_OBJECT);
      }

      val averageNumberOfOilPickupBlocksGenerated = actualNumberOfOilPickupBlocksGenerated/(numberOfIterations + 0.0)
      val deviationInTermsOfNumberOfObjects = acceptableDeviationFromStatedGenerationPercentage * maxAllowedNumberOfOilPickupBlocks
      assert(Math.abs(maxAllowedNumberOfOilPickupBlocks - averageNumberOfOilPickupBlocksGenerated) <= deviationInTermsOfNumberOfObjects, "Oil pickup blocks not generated not within bounds");
  }

  test("Given any seed when generating the map tweet generation percentage is respected")
  {
    initialise()

    val totalNumberOfBlocks = Config.MAX_LANE * Config.TRACK_LENGTH;
    val maxAllowedNumberOfTweetPickupBlocks = totalNumberOfBlocks * (Config.TWEET_GENERATION_PERCENTAGE/100.0);
    var actualNumberOfTweetPickupBlocksGenerated = 0

    val randomNumberGenerator = new scala.util.Random(randomNumberGeneratorSeed)
    for(i <- 0 to numberOfIterations - 1) {
      val randomSeed = randomNumberGenerator.nextInt()
      var gameMap = TestHelper.initialiseMapWithSeed(randomSeed)
      actualNumberOfTweetPickupBlocksGenerated += gameMap.asInstanceOf[CarGameMap].blocks.count(x => x.mapObject == Config.TWEET_MAP_OBJECT);
    }

    val averageNumberOfTweetPickupBlocksGenerated = actualNumberOfTweetPickupBlocksGenerated/(numberOfIterations + 0.0)
    val deviationInTermsOfNumberOfObjects = acceptableDeviationFromStatedGenerationPercentage * maxAllowedNumberOfTweetPickupBlocks
    assert(Math.abs(maxAllowedNumberOfTweetPickupBlocks - averageNumberOfTweetPickupBlocksGenerated) <= deviationInTermsOfNumberOfObjects, "Tweet pickup blocks not generated not within bounds");
  }

  test("Given any seed when generating the map lizard generation percentage is respected")
  {
      initialise()

      val totalNumberOfBlocks = Config.MAX_LANE * Config.TRACK_LENGTH;
      val maxAllowedNumberOfLizardPickupBlocks = totalNumberOfBlocks * (Config.LIZARD_GENERATION_PERCENTAGE/100.0);
      var actualNumberOfLizardPickupBlocksGenerated = 0

      val randomNumberGenerator = new scala.util.Random(randomNumberGeneratorSeed)
      for(i <- 0 to numberOfIterations - 1) {
        val randomSeed = randomNumberGenerator.nextInt()
        var gameMap = TestHelper.initialiseMapWithSeed(randomSeed)
        actualNumberOfLizardPickupBlocksGenerated += gameMap.asInstanceOf[CarGameMap].blocks.count(x => x.mapObject == Config.LIZARD_MAP_OBJECT);
      }

      val averageNumberOfLizardPickupBlocksGenerated = actualNumberOfLizardPickupBlocksGenerated/(numberOfIterations + 0.0)
      val deviationInTermsOfNumberOfObjects = acceptableDeviationFromStatedGenerationPercentage * maxAllowedNumberOfLizardPickupBlocks
      assert(Math.abs(maxAllowedNumberOfLizardPickupBlocks - averageNumberOfLizardPickupBlocksGenerated) <= deviationInTermsOfNumberOfObjects, "Lizard pickup blocks not generated not within bounds");
  }

  test("Given any seed when generating the map emp generation percentage is respected")
  {
      initialise()

      val totalNumberOfBlocks = Config.MAX_LANE * Config.TRACK_LENGTH;
      val maxAllowedNumberOfEmpPickupBlocks = totalNumberOfBlocks * (Config.EMP_GENERATION_PERCENTAGE/100.0);
      var actualNumberOfEmpPickupBlocksGenerated = 0

      val randomNumberGenerator = new scala.util.Random(randomNumberGeneratorSeed)
      for(i <- 0 to numberOfIterations - 1) {
        val randomSeed = randomNumberGenerator.nextInt()
        var gameMap = TestHelper.initialiseMapWithSeed(randomSeed)
        actualNumberOfEmpPickupBlocksGenerated += gameMap.asInstanceOf[CarGameMap].blocks.count(x => x.mapObject == Config.EMP_MAP_OBJECT);
      }

      val averageNumberOfEmpPickupBlocksGenerated = actualNumberOfEmpPickupBlocksGenerated/(numberOfIterations + 0.0)
      val deviationInTermsOfNumberOfObjects = acceptableDeviationFromStatedGenerationPercentage * maxAllowedNumberOfEmpPickupBlocks
      assert(Math.abs(maxAllowedNumberOfEmpPickupBlocks - averageNumberOfEmpPickupBlocksGenerated) <= deviationInTermsOfNumberOfObjects, "Lizard pickup blocks not generated not within bounds");
  }

}
