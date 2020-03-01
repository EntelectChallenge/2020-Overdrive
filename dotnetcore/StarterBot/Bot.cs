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
            ICommand command;
            MapPosition playerPosition = gameState.Player.Position;

            List<CellType> nextCells = getNextBlocks(playerPosition.Lane, playerPosition.BlockNumber, maxSpeed);

            if (nextCells.Contains(CellType.MUD))
            {
                return new ChangeLaneCommand(1)?.RenderCommand();
            }

            return new AccelerateCommand()?.RenderCommand();
        }

        private List<CellType> getNextBlocks(int lane, int block, int maxSpeed)
        {
            Dictionary<int, List<Lane>> map = getListMapStructure();
            List<CellType> blockTypes = new List<CellType>();

            int startBlock = gameState.Lanes[0].Position.BlockNumber;

            List<Lane> laneList = map[lane];

            for (int i = block - startBlock; i < Math.Min(block - startBlock + maxSpeed, laneList.Count); i++)
            {
                if (laneList[i] == null)
                {
                    break;
                }
                blockTypes.Add(laneList[i].Object);

            }

            return blockTypes;
        }

        private Dictionary<int, List<Lane>> getListMapStructure()
        {
            Dictionary<int, List<Lane>> map = new Dictionary<int, List<Lane>>();

            int totalLanes = gameState.Lanes.Length;
            Lane lastLane = gameState.Lanes[totalLanes - 1];

            int mapHeight = lastLane.Position.Lane;
            int mapWidth = totalLanes / mapHeight;

            for (int lane = 1; lane <= mapHeight; lane++)
            {
                List<Lane> blocks = new List<Lane>();
                for (int block = 0; block < mapWidth; block++)
                {
                    blocks.Add(gameState.Lanes[((lane - 1) * mapWidth) + block]);
                }

                map.Add(lane, blocks);
            }

            return map;
        }

        private ICommand GetRandomCommand()
        {
            var random = new Random();

            return new AccelerateCommand();
        }
    }
}