package za.co.entelect.challenge.game.contracts.Config

object Config {
    def NUMBER_OF_LANES: Int = 4;
    def TRACK_LENGTH: Int = 1500;
    def MIN_LANE = 1;
    def MAX_LANE = 4;
    def MAX_ROUNDS = 600;

    def EMPTY_PLAYER: Int = 0;
    
    def DEFAULT_HEALTH: Int = 0;
    def DEFAULT_SCORE: Int = 0;

    def READY_PLAYER_STATE: String = "READY";
    def NOTHING_PLAYER_STATE: String = "NOTHING";
    def TURNING_LEFT_PLAYER_STATE: String = "TURNING_LEFT";
    def TURNING_RIGHT_PLAYER_STATE: String = "TURNING_RIGHT";
    def ACCELERATING_PLAYER_STATE: String = "ACCELERATING";
    def DECELERATING_PLAYER_STATE: String = "DECELERATING";
    def PICKED_UP_POWERUP_PLAYER_STATE: String = "PICKED_UP_POWERUP";
    def USED_POWERUP_BOOST_PLAYER_STATE: String = "USED_BOOST";
    def USED_POWERUP_OIL_PLAYER_STATE: String = "USED_OIL";
    def HIT_MUD_PLAYER_STATE: String = "HIT_MUD";
    def HIT_OIL_PLAYER_STAE: String = "HIT_OIL";
    def FINISHED_PLAYER_STATE: String = "FINISHED";

    def MINIMUM_SPEED: Int = 0;
    def SPEED_STATE_1: Int = 3;
    def INITIAL_SPEED: Int = 5;
    def SPEED_STATE_2: Int = 6;
    def SPEED_STATE_3: Int = 8;
    def MAXIMUM_SPEED: Int = 9; 
    def BOOST_SPEED: Int = 15;

    def PLAYER_ONE_START_LANE: Int = 1;
    def PLAYER_ONE_START_BLOCK: Int = 1;
    def PLAYER_TWO_START_LANE: Int = 3;
    def PLAYER_TWO_START_BLOCK: Int = 1;
    
    def EMPTY_MAP_OBJECT: Int = 0;

    def STARTING_BLOCK_FOR_GENERATED_MAP_OBJECTS: Int = 6;

    def MUD_GENERATION_PERCENTAGE: Int = 10;
    def BOOST_GENERATION_PERCENTAGE: Int = 1;
    def OIL_ITEM_GENERATION_PERCENTAGE: Int = 1

    def MUD_MAP_OBJECT: Int = 1;
    def OIL_SPILL_MAP_OBJECT: Int = 2
    def OIL_ITEM_MAP_OBJECT: Int = 3
    def FINISH_LINE_MAP_OBJECT: Int = 4;
    def BOOST_MAP_OBJECT: Int = 5;

    def OIL_POWERUP_ITEM: String = "OIL";
    def BOOST_POWERUP_ITEM: String = "BOOST";
    def BOOST_DURATION: Int = 5;

    def NO_COMMAND: String = "NO COMMAND"; //sent by game runner so no control over the space (DO NOT UPDATE THE GAME RUNNER)
    def NOTHING_COMMAND: String = "NOTHING";

    def TURN_LEFT_COMMAND: String = "TURN_LEFT";
    def TURN_RIGHT_COMMAND: String = "TURN_RIGHT";
    def CHANGE_LANE_PENALTY: Int = 1;

    def ACCELERATE_COMMAND: String = "ACCELERATE";
    def DECELERATE_COMMAND: String = "DECELERATE";

    def USE_BOOST_COMMAND: String = "USE_BOOST";
    def USE_OIL_COMMAND: String = "USE_OIL";

    def BACKWARD_VISIBILITY: Int = 5;
    def FORWARD_VISIBILITY: Int = 20;

    def HIT_MUD_SCORE_PENALTY: Int = -3;
    def HIT_OIL_SCORE_PENALTY: Int = -4;
    def PICKUP_POWERUP_BONUS: Int = 4;
    def USE_POWERUP_BONUS: Int = 4;
}
