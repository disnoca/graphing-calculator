package graphplotter.graphics;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import functionComponents.Function;
import functionComponents.Point;

public class FunctionGraphic extends BufferedImage {
	
	private Graphics2D g2d;
	private Function function;
	
	
	public FunctionGraphic(Dimension size, Function function) {
		super(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		this.function = function;
		g2d = this.createGraphics();
		drawFunction();
	}
	
	public void drawFunction() {
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(function.getColor());

		Polygon pol = new Polygon();
		ArrayList<Point> points = function.getPoints();
		points.forEach(point -> pol.addPoint(point.getXFrameCoord(-10, 10), point.getYFrameCoord(-10, 10)));
		g2d.drawPolyline(pol.xpoints, pol.ypoints, pol.npoints);
	}
	
	public String getExpression() {
		return function.getExpression();
	}

	public Color getColor() {
		return function.getColor();
	}
	
}