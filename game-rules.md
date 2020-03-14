# The Game

The Game for Entelect Challenge 2020 is **Overdrive**. 

In a match **2** players with **1** car each will race against each other. The goal is to be the first player to the finish line.

## Contents
- [The Game](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#the-game)
  - [The Map](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#the-map)
      - [Structure](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#structure)
      - [Visiblity](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#visibility)
      - [Files](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#files)
  - [The Car](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#the-car)
  - [The Commands](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#the-commands)
    - [All Commands](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#move)
    - [Command Structure](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#move)
    - [Command: NOTHING](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#command:-nothing)
    - [Command: ACCELERATE](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#command:-ACCELERATE)
    - [Command: DECELERATE](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#command:-DECELERATE)
    - [Command: TURN_LEFT](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#command:-TURN_LEFT)
    - [Command: TURN_RIGHT](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#command:-TURN_RIGHT)
    - [Command: USE_BOOST](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#command:-USE_BOOST)
    - [Command: USE_OIL](https://github.com/EntelectChallenge/2020-Overdrive1/blob/master/game-engine/game-rules.md#command:-USE_OIL)
  - [Collisions](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#collisions)
  - [Endgame](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#endgame)

## The Map

### Structure 

The Map is a 2D Array of 4 lanes. Each lane is made up of consecutive blocks. Each block is one of the following types:
* Empty - the block is clear and will have no impact on your car's speed
* Mud - the block has mud on it, and will slow your car down
* Oil Spill - the block has oil on it, and will slow your car down
* Finish Line - Your car has reached the end of the map.

Blocks can contain powerups. Powerups are picked up when a car moves onto that block.
* Oil Item - the block has an oil drum on it for you to collect, allowing you to create oil spills later.
* Boost - the block has a boost powerup which upon use will dramatically speed up your car.

### Visiblity

The Map is 1500 blocks in length. However, the `state` file gives a visibility of up to 5 blocks behind your bot, where available, and up to 20 blocks ahead of your bot, where available.

### Files

Two map files will be a available: A json file `state.json` and a text file `state.txt`. In addition the map will be rendered on the console during a running game. 

State files are explained in detail [here](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/state-files.md "Detailed explanation of the state files").

## The Car

Your bot is playing as a race car, on a straight line course. Your car begins with a speed of 5. This means that your car will move forward five blocks in each round. 
The commands available to you will allow you to change lanes, speed up, as well as slow down, and use powerups.

Powerups can be collected by the car by continuing over a block that is shown to have a powerup in it. 

Once a powerup has been collected, its corresponding command can be used to active that powerup. 

*What happens if a car uses a powerup they dont have?* 
**Should you try to use a powerup you do not have this will default to a DO_NOTHING command and you will incur a score penalty

*Various actions withing game will affect your player state?* 
**READY => state all players start in at the beggining of the race
**NOTHING => state player is in after executing a DO_NOTHING command
**TURNING_LEFT => state player is in after successfully executing a TURN_LEFT command
**TURNING_RIGHT => state player is in after successfully executing a TURN_RIGHT command
**ACCELERATING => state player is in after executing an ACCELERATE command
**DECELERATING => state player is in after executing a DECELERATE command
**PICKED_UP_POWERUP => state player is in after picking up a power up
**USED_BOOST => state player is in after successfully using a boost power up
**USED_OIL => state player is in after successfully using an oil power up
**HIT_MUD => state player is in after hitting a mud a block
**FINISHED => state player is in once they land on the finish line

## The Commands

*In every round each player can submit one command for their car.

*All player commands are validated before executing any commands. Invalid commands (eg. Invalid syntax) result in the car doing nothing for the round.
**invalid commands will negatively impact your score
**too many invalid commands will invalidate the race

*Both player's commands are executed at the same time (in a single round), and not sequentially.

## Speed
*Speed determines how many blocks forward your car will move this round.
*Speed scaling is not linear and is illustrated below:
**MINIMUM_SPEED = 0
**SPEED_STATE_1 = 3
**INITIAL_SPEED = 5
**SPEED_STATE_2 = 6
**SPEED_STATE_3 = 8
**MAXIMUM_SPEED = 9 
**BOOST_SPEED = 15

### All Commands

- NOTHING
- ACCELERATE
- DECELERATE
- TURN_LEFT
- TURN_RIGHT
- USE_BOOST
- USE_OIL

### Command Structure

Commands should be issued to the console in the format `C;{round_number};{command}`
For Example, to accelerate in the first round: `C;1;ACCELERATE`

### Command: NOTHING

This command allows your car to do nothing in the round. Your speed will not change, and your car will remain in the same lane it is currently in.

### Command: ACCELERATE

This command increases your car's speed to the next speed state (up to *MAXIMUM_SPEED*). Your car remains in the same lane.
*

### Command: DECELERATE

This command decreases your car's speed to the previous speed state (up to *MINIMUM_SPEED*). Your car remains in the same lane.

### Command: TURN_LEFT

This command changes your car's lane to the next lane on the left. This is equivalent to up on the visualisation in the console. The first block the bot moves will be directly to the left, and then the rest of the blocks directly forward in the new lane.
Your current speed does not change. To be clear this means the total forward movement of your car will be speed - 1.
*attempting to turn outside of the track will keep your car in the current lane, you will still incur the speed -1 penalty AND this will negatively impact your score

### Command: TURN_RIGHT

This command changes your car's lane to the next lane on the right. This is equivalent to down on the visualisation in the console. The first block the bot moves will be directly to the left, and then the rest of the blocks directly forward in the new lane.
Your current speed does not change. To be clear this means the total forward movement of your car will be speed - 1.
*attempting to turn outside of the track will keep your car in the current lane, you will still incur the speed -1 penalty AND this will negatively impact your score

### Command: USE_BOOST

This command allows you to use a boost powerup that your car has collected.
Using a boost will:
*immediately increase your speed to BOOST_SPEED for 5 turns. 
*any form of deceleration (a DECELERATE command or hitting mud / oil) will cancel the boost bringing speed back down to MAXIMUM_SPEED
*attempting to use a boost when you have none will have a negative impact on your score and default to a DO_NOTHING command

### Command: USE_OIL

This command will:
*place an oil block directly behind your car. 
*any car that passes through it will have their speed reduced to the previous speed state
*attempting to use an oil powerup when you have none will have a negative impact on your score and default to a DO_NOTHING command

## Collisions

Commands are processed in parallel, and not sequentially. This means that no bot has a command preference or precendence over the other. Due to this, should after processing bot coommands they end up in the same block then,
***both** bots will remain in the lane they were previously, with a speed penalty of -1 being applied.

For Example:
*Bot 1 is in position(3,28) with a speed of 6 and issues a ACCELERATE command => future position is (3,36)
*Bot 2 is in positon(2,31) with a speed of 6 and issues a TURN_RIGHT command => future position is (3,36)
*the collision will be resolved by moving bot 1 to (3,35) and bot 2 to (2,35)

## Endgame

The first car to reach the end of the track, defined by blocks containing a FINISH_LINE object is the winning car and thus the winning bot.
*should both bots reached the finish line then the bot that crossed the finish line with the highest speed will win
*should both bots reach the finish line with the same speed, then the bot with the *highest score* will win

## Score
All players start the race with a score of 0. Different actions that occur in game will affect your score as below:
*hitting mud will reduce your score by 3
*hitting an oil spill will reduce your score by 4
*picking up a powerup will increase your score by 4
*using a powerup will increase your score by 
*issuing and invalid command will reduce your score by 5. Invalid commands are defined as below:
**turning left while in lane 1
**turning right while in lane 4
**using a power up you do not have
**issuing a command that does not follow the syntax described in a previous section
**your bot failing to issue a command after the wait timeout has transpired