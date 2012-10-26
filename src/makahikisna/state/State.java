package makahikisna.state;

import java.util.List;
import makahikisna.Event;
import makahikisna.Player;

public abstract class State {
  protected Player player;
  public abstract void processTimestampData(List<Event> events);
  
  public State(Player player) {
    this.player = player;
  }
}
