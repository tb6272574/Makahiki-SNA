package makahikisna;

import processing.core.PApplet;
import processing.core.PConstants;

public class MakahikiSNA extends PApplet {

  /** Might want the class to be serializable at some point. */
  private static final long serialVersionUID = 1L;
  
  public static MakahikiSNA processing;
  public static Color color;
  public static long startTime;
  public static int timeStep;
  
  public static final int canvasWidth = 400;
  public static final int canvasHeight = 600;

  @Override
  public void setup() {
    // Set up global variables
    processing = this;
    color = new Color();
    startTime = millis();
    processing.ellipseMode(PConstants.CENTER);
    
    // Define the size of the canvas and the background color. 
    size(canvasWidth, canvasHeight);
    background(color.white);
    
     // Create the font
    textFont(createFont("Arial", 16));
    hint(ENABLE_NATIVE_FONTS);
    
    
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
    background(color.white);
    timeStep = (int)(millis() / 1000);
    // Draw the teams and rooms.
    Team lehua_09 = Team.getTeam("LE-09");
    lehua_09.draw();
    drawTimestampLabel();
  }

  public static void main(String _args[]) {
    PApplet.main(new String[] { makahikisna.MakahikiSNA.class.getName() });
  }
  
  private void drawTimestampLabel() {
    textAlign(LEFT, CENTER);
    processing.fill(color.black);
    processing.text("Timestamp: " + timeStep, 1, canvasHeight - 10);
  }
  

}
