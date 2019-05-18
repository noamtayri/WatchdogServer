package watchdogServer.algorithms.seviceClasses;

import watchdogServer.algorithms.entities.Position;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EstimatedArea {
    private double percentage;
    private int radius;
    private Position center;

    public EstimatedArea(double percentage, int radius, Position center) {
        this.percentage = percentage;
        this.radius = radius;
        this.center = center;
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
        return "EstimatedArea {" +
                " percentage = " + round(percentage,2) + "%" +
                ", radius = " + radius + " meters" +
                ", center (" + center +
                ") }";

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


}
