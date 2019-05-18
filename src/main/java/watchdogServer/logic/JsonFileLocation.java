package watchdogServer.logic;

import java.util.Date;

public class JsonFileLocation {
	String id;
	Position position;
	Date time;
	
	public JsonFileLocation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JsonFileLocation(String id, Position position, Date time) {
		super();
		this.id = id;
		this.position = position;
		this.time = time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "JsonFileLocation [id=" + id + ", position=" + position + ", time=" + time + "]";
	}
	
}
