package graphplotter.popupWindows;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ListFunctionsFrame extends JFrame {

	private JFrame parent;
	
	public ListFunctionsFrame(JFrame parent) {
		super("Functions List");
		this.parent = parent;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
	}
	
	public void showWindow(String[] expressions, int[] colorIds) {
		Container contentPane = getContentPane();
		contentPane.removeAll();
		addComponents(contentPane, expressions, colorIds);
		
		parent.setEnabled(false);
		this.pack();
		this.setLocationRelativeTo(parent);
		this.setVisible(true);
	}
	
	private void addComponents(Container contentPane, String[] expressions, int[] colorIds) {
		
		JPanel basePane = new JPanel();
		basePane.setLayout(new BoxLayout(basePane, BoxLayout.PAGE_AXIS));
		
		JLabel label = new JLabel("Functions list:                                                   ");
		basePane.add(label);
		basePane.add(Box.createRigidArea(new Dimension(0,5)));
		basePane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		JPanel listPane = new JPanel();
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
		
		for(int i = 0; i < expressions.length; i++) {
			String filename = "src\\assets\\color"+colorIds[i]+".jpg";
			ImageIcon icon = new ImageIcon(filename);
			JLabel imageLabel = new JLabel(icon);
			JLabel expressionLabel = new JLabel(expressions[i]);
			JPanel itemPane = new JPanel();
			
			itemPane.add(imageLabel);
			itemPane.add(expressionLabel);
			listPane.add(itemPane);
		}
		
		basePane.add(listPane);
		contentPane.add(basePane);
	}
	
}
