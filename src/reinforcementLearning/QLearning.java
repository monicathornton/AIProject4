package reinforcementLearning;

import java.util.logging.Level;

public class QLearning extends Driver {
	private String[][] track;
	private String algoName;
	private String trackName;
	private String crashName;
	
	public QLearning(String[][] track, String algoName, String trackName, String crashName) {
		this.track = track;
		this.algoName = algoName;
		this.trackName = trackName;
		this.crashName = crashName;
		
		printTrackInfo(algoName, trackName, crashName);
	}
	
	void train() {
		// TODO Auto-generated method stub
		
	}

	void test() {
		// TODO Auto-generated method stub
	}
	
	
	
	
	public void printTrackInfo(String algoName, String trackName,
			String crashName) {
		super.get_logger().log(Level.INFO, "Running " + algoName + " on " + trackName + " with " + crashName);
		super.get_logger().log(Level.INFO, "");		
	}	
}