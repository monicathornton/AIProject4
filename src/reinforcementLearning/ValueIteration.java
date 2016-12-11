package reinforcementLearning;

/*
inputs:
	States S
	action A(s)
	transition model P(s'|a,s)
	rewards R(s)
	discount y
	error e

local:
	U, U' vectors of utilities for states in S, initially 0
	delta, max change in utility of any state in a single iteration

###############################

	repeat
		U <- U';
		delta <- 0

		for each state s in S do
			U'[s] <- R(s) + γarg maxa SUM P(s'|a,s)U[s']
			if | U'[s] - U[s] | < delta
				delta <- | U'[s] - U[s] |
	until
		delta < e(1 - γ) / Y

###############################
 */
import java.io.IOException;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.logging.Level;

import reinforcementLearning.Driver;

public class ValueIteration extends Driver {
	// holds the racetrack and variables selected by the user
	private String[][] track;
	private String algoName;
	private String trackName;
	private String crashName;
	private String crashChoice;

	private HashMap states = new HashMap<String, Double>(); //c(x,y)v(x,y) -> utility
	private final double successfulMove = .8;
	private final double stayInPlace = .2;
	private HashMap policy = new HashMap<String, Double>(); //state -> action



	private double normalCellRewoard = -0.4;
	private int[] velocityPossibilities = {-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5};
	private String[] actions = {"00",  "11", "-1-1",
								"01",  "10",
								"0-1", "-10",
								"1-1", "-11"};

	// the previous position of the car (used for collision detection and map updates)
	private int prevPosX;
	private int prevPosY;

	// acceleration/deceleration (as determined by the algorithm)
	int accelerationX;
	int accelerationY;
	
	int[] accelVals = {-1, 0, 1};
	
	//TODO: clean this up when done testing
	public ValueIteration(String[][] track, String algoName, String trackName, String crashName, String crashChoice) throws IOException {
		this.track = track;
		this.algoName = algoName;
		this.trackName = trackName;
		this.crashName = crashName;
		this.crashChoice = crashChoice;
				
		printTrackInfo(algoName, trackName, crashName);

		int t = 0;
		
		Car c = new Car(track, crashChoice);

		//c.putCarAtStart();
		
		//c.positionX = c.getCarLocation(track)[0];
		//c.positionY = c.getCarLocation(track)[1];
		c.positionX = 32;
		c.positionY = 2;
		prevPosX = c.positionX;
		prevPosY = c.positionY;
		printTrackConsole(track, t, c, accelerationX, accelerationY);

		train();

	
	}
	
	
	void train() {
		createStates();
		double delta = 0;
		double threshold = 1;
		while (delta < threshold){

		}
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


	/*
		Create a dictionary of states, key =cell,velocity value=utility
 	*/
	private void createStates(){
		// for each cell not a wall cell
		for (int row = 0; row < track.length - 1; row++){
			for (int col = 0; col < track.length - 1; col++){
					// for each possible combination of velocities in x and y direction
					for (int x = 0; x < velocityPossibilities.length -1; x++){
						for (int j = 0; j < velocityPossibilities.length -1; j++){


							if (track[row][col].equals("#")) {
								states.put(String.format("c(%s,%s)v(%s,%s)", row, col, x, j), -10.0);
							}
							else if(track[row][col].equals("F")){
								states.put(String.format("c(%s,%s)v(%s,%s)", row, col, x, j), +10.0);
							}
							else {
								states.put(String.format("c(%s,%s)v(%s,%s)", row, col, x, j), 0.0);
							}

					}
				}
			}
		}
	}



	/*
	 	returns P(s|a,s)
	 */
	private double transitionResult(String curentState, String wantedState){
		return 0.0;
	}




}

