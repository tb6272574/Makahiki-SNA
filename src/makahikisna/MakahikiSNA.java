package makahikisna;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import processing.core.PApplet;
import processing.core.PConstants;

/**
 * Top-level class that is responsible for running the visualization.
 * Also provides access to a bunch of global variables via public static instance variables. 
 * @author Philip Johnson
 */
public class MakahikiSNA extends PApplet {

  /** Might want the class to be serializable at some point. */
  private static final long serialVersionUID = 1L;
  /** The list of messages (i.e. event names) to be displayed during this visualization. */
  private MessageData messageData;
  /** The processing instance used to visualize the data. */
  public static MakahikiSNA processing;
  /** An instance of the Color utility class, used to simplify color manipulation. */
  public static Color color;
  /** The current timestep in the visualization. */
  public static int timeStep;
  /** How many minutes of game time corresponds to one second of real time in the visualization. */
  public static int timeStepIntervalMinutes = 60 * 24;
  /** How many milliseconds of real time correspond to a single time step. */
  public static int timeStepDurationMillis = 1000;
  /** Enables conversion between timestamps and time steps. Defined in EventData.loadEventData() */
  public static TimeStepDefinition timeStepDefinition;
  // TODO Make canvasWidth and canvasHeight a configuration parameter.
  public static int canvasWidth;
  public static int canvasHeight;
  
  public static long setupFinishedMillis;
  
  // TODO Don't hardcode team definition string arrays. 
  public static final String[] lehuaTeams = {
    "B-12", "B-11", "B-10", "B-09", "B-08", "B-07", "B-06", "B-05", "B-04", "B-03" };
  
  public static final String[] ilimaTeams = { 
    "A-12", "A-11", "A-10", "A-09", "A-08", "A-07", "A-06", "A-05", "A-04", "A-03" };

  public static final String[] lokelaniTeams = {
    "C-12", "C-11", "C-10", "C-09", "C-08", "C-07", "C-06", "C-05", "C-04", "C-03" };

  public static final String[] mokihanaTeams = {
    "D-12", "D-11", "D-10", "D-09", "D-08", "D-07", "D-06", "D-05", "D-04", "D-03" };
  
  public static DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

  @Override
  public void setup() {
    // Set up global variables
    processing = this;
    color = new Color();
    processing.ellipseMode(PConstants.CENTER);
    
    background(color.white);
    frameRate(2);
    
     // Create the font
    textFont(createFont("Verdana-Bold", 16));
    hint(ENABLE_NATIVE_FONTS);
    
    // Load player, room, and team data.
    Team.loadTeamData(this);
    
    // Load event data, create global TimeStepDefinition instance.
    EventData.loadEventData(this);
    
    // Load message data.
    // TODO Why is MessageData loaded differently from team and event data?
    this.messageData = new MessageData();
    
    
    // Load data on team points accumulated at each timestep.
    TeamPointsData.loadData();
    
    EnergyGoalData.loadEnergyGoalData(this);
    
    // Print out summary of events.
    //Player.printPlayerEventSummary();

    // TODO Need to define the layout to be used, and dynamically generate the teams. 
    layoutTeam(MakahikiSNA.ilimaTeams, 0);
    int loungeWidth = canvasWidth; // determined by the first call to layoutTeam. Non-optimal.
    layoutTeam(MakahikiSNA.lehuaTeams, loungeWidth);
    layoutTeam(MakahikiSNA.lokelaniTeams, 2 * loungeWidth);
    layoutTeam(MakahikiSNA.mokihanaTeams, 3 * loungeWidth);
    
    size(canvasWidth, canvasHeight);
    //Team.printTeamData();
    
    setupFinishedMillis = millis();
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
  
 
  @Override
  public void draw() {
    background(color.white);
    // Each timestep lasts timeStepDurationMillis of real time. Set the global timeStep variable.
    timeStep = (int)((millis() - setupFinishedMillis)/ timeStepDurationMillis);
    // Draw the teams and rooms.
    // TODO Another spot where we hardcode the team names.
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
    // Draw the team label data so that they are written over the social lines.
    for (String teamName : Arrays.asList(MakahikiSNA.lehuaTeams)) {
      Team.getTeam(teamName).drawTeamLabel();
    }
    for (String teamName : Arrays.asList(MakahikiSNA.ilimaTeams)) {
      Team.getTeam(teamName).drawTeamLabel();
    }
    for (String teamName : Arrays.asList(MakahikiSNA.lokelaniTeams)) {
      Team.getTeam(teamName).drawTeamLabel();
    }
    for (String teamName : Arrays.asList(MakahikiSNA.mokihanaTeams)) {
      Team.getTeam(teamName).drawTeamLabel();
    }
    
    drawStatusLine();
    //processing.stop();
    
    if (timeStep == timeStepDefinition.getLastTimeStep()) {
      processing.stop();
    }
  }

  public static void main(String _args[]) {
    PApplet.main(new String[] { makahikisna.MakahikiSNA.class.getName() });
  }
  
  private void drawStatusLine() {
    textAlign(LEFT, CENTER);
    processing.fill(color.black);
    Date currTime = timeStepDefinition.timeStep2TimeStamp(timeStep);
    int lastTimeStep = timeStepDefinition.getLastTimeStep();
    
    // First, draw the left side with the timestamp label.
    String timestampLabel = String.format("%s (Timestamp %d out of %d)", 
        dateFormat.format(currTime), timeStep, lastTimeStep);
    processing.text(timestampLabel, 1, canvasHeight - 15);
    
    // Second, draw the center portion to indicate any events. 
    String messageData = this.messageData.getMessage(timeStep);
    processing.text(messageData, canvasWidth - 800, canvasHeight - 15);
    
    // Third, draw the legend.
    processing.text("Low", canvasWidth - 300, canvasHeight - 15);
    processing.fill(color.black);
    processing.stroke(color.black);
    int circleX = canvasWidth - 250;
    int circleY = canvasHeight - 15;
    // Draw the black circle first
    processing.ellipse(circleX, circleY, Room.roomDiameter, Room.roomDiameter);
    // Now draw five colored ones. 
    for (int i = 0; i < 5; i++) {
      circleX += 30;
      int circleColor = Color.setHue(color.red, (i * 60));
      processing.fill(circleColor);
      processing.stroke(circleColor);
      processing.ellipse(circleX, circleY, Room.roomDiameter, Room.roomDiameter);
    }
    processing.fill(color.black);
    processing.stroke(color.black);
    processing.text("High", circleX + 20, circleY);
  }
  

}
