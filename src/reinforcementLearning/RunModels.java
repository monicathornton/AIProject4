package reinforcementLearning;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class RunModels {

	public static void main(String[] args) throws IOException {
		// Scanner for reading in the user's choice
		Scanner in = new Scanner(System.in);

		// keeps track of user choices
		String algoChoice = "";
		String trackChoice = "";
		String crashChoice = "";

		// keeps track of names associated with user choices
		String algoName = "";
		String trackName = "";
		String crashName = "";

		// gets location of track (for sending to TrackBuilder)
		String trackFilePath = "";

		// gives the user algorithm choices, forces them to choose from the list
		while (!algoChoice.equalsIgnoreCase("v") && !algoChoice.equalsIgnoreCase("q")) {
//
			System.out.println("***Please pick from one of the following algorithms***");
			System.out.println("To race a car using Value Iteration, please type 'v'");
			System.out.println("To race a car using Q-learning, please type 'q'");

			// holds the user's choice of algorithm
			algoChoice = in.nextLine().toLowerCase();

			if (!algoChoice.equalsIgnoreCase("v") && !algoChoice.equalsIgnoreCase("q")) {
				System.out.println(algoChoice + " is an invalid choice for algorithm, please choose again.");
				System.out.println();
			} else {
				System.out.println();
			}
		} // end while: user has picked a valid algo choice
		//algoChoice = "v";

//		// gives the user a series of track choices, forces them to choose from the list
//		while (!trackChoice.equalsIgnoreCase("l") && !trackChoice.equalsIgnoreCase("o") && !trackChoice.equalsIgnoreCase("r") && !trackChoice.equalsIgnoreCase("t") && !trackChoice.equalsIgnoreCase("s")) {
//			System.out.println("***Please pick a racetrack***");
//			System.out.println("To select the L-track please type 'l'");
//			System.out.println("To select the O-track please type 'o'");
//			System.out.println("To select the R-track please type 'r'");
//			System.out.println("To select the training track please type 't'");
//			System.out.println("To select the simple track please type 's'");
//
//			// holds the user's choice of track
//			trackChoice = in.nextLine().toLowerCase();
//
//			if (!trackChoice.equalsIgnoreCase("l") && !trackChoice.equalsIgnoreCase("o") && !trackChoice.equalsIgnoreCase("r") && !trackChoice.equalsIgnoreCase("t") && !trackChoice.equalsIgnoreCase("s")) {
//				System.out.println(trackChoice + " is an invalid choice for the racetrack, please choose again.");
//				System.out.println();
//			} else {
//				System.out.println();
//			}
//
//		} // end while: user has picked a valid track choice
		trackChoice = "s";
		
//		// gives the user the crash choices, forces them to choose from the list
//		while (!crashChoice.equalsIgnoreCase("b") && !crashChoice.equalsIgnoreCase("w") && !crashChoice.equalsIgnoreCase("x")) {
//
//			System.out.println("***Please indicate the severity of crashing***");
//			System.out.println("If crashing places the car back at the crash site type 'b'");
//			System.out.println("If crashing places the car back at the beginning type w'");
//			System.out.println("Type 'x' to exit the program");
//
//			// holds the user's choice of crash variation
//			crashChoice = in.nextLine().toLowerCase();
//
//			if (!crashChoice.equalsIgnoreCase("b") && !crashChoice.equalsIgnoreCase("w") && !crashChoice.equalsIgnoreCase("x")) {
//				System.out.println(crashChoice + " is an invalid choice for crash variation, please choose again.");
//				System.out.println();
//			} else {
//				System.out.println();
//			}
//
//
//		} // end while: user has picked a valid crash choice
		crashChoice = "b";
		
		// associates the user's choices with names of algorithm, track & crash type
		switch (algoChoice) {
		case "v":
			algoName = "Value Iteration";
			break;
		case "q":
			algoName = "Q-learning";
			break;
		}

		switch (trackChoice) {
		case "l":
			trackName = "L-track";
			break;
		case "o":
			trackName = "O-track";
			break;
		case "r":
			trackName = "R-track";
			break;
		case "t":
			trackName = "Training-track";
			break;		
		case "s":
			trackName = "Simple-track";
			break;	
		}

		switch (crashChoice) {
		case "b":
			crashName = "in situ restart on crash.";
			break;
		case "w":
			crashName = "restart at the beginning on crash.";
			break;
		case "x":
			System.out.println("Exiting program.");
			in.close();
			System.exit(0);			
		}

		// get the file path for the race track of interest
		trackFilePath = getTrack(trackName);

		// reads the racetrack in from the file
		TrackBuilder track = new TrackBuilder(trackFilePath); 
		String[][] racetrack = track.buildTrack();
		
		if (algoChoice.equals("v")) {
			// call algo with track data
			Driver v = new ValueIteration(racetrack, algoName, trackName, crashName, crashChoice);
			
			// TODO: call racecar in each algo (crashName tells us which crash variant to use) - pass data and crashName (or corresponding Boolean)
			
		} else if (algoChoice.equals("q")) {
			// call algo with track data
			Driver q = new QLearning(racetrack, algoName, trackName, crashName);			
			
		} 

		// closes the scanner
		in.close();
	}

	// gets the location of the racetrack 
	public static String getTrack(String trackName) {
		// gets the os for the computer this program is run on
		String os = System.getProperty("os.name").toLowerCase();

		File file = new File("");

		// get the location where the data files are stored
		String filePathData = file.getAbsolutePath();

		// uses file separator so is operating system agnostic
		if (os.startsWith("windows")) { // Windows
			filePathData += File.separator + "racetracks" + File.separator + trackName + ".txt";
		} else if (os.startsWith("mac")) { // Mac
			filePathData += File.separator + "racetracks" + File.separator + trackName + ".txt";
		} else {
			// everything else
			filePathData += File.separator + "racetracks" + File.separator + trackName + ".txt";
		}

		// calls the file chooser, returns the updated file path
		System.out.println("Racetrack Location: " + filePathData);
		System.out.println();
		return filePathData;
	}
}
