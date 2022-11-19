package functionComponents;

import java.awt.Dimension;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;

public class ReferentialLimits {

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
		int decimalPlaces = calculateDecimalPoint(length);
		
		double maxLength;
		if(decimalPlaces <= 0) {
			maxLength = Math.pow(10, Math.abs(decimalPlaces));
			
		}
		else {
			maxLength = Math.pow(10, -decimalPlaces+1);
			min = Double.parseDouble(formattedNumberString(min, decimalPlaces));
		}
		
		double step = maxLength/10;
		
		for(double current = min; current <= max; current += step) {
			if(current == 0) continue;
			
			String label = formattedNumberString(current, decimalPlaces);
			if(xLine)
				referentialMarks.put(new Point(current, 0, frameWidth, frameHeight), label);
			else
				referentialMarks.put(new Point(0, current, frameWidth, frameHeight), label);
		}
		
		return referentialMarks;
	}
	
	// negative decimal places represent the number of "0"s to the right
	private int calculateDecimalPoint(double length) {
		length /= 2;
		int decimalPoints = 0;
		
		if(length > 1)
			while(length > 1) {
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
		df.setRoundingMode(RoundingMode.FLOOR);
		return df.format(number);
	}
	
	public double[] getLimits() {
		double[] limits = {xMin, xMax, yMin, yMax};
		return limits;
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
	
}
