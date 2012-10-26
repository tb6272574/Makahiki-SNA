package makahikisna.state;

import makahikisna.Player;

public abstract class FillState extends State {
  public abstract void setFillColor();
  
  public FillState(Player player) {
    super(player);
  }
}
