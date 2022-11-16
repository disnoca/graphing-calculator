package functionComponents;

import java.awt.Dimension;
import java.util.ArrayList;

import graphplotter.GraphPlotterFrame;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Function {
	
	private int width, height;
	private double minX, maxX;
	private String expression;
	private Expression function;
	
	private ArrayList<Point> points;
	
	// determines how fluid is the function's drawing
	// setting it any higher than this can cause significant loading times
	private final double ACCURACY = 20000;	
	

	public Function(Dimension size, String expression) {
		this.width = size.width;
		this.height = size.height;
		this.minX = GraphPlotterFrame.STARTING_MINY;
		this.maxX = GraphPlotterFrame.STARTING_MAXY;
		
		this.expression = expression;
		this.function = new ExpressionBuilder(expression).variable("x").build();
		
		computeFunction();
	}

	public String getExpression() {
		return expression;
	}
	
	private void computeFunction() {
		double step = (maxX-minX)/ACCURACY;
		int pointCount = (int) Math.ceil((maxX-minX)/step);
		
		points = new ArrayList<>(pointCount);
		
		for(double i=minX; i<=maxX; i+=step)
			points.add(new Point(i, f(i), width, height));
	}
	
	private double f(double x) {
		return function.setVariable("x", x).evaluate();
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	public void recalculateFrameSize(Dimension size) {
		this.width = size.width;
		this.height = size.height;
		computeFunction();
	}
	
	public void recalculateReferentialSize() {
		
	}
	
}
