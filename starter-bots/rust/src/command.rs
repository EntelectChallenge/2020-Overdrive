use std::fmt;

#[derive(Clone, Copy, Debug, PartialEq, Eq)]
pub enum Command {
    Nothing,
    Accelerate,
    Decelerate,
    TurnLeft,
    TurnRight,
    UseBoost,
    UseOil,
}

impl fmt::Display for Command {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        use Command::*;
        match self {
            Nothing => write!(f, "NOTHING"),
            Accelerate => write!(f, "ACCELERATE"),
            Decelerate => write!(f, "DECELERATE"),
            TurnLeft => write!(f, "TURN_LEFT"),
            TurnRight => write!(f, "TURN_RIGHT"),
            UseBoost => write!(f, "USE_BOOST"),
            UseOil => write!(f, "USE_OIL"),
        }
    }
}
