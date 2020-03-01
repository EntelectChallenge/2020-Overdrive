using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Text;

namespace StarterBot.Enums
{
    public enum State
    {
        READY,
        ACCELERATING,
        NOTHING,
        TURNING_RIGHT,
        TURNING_LEFT,
        HIT_MUD,
        HIT_OIL
    }
}
