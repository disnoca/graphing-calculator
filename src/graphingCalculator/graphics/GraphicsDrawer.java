package graphingCalculator.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JComponent;

import functionComponents.Function;
import functionComponents.Point;
import functionComponents.ReferentialLimits;
import graphingCalculator.gSolveState.GSolveState;
import graphingCalculator.gSolveState.GSolveStateWrapper;
import graphingCalculator.popupWindows.GSolveIntegralWindow;
import graphingCalculator.saver.GraphingCalculatorProjectSave;

@SuppressWarnings("serial")
public class GraphicsDrawer extends JComponent {
	
	private Dimension size;
	private ReferentialLimits referentialLimits;
	private BufferedImage referentialGraphic;
	private ArrayList<FunctionGraphic> functionGraphics;
	private HighlightGraphic highlightGraphic;
	
	private ArrayList<Point> lastGSolveResults;
	private Point lastIntegralLowerBound, lastIntegralUpperBound;
	private int currGSolveSolutionPos;
	
	private GSolveStateWrapper gSolveState;
	

	public GraphicsDrawer(Dimension size, ReferentialLimits referentialLimits, GSolveStateWrapper gSolveState) {
		this.size = size;
		this.referentialLimits = referentialLimits;
		
		setReferentialGraphic();
		functionGraphics = new ArrayList<>();
		
		this.gSolveState = gSolveState;
	}
	
	@Override
    public void paintComponent(Graphics g){
		super.paintComponent(g);
		// referential is always the bottom layer
		g.drawImage(referentialGraphic, 0, 0, size.width, size.height, null);
		
        for(FunctionGraphic layer : functionGraphics)
            g.drawImage(layer, 0, 0, size.width, size.height, null);
        
        if(highlightGraphic != null)
        	g.drawImage(highlightGraphic, 0, 0, size.width, size.height, null);
    }
	
	private void setReferentialGraphic() {
		referentialGraphic = new ReferentialGraphic(size, referentialLimits);
	}
	
	private void setPointHighlightsGraphic(List<Point> pointHighlights) {
		highlightGraphic = new HighlightGraphic(size, pointHighlights);
	}
	
	private void setIntegralHighlightGraphic(List<Point> integralPoints) {		
		double limits[] = referentialLimits.getLimits();
		Point integralAreaStartRoot = new Point(lastIntegralLowerBound.getX(), 0, size.width, size.height, limits);
		Point integralAreaEndRoot = new Point(lastIntegralUpperBound.getX(), 0, size.width, size.height, limits);
		
		highlightGraphic = new HighlightGraphic(size, integralAreaStartRoot, integralAreaEndRoot, integralPoints, 0);
	}
	
	public void clearHighlights() {
		highlightGraphic = null;
		lastGSolveResults = null;
		lastIntegralLowerBound = null;
		lastIntegralUpperBound = null;
	}
	
	public void addFunction(Function function, Color color) {
		FunctionGraphic functionGraphic = new FunctionGraphic(size, function, color);
		functionGraphics.add(functionGraphic);
	}
	
	public void removeFunction(int pos) {
		functionGraphics.remove(pos);
	}
	
	public void swapFunctions(int pos1, int pos2) {
		Collections.swap(functionGraphics, pos1, pos2);
	}
	
	public int getFunctionCount() {
		return functionGraphics.size();
	}
	
	public ReferentialLimits getReferentialLimits() {
		return referentialLimits;
	}
	
	public Function getFunction(int pos) {
		return functionGraphics.get(pos).getFunction();
	}
	
	public String getFunctionExpression(int pos) {
		return functionGraphics.get(pos).getExpression();
	}

	public Color getFunctionColor(int pos) {
		return functionGraphics.get(pos).getColor();
	}
	
	public String[] getFunctionExpressions() {
		String[] functionExpressions = new String[functionGraphics.size()];
		for(int i = 0; i < functionGraphics.size(); i++)
			functionExpressions[i] = functionGraphics.get(i).getExpression();
		return functionExpressions;
	}
	
	
	public void setFrameSize(Dimension size) {
		this.size = size;
		referentialLimits.updateFrameSize(size);
		updateGraphics();
	}
	
	public void doubleReferentialLimits() {
		zoomReferentialLimitsBy(-1);
	}
	
	public void halveReferentialLimits() {
		zoomReferentialLimitsBy(1);
	}
	
