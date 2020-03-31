use std::fs::File;
use std::io::prelude::*;

use anyhow::Result;
use serde::{Deserialize, Serialize};
use serde_json;
use serde_repr::{Deserialize_repr, Serialize_repr};

pub fn read_state_from_json_file(filename: &str) -> Result<State> {
    let mut file = File::open(filename)?;
    let mut content = String::new();
    file.read_to_string(&mut content)?;
    let state: State = serde_json::from_str(content.as_ref())?;

    Ok(state)
}

#[derive(Serialize, Deserialize, Clone, Debug, PartialEq, Eq)]
#[serde(rename_all = "camelCase")]
pub struct State {
    pub current_round: u32,
    pub max_rounds: u32,
    pub player: Player,
    pub opponent: Opponent,
    pub world_map: Vec<Vec<WorldMapCell>>,
}

#[derive(Serialize, Deserialize, Clone, Debug, PartialEq, Eq)]
#[serde(rename_all = "camelCase")]
pub struct Player {
    id: u32,
    position: Position,
    speed: u32,
    state: PlayerState,
    powerups: Vec<Powerup>,
    boosting: bool,
    boost_counter: u32,
}

#[derive(Serialize, Deserialize, Clone, Debug, PartialEq, Eq)]
#[serde(rename_all = "camelCase")]
pub struct Opponent {
    id: u32,
    position: Position,
    speed: u32,
}

#[derive(Serialize, Deserialize, Clone, Debug, PartialEq, Eq)]
#[serde(rename_all = "camelCase")]
pub struct WorldMapCell {
    position: Position,
    surface_object: SurfaceObject,
    occupied_by_player_id: u32,
}

#[derive(Serialize, Deserialize, Clone, Debug, PartialEq, Eq)]
#[serde(rename_all = "camelCase")]
pub struct Position {
    x: u32,
    y: u32,
}

#[derive(Serialize, Deserialize, Clone, Debug, PartialEq, Eq)]
#[serde(rename_all = "SCREAMING_SNAKE_CASE")]
pub enum PlayerState {
    Ready,
    Nothing,
    TurningLeft,
    TurningRight,
    Accelerating,
    Decelarating,
    PickedUpPowerup,
    UsedBoost,
    UsedOil,
    HitMud,
    Finishing,
}

#[derive(Serialize, Deserialize, Clone, Debug, PartialEq, Eq)]
#[serde(rename_all = "SCREAMING_SNAKE_CASE")]
pub enum Powerup {
    Boost,
    Oil,
}

#[derive(Serialize_repr, Deserialize_repr, Clone, Debug, PartialEq, Eq)]
#[serde(rename_all = "camelCase")]
#[repr(u8)]
pub enum SurfaceObject {
    Empty = 0,
    Mud = 1,
    OilSpill = 2,
    OilItem = 3,
    FinishLine = 4,
    Boost = 5,
}
