package functionComponents;

import java.awt.Dimension;
import java.util.ArrayList;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Function {
	
	private int width, height;
	private int minX, maxX;
	private String expression;
	private Expression function;
	
	private ArrayList<Point> points;
	private final double ACCURACY = 0.001;
	

	public Function(Dimension size, String expression) {
		this.width = size.width;
		this.height = size.height;
		this.minX = -10;
		this.maxX = 10;
		
		this.expression = expression;
		this.function = new ExpressionBuilder(expression).variable("x").build();
		
		computeFunction();
	}

	public String getExpression() {
		return expression;
	}
	
	private void computeFunction() {
		int pointCount = (int) Math.ceil((maxX-minX)/ACCURACY);
		points = new ArrayList<>(pointCount);
		
		for(double i=minX; i<=maxX; i+=ACCURACY)
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
