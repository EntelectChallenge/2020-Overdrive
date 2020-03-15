package za.co.entelect.challenge.game.contracts.bootstrapper

import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.bootstrapper.GameEngineBootstrapper
import za.co.entelect.challenge.game.contracts.commands.CommandFactory
import za.co.entelect.challenge.game.contracts.game.{CarGameEngine, CarGameRoundProcessor, CarMapGenerator, CarReferee, GameEngine, GameMapGenerator, GameReferee, GameRoundProcessor}
import za.co.entelect.challenge.game.contracts.renderer.GameMapRenderer
import za.co.entelect.challenge.game.contracts.renderer.RendererType
import za.co.entelect.challenge.game.contracts.renderer.CarGameRendererFactory

import scala.annotation.switch

class CarGameBootstrapper extends GameEngineBootstrapper {
  var carGameSeed: Int = 0;
  var carGameConfigPath: String = "";

  override def setSeed(seed: Int) = {
    carGameSeed = seed; 
  }

  override def setConfigPath(configPath: String): Unit = {
    carGameConfigPath = configPath;
    Config.load(configPath);
  }

  override def getGameEngine: GameEngine = {
    return new CarGameEngine;

  }

  override def getMapGenerator: GameMapGenerator = {
    return new CarMapGenerator(carGameSeed);
  }

  override def getRenderer(rendererType: RendererType): GameMapRenderer = {
    val carGameRendererFactory = new CarGameRendererFactory;

    rendererType match {
        case RendererType.TEXT  => return carGameRendererFactory.makeTextRenderer();
        case RendererType.JSON  => return carGameRendererFactory.makeJsonRenderer();
        case RendererType.CONSOLE  => return carGameRendererFactory.makeConsoleRenderer();
        case RendererType.CSV  => return carGameRendererFactory.makeCsvRenderer();
        case invalidRenderType  => throw new Exception("Invalid render type requested: " + invalidRenderType.toString())
    }

  }

  override def getRoundProcessor: GameRoundProcessor = {
    return new CarGameRoundProcessor;
  }

  override def getReferee: GameReferee = {
    return new CarReferee;
  }
}
