package graphplotter;

public class Point {

	private double x, y;
	private int frameWidth, frameHeight;
	
	public Point(double x, double y, int frameWidth, int frameHeight) {
		this.x = x;
		this.y = y;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public int getXFrameCoord(double xMin, double xMax) {
		double coordsRange = xMax-xMin;
		double pixelPerCoords = frameWidth/coordsRange;
		
		return (int) ((x-xMin)*pixelPerCoords);
	}
	
	public int getYFrameCoord(double yMin, double yMax) {
		double coordsRange = yMax-yMin;
		double pixelPerCoords = frameHeight/coordsRange;
		
		// bigger y translates to smaller frame pixel
		return (int) (frameHeight - (y-yMin)*pixelPerCoords);
	}
		
}
