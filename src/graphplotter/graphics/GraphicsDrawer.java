package graphplotter.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;

import javax.swing.JComponent;

import functionComponents.Function;
import functionComponents.ReferentialLimits;
import graphplotter.saver.GraphPlotterProjectSave;

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
	
	@Override
    public void paintComponent(Graphics g){
		super.paintComponent(g);
		// referential is always the bottom layer
		g.drawImage(referentialGraphic, 0, 0, size.width, size.height, null);
		
        for(FunctionGraphic layer : functionGraphics)
            g.drawImage(layer, 0, 0, size.width, size.height, null);
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
		updateGraphics();
	}
	
	public void doubleReferentialLimits() {
		double xAdjustment = referentialLimits.getXLength()/2;
		double yAdjustment = referentialLimits.getYLength()/2;
		double[] limits = referentialLimits.getLimits();
		setReferentialLimits(limits[0]-xAdjustment, limits[1]+xAdjustment, limits[2]-yAdjustment, limits[3]+yAdjustment);
	}
	
	public void halveReferentialLimits() {
		double xAdjustment = referentialLimits.getXLength()/4;
		double yAdjustment = referentialLimits.getYLength()/4;
		double[] limits = referentialLimits.getLimits();
		setReferentialLimits(limits[0]+xAdjustment, limits[1]-xAdjustment, limits[2]+yAdjustment, limits[3]-yAdjustment);
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
	
	public void loadProject(GraphPlotterProjectSave save) {
		referentialLimits = save.getReferentiaLimits();
		setReferentialGraphic();
		
		functionGraphics.clear();
		for(Entry<Color, String> entry : save.getFunctions().entrySet()) {
			Function function = new Function(size, referentialLimits, entry.getValue());
			addFunction(function, entry.getKey());
		}
	}
	
}
