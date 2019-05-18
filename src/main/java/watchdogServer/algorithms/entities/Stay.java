package watchdogServer.algorithms.entities;

import java.util.Date;

public class Stay {
    private static int numOfStops = 0;

    private int id;
    private Position position;
    private Date startTime;
    private Date endTime;


    public Stay(Position position, Date startTime, Date endTime) {
        id = numOfStops++;
        this.position = position;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return /*"id = " + id + "\nposition:" + position +*/ "STAY:" + "\n\tstart time: "+startTime + "\n\t" +"end time: "+endTime + "\n";
    }
}
