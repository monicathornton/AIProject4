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
	
	
	public ValueIteration(String[][] track, String algoName, String trackName, String crashName, String crashChoice) throws IOException {
		this.track = track;
		this.algoName = algoName;
		this.trackName = trackName;
		this.crashName = crashName;
		this.crashChoice = crashChoice;
		
		
		printTrackInfo(algoName, trackName, crashName);
		
		Car c = new Car(track, crashChoice);

		//track = c.putCarAtStart();
		
		//positionX = c.getCarLocation(track)[0];
		//positionY = c.getCarLocation(track)[1];
		c.positionX = 1;
		c.positionY = 7;
		prevPosX = c.positionX;
		prevPosY = c.positionY;
		
		// the below is all for testing, MT will remove after finished with car/driver
		//int newX, int newY, int oldX, int oldY
		track = c.moveCar(track, c.positionX, c.positionY, prevPosX, prevPosY);
		printTrack(track);
		prevPosX = c.positionX;
		prevPosY = c.positionY;
		c.positionY += 1;
		
		track = c.moveCar(track, c.positionX, c.positionY, prevPosX, prevPosY);
		printTrack(track);
		prevPosX = c.positionX;
		prevPosY = c.positionY;
		c.positionY+= 1;
		
		track = c.moveCar(track, c.positionX, c.positionY, prevPosX, prevPosY);		
		printTrack(track);
		prevPosX = c.positionX;
		prevPosY = c.positionY;
		c.positionY += 1;
		
		track = c.moveCar(track, c.positionX, c.positionY, prevPosX, prevPosY);
		printTrack(track);
		prevPosX = c.positionX;
		prevPosY = c.positionY;
		c.positionY += 1;
		
		track = c.moveCar(track, c.positionX, c.positionY, prevPosX, prevPosY);		
		printTrack(track);
		prevPosX = c.positionX;
		prevPosY = c.positionY;
		c.positionX += 2;	
		
		track = c.moveCar(track, c.positionX, c.positionY, prevPosX, prevPosY);		
		printTrack(track);
		prevPosX = c.positionX;
		prevPosY = c.positionY;
		c.positionX += 1;		
	
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

