package graphplotter;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import graphplotter.saver.GraphPlotterProjectFileFilter;

public class SwingFunctions {

	public static void showErrorMessageDialog(JFrame parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void evenButtonsWidth(JButton b1, JButton b2) {
		int width = Math.max(b1.getPreferredSize().width, b2.getPreferredSize().width);
		int height = b1.getPreferredSize().height;
		Dimension buttonsSize = new Dimension(width, height);
		
		b1.setPreferredSize(buttonsSize);
		b2.setPreferredSize(buttonsSize);
	}
	
	// for some reason the jframe must be set as non visible and then visible or be resized for its contents to be updated
	// since changing visible values creates visual problems, this method resizes the frame
	public static void updateFrameContents(JFrame frame) {
		Dimension size = frame.getSize();
		int width = size.width;
		int height = size.height;
		Dimension tempSize = new Dimension(width+1, height+1);
		
		frame.setSize(tempSize);
		frame.setSize(size);
	}
	
	public static String showSaveFileDialog(JFrame parent, String fileExtension, FileFilter fileFilter) {
		JFileChooser fileSaver = new JFileChooser();
		fileSaver.addChoosableFileFilter(fileFilter);
		fileSaver.setAcceptAllFileFilterUsed(false);
		int choice = fileSaver.showSaveDialog(parent);
		if (choice != JFileChooser.APPROVE_OPTION) return null;
		File chosenFile = fileSaver.getSelectedFile();
		String filePath = chosenFile.getAbsolutePath();
		if(GraphPlotterProjectFileFilter.getExtension(chosenFile) == null)
			filePath += "."+fileExtension;
		return filePath;
	}
	
	public static File showLoadFileDialog(JFrame parent) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new GraphPlotterProjectFileFilter());
		fileChooser.setAcceptAllFileFilterUsed(false);
		int choice = fileChooser.showOpenDialog(parent);
		if (choice != JFileChooser.APPROVE_OPTION) return null;
		return fileChooser.getSelectedFile();
	}
	
}
