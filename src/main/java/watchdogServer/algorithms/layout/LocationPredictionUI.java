package watchdogServer.algorithms.layout;

import  watchdogServer.algorithms.logic.*;
import  watchdogServer.algorithms.entities.Location;
import  watchdogServer.algorithms.seviceClasses.EstimatedArea;

import java.io.IOException;
import java.util.*;

public class LocationPredictionUI {

    public static void predictLocationUI(Location lastKnownLocation) throws IOException {
        List<EstimatedArea> estimateAreas = LocationPrediction.predictLocation();
        System.out.println();
        if( estimateAreas == null){
            System.out.println("predictLocation algorithm failed");
            return;
        }
        //Collections.sort(estimateAreas, new EstimateAreaComparator());
        System.out.println("acording to the historical data, from the given location and time,\nthe probability(%) areas are:");
        for (EstimatedArea estimatedArea: estimateAreas) {
            System.out.println(estimatedArea);
        }
    }
}
