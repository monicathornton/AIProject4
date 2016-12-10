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
import java.util.logging.Level;

import reinforcementLearning.Driver;

public class ValueIteration extends Driver {
	private String[][] track;
	private String algoName;
	private String trackName;
	private String crashName;
	
	private int positionX = 7;
	private int positionY = 1;
	private int prevPosX = 0;
	private int prevPosY = 0;
	
	
	public ValueIteration(String[][] track, String algoName, String trackName, String crashName) throws IOException {
		this.track = track;
		this.algoName = algoName;
		this.trackName = trackName;
		this.crashName = crashName;
		
		
		printTrackInfo(algoName, trackName, crashName);
		Car c = new Car(track);
		
		// the below is all for testing, MT will remove after finished with car/driver
		// newX, newY, oldX, oldY
		track = c.moveCar(track, positionX, positionY, prevPosX, prevPosY);
		prevPosX = positionX;
		prevPosY = positionY;
		printTrack(track);
		positionY++;
		
		track = c.moveCar(track, positionX, positionY, prevPosX, prevPosY);
		prevPosX = positionX;
		prevPosY = positionY;
		printTrack(track);
		positionY++;
		
		track = c.moveCar(track, positionX, positionY, prevPosX, prevPosY);
		printTrack(track);		
		prevPosX = positionX;
		prevPosY = positionY;
		positionY++;

		track = c.moveCar(track, positionX, positionY, prevPosX, prevPosY);
		printTrack(track);		
		prevPosX = positionX;
		prevPosY = positionY;
		positionY++;
		
		track = c.moveCar(track, positionX, positionY, prevPosX, prevPosY);
		printTrack(track);		
		prevPosX = positionX;
		prevPosY = positionY;
		positionX+=2;	
		
		track = c.moveCar(track, positionX, positionY, prevPosX, prevPosY);
		printTrack(track);		
		positionX++;
		prevPosX = positionX;
		prevPosY = positionY;

		
		track = c.moveCar(track, positionX, positionY, prevPosX, prevPosY);
		printTrack(track);		
		
		
		
	
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

