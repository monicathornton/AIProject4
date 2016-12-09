package reinforcementLearning;

import java.io.IOException;
import java.util.logging.Level;

import reinforcementLearning.Driver;

public class ValueIteration extends Driver {
	private String[][] track;
	private String algoName;
	private String trackName;
	private String crashName;
	
	private int prevPosX;
	private int prevPosY;
	private String crashChoice;
	
	int accelerationX;
	int accelerationY;
	
	
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
		
		// the below is all for testing, MT will remove after finished with car/driver
		//int newX, int newY, int oldX, int oldY
		
		boolean raceOver = false;
		
		while (!raceOver) {
			// after we make move, check if race is over (value of move car - or whatever we send from Driver will be true)
			raceOver = drive(c, accelerationX, accelerationY);
			printTrack(track, t, c, accelerationX, accelerationY);
			t++;
			accelerationY = -1;
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

