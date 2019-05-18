package watchdogServer.algorithms.logic;

import watchdogServer.algorithms.entities.Location;
import watchdogServer.algorithms.entities.Position;
import watchdogServer.algorithms.seviceClasses.Cluster;
import watchdogServer.algorithms.seviceClasses.EstimatedArea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LocationPrediction {
    private static final int LOCATION_DEVIATION_IN_METERS = 5;
    private static final int TIME_DEVIATION_IN_MIN = 120;
    private static final int CLUSTERS_RADIUS_IN_METERS = 50;

    public static List<EstimatedArea> predictLocation() throws IOException {
    	Location lastKnownLocation = new Location(new Position(31.555526,34.601917), null); // basic example + long msTimeDiff = 300000
        long msTimeDiff = 300000; //to set deltaT manually (in testing cases)
        //long msTimeDiff = Math.abs((new Date()).getTime() - lastKnownLocation.getTime().getTime());
        System.err.println("msTimeDif = " + TimeUnit.MINUTES.convert(msTimeDiff, TimeUnit.MILLISECONDS));

        List<Location> equalLastKnownLocation, possibleMatch;

        equalLastKnownLocation = LocationPredictionService.getSameLocation(lastKnownLocation, LOCATION_DEVIATION_IN_METERS);

        if(equalLastKnownLocation.isEmpty()){
            System.err.println("there are not enough data1");
            return null;
        }

        possibleMatch = LocationPredictionService.getLocationsPlusDeltaT(equalLastKnownLocation, msTimeDiff, TIME_DEVIATION_IN_MIN);

        if(possibleMatch.isEmpty()){
            System.err.println("there are not enough data2");
            return null;
        }

        System.err.println("list1 size = " + equalLastKnownLocation.size());

        System.err.println("list2 size = " + possibleMatch.size());

        K_Means kMeans = new K_Means(possibleMatch, CLUSTERS_RADIUS_IN_METERS);
        List<Cluster> clusters = kMeans.run();

        System.err.println("clusters size = " + clusters.size());

        List<EstimatedArea> clientEstimateAreas = new ArrayList<>();
        double percentPerLocation = 100d / possibleMatch.size();
        for (Cluster cluster:clusters) {
            clientEstimateAreas.add(new EstimatedArea(cluster.numOfPoints * percentPerLocation, CLUSTERS_RADIUS_IN_METERS, cluster.center));
        }
        Collections.sort(clientEstimateAreas, new EstimateAreaComparator());
        return clientEstimateAreas;
    }
}
