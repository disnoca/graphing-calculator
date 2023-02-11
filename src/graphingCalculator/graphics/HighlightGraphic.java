package graphingCalculator.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import functionComponents.Point;

public class HighlightGraphic extends BufferedImage {
	
	private final int POINT_THICKNESS = 6;
	
	private Graphics2D g2d;

	public HighlightGraphic(Dimension size, List<Point> pointHighlights) {
		super(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		
		g2d = this.createGraphics();
		
		drawPointHighlights(pointHighlights);
	}
	
	private void drawPointHighlights(List<Point> pointHighlights) {
		g2d.setStroke(new BasicStroke(POINT_THICKNESS));
		g2d.setColor(Color.BLACK);
		
		for(Point p : pointHighlights) {
			int pointX = p.getXFrameCoord() - POINT_THICKNESS/2;
			int pointY = p.getYFrameCoord() - POINT_THICKNESS/2;
			g2d.drawOval(pointX, pointY, POINT_THICKNESS, POINT_THICKNESS);
			
			String label = "(" + p.getX() + ", " + p.getY() + ")";
			int labelX = p.getXFrameCoord() - label.length()*3 + 4;
			int labelY = p.getYFrameCoord() - POINT_THICKNESS*2;
			g2d.drawString(label, labelX, labelY);
		}
	}

}
