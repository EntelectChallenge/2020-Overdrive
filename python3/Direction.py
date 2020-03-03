import enum
import DirectionChange

class Direction(enum.Enum):
    FRONT = DirectionChange(0, 1)
    BACK = DirectionChange(0, -1)
    LEFT = DirectionChange(-1, 0)
    RIGHT = DirectionChange(1, 0)
