package makahikisna.state;

import java.util.Random;
import static makahikisna.MakahikiSNA.*;

public class RegistrationStrokeState extends StrokeState {
  private int startTimeStep = new Random().nextInt(10);
  private boolean isStarted = false;

  @Override
  public void setStrokeColor() {
    processing.strokeWeight(3);
    processing.stroke(isStarted ? color.green : color.black);
  }

  @Override
  public void processTimestampData(int data) {
    this.isStarted = (data > startTimeStep);
  }

}
