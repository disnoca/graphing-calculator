package functionComponents;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Function implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final boolean FIND_MAX = true;
	private final boolean FIND_MIN = false;
	
	private int width, height;
	private ReferentialLimits referentialLimits;
	private String expression;
	
	// secondary function is the function chosen to find the intersection with
	// it's declared here so that findRoot() and findIntersection() can be the same block of code
	private Expression function, secondaryFunction;
	
	private ArrayList<Point> points;
	
	// determines how fluid is the function's drawing
	// setting it any higher than this can cause significant loading times
	private final double DRAWING_ACCURACY = 20000;	
	

	public Function(Dimension size, ReferentialLimits referentialLimits, String expression) {
		this.width = size.width;
		this.height = size.height;
		this.referentialLimits = referentialLimits;
		
		setExpression(expression);
		secondaryFunction = null;
	}

	public String getExpression() {
		return expression;
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
		this.function = new ExpressionBuilder(expression).variable("x").build();
		computeFunctionPoints();
	}
	
	private void computeFunctionPoints() {
		double xLength = referentialLimits.getXLength();
		double step = xLength/DRAWING_ACCURACY;
		int pointCount = (int) Math.ceil(xLength/step);
		
		points = new ArrayList<>(pointCount);
		
		for(double i=referentialLimits.getXMin(); i<=referentialLimits.getXMax(); i+=step)
			points.add(new Point(i, f(i), width, height));
	}
	
	private double f(double x) {
		try {
			return function.setVariable("x", x).evaluate();
		} catch(Exception e) {
			return Double.NaN;
		}
	}
	
	private double h(double x) {
		try {
			if(secondaryFunction == null)
				return function.setVariable("x", x).evaluate();
			else
				return function.setVariable("x", x).evaluate() - secondaryFunction.setVariable("x", x).evaluate();
		} catch(Exception e) {
			return Double.NaN;
		}
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	public void recalculateFrameSize(Dimension size) {
		this.width = size.width;
		this.height = size.height;
		computeFunctionPoints();
	}
	
	
	// Mathematical Algorithms
	private final double TOLERANCE = 0.000001;
	private final double GR = (Math.sqrt(5)-1)/2; 	// golden ratio
	
	// Root Finding Algorithm
	// Secant Method
	// find the root that exists between two points with different signs
	// if the secondaryFunction variable is set then this method returns the intersection between the two functions
	private double computeRoot(double a, double b) {
		if(h(a)*h(b) > 0) return Double.NaN;
		double m = (a+b)/2;
		if(h(m) == 0) return m;
		
		double y1, ym;
		while(b-a > TOLERANCE) {
			y1 = h(a);
			ym = h(m);
			
			if(y1*ym < 0)
				b = m;
			else
				a = m;
			
			m = (a+b)/2;
		}
		
		return m;
	}
	
	// Maximum/Minimum Finding Algorithm
	// Golden Section Search Method
	// find the maximum/minimum that exists between two points
	private double computeExtreme(double a, double b, boolean findMaximum) {
		double x1, x2, d;
		boolean cond;
		
		while(b-a > TOLERANCE) {
			d = GR*(b-a);
			x1 = a+d;
			x2 = b-d;
			
			if(findMaximum)
				cond = f(x1) > f(x2);
			else
				cond = f(x1) < f(x2);
		
			if(cond)
				a = x2;
			else
				b = x1;
		}
		
		return (a+b)/2;
	}
	
	// Integral Calculation Algorithm
	// Monte Carlo integration using anthitetic variables for variance reduction
	private final int SAMPLE_SIZE = 100000;
	
	private double copmuteIntegral(double a, double b) {
		Random rand = new Random();
		double range = (b-a);
		double randValues[] = new double[SAMPLE_SIZE];
		double funcSamples[] = new double[SAMPLE_SIZE];
		double funcSamplesSum = 0;
		
		// the generated values must be generated from a Standard Uniform Distribution in order for the anthitetic variables method to be used
		// hence first generating the value between 0 and 1 (and its opposite) and only then performing the scaling to the specified bounds
		for(int i = 0; i < SAMPLE_SIZE; i+=2) {
			randValues[i] = rand.nextDouble();
			randValues[i+1] = 1-randValues[i];
			randValues[i] = randValues[i]*range + a;
			randValues[i+1] = randValues[i+1]*range + a;
		}
		
		for(int i = 0; i < SAMPLE_SIZE; i++) {
			funcSamples[i] = range*f(randValues[i]);
			funcSamplesSum += funcSamples[i];
		}
		
		return funcSamplesSum/SAMPLE_SIZE;
	}
	
}
