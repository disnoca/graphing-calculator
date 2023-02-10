package graphingCalculator.saver;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;

import functionComponents.ReferentialLimits;
import graphingCalculator.graphics.GraphicsDrawer;

public class GraphingCalculatorProjectSave implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ReferentialLimits referentialLimits;
	private HashMap<Color, String> functions;
	
	
	public GraphingCalculatorProjectSave(GraphicsDrawer graphicsDrawer) {
		referentialLimits = graphicsDrawer.getReferentialLimits();
		
		int functionCount = graphicsDrawer.getFunctionCount();
		functions = new HashMap<>(functionCount);
		for(int i = 0; i < functionCount; i++)
			functions.put(graphicsDrawer.getFunctionColor(i), graphicsDrawer.getFunctionExpression(i));
	}
	
	public ReferentialLimits getReferentiaLimits() {
		return referentialLimits;
	}
	
	public HashMap<Color, String> getFunctions() {
		return functions;
	}
	
}
