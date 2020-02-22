package za.co.entelect.challenge.game.contracts.game

class CarGamePlayer(health: Int, score: Int, gamePlayerId: Int, var speed: Int, var state: String) extends GamePlayer{
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
}
