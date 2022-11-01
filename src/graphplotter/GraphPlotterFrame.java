package graphplotter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class GraphPlotterFrame extends JFrame implements ActionListener {
	
	private JMenu menuFunc, menuVW, menuGS;
	private JMenuItem mfuncAdd, mfuncRemove, mfuncList;
	private JMenuItem vwDefault, vwSetValues, vwZoomIn, vwZoomOut;
	private JMenuItem gsRoot, gsMax, gsMin, gsYIntersect, gsIntersect, gsYCalc, gsXCalc, gsIntegral;
	
	private ReferentialGraphic referentialGraphic;
	private ArrayList<FunctionGraphic> functionGraphics;
	
	private final int MAX_FUNCTIONS = 5;
	
	private final Color[] functionColors = {Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA, Color.CYAN};
	

	public GraphPlotterFrame() {
		super();
		
	    this.setSize(1000, 1000);
	    this.setTitle("Graph Plotter");
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    this.setResizable(false);
	    this.setVisible(true);
	    
	    functionGraphics = new ArrayList<>();
	    
		addMenuBar();
	}
	
	private void addMenuBar() {
		JMenuBar menubar = new JMenuBar();
		
		menuFunc = new JMenu("Function");
	    menuVW = new JMenu("View Window");
	    menuGS = new JMenu("G-Solve");
	    
	    mfuncAdd = new JMenuItem("Add");
	    mfuncRemove = new JMenuItem("Remove");
	    mfuncList = new JMenuItem("List");
	    
	    vwDefault = new JMenuItem("Default");
	    vwSetValues = new JMenuItem("Set Values");
	    vwZoomIn = new JMenuItem("Zoom In");
	    vwZoomOut = new JMenuItem("Zoom Out");
	    
	    gsRoot = new JMenuItem("Root");
	    gsMax = new JMenuItem("Maximum");
	    gsMin = new JMenuItem("Minimum");
	    gsYIntersect = new JMenuItem("Intersection with the Y-Axis");
	    gsIntersect = new JMenuItem("Intersection between two functions");
	    gsYCalc = new JMenuItem("Y-Value");
	    gsXCalc = new JMenuItem("X-Value");
	    gsIntegral = new JMenuItem("Integral");
	    
	    mfuncAdd.addActionListener(this);
	    mfuncRemove.addActionListener(this);
	    mfuncList.addActionListener(this);
	    vwDefault.addActionListener(this);
	    vwSetValues.addActionListener(this);
	    vwZoomIn.addActionListener(this);
	    vwZoomOut.addActionListener(this);
	    gsRoot.addActionListener(this);
	    gsMax.addActionListener(this);
	    gsMin.addActionListener(this);
	    gsYIntersect.addActionListener(this);
	    gsIntersect.addActionListener(this);
	    gsYCalc.addActionListener(this);
	    gsXCalc.addActionListener(this);
	    gsIntegral.addActionListener(this);
	    
	    menuFunc.add(mfuncAdd);
	    menuFunc.add(mfuncRemove);
	    menuFunc.add(mfuncList);
	    menuVW.add(vwDefault);
	    menuVW.add(vwSetValues);
	    menuVW.add(vwZoomIn);
	    menuVW.add(vwZoomOut);    
	    menuGS.add(gsRoot);
	    menuGS.add(gsMax);
	    menuGS.add(gsMin);
	    menuGS.add(gsYIntersect);
	    menuGS.add(gsIntersect);
	    menuGS.add(gsYCalc);
	    menuGS.add(gsXCalc);
	    menuGS.add(gsIntegral);
	    
	    menubar.add(menuFunc);
	    menubar.add(menuVW);
	    menubar.add(menuGS);
	    this.setJMenuBar(menubar);
	}
	
	public void drawReferential() {
		referentialGraphic = new ReferentialGraphic();
		this.add(referentialGraphic);
		this.setVisible(true);
	}
	
	public void drawFunction(String expression) {	//TODO: draw newer functions over old ones
		try {
			FunctionGraphic fg = new FunctionGraphic(expression, functionColors[functionGraphics.size()]);
			functionGraphics.add(fg);
			this.add(fg);
			this.setVisible(true);
		}
		catch(Exception e) {
			this.showErrorMessage("Invalid function");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// TODO: error for adding more than MAX_FUNCTIONS, make cancel button work, make the window not close when a function was not written or was invalid
		if(e.getSource() == mfuncAdd) {
			String expression = (String)JOptionPane.showInputDialog(this ,"Enter the function expression:", "Add Function", JOptionPane.PLAIN_MESSAGE);;
			
			if(expression != null && expression.length() > 0)
				this.drawFunction(expression);
			else
				this.showErrorMessage("Please enter a function");
		}
		
		if(e.getSource() == mfuncRemove) {
			
		}
		
		if(e.getSource() == mfuncList) {
			
		}
		
		if(e.getSource() == vwDefault) {
			
		}
		
		if(e.getSource() == vwSetValues) {
			
		}
		
		if(e.getSource() == vwZoomIn) {
			
		}
		
		if(e.getSource() == vwZoomOut) {
			
		}
		
		if(e.getSource() == gsRoot) {
			
		}
		
		if(e.getSource() == gsMax) {
			
		}
		
		if(e.getSource() == gsMin) {
			
		}
		
		if(e.getSource() == gsYIntersect) {
			
		}
		
		if(e.getSource() == gsIntersect) {
			
		}
		
		if(e.getSource() == gsYCalc) {
			
		}
		
		if(e.getSource() == gsXCalc) {
			
		}
		
		if(e.getSource() == gsIntegral) {
			
		}
		
	}
	
	private void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
