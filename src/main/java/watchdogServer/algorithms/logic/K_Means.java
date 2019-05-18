package watchdogServer.algorithms.logic;

import watchdogServer.algorithms.seviceClasses.Cluster;
import watchdogServer.algorithms.entities.Location;
import watchdogServer.algorithms.entities.Position;

import java.util.ArrayList;
import java.util.List;

public class K_Means {
    private int numOfClusters;
    private List<Point> pointsList = new ArrayList<>();
    private List<Cluster> clusters = new ArrayList<>();
    private double radius;

    public K_Means(List<Location> locationsList, double radius){
        for (Location location: locationsList) {
            pointsList.add(new Point(location));
        }
        numOfClusters = 1;
        this.radius = radius;
    }

    public List<Cluster> run(){
        boolean allClustersInRadius = false;
        initClusters();
        while(!allClustersInRadius){
            //initClusters();
            boolean pointsDidNotSwitchClusters = false;
            while(!pointsDidNotSwitchClusters){
                pointsDidNotSwitchClusters = groupPoints();
                calculateClustersCenters();
            }
            allClustersInRadius = isAllClusterInRadiusRange();
            if(!allClustersInRadius){
                //numOfClusters++;
                //clusters.clear();
                Cluster newCluster = generateNewCluster();
                if(newCluster == null){
                    System.out.println("error in genarating new cluster");
                    break;
                }
                clusters.add(newCluster);
                numOfClusters = clusters.size();
            }
        }
        return clusters;
    }

    private void initClusters(){
        for(int clusterIndex = 0; clusterIndex < numOfClusters; clusterIndex++){
            clusters.add(new Cluster(clusterIndex, pointsList.get(clusterIndex).position));
        }
    }

    private boolean groupPoints(){
        boolean noChange = true;
        for (Point point:
             pointsList) {
            double minDistance = LocationMethods.distance(point.position, clusters.get(0).center);
            int closestClusterIndex = 0;
            for(int clusterIndex = 0; clusterIndex < numOfClusters; clusterIndex++){
                double currentDistance = LocationMethods.distance(point.position, clusters.get(clusterIndex).center);
                if(currentDistance < minDistance){
                    minDistance = currentDistance;
                    closestClusterIndex = clusterIndex;
                }
            }
            if(point.myCluster != closestClusterIndex){
                point.myCluster = closestClusterIndex;
                noChange = false;
            }
        }
        return noChange;
    }

    private void calculateClustersCenters(){
        for (Cluster cluster:
             clusters) {
            List<Position> positionsInCurrentCluster = new ArrayList<>();
            for (Point point:
                 pointsList) {
                if(point.myCluster == cluster.id){
                    positionsInCurrentCluster.add(point.position);
                }
            }
            cluster.numOfPoints = positionsInCurrentCluster.size();
            cluster.center = LocationMethods.midpoint(positionsInCurrentCluster);
        }
    }

    private boolean isAllClusterInRadiusRange(){
        for (Cluster cluster: clusters) {
            for (Point point: pointsList) {
                if(point.myCluster == cluster.id){
                    if(LocationMethods.distance(point.position, cluster.center) > radius){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private Cluster generateNewCluster(){
        for (Point point: pointsList){
            for (Cluster cluster: clusters){
                if(point.myCluster == cluster.id) {
                    if (LocationMethods.distance(point.position, cluster.center) > radius) {
                        return new Cluster(clusters.size(), point.position);
                    }
                }
            }
        }
        return null;
    }


    private class Point{
        private Position position;
        private int myCluster;

        public Point(Location l){
            this.position = l.getPosition();
            myCluster = -1;
        }
    }

}
