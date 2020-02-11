package za.co.entelect.challenge.game.contracts.renderer
import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.GameMap

class CarGameRenderer extends GameMapRenderer{
  override def render(gameMap: GameMap, player: GamePlayer): String = ???

  override def commandPrompt(gamePlayer: GamePlayer): String = ???
}
