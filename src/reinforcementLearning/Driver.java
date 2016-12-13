package reinforcementLearning;

// Logging examples obtained from https://examples.javacodegeeks.com/core-java/util/logging/java-util-logging-example/
import java.io.IOException;
import java.util.Random;
import java.util.logging.*;

public abstract class Driver {
	
    private static final Logger LOGGER = Logger.getLogger(Driver.class.getName());
        
    public Driver(){

        try {
            Handler consoleHandler = null;
            Handler fileHandler = null;
            //Creating consoleHandler and fileHandler
            consoleHandler = new ConsoleHandler();
            fileHandler = new FileHandler("./output.txt");
            System.out.println("NOTE THAT THE LOGGER OVERWRITES WHATEVER IS ALREADY IN OUTPUT!!!!!");
            System.out.println("Used log level info for writing to file and fine for debugging.");

            Formatter customFormater = new CustomFormatter();
            
            //Assigning handlers to LOGGER object
            LOGGER.addHandler(consoleHandler);
            consoleHandler.setFormatter(customFormater);

            LOGGER.addHandler(fileHandler);
            fileHandler.setFormatter(customFormater);

            //Setting levels to handlers and LOGGER
            consoleHandler.setLevel(Level.ALL);
            fileHandler.setLevel(Level.INFO);
            LOGGER.setLevel(Level.ALL);
        }
        catch(IOException exception){

        }

    }

    public Logger get_logger(){
        return LOGGER;
    }

    abstract void train();
    abstract void test();
 
    // Given the acceleration value from the algorithm (Value Iteration or Q-learning), drive the racecar.
    // There is a non-deterministic element to acceleration, where there is a 20% chance that the acceleration
    // will fail and the velocity will remain unchanged
    public boolean drive(Car c, int accelX, int accelY) {
    	
        Random rando = new Random();
       	int randomNum = rando.nextInt(10);    	

       	// deals with the non-determinism, where 20% of time acceleration/deceleration fails
    	if (randomNum == 7 && randomNum == 3) {
    		accelX = 0;
    		accelY = 0;
    	}
       	
    	boolean raceOver = false;
     	
		raceOver = c.newPosition(accelX, accelY);
		
		return raceOver;
    }
    
   
	// print out the racetrack
	public void printTrack(String[][] thisTrack, int t, Car c, int accelerationX, int accelerationY) {
		String thisLine = "";
		
		LOGGER.log(Level.INFO, "At time t = " + t + ":");
		LOGGER.log(Level.INFO, "  Position = (" + c.getPositionX() + "," + c.getPositionY() + ")");
		LOGGER.log(Level.INFO, "  Velocity = (" + c.getVelocityX() + "," + c.getVelocityY() + ")");	
		LOGGER.log(Level.INFO, "  Acceleration = (" + accelerationX + "," + accelerationY + ")");
		
		for (int i = 0; i < thisTrack.length; i++) {
			thisLine = "";
			
			for (int j = 0; j < thisTrack[i].length; j++) {
				thisLine += thisTrack[i][j];	
			}
			LOGGER.log(Level.INFO, thisLine);	
		}
		LOGGER.log(Level.INFO, "");
	}

    // print out the racetrack
    public void printTrackConsole(String[][] thisTrack, int t, Car c, int accelerationX, int accelerationY) {
        String thisLine = "";
        System.out.println("Position = (" + c.getPositionX() + "," + c.getPositionY() + ")");
        System.out.println("Velocity at time step t = " + t + " is (" + c.velocityX + "," + c.velocityY + ")");
        System.out.println("Acceleration at time step t = " + t + " is (" + accelerationX + "," + accelerationY + ")");

        for (int i = 0; i < thisTrack.length; i++) {
            thisLine = "";

            for (int j = 0; j < thisTrack[i].length; j++) {
                thisLine += thisTrack[i][j];
            }
            System.out.println(thisLine);
        }
        System.out.println("");
    }
   
}

class CustomFormatter extends Formatter{
    public String format(LogRecord record){
        StringBuilder builder = new StringBuilder(1000);
        builder.append(formatMessage(record));
        builder.append("\n");
        return builder.toString();
    }
}