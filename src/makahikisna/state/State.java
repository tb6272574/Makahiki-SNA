package makahikisna.state;

import java.util.List;
import makahikisna.Event;

public abstract class State {
  public abstract void processTimestampData(List<Event> events);
}
