package makahikisna;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import au.com.bytecode.opencsv.CSVReader;
import static makahikisna.MakahikiSNA.*;

public class MessageData {
  
  private Map<Integer, String> messages = new HashMap<Integer, String>();
  
  /**
   * Initialization function that reads the messages.csv file in the data/ directory.
   * It determines the timestamp associated with each message and stores it in the messages
   * data structure for display during the visualization. 
   * @param processor The processing instance.
   */
  public MessageData() {
    // [1] Read all of the CSV message data into a list of string arrays.
    List<String[]> messageLines; 
    try { 
      File dir = new File(processing.dataPath(""));
      CSVReader csvFile = new CSVReader(new FileReader(new File(dir, "messages.csv")));
      messageLines = csvFile.readAll();
      messageLines.remove(0);  // get rid of the header line.
      csvFile.close();
    } 
    catch (Exception e) {
      System.out.println("Failure in MessageData constructor: " + e);
      throw new RuntimeException();
    } 
    
    // [2] Process each message and build the MessageData data structure. 
    // Format:  timestamp,message
    for (String[] eventLine : messageLines) {
      String timestamp = eventLine[0];
      String message = eventLine[1];
      // Date example: 2012-09-04 13:57:18.067132
      SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
      Date date;
      try {
        date = formatter.parse(timestamp);
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
      int timestep = timeStepDefinition.timeStamp2TimeStep(date.getTime());
      // There may be more than one message for a given timestep.
      String newMessage = (messages.containsKey(timestep)) ? 
          messages.get(timestep) + ", " + message : message;
      messages.put(timestep, newMessage);
    }
  } 
  
  public String getMessage(int timestep) {
    return (messages.containsKey(timestep)) ? messages.get(timestep) : "";
  }
}
