package makahikisna;

public class Color {
  
  // Basic colors
  public int blue;
  public int white;
  public int black;
  public int green;
  public int red;
  
  public Color() {
    this.black = MakahikiSNA.processing.color(0);
    this.red = MakahikiSNA.processing.color(255,0,0, 128);
    this.green = MakahikiSNA.processing.color(0, 255, 0);
    this.blue = MakahikiSNA.processing.color(0, 0, 255);
    this.white = MakahikiSNA.processing.color(255, 255, 255);  
  }
}
