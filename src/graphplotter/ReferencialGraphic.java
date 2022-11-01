package graphplotter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class ReferencialGraphic extends JComponent {
	
	private int width, height;
	private Graphics2D g2d;
	
	
	public void paintComponent(Graphics g) {
		this.width = this.getWidth();
		this.height = this.getHeight();
		
		g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(Color.black);
		
		// draw X and Y axis
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
	}
	
}