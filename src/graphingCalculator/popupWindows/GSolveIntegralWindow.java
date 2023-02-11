package graphingCalculator.popupWindows;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
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
public class GSolveIntegralWindow extends PopupWindow {
	
	public static final int INTEGRAL_SUCCESS = 0;
	public static final int INTEGRAL_LOWER_BOUND_ERROR = 1;
	public static final int INTEGRAL_UPPER_BOUND_ERROR = 2;
	public static final int INTEGRAL_CALCULATION_ERROR = 3;

	private JTextField lowerBoundTF, upperBoundTF;
	private JButton calculateButton;
	
	private GSolveStateWrapper gSolveState;
	
	public GSolveIntegralWindow(JFrame parent, GraphicsDrawer graphicsDrawer, GSolveStateWrapper gSolveState) {
		super(parent, GSolveState.INTEGRAL.getTitle(), graphicsDrawer);
		this.gSolveState = gSolveState;
	}

	@Override
	protected void addComponents(Container contentPane) {
		calculateButton = new JButton("Calculate");
		calculateButton.setFocusable(false);
		calculateButton.addActionListener(this);
		
		lowerBoundTF = new JTextField(5);
		lowerBoundTF.getInputMap().put(KeyStroke.getKeyStroke("pressed ENTER"), "enter");
		lowerBoundTF.getActionMap().put("enter", new SimulateButtonPressAction(calculateButton));
		upperBoundTF = new JTextField(5);
		upperBoundTF.getInputMap().put(KeyStroke.getKeyStroke("pressed ENTER"), "enter");
		upperBoundTF.getActionMap().put("enter", new SimulateButtonPressAction(calculateButton));
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(this);
		
		JPanel boundsInputTFPane = new JPanel();
		boundsInputTFPane.add(new JLabel("Lower bound:"));
		boundsInputTFPane.add(lowerBoundTF);
		boundsInputTFPane.add(Box.createRigidArea(new Dimension(10, 0)));
		boundsInputTFPane.add(new JLabel("Upper bound:"));
		boundsInputTFPane.add(upperBoundTF);
		boundsInputTFPane.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
		
		JPanel buttonPane = new JPanel();
		buttonPane.add(calculateButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(cancelButton);
		
		contentPane.add(boundsInputTFPane, BorderLayout.PAGE_START);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);
		((JComponent) contentPane).setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		SwingUtils.evenButtonsWidth(calculateButton, cancelButton);
	}
	
	private void executeGSolve(double lowerBound, double upperBound) {
		switch(graphicsDrawer.gSolveIntegral(lowerBound, upperBound)) {
		case(INTEGRAL_LOWER_BOUND_ERROR): 
			SwingUtils.showErrorMessageDialog(this, "Invalid lower bound");
			gSolveState.state = GSolveState.NONE;
			break;
		case(INTEGRAL_UPPER_BOUND_ERROR): 
			SwingUtils.showErrorMessageDialog(this, "Invalid upper bound");
			gSolveState.state = GSolveState.NONE;
			break;
		case(INTEGRAL_CALCULATION_ERROR): 
			SwingUtils.showErrorMessageDialog(this, "Invalid integration area");
			gSolveState.state = GSolveState.NONE;
			break;
		}
		
		SwingUtils.updateFrameContents(parent);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == calculateButton) {
			try {
				executeGSolve(Double.parseDouble(lowerBoundTF.getText().trim()), Double.parseDouble(upperBoundTF.getText().trim()));
			} catch(IllegalArgumentException e3) {
				SwingUtils.showErrorMessageDialog(this, "Invalid input");
				return;
			}
		}
				
		parent.setEnabled(true);
		this.dispose();
	}

}
