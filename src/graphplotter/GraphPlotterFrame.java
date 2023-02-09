package graphplotter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import functionComponents.Point;
import functionComponents.ReferentialLimits;
import graphplotter.graphics.GraphicsDrawer;
import graphplotter.popupWindows.AddFunctionWindow;
import graphplotter.popupWindows.GSolveXYValueWindow;
import graphplotter.popupWindows.ListFunctionsWindow;
import graphplotter.popupWindows.PopupWindow;
import graphplotter.popupWindows.RemoveFunctionWindow;
import graphplotter.popupWindows.SaveImageWindow;
import graphplotter.popupWindows.SetReferentialLimitsWindow;
import graphplotter.saver.GraphPlotterProjectFileFilter;
import graphplotter.saver.GraphPlotterProjectSave;

@SuppressWarnings("serial")
public class GraphPlotterFrame extends JFrame implements ActionListener, KeyListener, MouseListener {
	
	private JMenuBar menubar;
	private JMenu menuFile, menuFileSave, menuFileLoad;
	private JMenu menuFunc, menuVW, menuGS;
	private JMenuItem mfilesaveProject, mfilesaveImage, mfileloadProject;
	private JMenuItem mfuncAdd, mfuncRemove, mfuncList;
	private JMenuItem vwDefault, vwSetValues, vwZoomIn, vwZoomOut;
	private JMenuItem gsRoot, gsMax, gsMin, gsYIntersect, gsFuncIntersect, gsYVal, gsXVal, gsIntegral;
	
	private GraphicsDrawer graphicsDrawer;
	
	private PopupWindow saveImageWindow;
	private PopupWindow addFunctionWindow, removeFunctionWindow, listFunctionsWindow;
	private PopupWindow setReferentialLimitsWindow;
	private PopupWindow gSolveXYValueWindow;
	
	private final int MAX_FUNCTIONS = 6;
	
	private final Color[] functionColors = {Color.BLUE, Color.RED, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.ORANGE};
	private Stack<Color> colorStack;
	private HashMap<Color, Integer> colorIdsMap;
	
	private final double DEFAULT_MINX = -10;
	private final double DEFAULT_MAXX = 10;
	private final double DEFAULT_MINY = -10;
	private final double DEFAULT_MAXY = 10;
	
	private Timer screenResizeTimer, referentialZoomTimer;
	private boolean screenResizeScheduled, referentialZoomScheduled;
	private int referentialZoomsScheduledCount;
	
	private GSolveState gSolveState;
	

	public GraphPlotterFrame() {
		super("Graph Plotter");
	    this.setTitle("Graph Plotter");
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setResizable(true);	
	    this.setSize(1000,1000);
	    
	    initFunctionColors();
		addMenuBar();
		initGraphics();
		initPopupWindows();
		setupListeners();
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		gSolveState = GSolveState.NONE;
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
		
		menuFile = new JMenu("File");
		menuFileSave = new JMenu("Save");
		menuFileLoad = new JMenu("Load");
		menuFunc = new JMenu("Function");
	    menuVW = new JMenu("View Window");
	    menuGS = new JMenu("G-Solve");
	    
	    mfilesaveProject = new JMenuItem("Project");
	    mfilesaveImage = new JMenuItem("Image");
	    mfileloadProject = new JMenuItem("Project");
	    
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
	    gsFuncIntersect = new JMenuItem("Intersection between two functions");
	    gsYVal = new JMenuItem("Y-Value");
	    gsXVal = new JMenuItem("X-Value");
	    gsIntegral = new JMenuItem("Integral");
	    
	    mfilesaveProject.addActionListener(this);
	    mfilesaveImage.addActionListener(this);
	    mfileloadProject.addActionListener(this);
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
	    gsFuncIntersect.addActionListener(this);
	    gsYVal.addActionListener(this);
	    gsXVal.addActionListener(this);
	    gsIntegral.addActionListener(this);
	    
	    menuFileSave.add(mfilesaveProject);
	    menuFileSave.add(mfilesaveImage);
	    menuFileLoad.add(mfileloadProject);
	    menuFile.add(menuFileSave);
	    menuFile.add(menuFileLoad);
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
	    menuGS.add(gsFuncIntersect);
	    menuGS.add(gsYVal);
	    menuGS.add(gsXVal);
	    menuGS.add(gsIntegral);
	    menubar.add(menuFile);
	    menubar.add(menuFunc);
	    menubar.add(menuVW);
	    menubar.add(menuGS);
	    this.setJMenuBar(menubar);
	}
	
