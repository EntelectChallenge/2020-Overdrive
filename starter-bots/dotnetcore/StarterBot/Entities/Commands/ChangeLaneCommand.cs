using StarterBot.Enums;

namespace StarterBot.Entities.Commands
{
    public class ChangeLaneCommand: ICommand
    {
       public string direction;

       public ChangeLaneCommand(int laneIndicator) {
            if (laneIndicator == 1)
            {
                this.direction ="RIGHT";
            }
            else
            {
                this.direction = "LEFT";
            }
        }


        public string RenderCommand()
        {
            return $"TURN_{direction}";
        }
    }
}
