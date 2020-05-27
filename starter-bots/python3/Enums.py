import enum


class BlockObject(enum.Enum):
    EMPTY = 0
    MUD = 1
    OIL_SPILL = 2
    OIL_POWER = 3
    FINISH = 4
    BOOST = 5
    WALL = 6
    LIZARD = 7
    TWEET = 8


class Direction(enum.Enum):
    LEFT = "LEFT"
    RIGHT = "RIGHT"


class Commands(enum.Enum):
    ACCELERATE = "ACCELERATE"
    DECELERATE = "DECELERATE"
    NOTHING = "NOTHING"
    USE_OIL = "USE_OIL"
    USE_BOOST = "USE_BOOST"
    TURN = "TURN_"


class State(enum.Enum):
    ACCELERATING = "ACCELERATING"
    READY = "READY"
    NOTHING = "NOTHING"
    TURNING_RIGHT = "TURNING_RIGHT"
    TURNING_LEFT = "TURNING_LEFT"
    HIT_MUD = "HIT_MUD"
    DECELERATING= "DECELERATING"
    PICKED_UP_POWERUP = "PICKED_UP_POWERUP"
    USED_BOOST = "USED_BOOST"
    USED_OIL = "USED_OIL"
    HIT_OIL = "HIT_OIL"
    FINISHED = "FINISHED"
    HIT_WALL = "HIT_WALL",
    HIT_CYBER_TRUCK = "HIT_CYBER_TRUCK",
