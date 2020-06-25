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
    USED_POWERUP_LIZARD_PLAYER_STATE: String,
    USED_POWERUP_TWEET_PLAYER_STATE: String,
    USED_POWERUP_EMP_PLAYER_STATE: String,
    HIT_MUD_PLAYER_STATE: String,
    HIT_OIL_PLAYER_STATE: String,
    FINISHED_PLAYER_STATE: String,
    HIT_WALL_PLAYER_STATE: String,
    HIT_CYBER_TRUCK_PLAYER_STATE: String,
    HIT_EMP_PLAYER_STATE: String,
    FIX_CAR_PLAYER_STATE: String,

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
    EMP_BLOCK_GAP: Int,

    MUD_GENERATION_PERCENTAGE: Int,
    BOOST_GENERATION_PERCENTAGE: Int,
    OIL_ITEM_GENERATION_PERCENTAGE: Int,
    WALL_GENERATION_PERCENTAGE: Int,
    TWEET_GENERATION_PERCENTAGE: Int,
    LIZARD_GENERATION_PERCENTAGE: Int,
    EMP_GENERATION_PERCENTAGE: Int,

    MUD_MAP_OBJECT: Int,
    OIL_SPILL_MAP_OBJECT: Int,
    OIL_ITEM_MAP_OBJECT: Int,
    FINISH_LINE_MAP_OBJECT: Int,
    BOOST_MAP_OBJECT: Int,
    WALL_MAP_OBJECT: Int,
    LIZARD_MAP_OBJECT: Int,
    TWEET_MAP_OBJECT: Int,
    EMP_MAP_OBJECT: Int,

    OIL_POWERUP_ITEM: String,
    BOOST_POWERUP_ITEM: String,
    BOOST_DURATION: Int,
    LIZARD_POWERUP_ITEM: String,
    TWEET_POWERUP_ITEM: String,
    EMP_POWERUP_ITEM: String,

    NO_COMMAND: String,
    NOTHING_COMMAND: String,

    TURN_LEFT_COMMAND: String,
    TURN_RIGHT_COMMAND: String,
    CHANGE_LANE_PENALTY: Int,

    ACCELERATE_COMMAND: String,
    DECELERATE_COMMAND: String,

    USE_BOOST_COMMAND: String,
    USE_OIL_COMMAND: String,
    USE_LIZARD_COMMAND: String,
    USE_TWEET_COMMAND: String,
    USE_EMP_COMMAND: String,

    FIX_COMMAND: String,

    BACKWARD_VISIBILITY: Int,
    FORWARD_VISIBILITY: Int,

    HIT_MUD_SCORE_PENALTY: Int,
    HIT_OIL_SCORE_PENALTY: Int,
    HIT_WALL_SCORE_PENALTY: Int,
    HIT_CYBERTRUCK_SCORE_PENALTY: Int,
    HIT_EMP_SCORE_PENALTY: Int,
    PICKUP_POWERUP_BONUS: Int,
    USE_POWERUP_BONUS: Int,
    INVALID_COMMAND_PENALTY: Int,

    csvSeparator: String,

    RACER_PLACEMENT_SORTING: Int,

    DAMAGE_MUD: Int,
    DAMAGE_OIL: Int,
    DAMAGE_WALL: Int,
    DAMAGE_CYBERTRUCK: Int,
    DAMAGE_REPAIR_VALUE: Int
) {}

object Config {
    implicit val formats = DefaultFormats

    private var ConfigFilePath: String = ""
    private var ConfigJson: String = ""
    private var ParsedConfigJson: JValue = null
    private var ConfigFileValues: GameEngineConfig = null

    def load(configFilePath: String) = {
        ConfigFilePath = configFilePath
        ConfigJson = Source.fromFile(ConfigFilePath).getLines.mkString
        ParsedConfigJson = parse(ConfigJson)
        ConfigFileValues = ParsedConfigJson.extract[GameEngineConfig]
    }

