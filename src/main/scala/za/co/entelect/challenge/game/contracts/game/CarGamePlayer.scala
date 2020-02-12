package za.co.entelect.challenge.game.contracts.game

class CarGamePlayer(health: Int, score: Int) extends GamePlayer{
  override def getHealth: Int = {
    return health;
  }

  override def getScore: Int = {
    return score;
  }
}
