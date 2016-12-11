package reinforcementLearning;

import java.util.HashMap;
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

	HashMap<Pair, HashMap<Pair, HashMap<Pair, Double>>> rewards;//HashMap<Position, HashMap<Velocity, HashMap<Action, Reward>>>
	
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
	
	void initialize(){
		//get open positions
		//for each open position:
			//create a HashMap
			//for each possible velocity
				//create a HashMap
				//for each possible action
					//create a HashMap with each value equal to -1
		
	}
	
	
	// info for printing out for samples run
	public void printTrackInfo(String algoName, String trackName,
			String crashName) {
		super.get_logger().log(Level.INFO, "Running " + algoName + " on " + trackName + " with " + crashName);
		super.get_logger().log(Level.INFO, "");		
	}	
}