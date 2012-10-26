package makahikisna.state;

import makahikisna.Color;
import makahikisna.MakahikiSNA;
import makahikisna.Player;
import static makahikisna.MakahikiSNA.*;

import java.util.List;
import makahikisna.Event;

/**
 * Create a fill color based upon the "intensity" of player effort during
 * this timestep.  Intensity goes from red to green and is scaled based upon
 * the maximum number of events in a timestep recorded in this dataset. 
 * Note: if one player goes crazy in a single timestep, then scaling will be
 * thrown off due to an outlier.
 * @author johnson
 *
 */
public class ActiveInGameFillState extends FillState {
  private int currColor = color.white;
  private int nonActiveColor = color.white;
  
  public ActiveInGameFillState(Player player) {
    super(player);
  }

  @Override
  public void processTimestampData(List<Event> events) {
    int maxEvents = Player.maxPlayerTimeStepEvents;
    int newHue = (int) (((double)events.size()/(double)maxEvents) * 100);
    if (!events.isEmpty()) {
      System.out.format("%-20s Max: %3d CurrEvents: %3d Hue: %3d%n",  
          player.getPlayerID(), Player.maxPlayerTimeStepEvents, events.size(), newHue);
    }
    //currColor = (events.isEmpty()) ? nonActiveColor : Color.setHue(currColor, newHue);
    currColor = Color.setHue(currColor, newHue);
    if (events.isEmpty()) {
      currColor = this.nonActiveColor;
    } else {
      currColor = Color.setHue(currColor, newHue);
    }
  }
  
  @Override
  public void setFillColor() {
    processing.fill(currColor);
  }
}
