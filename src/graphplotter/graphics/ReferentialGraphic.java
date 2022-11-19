package graphplotter.graphics;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map.Entry;

import functionComponents.Point;
import functionComponents.ReferentialLimits;

public class ReferentialGraphic extends BufferedImage {
	
	private int width, height;
	private ReferentialLimits referentialLimits;
	
	private Graphics2D g2d;
	
	
	public ReferentialGraphic(Dimension size, ReferentialLimits referentialLimits) {
		super(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		this.width = this.getWidth();
		this.height = this.getHeight();
		this.referentialLimits = referentialLimits;
		g2d = this.createGraphics();
		
		drawReferential();
	}

	private void drawReferential() {
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(Color.black);
		
		g2d.drawLine(0 , height/2, width, height/2);
		g2d.drawLine(width/2 ,0, width/2, height);
		
		drawReferenceLines();
	}
	
	private void drawReferenceLines() {
		HashMap<Point,String> xReferentialMarks = referentialLimits.getXReferentialMarks();
		HashMap<Point,String> yReferentialMarks = referentialLimits.getYReferentialMarks();
		double[] limits = referentialLimits.getLimits();
		
		for(Entry<Point,String> mark : xReferentialMarks.entrySet()) {
			Point p = mark.getKey();
			String label = mark.getValue();
			int xFrameCoord = p.getXFrameCoord(limits[0], limits[1]);
			int yFrameCoord = p.getYFrameCoord(limits[2], limits[3]);
			g2d.drawLine(xFrameCoord, yFrameCoord-5, xFrameCoord, yFrameCoord+5);
			g2d.drawString(label, xFrameCoord-5, yFrameCoord+20);
		}
		
		for(Entry<Point,String> mark : yReferentialMarks.entrySet()) {
			Point p = mark.getKey();
			String label = mark.getValue();
			int xFrameCoord = p.getXFrameCoord(limits[0], limits[1]);
			int yFrameCoord = p.getYFrameCoord(limits[2], limits[3]);
			g2d.drawLine(xFrameCoord-5, yFrameCoord, xFrameCoord+5, yFrameCoord);
			g2d.drawString(label, xFrameCoord-20, yFrameCoord+5);
		}
	}
	
}