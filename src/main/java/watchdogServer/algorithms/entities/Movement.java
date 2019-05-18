package  watchdogServer.algorithms.entities;

import  watchdogServer.algorithms.logic.LocationMethods;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Movement {

    //private static int numOfTrips = 0;

    //private int tripId;
    private Date startTime;
    private Date endTime;
    private List<Location> locationList;

    public Movement(){
        //tripId = numOfTrips++;
        locationList = new ArrayList<>();
    }
    public Movement(Location location){
        this();
        addLocation(location); //set startTime & endTime according to the location time
    }

    public Movement(List<Location> locationList){
        this.locationList = new ArrayList<>(locationList);
        setStartTime(locationList.get(0).getTime());
        setEndTime(locationList.get(locationList.size() - 1).getTime());
    }

    public Date getStartTime() {
        return startTime;
    }

    private void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    private void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void addLocation(Location newLocation){
        if(locationList.isEmpty()){
            setStartTime(newLocation.getTime());
        }
        locationList.add(newLocation);
        setEndTime(newLocation.getTime());
    }

    public double getTotalDistance(){
        return LocationMethods.calculateTotalDistance(getLocationList());
    }


    @Override
    public String toString() {
        String retString = "MOVEMENT:\n\tstart time: " + startTime + "\n\tend time: " + endTime + "\n";

        return retString;
    }
}
