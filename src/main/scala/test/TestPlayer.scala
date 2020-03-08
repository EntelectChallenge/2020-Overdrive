package test

import za.co.entelect.challenge.game.contracts.map.GameMap
import za.co.entelect.challenge.game.contracts.player.Player

class TestPlayer(playerName: String) extends Player(playerName){
  override def startGame(gameMap: GameMap): Unit = ???

  override def newRoundStarted(gameMap: GameMap): Unit = ???

  override def gameEnded(gameMap: GameMap): Unit = ???
}
