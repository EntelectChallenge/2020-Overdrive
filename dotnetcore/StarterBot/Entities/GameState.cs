using System.Collections.Generic;

namespace StarterBot.Entities
{
    public class GameState
    {
        public int CurrentRound { get; set; }
        public int MaxRounds { get; set; }
        public int MapSize { get; set; }
        public int ConsecutiveDoNothingCount { get; set; }

        public Player Player { get; set; }

        public Lane[] Lanes { get; set; }
    }
}