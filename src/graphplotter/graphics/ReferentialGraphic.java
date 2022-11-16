package graphplotter.graphics;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import functionComponents.Point;
import graphplotter.GraphPlotterFrame;

public class ReferentialGraphic extends BufferedImage {
	
	private int width, height;
	
	// these are ints in this class because only the integers are marked in a referential
	private int minX, maxX, minY, maxY;
	
	private Graphics2D g2d;
	
	
	public ReferentialGraphic(Dimension size) {
		this(size, GraphPlotterFrame.STARTING_MINX, GraphPlotterFrame.STARTING_MAXX, GraphPlotterFrame.STARTING_MINY, GraphPlotterFrame.STARTING_MAXY);
	}
	
	public ReferentialGraphic(Dimension size, double minX, double maxX, double minY, double maxY) {
		super(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		this.width = this.getWidth();
		this.height = this.getHeight();
		g2d = this.createGraphics();
		
		minX = (int) Math.ceil(minX);
		maxX = (int) Math.floor(maxX);
		minY = (int) Math.ceil(minY);
		maxY = (int) Math.floor(maxY);
		
		drawReferential();
	}

	private void drawReferential() {
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(Color.black);
		
		g2d.drawLine(0 , height/2, width, height/2);
		g2d.drawLine(width/2 ,0, width/2, height);
		g2d.drawString("0", width/2+5, height/2+15);
		
		drawReferenceLines();
	}
	
	private void drawReferenceLines() {
		for(int i=minX; i<=maxX; i++) {
			if(i == 0) continue;
			
			Point p = new Point(i, 0, width, height);
			int xFrameCoord = p.getXFrameCoord(minX, maxX);
			int yFrameCoord = p.getYFrameCoord(minY, maxY);
			g2d.drawLine(xFrameCoord, yFrameCoord-5, xFrameCoord, yFrameCoord+5);
			g2d.drawString(String.valueOf(i), xFrameCoord-5, yFrameCoord+20);
		}
		
		for(int i=minY; i<=maxY; i++) {
			if(i == 0) continue;
			
			Point p = new Point(0, i, width, height);
			int xFrameCoord = p.getXFrameCoord(minX, maxX);
			int yFrameCoord = p.getYFrameCoord(minY, maxY);
			g2d.drawLine(xFrameCoord-5, yFrameCoord, xFrameCoord+5, yFrameCoord);
			g2d.drawString(String.valueOf(i), xFrameCoord-20, yFrameCoord+5);
		}
		
		g2d.setFont(new Font("", Font.PLAIN, 20));
	}
	
}