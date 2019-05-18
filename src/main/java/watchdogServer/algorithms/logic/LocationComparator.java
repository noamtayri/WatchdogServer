package watchdogServer.algorithms.logic;

import watchdogServer.algorithms.entities.Location;

import java.util.Comparator;

public class LocationComparator implements Comparator<Location> {
    @Override
    public int compare(Location l1, Location l2) {
        return l1.getTime().compareTo(l2.getTime());
    }
}