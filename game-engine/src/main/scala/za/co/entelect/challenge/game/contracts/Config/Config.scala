package za.co.entelect.challenge.game.contracts.Config

import scala.io.Source
import net.liftweb.json._
import net.liftweb.json.DefaultFormats

case class GameEngineConfig
(
  NUMBER_OF_LANES: Int,
  TRACK_LENGTH: Int,
  MIN_LANE: Int,
  MAX_LANE: Int,
  MAX_ROUNDS: Int,

  EMPTY_PLAYER: Int,
  DEFAULT_HEALTH: Int,
  DEFAULT_SCORE: Int,

  READY_PLAYER_STATE: String,
  NOTHING_PLAYER_STATE: String,
  TURNING_LEFT_PLAYER_STATE: String,
  TURNING_RIGHT_PLAYER_STATE: String,
  ACCELERATING_PLAYER_STATE: String,
  DECELERATING_PLAYER_STATE: String,
  PICKED_UP_POWERUP_PLAYER_STATE: String,
  USED_POWERUP_BOOST_PLAYER_STATE: String,
  USED_POWERUP_OIL_PLAYER_STATE: String,
  HIT_MUD_PLAYER_STATE: String,
  HIT_OIL_PLAYER_STAE: String,
  FINISHED_PLAYER_STATE: String,

  MINIMUM_SPEED: Int,
  SPEED_STATE_1: Int,
  INITIAL_SPEED: Int,
  SPEED_STATE_2: Int,
  SPEED_STATE_3: Int,
  MAXIMUM_SPEED: Int,
  BOOST_SPEED: Int,

  PLAYER_ONE_START_LANE: Int,
  PLAYER_ONE_START_BLOCK: Int,
  PLAYER_TWO_START_LANE: Int,
  PLAYER_TWO_START_BLOCK: Int,

  EMPTY_MAP_OBJECT: Int,

  STARTING_BLOCK_FOR_GENERATED_MAP_OBJECTS: Int,

  MUD_GENERATION_PERCENTAGE: Int,
  BOOST_GENERATION_PERCENTAGE: Int,
  OIL_ITEM_GENERATION_PERCENTAGE: Int,

  MUD_MAP_OBJECT: Int,
  OIL_SPILL_MAP_OBJECT: Int,
  OIL_ITEM_MAP_OBJECT: Int,
  FINISH_LINE_MAP_OBJECT: Int,
  BOOST_MAP_OBJECT: Int,

  OIL_POWERUP_ITEM: String,
  BOOST_POWERUP_ITEM: String,
  BOOST_DURATION: Int,

  NO_COMMAND: String,
  NOTHING_COMMAND: String,

  TURN_LEFT_COMMAND: String,
  TURN_RIGHT_COMMAND: String,
  CHANGE_LANE_PENALTY: Int,

  ACCELERATE_COMMAND: String,
  DECELERATE_COMMAND: String,

  USE_BOOST_COMMAND: String,
  USE_OIL_COMMAND: String,

  BACKWARD_VISIBILITY: Int,
  FORWARD_VISIBILITY: Int,

  HIT_MUD_SCORE_PENALTY: Int,
  HIT_OIL_SCORE_PENALTY: Int,
  PICKUP_POWERUP_BONUS: Int,
  USE_POWERUP_BONUS: Int,

  csvSeparator: String,

  RACER_PLACEMENT_SORTING: Int
)

object Config {
    implicit val formats = DefaultFormats

    private var ConfigFilePath: String = "";
    private var ConfigJson: String = "";
    private var ParsedConfigJson: JValue = null;
    private var ConfigFileValues: GameEngineConfig = null;

    def load(configFilePath: String) = {
        ConfigFilePath = configFilePath;
        ConfigJson = Source.fromFile(ConfigFilePath).getLines.mkString;
        ParsedConfigJson = parse(ConfigJson);
        ConfigFileValues = ParsedConfigJson.extract[GameEngineConfig];
    }

    def loadDefault() = {
        load("default-config.json");
    }
    def NUMBER_OF_LANES: Int = ConfigFileValues.NUMBER_OF_LANES
    def TRACK_LENGTH: Int = ConfigFileValues.TRACK_LENGTH
    def MIN_LANE = ConfigFileValues.MIN_LANE
    def MAX_LANE = ConfigFileValues.MAX_LANE
    def MAX_ROUNDS = ConfigFileValues.MAX_ROUNDS

    def EMPTY_PLAYER: Int = ConfigFileValues.EMPTY_PLAYER
    
    def DEFAULT_HEALTH: Int = ConfigFileValues.DEFAULT_HEALTH
    def DEFAULT_SCORE: Int = ConfigFileValues.DEFAULT_SCORE

