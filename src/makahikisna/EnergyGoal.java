package makahikisna;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implements an abstract data type representing an event.
 * @author Philip Johnson
 */
public class EnergyGoal implements Comparable<EnergyGoal> {
  
  Date date;
  List<Team> teams = new ArrayList<Team>();
  int numGoals;
  
  public EnergyGoal(String timestamp, String loungeString, String goalString) {
    // Process the timestamp, make sure it's legal.
    // Date example: 2012-09-04 13:57:18.067132
    SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
    try {
      this.date = formatter.parse(timestamp);
    }
    catch (Exception e) {
      throw new RuntimeException("Error parsing date string: " + timestamp);
    }
    
    // Process the team name, make sure it's a team.
    String building;
    if (loungeString.startsWith("Ilima")) {
      building = "A";
    } else if (loungeString.startsWith("Lehua")) {
      building = "B";
    } else if (loungeString.startsWith("Lokelani")) {
      building = "C";
    } else if (loungeString.startsWith("Mokihana")) {
      building = "D";
    } else {
      throw new RuntimeException("Unknown building: " + loungeString);
    }
    
    if (loungeString.endsWith("A")) {
      teams.add(Team.getTeam(building + "-03"));
      teams.add(Team.getTeam(building + "-04"));
    } else if (loungeString.endsWith("B")) {
      teams.add(Team.getTeam(building + "-05"));
      teams.add(Team.getTeam(building + "-06"));
    } else if (loungeString.endsWith("C")) {
      teams.add(Team.getTeam(building + "-07"));
      teams.add(Team.getTeam(building + "-08"));
    } else if (loungeString.endsWith("D")) {
      teams.add(Team.getTeam(building + "-09"));
      teams.add(Team.getTeam(building + "-10"));
    } else if (loungeString.endsWith("E")) {
      teams.add(Team.getTeam(building + "-11"));
      teams.add(Team.getTeam(building + "-12"));
    } else {
      throw new RuntimeException("Unknown floor: " + loungeString);
    }
    
    // Process the goalString.
    try {
      numGoals = Integer.parseInt(goalString);
    }
    catch (Exception e) {
      throw new RuntimeException("Illegal goal value: " + goalString);
    }
  }
  
  public long getTimestamp() {
    return this.date.getTime();
  }
  
  public List<Team> getTeams() {
    return this.teams;
  }
  
  public int getGoals() {
    return this.numGoals;
  }
  
  @Override
  public int hashCode() {
    return this.date.hashCode();
  }
  
  @Override
  public boolean equals(Object obj) {
    return (obj instanceof EnergyGoal) && this.date.equals(((EnergyGoal)obj).date);
  }
  
  @Override
  public int compareTo(EnergyGoal otherEnergyGoal) {
    return this.date.compareTo(otherEnergyGoal.date);
  }
  
  @Override
  public String toString() {
    return String.format("[EnergyGoal %s %s]", this.date, this.teams);
  }
}
