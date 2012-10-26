package makahikisna.state;

import makahikisna.Player;

public abstract class StrokeState extends State {
  public abstract void setStrokeColor();
  
  public StrokeState(Player player) {
    super(player);
  }
}
