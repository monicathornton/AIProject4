package reinforcementLearning;

import java.io.IOException;

public class Car {
	// the racetrack for racing this car
	private String[][] track; 
	
	// TODO: bound positions based on map
	// x and y positions of car at time t
	int positionx = 0;
	int positiony = 0;
	
	//TODO: bound velocity between -5 to 5
	// x and y components of velocity at time t
	int velocityx = 0;
	int velocityy = 0;

	//TODO: acceleration can only be -1, 0 or 1
	int accelerationx = 0;
	int accelerationy = 0;

	int [][] openLocs;
	int [][] startLocs;
	int [][] finishLocs;	
	int [][] wallLocs;
	
	
	public Car(String[][] track) throws IOException {
		this.track = track;
		openLocs = getOpenLocs();
		startLocs = getStartLocs();
		finishLocs = getFinishLocs();
		wallLocs = getWallLocs();
	}

	
	// place car on track at specified location
	// print out the racetrack
	public String[][] moveCar(String[][] track, int newX, int newY, int oldX, int oldY) {
		// car is currently at the position indicated by oldX, oldY
		// update the track (at this point car can be on open track or start line)
		
		
		
		// check to see if the location of the car was on one of the open spaces
		for (int i = 0; i < openLocs.length; i++) {
//			System.out.println(oldX);
//			System.out.println(openLocs[i][0]);
//			System.out.println(openLocs[i][0] == oldX);
//
//			System.out.println(oldY);
//			System.out.println(openLocs[i][1]);
//			System.out.println(openLocs[i][1] == oldY);
//			
			
			if (openLocs[i][0] == oldX && openLocs[i][1] == oldY) {
				// replace the space on the track with the appropriate symbol before replacing with new car loc 
				track[oldX][oldY] = ".";
			}

		} // end for: have checked if the car is in the open locs (so we can replace the right symbol when the car moves)
		
		System.out.println(oldX);
		System.out.println(oldY);
		System.out.println("==========");
		
		for (int i = 0; i < startLocs.length; i++) {
			System.out.println(startLocs[i][0]);
			System.out.println(startLocs[i][1]);
		}
		
		// check to see if the location of the car was on one of the start spaces
		for (int i = 0; i < startLocs.length; i++) {
			if (startLocs[i][0] == oldX && startLocs[i][1] == oldY) {
			// replace the space on the track with the appropriate symbol before replacing with new car loc 
				System.out.println("in if for S");
				track[oldX][oldY] = "S";
			}
		}// end for: have checked if the car is in the open locs (so we can replace the right symbol when the car
		
		// have updated the map to scrub the old location
		// TODO: Collision detection
		// TODO: Check if we have crossed the finish line
		track[newX][newY] = "C";
		
		// car can only be placed on an open space or finish line
		return track;
	}	
	
	
	
	// write method for collisionDetection(int accelx, int accely)
	
	
	// print out the racetrack
	public void printTrack(String[][] thisTrack) {
		String thisLine = "";
		
		for (int i = 0; i < thisTrack.length; i++) {
			thisLine = "";
			
			for (int j = 0; j < thisTrack[i].length; j++) {
				thisLine += thisTrack[i][j];	
			}
				
			System.out.println();
		}		
	}	
	
	// gets the locations for the starting line
	public int[][] getStartLocs() {
		// array to store start locations (xLoc element 0, yLoc element 1)
		int[][] startLocs;
		// keep track of the number of times a start loc is encountered
		int startLocsCount = 0;
		
		String findChar = "S";
		
		for (int i = 0; i < track.length; i++) {
			for (int j = 0; j < track[i].length; j++) {
				if(track[i][j].equalsIgnoreCase(findChar)) {
					startLocsCount++;
				}
			} 
		} // end for: have counted the number of times the start loc symbol (S) has been encountered		
		
		startLocs = new int[startLocsCount][startLocsCount]; 
		
		startLocs = buildArray(track, findChar, startLocsCount);
		
		// get all start locations in original racetrack
		return startLocs;
	}
	
	// gets the locations for the open spaces on the track
	public int[][] getOpenLocs() {
		// keep track of the number of times a start loc is encountered
		int openLocsCount = 0;
		
		String findChar = ".";
		
		for (int i = 0; i < track.length; i++) {
			for (int j = 0; j < track[i].length; j++) {
				if(track[i][j].equalsIgnoreCase(findChar)) {
					openLocsCount++;
				}
			} 
		} // end for: have counted the number of times the open track symbol (.) has been encountered		
		
		openLocs = new int[openLocsCount][openLocsCount]; 
		
		openLocs = buildArray(track, findChar, openLocsCount);
		
		// get all start locations in original racetrack
		return openLocs;
	}
	
	// gets the locations for the finish line
	public int[][] getFinishLocs() {
		// keep track of the number of times a start loc is encountered
		int finishLocsCount = 0;
		
		String findChar = "F";
		
		for (int i = 0; i < track.length; i++) {
			for (int j = 0; j < track[i].length; j++) {
				if(track[i][j].equalsIgnoreCase(findChar)) {
					finishLocsCount++;
				}
			} 
		} // end for: have counted the number of times the start loc symbol (S) has been encountered		
		
		finishLocs = new int[finishLocsCount][finishLocsCount]; 
		
		finishLocs = buildArray(track, findChar, finishLocsCount);
			
		// get all start locations in original racetrack
		return finishLocs;
	}	
	
	// gets the locations for the walls
	public int[][] getWallLocs() {
		// array to store finish locations (xLoc element 0, yLoc element 1)
		// keep track of the number of times a start loc is encountered
		int wallLocsCount = 0;
		
		String findChar = "#";
		
		for (int i = 0; i < track.length; i++) {
			for (int j = 0; j < track[i].length; j++) {
				if(track[i][j].equalsIgnoreCase(findChar)) {
					wallLocsCount++;
				}
			} 
		} // end for: have counted the number of times the start loc symbol (S) has been encountered		
		
		wallLocs = new int[wallLocsCount][wallLocsCount]; 
		
		wallLocs = buildArray(track, findChar, wallLocsCount);
			
		// get all start locations in original racetrack
		return wallLocs;
	}		
	
	// builds arrays to store locations of characters of interest
	public int[][] buildArray(String[][] track, String thisChar, int arraySize) {
		
		// at each row, element 0 is xLoc, element 1 is yLoc
		int[][] localArray = new int[arraySize][2];
				
		int laIndex = 0;
		
		for (int i = 0; i < track.length; i++) {
			for (int j = 0; j < track[i].length; j++) {
				if(track[i][j].equalsIgnoreCase(thisChar)) {
					localArray[laIndex][0] = i;
					localArray[laIndex][1] = j;
					laIndex++;
				}
			} 
		} // end for: have stored x and y loc every time thisChar has been encountered		
					
		return localArray;
	}


	
	
	
}

