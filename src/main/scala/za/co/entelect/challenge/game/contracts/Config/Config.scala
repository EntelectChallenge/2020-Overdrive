package za.co.entelect.challenge.game.contracts.Config

object Config {
    def NUMBER_OF_LANES: Int = 4;
    def TRACK_LENGTH: Int = 1500;
    def MIN_LANE = 1;
    def MAX_LANE = 4;

    def EMPTY_PLAYER: Int = 0;
    
    def DEFAULT_HEALTH: Int = 0;
    def DEFAULT_SCORE: Int = 0;

    def READY_PLAYER_STATE: String = "READY";

    def MINIMUM_SPEED: Int = 0;
    def SPEED_STATE_1: Int = 3;
    def INITIAL_SPEED: Int = 5;
    def SPEED_STATE_2: Int = 6;
    def SPEED_STATE_3: Int = 8;
    def MAXIMUM_SPEED: Int = 9; 

    def PLAYER_ONE_START_LANE: Int = 1;
    def PLAYER_ONE_START_BLOCK: Int = 1;
    def PLAYER_TWO_START_LANE: Int = 3;
    def PLAYER_TWO_START_BLOCK: Int = 3;
    
    def STARTING_BLOCK_FOR_GENERATED_MAP_OBJECTS: Int = 6;
    def MUD_GENERATION_PERCENTAGE: Int = 10;
    def EMPTY_MAP_OBJECT: Int = 0;
    def MUD_MAP_OBJECT: Int = 1;
    def FINISH_LINE_MAP_OBJECT: Int = 2;

    def NO_COMMAND: String = "NO COMMAND"; //sent by game runner so no control over the space (DO NOT UPDATE THE GAME RUNNER)
    def NOTHING_COMMAND: String = "NOTHING";

    def TURN_LEFT_COMMAND:String = "TURN_LEFT";
    def TURN_RIGHT_COMMAND: String = "TURN_RIGHT";
    def CHANGE_LANE_PENALTY: Int = 1;

    def ACCELERATE_COMMAND:String = "ACCELERATE";
    def DECELERATE_COMMAND:String = "DECELERATE";

    def BACKWARDS_VISIBILITY: Int = 5;
    def FOREWARDS_VISIBILITY: Int = 20;
}