    def READY_PLAYER_STATE: String = ConfigFileValues.READY_PLAYER_STATE
    def NOTHING_PLAYER_STATE: String = ConfigFileValues.NOTHING_PLAYER_STATE
    def TURNING_LEFT_PLAYER_STATE: String = ConfigFileValues.TURNING_LEFT_PLAYER_STATE
    def TURNING_RIGHT_PLAYER_STATE: String = ConfigFileValues.TURNING_RIGHT_PLAYER_STATE
    def ACCELERATING_PLAYER_STATE: String = ConfigFileValues.ACCELERATING_PLAYER_STATE
    def DECELERATING_PLAYER_STATE: String = ConfigFileValues.DECELERATING_PLAYER_STATE
    def PICKED_UP_POWERUP_PLAYER_STATE: String = ConfigFileValues.PICKED_UP_POWERUP_PLAYER_STATE
    def USED_POWERUP_BOOST_PLAYER_STATE: String = ConfigFileValues.USED_POWERUP_BOOST_PLAYER_STATE
    def USED_POWERUP_OIL_PLAYER_STATE: String = ConfigFileValues.USED_POWERUP_OIL_PLAYER_STATE
    def HIT_MUD_PLAYER_STATE: String = ConfigFileValues.HIT_MUD_PLAYER_STATE
    def HIT_OIL_PLAYER_STAE: String = ConfigFileValues.HIT_OIL_PLAYER_STAE
    def FINISHED_PLAYER_STATE: String = ConfigFileValues.FINISHED_PLAYER_STATE

    def MINIMUM_SPEED: Int = ConfigFileValues.MINIMUM_SPEED
    def SPEED_STATE_1: Int = ConfigFileValues.SPEED_STATE_1
    def INITIAL_SPEED: Int = ConfigFileValues.INITIAL_SPEED
    def SPEED_STATE_2: Int = ConfigFileValues.SPEED_STATE_2
    def SPEED_STATE_3: Int = ConfigFileValues.SPEED_STATE_3
    def MAXIMUM_SPEED: Int = ConfigFileValues.MAXIMUM_SPEED
    def BOOST_SPEED: Int = ConfigFileValues.BOOST_SPEED

    def PLAYER_ONE_START_LANE: Int = ConfigFileValues.PLAYER_ONE_START_LANE
    def PLAYER_ONE_START_BLOCK: Int = ConfigFileValues.PLAYER_ONE_START_BLOCK
    def PLAYER_TWO_START_LANE: Int = ConfigFileValues.PLAYER_TWO_START_LANE
    def PLAYER_TWO_START_BLOCK: Int = ConfigFileValues.PLAYER_TWO_START_BLOCK
    
    def EMPTY_MAP_OBJECT: Int = ConfigFileValues.EMPTY_MAP_OBJECT;

    def STARTING_BLOCK_FOR_GENERATED_MAP_OBJECTS: Int = ConfigFileValues.STARTING_BLOCK_FOR_GENERATED_MAP_OBJECTS

    def MUD_GENERATION_PERCENTAGE: Int = ConfigFileValues.MUD_GENERATION_PERCENTAGE
    def BOOST_GENERATION_PERCENTAGE: Int = ConfigFileValues.BOOST_GENERATION_PERCENTAGE
    def OIL_ITEM_GENERATION_PERCENTAGE: Int = ConfigFileValues.OIL_ITEM_GENERATION_PERCENTAGE

    def MUD_MAP_OBJECT: Int = ConfigFileValues.MUD_MAP_OBJECT
    def OIL_SPILL_MAP_OBJECT: Int = ConfigFileValues.OIL_SPILL_MAP_OBJECT
    def OIL_ITEM_MAP_OBJECT: Int = ConfigFileValues.OIL_ITEM_MAP_OBJECT
    def FINISH_LINE_MAP_OBJECT: Int = ConfigFileValues.FINISH_LINE_MAP_OBJECT
    def BOOST_MAP_OBJECT: Int = ConfigFileValues.BOOST_MAP_OBJECT

    def OIL_POWERUP_ITEM: String = ConfigFileValues.OIL_POWERUP_ITEM
    def BOOST_POWERUP_ITEM: String = ConfigFileValues.BOOST_POWERUP_ITEM
    def BOOST_DURATION: Int = ConfigFileValues.BOOST_DURATION

    def NO_COMMAND: String = ConfigFileValues.NO_COMMAND
    def NOTHING_COMMAND: String = ConfigFileValues.NOTHING_COMMAND

    def TURN_LEFT_COMMAND: String = ConfigFileValues.TURN_LEFT_COMMAND
    def TURN_RIGHT_COMMAND: String = ConfigFileValues.TURN_RIGHT_COMMAND
    def CHANGE_LANE_PENALTY: Int = ConfigFileValues.CHANGE_LANE_PENALTY

    def ACCELERATE_COMMAND: String = ConfigFileValues.ACCELERATE_COMMAND
    def DECELERATE_COMMAND: String = ConfigFileValues.DECELERATE_COMMAND

    def USE_BOOST_COMMAND: String = ConfigFileValues.USE_BOOST_COMMAND
    def USE_OIL_COMMAND: String = ConfigFileValues.USE_OIL_COMMAND

    def BACKWARD_VISIBILITY: Int = ConfigFileValues.BACKWARD_VISIBILITY
    def FORWARD_VISIBILITY: Int = ConfigFileValues.FORWARD_VISIBILITY

    def HIT_MUD_SCORE_PENALTY: Int = ConfigFileValues.HIT_MUD_SCORE_PENALTY
    def HIT_OIL_SCORE_PENALTY: Int = ConfigFileValues.HIT_OIL_SCORE_PENALTY
    def PICKUP_POWERUP_BONUS: Int = ConfigFileValues.PICKUP_POWERUP_BONUS
    def USE_POWERUP_BONUS: Int = ConfigFileValues.USE_POWERUP_BONUS

    def RACER_PLACEMENT_SORTING: Int = ConfigFileValues.RACER_PLACEMENT_SORTING
}
