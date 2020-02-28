using System.Collections.Generic;
using StarterBot.Enums;

namespace StarterBot.Entities
{
    public class Player
    {
        public int Id { get; set; }
        public int Score { get; set; }
        public int Speed { get; set; }
        public State state { get; set; }
    }
}