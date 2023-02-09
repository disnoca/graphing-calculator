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
		
		double[] limits = referentialLimits.getLimits();
		Point origin = new Point(0, 0, width, height, limits);
		
		int xLineYFrameCoord = getXLineYFrameCoord(origin, limits[2], limits[3]);
		int yLineXFrameCoord = getYLineXFrameCoord(origin, limits[0], limits[1]);
		
		g2d.drawLine(0, xLineYFrameCoord, width, xLineYFrameCoord);		// x line
		g2d.drawLine(yLineXFrameCoord, 0, yLineXFrameCoord, height);	// y line
		
		drawReferentialMarks(xLineYFrameCoord, yLineXFrameCoord);
	}
	
	private int getXLineYFrameCoord(Point origin, double yMin, double yMax) {
		int xLineYFrameCoord = origin.getYFrameCoord();
		if(xLineYFrameCoord < 0)
			return 0;
		if(xLineYFrameCoord > height)
			return height;
		return xLineYFrameCoord;
	}
	
	private int getYLineXFrameCoord(Point origin, double xMin, double xMax) {
		int yLineXFrameCoord = origin.getXFrameCoord();
		if(yLineXFrameCoord < 0)
			return 0;
		if(yLineXFrameCoord > width)
			return width;
		return yLineXFrameCoord;
	}
	
	private void drawReferentialMarks(int xLineYFrameCoord, int yLineXFrameCoord) {
		HashMap<Point,String> xReferentialMarks = referentialLimits.getXReferentialMarks();
		HashMap<Point,String> yReferentialMarks = referentialLimits.getYReferentialMarks();
		
		for(Entry<Point,String> mark : xReferentialMarks.entrySet()) {
			Point p = mark.getKey();
			String label = mark.getValue();
			int xFrameCoord = p.getXFrameCoord();
			
			g2d.drawLine(xFrameCoord, xLineYFrameCoord-5, xFrameCoord, xLineYFrameCoord+5);
			if(xLineYFrameCoord <= width/2)
				g2d.drawString(label, xFrameCoord-label.length()*3, xLineYFrameCoord+20);
			else
				g2d.drawString(label, xFrameCoord-label.length()*3, xLineYFrameCoord-10);
		}
		
		for(Entry<Point,String> mark : yReferentialMarks.entrySet()) {
			Point p = mark.getKey();
			String label = mark.getValue();
			int yFrameCoord = p.getYFrameCoord();
			
			g2d.drawLine(yLineXFrameCoord-5, yFrameCoord, yLineXFrameCoord+5, yFrameCoord);
			if(yLineXFrameCoord >= height/2)
				g2d.drawString(label, yLineXFrameCoord-10-label.length()*6, yFrameCoord+4);
			else
				g2d.drawString(label, yLineXFrameCoord+8+label.length(), yFrameCoord+4);
		}
	}
	
}