package makahikisna;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import au.com.bytecode.opencsv.CSVReader;

public class EventData {
  
  /**
   * Initialization function that reads the events.csv file in the data/ directory.
   * It converts this data into Event objects, determines the timestamp associated 
   * with each event, then updates each Player's instance with their associated event info.
   * @param processor The processing instance.
   */
  public static void loadEventData(MakahikiSNA processor) {
    // [1] Read all of the CSV event data into a list of string arrays called defs.
    List<String[]> eventLines; 
    try { 
      File dir = new File(processor.dataPath(""));
      CSVReader csvFile = new CSVReader(new FileReader(new File(dir, "events.csv")));
      eventLines = csvFile.readAll();
      eventLines.remove(0);  // get rid of the header line.
      csvFile.close();
    } 
    catch (Exception e) {
      System.out.println("Failure in loadEventData: " + e);
      throw new RuntimeException();
    } 
    
    // [2] Process each event and create a list of Events. Find the earliest event.
    // timestamp,user,last_name,group,room,action,action-url,partner,partner-group,partner-room
    List<Event> eventList = new ArrayList<Event>();
    long firstTimeStampMillis = (new Date()).getTime();
    long lastTimeStampMillis = 0;
    for (String[] eventLine : eventLines) {
      String timestamp = eventLine[0];
      String user = eventLine[1] + "@hawaii.edu";
      //String lastName = event[2];
      //String group = event[3];
      //String room = event[4];
      String action = eventLine[5];
      //String url = event[6];
      String partner = (eventLine.length > 7) ? eventLine[7] + "@hawaii.edu" : "";
      //String partnerGroup = (event.length > 7) ? event[8] : "";
      //String partnerRoom = (event.length > 7) ? event[9] : "";
      Event event;
      try {
        event = new Event(timestamp, user, action, partner);
        eventList.add(event);
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
      // update the firstTimeStamp if this event turns out to be the earliest event so far. 
      if (firstTimeStampMillis > event.getTimestamp()) {
        firstTimeStampMillis = event.getTimestamp();
      }
      if (lastTimeStampMillis < event.getTimestamp()) {
        lastTimeStampMillis = event.getTimestamp();
      }
    }
    
    // [3] Create the TimeStepDefinition instance, now that we know the start time.
    // Make it publicly available so the MakahikiSNA can refer to it.
    MakahikiSNA.timeStepDefinition = 
        new TimeStepDefinition(firstTimeStampMillis, lastTimeStampMillis);
    
    // [4] Loop through all the events, passing them to the appropriate Player to store. 
    for (Event event : eventList) {
      Player player = event.getPlayer();
      player.addEvent(MakahikiSNA.timeStepDefinition, event);
    }
  } 
}
