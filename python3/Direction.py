import enum
import DirectionChange


class Direction(enum.Enum):
    front = DirectionChange(0, 1)
    back = DirectionChange(0, -1)
    left = DirectionChange(-1, 0)
    right = DirectionChange(1, 0)
