package za.co.entelect.challenge.game.contracts.bootstrapper

import za.co.entelect.challenge.game.contracts.bootstrapper.GameEngineBootstrapper
import za.co.entelect.challenge.game.contracts.game.{CarGameEngine, CarGameRoundProcessor, CarMapGenerator, CarReferee, GameEngine, GameMapGenerator, GameReferee, GameRoundProcessor}
import za.co.entelect.challenge.game.contracts.renderer.{CarGameRenderer, GameMapRenderer, RendererType}

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

  override def getRenderer(rendererType: RendererType): GameMapRenderer = {
    return new CarGameRenderer;
  }

  override def getRoundProcessor: GameRoundProcessor = {
    return new CarGameRoundProcessor;
  }

  override def getReferee: GameReferee = {
    return new CarReferee;
  }
}
