package watchdogServer.algorithms.seviceClasses;

import java.util.Date;

public class Log {
    private Date startTime;
    private Date endTime;
    private ActivityType activityType;

    public Log() {
    }

    public Log(Date startTime, Date endTime, ActivityType activityType) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.activityType = activityType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }
}
