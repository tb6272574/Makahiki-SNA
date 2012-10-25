package makahikisna.state;

import java.util.Random;
import static makahikisna.MakahikiSNA.*;

public class ActiveInGameFillState extends FillState {
  private int startTimeStep = 10 + new Random().nextInt(10);
  private int transparency = 10;
  private boolean isActive = false;

  @Override
  public void setFillColor() {
    processing.fill(isActive ? color.green(transparency) : color.white);
  }

  @Override
  public void processTimestampData(int data) {
    this.isActive = (data > startTimeStep);
    if (data > startTimeStep) {
      transparency += 5;
    }
  }

}
