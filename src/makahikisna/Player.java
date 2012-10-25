package makahikisna;

import java.util.HashMap;
import java.util.Map;

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
  
  @Override
  public String toString() {
    return this.email;
  }

}
