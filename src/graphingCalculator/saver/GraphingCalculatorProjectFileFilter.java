package graphingCalculator.saver;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class GraphingCalculatorProjectFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
	        return true;

	    String extension = getExtension(f);
	    if (extension == null) return false;
	    return extension.equals("gpp");
	}
	
	public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

	@Override
	public String getDescription() {
		return "Graph Plotter Project Files (.gpp)";
	}

}
