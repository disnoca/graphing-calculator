package functionComponents;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Function implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int width, height;
	private ReferentialLimits referentialLimits;
	private String expression;
	private Expression function;
	
	private ArrayList<Point> points;
	
	// determines how fluid is the function's drawing
	// setting it any higher than this can cause significant loading times
	private final double DRAWING_ACCURACY = 20000;	
	

	public Function(Dimension size, ReferentialLimits referentialLimits, String expression) {
		this.width = size.width;
		this.height = size.height;
		this.referentialLimits = referentialLimits;
		
		setExpression(expression);
	}

	public String getExpression() {
		return expression;
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
		this.function = new ExpressionBuilder(expression).variable("x").build();
		computeFunction();
	}
	
	private void computeFunction() {
		double xLength = referentialLimits.getXLength();
		double step = xLength/DRAWING_ACCURACY;
		int pointCount = (int) Math.ceil(xLength/step);
		
		points = new ArrayList<>(pointCount);
		
		for(double i=referentialLimits.getXMin(); i<=referentialLimits.getXMax(); i+=step)
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
	
	
	// Mathematical Algorithms
	private final double TOLERANCE = 0.000001;
	
	// Root Finding Algorithm
	// Secant method
	// find the root of a function between two points with different signs
	private double findRoot(double x1, double x2) {
		if(f(x1)*f(x2) > 0) return Double.NaN;
		double m = (x1+x2)/2;
		if(f(m) == 0) return m;
		
		double y1, ym;
		while((x2-x1)/2 > TOLERANCE) {
			y1 = f(x1);
			ym = f(m);
			
			if(y1*ym < 0)
				x2 = m;
			else
				x1 = m;
			
			m = (x1+x2)/2;
		}
		
		return m;
	}
	
	// Integral Calculation Algorithm
	// Monte Carlo integration using anthitetic variables for variance reduction
	private final int SAMPLE_SIZE = 100000;
	
	private double calculateIntegral(double x1, double x2) {
		Random rand = new Random();
		double range = (x2-x1);
		double randValues[] = new double[SAMPLE_SIZE];
		double funcSamples[] = new double[SAMPLE_SIZE];
		double funcSamplesSum = 0;
		
		// the generated values must be generated from a Standard Uniform Distribution in order for the anthitetic variables method to be used
		// hence first generating the value between 0 and 1 (and its counterpart) and only then performing the scaling to the specified bounds
		for(int i = 0; i < SAMPLE_SIZE; i+=2) {
			randValues[i] = rand.nextDouble();
			randValues[i+1] = 1-randValues[i];
			randValues[i] = randValues[i]*range + x1;
			randValues[i+1] = randValues[i+1]*range + x1;
		}
		
		for(int i = 0; i < SAMPLE_SIZE; i++) {
			funcSamples[i] = range*f(randValues[i]);
			funcSamplesSum += funcSamples[i];
		}
		
		return funcSamplesSum/SAMPLE_SIZE;
	}
	
}
