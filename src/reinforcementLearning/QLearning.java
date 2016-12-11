package reinforcementLearning;

import java.util.logging.Level;
/*
 * Q learning algorithm:
 * 		Initialize all rewards to 1
 * 		While number of iterations < max iterations
 * 			Pick a state (x, y, xv, yv)
 * 			for each acceleration (xa, ya)
 * 				calculate maximum expected reward
 * 				store reward for each action
 * 			run car through and record moves and crashes
 */
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
	
	
	
	// info for printing out for samples run
	public void printTrackInfo(String algoName, String trackName,
			String crashName) {
		super.get_logger().log(Level.INFO, "Running " + algoName + " on " + trackName + " with " + crashName);
		super.get_logger().log(Level.INFO, "");		
	}	
}