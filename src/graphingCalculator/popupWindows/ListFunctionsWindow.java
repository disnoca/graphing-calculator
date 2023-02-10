package graphingCalculator.popupWindows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
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

import graphingCalculator.graphics.GraphicsDrawer;
import graphingCalculator.utils.SwingUtils;

@SuppressWarnings("serial")
public class ListFunctionsWindow extends PopupWindow {
	
	private EditFunctionWindow editFunctionWindow;
	
	private ArrayList<JLabel> colorLabels, expressionLabels;
	
	private ArrayList<JButton> editButtons;
	private ArrayList<JButton> upButtons;
	private ArrayList<JButton> downButtons;
	
	private HashMap<Color, Integer> colorIdsMap;
	
	private Dimension buttonSize;
	
	
	public ListFunctionsWindow(JFrame parent, String title, GraphicsDrawer graphicsDrawer, HashMap<Color,Integer> colorIdsMap) {
		super(parent, title, graphicsDrawer);
		this.colorIdsMap = colorIdsMap;
		
		buttonSize = new Dimension(20,20);
		editFunctionWindow = new EditFunctionWindow(this, "Edit Function", graphicsDrawer, parent);
	}
	
	protected void addComponents(Container contentPane) {
		String[] expressions = graphicsDrawer.getFunctionExpressions();
		int[] colorIds = getColorIds();
		
		colorLabels = new ArrayList<>(expressions.length);
		expressionLabels = new ArrayList<>(expressions.length);
		
		editButtons = new ArrayList<>(expressions.length);
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
		
		// The expressions and colors must be listed in reverse because the functions that are listed first are the functions that are drawn last.
		// This would confuse the user, therefore we want to make it so that the higher the function is in the list, the more on top of other layers it is.
		for(int i = 0; i < expressions.length; i++) {
			// create edit buttons
			JButton editButton = new JButton();
			editButton.setFocusable(false);
			editButton.setMargin(new Insets(0,0,0,0));
			editButton.setOpaque(false);
			editButton.setContentAreaFilled(false);
			editButton.setBorderPainted(false);
			editButton.setIcon(new ImageIcon("assets\\edit_icon.png"));
			editButton.addActionListener(this);
			editButtons.add(editButton);
			
			// create color and expression labels
			String filename = "assets\\color"+colorIds[colorIds.length-i-1]+".jpg";
			ImageIcon icon = new ImageIcon(filename);
			JLabel imageLabel = new JLabel(icon);
			JLabel expressionLabel = new JLabel(expressions[expressions.length-i-1]);
			
			colorLabels.add(imageLabel);
			expressionLabels.add(expressionLabel);
			
			JPanel expressionPane = new JPanel();
			expressionPane.add(editButton);
			expressionPane.add(imageLabel);
			expressionPane.add(expressionLabel);
			listPane.add(expressionPane);
			listPane.add(Box.createRigidArea(new Dimension(0,5)));
			
			
			// create up and down buttons
			JButton upButton = new JButton();
			upButton.setFocusable(false);
			upButton.setMargin(new Insets(0,0,0,0));
			upButton.setOpaque(false);
			upButton.setContentAreaFilled(false);
			upButton.setBorderPainted(false);
			upButton.setIcon(new ImageIcon("assets\\up_arrow_icon.png"));
			upButton.addActionListener(this);
			upButtons.add(upButton);
			
			JButton downButton = new JButton();
			downButton.setFocusable(false);
			downButton.setMargin(new Insets(0,0,0,0));
			downButton.setOpaque(false);
			downButton.setContentAreaFilled(false);
			downButton.setBorderPainted(false);
			downButton.setIcon(new ImageIcon("assets\\down_arrow_icon.png"));
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
	
	public void resetLabels() {
		String[] expressions = graphicsDrawer.getFunctionExpressions();
		int[] colorIds = getColorIds();
		
		for(int i = 0; i < expressions.length; i++) {
			String filename = "assets\\color"+colorIds[colorIds.length-i-1]+".jpg";
			ImageIcon icon = new ImageIcon(filename);
			colorLabels.get(i).setIcon(icon);
			expressionLabels.get(i).setText(expressions[expressions.length-i-1]);;
		}
	}
	
	private int[] getColorIds() {
		int[] colorIds = new int[graphicsDrawer.getFunctionCount()];
		for(int i = 0; i < colorIds.length; i++)
			colorIds[i] = colorIdsMap.get(graphicsDrawer.getFunctionColor(i));
		return colorIds;
	}

	private void swapFunctions(int pos1, int pos2) {
		graphicsDrawer.swapFunctions(pos1, pos2);
		resetLabels();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Since the functions are listed in reverse, we need to reverse their positions again to the actual function's position in order to switch them correctly
		if(upButtons.contains(e.getSource())) {
			int pos = graphicsDrawer.getFunctionCount()-upButtons.indexOf(e.getSource())-1;
			swapFunctions(pos, pos+1);
			SwingUtils.updateFrameContents(parent);
		}
		
		if(downButtons.contains(e.getSource())) {
			int pos = graphicsDrawer.getFunctionCount()-downButtons.indexOf(e.getSource())-1;
			swapFunctions(pos, pos-1);
			SwingUtils.updateFrameContents(parent);
		}
		
		if(editButtons.contains(e.getSource())) {
			int pos = graphicsDrawer.getFunctionCount()-editButtons.indexOf(e.getSource())-1;
			editFunctionWindow.showWindow(graphicsDrawer.getFunction(pos));
		}
		
	}

}

