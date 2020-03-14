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
        private int maxSpeed = 9;

        public Bot(GameState newGameState)
        {
            gameState = newGameState;
        }

        public string Run()
        {
            MapPosition playerPosition = gameState.Player.Position;

            List<CellType> nextCells = getNextBlocks(playerPosition.Y, playerPosition.X, maxSpeed);

            if (nextCells.Contains(CellType.MUD))
            {
                return new ChangeLaneCommand(1)?.RenderCommand();
            }

            return new AccelerateCommand()?.RenderCommand();
        }

        private List<CellType> getNextBlocks(int lane, int block, int maxSpeed)
        {
            List<Lane[]> map = gameState.WorldMap;
            List<CellType> blockTypes = new List<CellType>();

            int startBlock = gameState.WorldMap[0][0].Position.X;

            Lane[] laneList = map[lane-1];

            for (int i = Math.Max(block - startBlock,0); i < Math.Min(block - startBlock + maxSpeed, laneList.Length); i++)
            {
                if (laneList[i] == null || laneList[i].SurfaceObject == CellType.FINISH)
                {
                    break;
                }
                blockTypes.Add(laneList[i].SurfaceObject);

            }

            return blockTypes;
        }


        private ICommand GetRandomCommand()
        {
            var random = new Random();

            return new AccelerateCommand();
        }
    }
}