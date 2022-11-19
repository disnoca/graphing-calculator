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
import functionComponents.ReferentialLimits;

public class FunctionGraphic extends BufferedImage {
	
	private ReferentialLimits referentialLimits;
	private Graphics2D g2d;
	private Color color;
	private Function function;
	
	
	public FunctionGraphic(Dimension size, ReferentialLimits referentialLimits, Function function, Color color) {
		super(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		this.referentialLimits = referentialLimits;
		this.function = function;
		this.color = color;
		
		g2d = this.createGraphics();
		drawFunction();
	}
	
	public void drawFunction() {
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(color);

		Polygon pol = new Polygon();
		ArrayList<Point> points = function.getPoints();
		double[] limits = referentialLimits.getLimits();
		
		for(Point point : points)
			pol.addPoint(point.getXFrameCoord(limits[0], limits[1]), point.getYFrameCoord(limits[2], limits[3]));
			
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