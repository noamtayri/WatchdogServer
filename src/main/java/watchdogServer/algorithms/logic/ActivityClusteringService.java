package  watchdogServer.algorithms.logic;

import watchdogServer.algorithms.entities.Location;
import watchdogServer.algorithms.seviceClasses.ActivityCluster;
import watchdogServer.algorithms.seviceClasses.ActivityType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityClusteringService {
    public final double MIN_MODERATE_SPEED_IN_MPS = 0;
    public final double MAX_MODERATE_SPEED_IN_MPS = 3; //10.8 kph
    public final double MIN_STRENUOUS_SPEED_IN_MPS = 3;
    public final double MAX_STRENUOUS_SPEED_IN_MPS = 13; //45 kph
    public final double MIN_RIDE_SPEED_IN_MPS = 13;
    public final double MAX_RIDE_SPEED_IN_MPS = 55;

    private Map<ActivityType, ActivityCluster> activityClusters;

    public ActivityClusteringService(){
        activityClusters = new HashMap<>();
    }

    private void initClusters(){
        activityClusters.clear();

        activityClusters.put(ActivityType.MODERATE, new ActivityCluster(MIN_MODERATE_SPEED_IN_MPS,MAX_MODERATE_SPEED_IN_MPS));
        activityClusters.put(ActivityType.STRENUOUS, new ActivityCluster(MIN_STRENUOUS_SPEED_IN_MPS,MAX_STRENUOUS_SPEED_IN_MPS));
        activityClusters.put(ActivityType.RIDE, new ActivityCluster(MIN_RIDE_SPEED_IN_MPS,MAX_RIDE_SPEED_IN_MPS));
    }

    private void buildClusters(List<Location> locationList){
        for(int locationIndex = 0; locationIndex < locationList.size() - 1; locationIndex++){
            Location currentLocation = locationList.get(locationIndex);
            Location nextLocation = locationList.get(locationIndex + 1);
            double speed = LocationMethods.speedInMps(currentLocation,nextLocation);

            for(ActivityType currentActivity : activityClusters.keySet()) {
                ActivityCluster currentCluster = activityClusters.get(currentActivity);
                if(currentCluster.isSpeedInRange(speed)) {
                    currentCluster.updateDuration(LocationMethods.timeDiffInSeconds(currentLocation,nextLocation));
                }
            }
        }
    }

    public Map<ActivityType, ActivityCluster> clusterActivities(List<Location> locationList){
        initClusters();
        buildClusters(locationList);

        return activityClusters;
    }
}