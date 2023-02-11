package graphingCalculator.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.List;

import functionComponents.IntegralInformation;
import functionComponents.Point;
import graphingCalculator.utils.RoundingUtils;

public class HighlightGraphic extends BufferedImage {
	
	private final int POINT_THICKNESS = 6;
	private final int LABEL_DECIMAL_PLACES = 3;
	
	private Graphics2D g2d;

	public HighlightGraphic(Dimension size, List<Point> pointHighlights) {
		super(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		
		g2d = this.createGraphics();
		
		g2d.setStroke(new BasicStroke(POINT_THICKNESS));
		g2d.setColor(Color.BLACK);
		for(Point p : pointHighlights)
			drawPointHighlight(p);
	}
	
	public HighlightGraphic(Dimension size, IntegralInformation integralInformation) {
		super(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		
		g2d = this.createGraphics();
		
		drawIntegralHighlight(integralInformation);
	}
	
	private void drawPointHighlight(Point p) {
		p.roundCoords(LABEL_DECIMAL_PLACES);
		
		int pointX = p.getXFrameCoord() - POINT_THICKNESS/2;
		int pointY = p.getYFrameCoord() - POINT_THICKNESS/2;
		g2d.drawOval(pointX, pointY, POINT_THICKNESS, POINT_THICKNESS);
		
		String label = "(" + p.getX() + ", " + p.getY() + ")";
		int labelX = p.getXFrameCoord() - label.length()*3 + 4;
		int labelY = p.getYFrameCoord() - POINT_THICKNESS*2;
		g2d.drawString(label, labelX, labelY);
	}
	
	private void drawIntegralHighlight(IntegralInformation integralInformation) {
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(Color.LIGHT_GRAY);
		
		Polygon integralPointsPol = new Polygon();
		List<Point> pointsToDraw = integralInformation.getVisiblePoints();
		
		for(Point p : pointsToDraw)
			integralPointsPol.addPoint(p.getXFrameCoord(), p.getYFrameCoord());
		
		Point firstDrawnPoint = integralInformation.getFirstDrawnPoint();
		Point lastDrawnPoint = integralInformation.getLastDrawnPoint();
		integralPointsPol.addPoint(lastDrawnPoint.getXFrameCoord(), lastDrawnPoint.getYFrameCoord());
		integralPointsPol.addPoint(firstDrawnPoint.getXFrameCoord(), firstDrawnPoint.getYFrameCoord());
		
		g2d.fill(integralPointsPol);
		
		g2d.setStroke(new BasicStroke(POINT_THICKNESS));
		g2d.setColor(Color.BLACK);
		if(integralInformation.lowerBoundIsVisible())
			drawPointHighlight(integralInformation.getLowerBoundPoint());
		if(integralInformation.upperBoundIsVisible())
			drawPointHighlight(integralInformation.getUpperBoundPoint());
		
		String integralResult = RoundingUtils.roundToDecimalPlacesStr(integralInformation.getResult(), LABEL_DECIMAL_PLACES);
		if(integralResult.equals("-0")) integralResult = "0";
		
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, integralResult.length()*7, 20);
		g2d.setColor(Color.BLACK);
		g2d.drawString(integralResult , 2, 15);
	}

}