	/*
	 * positive values zoom in zoomFactor times
	 * negative values zoom out -zoomFactor times
	 */
	public void zoomReferentialLimitsBy(int zoomFactor) {
		if(zoomFactor == 0) return;
		
		double[] limits = referentialLimits.getLimits();
		double xLength = referentialLimits.getXLength();
		double yLength = referentialLimits.getYLength();
		double xAdjustment = 0, yAdjustment = 0;
		
		// zoom in
		if(zoomFactor > 0) {
			for(int i = 0; i < zoomFactor; i++) {
				xAdjustment += xLength/Math.pow(2, i)/4;
				yAdjustment += yLength/Math.pow(2, i)/4;
			}
			setReferentialLimits(limits[0]+xAdjustment, limits[1]-xAdjustment, limits[2]+yAdjustment, limits[3]-yAdjustment);
		}
		
		// zoom out
		else {
			for(int i = 0; i > zoomFactor; i--) {
				xAdjustment += xLength*Math.pow(2, -i)/2;
				yAdjustment += yLength*Math.pow(2, -i)/2;
			}
			setReferentialLimits(limits[0]-xAdjustment, limits[1]+xAdjustment, limits[2]-yAdjustment, limits[3]+yAdjustment);
		}
	}
	
	public void moveOriginLocation(double xMove, double yMove) {
		double[] limits = referentialLimits.getLimits();
		setReferentialLimits(limits[0]+xMove, limits[1]+xMove, limits[2]+yMove, limits[3]+yMove);
	}
	
	private void setOriginLocation(double x, double y) {
		double xAdjustment = referentialLimits.getXLength()/2;
		double yAdjustment = referentialLimits.getYLength()/2;
		setReferentialLimits(x-xAdjustment, x+xAdjustment, y-yAdjustment, y+yAdjustment);
	}
	
	public void setReferentialLimits(double xMin, double xMax, double yMin, double yMax) {
		referentialLimits.updateLimits(xMin, xMax, yMin, yMax);
		updateGraphics();
	}
	
	public void updateGraphics() {
		setReferentialGraphic();
		
		ArrayList<FunctionGraphic> temp = new ArrayList<>(functionGraphics.size());
		for(FunctionGraphic fg: functionGraphics) {
			Function f = fg.getFunction();
			Color c = fg.getColor();
			f.recalculateFrameSize(size);
			temp.add(new FunctionGraphic(size, f, c));
		}
		
		functionGraphics = temp;
		
		updateHighlights();
	}
	
	private void updateHighlights() {
		if(gSolveState.state == GSolveState.NONE) return;
		
		if(gSolveState.state == GSolveState.INTEGRAL)
			setIntegralHighlightGraphic(getAndUpdateVisiblePoints(lastGSolveResults));
		else
			setPointHighlightsGraphic(getAndUpdateVisiblePoints(lastGSolveResults));
	}
	
	public BufferedImage getBufferedImage(boolean transparent) {
		BufferedImage bImg;
		if(transparent)
			bImg = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		else
			bImg = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		
	    Graphics2D cg = bImg.createGraphics();
	    
	    if(!transparent) {
	    	cg.setPaint(Color.WHITE);
	    	cg.fillRect (0, 0, size.width, size.height);
	    }
	    
	    this.paintAll(cg);
	    return bImg;
	}
	
	public void loadProject(GraphingCalculatorProjectSave save) {
		referentialLimits = save.getReferentiaLimits();
		setReferentialGraphic();
		
		functionGraphics.clear();
		for(Entry<Color, String> entry : save.getFunctions().entrySet()) {
			Function function = new Function(size, referentialLimits, entry.getValue());
			addFunction(function, entry.getKey());
		}
	}
	
	
	// G-Solve functions
	
	private Function getCurrentWorkingFunction() {
		return functionGraphics.get(functionGraphics.size()-1).getFunction();
	}
	
	private ArrayList<Point> getAndUpdateVisiblePoints(ArrayList<Point> allPoints) {
		ArrayList<Point> visiblePoints = new ArrayList<>();
		double limits[] = referentialLimits.getLimits();
		
		for(Point p : allPoints)
			if(referentialLimits.pointIsVisible(p))
				// a new point must be created in the case the referential has moved since when it was created
				visiblePoints.add(new Point(p.getX(), p.getY(), size.width, size.height, limits));
		
		return visiblePoints;
	}
	
	private void startGSolveSolution() {
		currGSolveSolutionPos = 0;
		double currOrigin = referentialLimits.getXMin() + referentialLimits.getXLength()/2;
		Point mainSolution = lastGSolveResults.get(0);
		double mainSolutionDist = mainSolution.getX()-currOrigin;
		
		Point p;
		for(int i = 1; i < lastGSolveResults.size(); i++) {
			p = lastGSolveResults.get(i);
			double currDist = p.getX()-currOrigin;
			
			if((mainSolutionDist < 0 && currDist > mainSolutionDist) || (mainSolutionDist > 0 && currDist < mainSolutionDist)) {
				currGSolveSolutionPos = i;
				mainSolution = p;
				mainSolutionDist = mainSolution.getX()-currOrigin;
			}
		}
		
		setOriginLocation(mainSolution.getX(), mainSolution.getY());
		setPointHighlightsGraphic(getAndUpdateVisiblePoints(lastGSolveResults));
	}
	
