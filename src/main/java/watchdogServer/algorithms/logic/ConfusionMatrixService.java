package watchdogServer.algorithms.logic;

import watchdogServer.algorithms.seviceClasses.ActivityType;
import watchdogServer.algorithms.seviceClasses.TimeLine;

import java.util.Date;

public class ConfusionMatrixService {
    private long [][] confusionMatrix;

    public ConfusionMatrixService(int matrixSize){
        confusionMatrix = new long[matrixSize][matrixSize];
    }

    private void clearMatrix(){
        for(int rowIndex = 0; rowIndex < confusionMatrix.length; rowIndex++){
            for(int colIndex = 0; colIndex < confusionMatrix[rowIndex].length; colIndex++){
                confusionMatrix[rowIndex][colIndex] = 0;
            }
        }
    }

    private Date getNextTimeStamp(TimeLine actualTimeLine, TimeLine predictedTimeLine, Date currentDate){
        Date ret;
        Date nextActualTime = actualTimeLine.getNextTimeStamp(currentDate);
        Date nextPredictedTime = predictedTimeLine.getNextTimeStamp(currentDate);
        if(nextActualTime.compareTo(nextPredictedTime) < 0){
            ret = nextActualTime;
        }
        else{
            ret = nextPredictedTime;
        }
        return ret;
    }

    private void setActivityPeriod(TimeLine actualTimeLine, TimeLine predictedTimeLine, Date startTime, Date endTime){
        ActivityType actualActivity = actualTimeLine.getActivityAtTime(startTime);
        ActivityType predictedActivity = predictedTimeLine.getActivityAtTime(startTime);
        long durationInSeconds = LocationMethods.timeDiffInSeconds(startTime,endTime);
        confusionMatrix[actualActivity.ordinal()][predictedActivity.ordinal()] += durationInSeconds;
    }


    public long [][] getConfusionMatrix(TimeLine actualTimeLine, TimeLine predictedTimeLine){
        clearMatrix();
        Date startTime = actualTimeLine.getStartTime();
        while(startTime.compareTo(actualTimeLine.getEndTime()) < 0){
            Date endTime = getNextTimeStamp(actualTimeLine, predictedTimeLine, startTime);
            setActivityPeriod(actualTimeLine, predictedTimeLine, startTime, endTime);
            startTime = getNextTimeStamp(actualTimeLine, predictedTimeLine, endTime);
        }
        return confusionMatrix;
    }
}
