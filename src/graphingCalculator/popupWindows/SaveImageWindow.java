package graphingCalculator.popupWindows;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import graphingCalculator.graphics.GraphicsDrawer;
import graphingCalculator.saver.ImageFileFilter;
import graphingCalculator.utils.SwingUtils;

@SuppressWarnings("serial")
public class SaveImageWindow extends PopupWindow {
	
	private ButtonGroup buttonGroup;
	private JCheckBox transparentCheckbox;
	private JButton saveButton;
	

	public SaveImageWindow(JFrame parent, String title, GraphicsDrawer graphicsDrawer) {
		super(parent, title, graphicsDrawer);
	}

	@Override
	protected void addComponents(Container contentPane) {
		JRadioButton jpgButton = new JRadioButton("jpg");
		jpgButton.setFocusable(false);
		jpgButton.setSelected(true);
		JRadioButton pngButton = new JRadioButton("png");	// keep this order when adding new formats (jpg -> png)
		pngButton.setFocusable(false);
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(jpgButton);
		buttonGroup.add(pngButton);
		
		transparentCheckbox = new JCheckBox("transparent background");
		transparentCheckbox.setFocusable(false);
		transparentCheckbox.setEnabled(false);
		transparentCheckbox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(transparentCheckbox.isSelected()) {
					if(jpgButton.isSelected())
						buttonGroup.getElements().nextElement().setSelected(true);
					jpgButton.setEnabled(false);
				}
				else
					jpgButton.setEnabled(true);
			}
		});
		
		jpgButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(jpgButton.isSelected())
					transparentCheckbox.setEnabled(false);
				else
					transparentCheckbox.setEnabled(true);
			}
		});
		
		saveButton = new JButton("Save");
		saveButton.setFocusable(false);
		saveButton.addActionListener(this);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(this);
		
		JPanel fileTypePane = new JPanel();
		fileTypePane.setLayout(new BoxLayout(fileTypePane, BoxLayout.PAGE_AXIS));
		fileTypePane.add(jpgButton);
		fileTypePane.add(pngButton);
		fileTypePane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		JPanel optionsPane = new JPanel();
		optionsPane.add(transparentCheckbox);
		optionsPane.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(saveButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(cancelButton);
		
		contentPane.add(fileTypePane, BorderLayout.WEST);
		contentPane.add(optionsPane, BorderLayout.EAST);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);
		
		SwingUtils.evenButtonsWidth(saveButton, cancelButton);
	}
	
	private boolean saveImage(boolean transparent, String format) throws IOException {
		String filePath = SwingUtils.showSaveFileDialog(this, format, new ImageFileFilter("."+format));
		if(filePath == null) return false;
		
		//if(format.equals("jpg")) format = "jpeg";		// to test if this line matters
		BufferedImage saveImg = graphicsDrawer.getBufferedImage(transparent);
		ImageIO.write(saveImg, format, new File(filePath));
		return true;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == saveButton) {
			try {
				if(!saveImage(transparentCheckbox.isSelected(), getSelectedButtonText(buttonGroup)))
					return;
			} catch (IOException e1) {
				SwingUtils.showErrorMessageDialog(this, "There was a problem saving the image.");
			}
		}
		
		parent.setEnabled(true);
		this.dispose();
	}
	private String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected())
                return button.getText();
        }

        return null;
    }
}
