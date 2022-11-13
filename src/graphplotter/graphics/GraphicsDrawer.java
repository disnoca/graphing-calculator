package graphplotter.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JComponent;

import functionComponents.Function;

@SuppressWarnings("serial")
public class GraphicsDrawer extends JComponent {
	
	private Dimension size;
	private BufferedImage referentialGraphic;
	private ArrayList<FunctionGraphic> functionGraphics;
	

	public GraphicsDrawer(Dimension size) {
		this.size = size;
		functionGraphics = new ArrayList<>();
	}
	
	public void setReferentialGraphic() {
		referentialGraphic = new ReferentialGraphic(size);
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
	
	@Override
    public void paintComponent(Graphics g){
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
	
	public void recalculateFrameSize(Dimension size) {
		this.size = size;
		setReferentialGraphic();
		
		// todo redraw functions
	}
	
}
