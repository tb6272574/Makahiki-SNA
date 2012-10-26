package makahikisna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import makahikisna.state.ActiveInGameFillState;
import makahikisna.state.BonusSocialState;
import makahikisna.state.FillState;
import makahikisna.state.RegistrationStrokeState;
import makahikisna.state.SocialState;
import makahikisna.state.StrokeState;

public class Player {
  
  public static Map<String, Player> players = new HashMap<String, Player>();
  
  @SuppressWarnings("unused")
  private String lastName;
  @SuppressWarnings("unused")
  private String firstName;
  private String email;
  @SuppressWarnings("unused")
  private Team team;
  @SuppressWarnings("unused")
  private Room room;
  
  /** Maps a timestep to a list of events. */
  private Map<Integer, List<Event>> events = new TreeMap<Integer, List<Event>>();
  
  private FillState fillState = new ActiveInGameFillState();
  private StrokeState strokeState = new RegistrationStrokeState();
  private SocialState socialState = new BonusSocialState();
  

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
    if (!events.containsKey(timestep)) {
      events.put(timestep, new ArrayList<Event>());
    }
    List<Event> eventList = events.get(timestep);
    eventList.add(event);
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
  
  public static void printPlayerEventSummary() {
    for (Player player : players.values()) {
      for (int timestep : player.events.keySet()) {
        System.out.format("%s Timestep: %d Num Events: %d%n", player.getPlayerID(), timestep,
            player.events.get(timestep).size());
      }
    }
  }
}
