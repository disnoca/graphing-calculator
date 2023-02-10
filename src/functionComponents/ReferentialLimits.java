package functionComponents;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.HashMap;

import graphingCalculator.utils.RoundingUtils;

public class ReferentialLimits implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final boolean X_LINE = true;
	private final boolean Y_LINE = false;
	
	private double xMin, xMax, yMin, yMax;
	private int frameWidth, frameHeight;
	private HashMap<Point, String> xReferentialMarks, yReferentialMarks;
	
	
	public ReferentialLimits(Dimension size, double xMin, double xMax, double yMin, double yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		
		frameWidth = size.width;
		frameHeight = size.height;
		
		xReferentialMarks = new HashMap<>();
		yReferentialMarks = new HashMap<>();
	
		xReferentialMarks = calculateReferentialMarks(X_LINE);
		yReferentialMarks = calculateReferentialMarks(Y_LINE);
	}
	
	private HashMap<Point, String> calculateReferentialMarks(boolean xLine) {
		HashMap<Point, String> referentialMarks = new HashMap<>();
		double min, max;
		if(xLine) {
			min = xMin; max = xMax;
		} else {
			min = yMin; max = yMax;
		}
		double length = max-min;
		int decimalPlaces = calculateDecimalPlacesForReferential(length);
		
		double maxLength;
		if(decimalPlaces <= 0)
			maxLength = Math.pow(10, Math.abs(decimalPlaces)+1)*2;
		else {
			maxLength = Math.pow(10, -decimalPlaces+1)*2;
			min = RoundingUtils.roundToDecimalPlaces(min, decimalPlaces);
		}
		
		double step = maxLength/20;
		
		double start = 0;
		if(start > min)
			while(start > min)
				start -= step;
		else
			while(start < min)
				start += step;
		
		for(int i = 0; referentialMarks.size() <= 10; i++) {
			HashMap<Point, String> marksBatch = new HashMap<>();
			double currStep = step/Math.pow(2, Math.max(0,i-1));
			
			for(double current = start+currStep/2; current <= max; current += currStep) {
				if(i == 0 && current == start+currStep/2) current = start;
				
				current = RoundingUtils.roundToDecimalPlaces(current, decimalPlaces+i);
				if(current == 0 || current < min || current > max) continue;
				
				String label = RoundingUtils.roundToDecimalPlacesStr(current, decimalPlaces+i);
				
				if(!referentialMarks.containsValue(label)) {
					if(xLine)
						marksBatch.put(new Point(current, (double) 0, frameWidth, frameHeight, getLimits()), label);
					else
						marksBatch.put(new Point((double) 0, current, frameWidth, frameHeight, getLimits()), label);
				}
			}
			
			// in some cases, the step might be too high and too few marks will appear
			// ine those cases, this allows for more marks to be added, up to a maximum of 19
			if(i > 0 && referentialMarks.size()+marksBatch.size() > 20) break;
			referentialMarks.putAll(marksBatch);
		}
		
		return referentialMarks;
	}
	
	// negative decimal places represent the number of '0's to the right
	// in this method, edge cases like 0.1, 1 and 10 return their decimal places minus 1
	// this is so that the referential is marked with the units below in these specific cases
	// which makes for a better looking referential:
	// ex. -10 -> 10 is marked with units and -1 -> 1 marked with decimals
	private int calculateDecimalPlacesForReferential(double length) {
		length /= 2;
		int decimalPoints = 0;
		
		if(length > 10)
			while(length > 10) {
				length /= 10;
				decimalPoints--;
			}
		else
			while(length <= 1) {
				length *= 10;
				decimalPoints++;
			}
		return decimalPoints;
	}
	
	public double[] getLimits() {
		double[] limits = {xMin, xMax, yMin, yMax};
		return limits;
	}
	
	public String[] getFormattedLimits() {
		String[] limitStrs = {Double.toString(xMin), Double.toString(xMax), Double.toString(yMin), Double.toString(yMax)};
		
		// integer values (coded as doubles) have a trailing .0 when turned into a String. This gets rid of that
		for(int i = 0; i < limitStrs.length; i++)
			if(limitStrs[i].endsWith(".0"))
				limitStrs[i] = limitStrs[i].substring(0, limitStrs[i].length()-2);
		
		return limitStrs;
	}
	
	public double getXMin() {
		return xMin;
	}
	
	public double getXMax() {
		return xMax;
	}
	
	public double getYMin() {
		return yMin;
	}
	
	public double getYMax() {
		return yMax;
	}
	
	public double getXLength() {
		return xMax-xMin;
	}
	
	public double getYLength() {
		return yMax-yMin;
	}
	
	public HashMap<Point, String> getXReferentialMarks() {
		return xReferentialMarks;
	}

	public HashMap<Point, String> getYReferentialMarks() {
		return yReferentialMarks;
	}
	
	public void updateLimits(double xMin, double xMax, double yMin, double yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		
		xReferentialMarks = calculateReferentialMarks(X_LINE);
		yReferentialMarks = calculateReferentialMarks(Y_LINE);
	}
	
	public void updateFrameSize(Dimension frameSize) {
		this.frameWidth = frameSize.width;
		this.frameHeight = frameSize.height;
		
		xReferentialMarks = calculateReferentialMarks(X_LINE);
		yReferentialMarks = calculateReferentialMarks(Y_LINE);
	}
	
}
