package reinforcementLearning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.*;

/*
   Learning algorithm for optimizing a timed run of a racecar through differently shaped racetracks.
*/
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

    //cell xy then velocity xy -> utility
    private HashMap<String, Double> states = new HashMap<>();
    //probabilities of sucessfully moving or staying in place for each move
    private final double successfulMove = .8;
    private final double stayInPlace = .2;

    // appropriate values of gamma and error were determined by pruning process
    // selected values for gamma and error that gave best results on tuning track 
    private final double gamma = .07; //prefer long term rewards
    private final double error = 0.000000001;

    //state -> action
    private HashMap<String, ArrayList> policy = new HashMap();


    private int[] velocityPossibilities = {-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5};
    private String[] actionsPossibilities = {"0,0", "1,1", "-1,-1",
            "0,1", "1,0",
            "0,-1", "-1,0",
            "1,-1", "-1,1"};

    private Car trainCar;
    private Car testCar;

    /*
        Constructor
     */
    public ValueIteration(String[][] trainTrack, String algoName, String trackName, String crashName, String crashChoice) throws IOException {
        //track to train on
        this.trainTrack = trainTrack;
        //track to run actual race on
        testTrack = new String[trainTrack.length][];
        //clean version of original track
        cleanTrack = new String[trainTrack.length][];

        //deep copy tracks
        this.testTrack = copyOf(trainTrack);
        this.cleanTrack = copyOf(trainTrack);



        //run information
        this.algoName = algoName;
        this.trackName = trackName;
        this.crashName = crashName;
        this.crashChoice = crashChoice;
        this.trainCar = new Car(trainTrack, crashChoice);
        this.trainCar.setTraining();
        printTrackInfo(algoName, trackName, crashName);

        //train value iteration algorithm to obtain all utilities and an optimal policy
//        super.get_logger().log(Level.INFO, "Started training.\n");
        train();
//        super.get_logger().log(Level.INFO, "Done training.\n");
//        super.get_logger().log(Level.INFO, "Number of actions: " + actionsPossibilities.length + "\nNumber of velocity values: " + velocityPossibilities.length + "\nNumber of states: " + states.size());
//        super.get_logger().log(Level.INFO, "Gamma = " + gamma + "\nError " + error);

        //run car on track given information from optimal policy
//        super.get_logger().log(Level.INFO, "Started testing.\n");
        test();
//        super.get_logger().log(Level.INFO, "Done testing, algorithm complete.");
//        super.get_logger().log(Level.INFO, "Number of crashes: " + testCar.carCrashes + "\nNumber of timesteps: " + t);

        super.get_logger().log(Level.INFO, "ValueIteration," + trackName + "," + crashName + "," + "testing," + "n/a" + "," + t + "," + testCar.carCrashes);

    }



    /*
        Create a deep copy of a 2d String array
     */
    public String[][] copyOf(String[][] input) {
        if (input == null)
            return null;
        String[][] result = new String[input.length][];
        for (int r = 0; r < input.length; r++) {
            result[r] = input[r].clone();
        }
        return result;
    }

    /*
        Setup data structures and run value iteration algorithm, then create policy dictionary
     */
    void train() {
        createStates();
        valueIterate();
        createPolicy();
    }

    /*
        Actual value iteration algorithm. Loop through all states (combinations of position and velocities),
        updating utilities according to immediate rewards and utility of next state after action taken.
        Continue until change in states' utilities' is below a threshold.
     */
    private void valueIterate() {
        int iterationNumber = 0;
        int numActions = 0;
        //difference in former and updated utility
        double delta = 0;
        double threshold = error * (1 - gamma) / gamma;

        //state after action taken
        String statePrime;
        //former utility
        double U = 0.0;
        //new utility
        double UPrime = 0.0;
        //former x location
        int oldX = 0;
        //former y location
        int oldY = 0;

        while (delta < threshold) {
            //calculate utility for each state
            U = UPrime;
            for (String state : states.keySet()) {

                double maxActionUtility = 0.0;
                //set car in approriate state

                // get action that returns best utility
                for (String action : actionsPossibilities) {
                    try {
                        setTrainState(state);
                    }
                    catch (Exception e){

                    }

                    numActions += 1;
                    //reset track
                    trainTrack = copyOf(cleanTrack);

                    //get utilities of new states according to probability distribution
                    double temp = calcTransition(state, action);

                    //update action if needed
                    if (maxActionUtility < temp) {
                        maxActionUtility = temp;
                        String[] stateParts = state.split(",");
                        oldX = Integer.valueOf(stateParts[0]);
                        oldY = Integer.valueOf(stateParts[1]);
                    }
                }

                //calculate Bellman update
                UPrime = getReward(oldX, oldY) + gamma * maxActionUtility;

                //Put new utility into states
                states.put(state, UPrime);
                test();
                super.get_logger().log(Level.INFO, "ValueIteration," + trackName + "," + crashName + "," + "training," + iterationNumber + "," + t + "," + testCar.carCrashes);
                iterationNumber++;


                // update delta
                if (Math.abs(UPrime - U) > delta) {
                    delta = Math.abs(UPrime - U);
                }

                if (delta < threshold) {
                    break;
                }

            }

        }
    }

    /*
        Run car on racetrack until passes finish line. Each action is chosen from the optimal policy function, except
        when the car has been in the same cell three times consecutively, in which case a random action is added to the
        possible actions for the state.
     */
    void test() {
        //setup and show track
        try {
            testCar = new Car(testTrack, crashChoice);
        }
        catch (IOException e){

        }
        int sameSpot = 0;
        String oldSpot = "00";
        boolean finished = false;
        t = 0;

        testCar.putCarAtStart();
//        printTrack(testTrack, t, testCar, 0, 0);

        //finished is defined as being on or crossing a finished cell
        while (!finished) {
            //get current state, ie position and x and y velocities
            String curState = String.format("%d,%d,%d,%d", testCar.positionY, testCar.positionX, testCar.velocityX, testCar.velocityY);

            //Check to see if the car has moved to a different cell than the last iteration
            if (String.format("%d%d", testCar.positionY, testCar.positionX).equals(oldSpot)) {
                sameSpot++;
            } else {
                sameSpot = 0;
            }

            //get best actions as defined by policy
            ArrayList<String> preferredActions = policy.get(curState);
            Random r = new Random();

            //Don't get stuck in death loop (anyone seen supernatural?)
            //this happens when the policy is wrong and has the car stay in place
            //or repeated commit suicide against wall
            //to counter, just add element of randomness to choice
            if (sameSpot > 3) {
                int index = r.nextInt(actionsPossibilities.length);
                preferredActions.add(actionsPossibilities[index]);
            }

            //choose action at random from perferred acitons
            int index = r.nextInt(preferredActions.size());
            String[] actionParts = preferredActions.get(index).split(",");
            int xa = Integer.valueOf(actionParts[0]);
            int ya = Integer.valueOf(actionParts[1]);

            //actual movement of car
            finished = drive(testCar, xa, ya);

            //update values and print track
            t++;
//            printTrack(testTrack, t, testCar, xa, ya);
            oldSpot = String.format("%d%d", testCar.positionY, testCar.positionX);
        }
    }


    /*
        Get reward for movement. All moves cost -1 except a finishing cell, which is 0.
     */
    private double getReward(int x, int y) {
        if (trainTrack[x][y].equals("F")) {
            return 0.0;
        } else {
            return -1.0;
        }
    }

    /*
       Calculates the utilities of old state and new state given an action and according to probability distribution.
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

    /*
        Given an state and action, take the action to put the car into the new state. Because this is training, the
        probability of successfully or not moving should NOT be included.
     */
    private Double trainMove(String action) {
        String[] actionParts = action.split(",");
        int xa = Integer.valueOf(actionParts[0]);
        int ya = Integer.valueOf(actionParts[1]);

        //actual movement of car
        this.trainCar.newPosition(xa, ya);

        //Calculate new state. If car is in wall or crashed, velocities should always be 0 0.
        String newState;
        if (trainTrack[trainCar.positionY][trainCar.positionX].equals("#") || trainTrack[trainCar.positionY][trainCar.positionX].equals("X")) {
            newState = String.format("%d,%d,%d,%d", trainCar.positionY, trainCar.positionX, 0, 0);
        } else {

            newState = String.format("%d,%d,%d,%d", trainCar.positionY, trainCar.positionX, trainCar.velocityX, trainCar.velocityY);
        }

        //return utility of new state.
        return states.get(newState);
    }


    /*
        Print out information regarding track and run
     */
    public void printTrackInfo(String algoName, String trackName,
                               String crashName) {
        super.get_logger().log(Level.INFO, "Running " + algoName + " on " + trackName + " with " + crashName);
        super.get_logger().log(Level.INFO, "");
    }

    /*
        Put the car in a new state. Used for training.
     */
    private void setTrainState(String curState) throws IOException {
        this.trainCar = new Car(trainTrack, crashChoice);
        t = 0;
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
                String key = String.format("%d,%d,%d,%d", row, col, 0, 0);

                //You're only ever going to be at zero when against (actually in) a wall
                if (trainTrack[row][col].equals("#")) {
                    states.put(key, -1.0);
                } else {
                    // for each possible combination of velocities in x and y direction
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
        Create a dictionary of policies with state -> actions. Actions with equal utility are added, and then chosen at
        random during actual racing.
     */
    private void createPolicy() {

        ArrayList<String> allAction = new ArrayList();
        //for every state
        for (String state : states.keySet()) {
            try {
                setTrainState(state);
            }
            catch (Exception e){

            }
            double maxUtility = -10000.0;
            String bestAction = "00";
            //   for every action
            for (String action : actionsPossibilities) {
                double U = trainMove(action);
                //  if U > maxU then action = bestAction
                if (U > maxUtility) {
                    maxUtility = U;
                    bestAction = action;
                    allAction.add(bestAction);
                } else if (U == maxUtility) {
                    allAction.add(action);
                }
            }
            //update policy and reset for next state
            policy.put(state, allAction);
            allAction = new ArrayList<>();
        }
    }

}

