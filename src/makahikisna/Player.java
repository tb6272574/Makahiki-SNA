package makahikisna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import makahikisna.state.ActiveInGameFillState;
import makahikisna.state.BonusSocialState;
import makahikisna.state.FillState;
import makahikisna.state.CumulativeEventsStrokeState;
import makahikisna.state.SocialState;
import makahikisna.state.StrokeState;

public class Player {
  
  public static Map<String, Player> players = new HashMap<String, Player>();
  
  public static int maxPlayerTotalEvents = 0;
  
  public static int maxPlayerTimeStepEvents = 0;
  
  @SuppressWarnings("unused")
  private String lastName;
  @SuppressWarnings("unused")
  private String firstName;
  private String email;
  @SuppressWarnings("unused")
  private Team team;
  @SuppressWarnings("unused")
  private Room room;
  
  private int numEvents = 0;
  private int numTimeSteps = 0;
  
  /** Maps a timestep to a list of events. */
  private Map<Integer, List<Event>> events = new TreeMap<Integer, List<Event>>();
  
  private FillState fillState = new ActiveInGameFillState(this);
  private StrokeState strokeState = new CumulativeEventsStrokeState(this);
  private SocialState socialState = new BonusSocialState(this);
  

  public Player(String lastName, String firstName, String email, Team team, Room room) {
    this.lastName = lastName;
    this.firstName = firstName;
    this.email = email;
    this.team = team;
    this.room = room;
    if (players.containsKey(getPlayerID())) {
      throw new RuntimeException("Attempt to create duplicate Player: " + getPlayerID());
    }
    Player.players.put(getPlayerID(), this);
  }
  
  public String getPlayerID() {
    return this.email;
  }
  
  public static boolean isPlayer(String playerID) {
    return players.containsKey(playerID);
  }
  
  public static Player getPlayer(String playerID) {
    if (!players.containsKey(playerID)) {
      throw new RuntimeException("Attempt to retrieve non-existent player: " + playerID);
    }
    return players.get(playerID);
  }
  
  public void addEvent(TimeStepDefinition timestepdef, Event event) {
    int timestep = timestepdef.timeStamp2TimeStep(event.getTimestamp());
    // If this data is associated with a new timestep, set it up.
    if (!events.containsKey(timestep)) {
      events.put(timestep, new ArrayList<Event>());
      this.numTimeSteps++;
    }
    
    // Add this event to this timestep's list. 
    List<Event> eventList = events.get(timestep);
    eventList.add(event);
    
    // If this player's timestep total is the most seen so far, record that.
    if (eventList.size() > Player.maxPlayerTimeStepEvents) {
      Player.maxPlayerTimeStepEvents = eventList.size();
    }

    //Update the total number of events associated with this Player.
    this.numEvents++;
    
    // If this player's total events is the max seen so far, record that.
    if (this.numEvents > Player.maxPlayerTotalEvents) {
      Player.maxPlayerTotalEvents = this.numEvents;
    }

  }
  
  public List<Event> getEvents(int timestep) {
    return (events.containsKey(timestep)) ? 
        events.get(timestep) : new ArrayList<Event>();
  }
  
  @Override
  public String toString() {
    return this.email;
  }
  
  public void updateState(int timestep) {
    List<Event> events = getEvents(timestep);
    fillState.processTimestampData(events);
    fillState.setFillColor();
    socialState.processTimestampData(events);
    socialState.drawLines();
    strokeState.processTimestampData(events);
    strokeState.setStrokeColor();
  }
  
  public int getNumEvents() {
    return this.numEvents;
  }
  
  public int getNumTimeSteps() {
    return this.numTimeSteps;
  }
  
  public static void printPlayerEventSummary() {
    for (Player player : players.values()) {
      int timesteps = player.getNumTimeSteps();
      int events = player.getNumEvents();
      if (timesteps > 0) {
        System.out.format("%-20s Timesteps: %-3d Events: %-5d%n", 
            player.getPlayerID(), timesteps, events);
      }
    }
  }
}
