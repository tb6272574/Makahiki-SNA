package makahikisna;

import static makahikisna.MakahikiSNA.processing;
import static makahikisna.MakahikiSNA.timeStepDefinition;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import au.com.bytecode.opencsv.CSVReader;

/**
 * Loads the data on how many points each user has at each timestep.
 * @author Philip Johnson
 */
public class TeamPointsData {
  
  public static void loadData() {
    // [1] Read all of the CSV data into a list of string arrays.
    List<String[]> playerPointLines; 
    try { 
      //TODO Read from subdirectory.
      File dir = new File(processing.dataPath(""));
      CSVReader csvFile = new CSVReader(new FileReader(new File(dir, "userPoints.csv")));
      playerPointLines = csvFile.readAll();
      playerPointLines.remove(0);  // get rid of the header line.
      csvFile.close();
    } 
    catch (Exception e) {
      System.out.println("Failure in TeamPointsData constructor: " + e);
      throw new RuntimeException();
    } 
    
    // [2] Process each message and build the player2timestep2points data structure.
    Map<Player, Map<Integer,Integer>> player2timestep2points = new TreeMap<Player, Map<Integer, Integer>>();
    // Format:  timestamp,user,points
    for (String[] eventLine : playerPointLines) {
      String timestamp = eventLine[0];
      String user = eventLine[1] + "@hawaii.edu";
      String pointString = eventLine[2];
      // Date example: 2012-09-04 13:00:00
      SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
      Date date;
      try {
        date = formatter.parse(timestamp);
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
      int points;
      try {
        points = Integer.parseInt(pointString);
      } 
      catch (Exception e) {
        throw new RuntimeException(e);
      }
      List<String> badUsers = new ArrayList<String>();
      Player player = (Player.isPlayer(user)) ? Player.getPlayer(user) : null;
      if (player == null) {
        badUsers.add(user);
      }
      else {
        int timestep = timeStepDefinition.timeStamp2TimeStep(date.getTime());
        // Make sure the data structure is initialized.
        if (!player2timestep2points.containsKey(player)) {
          player2timestep2points.put(player, new TreeMap<Integer, Integer>());
        }
        // record the latest value for this timestep.
        player2timestep2points.get(player).put(timestep, points);
      }
    }
//    // Print out our data structure.
//    for (Player player : player2timestep2points.keySet()) {
//      String outputline = "";
//      for (int timestep : player2timestep2points.get(player).keySet()) {
//        int points = player2timestep2points.get(player).get(timestep);
//        //outputline += " " + timestep + "(" + points + ")";
//        outputline += " " + points;
//      }
//      System.out.format("%20s %s%n", player.getPlayerID(), outputline);
//    }
    // Now loop through all the players we just found, and update their team for each timestep.
    for (Player player : player2timestep2points.keySet()) {
      Team team = player.getTeam();
      for (int timestep : player2timestep2points.get(player).keySet()) {
        int points = player2timestep2points.get(player).get(timestep);
        //System.out.format("Adding %3d points to team %s at timestep %d from player %s%n ", points, team, timestep, player);
        team.addPoints2Timestep(timestep, points);
      }
    }
    
//    // Print out the team data structure.
//    for (Team team : Team.teams.values()) {
//      String outputline = "";
//      for (int timestep = 0; timestep < 80; timestep++) {
//        outputline += " " + team.getPoints(timestep);
//      }
//      System.out.format("%s %s%n", team.getTeamID(), outputline);
//    }
  }

}
