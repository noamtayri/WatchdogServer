package watchdogServer.algorithms.logic;

import watchdogServer.algorithms.entities.Location;
import watchdogServer.algorithms.entities.Position;
import watchdogServer.algorithms.entities.Stay;
import watchdogServer.algorithms.entities.Movement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivitySegmentationService {
    private enum SegmentType {MOVEMENT, STAY}

    public final long MAX_TIME_INTERVAL_IN_MIN = 4;
    public final long MAX_TIME_GAP_IN_HOURS = 24;
    public final long STAY_DURATION_IN_MIN = 5;
    public final double ROAMING_DISTANCE_IN_METER = 100; //50

    private List<Stay> stayList;
    private List<Movement> movementList;

    public ActivitySegmentationService(){
        stayList = new ArrayList<>();
        movementList = new ArrayList<>();
    }

    public List<Stay> getStayList() {
        return stayList;
    }

    public List<Movement> getMovementList() {
        return movementList;
    }

    public double diameter(List<Location> locationList){
        double diameter = 0;
        for(int l1Index = 0; l1Index < locationList.size(); l1Index++){
            for(int l2Index = l1Index+1; l2Index < locationList.size(); l2Index++){
                double distance = LocationMethods.distance(locationList.get(l1Index).getPosition(),locationList.get(l2Index).getPosition());
                if(distance>diameter) {
                    diameter = distance;
                }
            }
        }

        return diameter;
    }

    public Position medoid(List<Location> locationList){
        List<Position> positionsList = Location.convertLocationsToPositions(locationList);
        return LocationMethods.midpoint(positionsList);
    }

    private int findNextPointIndexByStayDuration(List<Location> locationList) {
        Location currentLocation = locationList.get(0);

        boolean found = false;

        int locationIndex = 0;
        while(++locationIndex < locationList.size() && !found){
            //locationIndex++;
            double timeDiffInMinutes =
                    LocationMethods.timeDiffInMinutes(currentLocation, locationList.get(locationIndex));
            if(timeDiffInMinutes >= STAY_DURATION_IN_MIN){
                found = true;
            }
        }
        if(!found || locationIndex == locationList.size()){
            locationIndex--;
        }
        return locationIndex;
    }

    private void setMovement(List<Location> locationList, int locationIndex, SegmentType lastActivityType){
        Movement movement = new Movement();
        Location currentLocation = locationList.get(locationIndex);

        switch(lastActivityType){
            case STAY:
                movementList.add(movement);
                if( locationIndex > 0 ) {
                    Location previousLocation = locationList.get(locationIndex - 1);
                    if(LocationMethods.timeDiffInMinutes(currentLocation,previousLocation) < MAX_TIME_INTERVAL_IN_MIN) {
                        movement.addLocation(previousLocation);
                    }
                }
                break;
            case MOVEMENT:
                if( locationIndex > 0 ) {
                    Location previousLocation = locationList.get(locationIndex - 1);
                    if(LocationMethods.timeDiffInMinutes(currentLocation,previousLocation) > MAX_TIME_INTERVAL_IN_MIN) {
                        movementList.add(movement);
                    }
                    else{
                        int movementId = movementList.size() - 1;
                        movement = movementList.get(movementId);
                    }
                }
                break;
            default:
                //Do nothing
                break;
        }
        movement.addLocation(currentLocation);
    }

    private void setStay(List<Location> locationList, List<Location> stayList, int fromIndex, int toIndex){
        if(fromIndex > 0){
            Location firstLocation = locationList.get(fromIndex);
            Location previousLocation = locationList.get(fromIndex - 1);
            if(LocationMethods.distance(previousLocation.getPosition(), firstLocation.getPosition()) < ROAMING_DISTANCE_IN_METER){
                fromIndex--;
            }
        }
        Position stayPosition = medoid(stayList);
        Date stayStartTime = locationList.get(fromIndex).getTime();
        Date stayEndTime = locationList.get(toIndex).getTime();
        Stay stay = new Stay(stayPosition, stayStartTime, stayEndTime);
        this.stayList.add(stay);
    }

    private int findNextPointByRoamingDistance(List<Location> locationList, List<Location>stayCandidateList){
        int nextPointIndex = 0;
        boolean moveOverRoamingDistance = false;
        while( !moveOverRoamingDistance && (++nextPointIndex < locationList.size())) {
            stayCandidateList.add(locationList.get(nextPointIndex));
            double diameter = diameter(stayCandidateList);
            if((diameter > ROAMING_DISTANCE_IN_METER)){
                moveOverRoamingDistance = true;
                stayCandidateList.remove(stayCandidateList.get(stayCandidateList.size()-1));
            }
        }
        nextPointIndex--;
        return nextPointIndex;
    }

    public int findLocationSequence(List<Location> locationList, int currentIndex){
        int endIndex = currentIndex + 1;
        boolean found = false;
        while(endIndex < locationList.size() && !found){
            long timeDiffInHours = LocationMethods.timeDiffInHours(locationList.get(currentIndex), locationList.get(endIndex));
            if(timeDiffInHours > MAX_TIME_GAP_IN_HOURS){
                found = true;
            }
            else{
                currentIndex++;
                endIndex++;
            }
        }
        return endIndex;
    }

    public void segmentActivity(List<Location> locationList){
        //System.out.println("Segmenting Data...");
        SegmentType lastActivityType = SegmentType.STAY;

        int locationListSize = locationList.size();
        int locationIndex = 0;
        while(locationIndex < locationListSize){
            int endOfLocationSequence = findLocationSequence(locationList,locationIndex);
            List<Location> restLocationList = locationList.subList(locationIndex,endOfLocationSequence);

            int nextPointIndex = locationIndex +
                    findNextPointIndexByStayDuration(restLocationList);

            List<Location> stayCandidateList = new ArrayList<>(locationList.subList(locationIndex,nextPointIndex+1));

            if(diameter(stayCandidateList) > ROAMING_DISTANCE_IN_METER) {

                setMovement(locationList,locationIndex,lastActivityType);
                lastActivityType = SegmentType.MOVEMENT;

                locationIndex++;
            }
            else{
                nextPointIndex = locationIndex +
                        findNextPointByRoamingDistance(restLocationList,stayCandidateList);

                setStay(locationList, stayCandidateList, locationIndex, nextPointIndex);

                locationIndex = nextPointIndex + 1;
                lastActivityType = SegmentType.STAY;
            }
        }
        try {
            Utils.writeSegmentsToFile(stayList,movementList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
