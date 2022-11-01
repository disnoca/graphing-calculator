import graphplotter.GraphPlotterFrame;

public class Main {
	
	public static GraphPlotterFrame frame;

	public static void main(String[] args) {
		frame = new GraphPlotterFrame();
		frame.drawReferential();
		frame.drawFunction();
	}
}