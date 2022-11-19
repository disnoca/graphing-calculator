package graphplotter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import functionComponents.ReferentialLimits;
import graphplotter.graphics.GraphicsDrawer;
import graphplotter.popupWindows.AddFunctionWindow;
import graphplotter.popupWindows.ListFunctionsWindow;
import graphplotter.popupWindows.PopupWindow;
import graphplotter.popupWindows.RemoveFunctionWindow;

@SuppressWarnings("serial")
public class GraphPlotterFrame extends JFrame implements ActionListener {
	
	private JMenuBar menubar;
	private JMenu menuFunc, menuVW, menuGS;
	private JMenuItem mfuncAdd, mfuncRemove, mfuncList;
	private JMenuItem vwDefault, vwSetValues, vwZoomIn, vwZoomOut;
	private JMenuItem gsRoot, gsMax, gsMin, gsYIntersect, gsIntersect, gsYCalc, gsXCalc, gsIntegral;
	
	private GraphicsDrawer graphicsDrawer;
	
	private PopupWindow addFunctionWindow, removeFunctionWindow, listFunctionsWindow;
	
	private final int MAX_FUNCTIONS = 6;
	
	private final Color[] functionColors = {Color.BLUE, Color.RED, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.ORANGE};
	private Stack<Color> colorStack;
	private HashMap<Color, Integer> colorIdsMap;
	
	public final double STARTING_MINX = -10;
	public final double STARTING_MAXX = 10;
	public final double STARTING_MINY = -10;
	public final double STARTING_MAXY = 10;
	

	public GraphPlotterFrame() {
		super("Graph Plotter");
	    this.setTitle("Graph Plotter");
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setResizable(false);	// TODO: change to true once I find how to only resize after mouse released
	    this.setSize(1000,1000);
	    
	    initFunctionColors();
		addMenuBar();
		initGraphics();
		initSecondaryWindows();
		
		this.addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) {
		        graphicsDrawer.recalculateFrameSize(drawingAreaSize());
		    }
		});
		
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
		ReferentialLimits referentialLimits = new ReferentialLimits(drawingAreaSize(), STARTING_MINX, STARTING_MAXX, STARTING_MINY, STARTING_MAXY);
		graphicsDrawer = new GraphicsDrawer(drawingAreaSize(), referentialLimits);
		graphicsDrawer.setReferentialGraphic();
		this.add(graphicsDrawer);
	}
	
	private void initSecondaryWindows() {
		addFunctionWindow = new AddFunctionWindow(this, "Add Function", graphicsDrawer, colorStack);
		removeFunctionWindow = new RemoveFunctionWindow(this, "Remove Functions", graphicsDrawer, colorStack);
		listFunctionsWindow = new ListFunctionsWindow(this, "Functions List", graphicsDrawer, colorIdsMap);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == mfuncAdd) {		//TODO: verify if function is not duplicate
			if(graphicsDrawer.getFunctionCount() >= MAX_FUNCTIONS)
				SwingFunctions.showErrorMessage(this, "Maximum functions limit reached. Remove a function before adding a new one.");
			else
				addFunctionWindow.showWindow();
		}
		
		if(e.getSource() == mfuncRemove) {
			if(graphicsDrawer.getFunctionCount() == 0)
				SwingFunctions.showErrorMessage(this, "There are no functions to remove.");
			else
				removeFunctionWindow.showWindow();
		}
		
		if(e.getSource() == mfuncList) {
			if(graphicsDrawer.getFunctionCount() == 0)
				SwingFunctions.showErrorMessage(this, "There are no functions to list.");
			else
				listFunctionsWindow.showWindow();
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
	
	public Dimension drawingAreaSize() {
		Dimension size = this.getSize();
		size.width -= 21;
		size.height -= 63;
		return size;
	}
	
	public static void main(String[] args) {
		new GraphPlotterFrame();
	}
	
}
