package watchdogServer.algorithms.entities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import watchdogServer.algorithms.entities.Position;
import watchdogServer.logic.LocationEntity;

public class Location {
    private Position position;
    private Date time;

    public Location(Position position, Date time) {
        this.position = position;
        this.time = time;
/*
        DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
        timeFormat.format(this.time);
*/
    }
    
    public Location(LocationEntity le) {
    	this.position = new Position(le.getPosition()[0], le.getPosition()[1]);
    	this.time = le.getTime();
    }

    public Location(Location location){
        position = new Position(location.getPosition().getLat(), location.getPosition().getLon());
        time = location.getTime();
    }

    public Location() {
        position = new Position();
        time = null;
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


    public static List<Position> convertLocationsToPositions(List<Location> locationList){
        List<Position> positionsList = new LinkedList<>();

        for(Location location : locationList){
            positionsList.add(location.getPosition());
        }

        return positionsList;
    }

    public String toString() {
        return position + "\ttime = " + time;
    }

    public void print(){
        System.out.println(position);
    }
}
