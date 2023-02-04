package functionComponents;

import java.awt.Dimension;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;

public class ReferentialLimits implements Serializable {

	private static final long serialVersionUID = 1L;
	
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
		
		xReferentialMarks = calculateReferentialMarks(true);
		yReferentialMarks = calculateReferentialMarks(false);
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
			min = formatDecimalPlaces(min, decimalPlaces);
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
				
				current = formatDecimalPlaces(current, decimalPlaces+i);
				if(current == 0 || current < min || current > max) continue;
				
				String label = formattedNumberString(current, decimalPlaces+i);
				
				if(!referentialMarks.containsValue(label)) {
					if(xLine)
						marksBatch.put(new Point(current, 0, frameWidth, frameHeight), label);
					else
						marksBatch.put(new Point(0, current, frameWidth, frameHeight), label);
				}
			}
			
			if(referentialMarks.size()+marksBatch.size() > 20) break;
			referentialMarks.putAll(marksBatch);
		}
		
		return referentialMarks;
	}
	
	
	// duplicated code but I cant do anything about it. joining these two methods into one
	// would make it much more complex and it would be impossible to understand what it does
	
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
	
	// this method returns the actual decimal places because it is used to display the numbers correctly
	// unlike the one above which is used for calculations and better visuals of the referential marks
	private int calculateDecimalPlacesForFormatting(double number) {	
		number = Math.abs(number);
		number = number-Math.floor(number);
		if(number == 0) return 0;
		
		int decimalPoints = 0;
			while(number < 1) {
				number *= 10;
				decimalPoints++;
			}
		return decimalPoints;
	}
	
	private String formattedNumberString(double number, int decimalPlaces) {
		if(decimalPlaces <= 0) {
			String numberString = String.valueOf(number);
			int pointCharIndex = numberString.indexOf('.');
			return numberString.substring(0, pointCharIndex);
		}
		
		String format = "#.";
		for(int i=0; i<decimalPlaces; i++)
			format += '#';
		
		DecimalFormat df = new DecimalFormat(format);
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		return df.format(number);
	}
	
	private double formatDecimalPlaces(double number, int decimalPlaces) {
		return Double.parseDouble(formattedNumberString(number, decimalPlaces));
	}
	
	public double[] getLimits() {
		double[] limits = {xMin, xMax, yMin, yMax};
		return limits;
	}
	
	public String[] getFormattedLimits() {
		String[] formattedLimits = {formattedNumberString(xMin, calculateDecimalPlacesForFormatting(xMin)), 
				formattedNumberString(xMax, calculateDecimalPlacesForFormatting(xMax)), 
				formattedNumberString(yMin, calculateDecimalPlacesForFormatting(yMin)), 
				formattedNumberString(yMax, calculateDecimalPlacesForFormatting(yMax))};
		
		return formattedLimits;
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
		
		xReferentialMarks = calculateReferentialMarks(true);
		yReferentialMarks = calculateReferentialMarks(false);
	}
	
}
