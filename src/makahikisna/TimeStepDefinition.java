package makahikisna;

import java.util.Calendar;
import java.util.Date;

/**
 * Provides a way to convert between time stamps in the data and time steps in the visualization.
 * @author Philip Johnson
 */
public class TimeStepDefinition {
  /** How long each timestep is in milliseconds. */
  private long intervalMillis;
  /** The timestamp of the first event found in the data set. */
  private long firstEventTimeMillis;
  /** The timestamp of the start of the first interval.  The top of the hour of the first event. */
  private long firstIntervalTimeMillis;
  @SuppressWarnings("unused")
  private long lastEventTimeMillis;
  
  private int lastTimeStep;
  
  public TimeStepDefinition(long firstEventTimeMillis, long lastEventTimeMillis) {
    this.firstEventTimeMillis = firstEventTimeMillis;
    this.lastEventTimeMillis = lastEventTimeMillis;
    // Now figure out the first interval start time 
    Calendar date = Calendar.getInstance();
    date.setTimeInMillis(this.firstEventTimeMillis);
    date.set(Calendar.MILLISECOND, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MINUTE, 0);
    this.firstIntervalTimeMillis = date.getTimeInMillis();
    this.intervalMillis = MakahikiSNA.timeStepIntervalMinutes * 1000 * 60;
    this.lastTimeStep = timeStamp2TimeStep(lastEventTimeMillis);
    System.out.format("Events: %s to %s, Timesteps: %s to %s%n", 
        new Date(firstEventTimeMillis), new Date(lastEventTimeMillis),
        timeStamp2TimeStep(firstEventTimeMillis), timeStamp2TimeStep(lastEventTimeMillis));
  }
  
  public int timeStamp2TimeStep(long tstampMillis) {
    return (int) ((tstampMillis - firstIntervalTimeMillis) / intervalMillis);
  }
  
  public Date timeStep2TimeStamp(int timestep) {
    long timestamp = firstIntervalTimeMillis + 
        (timestep * (MakahikiSNA.timeStepIntervalMinutes * 1000 * 60));
    return new Date(timestamp);
  }
  
  public int getLastTimeStep() {
    return this.lastTimeStep;
  }

}
