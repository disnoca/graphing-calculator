package graphplotter.popupWindows;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public abstract class PopupWindow extends JFrame implements ActionListener {

	protected JFrame parent;
	
	public PopupWindow(JFrame parent, String title) {
		super(title);
		this.parent = parent;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
	}
	
	protected void showWindow(int width) {
		parent.setEnabled(false);
		this.pack();
		this.setWidth(width);
		this.setLocationRelativeTo(parent);
		this.setVisible(true);
	}
	
	private void setWidth(int width) {
		Dimension size = this.getSize();
		size.width = width;
		this.setSize(size);
	}
	
	protected void evenButtonsWidth(JButton b1, JButton b2) {
		int width = Math.max(b1.getPreferredSize().width, b2.getPreferredSize().width);
		int height = b1.getPreferredSize().height;
		Dimension buttonsSize = new Dimension(width, height);
		
		b1.setPreferredSize(buttonsSize);
		b2.setPreferredSize(buttonsSize);
	}
	
}
