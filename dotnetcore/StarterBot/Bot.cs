using System;
using System.Collections.Generic;
using System.Linq;
using StarterBot.Entities;
using StarterBot.Entities.Commands;
using StarterBot.Enums;

namespace StarterBot
{
    public class Bot
    {
        private readonly GameState gameState;

        public Bot(GameState newGameState)
        {
            gameState = newGameState;
        }

        public string Run()
        {
            ICommand command;

            command = GetRandomCommand();

            return command?.RenderCommand();
        }

        private ICommand GetRandomCommand()
        {
            var random = new Random();

            return new AccelerateCommand();
        }
    }
}