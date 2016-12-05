package reinforcementLearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TrackBuilder {

	private String dataFileLocation;
	private int rows = 0; 
	private int cols = 0;
	private String[][] racetrack;
	
	
	public TrackBuilder(String dataFileLocation) throws IOException {
		this.dataFileLocation = dataFileLocation;
		buildTrack();
	}
	
	public void buildTrack() {
		// read in data
		String currentLine = null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					dataFileLocation)));
		    
		    // get the size of the array, which is the first line in each of the files
			currentLine = br.readLine();
			rows = Integer.parseInt(currentLine.substring(0, currentLine.indexOf(',')));
			cols = Integer.parseInt(currentLine.substring(currentLine.indexOf(',') + 1, currentLine.length()));
		
			// stores the racetrack as it is read in
			racetrack = new String[rows][cols];
		
			int rowCounter = 0;
			
			// while there is still information to be read in
			while ((currentLine = br.readLine()) != null) {
					// have read current line in as a string
					// put each character in the appropriate location in array
					for(int c = 0; c < cols; c++){		
						String thisChar = String.valueOf(currentLine.charAt(c));
						racetrack[rowCounter][c] = thisChar;
			         } // end for
					// increment row counter
				    rowCounter++;
				
			} // end while
			// close the scanner
			br.close();				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// print out the racetrack
	public void printTrack(String[][] thisTrack) {
		for (int i = 0; i < thisTrack.length; i++) {
			for (int j = 0; j < thisTrack[i].length; j++) {
				System.out.print(thisTrack[i][j]);	
			}
			System.out.println();
		}		
	}

	// builds arrays to store locations of characters of interest
	public int[][] buildArray(String thisChar, int arraySize) {
		
		// at each row, element 0 is xLoc, element 1 is yLoc
		int[][] localArray = new int[arraySize][2];
				
		int laIndex = 0;
		
		for (int i = 0; i < racetrack.length; i++) {
			for (int j = 0; j < racetrack[i].length; j++) {
				if(racetrack[i][j].equalsIgnoreCase(thisChar)) {
					localArray[laIndex][0] = i;
					localArray[laIndex][1] = j;
					laIndex++;
				}
			} 
		} // end for: have stored x and y loc every time thisChar has been encountered		
					
		return localArray;
	}
	
	// gets the locations for the starting line
	public int[][] getStartLocs() {
		// array to store start locations (xLoc element 0, yLoc element 1)
		int[][] startLocs;
		// keep track of the number of times a start loc is encountered
		int startLocsCount = 0;
		
		String findChar = "S";
		
		for (int i = 0; i < racetrack.length; i++) {
			for (int j = 0; j < racetrack[i].length; j++) {
				if(racetrack[i][j].equalsIgnoreCase(findChar)) {
					startLocsCount++;
				}
			} 
		} // end for: have counted the number of times the start loc symbol (S) has been encountered		
		
		startLocs = new int[startLocsCount][startLocsCount]; 
		
		startLocs = buildArray(findChar, startLocsCount);
		
		// get all start locations in original racetrack
		return startLocs;
	}
	
	// gets the locations for the finish line
	public int[][] getFinishLocs() {
		// array to store finish locations (xLoc element 0, yLoc element 1)
		int[][] finishLocs;
		// keep track of the number of times a start loc is encountered
		int finishLocsCount = 0;
		
		String findChar = "F";
		
		for (int i = 0; i < racetrack.length; i++) {
			for (int j = 0; j < racetrack[i].length; j++) {
				if(racetrack[i][j].equalsIgnoreCase(findChar)) {
					finishLocsCount++;
				}
			} 
		} // end for: have counted the number of times the start loc symbol (S) has been encountered		
		
		finishLocs = new int[finishLocsCount][finishLocsCount]; 
		
		finishLocs = buildArray(findChar, finishLocsCount);
			
		// get all start locations in original racetrack
		return finishLocs;
	}	
	
	// gets the locations for the walls
	public int[][] getWallLocs() {
		// array to store finish locations (xLoc element 0, yLoc element 1)
		int[][] wallLocs;
		// keep track of the number of times a start loc is encountered
		int wallLocsCount = 0;
		
		String findChar = "#";
		
		for (int i = 0; i < racetrack.length; i++) {
			for (int j = 0; j < racetrack[i].length; j++) {
				if(racetrack[i][j].equalsIgnoreCase(findChar)) {
					wallLocsCount++;
				}
			} 
		} // end for: have counted the number of times the start loc symbol (S) has been encountered		
		
		wallLocs = new int[wallLocsCount][wallLocsCount]; 
		
		wallLocs = buildArray(findChar, wallLocsCount);
			
		// get all start locations in original racetrack
		return wallLocs;
	}		
}

				
