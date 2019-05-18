package watchdogServer.layout;

import java.util.Date;

import watchdogServer.algorithms.seviceClasses.ActivityType;
import watchdogServer.algorithms.seviceClasses.Log;

public class LogTO {
	private Date startTime;
    private Date endTime;
    private String activityType;
	public LogTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LogTO(Date startTime, Date endTime, String activityType) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.activityType = activityType;
	}
	public LogTO(Log log) {
		this.startTime = log.getStartTime();
		this.endTime = log.getEndTime();
		this.activityType = log.getActivityType().toString();
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	@Override
	public String toString() {
		return "LogTO [startTime=" + startTime + ", endTime=" + endTime + ", activityType=" + activityType + "]";
	}
    
    
}
