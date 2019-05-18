package watchdogServer.layout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.aggregation.DateOperators.IsoDateFromParts;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

import watchdogServer.algorithms.logic.ActivityTracker;
import watchdogServer.algorithms.logic.LocationPrediction;
import watchdogServer.algorithms.seviceClasses.EstimatedArea;
import watchdogServer.algorithms.seviceClasses.Log;
import watchdogServer.dal.LocationDao;
import watchdogServer.logic.JsonFileLocation;
import watchdogServer.logic.LocationEntity;
import watchdogServer.logic.Position;


/**
 * /watchdog/add/{lat}/{lon}/{date}
 * /watchdog/add/{lat}/{lon}
 * /watchdog/predict
 * /watchdog/activity/{from}/{to}
 * 
 * /watchdog/dev/push
 * /watchdog/dev/pull
 * /watchdog/dev/clean
 * /watchdog/dev/file
 */


@CrossOrigin
@RestController
public class Controller {
	private LocationDao locations;
	
	@Autowired
	public void setLocationDao(LocationDao locations) {
		this.locations = locations;
	}

	@RequestMapping(
			method=RequestMethod.GET,
			path="/watchdog/add/{lat}/{lon}/{date}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public LocationEntity addLocation(
			@PathVariable("lat") double lat,
			@PathVariable("lon") double lon,
			@PathVariable("date") Date date
			) throws Exception{
		//Position position = new Position(lat, lon);
		LocationEntity location = new LocationEntity(lat, lon, date);
		this.locations.save(location);
		return location;
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/watchdog/add/{lat}/{lon}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public LocationEntity addPosition(
			@PathVariable("lat") double lat,
			@PathVariable("lon") double lon
			) throws Exception{
		//Position position = new Position(lat, lon);
		LocationEntity location = new LocationEntity(lat, lon);
		this.locations.save(location);
		return location;
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/watchdog/predict",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String predictLocation () throws Exception {
		return "test test test - /watchdog/predict";
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/watchdog/activity/{from}/{to}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String activityTracker (
			@PathVariable("from") String from,
			@PathVariable("to") String to
			) throws Exception {
		return "test test test - /watchdog/activity/{from}/{to} - from = " + from + " - to = " + to;
	}
	
	//==========================================DEV API'S===================================================
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/watchdog/dev/push",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String testPush () throws Exception {
		Date d = new Date();
		//this.locations.save(new LocationEntity(new Position(1.1,2.2), d));
		//this.locations.save(new LocationEntity(new Point(1.1,2.2), d));
		this.locations.save(new LocationEntity(1.1,2.2, d));
		System.err.println(d);
		return "data saved";
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/watchdog/dev/pull",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String testPull () throws Exception {
		String s = "";
		for (LocationEntity location : this.locations.findAll()) {
			System.out.println(location);
			s = s + location + "\n";
		}
		return s;
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/watchdog/dev/clean",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String testClean () throws Exception {
		this.locations.deleteAll();
		return "all db deleted";
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/watchdog/dev/file",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String testFileToDB () throws Exception {
		
		List<JsonFileLocation> data = new ArrayList<>();
		
		ObjectMapper mapper = new ObjectMapper();
		File file = new File("C:\\Users\\USER\\Desktop\\Noam\\watchdog\\data\\json.json");
		
		data = mapper.readValue(file, new TypeReference<List<JsonFileLocation>>() {
		});
		
		for(JsonFileLocation location : data) {
			this.locations.save(new LocationEntity(location.getPosition().getLat(), location.getPosition().getLon(), location.getTime()));
		}
		return "data transfer from file to db";
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/watchdog/dev/query1",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String testQuery () throws Exception { // IMPORTANT!! all the DB is 3 hours early
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd-H-m-s");
		//Sat Mar 02 21:20:54 IST 2019
		
		long timeDeviationInSec = 30000;
		Date timeFromList = fmt.parse(2018 + "-" + 9 + "-" + 4 + "-" + 3 + "-" + 43 + "-" + 13); 
		Date timePlusDeltaT = new Date(timeFromList.getTime() + 30000);
		Date timeLimit = new Date(timePlusDeltaT.getTime() + timeDeviationInSec);
		
		System.err.println(); 
		System.err.println("timePlusDeltaT = " + timePlusDeltaT);
		System.err.println("timeLimit = " + timeLimit);
		System.err.println();
		
		/*
		String timeStr1 = 2018 + "-" + 9 + "-" + 4 + "-" + 3 + "-" + 43 + "-" + 13;
		String timeStr2 = 2018 + "-" + 9 + "-" + 4 + "-" + 3 + "-" + 43 + "-" + 28;
		
		
		
        Date d1 = fmt.parse(timeStr1);
        Date d2 = fmt.parse(timeStr2);
        */
        String s = "";
        for(LocationEntity location : this.locations.findAllByTimeBetween(timePlusDeltaT, timeLimit)) {
        	System.err.println(location);
        	s = s + location + "\n";
        }
        
        //LocationEntity [position=Position [lat=31.557246, lon=34.60207], time=Thu Sep 27 14:50:22 IDT 2018]
        
		return s;
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/watchdog/dev/query2",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String testQuery2 () throws Exception {
				
		//Position p1 = new Position(1.1, 2.2);
		Point p2 = new Point(20.555600, 12.603000);
		Distance d2 = new Distance(0.005, Metrics.KILOMETERS);
		
		String s = "";
        for(GeoResult<LocationEntity> location : this.locations.findByPositionNear(p2, d2)) {
        	System.err.println(location);
        	s = s + location + "\n";
        }
        
        //LocationEntity [position=Position [lat=31.557246, lon=34.60207], time=Thu Sep 27 14:50:22 IDT 2018]
        
		return s;
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/watchdog/dev/algo1",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public EstimateAreaTO[] testalgo1 () throws Exception {
        
		List<EstimatedArea> list1 = LocationPrediction.predictLocation();
		
		if(list1 == null)
			return null;
		
		System.err.println("location prediction algorithm execute");
		
		return list1
				.stream()
				.map(EstimateAreaTO::new)
				.collect(Collectors.toList())
				.toArray(new EstimateAreaTO[0]);
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/watchdog/dev/algo2/{from}/{to}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public LogTO[] testalgo2 (
			@PathVariable("from") String from,
			@PathVariable("to") String to
			) throws Exception {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd-H-m-s");
		Date fromDate = fmt.parse(from + "-0-0-0");
		Date toDate = fmt.parse(to + "-23-59-59");
		
		List<Log> list1 = ActivityTracker.analyzeData(fromDate, toDate);
		
		if(list1 == null)
			return null;
		
		System.err.println("activity tracker algorithm execute");
		
		return list1
				.stream()
				.map(LogTO::new)
				.collect(Collectors.toList())
				.toArray(new LogTO[0]);
	}
}
