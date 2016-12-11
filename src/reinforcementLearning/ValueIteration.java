package reinforcementLearning;

/*
inputs:
	States S
	action A(s)
	transition model P(s'|a,s)
	rewards R(s)
	discount y
	error e

local:
	U, U' vectors of utilities for states in S, initially 0
	delta, max change in utility of any state in a single iteration

###############################

	repeat
		U <- U';
		delta <- 0

		for each state s in S do
			U'[s] <- R(s) + γarg maxa SUM P(s'|a,s)U[s']
			if | U'[s] - U[s] | < delta
				delta <- | U'[s] - U[s] |
	until
		delta < e(1 - γ) / Y

###############################
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.LinkedHashMap;

import reinforcementLearning.Driver;

public class ValueIteration extends Driver {
    // holds the racetrack and variables selected by the user
    private String[][] track;
    private String algoName;
    private String trackName;
    private String crashName;
    private String crashChoice;

    private HashMap<String, Double> states = new HashMap<>(); //cell xy then velocity xy -> utility
    private final double successfulMove = .8;
    private final double stayInPlace = .2;
    private final double gamma = .5;
    private final double error = .2;
    private HashMap policy = new HashMap<String, Double>(); //state -> action


    private double normalCellRewoard = -0.4;
    private int[] velocityPossibilities = {-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5};
    private String[] actions = {"00", "11", "-1-1",
            "01", "10",
            "0-1", "-10",
            "1-1", "-11"};

    private Car trainCar;

    int[] accelVals = {-1, 0, 1};

    //TODO: clean this up when done testing
    public ValueIteration(String[][] track, String algoName, String trackName, String crashName, String crashChoice) throws IOException {
        this.track = track;
        this.algoName = algoName;
        this.trackName = trackName;
        this.crashName = crashName;
        this.crashChoice = crashChoice;

        this.trainCar = new Car(track, crashChoice);
        printTrackInfo(algoName, trackName, crashName);

        int t = 0;


        printTrackConsole(track, t, trainCar, 0, 0);


        //bug replication?

        this.trainCar.positionX = 2;
        this.trainCar.positionY = 1;
        this.trainCar.velocityX = 2;
        this.trainCar.velocityY = 1;
        this.trainCar.newPosition(1,1);
        String a = track[trainCar.positionX][trainCar.positionY];

//        train();

    }


    void train() {
        createStates();
        double delta = 0;
        double threshold = error * (1 - gamma) / gamma;

        String statePrime;
        double U = 0.0;
        double UPrime;

        while (delta < threshold) {
            //calculate utility for each state
            for (String state : states.keySet()) {

                // get action that returns best utility
                double maxAction = 0.0;
                for (String action : actions) {
                    double temp = calcTransition(state, action);
                    if (maxAction < temp) {
                        maxAction = temp;
                    }
                }

                UPrime = getReward() + gamma * maxAction;

                states.put(state, UPrime);


                // update delta
                if (Math.abs(UPrime - U) > delta) {
                    delta = Math.abs(UPrime - U);
                }

            }
        }
    }

    void test() {
        // TODO Auto-generated method stub

    }

    private double getReward() {
        if (track[trainCar.positionX][trainCar.positionY].equals("F")) {
            return 0.0;
        } else {
            return -1.0;
        }
    }

    /*
       returns P(s'|s, a)*U(s')
    */
    private double calcTransition(String curState, String action) {
        try {
            // utility and possibility of staying in place
            double total = stayInPlace * states.get(curState);

            this.trainCar.positionX = Integer.valueOf(Character.getNumericValue(curState.charAt(0)));
            this.trainCar.positionY = Integer.valueOf(Character.getNumericValue(curState.charAt(1)));

            if (curState.charAt(2) == '-') {  //-x
                this.trainCar.velocityX = 0 - Integer.valueOf(Character.getNumericValue(curState.charAt(3)));
                if (curState.charAt(4) == '-') {  //-y
                    this.trainCar.velocityY = 0 - Integer.valueOf(Character.getNumericValue(curState.charAt(5)));
                } else {  //y
                    this.trainCar.velocityY = Integer.valueOf(Character.getNumericValue(curState.charAt(4)));
                }
            } else {  //x
                this.trainCar.velocityX = Integer.valueOf(Character.getNumericValue(curState.charAt(2)));
                if (curState.charAt(3) == '-') {  //y
                    this.trainCar.velocityY = 0 - Integer.valueOf(Character.getNumericValue(curState.charAt(4)));
                } else {   //y
                    this.trainCar.velocityY = Integer.valueOf(Character.getNumericValue(curState.charAt(3)));
                }

            }


            int xa = Integer.valueOf(Character.getNumericValue(action.charAt(0)));
            int ya = Integer.valueOf(Character.getNumericValue(action.charAt(1)));

            if (track[trainCar.positionX][trainCar.positionY].equals("#") || track[trainCar.positionX][trainCar.positionY].equals("X") && xa == 0 || ya ==0) {
                return -100.0; //We should not remain in place when in a wall!!!
            }
            else{
                this.trainCar.newPosition(xa, ya);
            }

            String newState;

            if (track[trainCar.positionX][trainCar.positionY].equals("#") || track[trainCar.positionX][trainCar.positionY].equals("X")){
                newState = String.format("%d%d%d%d", trainCar.positionX, trainCar.positionY, 0, 0);
            }
            else {

                 newState = String.format("%d%d%d%d", trainCar.positionX, trainCar.positionY, trainCar.velocityX, trainCar.velocityY);
            }

            total *= successfulMove * states.get(newState);
            return total;

        } catch (Exception e) {
            System.out.println("hi");
            return 0.0;
        }

    }


    // info for printing out for samples run
    public void printTrackInfo(String algoName, String trackName,
                               String crashName) {
        super.get_logger().log(Level.INFO, "Running " + algoName + " on " + trackName + " with " + crashName);
        super.get_logger().log(Level.INFO, "");
    }


    /*
        Create a dictionary of states, key =cell,velocity value=utility
     */
    private void createStates() {
        // for each cell not a wall cell
        for (int row = 0; row < track.length; row++) {
            for (int col = 0; col < track[0].length; col++) {
                // for each possible combination of velocities in x and y direction
                String key = String.format("%d%d%d%d", row, col, 0, 0);

                if (track[row][col].equals("#")) {  //You're only ever going to be at zero when against (actually in) a wall
                    states.put(key, -10.0);
                } else {

                    for (int xv = 0; xv < velocityPossibilities.length - 1; xv++) {
                        for (int yv = 0; yv < velocityPossibilities.length - 1; yv++) {

                            key = String.format("%d%d%d%d", row, col, velocityPossibilities[xv], velocityPossibilities[yv]);

                            if (track[row][col].equals("F")) {
                                states.put(key, +10.0);
                            } else {
                                states.put(key, 0.0);
                            }
                        }
                    }
                }
            }
        }
    }


    /*
         returns P(s|a,s)
     */
    private double transitionResult(String curentState, String wantedState) {
        return 0.0;
    }


}

