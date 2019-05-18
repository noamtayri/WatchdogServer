package watchdogServer.algorithms.seviceClasses;

import java.util.*;

import watchdogServer.algorithms.logic.LocationMethods;

public class TimeLine {
    private TreeMap<Date, Log> logMap;

    public TimeLine(){
        logMap = new TreeMap<>();
    }

    public Date getStartTime(){
        return logMap.firstEntry().getValue().getStartTime();
    }

    public Date getEndTime(){
        return logMap.lastEntry().getValue().getEndTime();
    }

    public ActivityType getActivityAtTime(Date time){
        Log currentLog = logMap.floorEntry(time).getValue();
        ActivityType activityType = currentLog.getActivityType();

        return activityType;
    }

    public Date getNextTimeStamp(Date currentTime){
        Date ret;
        if(currentTime.compareTo(logMap.firstEntry().getValue().getStartTime()) < 0){
            ret = logMap.firstEntry().getValue().getStartTime();
        }
        else{
            Log prevLog = logMap.floorEntry(currentTime).getValue();
            ret = prevLog.getEndTime();
            if(currentTime.compareTo(prevLog.getEndTime()) >= 0){
                Log lastLog = logMap.lastEntry().getValue();
                if(currentTime.compareTo(lastLog.getEndTime()) == 0){
                    ret = currentTime;
                }
                else{
                    Log nextLog = logMap.higherEntry(currentTime).getValue();
                    ret = nextLog.getStartTime();
                }
            }

        }
        return ret;
    }

    public void setUnrecordedLog(){
        List<Log> unrecorded = new ArrayList<>();
        List<Log> logList = new ArrayList<>(logMap.values());
        for(int logIndex = 0; logIndex < logList.size() - 1; logIndex++){
            Date startTime = logList.get(logIndex).getEndTime();
            Date endTime = logList.get(logIndex+1).getStartTime();
            long timeGap = LocationMethods.timeDiffInMinutes(startTime, endTime);
            if(timeGap > 4){
                unrecorded.add(new Log(startTime,endTime,ActivityType.UNRECORDED));
            }
        }

        for(Log log : unrecorded){
            logMap.put(log.getStartTime(),log);
        }
    }

    public boolean addLog(Log log){
        boolean success = true;

        if(!logMap.isEmpty()){
            Log lastLog = logMap.lastEntry().getValue();
            if(lastLog.getEndTime().compareTo(log.getStartTime()) > 0){ //if end time of last log is greater than the start of the new log
                success = false;
            }
        }
        if(success) {
            logMap.put(log.getStartTime(), log);
        }

        return success;
    }

    public List<Log> getLogList(){
        return new ArrayList<>(logMap.values());
    }
}
