package reinforcementLearning;

import java.io.IOException;
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
	private Car car;
	private int maxIter;
	private double alpha; //learning rate
	private double gamma; //discount factor

	HashMap<Pair, HashMap<Pair, HashMap<Pair, Double>>> rewards;//HashMap<Position, HashMap<Velocity, HashMap<Action, Reward>>>
	
	public QLearning(String[][] track, String algoName, String trackName, String crashName) {
		this.track = track;
		this.algoName = algoName;
		this.trackName = trackName;
		this.crashName = crashName;
		try {
			car = new Car(track, crashName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maxIter = 20;
		rewards = new HashMap<Pair, HashMap<Pair, HashMap<Pair, Double>>>();
		printTrackInfo(algoName, trackName, crashName);
		train();
	}
	
	void train() {
		initialize();
		//super.get_logger().log(Level.INFO, rewards.toString());
		for(int i = 0; i < maxIter; i++){
			//select position
			Pair position = new Pair(1,2);//TODO select a real position!
			//select Velocity
			int velocityx = (int)Math.floor(Math.random()*11)-5;
			int velocityy = (int)Math.floor(Math.random()*11)-5;
			Pair velocity = new Pair(velocityx, velocityy);
			for(int m = -1; m <= 1; m++){//for each action
				for(int n = -1; n <= 1; n++){
					Pair act = new Pair(m,n);
					//calculate next position and velocity //TODO next pos and velocity
					//find max reward of actions
					double maxreward = 0;//TODO actually find max reward
					double q = rewards.get(position).get(velocity).get(act);
					double reward = 0;//TODO figure out reward
					double newq = q + (alpha*(reward + (gamma*maxreward) - q));
					rewards.get(position).get(velocity).put(act, newq);
				}
			}
			//drive car through track recording crashes and moves
		}
		
	}

	void test() {
		// TODO Auto-generated method stub
	}
	
	void initialize(){
		//get open positions
		int[][] locs = car.openLocs;
		//for each open position:
		for(int i = 0; i < locs.length; i++){
			for(int j = 0; j < locs[i].length; j++){
				//create a HashMap
				Pair pos = new Pair(i,j);
				HashMap<Pair, HashMap<Pair, Double>> intermediate = new HashMap<Pair, HashMap<Pair, Double>>();
				//for each possible velocity
				for(int k = -5; k <= 5; k++){
					for(int l = -5; l <= 5; l++){
						//create a HashMap
						Pair vel = new Pair(k, l);
						HashMap<Pair, Double> tertiary = new HashMap<Pair, Double>();
						//for each possible action
						for(int m = -1; m <= 1; m++){
							for(int n = -1; n <= 1; n++){
								Pair act = new Pair(m,n);
								tertiary.put(act, -1.0);
								//create a HashMap with each value equal to -1
							}
						}
						intermediate.put(vel, tertiary);
					}
				}
				rewards.put(pos, intermediate);
			}
		}
		
	}
	
	
	// info for printing out for samples run
	public void printTrackInfo(String algoName, String trackName,
			String crashName) {
		super.get_logger().log(Level.INFO, "Running " + algoName + " on " + trackName + " with " + crashName);
		super.get_logger().log(Level.INFO, "");		
	}	
}