using System.Collections.Generic;
using StarterBot.Enums;

namespace StarterBot.Entities
{
    public class Player
    {
        public int Id { get; set; }
        public MapPosition Position { get; set; }
        public int Speed { get; set; }
        public State state { get; set; }
        public PowerUp[] PowerUps { get; set; }
        public bool Boosting { get; set; }
        public int BoostCounter { get; set; }
    }
}