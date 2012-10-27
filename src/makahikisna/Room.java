package makahikisna;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static makahikisna.MakahikiSNA.*;

public class Room implements Comparable<Room> {
  
  public static Map<String, Room> rooms = new TreeMap<String, Room>();
  public static int roomDiameter = 20;
  public static int roomRadius = roomDiameter / 2;
  
  private List<Player> players = new ArrayList<Player>();
  private Team team;
  private String roomNumber;
  private int center_x = 0;
  private int center_y = 0;
  
  public Room(Team team, String roomNumber) {
    this.team = team;
    this.roomNumber = roomNumber;
    String id = makeID(team.getTeamID(), roomNumber);
    if (rooms.containsKey(id)) {
      throw new RuntimeException("Attempt to create duplicate room: " + id);
    }
    Room.rooms.put(id, this);
  }
  
  public void addPlayer(Player player) {
    this.players.add(player);
  }
  
  private static String makeID(String teamID, String roomNumber) {
    return teamID + "-" + roomNumber;
  }
  
  public String getRoomID () {
    return makeID(team.getTeamID(), roomNumber);
  }
  
  public static boolean isRoom (String teamID, String roomNumber) {
    return rooms.containsKey(makeID(teamID, roomNumber));
  }
  
  public static Room getRoom(String teamID, String roomNumber) {
    String roomID = makeID(teamID, roomNumber);
    Room room = rooms.get(roomID);
    if (room == null) {
      throw new RuntimeException("Attempt to retrieve missing room: " + roomID);
    }
    return room;
  }
  
  public void setCoordinates(int center_x, int center_y) {
    this.center_x = center_x;
    this.center_y = center_y;
  }
  
  public void draw() {
    if ((center_x == 0) && (center_y == 0)) {
      throw new RuntimeException("Attempt to draw room without layout(): " + getRoomID());
    }
    if (players.isEmpty()) {
      throw new RuntimeException("Attempt to draw an empty room: " + getRoomID());
    }

    // Currently all rooms must have exactly 1 or 2 players.
    if (players.size() == 1) {
      drawOnePlayerRoom(players.get(0));
    }
    else if (players.size() == 2) {
      drawTwoPlayerRoom(players.get(0), players.get(1)); 
    }
    else {
      throw new RuntimeException("Room with more than 2 players: " + this.getRoomID());
    }
  }
  
  public void drawSocialLines() {
    for (Player player : players) {
      player.updateSocialState(timeStep);
    }
  }
  
  public void drawTwoPlayerRoom(Player player1, Player player2) {
    // Left side.
    player1.updateStrokeState(timeStep);
    player1.updateFillState(timeStep);
    processing.arc(center_x, center_y, roomDiameter, roomDiameter, (float) Math.toRadians(90),
        (float) Math.toRadians(270));
    // Right side.
    player2.updateStrokeState(timeStep);
    player2.updateFillState(timeStep);
    processing.arc(center_x, center_y, roomDiameter, roomDiameter, (float) Math.toRadians(0),
        (float) Math.toRadians(90));
    processing.arc(center_x,  center_y, roomDiameter, roomDiameter, (float) Math.toRadians(270),
        (float) Math.toRadians(360));
    // draw the middle bar.
    processing.stroke(color.white);
    processing.line(center_x, (center_y + roomRadius), center_x, (center_y - roomRadius));
  }
  
  public void drawOnePlayerRoom(Player player) {
    // Left side.
    player.updateStrokeState(timeStep);
    player.updateFillState(timeStep);
    processing.arc(center_x, center_y, roomDiameter, roomDiameter, (float) Math.toRadians(90),
        (float) Math.toRadians(270));
    // Right side.
    processing.arc(center_x, center_y, roomDiameter, roomDiameter, (float) Math.toRadians(0),
        (float) Math.toRadians(90));
    processing.arc(center_x,  center_y, roomDiameter, roomDiameter, (float) Math.toRadians(270),
        (float) Math.toRadians(360));
  }
  
  public List<Player> getPlayers() {
    return this.players;
  }

  @Override
  public int hashCode() {
    return this.getRoomID().hashCode();
  }
  
  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Room) && this.getRoomID().equals(((Room)obj).getRoomID());
  }
  
  @Override
  public int compareTo(Room otherRoom) {
    return this.getRoomID().compareTo(otherRoom.getRoomID());
  }
  
  @Override
  public String toString() {
    return this.getRoomID();
  }

  public int getPlayerX(Player player) {
    //Only handle 1 or 2 players in a room right now.
    if (this.players.size() == 1) {
      return this.center_x;
    }
    else if (this.players.get(0).equals(player)) {
      return this.center_x - (roomDiameter / 4);
    } 
    else if (this.players.get(1).equals(player)) {
      return this.center_x + (roomDiameter / 4);
    }
    else {
      throw new RuntimeException("Attempted x value for non-room player." + player + " " + this);
    }
  }

  public int getPlayerY(Player player) {
    return this.center_y;
  }
}
