import javax.swing.JFrame;

import graphplotter.FunctionGraphic;
import graphplotter.ReferencialGraphic;

public class Main {
	
	public static JFrame frame;

	public static void main(String[] args) {
		frame = new JFrame();
	    frame.setSize(1000, 1000);
	    frame.setTitle("Graphs");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.setResizable(false);
		
		ReferencialGraphic rg = new ReferencialGraphic();
		FunctionGraphic fg = new FunctionGraphic();
		
		frame.add(rg);
		frame.setVisible(true);
	    frame.add(fg);
	    frame.setVisible(true);
	}
}

