package watchdogServer.algorithms.logic;

import watchdogServer.algorithms.entities.Location;
import watchdogServer.algorithms.entities.Position;

import java.util.ArrayList;
import java.util.List;

public class FilterDataService {
    public final long MAX_TIME_INTERVAL_IN_MIN = 1;

    private final double[] LOCATIONS_WEIGHT = {1,1,1,1,1};

    private int filterSize;

    private List<Location> filteredLocationList;

    public FilterDataService(){
        filteredLocationList = new ArrayList<>();
        filterSize = LOCATIONS_WEIGHT.length;
    }

    public List<Location> getFilteredLocationList() {
        return filteredLocationList;
    }

    private double getFilterWeightSum(){
        double sum = 0;
        for(int weightIndex = 0; weightIndex < filterSize; weightIndex++){
            sum+=LOCATIONS_WEIGHT[weightIndex];
        }
        return sum;
    }

    private void extendEdge(List<Location> locationList,Location location,int index, int times){
        for(int locationIndex = 0; locationIndex < times; locationIndex++){
            locationList.add(index,new Location(location));
        }
    }

    private void extendEdges(List<Location> locationList){
        int extensionSize = filterSize / 2;

        extendEdge(locationList, locationList.get(0), 0, extensionSize);
        int locationListSize = locationList.size();
        extendEdge(locationList, locationList.get(locationListSize - 1), locationListSize, extensionSize);
    }

    private Location filterLocations(List<Location> locationList){
        double latSum = 0;
        double lonSum = 0;
        for(int locationIndex = 0; locationIndex < filterSize; locationIndex++){
            Position currentPosition = locationList.get(locationIndex).getPosition();
            double currentWeight = LOCATIONS_WEIGHT[locationIndex];
            latSum+= currentPosition.getLat()*currentWeight;
            lonSum+= currentPosition.getLon()*currentWeight;
        }
        double filterWeightSum = getFilterWeightSum();
        Position filteredPosition = new Position(latSum/filterWeightSum, lonSum/filterWeightSum);

        int mainLocationIndex = (filterSize / 2) + 1;
        Location mainLocation = locationList.get(mainLocationIndex);
        mainLocation.setPosition(filteredPosition);

        return mainLocation;
    }

    private void filterRecord(List<Location> locationList){
        //TODO: BUG - less one iteration
        for(int locationIndex = 0; locationIndex < locationList.size() - filterSize; locationIndex++) {
            List<Location> currentLocations = locationList.subList(locationIndex, locationIndex + filterSize);
            filteredLocationList.add(filterLocations(currentLocations));
        }
    }

    private int findEndOfRecordIndex(List<Location> locationList){
        int locationIndex = 0;
        boolean endOfRecord = false;
        while((locationIndex < locationList.size() - 1) && !endOfRecord){
            Location currentLocation = locationList.get(locationIndex);
            Location nextLocation = locationList.get(locationIndex + 1);

            if(LocationMethods.timeDiffInMinutes(currentLocation,nextLocation) > MAX_TIME_INTERVAL_IN_MIN) {
                endOfRecord = true;
            }
            else{
                locationIndex++;
            }
        }
        return locationIndex;
    }

    public void filterData(List<Location> locationList){
        //System.out.println("Filtering Data...");

        int locationIndex = 0;
        while( locationIndex < locationList.size()){
            int fromIndex = locationIndex;
            int toIndex = fromIndex + findEndOfRecordIndex(locationList.subList(fromIndex, locationList.size())) + 1;
            List<Location> currentRecord = new ArrayList<>(locationList.subList(fromIndex,toIndex));
            if(currentRecord.size() >= filterSize) {
                extendEdges(currentRecord);
                filterRecord(currentRecord);
            }
            locationIndex = toIndex;
        }
    }
}
