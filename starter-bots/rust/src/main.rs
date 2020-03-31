use std::io::prelude::*;
use std::io::stdin;

mod command;
mod json;

use command::*;
use json::*;

fn main() {
    for line in stdin().lock().lines() {
        let round_number = line.expect("Failed to read line from stdin: {}");
        let command =
            match read_state_from_json_file(&format!("./rounds/{}/state.json", round_number)) {
                Ok(state) => choose_command(state),
                Err(e) => {
                    eprintln!("WARN: State file could not be parsed: {}", e);
                    Command::Nothing
                }
            };
        println!("C;{};{}", round_number, command);
    }
}

fn choose_command(state: State) -> Command {
    Command::Accelerate
}
