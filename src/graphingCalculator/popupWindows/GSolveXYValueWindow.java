package graphingCalculator.popupWindows;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import graphingCalculator.GSolveState;
import graphingCalculator.SwingFunctions;
import graphingCalculator.graphics.GraphicsDrawer;

@SuppressWarnings("serial")
public class GSolveXYValueWindow extends PopupWindow {
	
	private JButton okButton;
	private JTextField textField;

	private GSolveState gSolveState;

	public GSolveXYValueWindow(JFrame parent, GraphicsDrawer graphicsDrawer) {
		super(parent, "", graphicsDrawer);
	}
	
	public void setGSolveState(GSolveState gSolveState) {
		this.gSolveState = gSolveState;
	}
	
	@Override
	protected void addComponents(Container contentPane) {
		this.setTitle(gSolveState.getTitle());
		
		okButton = new JButton("Ok");
		okButton.setFocusable(false);
		okButton.addActionListener(this);
		
		textField = new JTextField();
		// when textField is selected and user presses enter, Ok button is automatically clicked
		textField.getInputMap().put(KeyStroke.getKeyStroke("pressed ENTER"), "enter");
		textField.getActionMap().put("enter", new SimulateButtonPressAction(okButton));
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(this);
		
		JPanel inputPane = new JPanel();
		inputPane.setLayout(new BoxLayout(inputPane, BoxLayout.PAGE_AXIS));
		
		JLabel label = new JLabel(gSolveState.getInputMessage());
		inputPane.add(label);
		inputPane.add(Box.createRigidArea(new Dimension(0,10)));
		inputPane.add(textField);
		inputPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(okButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(cancelButton);
		
		contentPane.add(inputPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);
		
		SwingFunctions.evenButtonsWidth(okButton, cancelButton);
	}
	
	private void executeGSolve(double var) {
		switch(gSolveState) {
		case Y_VALUE: graphicsDrawer.gSolveYValue(var); break;
		case X_VALUE: graphicsDrawer.gSolveXValue(var); break;
		default: break;
		}
		SwingFunctions.updateFrameContents(parent);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// always closes window after a button is pressed unless user tried to add nothing or an invalid function
		// in which case exits this method and window remains open
		if(e.getSource() == okButton) {
			try {
				executeGSolve(Double.parseDouble(textField.getText().trim()));
			} catch(IllegalArgumentException e3) {
				SwingFunctions.showErrorMessageDialog(this, "Invalid input");
				return;
			}
		}
				
		parent.setEnabled(true);
		this.dispose();
	}

}
