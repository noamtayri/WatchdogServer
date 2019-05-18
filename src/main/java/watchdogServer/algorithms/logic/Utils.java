package watchdogServer.algorithms.logic;

import watchdogServer.algorithms.entities.Location;
import watchdogServer.algorithms.entities.Stay;
import watchdogServer.algorithms.entities.Movement;
import watchdogServer.algorithms.seviceClasses.ActivityType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Utils {
    public static int testIndex = 0;

    public static void printTest(){
        System.out.println("test #" + testIndex++);
    }

    public static void printLocationList(List<Location> locationList){
        for(Location location : locationList){
            System.out.println(location);
        }
    }

    public static void printStayList(List<Stay> stayList){
        for(Stay stay : stayList){
            System.out.println(stay);
        }
    }

    public static void printMovementList(List<Movement> movementList){
        for(Movement movement : movementList){
            System.out.println(movement);
        }
    }

    public static void printMatrix(long [][] matrix){
        for(int rowIndex = 0; rowIndex < matrix.length; rowIndex++){
            System.out.print(ActivityType.values()[rowIndex] + "\t");
            for(int colIndex = 0; colIndex < matrix[rowIndex].length; colIndex++){
                System.out.print(matrix[rowIndex][colIndex] + "\t");
            }
            System.out.println();
        }
    }

    public static void writeSegmentsToFile(List<Stay> stayList, List<Movement> movementList) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("segments.txt"));

        int stayIndex = 0;
        int movementIndex = 0;
        while(stayIndex < stayList.size() && movementIndex < movementList.size()){
            Stay currentStay = stayList.get(stayIndex);
            Movement currentMovement = movementList.get(movementIndex);
            String str;
            if(currentStay.getStartTime().compareTo(currentMovement.getStartTime()) < 0){
                str = currentStay.toString();
                stayIndex++;
            }
            else{
                str = currentMovement.toString();
                movementIndex++;
            }
            writer.write(str);
        }

        while(stayIndex < stayList.size()){
            String str = stayList.get(stayIndex).toString();
            writer.write(str);
            stayIndex++;
        }

        while(movementIndex < movementList.size()){
            String str = movementList.get(movementIndex).toString();
            writer.write(str);
            movementIndex++;
        }

        writer.close();
    }
}
