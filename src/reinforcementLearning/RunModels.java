package reinforcementLearning;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import java.io.FileWriter;

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

		// gives the user algorithm choices
		System.out
				.println("Please pick from one of the following reinforcement learning algorithms");
		System.out
				.println("To race a car using Value Iteration, please type 'v'");
		System.out.println("To race a car using Q-learning, please type 'q'");

		// holds the user's choice of algorithm
		algoChoice = in.nextLine().toLowerCase();

		// gives the user a series of track choices
		System.out.println("Please pick a racetrack");
		System.out.println("To select the L-track please type 'l'");
		System.out.println("To select the O-track please type 'o'");
		System.out.println("To select the R-track please type 'r'");
		System.out.println("To select the training track please type 't'");
		// holds the user's choice of track
		trackChoice = in.nextLine().toLowerCase();

		// gives the user the crash choices
		System.out.println("Please indicate the severity of crashing");
		System.out
				.println("If crashing places the car back at the crash site type 'b'");
		System.out
				.println("If crashing places the car back at the beginning type w'");
		System.out.println("Type 'x' to exit");

		// holds the user's choice of crash variation
		crashChoice = in.nextLine().toLowerCase();

		// associates the user's choices with names of algorithm, track and
		// crash type
		// TODO: make sure algo choice, track choice and crash choice are valid choices
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
		}

		switch (crashChoice) {
		case "b":
			crashName = "in situ restart.";
			break;
		case "w":
			crashName = "restart at the beginning.";
			break;
		}

		// TODO: call starting at racetracks data file
		trackFilePath = getTrack(trackName);

		TrackBuilder track = new TrackBuilder(trackFilePath); 
		String[][] racetrack = track.buildTrack();
		
		if (algoChoice.equals("v")) {
			// call algo with track data
			ValueIteration v = new ValueIteration(racetrack, algoName, trackName, crashName);
			
			
			// call racecar in each algo (crashName tells us which crash variant to use) - pass data and crashName (or corresponding Boolean)
			
		} else if (algoChoice.equals("q")) {
			// call algo with track data
			QLearning q = new QLearning(racetrack, algoName, trackName, crashName);			
			
			
		} else {
			// user chose to exit the program or typed their choice incorrectly
			System.out.println("Exiting program.");
			in.close();
			System.exit(0);
		}
		// closes the scanner
		in.close();

	}

	// gets the location of the data for classification (training and test sets
	// are built in the parser)
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
		//System.out.println("Select Racetrack Location");
		//filePathData = callFileChooser(filePathData);
		System.out.println("Racetrack Location: " + filePathData);
		System.out.println();
		return filePathData;
	}

	// calls a window with a pop up box that lets the user choose their exact
	// file location (with input from file string that gives user's home
	// directory.
	public static String callFileChooser(String filePath) {
		// builds a JFrame
		JFrame frame = new JFrame("Folder Selection Pane");
		// string to score the path
		String thisPath = "";

		// JFrame look and feel
		frame.setPreferredSize(new Dimension(400, 200));
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton button = new JButton("Select Folder");

		// sets up the file chooser
		JFileChooser fileChooser = new JFileChooser();
		// uses file path as a starting point for file browsing
		fileChooser.setCurrentDirectory(new File(filePath));
		// choose only from directories
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int fileChosen = fileChooser.showOpenDialog(null);

		// returns either the file path, or nothing (based on user choice)
		if (fileChosen == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			thisPath = selectedFile.getAbsolutePath();
			return thisPath;
		} else {
			return null;
		}
	}
}
