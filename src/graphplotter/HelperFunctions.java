package graphplotter;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class HelperFunctions {

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
	
}
