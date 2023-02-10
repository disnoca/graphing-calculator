package graphingCalculator;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MathFunctions {

	public static String roundToDecimalPlacesStr(double number, int decimalPlaces) {
		if(decimalPlaces <= 0) return String.valueOf(Math.round(number));
		
		String format = "#.";
		for(int i=0; i<decimalPlaces; i++)
			format += '#';
		
		DecimalFormat df = new DecimalFormat(format);
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		return df.format(number);
	}
	
	public static double roundToDecimalPlaces(double number, int decimalPlaces) {
		return Double.parseDouble(roundToDecimalPlacesStr(number, decimalPlaces));
	}
	
	public static int numberOfDecimalPlaces(double number) {	
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
	
}