	public void nextGSolveSolution() {
		if(currGSolveSolutionPos >= lastGSolveResults.size()-1) return;
		
		Point p = lastGSolveResults.get(++currGSolveSolutionPos);
		setOriginLocation(p.getX(), p.getY());
		setPointHighlightsGraphic(getAndUpdateVisiblePoints(lastGSolveResults));
	}
	
	public void prevGSolveSolution() {
		if(currGSolveSolutionPos <= 0) return;
		
		Point p = lastGSolveResults.get(--currGSolveSolutionPos);
		setOriginLocation(p.getX(), p.getY());
		setPointHighlightsGraphic(getAndUpdateVisiblePoints(lastGSolveResults));
	}
	
	public boolean gSolveRoot() {
		lastGSolveResults = getCurrentWorkingFunction().getRoots();
		if(lastGSolveResults.isEmpty()) return false;
		
		startGSolveSolution();
		return true;
	}
	
	public boolean gSolveMaximum() {
		lastGSolveResults = getCurrentWorkingFunction().getMaximum();
		if(lastGSolveResults.isEmpty()) return false;
		
		startGSolveSolution();
		return true;
	}
	
	public boolean gSolveMinimum() {
		lastGSolveResults = getCurrentWorkingFunction().getMinimum();
		if(lastGSolveResults.isEmpty()) return false;
		
		startGSolveSolution();
		return true;
	}
	
	public boolean gSolveYAxisIntersection() {
		Point[] solution = {getCurrentWorkingFunction().getYAxisIntersection()};
		if(solution[0] == null) return false;
		
		lastGSolveResults = new ArrayList<>(Arrays.asList(solution));
		startGSolveSolution();
		return true;
	}
	
	public boolean gSolveFunctionIntersection() {
		Function intersectionFuntion = functionGraphics.get(functionGraphics.size()-2).getFunction();
		lastGSolveResults = getCurrentWorkingFunction().getFunctionIntersections(intersectionFuntion);
		if(lastGSolveResults.isEmpty()) return false;
		
		startGSolveSolution();
		return true;
	}
	
	public boolean gSolveYValue(double x) {
		Point[] solution = {getCurrentWorkingFunction().getYValue(x)};
		if(solution[0] == null) return false;
		
		lastGSolveResults = new ArrayList<>(Arrays.asList(solution));
		startGSolveSolution();
		return true;
	}

	// returns true if any value was found, false otherwise
	public boolean gSolveXValue(double y) {
		lastGSolveResults = getCurrentWorkingFunction().getXValue(y);
		if(lastGSolveResults.isEmpty()) return false;
		
		startGSolveSolution();
		return true;
	}
	
	public int gSolveIntegral(double lowerBound, double upperBound) {
		Function f = getCurrentWorkingFunction();
		if(f.getYValue(lowerBound) == null) return GSolveIntegralWindow.INTEGRAL_LOWER_BOUND_ERROR;
		if(f.getYValue(upperBound) == null) return GSolveIntegralWindow.INTEGRAL_UPPER_BOUND_ERROR;
		
		double integral = getCurrentWorkingFunction().getIntegral(lowerBound, upperBound);
		if(integral == Double.NaN) return GSolveIntegralWindow.INTEGRAL_CALCULATION_ERROR;
		
		lastGSolveResults = f.getPointsInInterval(lowerBound, upperBound);
		lastIntegralLowerBound = lastGSolveResults.get(0);
		lastIntegralUpperBound = lastGSolveResults.get(lastGSolveResults.size()-1);
		
		double xIntegralRange = upperBound-lowerBound;
		double yIntegralRange = f.getMaxValueInInterval(lowerBound, upperBound) - f.getMinValueInInterval(lowerBound, upperBound);
		double maxIntegralRange = Math.max(xIntegralRange, yIntegralRange);
		double limits[] = referentialLimits.getLimits();
		
		setReferentialLimits(limits[0]-maxIntegralRange/2, limits[1]+maxIntegralRange/2, limits[2]-maxIntegralRange/2, limits[3]+maxIntegralRange/2);
		doubleReferentialLimits();
		setIntegralHighlightGraphic(getAndUpdateVisiblePoints(lastGSolveResults));
		return 0;
	}
}
