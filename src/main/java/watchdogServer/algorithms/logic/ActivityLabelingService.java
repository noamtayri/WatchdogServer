package watchdogServer.algorithms.logic;

import watchdogServer.algorithms.entities.Location;
import watchdogServer.algorithms.entities.Movement;
import watchdogServer.algorithms.seviceClasses.ActivityCluster;
import watchdogServer.algorithms.seviceClasses.ActivityType;
import watchdogServer.algorithms.seviceClasses.LabeledMovement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityLabelingService {
    private final long RIDE_PART_IN_PERCENT_THRESHOLD = (long) 10;

    private ActivityClusteringService activityClusteringService;
    //List<Map<ActivityType, ActivityCluster>> labeledActivities;
    //Map<Movement,Map<ActivityType, ActivityCluster>> labeledActivities;
    List<LabeledMovement> labeledActivities;
    public ActivityLabelingService(){
        activityClusteringService = new ActivityClusteringService();
        //labeledActivities = new HashMap<>();
        labeledActivities = new ArrayList<>();
    }

    private void setRide(Map<ActivityType, ActivityCluster> activities, long totalMovementDurationInSec){
        for(ActivityType currentActivity : activities.keySet()){
            long currentDuration;
            if(currentActivity == ActivityType.RIDE){
                currentDuration = totalMovementDurationInSec;
            }
            else{
                currentDuration = 0;
            }
            activities.get(currentActivity).setActivityDurationInSec(currentDuration);
        }
    }

    private void filterRide(Map<ActivityType, ActivityCluster> activities){
        ActivityCluster ride = activities.get(ActivityType.RIDE);
        ActivityCluster strenuous = activities.get(ActivityType.STRENUOUS);

        strenuous.updateDuration(ride.getActivityDurationInSec());
        ride.setActivityDurationInSec(0);
    }

    private void setStrenuous(Map<ActivityType,ActivityCluster> activities, long totalMovementDurationInSec) {
        activities.get(ActivityType.STRENUOUS).setActivityDurationInSec(totalMovementDurationInSec);
        activities.get(ActivityType.MODERATE).setActivityDurationInSec(0);
    }

/*
    private Map<ActivityType, ActivityCluster> determineMovementType(Movement movement){
        Map<ActivityType, ActivityCluster> activities = new HashMap<>(activityClusteringService.clusterActivities(movement.getLocationList()));
        List<Location> locations = movement.getLocationList();
        long totalMovementDurationInSec = LocationMethods.timeDiffInSeconds(locations.get(0), locations.get(locations.size()-1));

        //TODO: this scenario is a BUG!!
        if(totalMovementDurationInSec <= 0){
            totalMovementDurationInSec = 1;
        }

        long rideDurationInSec = activities.get(ActivityType.RIDE).getActivityDurationInSec();
        double ridePart = ((double)(100*rideDurationInSec) / (double)totalMovementDurationInSec);
        if(ridePart > RIDE_PART_IN_PERCENT_THRESHOLD){
            setRide(activities, totalMovementDurationInSec);
        }
        else{
            filterRide(activities);
        }
        long strenuousActivityDuration = activities.get(ActivityType.STRENUOUS).getActivityDurationInSec();
        if(strenuousActivityDuration > 0){
            setStrenuous(activities, totalMovementDurationInSec);
        }
        return activities;
    }
*/

    private LabeledMovement determineMovementType(Movement movement){
        ActivityType dominantActivityType = ActivityType.MODERATE;
        Map<ActivityType, ActivityCluster> activities = new HashMap<>(activityClusteringService.clusterActivities(movement.getLocationList()));
        List<Location> locations = movement.getLocationList();
        long totalMovementDurationInSec = LocationMethods.timeDiffInSeconds(locations.get(0), locations.get(locations.size()-1));

        long rideDurationInSec = activities.get(ActivityType.RIDE).getActivityDurationInSec();
        double ridePart = ((double)(100*rideDurationInSec) / (double)totalMovementDurationInSec);
        if(ridePart > RIDE_PART_IN_PERCENT_THRESHOLD){
            dominantActivityType = ActivityType.RIDE;
        }
        else {
            long strenuousActivityDuration = activities.get(ActivityType.STRENUOUS).getActivityDurationInSec();
            if (strenuousActivityDuration > 0) {
                dominantActivityType = ActivityType.STRENUOUS;
            }
        }
        return new LabeledMovement(movement,dominantActivityType);
    }

    public void labelActivities(List<Movement> movementList){
        //System.out.println("Labeling Data...");
        for(Movement movement : movementList){
            labeledActivities.add(determineMovementType(movement));
        }
    }

    public  List<LabeledMovement> getLabeledActivities() {
        return labeledActivities;
    }
}
