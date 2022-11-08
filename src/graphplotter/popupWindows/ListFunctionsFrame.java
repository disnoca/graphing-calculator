package graphplotter.popupWindows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import graphplotter.SwingFunctions;
import graphplotter.graphics.GraphicsDrawer;

@SuppressWarnings("serial")
public class ListFunctionsFrame extends PopupWindow {
	
	private ArrayList<JButton> upButtons;
	private ArrayList<JButton> downButtons;
	
	private GraphicsDrawer graphicsDrawer;
	private HashMap<Color, Integer> colorIdsMap;
	
	private Dimension buttonSize;
	
	private boolean swaped;
	
	
	public ListFunctionsFrame(JFrame parent, String title, GraphicsDrawer graphicsDrawer, HashMap<Color,Integer> colorIdsMap) {
		super(parent, title);
		this.graphicsDrawer = graphicsDrawer;
		this.colorIdsMap = colorIdsMap;
		
		buttonSize = new Dimension(20,20);
	}
	
	@Override
	public void showWindow() {
		super.showWindow();
		swaped = false;
	}
	
	protected void addComponents(Container contentPane) {
		String[] expressions = graphicsDrawer.getFunctionExpressions();
		int[] colorIds = getColorIds();
		
		upButtons = new ArrayList<>(expressions.length);
		downButtons = new ArrayList<>(expressions.length);
		
		// pane containing the label and expressions list
		JPanel leftPane = new JPanel();
		leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.PAGE_AXIS));
		
		JLabel label = new JLabel("Functions list:");
		leftPane.add(label);
		leftPane.add(Box.createRigidArea(new Dimension(0,5)));
		leftPane.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
		
		JPanel listPane = new JPanel();
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.PAGE_AXIS));
		buttonPane.add(Box.createRigidArea(new Dimension(0,25)));
		
		for(int i = 0; i < expressions.length; i++) {
			// create color and expression labels
			String filename = "assets\\color"+colorIds[i]+".jpg";
			ImageIcon icon = new ImageIcon(filename);
			JLabel imageLabel = new JLabel(icon);
			JLabel expressionLabel = new JLabel(expressions[i]);
			
			JPanel expressionPane = new JPanel();
			expressionPane.add(imageLabel);
			expressionPane.add(expressionLabel);
			listPane.add(expressionPane);
			listPane.add(Box.createRigidArea(new Dimension(0,5)));
			
			
			// create buttons
			JButton upButton = new JButton("^");
			upButton.setFocusable(false);
			upButton.setMargin(new Insets(0,0,0,0));
			upButton.setPreferredSize(buttonSize);
			upButton.addActionListener(this);
			upButtons.add(upButton);
			
			JButton downButton = new JButton("v");
			downButton.setFocusable(false);
			downButton.setMargin(new Insets(0,0,0,0));
			downButton.setPreferredSize(buttonSize);
			downButton.addActionListener(this);
			downButtons.add(downButton);
			
			JPanel buttonSetPane = new JPanel();
			
			// add blank space instead of button if function can't be swapped
			// more specifically, replace the up button for the first function and the down button for the last
			if(i == 0)
				buttonSetPane.add(Box.createRigidArea(buttonSize));
			else
				buttonSetPane.add(upButton);
			
			if(i == expressions.length-1)
				buttonSetPane.add(Box.createRigidArea(buttonSize));
			else
				buttonSetPane.add(downButton);
			
			buttonPane.add(buttonSetPane);
		}
		
		leftPane.add(listPane);
		contentPane.add(leftPane, BorderLayout.WEST);
		contentPane.add(buttonPane, BorderLayout.CENTER);
	}
	
	private int[] getColorIds() {
		int[] colorIds = new int[graphicsDrawer.getFunctionCount()];
		for(int i = 0; i < colorIds.length; i++)
			colorIds[i] = colorIdsMap.get(graphicsDrawer.getFunctionColor(i));
		return colorIds;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		swaped = true;
		
		if(upButtons.contains(e.getSource())) {
			int pos = upButtons.indexOf(e.getSource());
			swapFunctions(pos, pos-1);
		}
		
		if(downButtons.contains(e.getSource())) {
			int pos = downButtons.indexOf(e.getSource());
			swapFunctions(pos, pos+1);
		}
		
	}
	
	private void swapFunctions(int pos1, int pos2) {
		graphicsDrawer.swapFunctions(pos1, pos2);
		super.resetContainer();
		SwingFunctions.updateFrameContents(this);
	}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		parent.setEnabled(true);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		if(swaped)
			SwingFunctions.updateFrameContents(parent);
	}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}
	
}
