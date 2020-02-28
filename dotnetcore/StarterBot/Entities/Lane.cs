using Newtonsoft.Json;
using StarterBot.Enums;

namespace StarterBot.Entities
{
    public class Lane
    {
        private int lane { get; set; }
        private int block { get; set; }
        private PlayerIdentifier PlayerIdentifier { get; set; }

        public Lane()
        {
        }

        public Lane(int lane, int block, PlayerIdentifier playerIdentifier)
        {
            this.lane = lane;
            this.block = block;
            this.PlayerIdentifier = playerIdentifier;
        }

        public int getLane()
        {
            return lane;
        }

        public int getBlock()
        {
            return block;
        }

        public PlayerIdentifier getPlayerType()
        {
            return PlayerIdentifier;
        }
    }
}