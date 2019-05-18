package  watchdogServer.algorithms.entities;

public class Position {
    private double lat;
    private double lon;

    public Position(){
        this(0,0);
    }

    public Position(double lat, double lon){
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "lat = " + lat + ", lon = " + lon;
    }
}
