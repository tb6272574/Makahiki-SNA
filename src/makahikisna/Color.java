package makahikisna;

import static makahikisna.MakahikiSNA.*;

public class Color {
  
  // Basic colors
  public int blue;
  public int white;
  public int black;
  public int green;
  public int red;
  
  public Color() {
    this.black = processing.color(0);
    this.red = processing.color(255,0,0, 128);
    this.green = processing.color(0, 255, 0);
    this.blue = processing.color(0, 0, 255);
    this.white = processing.color(255, 255, 255);  
  }
  
  public int green(int transparency) {
    return processing.color(0, 255, 0, transparency);
  }
}
