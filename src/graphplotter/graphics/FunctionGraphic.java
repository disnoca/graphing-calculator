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
import graphplotter.GraphPlotterFrame;

public class FunctionGraphic extends BufferedImage {
	
	private double minX, maxX, minY, maxY;
	
	private Graphics2D g2d;
	private Color color;
	private Function function;
	
	
	public FunctionGraphic(Dimension size, Function function, Color color) {
		super(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		this.function = function;
		this.color = color;
		
		minX = GraphPlotterFrame.STARTING_MINX;
		maxX = GraphPlotterFrame.STARTING_MAXX;
		minY = GraphPlotterFrame.STARTING_MINY;
		maxY = GraphPlotterFrame.STARTING_MAXY;
		
		g2d = this.createGraphics();
		drawFunction();
	}
	
	public void drawFunction() {
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(color);

		Polygon pol = new Polygon();
		ArrayList<Point> points = function.getPoints();
		
		for(Point point : points)
			pol.addPoint(point.getXFrameCoord(minX, maxX), point.getYFrameCoord(minY, maxY));
			
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