package functionComponents;

import java.awt.Dimension;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import graphplotter.MathFunctions;
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
	
	private ArrayList<Double> roots, maximums, minimums;
	

	public Function(Dimension size, ReferentialLimits referentialLimits, String expression) {
		this.width = size.width;
		this.height = size.height;
		this.referentialLimits = referentialLimits;
		
		roots = new ArrayList<>();
		maximums = new ArrayList<>();
		minimums = new ArrayList<>();
		
		setExpression(expression);
		secondaryFunction = null;
		
		findRoots(-100, 100);
		findExtremes(-100, 100);
		
		System.out.println("root");
		for(double root : roots)
			System.out.println(root);
		
		System.out.println("max");
		for(double maximum : maximums)
			System.out.println(maximum);
		
		System.out.println("min");
		for(double minimum : minimums)
			System.out.println(minimum);
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
		
		double y;
		for(double i=referentialLimits.getXMin(); i<=referentialLimits.getXMax(); i+=step) {
			y = f(i);
			if(Double.isFinite(y))
				points.add(new Point(i, f(i), width, height));
			else
				points.add(null);
		}
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
	
	
	// G-Solve Functions
	private double searchStep = 0.1;
	private int searchDecimalPlaces = MathFunctions.numberOfDecimalPlaces(searchStep);
	private int resultdecimalPlaces = 2;
	
	/*
	 * How this method works:
	 * 
	 * Two consecutive xs are kept at a time. If the first x's evaluation value happens to 0, it's saved as a root.
	 * Otherwise, if the two xs signs differ it means there's a root in the area between them. All root areas are saved and later their respective roots calculated
	 */
	private void findRoots(double minCoord, double maxCoord) {
		HashMap<Double, Double> rootAreas = new HashMap<>();
		
		double prevY = 0;
		boolean prevWasNan = false;
		
		// first iteration is skipped
		for(double x = Math.floor(minCoord); x <= maxCoord; x = MathFunctions.roundToDecimalPlaces(x+searchStep, searchDecimalPlaces)) {
			double currY = h(x);
			
			if(Double.isNaN(currY))  {
				prevWasNan = true;
				continue;
			}
			
			if(prevWasNan) {
				prevWasNan = false;
				prevY = currY;
				continue;
			}
			
			if(currY == 0) {
				roots.add(x);
			}
			else if(currY*prevY < 0)
				rootAreas.put(x-searchStep, x);
			
			prevY = currY;
		}
		
		secondaryFunction = null;
		for(Entry<Double, Double> rootArea : rootAreas.entrySet())
			roots.add(MathFunctions.roundToDecimalPlaces(computeRoot(rootArea.getKey(), rootArea.getValue()), resultdecimalPlaces));
	}
	
	/*
	 * How this method works:
	 * 
	 * 1. Three consecutive xs are kept at time. If the middle x is evaluated higher/lower than the other variables, it means there's a local extreme in the area between the outer xs.
	 * All local extremes areas are saved and their extremes calculated.
	 * 
	 * 2. The minimum/maximum of the local extremes is calculated and only the local extremes that have that value as y are saved as function extremes
	 */
	private void findExtremes(double minCoord, double maxCoord) {
		HashMap<Double, Double> possibleMinimumAreas = new HashMap<>();
		HashMap<Double, Double> possibleMaximumAreas = new HashMap<>();
		
		double pprevY = 0, prevY = 0;
		int iterationsSinceNan = 0;
		
		// first two iterations are skipped
		for(double x = Math.floor(minCoord); x <= maxCoord; x = MathFunctions.roundToDecimalPlaces(x+searchStep, searchDecimalPlaces)) {
			double currY = f(x);
			if(Double.isNaN(currY))  {
				iterationsSinceNan = 1;
				continue;
			}
			
			// two iterations must happen after a NaN to resume the loop correctly
			// since three points are needed at a time
			if(iterationsSinceNan >= 0) {
				iterationsSinceNan--;
				prevY = currY;
				pprevY = f(x-1);
				continue;
			}

			if(prevY < pprevY && prevY < currY)
				possibleMinimumAreas.put(x-2, x);
			else if(prevY > pprevY && prevY > currY)
				possibleMaximumAreas.put(x-2, x);
			
			pprevY = prevY;
			prevY = currY;
		}
		
		// this affects up to how many decimal places are used when compared when comparing extremes' y values
		int extremeComparisonDecimalPlaces = MathFunctions.numberOfDecimalPlaces(TOLERANCE)-1;
		
		double possibleMinimum, possibleMinimumY, actualMinimumY = Double.MIN_VALUE;
		for(Entry<Double, Double> possibleMinimumArea : possibleMinimumAreas.entrySet()) {
			possibleMinimum = computeExtreme(possibleMinimumArea.getKey(), possibleMinimumArea.getValue(), FIND_MIN);
			possibleMinimumY = MathFunctions.roundToDecimalPlaces(f(possibleMinimum), extremeComparisonDecimalPlaces);
			if(possibleMinimumY <= actualMinimumY) {
				if(possibleMinimumY < actualMinimumY && !minimums.isEmpty()) {
					actualMinimumY = possibleMinimumY;
					minimums.clear();
				}
				minimums.add(MathFunctions.roundToDecimalPlaces(possibleMinimum, resultdecimalPlaces));
			}
		}
		
		double possibleMaximum, possibleMaximumY, actualMaximumY = Double.MAX_VALUE;
		for(Entry<Double, Double> possibleMaximumArea : possibleMaximumAreas.entrySet()) {
			possibleMaximum = computeExtreme(possibleMaximumArea.getKey(), possibleMaximumArea.getValue(), FIND_MAX);
			possibleMaximumY = MathFunctions.roundToDecimalPlaces(f(possibleMaximum), extremeComparisonDecimalPlaces);
			if(possibleMaximumY >= actualMaximumY) {
				if(possibleMaximumY > actualMaximumY && !maximums.isEmpty()) {
					actualMaximumY = possibleMaximumY;
					maximums.clear();
				}
				maximums.add(MathFunctions.roundToDecimalPlaces(possibleMaximum, resultdecimalPlaces));
			}
		}
	}
	
	
	// Mathematical Algorithms
	private final double TOLERANCE = 0.000001;
	private final double GR = (Math.sqrt(5)-1)/2; 	// golden ratio
	
	// Root Finding Algorithm
	// Bissection Method
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
	
	private double computeIntegral(double a, double b) {
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
