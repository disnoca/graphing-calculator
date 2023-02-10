package graphingCalculator.popupWindows;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import graphingCalculator.graphics.GraphicsDrawer;
import graphingCalculator.utils.SwingUtils;

@SuppressWarnings("serial")
public class SetReferentialLimitsWindow extends PopupWindow {
	
	private JTextField[] referentialLimitsTF;	// xMin, xMax, yMin, yMax textfields respectively
	private JButton changeButton;
	

	public SetReferentialLimitsWindow(JFrame parent, String title, GraphicsDrawer graphicsDrawer) {
		super(parent, title, graphicsDrawer);
		referentialLimitsTF = new JTextField[4];
	}

	@Override
	protected void addComponents(Container contentPane) {
		changeButton = new JButton("Change");
		changeButton.setFocusable(false);
		changeButton.addActionListener(this);
		
		String[] limits = graphicsDrawer.getReferentialLimits().getFormattedLimits();
		
		for(int i = 0; i < referentialLimitsTF.length; i++) {
			referentialLimitsTF[i] = new JTextField(5);
			referentialLimitsTF[i].setText(limits[i]);
			referentialLimitsTF[i].getInputMap().put(KeyStroke.getKeyStroke("pressed ENTER"), "enter");
			referentialLimitsTF[i].getActionMap().put("enter", new SimulateButtonPressAction(changeButton));
		}
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(this);
		
		JPanel xTextFieldsPane = new JPanel();
		xTextFieldsPane.add(new JLabel("min X:"));
		xTextFieldsPane.add(referentialLimitsTF[0]);
		xTextFieldsPane.add(Box.createRigidArea(new Dimension(10, 0)));
		xTextFieldsPane.add(new JLabel("max X:"));
		xTextFieldsPane.add(referentialLimitsTF[1]);
		xTextFieldsPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		JPanel yTextFieldsPane = new JPanel();
		yTextFieldsPane.add(new JLabel("min Y:"));
		yTextFieldsPane.add(referentialLimitsTF[2]);
		yTextFieldsPane.add(Box.createRigidArea(new Dimension(10, 0)));
		yTextFieldsPane.add(new JLabel("max Y:"));
		yTextFieldsPane.add(referentialLimitsTF[3]);
		yTextFieldsPane.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(changeButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(cancelButton);
		
		contentPane.add(xTextFieldsPane, BorderLayout.PAGE_START);
		contentPane.add(yTextFieldsPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);
		
		SwingUtils.evenButtonsWidth(changeButton, cancelButton);
		
	}
	
	private void setReferentialLimits(double xMin, double xMax, double yMin, double yMax) {
		graphicsDrawer.setReferentialLimits(xMin, xMax, yMin, yMax);
		SwingUtils.updateFrameContents(parent);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == changeButton) {
			double[] limits = new double[referentialLimitsTF.length];
			
			try {
				for(int i = 0; i < referentialLimitsTF.length; i++)
					limits[i] = Double.parseDouble(referentialLimitsTF[i].getText());
		    } catch (NumberFormatException ignore) {
		        SwingUtils.showErrorMessageDialog(this, "Invalid Values.");
		        return;
		    }
			
			if(limits[0] >= limits[1]) {
				SwingUtils.showErrorMessageDialog(this, "max X must be greater than min X.");
				return;
			}
			else if(limits[2] >= limits[3]) {
				SwingUtils.showErrorMessageDialog(this, "max Y must be greater than min Y.");
				return;
			}
				
			setReferentialLimits(limits[0], limits[1], limits[2], limits[3]);
		}
		
		parent.setEnabled(true);
		this.dispose();
	}

}
