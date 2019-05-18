package watchdogServer.algorithms.seviceClasses;

import watchdogServer.algorithms.entities.Movement;
import watchdogServer.algorithms.logic.LocationMethods;

import java.util.Date;

public class LabeledMovement extends Movement{
    private ActivityType activityType;

    public LabeledMovement(Movement movement, ActivityType activityType) {
        super(movement.getLocationList());
        this.activityType = activityType;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public long getActivityDuration(){return LocationMethods.timeDiffInSeconds(getStartTime(),getEndTime());
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }
}
