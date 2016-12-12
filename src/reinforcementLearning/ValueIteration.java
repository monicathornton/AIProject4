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
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.Arrays;

public class ValueIteration extends Driver {
    // holds the racetrack and variables selected by the user
    private String[][] trainTrack;
    private String[][] testTrack;
    private String[][] cleanTrack;


    private String algoName;
    private int t;
    private String trackName;
    private String crashName;
    private String crashChoice;

    private HashMap<String, Double> states = new HashMap<>(); //cell xy then velocity xy -> utility
    private final double successfulMove = .8;
    private final double stayInPlace = .2;
    
    // appropriate values of gamma and error were determined by pruning process
    // selected values for gamma and error that gave best results on tuning track 
    private final double gamma = .07; //prefer long term rewards
    private final double error = 0.000000001;
    
    private HashMap<String, ArrayList> policy = new HashMap(); //state -> action


    private int[] velocityPossibilities = {-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5};
    private String[] actions = {"0,0", "1,1", "-1,-1",
            "0,1", "1,0",
            "0,-1", "-1,0",
            "1,-1", "-1,1"};

    private Car trainCar;
    private Car testCar;


    //TODO: clean this up when done testing
    public ValueIteration(String[][] trainTrack, String algoName, String trackName, String crashName, String crashChoice) throws IOException {
        this.trainTrack = trainTrack;

        testTrack = new String[trainTrack.length][];
        cleanTrack = new String[trainTrack.length][];

        this.testTrack = copyOf(trainTrack);
        this.cleanTrack = copyOf(trainTrack);

        this.algoName = algoName;
        this.trackName = trackName;
        this.crashName = crashName;
        this.crashChoice = crashChoice;

        this.trainCar = new Car(trainTrack, crashChoice);
        this.trainCar.setTraining();
        printTrackInfo(algoName, trackName, crashName);


        super.get_logger().log(Level.INFO, "Started training.\n");
        train();
        super.get_logger().log(Level.INFO, "Done training.\n");
        super.get_logger().log(Level.INFO, "Number of actions: " + actions.length + "\nNumber of velocity values: " + velocityPossibilities.length + "\nNumber of states: " + states.size() );
        super.get_logger().log(Level.INFO, "Gamma = " + gamma + "\nError " + error);

        testCar = new Car(testTrack, crashChoice);
        super.get_logger().log(Level.INFO, "Started testing.\n");
        test();
        super.get_logger().log(Level.INFO, "Done testing, algorithm complete.");
        super.get_logger().log(Level.INFO, "Number of crashes: " + testCar.carCrashes + "\nNumber of timesteps: " + t);

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
                    trainTrack = copyOf(cleanTrack);
                    double temp = calcTransition(state, action);
                    if (maxAction < temp) {
                        maxAction = temp;
                        String[] stateParts = state.split(",");
                        actX = Integer.valueOf(stateParts[0]);
                        actY = Integer.valueOf(stateParts[1]);
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
        int sameSpot = 0;
        String oldSpot = "00";
        boolean finished = false;
        t = 0;
        testCar.putCarAtStart();
        printTrack(testTrack, t, testCar, 0, 0);
        while (!finished) {
            String curState = String.format("%d,%d,%d,%d", testCar.positionY, testCar.positionX, testCar.velocityX, testCar.velocityY);

            if (String.format("%d%d", testCar.positionY, testCar.positionX).equals(oldSpot)){
                sameSpot++;
            }
            else{
                sameSpot = 0;
            }

            ArrayList<String> preferredActions = policy.get(curState);
            Random r = new Random();

            if (sameSpot > 3){  //Don't get stuck in death loop (anyone seen supernatural?)
                int index = r.nextInt(actions.length);
                preferredActions.add(actions[index]);
            }

            int index = r.nextInt(preferredActions.size());
            String[] actionParts = preferredActions.get(index).split(",");
            int xa = Integer.valueOf(actionParts[0]);
            int ya = Integer.valueOf(actionParts[1]);
            finished = drive(testCar, xa, ya);
            t++;
            //printTrackConsole(testTrack, t, trainCar, xa, ya);
            printTrack(testTrack, t, testCar, xa, ya);
            oldSpot = String.format("%d%d", testCar.positionY, testCar.positionX);
        }
        System.out.println("Done!");

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
        } catch (Exception e) {
            newStateUtility = trainMove(action);
        }


        // utility and possibility of moving
        total += successfulMove * newStateUtility;
        return total;

    }

    private Double trainMove(String action) {
        try {
            String[] actionParts = action.split(",");
            int xa = Integer.valueOf(actionParts[0]);
            int ya = Integer.valueOf(actionParts[1]);

            String newState;

//            if (trainTrack[trainCar.positionY][trainCar.positionX].equals("#") || trainTrack[trainCar.positionY][trainCar.positionX].equals("X") && xa == 0 || ya == 0) {
//                return 0.0; //We should not remain in place when in a wall!!!
//            } else {
//                this.trainCar.newPosition(xa, ya);
//            }
            this.trainCar.newPosition(xa, ya);


            if (trainTrack[trainCar.positionY][trainCar.positionX].equals("#") || trainTrack[trainCar.positionY][trainCar.positionX].equals("X")) {
                newState = String.format("%d,%d,%d,%d", trainCar.positionY, trainCar.positionX, 0, 0);
            } else {

                newState = String.format("%d,%d,%d,%d", trainCar.positionY, trainCar.positionX, trainCar.velocityX, trainCar.velocityY);
            }
            return states.get(newState);
        } catch (Exception e) {
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
        String[] stateParts = curState.split(",");

        this.trainCar.positionX = Integer.valueOf(stateParts[1]);
        this.trainCar.positionY = Integer.valueOf(stateParts[0]);
        this.trainCar.velocityX = Integer.valueOf(stateParts[2]);
        this.trainCar.velocityY = Integer.valueOf(stateParts[3]);

    }


    /*
        Create a dictionary of states, key =cell,velocity value=utility
     */
    private void createStates() {
        // for each cell not a wall cell
        for (int row = 0; row < trainTrack.length; row++) {
            for (int col = 0; col < trainTrack[row].length; col++) {
                // for each possible combination of velocities in x and y direction
//                trainTrack[row][col] = "D";
//                printTrackConsole(trainTrack, 0, trainCar, 0, 0);
                String key = String.format("%d,%d,%d,%d", row, col, 0, 0);

                if (trainTrack[row][col].equals("#")) {  //You're only ever going to be at zero when against (actually in) a wall
                    states.put(key, -1.0);
                } else {

                    for (int xv = 0; xv < velocityPossibilities.length; xv++) {
                        for (int yv = 0; yv < velocityPossibilities.length; yv++) {

                            key = String.format("%d,%d,%d,%d", row, col, velocityPossibilities[xv], velocityPossibilities[yv]);

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


    /*
        Create a dictionary of policies with state -> action
     */
    private void createPolicy() {

        ArrayList<String> allAction = new ArrayList();
        //for every state
        for (String state : states.keySet()) {
            setTrainState(state);
            double maxUtility = -10000.0;
            String bestAction = "00";
            //   for every action
            for (String action : actions) {
                double U = trainMove(action);
                //  if U > maxU then action = bestAction
                if (U > maxUtility) {
                    maxUtility = U;
                    bestAction = action;
                    allAction.add(bestAction);
                }
                else if (U == maxUtility){
                    allAction.add(action);
                }
            }

            policy.put(state, allAction);
            allAction = new ArrayList<>();
        }
    }

}

