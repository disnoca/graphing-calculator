package graphingCalculator.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import functionComponents.Point;

public class HighlightGraphic extends BufferedImage {
	
	private final int POINT_THICKNESS = 6;
	private final int LABEL_DECIMAL_PLACES = 3;
	
	private Graphics2D g2d;

	public HighlightGraphic(Dimension size, List<Point> pointHighlights) {
		super(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		
		g2d = this.createGraphics();
		
		drawPointHighlights(pointHighlights);
	}
	
	public HighlightGraphic(Dimension size, Point integralAreaStartRoot, Point integralAreaEndRoot, List<Point> integralPoints, double integralResult) {
		super(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		
		g2d = this.createGraphics();
		
		drawIntegralHighlight(integralAreaStartRoot, integralAreaEndRoot, integralPoints);
	}
	
	private void drawPointHighlights(List<Point> pointHighlights) {
		g2d.setStroke(new BasicStroke(POINT_THICKNESS));
		g2d.setColor(Color.BLACK);
		
		for(Point p : pointHighlights) {
			p.roundCoords(LABEL_DECIMAL_PLACES);
			
			int pointX = p.getXFrameCoord() - POINT_THICKNESS/2;
			int pointY = p.getYFrameCoord() - POINT_THICKNESS/2;
			g2d.drawOval(pointX, pointY, POINT_THICKNESS, POINT_THICKNESS);
			
			String label = "(" + p.getX() + ", " + p.getY() + ")";
			int labelX = p.getXFrameCoord() - label.length()*3 + 4;
			int labelY = p.getYFrameCoord() - POINT_THICKNESS*2;
			g2d.drawString(label, labelX, labelY);
		}
	}
	
	private void drawIntegralHighlight(Point integralAreaStartRoot, Point integralAreaEndRoot, List<Point> integralPoints) {
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(Color.LIGHT_GRAY);
		
		Polygon integralPol = new Polygon();
		for(Point p : integralPoints)
			integralPol.addPoint(p.getXFrameCoord(), p.getYFrameCoord());
		
		integralPol.addPoint(integralAreaEndRoot.getXFrameCoord(), integralAreaEndRoot.getYFrameCoord());
		integralPol.addPoint(integralAreaStartRoot.getXFrameCoord(), integralAreaStartRoot.getYFrameCoord());
		
		g2d.fill(integralPol);
		
		// this if shouldn't be here, fix later
		if(integralPoints.size() > 2) {
			Point boundPoints[] = {integralPoints.get(0), integralPoints.get(integralPoints.size()-1)};
			drawPointHighlights(Arrays.asList(boundPoints));
		}
	}

}
