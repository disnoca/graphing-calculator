package graphplotter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class GraphPlotterFrame extends JFrame implements ActionListener, WindowListener {
	
	private JMenu menuFunc, menuVW, menuGS;
	private JMenuItem mfuncAdd, mfuncRemove, mfuncList;
	private JMenuItem vwDefault, vwSetValues, vwZoomIn, vwZoomOut;
	private JMenuItem gsRoot, gsMax, gsMin, gsYIntersect, gsIntersect, gsYCalc, gsXCalc, gsIntegral;
	
	private ReferentialGraphic referentialGraphic;
	private ArrayList<FunctionGraphic> functionGraphics;
	
	private RemoveFunctionFrame removeFunctionFrame;
	
	private final int MAX_FUNCTIONS = 5;
	
	private final Color[] functionColors = {Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA, Color.CYAN};
	

	public GraphPlotterFrame() {
		super("Graph Plotter");
		
	    this.setSize(1000, 1000);
	    this.setTitle("Graph Plotter");
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    this.setResizable(false);
	    this.setVisible(true);
	    
	    functionGraphics = new ArrayList<>();
	    
		addMenuBar();
		drawReferential();
		initSecondaryFrames();
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
	
	private void drawReferential() {
		referentialGraphic = new ReferentialGraphic();
		this.add(referentialGraphic);
		updateContents();
	}
	
	private void drawFunction(String expression) {
		try {
			Color color = functionColors[functionGraphics.size()];
			FunctionGraphic fg = new FunctionGraphic(expression, color);
			functionGraphics.add(fg);
			
			this.add(fg);
			updateContents();
		}
		
		// exception caught if expression has unknown symbols or is an empty string
		catch(IllegalArgumentException e) {
			this.showErrorMessage("Invalid function");
		}
	}
	
	private void initSecondaryFrames() {
		removeFunctionFrame = new RemoveFunctionFrame(this);
		removeFunctionFrame.addWindowListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == mfuncAdd) {		//TODO: verify if function is not duplicate
			if(functionGraphics.size() >= MAX_FUNCTIONS) {
				showErrorMessage("Maximum functions limit reached. Remove a function before adding a new one.");
				return;
			}
			
			// gets user input
			String expression = (String)JOptionPane.showInputDialog(this ,"Enter the function's expression:", "Add Function", JOptionPane.PLAIN_MESSAGE);
			
			// expression is null if user pressed Cancel or closed pop-up, in which case does nothing
			if(expression != null)
				this.drawFunction(expression);
		}
		
		if(e.getSource() == mfuncRemove) {
			if(functionGraphics.size() == 0) {
				showErrorMessage("There are no functions no remove.");
				return;
			}
			
			removeFunctionFrame.showWindow(getFunctionExpressions());
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
	
	private String[] getFunctionExpressions() {
		String[] functionExpressions = new String[functionGraphics.size()];
		for(int i = 0; i < functionGraphics.size(); i++)
			functionExpressions[i] = functionGraphics.get(i).getExpression();
		
		return functionExpressions;
	}

	
	// these WindowListener functions are listening to the secondary windows' window changes, not itself
	
	@Override
	public void windowOpened(WindowEvent e) {
		this.setEnabled(false);
	}

	// only activates when the X button is pressed
	@Override
	public void windowClosing(WindowEvent e) {
		this.setEnabled(true);
	}
	
	// setEnabled is done inside secondary window when button is pressed
	@Override
	public void windowClosed(WindowEvent e) {
		String source = e.getSource().getClass().getSimpleName();
		
		switch(source) {
		case("RemoveFunctionFrame"): removeFunctions(); break;
		}
	}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}
	
	private void removeFunctions() {
		boolean[] toRemove = removeFunctionFrame.getRemovedFunctions();
		// if Cancel button was pressed
		if(toRemove == null) return;
		
		// started with functionsNo functions before removal
		int oldFunctionsCount = functionGraphics.size();
		
		// for loop must be done in reverse because removing an element changes the indexs of the ones following it
		for(int i = toRemove.length-1; i >= 0; i--)
			if(toRemove[i]) {
				FunctionGraphic fg = functionGraphics.get(i);
				this.remove(fg);
				functionGraphics.remove(i);
			}
		
		// checks if any functions were removed
		if(oldFunctionsCount > functionGraphics.size())
			updateContents();
		else
			showErrorMessage("No functions were selected.");
	}
	
	// for some reason the frame must be set as non visible and then visible or be resized for its contents to be updated
	// since changing visible values creates visual problems, this method resizes the frame
	private void updateContents() {
		Dimension size = this.getSize();
		int width = size.width;
		int height = size.height;
		Dimension tempSize = new Dimension(width+1, height+1);
		
		this.setSize(tempSize);
		this.setSize(size);
	}
	
	private void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
