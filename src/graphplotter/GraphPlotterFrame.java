package graphplotter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import functionComponents.Function;
import graphplotter.graphics.GraphicsDrawer;
import graphplotter.popupWindows.ListFunctionsFrame;
import graphplotter.popupWindows.RemoveFunctionFrame;

@SuppressWarnings("serial")
public class GraphPlotterFrame extends JFrame implements ActionListener, WindowListener {
	
	private JMenuBar menubar;
	private JMenu menuFunc, menuVW, menuGS;
	private JMenuItem mfuncAdd, mfuncRemove, mfuncList;
	private JMenuItem vwDefault, vwSetValues, vwZoomIn, vwZoomOut;
	private JMenuItem gsRoot, gsMax, gsMin, gsYIntersect, gsIntersect, gsYCalc, gsXCalc, gsIntegral;
	
	private Dimension graphicsSize;
	private GraphicsDrawer graphicsDrawer;
	
	private RemoveFunctionFrame removeFunctionFrame;
	private ListFunctionsFrame listFunctionsFrame;
	
	private final int MAX_FUNCTIONS = 6;
	
	private final Color[] functionColors = {Color.BLUE, Color.RED, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.ORANGE};
	private Stack<Color> colorStack;
	private HashMap<Color, Integer> colorIdsMap;
	
	private int functionCount;
	

	public GraphPlotterFrame() {
		super("Graph Plotter");
	    this.setTitle("Graph Plotter");
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setResizable(false);
	    
	    graphicsSize = new Dimension(900,900);
	    functionCount = 0;
	    
	    initFunctionColors();
	    initSecondaryWindows();
		addMenuBar();
		initGraphics();
		
		this.setSize(graphicsSize);
		adjustSize();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void initFunctionColors() {
		colorStack = new Stack<>();
		colorIdsMap = new HashMap<>();
		
		for(int i = 0; i < MAX_FUNCTIONS; i++) {
			// colors must be pushed in reverse order for stack
			colorStack.push(functionColors[MAX_FUNCTIONS-1-i]);
			colorIdsMap.put(functionColors[i], i);
		}
	}
	
	private void addMenuBar() {
		menubar = new JMenuBar();
		
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
	
	private void initGraphics() {
		graphicsDrawer = new GraphicsDrawer(graphicsSize);
		graphicsDrawer.setReferentialGraphic();
		this.add(graphicsDrawer);
	}
	
	private void addFunction(String expression) {
		try {
			Color color = colorStack.pop();
			Function function = new Function(graphicsSize, expression, color);
			graphicsDrawer.addFunctionGraphic(function);
			functionCount++;
			updateContents();
		}
		
		// exception caught if expression has unknown symbols or is an empty string
		catch(IllegalArgumentException e) {
			this.showErrorMessage("Invalid function");
		}
	}
	
	private void removeFunctions() {
		boolean[] toRemove = removeFunctionFrame.getRemovedFunctions();
		
		// if Cancel button was pressed
		if(toRemove == null) return;
		
		// started with functionsNo functions before removal
		int oldFunctionCount = functionCount;
		
		// for loop must be done in reverse because removing an element changes the indexes of the ones following it
		for(int i = toRemove.length-1; i >= 0; i--)
			if(toRemove[i]) {
				Color color = graphicsDrawer.getFunctionColor(i);
				colorStack.add(color);
				graphicsDrawer.remove(i);
				functionCount--;
			}
		
		// checks if any functions were removed
		if(oldFunctionCount > functionCount)
			updateContents();
		else
			showErrorMessage("No functions were selected.");
	}
	
	private void initSecondaryWindows() {
		removeFunctionFrame = new RemoveFunctionFrame(this, "Remove Functions");
		removeFunctionFrame.addWindowListener(this);
		listFunctionsFrame = new ListFunctionsFrame(this, "Functions List");
		listFunctionsFrame.addWindowListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == mfuncAdd) {		//TODO: verify if function is not duplicate
			if(functionCount >= MAX_FUNCTIONS) {
				showErrorMessage("Maximum functions limit reached. Remove a function before adding a new one.");
				return;
			}
			
			// gets user input
			String expression = (String)JOptionPane.showInputDialog(this ,"Enter the function's expression:", "Add Function", JOptionPane.PLAIN_MESSAGE);
			
			// expression is null if user pressed Cancel or closed pop-up, in which case does nothing
			if(expression != null)
				this.addFunction(expression);
		}
		
		if(e.getSource() == mfuncRemove) {
			if(functionCount == 0)
				showErrorMessage("There are no functions to remove.");
			else
				removeFunctionFrame.showWindow(getFunctionExpressions());
		}
		
		if(e.getSource() == mfuncList) {
			if(functionCount == 0)
				showErrorMessage("There are no functions to list.");
			else
				listFunctionsFrame.showWindow(getFunctionExpressions(), getColorIds());
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
		String[] functionExpressions = new String[functionCount];
		for(int i = 0; i < functionCount; i++)
			functionExpressions[i] = graphicsDrawer.getFunctionExpression(i);
		
		return functionExpressions;
	}
	
	private int[] getColorIds() {
		int[] colorIds = new int[functionCount];
		for(int i = 0; i < functionCount; i++)
			colorIds[i] = colorIdsMap.get(graphicsDrawer.getFunctionColor(i));
		return colorIds;
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
	
	// for some reason the frame's size does not match the graphic's size visually, even though the values are the same
	// this function adjust the frame's size with a bias that makes it visually the same as the graphic's
	private void adjustSize() {
		Dimension size = this.getSize();
		size.width += 21;
		size.height += 63;
		this.setSize(size);
	}
	
	private void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