	private void initGraphics() {
		ReferentialLimits referentialLimits = new ReferentialLimits(drawingAreaSize(), DEFAULT_MINX, DEFAULT_MAXX, DEFAULT_MINY, DEFAULT_MAXY);
		graphicsDrawer = new GraphicsDrawer(drawingAreaSize(), referentialLimits);
		graphicsDrawer.setReferentialGraphic();
		this.add(graphicsDrawer);
	}
	
	private void initPopupWindows() {
		saveImageWindow = new SaveImageWindow(this, "Save Image", graphicsDrawer);
		addFunctionWindow = new AddFunctionWindow(this, "Add Function", graphicsDrawer, colorStack);
		removeFunctionWindow = new RemoveFunctionWindow(this, "Remove Functions", graphicsDrawer, colorStack);
		listFunctionsWindow = new ListFunctionsWindow(this, "Functions List", graphicsDrawer, colorIdsMap);
		setReferentialLimitsWindow = new SetReferentialLimitsWindow(this, "Set Referential Limits", graphicsDrawer);
		gSolveXYValueWindow = new GSolveXYValueWindow(this, graphicsDrawer);
	}
	
	private void saveProject() throws IOException {
		String filePath = SwingFunctions.showSaveFileDialog(this, "gpp", new GraphPlotterProjectFileFilter());
		if(filePath == null) return;
		
		GraphPlotterProjectSave save = new GraphPlotterProjectSave(graphicsDrawer);
		FileOutputStream fileOutputStream = new FileOutputStream(filePath);
	    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
	    objectOutputStream.writeObject(save);
	    objectOutputStream.flush();
	    objectOutputStream.close();
	}
	
	private void loadProject() throws ClassNotFoundException, IOException {
		File file = SwingFunctions.showLoadFileDialog(this);
		if(file == null) return;
		
		FileInputStream fileInputStream = new FileInputStream(file);
	    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
	    GraphPlotterProjectSave save = (GraphPlotterProjectSave) objectInputStream.readObject();
	    objectInputStream.close();
		
		initFunctionColors();
		int savedFunctions = save.getFunctions().keySet().size();
		for(int i = 0; i < savedFunctions; i++)
			colorStack.pop();
		
		graphicsDrawer.loadProject(save);
		SwingFunctions.updateFrameContents(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == mfilesaveProject) {
			try {
				saveProject();
			} catch (IOException e1) {
				e1.printStackTrace();
				SwingFunctions.showErrorMessageDialog(this, "There was a problem saving the project.");
			}
		}
		
		if(e.getSource() == mfilesaveImage) {
			saveImageWindow.showWindow();
		}
		
		if(e.getSource() == mfileloadProject) {
			try {
				loadProject();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
				SwingFunctions.showErrorMessageDialog(this, "There was a problem loading the project.");
			}
		}
		
		if(e.getSource() == mfuncAdd) {		//TODO: verify if function is not duplicate
			if(graphicsDrawer.getFunctionCount() >= MAX_FUNCTIONS)
				SwingFunctions.showErrorMessageDialog(this, "Maximum functions limit reached. Remove a function before adding a new one.");
			else
				addFunctionWindow.showWindow();
		}
		
		if(e.getSource() == mfuncRemove) {
			if(graphicsDrawer.getFunctionCount() == 0)
				SwingFunctions.showErrorMessageDialog(this, "There are no functions to remove.");
			else
				removeFunctionWindow.showWindow();
		}
		
		if(e.getSource() == mfuncList) {
			if(graphicsDrawer.getFunctionCount() == 0)
				SwingFunctions.showErrorMessageDialog(this, "There are no functions to list.");
			else
				listFunctionsWindow.showWindow();
		}
		
		if(e.getSource() == vwDefault) {
			graphicsDrawer.setReferentialLimits(DEFAULT_MINX, DEFAULT_MAXX, DEFAULT_MINY, DEFAULT_MAXY);
			SwingFunctions.updateFrameContents(this);
		}
		
		if(e.getSource() == vwSetValues) {
			setReferentialLimitsWindow.showWindow();
		}
		
		if(e.getSource() == vwZoomIn) {
			graphicsDrawer.halveReferentialLimits();
			SwingFunctions.updateFrameContents(this);
		}
		
		if(e.getSource() == vwZoomOut) {
			graphicsDrawer.doubleReferentialLimits();
			SwingFunctions.updateFrameContents(this);
		}
		
		if(e.getSource() == gsRoot) {
			
		}
		
		if(e.getSource() == gsMax) {
			
		}
		
		if(e.getSource() == gsMin) {
			
		}
		
		if(e.getSource() == gsYIntersect) {
			
		}
		
		if(e.getSource() == gsFuncIntersect) {
			
		}
		
		if(e.getSource() == gsYVal) {
			if(graphicsDrawer.getFunctionCount() == 0) {
				SwingFunctions.showErrorMessageDialog(this, "There are no functions to G-Solve");
				return;
			}
			
			gSolveState = GSolveState.Y_VALUE;
			((GSolveXYValueWindow) gSolveXYValueWindow).setGSolveState(gSolveState);
			gSolveXYValueWindow.showWindow();
		}
		
		if(e.getSource() == gsXVal) {
			
		}
		
		if(e.getSource() == gsIntegral) {
			
		}
		
	}
	
