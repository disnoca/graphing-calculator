package graphingCalculator.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class HighlighGraphic extends BufferedImage {
	
	private final int POINT_THICKNESS = 6;
	
	private Graphics2D g2d;

	public HighlighGraphic(Dimension size) {
		super(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		g2d = this.createGraphics();
	}
	
	public void drawHighlightPoint(double x, double y, int frameX, int frameY) {
		g2d.setStroke(new BasicStroke(POINT_THICKNESS));
		g2d.setColor(Color.BLACK);
		
		int pointX = frameX - POINT_THICKNESS/2;
		int pointY = frameY - POINT_THICKNESS/2;
		g2d.drawOval(pointX, pointY, POINT_THICKNESS, POINT_THICKNESS);
		
		String label = "(" + x + ", " + y + ")";
		int labelX = frameX - label.length()*3 + 4;
		int labelY = frameY - POINT_THICKNESS*2;
		g2d.drawString(label, labelX, labelY);
	}

}
