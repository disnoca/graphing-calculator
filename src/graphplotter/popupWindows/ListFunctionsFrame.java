package graphplotter.popupWindows;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ListFunctionsFrame extends PopupWindow {
	
	private ArrayList<JButton> upButtons;
	private ArrayList<JButton> downButtons;
	
	private Dimension buttonSize;
	
	
	public ListFunctionsFrame(JFrame parent, String title) {
		super(parent, title);
		buttonSize = new Dimension(20,20);
	}
	
	public void showWindow(String[] expressions, int[] colorIds) {
		Container contentPane = getContentPane();
		contentPane.removeAll();
		addComponents(contentPane, expressions, colorIds);
		
		super.showWindow(300);
	}
	
	private void addComponents(Container contentPane, String[] expressions, int[] colorIds) {
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
			
			JPanel itemPane = new JPanel();
			itemPane.add(imageLabel);
			itemPane.add(expressionLabel);
			listPane.add(itemPane);
			listPane.add(Box.createRigidArea(new Dimension(0,5)));
			
			
			// create buttons
			JButton upButton = new JButton("^");
			upButton.setFocusable(false);
			upButton.setMargin(new Insets(0,0,0,0));
			upButton.setPreferredSize(buttonSize);
			upButtons.add(upButton);
			
			JButton downButton = new JButton("v");
			downButton.setFocusable(false);
			downButton.setMargin(new Insets(0,0,0,0));
			downButton.setPreferredSize(buttonSize);
			downButtons.add(downButton);
			if(i == expressions.length-1) downButton.setVisible(false);
			
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

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(upButtons.contains(e.getSource())) {
			int pos = upButtons.indexOf(e.getSource());
			swapFunctions(pos, pos-1);
		}
		
		if(downButtons.contains(e.getSource())) {
			int pos = upButtons.indexOf(e.getSource());
			swapFunctions(pos, pos+1);
		}
		
	}
	
	private void swapFunctions(int pos1, int pos2) {
		
	}
	
}