    def loadDefault() = {
        load("default-config.json")
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
    def USED_POWERUP_LIZARD_PLAYER_STATE: String = ConfigFileValues.USED_POWERUP_LIZARD_PLAYER_STATE
    def USED_POWERUP_TWEET_PLAYER_STATE: String = ConfigFileValues.USED_POWERUP_TWEET_PLAYER_STATE
    def USED_POWERUP_EMP_PLAYER_STATE: String = ConfigFileValues.USED_POWERUP_EMP_PLAYER_STATE
    def HIT_MUD_PLAYER_STATE: String = ConfigFileValues.HIT_MUD_PLAYER_STATE
    def HIT_OIL_PLAYER_STATE: String = ConfigFileValues.HIT_OIL_PLAYER_STATE
    def FINISHED_PLAYER_STATE: String = ConfigFileValues.FINISHED_PLAYER_STATE
    def HIT_WALL_PLAYER_STATE: String = ConfigFileValues.HIT_WALL_PLAYER_STATE
    def HIT_CYBER_TRUCK_PLAYER_STATE: String = ConfigFileValues.HIT_CYBER_TRUCK_PLAYER_STATE
    def HIT_EMP_PLAYER_STATE: String = ConfigFileValues.HIT_EMP_PLAYER_STATE
    def FIX_CAR_PLAYER_STATE: String = ConfigFileValues.FIX_CAR_PLAYER_STATE

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

    def EMPTY_MAP_OBJECT: Int = ConfigFileValues.EMPTY_MAP_OBJECT

    def STARTING_BLOCK_FOR_GENERATED_MAP_OBJECTS: Int = ConfigFileValues.STARTING_BLOCK_FOR_GENERATED_MAP_OBJECTS
    def EMP_BLOCK_GAP: Int = ConfigFileValues.EMP_BLOCK_GAP

    def MUD_GENERATION_PERCENTAGE: Int = ConfigFileValues.MUD_GENERATION_PERCENTAGE
    def BOOST_GENERATION_PERCENTAGE: Int = ConfigFileValues.BOOST_GENERATION_PERCENTAGE
    def OIL_ITEM_GENERATION_PERCENTAGE: Int = ConfigFileValues.OIL_ITEM_GENERATION_PERCENTAGE
    def WALL_GENERATION_PERCENTAGE: Int = ConfigFileValues.WALL_GENERATION_PERCENTAGE
    def LIZARD_GENERATION_PERCENTAGE: Int = ConfigFileValues.LIZARD_GENERATION_PERCENTAGE
    def TWEET_GENERATION_PERCENTAGE: Int = ConfigFileValues.TWEET_GENERATION_PERCENTAGE
    def EMP_GENERATION_PERCENTAGE: Int = ConfigFileValues.EMP_GENERATION_PERCENTAGE

    def MUD_MAP_OBJECT: Int = ConfigFileValues.MUD_MAP_OBJECT
    def OIL_SPILL_MAP_OBJECT: Int = ConfigFileValues.OIL_SPILL_MAP_OBJECT
    def OIL_ITEM_MAP_OBJECT: Int = ConfigFileValues.OIL_ITEM_MAP_OBJECT
    def FINISH_LINE_MAP_OBJECT: Int = ConfigFileValues.FINISH_LINE_MAP_OBJECT
    def BOOST_MAP_OBJECT: Int = ConfigFileValues.BOOST_MAP_OBJECT
    def WALL_MAP_OBJECT: Int = ConfigFileValues.WALL_MAP_OBJECT
    def LIZARD_MAP_OBJECT: Int = ConfigFileValues.LIZARD_MAP_OBJECT
    def TWEET_MAP_OBJECT: Int = ConfigFileValues.TWEET_MAP_OBJECT
    def EMP_MAP_OBJECT: Int = ConfigFileValues.EMP_MAP_OBJECT

    def OIL_POWERUP_ITEM: String = ConfigFileValues.OIL_POWERUP_ITEM
    def BOOST_POWERUP_ITEM: String = ConfigFileValues.BOOST_POWERUP_ITEM
    def BOOST_DURATION: Int = ConfigFileValues.BOOST_DURATION
    def LIZARD_POWERUP_ITEM: String = ConfigFileValues.LIZARD_POWERUP_ITEM
    def TWEET_POWERUP_ITEM: String = ConfigFileValues.TWEET_POWERUP_ITEM
    def EMP_POWERUP_ITEM: String = ConfigFileValues.EMP_POWERUP_ITEM

    def NO_COMMAND: String = ConfigFileValues.NO_COMMAND
    def NOTHING_COMMAND: String = ConfigFileValues.NOTHING_COMMAND

    def TURN_LEFT_COMMAND: String = ConfigFileValues.TURN_LEFT_COMMAND
    def TURN_RIGHT_COMMAND: String = ConfigFileValues.TURN_RIGHT_COMMAND
    def CHANGE_LANE_PENALTY: Int = ConfigFileValues.CHANGE_LANE_PENALTY

    def ACCELERATE_COMMAND: String = ConfigFileValues.ACCELERATE_COMMAND
    def DECELERATE_COMMAND: String = ConfigFileValues.DECELERATE_COMMAND

    def USE_BOOST_COMMAND: String = ConfigFileValues.USE_BOOST_COMMAND
    def USE_OIL_COMMAND: String = ConfigFileValues.USE_OIL_COMMAND
    def USE_LIZARD_COMMAND: String = ConfigFileValues.USE_LIZARD_COMMAND
    def USE_TWEET_COMMAND: String = ConfigFileValues.USE_TWEET_COMMAND
    def USE_EMP_COMMAND: String = ConfigFileValues.USE_EMP_COMMAND

    def FIX_COMMAND: String = ConfigFileValues.FIX_COMMAND

    def BACKWARD_VISIBILITY: Int = ConfigFileValues.BACKWARD_VISIBILITY
    def FORWARD_VISIBILITY: Int = ConfigFileValues.FORWARD_VISIBILITY

    def HIT_MUD_SCORE_PENALTY: Int = ConfigFileValues.HIT_MUD_SCORE_PENALTY
    def HIT_OIL_SCORE_PENALTY: Int = ConfigFileValues.HIT_OIL_SCORE_PENALTY
    def HIT_WALL_SCORE_PENALTY: Int = ConfigFileValues.HIT_WALL_SCORE_PENALTY
    def HIT_CYBERTRUCK_SCORE_PENALTY: Int = ConfigFileValues.HIT_CYBERTRUCK_SCORE_PENALTY
    def HIT_EMP_SCORE_PENALTY: Int = ConfigFileValues.HIT_EMP_SCORE_PENALTY
    def PICKUP_POWERUP_BONUS: Int = ConfigFileValues.PICKUP_POWERUP_BONUS
    def USE_POWERUP_BONUS: Int = ConfigFileValues.USE_POWERUP_BONUS
    def INVALID_COMMAND_PENALTY: Int = ConfigFileValues.INVALID_COMMAND_PENALTY

    def RACER_PLACEMENT_SORTING: Int = ConfigFileValues.RACER_PLACEMENT_SORTING

    def DAMAGE_MUD: Int = ConfigFileValues.DAMAGE_MUD
    def DAMAGE_OIL: Int = ConfigFileValues.DAMAGE_OIL
    def DAMAGE_WALL: Int = ConfigFileValues.DAMAGE_WALL
    def DAMAGE_CYBERTRUCK: Int = ConfigFileValues.DAMAGE_CYBERTRUCK

    def DAMAGE_REPAIR_VALUE: Int = ConfigFileValues.DAMAGE_REPAIR_VALUE

}