	private Dimension drawingAreaSize() {
		Dimension size = this.getSize();
		size.width -= 16;
		size.height -= 62;
		return size;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(gSolveState == GSolveState.NONE) return;
		int keyVal = e.getKeyCode();
		
		if(keyVal == KeyEvent.VK_RIGHT) {
			
		}
		else if(keyVal == KeyEvent.VK_RIGHT) {
			
		}
	}
	
	private Point mousePressedPoint;
	
	@Override
	public void mousePressed(MouseEvent e) {
		mousePressedPoint = new Point(e.getX(), e.getY(), drawingAreaSize(), graphicsDrawer.getReferentialLimits().getLimits());
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		Point mouseReleasedPoint = new Point(e.getX(), e.getY(), drawingAreaSize(), graphicsDrawer.getReferentialLimits().getLimits());
		double xDiff = mousePressedPoint.getX()-mouseReleasedPoint.getX();
		double yDiff = mousePressedPoint.getY()-mouseReleasedPoint.getY();
		
		graphicsDrawer.moveOriginLocation(xDiff, yDiff);
		SwingFunctions.updateFrameContents(this);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
	/*
	 * When dragging the screen or scrolling, the graphics drawer tries to draw for every single changed frame
	 * through the process of resizing the window/zooming the referential, even though only the last frame is required.
	 * Since the all the functions must be recaulculated on every draw, the program would be stuck for several seconds
	 * processing frames that didn't need to be.
	 * 
	 * These blocks of code are for these two cases. What they do is the graphics drawer will only start
	 * drawing a new frame after a certain delay has passed. This delay must be small enough so that the user doesn't
	 * notice too much lag after one of the above mentioned actions but big enough that it lets the user keep
	 * dragging the window/scrolling without the graphics drawer being asked to draw unecessary frames.
	 */
	private final int ACTION_DELAY = 25;
	
	public void setupListeners() {
		this.addKeyListener(this);
		this.addMouseListener(this);
		
		screenResizeTimer = new Timer();
		screenResizeScheduled = false;
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				if(SwingFunctions.resizeUpdateState > 0) {
				    SwingFunctions.resizeUpdateState--;
				    return;
				}
				    	
				if(screenResizeScheduled) {
				    screenResizeTimer.cancel();
				    screenResizeTimer = new Timer();
				}
				screenResizeScheduled = true;
				    	
				screenResizeTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						graphicsDrawer.setFrameSize(drawingAreaSize());
						SwingFunctions.updateFrameContents(GraphPlotterFrame.this);
						screenResizeScheduled = false;
					}
				}, ACTION_DELAY);
			}
		});
		
		
		referentialZoomTimer = new Timer();
		referentialZoomScheduled = false;
		referentialZoomsScheduledCount = 0;
		this.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(referentialZoomScheduled) {
					referentialZoomTimer.cancel();
					referentialZoomTimer = new Timer();
				}
				referentialZoomScheduled = true;
				referentialZoomsScheduledCount += e.getWheelRotation();
				
				screenResizeTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						graphicsDrawer.zoomReferentialLimitsBy(-referentialZoomsScheduledCount);
						SwingFunctions.updateFrameContents(GraphPlotterFrame.this);
						referentialZoomScheduled = false;
						referentialZoomsScheduledCount = 0;
					}
				}, ACTION_DELAY);
			}
		});
	}
	
	
	public static void main(String[] args) {
		new GraphPlotterFrame();
	}
	
	// TODO:
	// mark point where mouse clicked and show its coordinates
	
	// Issues:
	// non-existent points/lines in functions like tan(x) and 1/x are being drawn 
	// functions like tan(x) and 1/x produce wrong results when applied the mathematical algorithms
	// functions that have y values tending to infinity are only drawn to a certain extent
	
}
