package makahikisna;

import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import au.com.bytecode.opencsv.CSVReader;
import static makahikisna.MakahikiSNA.*;

/**
 * Provides a method that reads the events.csv file in the data/ directory.
 * It converts this data into Event objects, determines the timestamp associated 
 * with each event, then updates each Player's instance with their associated event info.
 * @param processor The processing instance.
 */
public class EnergyGoalData {
  /** Maps Teams -> Timesteps -> Cumulative Number Energy Goals */ 
  public static Map<Team, Map<Integer, Integer>> energyGoals = 
      new HashMap<Team, Map<Integer, Integer>>();  

  public static void loadEnergyGoalData(MakahikiSNA processor) {
    // [1] Read all of the CSV event data into a list of string arrays called defs.
    // TODO Read from subdirectory. 
    List<String[]> eventLines; 
    try { 
      File dir = new File(processor.dataPath(""));
      CSVReader csvFile = new CSVReader(new FileReader(new File(dir, "energygoals.csv")));
      eventLines = csvFile.readAll();
      eventLines.remove(0);  // get rid of the header line.
      csvFile.close();
    } 
    catch (Exception e) {
      System.out.println("Failure in loadEnergyGoalData: " + e);
      throw new RuntimeException();
    } 
    
    // [2] Process each line and create a list of EnergyGoals. 
    // timestamp,loungeString,energy-goals
    for (String[] eventLine : eventLines) {
      String timestamp = eventLine[0];
      String loungeString = eventLine[1];
      String numGoals = eventLine[2];
      EnergyGoal energyGoal = new EnergyGoal(timestamp, loungeString, numGoals);
      
      // Build the data structure.
      for (Team team : energyGoal.getTeams()) {
        if (!energyGoals.containsKey(team)) {
          energyGoals.put(team, new HashMap<Integer, Integer>());
        }
        Map<Integer, Integer> timestamp2goal = energyGoals.get(team);
        int timestep = timeStepDefinition.timeStamp2TimeStep(energyGoal.getTimestamp());
        timestamp2goal.put(timestep, energyGoal.getGoals());
      }
    }
  } 
}
