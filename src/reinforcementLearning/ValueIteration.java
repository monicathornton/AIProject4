package reinforcementLearning;

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

