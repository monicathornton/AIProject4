package reinforcementLearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TrackBuilder {
	// location of the racetrack
	private String dataFileLocation;
		
	// 2-d array to store racetrack
	private String[][] racetrack;
	
	public TrackBuilder(String dataFileLocation) throws IOException {
		this.dataFileLocation = dataFileLocation;
	}
	
	// reads in the track from file
	// TODO: flip track
	public String[][] buildTrack() {
		// read in data
		String currentLine = null;
		
		// gets the number of rows and cols from the data file (first line, comma separated)
		int rows = 0; 
		int cols = 0;
		
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
		
		return racetrack;
	}

}

				
