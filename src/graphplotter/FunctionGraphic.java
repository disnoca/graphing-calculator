package graphplotter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import javax.swing.JComponent;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

@SuppressWarnings("serial")
public class FunctionGraphic extends JComponent {
	
	private int width, height;
	private Graphics2D g2d;
	private Color color;
	private Expression function;
	
	
	public FunctionGraphic(String expression, Color color) {
		super();
		this.function = new ExpressionBuilder(expression).variable("x").build();
		this.color = color;
	}
	
	public void paintComponent(Graphics g) {
		this.width = this.getWidth();
		this.height = this.getHeight();
		
		g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(this.color);

		drawFunction();
	}
	
	public void drawFunction() {
		Polygon pol = new Polygon();
		for(double i=-10; i<=10; i+=0.001) {
			Point pt = new Point(i, f(i), width, height);
			pol.addPoint(pt.getXFrameCoord(-10, 10), pt.getYFrameCoord(-10, 10));
		}
		
		g2d.drawPolyline(pol.xpoints, pol.ypoints, pol.npoints);
	}
	
	private double f(double x) {
		return function.setVariable("x", x).evaluate();
	}
	
}