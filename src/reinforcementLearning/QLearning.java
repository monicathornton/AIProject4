package reinforcementLearning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
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
	private String[][] testTrack; //track for testing
	private String[][] cleanTrack; //clean track that is copied for each training iteration
	private String[][] trainTrack; //track to fiddle on for training
	private String algoName;
	private String trackName;
	private String crashName; //crashing type
	private Car car; //training car
	private Car testCars; // test and evaluation car
	private int maxIter; //maximum iterations to run training
	private int ti; //number of moves made by the testCar in the testing phase
	private double alpha = 1; // learning rate
	private double gamma = 1; // discount factor
	private int finishingCars; // how many cars in training reached the finish line
	private String crashChoice;

	HashMap<Pair, HashMap<Pair, HashMap<Pair, Double>>> rewards;
	// HashMap<Position, HashMap<Velocity, HashMap<Action, Reward>>>

	public QLearning(String[][] trainTrack, String algoName, String trackName, String crashName, String crashChoice) {
		this.trainTrack = trainTrack;
		testTrack = new String[trainTrack.length][];
		cleanTrack = new String[trainTrack.length][];

		this.testTrack = copyOf(trainTrack);
		this.cleanTrack = copyOf(trainTrack);
		this.algoName = algoName;
		this.trackName = trackName;
		this.crashName = crashName;
		this.crashChoice = crashChoice;
		try {
			car = new Car(trainTrack, crashName);
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		maxIter = 360000;//set maximum number of training iterations
		rewards = new HashMap<Pair, HashMap<Pair, HashMap<Pair, Double>>>(); //initialize rewards map
		printTrackInfo(algoName, trackName, crashName);
		//super.get_logger().log(Level.INFO, "Started training.\n");
        train();
        //super.get_logger().log(Level.INFO, "Done training.\n");
        //super.get_logger().log(Level.INFO, "Started testing.\n");
        test();
        //super.get_logger().log(Level.INFO, "Done testing, algorithm complete.");
        //super.get_logger().log(Level.INFO, "Number of crashes: " + testCars.carCrashes + "\nNumber of timesteps: " + ti);
        System.out.println(finishingCars);
	}

	void train() {
		Random r = new Random();
		int sampleSize = 50;
		int prevDist = -1;
		//int scount = 0;
		initialize(); // fill the rewards map with initial values
		finishingCars = 0;
		int[][] olocs = car.openLocs;
		int[][] nlocs = new int[olocs.length + car.startLocs.length][];
		for(int j = 0; j < olocs.length; j++){
			nlocs[j] = olocs[j];
		}
		for(int j = olocs.length; j < car.startLocs.length + olocs.length; j++){
			nlocs[j] = car.startLocs[j-olocs.length];
		}
		int[][] flocs = car.finishLocs;
		for (int i = 0; i < maxIter; i++) {
			if(alpha > 0.1){
				alpha = alpha - 1/maxIter;//anneal learning rate
			}else{
				alpha=0.1;
			}
			car.setTraining();
			// select Position
			//ArrayList<Pair> positions = new ArrayList<Pair>(rewards.keySet());
			//Pair position = positions.get((int) Math.floor(Math.random() * positions.size()));
			int d = (int)(sampleSize*Math.floor(i/sampleSize));
			
			nlocs = shuffleArray(nlocs);
			//flocs = shuffleArray(flocs);
			//shuffleArray(flocs);
			Pair position = findPairWithin(d, flocs, nlocs, prevDist);
			if(i <= nlocs.length*sampleSize){
				prevDist = d;
			}
			else{
				prevDist = 0;
			}
//			prevDist = d;
			// select Velocity
			int velocityx = r.nextInt(11)-5;
			int velocityy = r.nextInt(11)-5;
			boolean besthashmap = false;
			boolean besthashmap2 = false;
			boolean besthashmap3 = false;
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
					//if key exists
					if (rewards.containsKey(position2)) {

						// find max reward of actions
						double maxreward = getMaxValue(rewards.get(position2).get(velocity2));
						// get previously calculated q
						double q = rewards.get(position).get(velocity).get(act);
						// get reward for the action
						double reward = getReward(trainTrack, position2.x, position2.y);
						// calculate q
						double newq = q + (alpha * (reward + (gamma * maxreward) - q));
						//super.get_logger().log(Level.INFO, "iteration: " + i + ", position: (" + position.x + "," + position.y + "), velocity: (" + velocity.x + "," + velocity.y + "), acceleration: ("
							//	+ act.x + "," + act.y + "), old reward: " + q + ", new reward: " + newq);
						// add q to rewards hashmap
						rewards.get(position).get(velocity).put(act, newq);
					} else {
						//invalid move! should be caught by collision detection
						super.get_logger().log(Level.INFO, "BAD MOVES:" + position2.x + " " + position2.y + " , " + velocity2.x + " "
								+ velocity2.y);
					}
				}

			}
			//evaluate learning so far
			if(i%sampleSize == 0){
				besthashmap = runthrough(i);
			}
//			if(besthashmap){
//				besthashmap2 = runthrough(i);
//				if(besthashmap2){
//					besthashmap3 = runthrough(i);
//					if(besthashmap3){
//						break;
//					}
//				}
//			}
		}
		//super.get_logger().log(Level.INFO, "Number of cars that made it to the finish line: " + finishingCars);
		

	}

	void test() {
		//test q-learning
		ti = runthrough2();
	}

	boolean runthrough(int it) {
		//super.get_logger().log(Level.INFO, "Iteration Info");
		String[][] tracks = copyOf(cleanTrack);//new clean track for evaluation
		Car testCar;
		try {
			testCar = new Car(tracks, crashChoice);
		} catch (IOException e) {
			e.printStackTrace();
			//if testCar fails to initialize end method
			super.get_logger().log(Level.INFO, "Car failed to initialize");
			return false;
		}
		testCar.putCarAtStart();//start testCar
		
		Pair pos = getPositionPair(testCar.positionX, testCar.positionY);
		Pair vel = getVelocityPair(pos, testCar.velocityX, testCar.velocityY);
		boolean raceover = false;
		int t = 0;
		int max0 = 0;//if the algorithm uses the accel (0,0) 25 times in a row, end the test
		while (!raceover && testCar.carCrashes < 100 && max0 < 25 && t < 13000) {//if the car gets to the finish line
			//or if the car has crashed 100 times, end the test
			if (rewards.get(pos) != null && rewards.get(pos).get(vel) != null) {//if keys exist in rewards
				Pair maxact = getMaxAction(rewards.get(pos).get(vel));//get the action with the maximum reward
				if(maxact.x==0 && maxact.y ==0){ 
					max0++; // increment max0 if warranted
				}
				if(maxact.x != 0 || maxact.y != 0){
					max0 = 0;// reset max0 if one (or both) accel values are not 0
				}
				raceover = drive(testCar, maxact.x, maxact.y);//move car
				
				pos = getPositionPair(testCar.positionX, testCar.positionY);//set position and velocity to current position and velocity
				vel = getVelocityPair(pos, testCar.velocityX, testCar.velocityY);
				
				//super.get_logger().log(Level.INFO, "time step: " + t);
				//System.out.println(t + " " + it);
			} else {
				//invalid move! should be caught by collision detection
				//super.get_logger().log(Level.INFO, "BAD MOVES:" + pos.x + " " + pos.y + " , " + vel.x + " " + vel.y);
			}
			t++;

		}
		super.get_logger().log(Level.INFO, algoName + "," + trackName + "," + crashName + "," + "training" + "," + it + "," + t + "," + testCar.carCrashes);
		if (raceover) {
			finishingCars++; // increment finished cars if the car reaches the finish line
			return true;
			//super.get_logger().log(Level.INFO, "Car FINISHEDDDD!!!! I WIN ALL THE THINGS!!!!");
		}
		return false;
	}
	public int runthrough2() {
		//super.get_logger().log(Level.INFO, "Test Info");
		String[][] tracks = copyOf(testTrack);
		Car testCar;
		try {
			testCar = new Car(tracks, crashChoice);
		} catch (IOException e) {
			//if testCar fails to initialize end method
			e.printStackTrace();
			super.get_logger().log(Level.INFO, "Car failed to initialize");
			return 0;
		}
		testCar.putCarAtStart();//start testCar
		
		Pair pos = getPositionPair(testCar.positionX, testCar.positionY);
		Pair vel = getVelocityPair(pos, testCar.velocityX, testCar.velocityY);
		boolean raceover = false;
		int t = 0;
		int max0 = 0; //if the algorithm uses the accel (0,0) 100 times in a row, end the test
		while (!raceover && t < 13000){// && testCar.carCrashes < 100) {//if the car gets to the finish line
			//or if the car has crashed 100 times, end the test
			if (rewards.get(pos) != null && rewards.get(pos).get(vel) != null) {//if keys exist in rewards
				Pair maxact = getMaxAction(rewards.get(pos).get(vel));//get the action with the maximum reward
				if(maxact.x==0 && maxact.y ==0){// increment max0 if warranted
					max0++;
				}
				if(maxact.x != 0 || maxact.y != 0){
					max0 = 0;// reset max0 if one (or both) accel values are not 0
				}
				raceover = drive(testCar, maxact.x, maxact.y);
				
				pos = getPositionPair(testCar.positionX, testCar.positionY);
				vel = getVelocityPair(pos, testCar.velocityX, testCar.velocityY);
				
				//super.get_logger().log(Level.INFO, "timestep: " + t);
				//printTrack(tracks, t, testCar, maxact.x, maxact.y);

			} else {
				//invalid move! should be caught by collision detection
				//super.get_logger().log(Level.INFO, "BAD MOVES:" + pos.x + " " + pos.y + " , " + vel.x + " " + vel.y);
			}
			t++;

		}
		if (raceover) {
			finishingCars++;
			//super.get_logger().log(Level.INFO, "Car FINISHEDDDD!!!!");
		}
		super.get_logger().log(Level.INFO, algoName + "," + trackName + "," + crashName + "," + "testing" + "," + "n/a" + "," + t + "," + testCar.carCrashes);

		testCars = testCar;
		return t;
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
							tertiary.put(act, -200.0);
							// create a HashMap with each value equal to -1
						}
					}
					intermediate.put(vel, tertiary);
				}

			}
			rewards.put(pos, intermediate);

		}
		int[][] flocs = car.finishLocs;
		// for each finish position:
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
		int[][] slocs = car.startLocs;

		// for each start position:
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
							tertiary.put(act, -200.0);
							// create a HashMap with each value equal to -1
						}

					}
					intermediate.put(vel, tertiary);
				}

			}
			rewards.put(pos, intermediate);

		}
		
	}

	private double getReward(String[][] track, int x, int y) {
		if (track[y][x].equals("F")) {//if at finish
			return 0.0;
		} else {//everything else
			return -1.0;
		}
	}

	// info for printing out for samples run
	public void printTrackInfo(String algoName, String trackName, String crashName) {
		super.get_logger().log(Level.INFO, "Running " + algoName + " on " + trackName + " with " + crashName);
		super.get_logger().log(Level.INFO, "");
	}

	private Pair getPositionPair(int x, int y) {//get the position. Used to handle the issue where Pair object x and y 
		//values are identical to a key in the hashmap, but the objects are not the same objects
		ArrayList<Pair> positions = new ArrayList<Pair>(rewards.keySet());
		Pair testPair = new Pair(x, y);
		for (Pair p : positions) {
			if (testPair.equals(p)) {
				return p;
			}
		}
		return null;
	}

	private Pair getVelocityPair(Pair p, int x, int y) {//get the velocity Pair. Used to handle the issue where Pair object x and y 
		//values are identical to a key in the hashmap, but the objects are not the same objects
		ArrayList<Pair> velocities = new ArrayList<Pair>(rewards.get(p).keySet());
		Pair testPair = new Pair(x, y);
		for (Pair v : velocities) {
			if (testPair.equals(v)) {
				return v;
			}
		}
		return null;
	}

	private Pair getActionPair(Pair p, Pair v, int x, int y) {//get the acceleration Pair. Used to handle the issue where Pair object x and y 
		//values are identical to a key in the hashmap, but the objects are not the same objects
		ArrayList<Pair> actions = new ArrayList<Pair>(rewards.get(p).get(v).keySet());
		Pair testPair = new Pair(x, y);
		for (Pair a : actions) {
			if (testPair.equals(a)) {
				return a;
			}
		}
		return null;
	}

	private Double getMaxValue(HashMap<Pair, Double> actionMap) {//get the maximum reward
		HashMap.Entry<Pair, Double> maxEntry = null;

		for (HashMap.Entry<Pair, Double> entry : actionMap.entrySet()) {
			if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
				//if(maxEntry == null || entry.getValue() != -1){
					maxEntry = entry;
				//}
			}
		}
		return maxEntry.getValue();
	}

	private Pair getMaxAction(HashMap<Pair, Double> actionMap) {//get the action with the maximum reward
		HashMap.Entry<Pair, Double> maxEntry = null;

		for (HashMap.Entry<Pair, Double> entry : actionMap.entrySet()) {
			if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
				//if(maxEntry == null || entry.getValue() != -1){
					maxEntry = entry;
				//}
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
		if (input == null){
			return null;
		}
		String[][] result = new String[input.length][];
		for (int r = 0; r < input.length; r++) {
			result[r] = input[r].clone();
		}
		return result;
	}
	
	private int integerDistance(int x1, int y1, int x2, int y2){
		double dist = Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
		return ((int)Math.floor(dist));
	}
	
	private Pair findPairWithin(int dist, int[][] finishLocs, int[][] openLocs, int prevDist){
		
		for(int i = 0; i < openLocs.length; i++){
			for(int j = 0; j < finishLocs.length; j++){
				int d = integerDistance(openLocs[i][0], openLocs[i][1], finishLocs[j][0], finishLocs[j][1]);
				if(d <= dist && d >= prevDist){
					Pair p = getPositionPair(openLocs[i][0], openLocs[i][1]);
					return p;
				}
			}
		}
		int mindist = integerDistance(openLocs[0][0], openLocs[0][1], finishLocs[0][0], finishLocs[0][1]);
		int[] nearestPair = openLocs[0];
//		for(int i = 0; i < openLocs.length; i++){
//			for(int j = 0; j < finishLocs.length; j++){
//				int d = integerDistance(openLocs[i][0], openLocs[i][1], finishLocs[j][0], finishLocs[j][1]);
//				if(d <= mindist){
//					mindist = d;
//					nearestPair = openLocs[i]; 
//				}
//			}
//		}
		return getPositionPair(nearestPair[0], nearestPair[1]);
	}
	private int[][] shuffleArray(int[][] ar){
	    // If running on Java 6 or older, use `new Random()` on RHS here
	    Random r = new Random();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = r.nextInt(i+1);
	      // Simple swap
	      int[] a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	    return ar;
	  }
}