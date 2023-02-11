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

import graphingCalculator.gSolveState.GSolveState;
import graphingCalculator.gSolveState.GSolveStateWrapper;
import graphingCalculator.graphics.GraphicsDrawer;
import graphingCalculator.utils.SwingUtils;

@SuppressWarnings("serial")
public class GSolveXYValueWindow extends PopupWindow {
	
	private JButton okButton;
	private JTextField textField;

	private GSolveStateWrapper gSolveState;

	public GSolveXYValueWindow(JFrame parent, GraphicsDrawer graphicsDrawer, GSolveStateWrapper gSolveState) {
		super(parent, "", graphicsDrawer);
		this.gSolveState = gSolveState;
	}
	
	public void setGSolveState(GSolveStateWrapper gSolveState) {
		this.gSolveState = gSolveState;
	}
	
	@Override
	protected void addComponents(Container contentPane) {
		this.setTitle(gSolveState.state.getTitle());
		
		okButton = new JButton("Ok");
		okButton.setFocusable(false);
		okButton.addActionListener(this);
		
		textField = new JTextField();
		textField.getInputMap().put(KeyStroke.getKeyStroke("pressed ENTER"), "enter");
		textField.getActionMap().put("enter", new SimulateButtonPressAction(okButton));
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(this);
		
		JPanel inputPane = new JPanel();
		inputPane.setLayout(new BoxLayout(inputPane, BoxLayout.PAGE_AXIS));
		
		JLabel label = new JLabel(gSolveState.state.getInputMessage());
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
		
		SwingUtils.evenButtonsWidth(okButton, cancelButton);
	}
	
	private void executeGSolve(double var) {
		switch(gSolveState.state) {
		case Y_VALUE: 
			if(!graphicsDrawer.gSolveYValue(var)) {
				gSolveState.state = GSolveState.NONE;
				SwingUtils.showErrorMessageDialog(this, "No solution found");
			}
			break;
		case X_VALUE: 
			if(!graphicsDrawer.gSolveXValue(var)) {
				gSolveState.state = GSolveState.NONE;
				SwingUtils.showErrorMessageDialog(this, "No solutions found");
			}
			break;
		default: break;
		}
		
		SwingUtils.updateFrameContents(parent);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == okButton) {
			try {
				executeGSolve(Double.parseDouble(textField.getText().trim()));
			} catch(IllegalArgumentException e3) {
				SwingUtils.showErrorMessageDialog(this, "Invalid input");
				return;
			}
		}
				
		parent.setEnabled(true);
		this.dispose();
	}

}
