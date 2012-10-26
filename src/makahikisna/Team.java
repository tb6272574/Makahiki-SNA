package makahikisna;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import processing.core.PConstants;
import au.com.bytecode.opencsv.CSVReader;

import static makahikisna.MakahikiSNA.*;

/**
 * Represents a Team, which is a set of Rooms. 
 * @author Philip Johnson
 */
public class Team implements Comparable<Team> {

  /** Maintain a data structure containing all of the defined Teams. */
  public static Map<String, Team> teams = new TreeMap<String, Team>();

  // Instance variables.
  int center_x = 0;
  int center_y = 0;
  private String teamID;
  SortedSet<Room> rooms = new TreeSet<Room>();

  /**
   * Create a new Team with minimal information about it. 
   * @param processing The processing instance. 
   * @param teamID The team ID name.
   */
  public Team(MakahikiSNA processing, String teamID) {
    this.teamID = teamID;
    if (teams.containsKey(teamID)) {
      throw new RuntimeException("Attempt to create duplicate team: " + teamID);
    }
    Team.teams.put(teamID, this);
  }
  
  public static boolean isTeam (String teamID) {
    return teams.containsKey(teamID);
  }
  
  public String getTeamID() {
    return this.teamID;
  }
  
  public static Team getTeam(String teamID) {
    Team team = teams.get(teamID); 
    if (team == null) {
      throw new RuntimeException("Attempt to retrieve missing team: " + teamID);
    }
    return team;
  }
  
  public void addRoom(Room room) {
    this.rooms.add(room);
  }
  
  public Set<Room> getRooms() {
    return this.rooms;
  }
  
  public int getTeamRadius() {
    //Note: circumference = 2piR ~= numRooms * roomDiameter
    int numRooms = this.rooms.size();
    int teamCircumference = (numRooms + 3) * Room.roomDiameter;
    return (int) (teamCircumference / (2 * Math.PI));
  }
  
  
  public void draw() {
    // Make sure this team has had layout() call.
    if ((center_x == 0) && (center_y == 0)) {
      throw new RuntimeException("Attempt to draw team without prior layout(): " + teamID);
    }
    // Label the team
    drawTeamLabel();
   
    for (Room room : this.rooms) {
      room.draw();
    }
  }
  
  private void drawTeamLabel() {
    processing.textAlign(PConstants.CENTER, PConstants.CENTER);
    processing.fill(MakahikiSNA.color.black);
    processing.text(teamID, center_x, center_y);
  }


  /**
   * Initialization function that reads the teams.csv file in the data/ directory.
   * This file must use the Hale Aloha XLS file format.  
   * @param processor The processing instance.
   */
  public static void loadTeamData(MakahikiSNA processor) {
    // [1] Read all of the CSV teams file into a list of string arrays called defs.
    List<String[]> defs; 
    try {
      File dir = new File(processor.dataPath(""));
      CSVReader csvFile = new CSVReader(new FileReader(new File(dir, "teams.csv")));
      defs = csvFile.readAll();
      defs.remove(0);  // get rid of the header line.
      csvFile.close();
    }
    catch (Exception e) {
      System.out.println("Failure in loadTeamData: " + e);
      throw new RuntimeException();
    }
    
    // [2] Process each line and add/update the Team, Room, and Player instances from it.
    for (String[] def : defs) {
      String lastName = def[0];
      String firstName = def[1];
      String email = def[2];
      String buildingID = def[3];
      String roomNum = def[4];
      String floor = (roomNum.length() == 3) ? roomNum.substring(0, 1) : roomNum.substring(0, 2);
      if (floor.length() == 1) {
        floor = "0" + floor;
      }
      String teamID = buildingID + "-" + floor;
      String playerID = email;
      
      // Find or create the team.
      Team team = (Team.isTeam(teamID)) ? Team.getTeam(teamID) : new Team(processor, teamID);
      
      // Find or create the room, then add it to the Team instance.
      Room room = (Room.isRoom(teamID, roomNum)) ? Room.getRoom(teamID, roomNum) : 
        new Room(team, roomNum);
      team.addRoom(room);
      
      if (Player.isPlayer(playerID)) {
        throw new RuntimeException("Multiple Player definitions in the file.");
      }
      else {
        Player player = new Player(lastName, firstName, email, team, room);
        room.addPlayer(player);
      }
    }
  }
  
  public static void layoutTeamData(Team team, int center_x, int center_y) {
    team.center_x = center_x;
    team.center_y = center_y;
    int numRooms = team.rooms.size();
    int teamRadius = team.getTeamRadius();
    int theta = Math.round(360 / numRooms);
    for (int i = 0; i < numRooms; i++) {
      int degree = i * theta;
      int x = center_x + (int) (teamRadius * Math.cos(Math.toRadians(degree)));
      int y = center_y + (int) (teamRadius * Math.sin(Math.toRadians(degree)));
      // tell the room what its center point is.
      Room room = (Room)team.rooms.toArray()[i];
      room.setCoordinates(x, y);
      //System.out.format("Room: %s (%d, %d)%n", room.getRoomID(), x, y);
    }
  }
  
  public static void printTeamData() {
    System.out.format("Number Teams: %d, Rooms: %d, Players: %d%n", 
        Team.teams.size(), Room.rooms.size(), Player.players.size());
    
    System.out.println("*************************************************************");
    
    for (Team team : Team.teams.values()) {
      System.out.format("Team %s has %d rooms.%n", team.getTeamID(), team.getRooms().size());
      for (Room room : team.getRooms()) {
        System.out.print(room.getRoomID() + " ");
      }
      System.out.println();
    }

    System.out.println("*************************************************************");
    
    for (Room room : Room.rooms.values()){
      System.out.format("%s: %s%n", room.getRoomID(), room.getPlayers()); 
    }
  }
  
  @Override
  public int hashCode() {
    return this.teamID.hashCode();
  }
  
  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Team) && this.teamID.equals(((Team)obj).teamID);
  }
  
  @Override
  public int compareTo(Team otherTeam) {
    return this.teamID.compareTo(otherTeam.teamID);
  }
  
  @Override 
  public String toString() {
    return this.teamID;
  }

}