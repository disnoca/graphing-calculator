package functionComponents;

import java.io.Serializable;

public class Point implements Serializable {

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

	public Point(int xFrameCoord, int yFrameCoord, int frameWidth, int frameHeight, double xCoordsRange, double yCoordsRange) {
		this.xFrameCoord = xFrameCoord;
		this.yFrameCoord = yFrameCoord;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		
		calculateReferentialCoords(xCoordsRange, yCoordsRange);
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
	
	private void calculateFrameCoords(double xMin, double xMax, double yMin, double yMax) {
		double xCoordsRange = xMax-xMin;
		double yCoordsRange = yMax-yMin;
		double xPixelPerCoords = frameWidth/xCoordsRange;
		double yPixelPerCoords = frameHeight/yCoordsRange;
		
		xFrameCoord = (int) ((x-xMin)*xPixelPerCoords);
		yFrameCoord = (int) (frameHeight - (y-yMin)*yPixelPerCoords);
	}
	
	private void calculateReferentialCoords(double xCoordsRange, double yCoordsRange) {
		double xCoordsPerPixel = xCoordsRange/frameWidth;
		double yCoordsPerPixel = yCoordsRange/frameHeight;

		x = xFrameCoord*xCoordsPerPixel;
		y = yFrameCoord*yCoordsPerPixel;
	}
		
}
