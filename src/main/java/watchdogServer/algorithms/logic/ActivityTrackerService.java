package watchdogServer.algorithms.logic;
import watchdogServer.algorithms.entities.Location;
import watchdogServer.algorithms.seviceClasses.ActivityType;
import watchdogServer.algorithms.seviceClasses.TimeLine;

import java.util.ArrayList;
import java.util.List;


public class ActivityTrackerService {
    private FilterDataService filterDataService;
    private ActivitySegmentationService activitySegmentationService;
    private ActivityLabelingService activityLabelingService;
    private TimeLineService timeLineService;
    private ConfusionMatrixService confusionMatrixService;

    private List<Location> locationList;

    public ActivityTrackerService(List<Location> locationList){
        this.locationList = new ArrayList<>(locationList);
        filterDataService = new FilterDataService();
        activitySegmentationService = new ActivitySegmentationService();
        activityLabelingService = new ActivityLabelingService();
        timeLineService = new TimeLineService();
        confusionMatrixService = new ConfusionMatrixService(5);
    }

    public TimeLine analyzeData() {
        filterDataService.filterData(locationList);
        activitySegmentationService.segmentActivity(filterDataService.getFilteredLocationList());
        activityLabelingService.labelActivities(activitySegmentationService.getMovementList());
        timeLineService.createTimeLine(activitySegmentationService.getStayList(),activityLabelingService.getLabeledActivities());
        TimeLine timeLine = timeLineService.getAnalyzedTimeLine();

        return timeLine;
    }
}
