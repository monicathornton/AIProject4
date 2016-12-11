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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.Arrays;

public class ValueIteration extends Driver {
    // holds the racetrack and variables selected by the user
    private String[][] trainTrack;
    private String[][] testTrack;
    private String algoName;
    private String trackName;
    private String crashName;
    private String crashChoice;

    private HashMap<String, Double> states = new HashMap<>(); //cell xy then velocity xy -> utility
    private final double successfulMove = .8;
    private final double stayInPlace = .2;
    private final double gamma = .3; //prefer short term rewards
    private final double error = .1;
    private HashMap<String, String> policy = new HashMap(); //state -> action


    private double normalCellRewoard = -0.4;
    private int[] velocityPossibilities = {-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5};
    private String[] actions = {"00", "11", "-1-1",
            "01", "10",
            "0-1", "-10",
            "1-1", "-11"};

    private Car trainCar;
    private Car testCar;

    int[] accelVals = {-1, 0, 1};

    //TODO: clean this up when done testing
    public ValueIteration(String[][] trainTrack, String algoName, String trackName, String crashName, String crashChoice) throws IOException {
        this.trainTrack = trainTrack;

        testTrack = new String[trainTrack.length][];


        this.testTrack = copyOf(trainTrack);
        this.algoName = algoName;
        this.trackName = trackName;
        this.crashName = crashName;
        this.crashChoice = crashChoice;

        this.trainCar = new Car(trainTrack, crashChoice);
        this.trainCar.setTraining();
        printTrackInfo(algoName, trackName, crashName);

        int t = 0;


        printTrackConsole(trainTrack, t, trainCar, 0, 0);

        //bug replication
        trainCar.positionX = 9;
        trainCar.positionY = 7;
        trainCar.velocityX = 3;
        trainCar.velocityY = -4;
        trainCar.newPosition(1,1);
        String a = trainTrack[trainCar.positionX][trainCar.positionY];

//        train();
//        super.get_logger().log(Level.INFO, "Done Training!");
//        testCar = new Car(testTrack, crashChoice);
//        test();
//        super.get_logger().log(Level.INFO, "Done Testing!");


    }

    public String[][] copyOf(String[][] input) {
        if (input == null)
            return null;
        String[][] result = new String[input.length][];
        for (int r = 0; r < input.length; r++) {
            result[r] = input[r].clone();
        }
        return result;
    }

    void train() {
        createStates();
        double delta = 0;
        double threshold = error * (1 - gamma) / gamma;

        String statePrime;
        double U = 0.0;
        double UPrime = 0.0;
        int actX = 0;
        int actY = 0;

        while (delta < threshold) {
            //calculate utility for each state
            U = UPrime;
            for (String state : states.keySet()) {

                // get action that returns best utility
                double maxAction = 0.0;
                setTrainState(state);

                for (String action : actions) {
                    double temp = calcTransition(state, action);
                    if (maxAction < temp) {
                        maxAction = temp;
                        actX = Integer.valueOf(Character.getNumericValue(state.charAt(0)));
                        actY = Integer.valueOf(Character.getNumericValue(state.charAt(1)));
                    }
                }

                UPrime = getReward(actX, actY) + gamma * maxAction;

                states.put(state, UPrime);


                // update delta
                if (Math.abs(UPrime - U) > delta) {
                    delta = Math.abs(UPrime - U);
                }

                if (delta < threshold) {
                    break;
                }

            }
        }

        createPolicy();
    }

    void test() {

        boolean finished = false;
        int t = 0;
        printTrackConsole(testTrack, t, trainCar, 0, 0);
        testCar.putCarAtStart();
        while (!finished) {
            String curState = String.format("%d%d%d%d", testCar.positionY, testCar.positionX, testCar.velocityX, testCar.velocityY);
            String action = policy.get(curState);
            int xa = Integer.valueOf(Character.getNumericValue(action.charAt(0)));
            int ya = Integer.valueOf(Character.getNumericValue(action.charAt(1)));
            finished = drive(testCar, xa, ya);
            t++;
            printTrackConsole(testTrack, t, trainCar, xa, ya);
        }

    }

    private double getReward(int x, int y) {
        if (trainTrack[x][y].equals("F")) {
            return 0.0;
        } else {
            return -1.0;
        }
    }

    /*
       returns P(s'|s, a)*U(s')
    */
    private double calcTransition(String curState, String action) {
            // utility and possibility of staying in place
            double total = stayInPlace * states.get(curState);

        double newStateUtility = 0.0;
        try {
             newStateUtility = trainMove(action);
        }
        catch (Exception e){
            newStateUtility = trainMove(action);
        }

            // utility and possibility of moving
            total += successfulMove * newStateUtility;
            return total;

    }

    private Double trainMove(String action) {
        try {
            int xa = Integer.valueOf(Character.getNumericValue(action.charAt(0)));
            int ya = Integer.valueOf(Character.getNumericValue(action.charAt(1)));

            String newState;

            if (trainTrack[trainCar.positionX][trainCar.positionY].equals("#") || trainTrack[trainCar.positionX][trainCar.positionY].equals("X") && xa == 0 || ya == 0) {
                return -100.0; //We should not remain in place when in a wall!!!
            } else {
                this.trainCar.newPosition(xa, ya);
            }


            if (trainTrack[trainCar.positionX][trainCar.positionY].equals("#") || trainTrack[trainCar.positionX][trainCar.positionY].equals("X")) {
                newState = String.format("%d%d%d%d", trainCar.positionX, trainCar.positionY, 0, 0);
            } else {

                newState = String.format("%d%d%d%d", trainCar.positionX, trainCar.positionY, trainCar.velocityX, trainCar.velocityY);
            }
            return states.get(newState);
        }
        catch (Exception e){
            System.out.println("Train Move");
            return 0.0;
        }
    }


    // info for printing out for samples run
    public void printTrackInfo(String algoName, String trackName,
                               String crashName) {
        super.get_logger().log(Level.INFO, "Running " + algoName + " on " + trackName + " with " + crashName);
        super.get_logger().log(Level.INFO, "");
    }

    private void setTrainState(String curState) {
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
    }



    /*
        Create a dictionary of states, key =cell,velocity value=utility
     */
    private void createStates() {
        // for each cell not a wall cell
        for (int row = 0; row < trainTrack.length; row++) {
            for (int col = 0; col < trainTrack[0].length; col++) {
                // for each possible combination of velocities in x and y direction
                String key = String.format("%d%d%d%d", col, row, 0, 0);

                if (trainTrack[row][col].equals("#")) {  //You're only ever going to be at zero when against (actually in) a wall
                    states.put(key, -10.0);
                } else {

                    for (int xv = 0; xv < velocityPossibilities.length - 1; xv++) {
                        for (int yv = 0; yv < velocityPossibilities.length - 1; yv++) {

                            key = String.format("%d%d%d%d", col, row, velocityPossibilities[xv], velocityPossibilities[yv]);

                            if (trainTrack[row][col].equals("F")) {
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


    private void createPolicy() {
        double maxUtility = -10000.0;
        String bestAction = "00";
        //for every state
        for (String state : states.keySet()) {
            setTrainState(state);
            //   for every action
            for (String action : actions) {
                double U = trainMove(action);
                //  if U > maxU then action = bestAction
                if (U > maxUtility) {
                    maxUtility = U;
                    bestAction = action;
                }
            }
        policy.put(state, bestAction);
        }
    }


    }

