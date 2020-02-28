using StarterBot.Enums;

namespace StarterBot.Entities.Commands
{
    public class ChangeLaneCommand: ICommand
    {
       public string direction;

       public ChangeLaneCommand(int laneIndicator) {
            if (laneIndicator == 1)
            {
                this.direction ="Right";
            }
            else
            {
                this.direction = "Left";
            }
        }


        public string RenderCommand()
        {
            return $"Change Lane:{direction}";
        }
    }
}
