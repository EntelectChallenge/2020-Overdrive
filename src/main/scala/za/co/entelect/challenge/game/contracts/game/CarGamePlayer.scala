package za.co.entelect.challenge.game.contracts.game

class CarGamePlayer(health: Int, score: Int, gamePlayerId: Int, var speed: Int, var state: String) extends GamePlayer{
  private val MINIMUM_SPEED: Int = 0;
  private val SPEED_STATE_1: Int = 3;
  private val INITIAL_SPEED: Int = 5;
  private val SPEED_STATE_2: Int = 6;
  private val SPEED_STATE_3: Int = 8;
  private val MAXIMUM_SPEED: Int = 9; 

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

  def reduceSpeed() = {
    speed match {
      case MINIMUM_SPEED => speed = MINIMUM_SPEED
      case SPEED_STATE_1 => speed = MINIMUM_SPEED
      case INITIAL_SPEED => speed = SPEED_STATE_1
      case SPEED_STATE_2 => speed = SPEED_STATE_1
      case SPEED_STATE_3 => speed = SPEED_STATE_2
      case MAXIMUM_SPEED => speed = SPEED_STATE_3
      case invalidSpeed => throw new Exception("Invalid current speed: " + invalidSpeed.toString())
    };
  }

  def increaseSpeed() = {
    speed match {
      case MINIMUM_SPEED => speed = SPEED_STATE_1
      case SPEED_STATE_1 => speed = SPEED_STATE_2
      case INITIAL_SPEED => speed = SPEED_STATE_2
      case SPEED_STATE_2 => speed = SPEED_STATE_3
      case SPEED_STATE_3 => speed = MAXIMUM_SPEED
      case MAXIMUM_SPEED => speed = MAXIMUM_SPEED
      case invalidSpeed => throw new Exception("Invalid current speed: " + invalidSpeed.toString())
    }
  }
}
