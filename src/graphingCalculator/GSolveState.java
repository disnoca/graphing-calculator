package graphingCalculator;

public enum GSolveState {
	NONE (false),
	ROOT (true),
	MAXIMUM (true),
	MINIMUM (true),
	Y_AXIS_INTERSECTION (false),
	FUNCTION_INTERSECTION (true),
	Y_VALUE ("Y-Value", "Enter the x coordinate:", false),
	X_VALUE ("X-Value", "Enter the y coordinate:", true),
	INTEGRAL (false);
	
	private final String title, inputMessage;
	private final boolean hasMultipleAnswers;
	
	GSolveState(String title, String inputMessage, boolean hasMultipleAnswers) {
		this.title = title;
		this.inputMessage = inputMessage;
		this.hasMultipleAnswers = hasMultipleAnswers;
	}
	
	GSolveState(boolean hasMultipleAnswers) {
		this.title = null;
		this.inputMessage = null;
		this.hasMultipleAnswers = hasMultipleAnswers;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getInputMessage() {
		return inputMessage;
	}
	
	public boolean hasMultipleAnswers() {
		return hasMultipleAnswers;
	}
}
