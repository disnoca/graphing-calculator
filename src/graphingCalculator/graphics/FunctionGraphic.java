package graphingCalculator.graphics;
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
	private Color color;
	private Function function;
	
	
	public FunctionGraphic(Dimension size, Function function, Color color) {
		super(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		this.function = function;
		this.color = color;
		
		g2d = this.createGraphics();
		
		drawFunction();
	}
	
	private void drawFunction() {
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(color);

		Polygon pol = new Polygon();
		ArrayList<Point> points = function.getPoints();
		
		for(Point point : points) {
			if(point == null) {
				g2d.drawPolyline(pol.xpoints, pol.ypoints, pol.npoints);
				pol.reset();
			}
			else
				pol.addPoint(point.getXFrameCoord(), point.getYFrameCoord());
		}
			
		g2d.drawPolyline(pol.xpoints, pol.ypoints, pol.npoints);
	}
	
	public String getExpression() {
		return function.getExpression();
	}

	public Color getColor() {
		return color;
	}
	
	public Function getFunction() {
		return function;
	}
	
}