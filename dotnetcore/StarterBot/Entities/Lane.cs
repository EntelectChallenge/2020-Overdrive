using Newtonsoft.Json;
using StarterBot.Enums;

namespace StarterBot.Entities
{
    public class Lane
    {
        public MapPosition Position { get; set; }
        public CellType SurfaceObject { get; set; }
        public PlayerIdentifier PlayerIdentifier { get; set; }
    }
}