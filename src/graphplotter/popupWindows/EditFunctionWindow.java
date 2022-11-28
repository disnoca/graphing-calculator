package graphplotter.popupWindows;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.EmptyStackException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import graphplotter.SwingFunctions;
import graphplotter.graphics.GraphicsDrawer;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;

@SuppressWarnings("serial")
public class EditFunctionWindow extends PopupWindow {
	
	private GraphicsDrawer graphicsDrawer;
	private int functionPos;
	private String functionExpression;
	
	private JTextField textField;
	private JButton changeButton;
	

	public EditFunctionWindow(JFrame parent, String title, GraphicsDrawer graphicsDrawer, int functionPos, String functionExpression) {
		super(parent, title);
		this.functionExpression = functionExpression;
	}
	
	@Override
	protected void addComponents(Container contentPane) {
		changeButton = new JButton("Change");
		changeButton.setFocusable(false);
		changeButton.addActionListener(this);
		
		textField = new JTextField();
		textField.getInputMap().put(KeyStroke.getKeyStroke("pressed ENTER"), "enter");
		textField.getActionMap().put("enter", new SimulateButtonPressAction(changeButton));
		textField.setText(functionExpression);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(this);
		
		JPanel inputPane = new JPanel();
		inputPane.setLayout(new BoxLayout(inputPane, BoxLayout.PAGE_AXIS));
		
		JLabel label = new JLabel("Edit the function's expression:");
		inputPane.add(label);
		inputPane.add(Box.createRigidArea(new Dimension(0,10)));
		inputPane.add(textField);
		inputPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(changeButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(cancelButton);
		
		contentPane.add(inputPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);
		
		SwingFunctions.evenButtonsWidth(changeButton, cancelButton);
	}
	
	private void changeFunction(String newExpression) {
		graphicsDrawer.getFunction(functionPos).setExpression(newExpression);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == changeButton) {
			try {
				changeFunction(textField.getText().trim());
			} catch(UnknownFunctionOrVariableException e1) {
				SwingFunctions.showErrorMessage(this, "Invalid function");
				return;
			}	catch(EmptyStackException e2) {
				SwingFunctions.showErrorMessage(this, "Check the number of parentheses");
				return;
			} catch(IllegalArgumentException e3) {
				SwingFunctions.showErrorMessage(this, "Please enter a function");
				return;
			}
		}
		
		parent.setEnabled(true);
		this.dispose();
	}

}
