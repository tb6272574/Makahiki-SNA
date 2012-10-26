package makahikisna.state;

import static makahikisna.MakahikiSNA.*;
import java.util.List;
import makahikisna.Event;

public class RegistrationStrokeState extends StrokeState {
  private boolean isStarted = false;

  @Override
  public void setStrokeColor() {
    processing.strokeWeight(3);
    processing.stroke(isStarted ? color.green : color.black);
  }

  @Override
  public void processTimestampData(List<Event> events) {
    if (!events.isEmpty()) {
      isStarted = true;
    }
  }

}
