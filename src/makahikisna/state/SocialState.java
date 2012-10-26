package makahikisna.state;

import makahikisna.Player;

public abstract class SocialState extends State {
  public abstract void drawLines();
  
  public SocialState(Player player) {
    super(player);
  }
}
