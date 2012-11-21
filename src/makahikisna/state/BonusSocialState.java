package makahikisna.state;

import static makahikisna.MakahikiSNA.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import makahikisna.Event;
import makahikisna.Player;

public class BonusSocialState extends SocialState {
  
  private Set<Player> partners = new HashSet<Player>();
  
  private boolean hasReferralBonus = false;
  private boolean hasSocialBonus = false;
  
  public BonusSocialState(Player player) {
    super(player);
  }

  @Override
  public void processTimestampData(List<Event> events) {
    for (Event event : events) {
      if (event.hasPartner()) {
        partners.add(event.getPartner());
        if (event.getAction().equalsIgnoreCase("Referral")) {
          hasReferralBonus = true;
        }
        else if (event.getAction().equalsIgnoreCase("Submission")) {
          hasSocialBonus = true;
        }
      }
    }
  }

  @Override
  public void drawLines() {
    processing.strokeWeight(1);
    // code to make the social line a different color depending
    // upon action type.
    if (!hasReferralBonus && !hasSocialBonus) {
      processing.stroke(color.black);
    }
    else if (!hasReferralBonus & hasSocialBonus) {
      processing.stroke(color.red);
    }
    else if (hasReferralBonus && !hasSocialBonus) {
      processing.stroke(color.green);
    }
    else if (hasReferralBonus && hasSocialBonus) {
      processing.stroke(color.blue);
    }
    // Comment out the following line to get colors. 
    processing.stroke(color.grey);
    for (Player partner : partners) {
      processing.line(player.getX(), player.getY(), partner.getX(), partner.getY());
    }
  }
  
  /*
   * Draws a lines with arrows of the given angles at the ends.
   * Credit: http://www.openprocessing.org/sketch/7029
   * x0 - starting x-coordinate of line
   * y0 - starting y-coordinate of line
   * x1 - ending x-coordinate of line
   * y1 - ending y-coordinate of line
   * startAngle - angle of arrow at start of line (in radians)
   * endAngle - angle of arrow at end of line (in radians)
   * solid - true for a solid arrow; false for an "open" arrow
   */
  void arrowLine(float x0, float y0, float x1, float y1,
    float startAngle, float endAngle, boolean solid)
  {
    processing.line(x0, y0, x1, y1);
    if (startAngle != 0)
    {
      arrowhead(x0, y0, atan2(y1 - y0, x1 - x0), startAngle, solid);
    }
    if (endAngle != 0)
    {
      arrowhead(x1, y1, atan2(y0 - y1, x0 - x1), endAngle, solid);
    }
  }
  
  /*
   * Draws an arrow head at given location.
   * Credit: http://www.openprocessing.org/sketch/7029
   * x0 - arrow vertex x-coordinate
   * y0 - arrow vertex y-coordinate
   * lineAngle - angle of line leading to vertex (radians)
   * arrowAngle - angle between arrow and line (radians)
   * solid - true for a solid arrow, false for an "open" arrow
   */
  void arrowhead(float x0, float y0, float lineAngle,
    float arrowAngle, boolean solid)
  {
    float x2;
    float y2;
    float x3;
    float y3;
    final float SIZE = 8;
     
    x2 = x0 + SIZE * cos(lineAngle + arrowAngle);
    y2 = y0 + SIZE * sin(lineAngle + arrowAngle);
    x3 = x0 + SIZE * cos(lineAngle - arrowAngle);
    y3 = y0 + SIZE * sin(lineAngle - arrowAngle);
    if (solid)
    {
      processing.triangle(x0, y0, x2, y2, x3, y3);
    }
    else
    {
      processing.line(x0, y0, x2, y2);
      processing.line(x0, y0, x3, y3);
    } 
  }
}
