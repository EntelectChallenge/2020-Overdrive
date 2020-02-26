package za.co.entelect.challenge.game.contracts.game

import za.co.entelect.challenge.game.contracts.Config.Config

class CarGamePlayer(health: Int, score: Int, gamePlayerId: Int, var speed: Int) extends GamePlayer{
  private val MINIMUM_SPEED: Int = Config.MINIMUM_SPEED;
  private val SPEED_STATE_1: Int = Config.SPEED_STATE_1;
  private val INITIAL_SPEED: Int = Config.INITIAL_SPEED;
  private val SPEED_STATE_2: Int = Config.SPEED_STATE_2;
  private val SPEED_STATE_3: Int = Config.SPEED_STATE_3;
  private val MAXIMUM_SPEED: Int = Config.MAXIMUM_SPEED; 

  private var state: String = "";

  override def getHealth: Int = {
    return health;
  }

  override def getScore: Int = {
    return score;
  }

  def getGamePlayerId(): Int = {
    return gamePlayerId;
  }

  def getSpeed(): Int = {
    return speed;
  }

  def getState(): String = {
    return state;
  }

  def getReady() = {
    setState(Config.READY_PLAYER_STATE);
  }

  def doNothing() = {
    setState(Config.NOTHING_PLAYER_STATE);
  }

  def turnRight() = {
    setState(Config.TURNING_RIGHT_PLAYER_STATE);
  }

  def turnLeft() = {
    setState(Config.TURNING_LEFT_PLAYER_STATE);
  }

  def hitMud() = {
    val allowStop: Boolean = false;
    reduceSpeed(allowStop);
    setState(Config.HIT_MUD_PLAYER_STATE);
  }

  def decelerate() = {
    val allowStop = true;
    reduceSpeed(allowStop);
    setState(Config.DECELERATING_PLAYER_STATE);
  }

  private def reduceSpeed(allowStop: Boolean) = {
    speed match {
      case MINIMUM_SPEED => speed = MINIMUM_SPEED
      case SPEED_STATE_1 => speed = if(allowStop) MINIMUM_SPEED else SPEED_STATE_1
      case INITIAL_SPEED => speed = SPEED_STATE_1
      case SPEED_STATE_2 => speed = SPEED_STATE_1
      case SPEED_STATE_3 => speed = SPEED_STATE_2
      case MAXIMUM_SPEED => speed = SPEED_STATE_3
      case invalidSpeed => throw new Exception("Invalid current speed: " + invalidSpeed.toString())
    };
  }

  def accelerate() = {
    increaseSpeed();
    setState(Config.ACCELERATING_PLAYER_STATE);
  }

  private def increaseSpeed() = {
    speed match {
      case MINIMUM_SPEED => speed = SPEED_STATE_1
      case SPEED_STATE_1 => speed = SPEED_STATE_2
      case INITIAL_SPEED => speed = SPEED_STATE_2
      case SPEED_STATE_2 => speed = SPEED_STATE_3
      case SPEED_STATE_3 => speed = MAXIMUM_SPEED
      case MAXIMUM_SPEED => speed = MAXIMUM_SPEED
      case invalidSpeed => throw new Exception("Invalid current speed: " + invalidSpeed.toString())
    };
  }

  def finish() = {
    setState(Config.FINISHED_PLAYER_STATE);
  }

  private def setState(newPlayerState: String) = {
    state = newPlayerState;
  }
}
