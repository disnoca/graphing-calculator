package functionComponents;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Function {
	
	private int width, height;
	private Color color;
	private String expression;
	private Expression function;
	
	private ArrayList<Point> points;
	private final double ACCURACY = 0.001;
	

	public Function(Dimension size, String expression, Color color) {
		this.width = size.width;
		this.height = size.height;
		
		this.expression = expression;
		this.function = new ExpressionBuilder(expression).variable("x").build();
		this.color = color;
		
		computeFunction();
	}

	public Color getColor() {
		return color;
	}

	public String getExpression() {
		return expression;
	}
	
	private void computeFunction() {
		int pointCount = (int) Math.ceil((10-(-10))/ACCURACY);
		points = new ArrayList<>(pointCount);
		
		for(double i=-10; i<=10; i+=ACCURACY)
			points.add(new Point(i, f(i), width, height));
	}
	
	private double f(double x) {
		return function.setVariable("x", x).evaluate();
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
}
