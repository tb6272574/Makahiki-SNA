package makahikisna.state;

import makahikisna.Player;
import static makahikisna.MakahikiSNA.*;

import java.util.List;
import makahikisna.Event;

/**
 * Create a fill color for each player: white if no activity during this timestep, green otherwise.
 * @author Philip Johnson
 *
 */
public class ActiveInGameFillState extends FillState {
  private int currColor = color.white;
  
  public ActiveInGameFillState(Player player) {
    super(player);
  }

  @Override
  public void processTimestampData(List<Event> events) {
    if (events.isEmpty()) {
      currColor = color.white;
    }
    else {
      currColor = color.green;
    }
  }

//  public void oldProcessTimestampData(List<Event> events) {
//    int maxEvents = Player.maxPlayerTimeStepEvents;
//    int newHue = (int) (((double)events.size()/(double)maxEvents) * 100);
//    if (!events.isEmpty()) {
//      System.out.format("%-20s Max: %3d CurrEvents: %3d Hue: %3d%n",  
//          player.getPlayerID(), Player.maxPlayerTimeStepEvents, events.size(), newHue);
//    }
//    //currColor = (events.isEmpty()) ? nonActiveColor : Color.setHue(currColor, newHue);
//    currColor = Color.setHue(currColor, newHue);
//    if (events.isEmpty()) {
//      currColor = this.nonActiveColor;
//    } else {
//      currColor = Color.setHue(currColor, newHue);
//    }
//  }
  
  @Override
  public void setFillColor() {
    processing.fill(currColor);
  }
}
