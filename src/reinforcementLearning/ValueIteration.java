package reinforcementLearning;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;

import reinforcementLearning.Driver;

public class ValueIteration extends Driver {
	// holds the racetrack and variables selected by the user
	private String[][] track;
	private String algoName;
	private String trackName;
	private String crashName;
	private String crashChoice;
	
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

		// keeps track of the time step
		int t = 0;
		
		// creates the racecar
		Car c = new Car(track, crashChoice);

		// puts the car at a random starting position
		c.putCarAtStart();
		printTrack(track, t, c, accelerationX, accelerationY);
		
		// for the first run, sets the prev x and y position to the current position
		prevPosX = c.getPositionX();
		prevPosY = c.getPositionY();
		
		// the below is all for testing, MT will remove after finished with car/driver
		//int newX, int newY, int oldX, int oldY
		
		boolean raceOver = false;
		
		// TODO: start at beginning and loop this to see if we come up with any errors (try on every track, with random accel vals) 
		int i = 0;
		
		//while (!raceOver) {
		while(i < 100) {
			i++;
			t++;
			Random rando = new Random();
			
	       	int randomNumX = rando.nextInt(accelVals.length);  
	       	int randomNumY = rando.nextInt(accelVals.length); 
	       	
	       	accelerationX = randomNumX;
	       	accelerationY = randomNumY;
	       	
			// after we make move, check if race is over (value of move car - or whatever we send from Driver will be true)
			raceOver = drive(c, accelerationX, accelerationY);
			printTrack(track, t, c, accelerationX, accelerationY);
			
		}
		
		super.get_logger().log(Level.INFO, "Car has finished race at time " + t);
		
		//TODO race over
		//raceOver = c.moveCar(c.positionX, c.positionY, prevPosX, prevPosY);	
		
			

		
		
		
		
//		c.moveCar(c.positionX, c.positionY, prevPosX, prevPosY);
//		printTrack(track, t, c);
//		t++;
//		prevPosX = c.positionX;
//		prevPosY = c.positionY;
//		c.positionY += 1;
//		
//		c.moveCar(c.positionX, c.positionY, prevPosX, prevPosY);
//		printTrack(track, t, c);
//		t++;
//		prevPosX = c.positionX;
//		prevPosY = c.positionY;
//		c.positionY+= 1;
//		
//		c.moveCar(c.positionX, c.positionY, prevPosX, prevPosY);		
//		printTrack(track, t, c);
//		t++;
//		prevPosX = c.positionX;
//		prevPosY = c.positionY;
//		c.positionY += 1;
//		
//		c.moveCar(c.positionX, c.positionY, prevPosX, prevPosY);
//		printTrack(track, t, c);
//		t++;
//		prevPosX = c.positionX;
//		prevPosY = c.positionY;
//		c.positionY += 1;
//		
//		c.moveCar(c.positionX, c.positionY, prevPosX, prevPosY);		
//		printTrack(track, t, c);
//		t++;
//		prevPosX = c.positionX;
//		prevPosY = c.positionY;
//		c.positionX += 2;	
		
//		c.moveCar(c.positionX, c.positionY, prevPosX, prevPosY);		
//		printTrack(track, t, c);
//		t++;
//		prevPosX = c.positionX;
//		prevPosY = c.positionY;
//		c.positionY -= 1;		
//		
//		c.moveCar(c.positionX, c.positionY, prevPosX, prevPosY);		
//		printTrack(track, t, c);
//		t++;
//		prevPosX = c.positionX;
//		prevPosY = c.positionY;
//		c.positionY -= 1;	
		
		
	
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

