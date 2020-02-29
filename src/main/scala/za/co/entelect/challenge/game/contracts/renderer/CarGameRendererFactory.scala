package za.co.entelect.challenge.game.contracts.renderer

import za.co.entelect.challenge.game.contracts.renderer.GameMapRenderer

class CarGameRendererFactory {
  
  def makeTextRenderer(): GameMapRenderer = {
    val textRenderer = new TextRenderer;
    return textRenderer;
  }

  def makeJsonRenderer(): GameMapRenderer = {
    val jsonRenderer = new JsonRenderer;
    return jsonRenderer;
  }

  def makeConsoleRenderer(): GameMapRenderer = {
    val consoleRenderer = new ConsoleRenderer;
    return consoleRenderer;
  }

  def makeCsvRenderer(): GameMapRenderer = {
    val csvRenderer = new CsvRenderer;
    return csvRenderer;
  }
}
