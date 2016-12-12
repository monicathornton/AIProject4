package reinforcementLearning;

import java.io.IOException;
import java.util.ArrayList;
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
	private double alpha = 0.1; // learning rate
	private double gamma = 0.9; // discount factor
	private int finishingCars;

	HashMap<Pair, HashMap<Pair, HashMap<Pair, Double>>> rewards;// HashMap<Position,
																// HashMap<Velocity,
																// HashMap<Action,
																// Reward>>>

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
		maxIter = 2000;
		rewards = new HashMap<Pair, HashMap<Pair, HashMap<Pair, Double>>>();
		printTrackInfo(algoName, trackName, crashName);
		train();
	}

	void train() {
		// car.setTraining();
		initialize();
		//printRewards();
		System.out.println();
		printTrackConsole(track, 0, car, 0,0);
		finishingCars = 0;
		// super.get_logger().log(Level.INFO, rewards.toString());
		for (int i = 0; i < maxIter; i++) {
			
			car.setTraining();
			// select position
			ArrayList<Pair> positions = new ArrayList<Pair>(rewards.keySet());
			
			Pair position = positions.get((int)Math.floor(Math.random()*positions.size()));// TODO select a real position!
			// select Velocity
			int velocityx = (int) Math.floor(Math.random() * 11) - 5;
			int velocityy = (int) Math.floor(Math.random() * 11) - 5;
			Pair velocity = new Pair(velocityx, velocityy);
			for (int m = -1; m <= 1; m++) {// for each action
				for (int n = -1; n <= 1; n++) {
					Pair act = new Pair(m, n);
					// calculate next position and velocity

					car.positionX = position.x;
					car.positionY = position.y;
					car.velocityX = velocity.x;
					car.velocityY = velocity.y;
					car.startLocX = position.x;
					car.startLocY = position.y;
					car.newPosition(act.x, act.y);

					Pair position2 = new Pair(car.positionX, car.positionY);
					Pair velocity2 = new Pair(car.velocityX, car.velocityY);
					System.out.println("Training Info:");
					printTrackConsole(track, i, car, act.x, act.y);
					if (rewards.get(position2) != null) {

						// find max reward of actions
						double maxreward = getMaxValue(rewards.get(position2).get(velocity2));
						// get previously calculated q
						double q = rewards.get(position).get(velocity).get(act);
						// get reward for the action
						double reward = getReward(position2.x, position2.y);
						// calculate q
						double newq = q + (alpha * (reward + (gamma * maxreward) - q));
						System.out.println(position.x + " " + position.y + " " + velocity.x + " " + velocity.y + " "
								+ act.x + " " + act.y + ": " + q + " ");
						// add q to rewards hashmap
						rewards.get(position).get(velocity).put(act, newq);
					} else {
						//HashMap<Pair, Double> bad = new HashMap<Pair, Double>();
						//bad.put(act, -10000.0);
						//HashMap<Pair, HashMap<Pair, Double>> bad2 = new HashMap<Pair, HashMap<Pair, Double>>();
						//bad2.put(velocity2, bad);
						//rewards.put(position2, bad2);
						//System.out.println("BAD MOVES:" + position2.x +" " + position2.y + " , " + velocity2.x + " " + velocity2.y);
					}
				}
			}
			// car.training = false;

			runthrough();
		}
		System.out.println(finishingCars);

	}

	void test() {
		// TODO Auto-generated method stub
	}
	void runthrough(){
		System.out.println("Iteration Info");
		car.putCarAtStart();
		car.carCrashes = 0;
		car.velocityX = 0;
		car.velocityY = 0;
		car.startLocX = car.positionX;
		car.startLocY = car.positionY;
		Pair pos = new Pair(car.positionX, car.positionY);
		Pair vel = new Pair(0, 0);
		boolean raceover = false;
		int t = 0;
		int badMoves = 0;
		int badMovesLimit = track.length * track[0].length * 2;
		//System.out.println(raceover + " " + t + " " + badMoves + " " + badMovesLimit + " car crashes: " + car.carCrashes);
		while (!raceover && car.carCrashes <= 100 && badMoves < badMovesLimit) {
			// System.out.println(t);
			System.out.println(raceover + " " + t + " " + badMoves + " " + badMovesLimit + " car crashes: " + car.carCrashes);
			
			if (rewards.get(pos) != null) {
				Pair maxact = getMaxAction(rewards.get(pos).get(vel));
				raceover = drive(car, maxact.x, maxact.y);
				// if(car.positionX == pos.x && car.positionY == pos.y){
				// System.out.println(car.positionX + " "+ pos.x + " " +
				// car.positionY + " " + pos.y);
				// }
				pos.x = car.positionX;
				pos.y = car.positionY;
				vel.x = car.velocityX;
				vel.y = car.velocityY;
				
				printTrackConsole(track, t, car, maxact.x, maxact.y);

			} else {
				System.out.println("BAD MOVES:" + pos.x +" " + pos.y + " , " + vel.x + " " + vel.y);
				
				//printRewards();
				badMoves++;
			}
			t++;

		}
		if (raceover) {
			finishingCars++;
		}
	}
	void initialize() {
		// get open positions
		int[][] locs = car.openLocs;
				
		//for each open position:
		for(int i = 0; i < locs.length; i++){
				//create a HashMap
				Pair pos = new Pair(locs[i][0],locs[i][1]);
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
		//System.out.println(rewards.keySet().size());
		int[][] flocs = car.finishLocs;
		//for each open position:
		for(int i = 0; i < flocs.length; i++){
				//create a HashMap
				Pair pos = new Pair(flocs[i][0],flocs[i][1]);
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
								tertiary.put(act, 0.0);
								//create a HashMap with each value equal to -1
							}

					}
					intermediate.put(vel, tertiary);
				}

			}
			rewards.put(pos, intermediate);


		}
		//System.out.println(rewards.keySet().size());
		int[][] slocs = car.startLocs;

		//for each open position:
		for(int i = 0; i < slocs.length; i++){
				//create a HashMap
				Pair pos = new Pair(slocs[i][0],slocs[i][1]);
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
		//System.out.println(rewards.keySet().size());

	}

	private double getReward(int x, int y) {
		if (track[x][y].equals("F")) {
			return 0.0;
		} else {
			return -1.0;
		}
	}

	// info for printing out for samples run
	public void printTrackInfo(String algoName, String trackName, String crashName) {
		super.get_logger().log(Level.INFO, "Running " + algoName + " on " + trackName + " with " + crashName);
		super.get_logger().log(Level.INFO, "");
	}

	private Double getMaxValue(HashMap<Pair, Double> actionMap) {
		HashMap.Entry<Pair, Double> maxEntry = null;

		for (HashMap.Entry<Pair, Double> entry : actionMap.entrySet()) {
			if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
				maxEntry = entry;
			}
		}
		return maxEntry.getValue();
	}

	private Pair getMaxAction(HashMap<Pair, Double> actionMap) {
		HashMap.Entry<Pair, Double> maxEntry = null;

		for (HashMap.Entry<Pair, Double> entry : actionMap.entrySet()) {
			if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
				maxEntry = entry;
			}
		}
		return maxEntry.getKey();
	}
	public void printRewards(){
		ArrayList<Pair> positions = new ArrayList<Pair>(rewards.keySet());
		for(Pair p : positions){
			System.out.print("Position: (" + p.x + " , " + p.y +"), ");
//			ArrayList<Pair> velocities = new ArrayList<Pair>(rewards.get(p).keySet());
//			for(Pair v : velocities){
//				System.out.print("Velocity: (" + v.x + " , " + v.y +"), ");
//				ArrayList<Pair> actions = new ArrayList<Pair>(rewards.get(p).get(v).keySet());
//				for(Pair a : actions){
//					
//					double r = rewards.get(p).get(v).get(a);
//					
//					
//					System.out.print("Action: (" + a.x + " , " + a.y +"), ");
//					System.out.print("Reward: " + r + "   ");
//					
//					
//				}
//			}
			System.out.println();
		}
	}
}