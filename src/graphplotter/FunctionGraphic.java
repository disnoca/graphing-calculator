package graphplotter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;

import javax.swing.JComponent;

import functionComponents.Function;
import functionComponents.Point;

@SuppressWarnings("serial")
public class FunctionGraphic extends JComponent {
	
	private Graphics2D g2d;
	private Function function;
	
	
	public FunctionGraphic(Function function) {
		super();
		this.function = function;
	}
	
	public void paintComponent(Graphics g) {
		g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(function.getColor());

		drawFunction();
	}
	
	public void drawFunction() {
		Polygon pol = new Polygon();
		ArrayList<Point> points = function.getPoints();
		points.forEach(point -> pol.addPoint(point.getXFrameCoord(-10, 10), point.getYFrameCoord(-10, 10)));
		g2d.drawPolyline(pol.xpoints, pol.ypoints, pol.npoints);
	}
	
	public String getExpression() {
		return function.getExpression();
	}

	public Color getColor() {
		return function.getColor();
	}
	
}