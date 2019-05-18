package watchdogServer.algorithms.logic;

import watchdogServer.algorithms.entities.Location;
import watchdogServer.algorithms.entities.Position;
import watchdogServer.dal.LocationDao;
import watchdogServer.logic.LocationEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

@Service
public class LocationPredictionService {
	private static LocationDao locations;
	@Autowired
	public void setLocationDao(LocationDao locations) {
		LocationPredictionService.locations = locations;
	}
	
	public static List<Location> getSameLocation(Location location, int locationDeviationInMeters) {
		List<Location> result = new ArrayList<>();
		Point point = new Point(location.getPosition().getLat(), location.getPosition().getLon());
		Distance distance = new Distance((locationDeviationInMeters/1000d), Metrics.KILOMETERS);
		for(GeoResult<LocationEntity> locationEntity : locations.findByPositionNear(point, distance)) {
        	result.add(new Location(
        			new Position(
        					locationEntity.getContent().getPosition()[0], 
        					locationEntity.getContent().getPosition()[1]),
        			locationEntity.getContent().getTime()));
        }
		return result;
	}
	
	public static List<Location> getLocationsPlusDeltaT(List<Location> list, long time, int timeDeviationInSec){
		List<Location> result = new ArrayList<>();
		for(Location l1: list) {
			Date locationTimePlusDeltaT = new Date(l1.getTime().getTime() + time);
			Date timeLimit = new Date(locationTimePlusDeltaT.getTime() + (timeDeviationInSec * 1000));
			List<LocationEntity> locationsFromDB = locations.findAllByTimeBetween(locationTimePlusDeltaT, timeLimit);
			if(locationsFromDB.isEmpty())
				continue;
			else
				result.add(new Location(locationsFromDB.get(0)));
		}
		return result;
	}
	
    public static List<Location> getSameLocationFromFile(Location location, int locationDeviationInMeters) throws IOException {
        List<Location> data = FileHandle.readFromJSON("C:\\Users\\USER\\Desktop\\Noam\\watchdog\\data\\json.json");
        data = data.subList(0,50000);
        List<Location> result = new ArrayList<>();

        for (Location l: data) {
            if(LocationMethods.distance(location.getPosition(), l.getPosition()) <= locationDeviationInMeters){
                result.add(l);
            }
        }

        return result;
    }

    public static List<Location> getLocationsPlusDeltaTFromFile(List<Location> list, long time, int timeDeviationInSec) throws IOException {
        List<Location> data = FileHandle.readFromJSON("C:\\Users\\USER\\Desktop\\Noam\\watchdog\\data\\json.json");
        data = data.subList(0,50000);
        List<Location> result = new ArrayList<>();

        for (Location l1: list) {
            Date locationPlusTime = new Date(l1.getTime().getTime() + time);
            for (Location l2: data) {
                long msDiff = Math.abs(locationPlusTime.getTime() - l2.getTime().getTime());
                if(checkIfToInsertLocation(l2, result, msDiff, timeDeviationInSec))
                    result.add(l2);
            }
        }

        return result;
    }

    public static boolean checkIfToInsertLocation(Location l2, List<Location> result, long msDiff, int timeDeviationInSec){
        int acceptableTimeDiff = 0;
        while(acceptableTimeDiff < timeDeviationInSec){
            if((TimeUnit.SECONDS.convert(msDiff, TimeUnit.MILLISECONDS)) != acceptableTimeDiff)
                acceptableTimeDiff++;
            else {
                if(result.isEmpty())
                    return true;
                else{
                    long diffBetweenLastLocation = Math.abs(result.get(result.size()-1).getTime().getTime() - l2.getTime().getTime());
                    if((TimeUnit.SECONDS.convert(diffBetweenLastLocation, TimeUnit.MILLISECONDS) > 300)){
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }

}
