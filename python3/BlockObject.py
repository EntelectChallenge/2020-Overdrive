import enum


class BlockObject(enum.Enum):
    EMPTY = 0
    MUD = 1
    OIL_SPILL = 2
    OIL_POWER = 3
    FINISH = 4
    BOOST = 5
