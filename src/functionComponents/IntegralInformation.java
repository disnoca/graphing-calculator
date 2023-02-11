package functionComponents;

import java.awt.Dimension;
import java.util.ArrayList;

public class IntegralInformation {
	
	private Dimension size;
	private ReferentialLimits referentialLimits;
	
	private ArrayList<Point> visiblePoints;
	private Point lowerBoundPoint, upperBoundPoint;
	
	// These points represent where to first put the "pencil" before following the visible points and where to release the "pencil" when drawing the integral shape.
	private Point firstDrawnPoint, lastDrawnPoint;
	
	private double integralResult;
	
	public IntegralInformation(Point lowerBoundPoint, Point upperBoundPoint, double integralResult, Dimension size, ReferentialLimits referentialLimits) {
		this.size = size;
		this.referentialLimits = referentialLimits;
		
		this.lowerBoundPoint = lowerBoundPoint;
		this.upperBoundPoint = upperBoundPoint;
		this.integralResult = integralResult;
	}
	
	public void setVisiblePointsAndUpdateBounds(ArrayList<Point> visiblePoints) {
		this.visiblePoints = visiblePoints;
		if(visiblePoints.size() == 0) return;
		
		double limits[] = referentialLimits.getLimits();
		
		Point firstVisiblePoint = visiblePoints.get(0);
		Point lastVisiblePoint = visiblePoints.get(visiblePoints.size()-1);
		
		firstDrawnPoint = new Point(firstVisiblePoint.getX(), 0, size.width, size.height, limits);
		lastDrawnPoint = new Point(lastVisiblePoint.getX(), 0, size.width, size.height, limits);
		
		lowerBoundPoint = new Point(lowerBoundPoint.getX(), lowerBoundPoint.getY(), size.width, size.height, limits);
		upperBoundPoint = new Point(upperBoundPoint.getX(), upperBoundPoint.getY(), size.width, size.height, limits);
	}
	
	public double getResult() {
		return integralResult;
	}
	
	public ArrayList<Point> getVisiblePoints() {
		return visiblePoints;
	}

	public Point getLowerBoundPoint() {
		return lowerBoundPoint;
	}

	public Point getUpperBoundPoint() {
		return upperBoundPoint;
	}

	public Point getLastDrawnPoint() {
		return lastDrawnPoint;
	}

	public Point getFirstDrawnPoint() {
		return firstDrawnPoint;
	}
	
	public boolean lowerBoundIsVisible() {
		return referentialLimits.pointIsVisible(lowerBoundPoint);
	}
	
	public boolean upperBoundIsVisible() {
		return referentialLimits.pointIsVisible(upperBoundPoint);
	}
	
}
