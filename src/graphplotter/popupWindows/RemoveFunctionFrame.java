package graphplotter.popupWindows;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class RemoveFunctionFrame extends PopupWindow {
	
	private ArrayList<JCheckBox> checkboxes;
	private boolean[] activeCheckboxes, functionsToRemove;
	private JButton removeButton, cancelButton;
	
	
	public RemoveFunctionFrame(JFrame parent, String title) {
		super(parent, title);
	}
	
	public void showWindow(String[] expressions) {	
		Container contentPane = getContentPane();
		contentPane.removeAll();
		addComponents(contentPane, expressions);
		functionsToRemove = null;
		
		this.showWindow(300);
	}
	
	private void addComponents(Container contentPane, String[] expressions) {
		removeButton = new JButton("Remove");
		removeButton.setFocusable(false);
		removeButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
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
		
		evenButtonsWidth(removeButton, cancelButton);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == removeButton) {
			functionsToRemove = activeCheckboxes;
		}
		
		parent.setEnabled(true);
		this.dispose();
	}
	
	public boolean[] getRemovedFunctions() {
		return functionsToRemove;
	}
	
}
