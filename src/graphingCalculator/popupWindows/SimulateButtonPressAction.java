package graphingCalculator.popupWindows;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class SimulateButtonPressAction extends AbstractAction {

	JButton button;
	
	public SimulateButtonPressAction(JButton button) {
		this.button = button;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		button.doClick();
	}

}