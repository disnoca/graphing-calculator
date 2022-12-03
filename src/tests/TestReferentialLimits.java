package tests;

import static org.junit.Assert.*;

import java.awt.Dimension;

import org.junit.jupiter.api.Test;

import functionComponents.ReferentialLimits;

public class TestReferentialLimits {

	@Test
	void testFormattedLimits() {
		ReferentialLimits referentialLimits = new ReferentialLimits(new Dimension(1000, 1000), -0.005, 0.1, 1, 5000);
		String[] formatedLimits = referentialLimits.getFormattedLimits();
		
		assertEquals("-0.005", formatedLimits[0]);
		assertEquals("0.1", formatedLimits[1]);
		assertEquals("1", formatedLimits[2]);
		assertEquals("5000", formatedLimits[3]);
	}
	
}
