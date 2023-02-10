package functionComponents;

import java.awt.Dimension;
import java.io.Serializable;

import graphingCalculator.MathFunctions;

public class Point implements Serializable, Comparable<Point> {

	private static final long serialVersionUID = 1L;
	
	private double x, y;
	private int xFrameCoord, yFrameCoord;
	private int frameWidth, frameHeight;
	
	public Point(double x, double y, int frameWidth, int frameHeight, double referentialLimits[]) {
		this.x = x;
		this.y = y;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		
		calculateFrameCoords(referentialLimits[0], referentialLimits[1], referentialLimits[2], referentialLimits[3]);
	}

	public Point(int xFrameCoord, int yFrameCoord, int frameWidth, int frameHeight, double referentialLimits[]) {
		this.xFrameCoord = xFrameCoord;
		this.yFrameCoord = yFrameCoord;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		
		calculateReferentialCoords(referentialLimits[0], referentialLimits[1], referentialLimits[2], referentialLimits[3]);
	}
	
	public Point(int xFrameCoord, int yFrameCoord, Dimension size, double referentialLimits[]) {
		this(xFrameCoord, yFrameCoord, size.width, size.height, referentialLimits);
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public int getXFrameCoord() {
		return xFrameCoord;
	}

	public int getYFrameCoord() {
		return yFrameCoord;
	}
	
	public void roundCoords(int decimalPlaces) {
		x = MathFunctions.roundToDecimalPlaces(x, decimalPlaces);
		y = MathFunctions.roundToDecimalPlaces(y, decimalPlaces);
	}
	
	private void calculateFrameCoords(double xMin, double xMax, double yMin, double yMax) {
		double xCoordsRange = xMax-xMin;
		double yCoordsRange = yMax-yMin;
		double xPixelPerCoords = frameWidth/xCoordsRange;
		double yPixelPerCoords = frameHeight/yCoordsRange;
		
		xFrameCoord = (int) ((x-xMin)*xPixelPerCoords);
		yFrameCoord = (int) (frameHeight - (y-yMin)*yPixelPerCoords);
	}
	
	private void calculateReferentialCoords(double xMin, double xMax, double yMin, double yMax) {
		double xCoordsRange = xMax-xMin;
		double yCoordsRange = yMax-yMin;
		double xCoordsPerPixel = xCoordsRange/frameWidth;
		double yCoordsPerPixel = yCoordsRange/frameHeight;

		x = xMin + xFrameCoord*xCoordsPerPixel;
		y = yMin + (frameHeight-yFrameCoord)*yCoordsPerPixel;
	}

	@Override
	public int compareTo(Point p2) {
		if(this.getX() > p2.getX()) return 1;
		if(this.getX() < p2.getX()) return -1;
		return 0;
	}
		
}
