package watchdogServer.algorithms.logic;

import watchdogServer.algorithms.entities.Location;
import watchdogServer.algorithms.entities.Position;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LocationMethods {

    // distance returned in METERS
    public static double distance(Position p1, Position p2) {
        int r = 6371; //Radius of the earth in km
        double dLat = LocationMethods.deg2rad(p2.getLat() - p1.getLat());
        double dLon = LocationMethods.deg2rad(p2.getLon() - p1.getLon());
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(LocationMethods.deg2rad(p1.getLat())) * Math.cos(LocationMethods.deg2rad(p2.getLat())) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = r * c;

        return d * 1000;
    }

    public static double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }

    public static long timeDiffInDays(Location l1, Location l2) {
        long msDiff = Math.abs(l2.getTime().getTime() - l1.getTime().getTime());
        return TimeUnit.DAYS.convert(msDiff, TimeUnit.MILLISECONDS);
    }

    public static long timeDiffInHours(Location l1, Location l2) {
        long msDiff = Math.abs(l2.getTime().getTime() - l1.getTime().getTime());
        return TimeUnit.HOURS.convert(msDiff, TimeUnit.MILLISECONDS);
    }

    public static long timeDiffInMinutes(Location l1, Location l2) {
        long msDiff = Math.abs(l2.getTime().getTime() - l1.getTime().getTime());
        return TimeUnit.MINUTES.convert(msDiff, TimeUnit.MILLISECONDS);
    }

    public static long timeDiffInMinutes(Date d1, Date d2) {
        long msDiff = Math.abs(d2.getTime() - d1.getTime());
        return TimeUnit.MINUTES.convert(msDiff, TimeUnit.MILLISECONDS);
    }

    public static long timeDiffInSeconds(Location l1, Location l2) {
        long msDiff = Math.abs(l2.getTime().getTime() - l1.getTime().getTime());
        return TimeUnit.SECONDS.convert(msDiff, TimeUnit.MILLISECONDS);
    }

    public static long timeDiffInSeconds(Date d1, Date d2) {
        long msDiff = Math.abs(d2.getTime() - d1.getTime());
        return TimeUnit.SECONDS.convert(msDiff, TimeUnit.MILLISECONDS);
    }


    public static long timeDiffInMilliSeconds(Location l1, Location l2) {
        return Math.abs(l2.getTime().getTime() - l1.getTime().getTime());
    }

    public static long timeDiffInMilliSeconds(Date d1, Date d2) {
        return Math.abs(d2.getTime() - d1.getTime());
    }



    public static double speedInMps(Location l1, Location l2){
        double distanceInMeter = distance(l1.getPosition(),l2.getPosition());
        long timeInSeconds = timeDiffInSeconds(l1,l2);

        double speedInMps = distanceInMeter / timeInSeconds;

        return speedInMps;
    }


    /**
     * Calculation of the geographic midpoint (also known as the geographic center,
     * or center of gravity) for two or more points on the earth's surface Ref:
     * http://www.geomidpoint.com/calculation.html
     *
     * @param points
     * @return
     */
    public static Position midpoint(List<Position> points) {
        double Totweight = 0;
        double xt = 0;
        double yt = 0;
        double zt = 0;
        for (Position point : points) {
            Double latitude = point.getLat();
            Double longitude = point.getLon();

            /**
             * Convert Lat and Lon from degrees to radians.
             */
            double latn = latitude * Math.PI / 180;
            double lonn = longitude * Math.PI / 180;

            /**
             * Convert lat/lon to Cartesian coordinates
             */
            double xn = Math.cos(latn) * Math.cos(lonn);
            double yn = Math.cos(latn) * Math.sin(lonn);
            double zn = Math.sin(latn);

            /**
             * Compute weight (by time) If locations are to be weighted equally,
             * set wn to 1
             */
            double years = 0;
            double months = 0;
            double days = 0;
            double wn = true ? 1 : (years * 365.25) + (months * 30.4375) + days;

            /**
             * Compute combined total weight for all locations.
             */
            Totweight = Totweight + wn;
            xt += xn * wn;
            yt += yn * wn;
            zt += zn * wn;
        }

        /**
         * Compute weighted average x, y and z coordinates.
         */
        double x = xt / Totweight;
        double y = yt / Totweight;
        double z = zt / Totweight;

        /**
         * If abs(x) < 10-9 and abs(y) < 10-9 and abs(z) < 10-9 then the
         * geographic midpoint is the center of the earth.
         */
        double lat = -0.001944;
        double lon = -78.455833;
        if (Math.abs(x) < Math.pow(10, -9) && Math.abs(y) < Math.pow(10, -9) && Math.abs(z) < Math.pow(10, -9)) {
        } else {

            /**
             * Convert average x, y, z coordinate to latitude and longitude.
             * Note that in Excel and possibly some other applications, the
             * parameters need to be reversed in the atan2 function, for
             * example, use atan2(X,Y) instead of atan2(Y,X).
             */
            lon = Math.atan2(y, x);
            double hyp = Math.sqrt(x * x + y * y);
            lat = Math.atan2(z, hyp);

            /**
             * Convert lat and lon to degrees.
             */
            lat = lat * 180 / Math.PI;
            lon = lon * 180 / Math.PI;
        }
        return new Position(lat, lon);
    }

    public static double calculateTotalDistance(List<Location> locationList){
        double distance = 0;
        for(int locationIndex = 0; locationIndex < locationList.size() - 1; locationIndex++){
            Position currentPosition = locationList.get(locationIndex).getPosition();
            Position nextPosition = locationList.get(locationIndex + 1).getPosition();
            distance+= distance(currentPosition,nextPosition);
        }
        return distance;
    }

}
