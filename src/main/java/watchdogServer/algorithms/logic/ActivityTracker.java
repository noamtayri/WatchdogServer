package watchdogServer.algorithms.logic;

import watchdogServer.algorithms.entities.Location;
import watchdogServer.algorithms.seviceClasses.ActivityType;
import watchdogServer.algorithms.seviceClasses.Log;
import watchdogServer.dal.LocationDao;
import watchdogServer.logic.LocationEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityTracker {
	private static LocationDao locations;
	@Autowired
	public void setLocationDao(LocationDao locations) {
		ActivityTracker.locations = locations;
	}
	
    public static List<Log> analyzeData(Date from, Date to) throws IOException {
    	//List<Location> locationList = FileHandle.readFromJSON("C:\\Users\\USER\\Desktop\\Noam\\watchdog\\data\\json.json");
    	List<Location> locationList = new ArrayList<>();
    	for(LocationEntity locationEntity : locations.findAllByTimeBetween(from, to)) {
        	locationList.add(new Location(locationEntity));
        }
        
        ActivityTrackerService activityTrackerService = new ActivityTrackerService(locationList);
        return activityTrackerService.analyzeData().getLogList();
    }

    public static Map<ActivityType,Long> getStatistics(List<Log> logList) {
        return ActivityStatisticsService.calculateStatistics(logList);
    }
}