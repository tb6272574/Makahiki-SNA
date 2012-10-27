package makahikisna;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Event implements Comparable<Event> {
  
  Date date;
  Player player;
  String action;
  Player partner;
  
  public Event(String timestamp, String user, String action, String partner) throws Exception {
    // Make sure partner is a string. 
    partner = (partner == null) ? "" : partner;

    // Date example: 2012-09-04 13:57:18.067132
    SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.SSSSSS");
    try {
      this.date = formatter.parse(timestamp);
    }
    catch (Exception e) {
      throw new Exception("Error parsing date string: " + timestamp + "for user: " + user);
    }
    if (!Player.isPlayer(user)) {
      throw new Exception("Error: unknown player: " + user);
    }
    this.player = Player.getPlayer(user);
    this.action= action;
    // if partner is supplied.
    if (!partner.isEmpty()) {
      if (!Player.isPlayer(partner)) {
        throw new Exception("Error: unknown partner for player: " + user + " " + partner);
      }
      this.partner = Player.getPlayer(partner);
    }
  }
  
  public Player getPartner() {
    if (this.partner == null) {
      throw new RuntimeException("Attempt to retrieve nonpartner from: " + this);
    }
    return this.partner;
  }
  
  public boolean hasPartner() {
    return (this.partner != null);
  }
  
  public long getTimestamp() {
    return this.date.getTime();
  }
  
  public Player getPlayer() {
    return this.player;
  }
  
  public String getAction() {
    return this.action;
  }
  
  @Override
  public int hashCode() {
    return this.date.hashCode();
  }
  
  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Event) && this.date.equals(((Event)obj).date);
  }
  
  @Override
  public int compareTo(Event otherEvent) {
    return this.date.compareTo(otherEvent.date);
  }
  
  @Override
  public String toString() {
    return String.format("[Event %s %s %s]", this.date, this.player.getPlayerID(), this.action);
  }
}
