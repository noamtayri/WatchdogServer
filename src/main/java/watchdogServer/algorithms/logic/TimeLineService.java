package watchdogServer.algorithms.logic;

import watchdogServer.algorithms.entities.Movement;
import watchdogServer.algorithms.entities.Stay;
import watchdogServer.algorithms.seviceClasses.*;

import java.util.List;
import java.util.Map;

public class TimeLineService {

    private TimeLine analyzedTimeLine;
    private TimeLine actualTimeLine;

    public TimeLineService(){
        analyzedTimeLine = new TimeLine();
        actualTimeLine = new TimeLine();
    }

    public TimeLine getAnalyzedTimeLine() {
        return analyzedTimeLine;
    }

    public TimeLine getActualTimeLine() {
        return actualTimeLine;
    }

    public void createTimeLine(List<Stay> stayList, List<LabeledMovement> labeledMovements){
        int stayIndex = 0;
        int movementIndex = 0;
        while(stayIndex < stayList.size() && movementIndex < labeledMovements.size()){
            Stay currentStay = stayList.get(stayIndex);
            LabeledMovement currentMovement = labeledMovements.get(movementIndex);

            Log currentLog;

            if(currentStay.getStartTime().compareTo(currentMovement.getStartTime()) < 0){
                currentLog = new Log(currentStay.getStartTime(), currentStay.getEndTime(), ActivityType.REST);
                stayIndex++;
            }
            else{
                currentLog = new Log(currentMovement.getStartTime(), currentMovement.getEndTime(), currentMovement.getActivityType());
                movementIndex++;
            }

            analyzedTimeLine.addLog(currentLog);
        }

        while(stayIndex < stayList.size()){
            Stay currentStay = stayList.get(stayIndex);
            Log currentLog = new Log(currentStay.getStartTime(), currentStay.getEndTime(), ActivityType.REST);
            analyzedTimeLine.addLog(currentLog);
            stayIndex++;
        }

        while(movementIndex < labeledMovements.size()){
            LabeledMovement currentMovement = labeledMovements.get(movementIndex);
            Log currentLog = new Log(currentMovement.getStartTime(), currentMovement.getEndTime(), currentMovement.getActivityType());
            analyzedTimeLine.addLog(currentLog);
            movementIndex++;
        }

        analyzedTimeLine.setUnrecordedLog();
    }

    public void createActualTimeLine(List<Log> logList){
        for(Log log : logList){
            actualTimeLine.addLog(log);
        }
    }

}
