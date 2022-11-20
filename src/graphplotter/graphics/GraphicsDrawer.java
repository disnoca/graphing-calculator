package graphplotter.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JComponent;

import functionComponents.Function;
import functionComponents.ReferentialLimits;

@SuppressWarnings("serial")
public class GraphicsDrawer extends JComponent {
	
	private Dimension size;
	private ReferentialLimits referentialLimits;
	private BufferedImage referentialGraphic;
	private ArrayList<FunctionGraphic> functionGraphics;
	

	public GraphicsDrawer(Dimension size, ReferentialLimits referentialLimits) {
		this.size = size;
		this.referentialLimits = referentialLimits;
		functionGraphics = new ArrayList<>();
	}
	
	public void setReferentialGraphic() {
		referentialGraphic = new ReferentialGraphic(size, referentialLimits);
	}
	
	public void addFunction(Function function, Color color) {
		FunctionGraphic functionGraphic = new FunctionGraphic(size, referentialLimits, function, color);
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
	
	@Override
    public void paintComponent(Graphics g){
		super.paintComponent(g);
		// referential is always the bottom layer
		g.drawImage(referentialGraphic, 0, 0, size.width, size.height, null);
		
        for(FunctionGraphic layer : functionGraphics)
            g.drawImage(layer, 0, 0, size.width, size.height, null);
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
		updateGraphics();
	}
	
	public void doubleReferentialLimits() {
		double[] limits = referentialLimits.getLimits();
		setReferentialLimits(limits[0]*2, limits[1]*2, limits[2]*2, limits[3]*2);
	}
	
	public void halveReferentialLimits() {
		double[] limits = referentialLimits.getLimits();
		setReferentialLimits(limits[0]/2, limits[1]/2, limits[2]/2, limits[3]/2);
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
			temp.add(new FunctionGraphic(size, referentialLimits, f, c));
		}
		
		functionGraphics = temp;
	}
	
}
