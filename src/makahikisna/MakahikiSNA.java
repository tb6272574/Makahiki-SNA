package makahikisna;

import processing.core.PApplet;

public class MakahikiSNA extends PApplet {

  /** Might want the class to be serializable at some point. */
  private static final long serialVersionUID = 1L;
  
  public static MakahikiSNA processing;
  public static Color color;
  public static long startTime;
  public static int timeStep;

  int canvasSize = 800;
  
  @Override
  public void setup() {
    processing = this;
    color = new Color();
    startTime = millis();
    size(800, 800);
    background(color.white);
    // Load player, room, and team data for all of Hale Aloha.
    Team.loadTeamData(this);
    // Layout only Lehua Floor 9
    Team lehua_09 = Team.getTeam("LE-09");
    Team.layoutTeamData(lehua_09, lehua_09.getTeamRadius() + Room.roomDiameter, 
        lehua_09.getTeamRadius() + Room.roomDiameter);
    //Team.printTeamData();
  }
  
  public static long getStartTime() {
    return startTime;
  }
  
  @Override
  public void draw() {
    timeStep = (int)(millis() / 1000); 
    Team lehua_09 = Team.getTeam("LE-09");
    lehua_09.draw();
    System.out.println(timeStep);
  }

  public static void main(String _args[]) {
    PApplet.main(new String[] { makahikisna.MakahikiSNA.class.getName() });
  }
  

}
