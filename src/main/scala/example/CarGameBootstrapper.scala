package example

import za.co.entelect.challenge.game.contracts.bootstrapper.GameEngineBootstrapper
import za.co.entelect.challenge.game.contracts.game.{GameEngine, GameMapGenerator, GameReferee, GameRoundProcessor}
import za.co.entelect.challenge.game.contracts.renderer.{GameMapRenderer, RendererType}

class CarGameBootstrapper extends GameEngineBootstrapper {

    var configPath: String = "default-config.json"
    var seed: Int = 0;

    override def setSeed(seed: Int): Unit = {
        this.seed = seed;
    }

    override def setConfigPath(configPath: String): Unit = {
        this.configPath = configPath
    }

    override def getGameEngine: GameEngine = {
      return new CarGameEngine;

    }

    override def getMapGenerator: GameMapGenerator = {
      return new CarMapGenerator;
    }

    override def getRenderer(rendererType: RendererType): GameMapRenderer = ???

    override def getRoundProcessor: GameRoundProcessor = ???

    override def getReferee: GameReferee = ???
}
