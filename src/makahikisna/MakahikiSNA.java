package makahikisna;

import java.text.DateFormat;
import java.util.Date;
import processing.core.PApplet;
import processing.core.PConstants;

public class MakahikiSNA extends PApplet {

  /** Might want the class to be serializable at some point. */
  private static final long serialVersionUID = 1L;
  
  public static MakahikiSNA processing;
  public static Color color;
  public static long startTime;
  /** The current timestep in the visualization. */
  public static int timeStep;
  /** How many minutes of game time corresponds to one second of real time in the visualization. */
  public static int timeStepIntervalMinutes = 30;
  public static TimeStepDefinition timeStepDefinition;
  
  public static final int canvasWidth = 400;
  public static final int canvasHeight = 600;
  
  public static DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

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
    frameRate(2);
    
     // Create the font
    textFont(createFont("Arial", 12));
    hint(ENABLE_NATIVE_FONTS);
    
    
    // Load player, room, and team data for all of Hale Aloha.
    Team.loadTeamData(this);
    
    // Load event data.
    EventData.loadEventData(this);
    
    // Print out summary of events.
    Player.printPlayerEventSummary();

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
    // Each timestep lasts one second. Update the global timeStep variable.
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
    Date currTime = timeStepDefinition.timeStep2TimeStamp(timeStep);
    int lastTimeStep = timeStepDefinition.getLastTimeStep();
    
    String timestampLabel = String.format("%s (Timestamp %d out of %d)", 
        dateFormat.format(currTime), timeStep, lastTimeStep);
    processing.text(timestampLabel, 1, canvasHeight - 10);
    if (timeStep == lastTimeStep) {
      processing.stop();
    }
  }
  

}
