package watchdogServer.logic;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "locations")
public class LocationEntity {
	
	@Id
	String id;
	@GeoSpatialIndexed
	double[] position;
	Date time;
	
	public LocationEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LocationEntity(double lat, double lon, Date time) {
		super();
		this.position = new double[2];
		position[0] = lat;
		position[1] = lon;
		this.time = time;
	}
	public LocationEntity(double lat, double lon) {
		super();
		this.position = new double[2];
		position[0] = lat;
		position[1] = lon;
		this.time = new Date();
	}
	
	public double[] getPosition() {
		return position;
	}
	public void setPosition(double lat, double lon) {
		this.position = new double[2];
		position[0] = lat;
		position[1] = lon;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return "LocationEntity [position=[lat=" + position[0] + " lon=" + position[1] + "], time=" + time + "]";
	}
	
}
