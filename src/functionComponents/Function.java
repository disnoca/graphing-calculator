package functionComponents;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import graphingCalculator.MathFunctions;

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
	
	private final double SEARCH_AREA = 2000;
	

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
	
	public Expression getParsedExpression() {
		return function;
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
				points.add(new Point(i, f(i), width, height, referentialLimits.getLimits()));
			else
				points.add(null);
		}
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	private Point createPoint(double x, double y) {
		return new Point(x, y, width, height, referentialLimits.getLimits());
	}
	
	public void recalculateFrameSize(Dimension size) {
		this.width = size.width;
		this.height = size.height;
		computeFunctionPoints();
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
	
	// G-Solve functions
	
	private double[] getSearchLimitCoords() {
		double limits[] = referentialLimits.getLimits();
		double searchLimits[] = {limits[0] - SEARCH_AREA/2, limits[1] + SEARCH_AREA/2};
		return searchLimits;
	}
	
	public ArrayList<Point> getRoots() {
		secondaryFunction = null;
		double searchLimits[] = getSearchLimitCoords();
		return findRoots(searchLimits[0], searchLimits[1]);
	}
	
	public ArrayList<Point> getMaximum() {
		secondaryFunction = null;
		double searchLimits[] = getSearchLimitCoords();
		return findFunctionMaximums(searchLimits[0], searchLimits[1]);
	}
	
	public ArrayList<Point> getMinimum() {
		secondaryFunction = null;
		double searchLimits[] = getSearchLimitCoords();
		return findFunctionMinimums(searchLimits[0], searchLimits[1]);
	}
	
	public Point getYAxisIntersection() {
		double y = f(0);
		if(!Double.isFinite(y)) return null;
		
		Point p = createPoint(0, y);
		p.roundCoords(resultdecimalPlaces);
		return p;
	}
	
	public ArrayList<Point> getFunctionIntersections(Function g) {
		secondaryFunction = g.getParsedExpression();
		double searchLimits[] = getSearchLimitCoords();
		return findRoots(searchLimits[0], searchLimits[1]);
	}
	
	public Point getYValue(double x) {
		double y = f(x);
		if(!Double.isFinite(y)) return null;
		
		Point p = createPoint(x, y);
		p.roundCoords(resultdecimalPlaces);
		return p;
	}
	
	public ArrayList<Point> getXValue(double x) {
		secondaryFunction = new ExpressionBuilder(String.valueOf(x)).variable("").build();
		double searchLimits[] = getSearchLimitCoords();
		return findRoots(searchLimits[0], searchLimits[1]);
	}
	
	
	// G-Solve function helpers
	private double searchStep = 0.1;
	private int searchDecimalPlaces = MathFunctions.numberOfDecimalPlaces(searchStep);
	private int resultdecimalPlaces = 3;
	
	/*
	 * How this method works:
	 * 
	 * Two consecutive xs are kept at a time. If the first x's evaluation value happens to 0, it's saved as a root.
	 * Otherwise, if the two xs signs differ it means there's a root in the area between them. All root areas are saved and later their respective roots calculated
	 */
	private ArrayList<Point> findRoots(double minCoord, double maxCoord) {
		ArrayList<Point> roots = new ArrayList<>();
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
				roots.add(createPoint(x, 0));
			}
			else if(currY*prevY < 0)
				rootAreas.put(x-searchStep, x);
			
			prevY = currY;
		}
		
		for(Entry<Double, Double> rootArea : rootAreas.entrySet()) {
			double x = MathFunctions.roundToDecimalPlaces(computeRoot(rootArea.getKey(), rootArea.getValue()), resultdecimalPlaces);
			roots.add(createPoint(x, 0));
		}
		
		ArrayList<Point> localExtremes = findLocalExtremes(minCoord, maxCoord, FIND_MAX);
		localExtremes.addAll(findLocalExtremes(minCoord, maxCoord, FIND_MIN));
		
		for(Point localExtreme : localExtremes)
			if(localExtreme.getY() == 0) {
				localExtreme.roundCoords(resultdecimalPlaces);
				roots.add(localExtreme);
			}
		
		Collections.sort(roots);
		return roots;
	}
	
	/*
	 * How this method works:
	 * Three consecutive xs are kept at time. If the middle x is evaluated higher/lower than the other variables, it means there's a local extreme in the area between the outer xs.
	 * All local extremes areas are saved and their extremes calculated.
	 * 
	 * The returned ArrayList Points aren't rounded to the usual amount or sorted since this is only used as an intermediate step in other functions
	 */ 
	private ArrayList<Point> findLocalExtremes(double minCoord, double maxCoord, boolean findMax) {
		ArrayList<Point> localExtremes = new ArrayList<>();
		HashMap<Double, Double> localExtremeAreas = new HashMap<>();
		
		double pprevY = 0, prevY = 0;
		int iterationsSinceNan = 0;
		
		// first two iterations are skipped
		for(double x = Math.floor(minCoord); x <= maxCoord; x = MathFunctions.roundToDecimalPlaces(x+searchStep, searchDecimalPlaces)) {
			double currY = h(x);
			if(Double.isNaN(currY))  {
				iterationsSinceNan = 1;
				continue;
			}
			
			// two iterations must happen after a NaN to resume the loop correctly
			// since three points are needed at a time
			if(iterationsSinceNan >= 0) {
				iterationsSinceNan--;
				prevY = currY;
				pprevY = h(x-1);
				continue;
			}

			if(findMax && prevY > pprevY && prevY > currY)
				localExtremeAreas.put(x-2, x);
			else if(!findMax && prevY < pprevY && prevY < currY)
				localExtremeAreas.put(x-2, x);
			
			pprevY = prevY;
			prevY = currY;
		}
		
		
		// the rounding guarantees the accuracy of the extreme's y values when used in other functions
		int calculationDecimalPlaces = MathFunctions.numberOfDecimalPlaces(TOLERANCE)-1;
		
		double x, y;
		for(Entry<Double, Double> localMinimumArea : localExtremeAreas.entrySet()) {
			x = computeExtreme(localMinimumArea.getKey(), localMinimumArea.getValue(), findMax);
			y = MathFunctions.roundToDecimalPlaces(h(x), calculationDecimalPlaces);
			localExtremes.add(createPoint(x, y));
		}
		
		return localExtremes;
	}
	
	private ArrayList<Point> findFunctionMaximums(double minCoord, double maxCoord) {
		ArrayList<Point> localMaximums = findLocalExtremes(minCoord, maxCoord, FIND_MAX);
		ArrayList<Point> functionMaximums = new ArrayList<>();
		
		double y, maxY = Double.MIN_VALUE;
		for(Point localMaximum : localMaximums) {
			y = localMaximum.getY();
			if(y >= maxY) {
				if(y > maxY && !localMaximums.isEmpty()) {
					maxY = y;
					functionMaximums.clear();
				}
				localMaximum.roundCoords(resultdecimalPlaces);
				functionMaximums.add(localMaximum);
			}
		}

		Collections.sort(functionMaximums);
		return functionMaximums;
	}
	
	private ArrayList<Point> findFunctionMinimums(double minCoord, double maxCoord) {
		ArrayList<Point> localMinimums = findLocalExtremes(minCoord, maxCoord, FIND_MIN);
		ArrayList<Point> functionMinimums = new ArrayList<>();
		
		double y, minY = Double.MAX_VALUE;
		for(Point localMinimum : localMinimums) {
			y = localMinimum.getY();
			if(y <= minY) {
				if(y < minY && !functionMinimums.isEmpty()) {
					minY = y;
					functionMinimums.clear();
				}
				localMinimum.roundCoords(resultdecimalPlaces);
				functionMinimums.add(localMinimum);
			}
		}
		
		Collections.sort(functionMinimums);
		return functionMinimums;
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
				cond = h(x1) > h(x2);
			else
				cond = h(x1) < h(x2);
		
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
