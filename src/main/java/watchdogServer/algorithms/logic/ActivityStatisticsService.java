package watchdogServer.algorithms.logic;

import watchdogServer.algorithms.entities.Stay;
import watchdogServer.algorithms.entities.Movement;
import watchdogServer.algorithms.seviceClasses.ActivityCluster;
import watchdogServer.algorithms.seviceClasses.ActivityType;
import watchdogServer.algorithms.seviceClasses.LabeledMovement;
import watchdogServer.algorithms.seviceClasses.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityStatisticsService {

    private static Map<ActivityType, Long> initStatistics(){
        Map<ActivityType, Long> activitiesDuration = new HashMap<>();
        activitiesDuration.put(ActivityType.MODERATE,(long)0);
        activitiesDuration.put(ActivityType.STRENUOUS,(long)0);
        activitiesDuration.put(ActivityType.RIDE,(long)0);
        return activitiesDuration;
    }

    public static Map<ActivityType, Long> calculateStatistics(List<Log> logList){
        Map<ActivityType, Long> activitiesDuration = initStatistics();
        for(Log log : logList){
            Long duration = LocationMethods.timeDiffInSeconds(log.getStartTime(),log.getEndTime());
            activitiesDuration.put(log.getActivityType(),duration);
        }
        return  activitiesDuration;
    }
}
