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
    - [All Commands](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#all-commands)
    - [Command Structure](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#command-structure)
    - [Command: NOTHING](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#command:-nothing)
    - [Command: ACCELERATE](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#command:-accelerate)
    - [Command: DECELERATE](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#command:-decelerate)
    - [Command: TURN_LEFT](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#command:-turn_left)
    - [Command: TURN_RIGHT](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#command:-turn_right)
    - [Command: USE_BOOST](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#command:-use_boost)
    - [Command: USE_OIL](https://github.com/EntelectChallenge/2020-Overdrive1/blob/master/game-engine/game-rules.md#command:-use_oil)
    - [Command: USE_TWEET 4 76](https://github.com/EntelectChallenge/2020-Overdrive1/blob/master/game-engine/game-rules.md#command:-use_tweet-4-76)
    - [Command: USE_LIZARD](https://github.com/EntelectChallenge/2020-Overdrive1/blob/master/game-engine/game-rules.md#command:-Command:-use_lizard)
  - [Collisions](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#collisions)
  - [Obstacles](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#obstacles) 
    - [Obstacle: MUD](https://github.com/EntelectChallenge/2020-Overdrive1/blob/master/game-engine/game-rules.md#obstacle:-mud)
    - [Obstacle: WALL](https://github.com/EntelectChallenge/2020-Overdrive1/blob/master/game-engine/game-rules.md#obstacle:-wall)
  - [Endgame](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#endgame)
  - [Score](https://github.com/EntelectChallenge/2020-Overdrive/blob/master/game-engine/game-rules.md#score)

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

Should you try to use a powerup you do not have this will default to a DO_NOTHING command and you will incur a score penalty

*Various actions withing game will affect your player state?* 

* READY: state all players start in at the beggining of the race
* NOTHING: state player is in after executing a DO_NOTHING command
* TURNING_LEFT: state player is in after successfully executing a TURN_LEFT command
* TURNING_RIGHT: state player is in after successfully executing a TURN_RIGHT command
* ACCELERATING: state player is in after executing an ACCELERATE command
* DECELERATING: state player is in after executing a DECELERATE command
* PICKED_UP_POWERUP: state player is in after picking up a power up
* USED_BOOST: state player is in after successfully using a boost power up
* USED_OIL: state player is in after successfully using an oil power up
* HIT_MUD: state player is in after hitting a mud a block
* FINISHED: state player is in once they land on the finish line

## The Commands

In every round each player can submit one command for their car.

* All player commands are validated before executing any commands. Invalid commands (eg. Invalid syntax) result in the car doing nothing for the round.
* Invalid commands will negatively impact your score
* Too many invalid commands will invalidate the race
* Both player's commands are executed at the same time (in a single round), and not sequentially.

## Speed
Speed determines how many blocks forward your car will move this round.

Speed scaling is not linear and is illustrated below:

* MINIMUM_SPEED = 0
* SPEED_STATE_1 = 3
* INITIAL_SPEED = 5
* SPEED_STATE_2 = 6
* SPEED_STATE_3 = 8
* MAXIMUM_SPEED = 9 
* BOOST_SPEED = 15

### All Commands

* NOTHING
* ACCELERATE
* DECELERATE
* TURN_LEFT
* TURN_RIGHT
* USE_BOOST
* USE_OIL

### Command Structure

Commands should be issued to the console in the format `C;{round_number};{command}`
For Example, to accelerate in the first round: `C;1;ACCELERATE`

### Command: NOTHING

This command allows your car to do nothing in the round. Your speed will not change, and your car will remain in the same lane it is currently in.

### Command: ACCELERATE

This command increases your car's speed to the next speed state (up to **MAXIMUM_SPEED**). Your car remains in the same lane.

### Command: DECELERATE

This command decreases your car's speed to the previous speed state (up to **MINIMUM_SPEED**). Your car remains in the same lane.

### Command: TURN_LEFT

This command changes your car's lane to the next lane on the left. This is equivalent to up on the visualisation in the console. The first block the bot moves will be directly to the left, and then the rest of the blocks directly forward in the new lane.
Your current speed does not change. To be clear this means the total forward movement of your car will be speed - 1.

* Attempting to turn outside of the track will keep your car in the current lane, you will still incur the speed -1 penalty AND this will negatively impact your score

### Command: TURN_RIGHT

This command changes your car's lane to the next lane on the right. This is equivalent to down on the visualisation in the console. The first block the bot moves will be directly to the right, and then the rest of the blocks directly forward in the new lane.
Your current speed does not change. To be clear this means the total forward movement of your car will be speed - 1.

* Attempting to turn outside of the track will keep your car in the current lane, you will still incur the speed -1 penalty AND this will negatively impact your score.

### Command: USE_BOOST

This command allows you to use a boost powerup that your car has collected.

Using a boost will:

* Immediately increase your speed to **BOOST_SPEED** for 5 turns. 
* Any form of deceleration (a **DECELERATE** command or hitting mud / oil) will cancel the boost bringing speed back down to **MAXIMUM_SPEED**
* Attempting to use a boost when you have none will default to a **DO_NOTHING** command.

### Command: USE_OIL

Using oil will:

* Place an oil block directly underneath your car. 
* Any car that passes through it (except you) will have their speed reduced to the previous speed state, same behaviour as driving through mud.
* Attempting to use an oil powerup when you have none will default to a **DO_NOTHING** command.

### Command: USE_TWEET 4 76

Using a tweet will: 

* Spawn a cyber truck at lane 4, block 76 next round.
* If a player collides with the cyber truck, they get stuck behind cyber truck until the end of the round and will have their speed reduced to 3, the cyber truck then disappears.
* If a player tweets again, their existing cyber truck will move to the new location next round.
* If a player tries to spawn cyber truck at location where another player's cybertruck is already located, their powerup usage will be refunded and their old cyber truck will remain where it was.
* If both players try to spawn a cyber truck at the same location at the same time, they will both be refunded and their old cyber trucks will remain where the cyber trucks were.
* Attempting to use a tweet powerup when you have none, will default to a **DO_NOTHING** command.

### Command: USE_LIZARD

Using a lizard will:
* Make your car jump for the round to avoid a lizard running across the track!
* All Collisions, including powerup pickups, obstacles, and other players, are ignored for the round. Your car stays in the same lane. 
* All Collisions, including powerup pickups, obstacles, and other players, in your final block(the block you land on) for the round are applied.
* With regards to player collisions occuring in the final block (the block you land on), they are treated in the same manner as when your car rear-ends the other player. 
    Example: If you land on [1,20], but another player is already in [1,20], your car will be put back to [1,19] as per normal collision     handling.

* Attempting to use an lizard powerup when you have none will default to a **DO_NOTHING** command.

## Collisions

Commands are processed in parallel, and not sequentially. This means that no bot has a command preference or precedence over the other. Due to this, should after processing bot commands they end up in the same block then,
**both bots** will remain in the lane they were previously, with a speed penalty of -1 being applied.

For Example:

* Bot 1 is in position(3,28) with a speed of 6 and issues a ACCELERATE command => future position is (3,36)
* Bot 2 is in position(2,31) with a speed of 6 and issues a TURN_RIGHT command => future position is (3,36)
* The collision will be resolved by moving bot 1 to (3,35) and bot 2 to (2,35).
* If your car is behind another player's car in the same lane you can not over take them using ACCELERATE as you will rear-end them       causing you to be "stuck" behind them unless a applicable boost is used(LIZARD) or you change lane.

## Obstacles

### Obstacle: MUD

Interaction: 

* If a player collides with mud their speed will be reduced a level, if they are boosting the boost will end.
    * SPEED_STATE_1 => SPEED_STATE_1
    * INITIAL_SPEED => SPEED_STATE_1
    * SPEED_STATE_2 => SPEED_STATE_1
    * SPEED_STATE_3 => SPEED_STATE_2
    * MAXIMUM_SPEED => SPEED_STATE_3
    * BOOST_SPEED   => MAXIMUM_SPEED
* If changing lane and turning into a block with the obstacle the car will still be affected.

### Obstacle: WALL

Interaction: 

* If a player collides with a wall their speed will be reduced to 3 regardless of the current speed, if they are boosting the boost will end.
    * SPEED_STATE_1 => SPEED_STATE_1
    * INITIAL_SPEED => SPEED_STATE_1
    * SPEED_STATE_2 => SPEED_STATE_1
    * SPEED_STATE_3 => SPEED_STATE_1
    * MAXIMUM_SPEED => SPEED_STATE_1
    * BOOST_SPEED   => SPEED_STATE_1
* If changing lane and turning into a block with the obstacle the car will still be affected.

## Endgame

The first car to reach the end of the track, defined by blocks containing a FINISH_LINE object is the winning car and thus the winning bot.

* Should both bots reached the finish line then the bot that crossed the finish line with the **highest speed** will win
* Should both bots reach the finish line with the same speed, then the bot with the **highest score** will win

## Score
All players start the race with a score of 0. 

Different actions that occur in game will affect your score as below:

* Hitting mud will reduce your score by 3
* Hitting an oil spill will reduce your score by 4
* Picking up a powerup will increase your score by 4
* Using a powerup will increase your score by 
* Issuing and invalid command will reduce your score by 5. Invalid commands are defined as below: 

    * Turning left while in lane 1
    * Turning right while in lane 4
    * Using a power up you do not have
    * Issuing a command that does not follow the syntax described in a previous section
    * Your bot failing to issue a command after the wait timeout has transpired
