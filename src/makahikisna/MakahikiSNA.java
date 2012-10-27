package makahikisna;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import processing.core.PApplet;
import processing.core.PConstants;

public class MakahikiSNA extends PApplet {

  /** Might want the class to be serializable at some point. */
  private static final long serialVersionUID = 1L;
  
  private MessageData messageData;
  
  public static MakahikiSNA processing;
  public static Color color;
  public static long startTime;
  /** The current timestep in the visualization. */
  public static int timeStep;
  /** How many minutes of game time corresponds to one second of real time in the visualization. */
  public static int timeStepIntervalMinutes = 360;
  /** How many milliseconds of real time correspond to a single time step. */
  public static int timeStepDurationMillis = 1000;
  
  /** Initialized in EventData.loadEventData() */
  public static TimeStepDefinition timeStepDefinition;
  
  public static int canvasWidth;
  public static int canvasHeight;
  
  public static final String[] lehuaTeams = {
    "LE-12", "LE-11", "LE-10", "LE-09", "LE-08", "LE-07", "LE-06", "LE-05", "LE-04", "LE-03" };
  
  public static final String[] ilimaTeams = {
    "IL-12", "IL-11", "IL-10", "IL-09", "IL-08", "IL-07", "IL-06", "IL-05", "IL-04", "IL-03" };

  public static final String[] lokelaniTeams = {
    "LO-12", "LO-11", "LO-10", "LO-09", "LO-08", "LO-07", "LO-06", "LO-05", "LO-04", "LO-03" };

  public static final String[] mokihanaTeams = {
    "MO-12", "MO-11", "MO-10", "MO-09", "MO-08", "MO-07", "MO-06", "MO-05", "MO-04", "MO-03" };
  
  public static DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

  @Override
  public void setup() {
    // Set up global variables
    processing = this;
    color = new Color();
    startTime = millis();
    processing.ellipseMode(PConstants.CENTER);
    
    background(color.white);
    frameRate(2);
    
     // Create the font
    textFont(createFont("Arial", 12));
    hint(ENABLE_NATIVE_FONTS);
    
    // Load player, room, and team data for all of Hale Aloha.
    Team.loadTeamData(this);
    
    // Load event data, create global TimeStepDefinition instance.
    EventData.loadEventData(this);
    
    // Load message data.
    this.messageData = new MessageData();
    
    // Load data on team points accumulated at each timestep.
    TeamPointsData.loadData();
    
    // Print out summary of events.
    //Player.printPlayerEventSummary();

    layoutTeam(MakahikiSNA.lehuaTeams, 0);
    int loungeWidth = canvasWidth;
    layoutTeam(MakahikiSNA.ilimaTeams, loungeWidth);
    layoutTeam(MakahikiSNA.lokelaniTeams, 2 * loungeWidth);
    layoutTeam(MakahikiSNA.mokihanaTeams, 3 * loungeWidth);
    size(canvasWidth, canvasHeight);
    //Team.printTeamData();
  }
  
  public void layoutTeam (String[] teamNames, int team_x_offset) {
    int max_x = 0;
    int max_y = 0;
    int floorDiameter = Team.getTeam(teamNames[0]).getTeamRadius() + Room.roomDiameter;
    int padding = 10;
    for (int lounge = 0; lounge < 10; lounge = lounge + 2) {
      String teamName1 = teamNames[lounge];
      String teamName2 = teamNames[lounge + 1];
      int team1_center_x_offset = team_x_offset;
      int team1_center_y_offset = (lounge * floorDiameter) + padding;
      int team2_center_x_offset = team_x_offset + (2 * floorDiameter) + padding;
      int team2_center_y_offset = (lounge * floorDiameter) + padding;
      
      // (x, y) positions, and figure out dimensions for page.
      int team1_center_x = floorDiameter + team1_center_x_offset;
      max_x = Math.max(max_x, team1_center_x);
      int team1_center_y = floorDiameter + team1_center_y_offset;
      max_y = Math.max(max_y, team1_center_y);
      int team2_center_x = floorDiameter + team2_center_x_offset;
      max_x = Math.max(max_x, team2_center_x);
      int team2_center_y = floorDiameter + team2_center_y_offset;
      max_y = Math.max(max_y, team2_center_y);
      
      Team.layoutTeamData(Team.getTeam(teamName1), team1_center_x, team1_center_y);
      Team.layoutTeamData(Team.getTeam(teamName2), team2_center_x, team2_center_y);
    }
    
    MakahikiSNA.canvasHeight = max_y + 100;
    MakahikiSNA.canvasWidth = max_x + 100;
  }
  
  public static long getStartTime() {
    return startTime;
  }
  
  @Override
  public void draw() {
    background(color.white);
    // Each timestep lasts timeStepDurationMillis of real time. Set the global timeStep variable.
    timeStep = (int)(millis() / timeStepDurationMillis);
    // Draw the teams and rooms.
    for (String teamName : Arrays.asList(MakahikiSNA.lehuaTeams)) {
      Team.getTeam(teamName).draw();
    }
    for (String teamName : Arrays.asList(MakahikiSNA.ilimaTeams)) {
      Team.getTeam(teamName).draw();
    }
    for (String teamName : Arrays.asList(MakahikiSNA.lokelaniTeams)) {
      Team.getTeam(teamName).draw();
    }
    for (String teamName : Arrays.asList(MakahikiSNA.mokihanaTeams)) {
      Team.getTeam(teamName).draw();
    }
    drawTimestampLabel();
    
    if (timeStep == timeStepDefinition.getLastTimeStep()) {
      processing.stop();
    }
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

    String messageData = this.messageData.getMessage(timeStep);
    processing.text(messageData, canvasWidth - 500, canvasHeight - 10);
  }
  

}
