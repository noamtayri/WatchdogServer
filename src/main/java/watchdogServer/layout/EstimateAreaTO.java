package watchdogServer.layout;

import watchdogServer.algorithms.seviceClasses.EstimatedArea;
import watchdogServer.logic.Position;

public class EstimateAreaTO {
	double percentage;
	int radius;
	Position center;
	public EstimateAreaTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public EstimateAreaTO(double percentage, int radius, Position center) {
		super();
		this.percentage = percentage;
		this.radius = radius;
		this.center = center;
	}
	public EstimateAreaTO(EstimatedArea estimateArea) {
		this.percentage = estimateArea.getPercentage();
		this.radius = estimateArea.getRadius();
		this.center = new Position(estimateArea.getCenter().getLat(), estimateArea.getCenter().getLon());
	}
	public double getPercentage() {
		return percentage;
	}
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public Position getCenter() {
		return center;
	}
	public void setCenter(Position center) {
		this.center = center;
	}
	@Override
	public String toString() {
		return "EstimateAreaTO [percentage=" + percentage + ", radius=" + radius + ", center=" + center + "]";
	}
	
}
