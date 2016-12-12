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
	private String[][] testTrack;
	private String[][] cleanTrack;
	private String[][] trainTrack;
	private String algoName;
	private String trackName;
	private String crashName;
	private Car car;
	// private Car testCar;
	private int maxIter;
	private double alpha = 0.1; // learning rate
	private double gamma = 0.9; // discount factor
	private int finishingCars;

	HashMap<Pair, HashMap<Pair, HashMap<Pair, Double>>> rewards;// HashMap<Position,
																// HashMap<Velocity,
																// HashMap<Action,
																// Reward>>>

	public QLearning(String[][] trainTrack, String algoName, String trackName, String crashName) {
		this.trainTrack = trainTrack;
		testTrack = new String[trainTrack.length][];
		cleanTrack = new String[trainTrack.length][];

		this.testTrack = copyOf(trainTrack);
		this.cleanTrack = copyOf(trainTrack);
		this.algoName = algoName;
		this.trackName = trackName;
		this.crashName = crashName;
		try {
			car = new Car(trainTrack, crashName);
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
		// printRewards();
		System.out.println();
		printTrackConsole(trainTrack, 0, car, 0, 0);
		finishingCars = 0;
		// super.get_logger().log(Level.INFO, rewards.toString());
		for (int i = 0; i < maxIter; i++) {
			//alpha = alpha - 1/maxIter;
			car.setTraining();
			// select position
			ArrayList<Pair> positions = new ArrayList<Pair>(rewards.keySet());

			Pair position = positions.get((int) Math.floor(Math.random() * positions.size()));// TODO
																								// select
																								// a
																								// real
																								// position!
			// select Velocity
			int velocityx = (int) Math.floor(Math.random() * 11) - 5;
			int velocityy = (int) Math.floor(Math.random() * 11) - 5;
			Pair velocity = getVelocityPair(position, velocityx, velocityy);
			for (int m = -1; m <= 1; m++) {// for each action
				for (int n = -1; n <= 1; n++) {

					Pair act = getActionPair(position, velocity, m, n);
					// calculate next position and velocity

					car.positionX = position.x;
					car.positionY = position.y;
					car.velocityX = velocity.x;
					car.velocityY = velocity.y;
					car.startLocX = position.x;
					car.startLocY = position.y;
					drive(car, act.x, act.y);

					Pair position2 = getPositionPair(car.positionX, car.positionY);
					Pair velocity2 = getVelocityPair(position2, car.velocityX, car.velocityY);
					System.out.println("Training Info:");
					// printTrackConsole(trainTrack, i, car, act.x, act.y);
					if (rewards.containsKey(position2)) {

						// find max reward of actions
						double maxreward = getMaxValue(rewards.get(position2).get(velocity2));
						// get previously calculated q
						double q = rewards.get(position).get(velocity).get(act);
						// get reward for the action
						double reward = getReward(trainTrack, position2.x, position2.y);
						// calculate q
						double newq = q + (alpha * (reward + (gamma * maxreward) - q));
						System.out.println(position.x + " " + position.y + " " + velocity.x + " " + velocity.y + " "
								+ act.x + " " + act.y + ": " + q + " " + newq);
						// add q to rewards hashmap
						rewards.get(position).get(velocity).put(act, newq);
					} else {
						// HashMap<Pair, Double> bad = new HashMap<Pair,
						// Double>();
						// bad.put(act, -10000.0);
						// HashMap<Pair, HashMap<Pair, Double>> bad2 = new
						// HashMap<Pair, HashMap<Pair, Double>>();
						// bad2.put(velocity2, bad);
						// rewards.put(position2, bad2);
						System.out.println("BAD MOVES:" + position2.x + " " + position2.y + " , " + velocity2.x + " "
								+ velocity2.y);
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

	void runthrough() {
		System.out.println("Iteration Info");
		String[][] tracks = copyOf(cleanTrack);
		Car testCar;
		try {
			testCar = new Car(tracks, crashName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.out.println("Car failed to initialize");
			return;
		}
		testCar.putCarAtStart();
		// testCar.carCrashes = 0;

		Pair pos = getPositionPair(testCar.positionX, testCar.positionY);
		Pair vel = getVelocityPair(pos, testCar.velocityX, testCar.velocityY);
		boolean raceover = false;
		int t = 0;
		int max0 = 0;
		// System.out.println(raceover + " " + t + " " + badMoves + " " +
		// badMovesLimit + " car crashes: " + car.carCrashes);
		while (!raceover && testCar.carCrashes <= 100 && max0 < 100) {
			System.out.println(t);
			System.out.println(raceover + " " + t + " car crashes: " + testCar.carCrashes);
			System.out.println();
			if (rewards.get(pos) != null && rewards.get(pos).get(vel) != null) {
				Pair maxact = getMaxAction(rewards.get(pos).get(vel));
				if(maxact.x==0 && maxact.y ==0){
					max0++;
				}
				if(maxact.x != 0 || maxact.y != 0){
					max0 = 0;
				}
				raceover = drive(testCar, maxact.x, maxact.y);
				// if(car.positionX == pos.x && car.positionY == pos.y){
				// System.out.println(car.positionX + " "+ pos.x + " " +
				// car.positionY + " " + pos.y);
				// }
				pos = getPositionPair(testCar.positionX, testCar.positionY);
				vel = getVelocityPair(pos, testCar.velocityX, testCar.velocityY);
				// System.out.println(rewards.get(pos).get(vel).get(maxact));
				printTrackConsole(tracks, t, car, maxact.x, maxact.y);

			} else {
				System.out.println("BAD MOVES:" + pos.x + " " + pos.y + " , " + vel.x + " " + vel.y);

				// printRewards();

			}
			t++;

		}
		if (raceover) {
			finishingCars++;
			System.out.println("Car FINISHEDDDD!!!!");
		}
	}

	void initialize() {
		// get open positions
		int[][] locs = car.openLocs;

		// for each open position:
		for (int i = 0; i < locs.length; i++) {
			// create a HashMap
			Pair pos = new Pair(locs[i][0], locs[i][1]);
			HashMap<Pair, HashMap<Pair, Double>> intermediate = new HashMap<Pair, HashMap<Pair, Double>>();
			// for each possible velocity
			for (int k = -5; k <= 5; k++) {
				for (int l = -5; l <= 5; l++) {
					// create a HashMap
					Pair vel = new Pair(k, l);
					HashMap<Pair, Double> tertiary = new HashMap<Pair, Double>();
					// for each possible action
					for (int m = -1; m <= 1; m++) {
						for (int n = -1; n <= 1; n++) {
							Pair act = new Pair(m, n);
							tertiary.put(act, -1.0);
							// create a HashMap with each value equal to -1

						}
					}
					intermediate.put(vel, tertiary);
				}

			}
			rewards.put(pos, intermediate);

		}
		// System.out.println(rewards.keySet().size());
		int[][] flocs = car.finishLocs;
		// for each open position:
		for (int i = 0; i < flocs.length; i++) {
			// create a HashMap
			Pair pos = new Pair(flocs[i][0], flocs[i][1]);
			HashMap<Pair, HashMap<Pair, Double>> intermediate = new HashMap<Pair, HashMap<Pair, Double>>();
			// for each possible velocity
			for (int k = -5; k <= 5; k++) {
				for (int l = -5; l <= 5; l++) {
					// create a HashMap
					Pair vel = new Pair(k, l);
					HashMap<Pair, Double> tertiary = new HashMap<Pair, Double>();
					// for each possible action
					for (int m = -1; m <= 1; m++) {
						for (int n = -1; n <= 1; n++) {
							Pair act = new Pair(m, n);
							tertiary.put(act, 0.0);
							// create a HashMap with each value equal to -1
						}

					}
					intermediate.put(vel, tertiary);
				}

			}
			rewards.put(pos, intermediate);

		}
		// System.out.println(rewards.keySet().size());
		int[][] slocs = car.startLocs;

		// for each open position:
		for (int i = 0; i < slocs.length; i++) {
			// create a HashMap
			Pair pos = new Pair(slocs[i][0], slocs[i][1]);
			HashMap<Pair, HashMap<Pair, Double>> intermediate = new HashMap<Pair, HashMap<Pair, Double>>();
			// for each possible velocity
			for (int k = -5; k <= 5; k++) {
				for (int l = -5; l <= 5; l++) {
					// create a HashMap
					Pair vel = new Pair(k, l);
					HashMap<Pair, Double> tertiary = new HashMap<Pair, Double>();
					// for each possible action
					for (int m = -1; m <= 1; m++) {
						for (int n = -1; n <= 1; n++) {
							Pair act = new Pair(m, n);
							tertiary.put(act, -1.0);
							// create a HashMap with each value equal to -1
						}

					}
					intermediate.put(vel, tertiary);
				}

			}
			rewards.put(pos, intermediate);

		}
		// System.out.println(rewards.keySet().size());

	}

	private double getReward(String[][] track, int x, int y) {
		// if(x >= track.length || y >= track[x].length){
		// return -1.0;
		// }else
		if (track[y][x].equals("F")) {
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

	private Pair getPositionPair(int x, int y) {
		ArrayList<Pair> positions = new ArrayList<Pair>(rewards.keySet());
		Pair testPair = new Pair(x, y);
		for (Pair p : positions) {
			if (testPair.equals(p)) {
				return p;
			}
		}
		return null;
	}

	private Pair getVelocityPair(Pair p, int x, int y) {
		ArrayList<Pair> velocities = new ArrayList<Pair>(rewards.get(p).keySet());
		Pair testPair = new Pair(x, y);
		for (Pair v : velocities) {
			if (testPair.equals(v)) {
				return v;
			}
		}
		return null;
	}

	private Pair getActionPair(Pair p, Pair v, int x, int y) {
		ArrayList<Pair> actions = new ArrayList<Pair>(rewards.get(p).get(v).keySet());
		Pair testPair = new Pair(x, y);
		for (Pair a : actions) {
			if (testPair.equals(a)) {
				return a;
			}
		}
		return null;
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
				// if(maxEntry == null || entry.getValue() != -1){
				maxEntry = entry;
				// }
			}
		}
		return maxEntry.getKey();
	}

	public void printRewards() {
		ArrayList<Pair> positions = new ArrayList<Pair>(rewards.keySet());
		for (Pair p : positions) {
			System.out.print("Position: (" + p.x + " , " + p.y + "), ");
			ArrayList<Pair> velocities = new ArrayList<Pair>(rewards.get(p).keySet());
			for (Pair v : velocities) {
				System.out.print("Velocity: (" + v.x + " , " + v.y + "), ");
				// ArrayList<Pair> actions = new
				// ArrayList<Pair>(rewards.get(p).get(v).keySet());
				// for(Pair a : actions){
				//
				// double r = rewards.get(p).get(v).get(a);
				//
				//
				// System.out.print("Action: (" + a.x + " , " + a.y +"), ");
				// System.out.print("Reward: " + r + " ");
				//
				//
				// }
			}
			System.out.println();
		}
	}

	public String[][] copyOf(String[][] input) {
		if (input == null)
			return null;
		String[][] result = new String[input.length][];
		for (int r = 0; r < input.length; r++) {
			result[r] = input[r].clone();
		}
		return result;
	}
}