package makahikisna;

public enum State {
  NONPLAYER(0), PLAYER(1), NOWPLAYING(2), RECENTLYPLAYED(3);
  
  private int numericValue;
  
  private State(int numericValue) {
    this.numericValue = numericValue;
  }
  
  public int getNum() {
    return this.numericValue;
  }
  
  public static State getState(int num){
    for (State state : State.values()) {
      if (state.getNum() == num) {
        return state;
      }
    }
    return NONPLAYER;
  }

  public int getColor() {
    switch (this) {
    case NONPLAYER:
      return MakahikiSNA.color.white;
    case PLAYER:
      return MakahikiSNA.color.red;
    case NOWPLAYING:
      return MakahikiSNA.color.blue;
    case RECENTLYPLAYED:
      return MakahikiSNA.color.green;
    default:
      return MakahikiSNA.color.black;
    }
  }

}
