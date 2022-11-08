package graphplotter.graphics;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import functionComponents.Point;

public class ReferentialGraphic extends BufferedImage {
	
	private int width, height;
	private Graphics2D g2d;
	
	
	public ReferentialGraphic(Dimension size) {
		super(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		this.width = this.getWidth();
		this.height = this.getHeight();
		g2d = this.createGraphics();
		
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
		for(int i=-10; i<=10; i++) {
			if(i == 0) continue;
			
			Point p = new Point(i, 0, width, height);
			int xFrameCoord = p.getXFrameCoord(-10, 10);
			int yFrameCoord = p.getYFrameCoord(-10, 10);
			g2d.drawLine(xFrameCoord, yFrameCoord-5, xFrameCoord, yFrameCoord+5);
			g2d.drawString(String.valueOf(i), xFrameCoord-5, yFrameCoord+20);
			
			p = new Point(0, i, width, height);
			xFrameCoord = p.getXFrameCoord(-10, 10);
			yFrameCoord = p.getYFrameCoord(-10, 10);
			g2d.drawLine(xFrameCoord-5, yFrameCoord, xFrameCoord+5, yFrameCoord);
			g2d.drawString(String.valueOf(i), xFrameCoord-20, yFrameCoord+5);
		}
		
		g2d.setFont(new Font("", Font.PLAIN, 20));
		g2d.drawString("X", width-20, height/2-10);
		g2d.drawString("Y", width/2+10, 20);
	}
	
}