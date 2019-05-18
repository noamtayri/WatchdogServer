package watchdogServer.algorithms.logic;

import watchdogServer.algorithms.entities.Location;
import watchdogServer.algorithms.seviceClasses.Cluster;
import watchdogServer.algorithms.seviceClasses.EstimatedArea;

import java.util.Comparator;

public class EstimateAreaComparator implements Comparator<EstimatedArea> {
    @Override
    public int compare(EstimatedArea ea1, EstimatedArea ea2) {
        double d = ea1.getPercentage() - ea2.getPercentage();
        if(d < 0)
            return 1;
        if(d > 0)
            return -1;
        return 0;
    }
}