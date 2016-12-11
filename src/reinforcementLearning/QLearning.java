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
	private double alpha = 0.1; //learning rate
	private double gamma = 0.9; //discount factor

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
		maxIter = 2000;
		rewards = new HashMap<Pair, HashMap<Pair, HashMap<Pair, Double>>>();
		printTrackInfo(algoName, trackName, crashName);
		train();
	}
	
	void train() {
		//car.setTraining();
		initialize();
		int finishingCars = 0;
		//super.get_logger().log(Level.INFO, rewards.toString());
		for(int i = 0; i < maxIter; i++){
			try {
				car = new Car(track, crashName);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			car.setTraining();
			//select position
			int[][]locs = car.openLocs;
			int pox = (int)Math.floor(Math.random()*locs.length);
			int poy = (int)Math.floor(Math.random()*locs[pox].length);
			Pair position = new Pair(pox,poy);//TODO select a real position!
			//select Velocity
			int velocityx = (int)Math.floor(Math.random()*11)-5;
			int velocityy = (int)Math.floor(Math.random()*11)-5;
			Pair velocity = new Pair(velocityx, velocityy);
			for(int m = -1; m <= 1; m++){//for each action
				for(int n = -1; n <= 1; n++){
					Pair act = new Pair(m,n);
					//calculate next position and velocity 
					
					car.positionX = position.x;
					car.positionY = position.y;
					car.velocityX = velocity.x;
					car.velocityY = velocity.y;
					car.newPosition(act.x, act.y);
					
					Pair position2 = new Pair(car.positionX, car.positionY);
					Pair velocity2 = new Pair(car.velocityX, car.velocityY);
					
					printTrackConsole(track, m, car, act.x, act.y);
					if(rewards.get(position2) != null){
						
						//find max reward of actions
						double maxreward = getMaxValue(rewards.get(position2).get(velocity2));
						//get previously calculated q
						double q = rewards.get(position).get(velocity).get(act);
						//get reward for the action
						double reward = getReward(position2.x, position2.y);
						//calculate q
						double newq = q + (alpha*(reward + (gamma*maxreward) - q));
						System.out.println(position.x + " " + position.y + " " + velocity.x + " " + velocity.y + " " +act.x + " " + act.y + ": " + q + " ");
						//add q to rewards hashmap
						rewards.get(position).get(velocity).put(act, newq);
					}
					else{
						HashMap<Pair, Double> bad = new HashMap<Pair, Double>();
						bad.put(act, -10000.0);
						HashMap<Pair, HashMap<Pair, Double>> bad2 = new HashMap<Pair, HashMap<Pair, Double>>();
						bad2.put(velocity2, bad);
						rewards.put(position2, bad2);
					}
				}
			}
			//car.training = false;
			
			try {
				car = new Car(track, crashName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			car.putCarAtStart();
			car.velocityX = 0;
			car.velocityY = 0;
			Pair pos = new Pair(car.positionX, car.positionY);
			Pair vel = new Pair(0,0);
			boolean raceover = false;
			int t = 0;
			int badMoves = 0;
			int badMovesLimit = track.length*track[0].length*2;
			while(!raceover && car.carCrashes <= 100 && badMoves < badMovesLimit){
				//System.out.println(t);
				if(rewards.get(pos) != null){
					Pair maxact = getMaxAction(rewards.get(pos).get(vel));
					raceover = drive(car, maxact.x, maxact.y);
					//if(car.positionX == pos.x && car.positionY == pos.y){
						//System.out.println(car.positionX + " "+ pos.x + " " + car.positionY + " " + pos.y);
					//}
					pos.x = car.positionX;
					pos.y = car.positionY;
					vel.x = car.velocityX;
					vel.y = car.velocityY;
					printTrackConsole(track, t, car, maxact.x, maxact.y);
					
				}else{
					badMoves++;
				}
				t++;
				
				
			}
			if(raceover){
				finishingCars++;
			}
		}
		System.out.println(finishingCars);
		
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
	
	private double getReward(int x, int y) {
        if (track[y][x].equals("F")) {
            return 0.0;
        } else {
            return -1.0;
        }
    }
	// info for printing out for samples run
	public void printTrackInfo(String algoName, String trackName,
			String crashName) {
		super.get_logger().log(Level.INFO, "Running " + algoName + " on " + trackName + " with " + crashName);
		super.get_logger().log(Level.INFO, "");		
	}
	
	private Double getMaxValue(HashMap<Pair, Double> actionMap){
		HashMap.Entry<Pair, Double> maxEntry = null;

		for (HashMap.Entry<Pair, Double> entry : actionMap.entrySet()) {
			if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
				maxEntry = entry;
			}
		}
		return maxEntry.getValue();
	}
	private Pair getMaxAction(HashMap<Pair, Double> actionMap){
		HashMap.Entry<Pair, Double> maxEntry = null;

		for (HashMap.Entry<Pair, Double> entry : actionMap.entrySet()) {
			if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
				maxEntry = entry;
			}
		}
		return maxEntry.getKey();
	}
}