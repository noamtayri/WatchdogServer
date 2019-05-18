package watchdogServer.algorithms.seviceClasses;

public class ActivityCluster {
    private long activityDurationInSec;
    private double minSpeed;
    private double maxSpeed;

    public ActivityCluster(double minSpeed, double maxSpeed){
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        activityDurationInSec = 0;
    }

    public double getMinSpeed() {
        return minSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public long getActivityDurationInSec() {
        return activityDurationInSec;
    }

    public void setActivityDurationInSec(long activityDurationInSec) {
        this.activityDurationInSec = activityDurationInSec;
    }

    public void updateDuration(long durationInSec){
        setActivityDurationInSec(activityDurationInSec + durationInSec);
    }

    public boolean isSpeedInRange(Double speed){
        return (speed >= getMinSpeed()) && (speed <= getMaxSpeed());
    }

/*
    @Override
    public String toString() {
        return "ID: " + activityType;// + " speed: " + speed + " numOfPoints: " + numOfPoints;
    }
*/
}
