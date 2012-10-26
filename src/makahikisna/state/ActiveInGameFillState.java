package makahikisna.state;

import makahikisna.Color;
import static makahikisna.MakahikiSNA.*;

import java.util.List;
import makahikisna.Event;

public class ActiveInGameFillState extends FillState {
  private boolean isActive = false;
  private int activeColor = Color.lighten(color.green, 75);
  private int nonActiveColor = color.white;

  @Override
  public void setFillColor() {
    processing.fill(isActive ? activeColor : nonActiveColor);
  }

  @Override
  public void processTimestampData(List<Event> events) {
    if (!events.isEmpty()){
      activeColor = Color.brighten(activeColor, 2);
    }
   
  }

}
