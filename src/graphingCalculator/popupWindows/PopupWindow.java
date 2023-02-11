package graphingCalculator.popupWindows;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import graphingCalculator.graphics.GraphicsDrawer;

@SuppressWarnings("serial")
public abstract class PopupWindow extends JFrame implements ActionListener, WindowListener {

	protected JFrame parent;
	protected GraphicsDrawer graphicsDrawer;
	private final int MIN_SECONDARY_WINDOW_WIDTH = 300;
	
	public PopupWindow(JFrame parent, String title, GraphicsDrawer graphicsDrawer) {
		super(title);
		this.parent = parent;
		this.graphicsDrawer = graphicsDrawer;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.addWindowListener(this);
	}
	
	public void showWindow() {
		resetContainer();
		parent.setEnabled(false);
		this.pack();
		this.setMinWidth(MIN_SECONDARY_WINDOW_WIDTH);
		this.setLocationRelativeTo(parent);
		this.setVisible(true);
	}
	
	protected void resetContainer() {
		Container contentPane = getContentPane();
		contentPane.removeAll();
		addComponents(contentPane);
	}
	
	protected abstract void addComponents(Container contentPane);

	private void setMinWidth(int width) {
		Dimension size = this.getSize();
		size.width = Math.max(size.width, width);
		this.setSize(size);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		parent.setEnabled(true);
	}

	@Override
	public void windowOpened(WindowEvent e) {}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}
	
}
