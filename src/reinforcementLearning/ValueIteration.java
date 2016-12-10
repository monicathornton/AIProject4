package reinforcementLearning;

/*
procedure value_iteration(P,r, θ )
inputs:
	P is state transition function specifying P(s|a,s')
	r is a reward function R(s, a,s')
	θ a threshold θ > 0
returns:
	π[s] approximately optimal policy
	V[s] value function
data structures:
	Vk [s] a sequence of value functions

###############################
	begin
		for k = 1 : ∞
			for each state s
				Vk [s] = maxa SUM P(s|a,s)(R(s, a,s) + γ Vk−1[s])
			if ∀s |Vk (s) − Vk−1(s)| < θ
				for each state s
					π(s) = arg maxa SUM P(s|a,s)(R(s, a,s) + γ Vk−1[s])
		return π, Vk
	end
###############################
 */
import java.io.IOException;
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

	private HashMap states = new HashMap<String, Double>();
	private final double successfulMove = .80;
	private double finishCellReward = 0;
	private double normalCellRewoard = -1;
	private int[] velocityPossibilities = {-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5};

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
		System.out.println("HI");
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

	private boolean successfulMove(String curLocation, String wantedLocation){
		double d = Math.random();

		if (d < successfulMove){
			return true;
		}
		else{
			return false;
		}
	}
}

