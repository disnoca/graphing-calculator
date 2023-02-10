package graphingCalculator.popupWindows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import graphingCalculator.graphics.GraphicsDrawer;
import graphingCalculator.utils.SwingUtils;

@SuppressWarnings("serial")
public class RemoveFunctionWindow extends PopupWindow {
	
	private ArrayList<JCheckBox> checkboxes;
	private boolean[] activeCheckboxes;
	private JButton removeButton;
	
	private Stack<Color> colorStack;
	
	
	public RemoveFunctionWindow(JFrame parent, String title, GraphicsDrawer graphicsDrawer, Stack<Color> colorStack) {
		super(parent, title, graphicsDrawer);
		this.colorStack = colorStack;
	}
	
	protected void addComponents(Container contentPane) {
		String[] expressions = graphicsDrawer.getFunctionExpressions();
		
		removeButton = new JButton("Remove");
		removeButton.setFocusable(false);
		removeButton.addActionListener(this);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(this);
		
		JPanel listPane = new JPanel();
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
		
		JLabel label = new JLabel("Select functions to remove:");
		listPane.add(label);
		listPane.add(Box.createRigidArea(new Dimension(0,10)));
		JPanel checkboxPane = createCheckboxPane(expressions);
		listPane.add(checkboxPane);
		listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(removeButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(cancelButton);
		
		contentPane.add(listPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);
		
		SwingUtils.evenButtonsWidth(removeButton, cancelButton);
	}
	
	private JPanel createCheckboxPane(String[] expressions) {
		checkboxes = new ArrayList<>(expressions.length);
		activeCheckboxes = new boolean[expressions.length];
		
		for(int i = 0; i < expressions.length; i++) {
			String expression = expressions[i];
			JCheckBox checkbox = new JCheckBox(expression);
			checkboxes.add(checkbox);
			
			checkbox.setFocusable(false);
			checkbox.addItemListener(
				new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						int checkboxPos = checkboxes.indexOf(checkbox);
						activeCheckboxes[checkboxPos] = !activeCheckboxes[checkboxPos];
			        }
				}
			);
		}
		
		JPanel checkboxPane = new JPanel();
		checkboxPane.setLayout(new BoxLayout(checkboxPane, BoxLayout.PAGE_AXIS));
		checkboxes.forEach(checkbox -> checkboxPane.add(checkbox));
		
		return checkboxPane;
	}
	
	// returns true if at least one function was removed
	private boolean removeFunctions() {
		boolean removed = false;
		
		// for loop must be done in reverse because removing an element changes the indexes of the ones following it
		for(int i = activeCheckboxes.length-1; i >= 0; i--)
			if(activeCheckboxes[i]) {
				Color color = graphicsDrawer.getFunctionColor(i);
				colorStack.push(color);
				graphicsDrawer.removeFunction(i);
				removed = true;
			}
		
		if(removed)
			SwingUtils.updateFrameContents(parent);
		else
			SwingUtils.showErrorMessageDialog(this, "No functions were selected.");
		
		return removed;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// always closes window after a button is pressed unless user tried to remove no functions
		// in which case exits this method and window remains open
		if(e.getSource() == removeButton)
			if(!removeFunctions())
				return;
		
		parent.setEnabled(true);
		this.dispose();
	}
	
}
