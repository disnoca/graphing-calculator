package graphplotter;

public class ThreadTest extends Thread {

	private double timer;
	
	public ThreadTest(double timer) {
		this.timer = timer;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			System.out.println(timer);
		} catch (InterruptedException e) {}
	}
	
}
