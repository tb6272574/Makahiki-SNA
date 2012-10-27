package makahikisna.state;

import static makahikisna.MakahikiSNA.*;
import java.util.List;
import makahikisna.Color;
import makahikisna.Event;
import makahikisna.MakahikiSNA;
import makahikisna.Player;

/**
 * Sets the stroke color to a shade between red and green depending upon the 
 * cumulative number of events associated with this Player so far.  
 * Shade is scaled to the maximum number of events recorded for any player.
 */
public class CumulativeEventsStrokeState extends StrokeState {
  private int activeColor = color.red;
  private int nonActiveColor = color.black;
  private int cumulativeEvents = 0;
  
  public CumulativeEventsStrokeState(Player player) {
    super(player);
  }

  @Override
  public void processTimestampData(List<Event> events) {
    // Calculate cumulative events from scratch each time; we don't know how
    // many times this function will be called per time step.
    int totalEvents = 0;
    // Find out how many events this player has recorded so far. 
    for(int timestep = 0; timestep <= MakahikiSNA.timeStep; timestep++) {
      totalEvents += player.getEvents(timestep).size();
    }

    int maxEvents = Player.maxPlayerTotalEvents;
    int newHue = (int) (((double)totalEvents/(double)maxEvents) * 250); // from red to blue.
    activeColor = Color.setHue(activeColor, newHue);
    cumulativeEvents = totalEvents;
  }

  @Override
  public void setStrokeColor() {
    processing.strokeWeight(3);
    processing.stroke((cumulativeEvents > 0) ? activeColor : nonActiveColor);
  }
}
