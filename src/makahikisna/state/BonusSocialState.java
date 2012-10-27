package makahikisna.state;

import static makahikisna.MakahikiSNA.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import makahikisna.Event;
import makahikisna.Player;

public class BonusSocialState extends SocialState {
  
  private Set<Player> partners = new HashSet<Player>();
  
  public BonusSocialState(Player player) {
    super(player);
  }

  @Override
  public void processTimestampData(List<Event> events) {
    for (Event event : events) {
      if (event.hasPartner()) {
        partners.add(event.getPartner());
      }
    }
  }

  @Override
  public void drawLines() {
    processing.strokeWeight(1);
    processing.stroke(color.blue);
    for (Player partner : partners) {
      processing.line(player.getX(), player.getY(), partner.getX(), partner.getY());
    }

  }

 
}
