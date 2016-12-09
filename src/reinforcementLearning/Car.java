package reinforcementLearning;

import java.io.IOException;
import java.util.Random;
import java.util.logging.*;

public class Car {
	// the racetrack for racing this car
	private String[][] track; 
	
	// the choice of crash variant selected by the user
	private String crashChoice;
	
	// logger for printing out info related to this car
	Logger carLog = Logger.getLogger(Driver.class.getName());

	// keeps track of x and y locs in case of collision
	private int xCrash = -1;
	private int yCrash = -1;
	
	// keeps track of the number of times the car has crashed
	public int carCrashes = 0;
	
	// x and y positions of car at time t
	public int positionX;
	public int positionY;
	
	//TODO: bound velocity between -5 to 5
	// x and y components of velocity at time t
	public int velocityX = 0;
	public int velocityY = 0;

	// holds the location where the car started
	public int startLocX;
	public int startLocY;
	
	/*
	 * The following store the locations of the open spots on the track, 
	 * the start locations, the finish locations and the wall 
	 * locations. Does this at the beginning so we are working with the 
	 * original (not updated) version of the racetrack.
	 */
	int [][] openLocs;
	int [][] startLocs;
	int [][] finishLocs;	
	int [][] wallLocs;
	
	public Car(String[][] track, String crashChoice) throws IOException {
		this.track = track;
		this.crashChoice = crashChoice;
		
		// gets the open, start, finish and wall locations
		openLocs = getOpenLocs();
		startLocs = getStartLocs();
		finishLocs = getFinishLocs();
		wallLocs = getWallLocs();
	}

	
	// place car on track at specified location
	// return true if finish line is crossed
	public boolean moveCar(int newX, int newY, int oldX, int oldY) {
		// car is currently at the position indicated by oldX, oldY
		// update the track (at this point car can be on open track or start line)
		
		System.out.println(newX);
		System.out.println(newY);
		System.out.println(oldX);
		System.out.println(oldY);
		
		
		// check to see if the location of the car was on one of the open spaces
		for (int i = 0; i < openLocs.length; i++) {			
		
			if (openLocs[i][0] == oldY && openLocs[i][1] == oldX) {				
				// replace the space on the track with the appropriate symbol before replacing with new car loc 
				track[oldY][oldX] = ".";
			}

		} // end for: have checked if the car is in the open locs (so we can replace the right symbol when the car moves)
		
		
		// check to see if the location of the car was on one of the start spaces
		for (int i = 0; i < startLocs.length; i++) {
			if (startLocs[i][0] == oldY && startLocs[i][1] == oldX) {
			// replace the space on the track with the appropriate symbol before replacing with new car loc 
				track[oldY][oldX] = "S";
			}
		}// end for: have checked if the car is in the open locs (so we can replace the right symbol when the car
		

		boolean carCrash;
		boolean crossedFinish = false ;
		
		// checks if the vehicle intersects with any walls on its path between the old and new location
		carCrash = collisionDetection(newX, newY, oldX, oldY);
		
		if (!carCrash) {
			track[newY][newX] = "C";
			
			positionX = newX;
			positionY = newY;
			crossedFinish = endRace(positionX, positionY); 
		} else {
			// places an X on the track to show where the car has crashed on its run
			track[yCrash][xCrash] = "X";
			crashHandler(crashChoice, oldX, oldY, newX, newY);		
		}
		
		return crossedFinish;
	
	}	

	// Checks if the car has crossed the finish line
	public boolean endRace(int positionX, int positionY) {
		boolean crossedFinish = false;
		for (int i = 0; i < finishLocs.length; i++) {
			if (finishLocs[i][0] == positionY && finishLocs[i][1] == positionX) {
				// are on the finish line
				crossedFinish = true;
			}
		}
		
		return crossedFinish;
	}
	
	// TODO: bound this
	public int updateVelocityX(int accelX) {
		
		velocityX = velocityX + accelX;
		
		return velocityX;
	}

	// TODO: bound this
	public int updateVelocityY(int accelY) {
		
		velocityY = velocityY + accelY;
			
		return velocityY;
	}
	
	//TODO: get position
	//TODO: bound this, or bounded by walls? play around with this to check
	public boolean newPosition(int accelX, int accelY) {
		boolean raceFinished; 
		int oldX = positionX;
		int oldY = positionY;
		
		positionY = positionY + updateVelocityY(accelY);
		positionX = positionX + updateVelocityX(accelX);		
		
		raceFinished = moveCar(positionX, positionY, oldX, oldY);
		
		return raceFinished;
	}
	
	//TODO: get velocity
	
	//TODO: get acceleration
	
		
	// handles the two versions of crashing, as specified in the project requirements
	public void crashHandler(String crashChoice, int oldX, int oldY, int newX, int newY) {
		// increments the number of crashes
		carCrashes++;
		carLog.log(Level.INFO, "BOOM! Crashed into a wall at (" + xCrash + ", " + yCrash + ")");
		
		if (crashChoice.equalsIgnoreCase("b")) {
			crashV1(oldX, oldY, xCrash, yCrash);
		} else {
			crashV2();
		}
	}
	
	// handles the "bad" version of crashing, where the car is placed at the nearest position on the track
	// to where it crashed, and velocity is set to 0,0
	public void crashV1(int oldX, int oldY, int crashX, int crashY) {
		velocityX = 0;
		velocityY = 0; 
		
		int newX = crashX;
		int newY = crashY;
		
		// TODO: check direction coming from, place on closest side
		int dX = oldX - newX;
		int dY = oldY - newY;
		
		// try > 0 direction (where the car came from)
		if (dX != 0) {
			if (dX > 0) {
				if (!collisionDetection(newX + 1, newY, oldX, oldY)) {
					newX++;
				} else if (!collisionDetection(newX - 1, newY, oldX, oldY)) {
					newX--;
				} else if (!collisionDetection(newX, newY + 1, oldX, oldY)) {
					newY++;
				} else if (!collisionDetection(newX, newY - 1, oldX, oldY)) {
					newY--;
				}
			} else {
				// dx < 0
				if (!collisionDetection(newX - 1, newY, oldX, oldY)) {
					newX--;
				} else if (!collisionDetection(newX + 1, newY, oldX, oldY)) {
					newX++;
				} else if (!collisionDetection(newX, newY + 1, oldX, oldY)) {
					newY++;
				} else if (!collisionDetection(newX, newY - 1, oldX, oldY)) {
					newY--;
				}				
			}
		} else {
			// dY != 0
			if (dY > 0) {
				if (!collisionDetection(newX, newY + 1, oldX, oldY)) {
					newY++;
				} else if (!collisionDetection(newX, newY - 1, oldX, oldY)) {
					newY--;
				} else if (!collisionDetection(newX + 1, newY, oldX, oldY)) {
					newX++;
				} else if (!collisionDetection(newX - 1, newY, oldX, oldY)) {
					newX--;
				}
			} else {
				if (!collisionDetection(newX, newY - 1, oldX, oldY)) {
					newY--;
				} else if (!collisionDetection(newX, newY + 1, oldX, oldY)) {
					newY++;
				} else if (!collisionDetection(newX + 1, newY, oldX, oldY)) {
					newX++;
				} else if (!collisionDetection(newX - 1, newY, oldX, oldY)) {
					newX--;
				}
			}
		} // end if
		
		if (newX < track.length && newX >= 0 && newY < track.length && newY >= 0) {
			track[newY][newX] = "C";		
		} else {
			// TODO: handle this
			
		}
		
		carLog.log(Level.INFO, "After crash restart at (" + newX + ", " + newY + ")");
	
		positionX = newX;
		positionY = newY;
	}	
	
	// handles the "worse" version of crashing, where the car is placed back at the original starting position
	// and velocity is set to 0,0
	public void crashV2() {
		// TODO: Set velocity to 0,0
		velocityX = 0;
		velocityY = 0;
		
		// put car back at original starting location
		carLog.log(Level.INFO, "After crash restart at (" + startLocX + ", " + startLocY + ")");
		track[startLocY][startLocX] = "C";
		
		//TODO: test this
		positionX = startLocX;
		positionY = startLocY;
	}
	
	
	// get the location of the car on the map
	public int[] getCarLocation(String[][] track) {
		// xLoc is in position 0, yLoc is in position 1
		int[] carLoc = new int[2];
		int xLoc;
		int yLoc;
		
		// if car is found, return location as int array
		for (int i = 0; i < track.length; i++) {
			for (int j = 0; j < track[i].length; j++) {
				if (track[i][j].equalsIgnoreCase("C")) {
					yLoc = i;
					xLoc = j;
					carLoc[0] = yLoc;
					carLoc[1] = xLoc;
					return carLoc;
				}				
			}
		}
		
		// else return null
		return null;
	}
	
	// places the car at a random location on the starting line
	public void putCarAtStart() {
		Random rand = new Random();
		int randomNum = rand.nextInt(startLocs.length);			
		
		startLocY = startLocs[randomNum][0];
		startLocX = startLocs[randomNum][1];
		
		track[startLocY][startLocX] = "C";
		carLog.log(Level.INFO, "Car on start line at (" + startLocX + ", " + startLocY + ")");
		
	}
	
	// gets the locations for the starting line
	// note: this is only used to get a valid location on the starting line, this information is 
	// not directly accessible to the learner
	private int[][] getStartLocs() {
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
	// TODO: we are not using this currently, get rid of if we don't end up needing
	private int[][] getOpenLocs() {
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
	// note: this is only used to check when we have crossed the finish line, this information is not
	// directly accessible to the learner
	private int[][] getFinishLocs() {
		// keep track of the number of times a start loc is encountered
		int finishLocsCount = 0;
		
		String findChar = "F";
		
		for (int i = 0; i < track.length; i++) {
			for (int j = 0; j < track[i].length; j++) {
				if(track[i][j].equalsIgnoreCase(findChar)) {
					finishLocsCount++;
				}
			} 
		} // end for: have counted the number of times the finish loc symbol (F) has been encountered		
		
		finishLocs = new int[finishLocsCount][finishLocsCount]; 
		
		finishLocs = buildArray(track, findChar, finishLocsCount);
			
		// get all start locations in original racetrack
		return finishLocs;
	}	
	
	// gets the locations for the walls
	// note: this is only used to check when we have crashed into a wall, this information is not directly
	// accessible to the learner
	private int[][] getWallLocs() {
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

	/*
	 * The following uses the Supercover approach described in 
	 * http://www.redblobgames.com/grids/line-drawing.html and 
	 * http://playtechs.blogspot.com/2007/03/raytracing-on-grid.html
	 * to determine if the vehicle crashes into a wall on its trip. 
	 * This is done by indicating all grid squares that a line between the two points (newPoint and oldPoint)
	 * would pass through. These points are checked to make sure that none of them are walls.
	 * Implementation below based heavily on two sources given above.
	 * 	 
	 * Output: TRUE in case of collision with wall, else returns FALSE.
	 * 
	 */
	
	public boolean collisionDetection(int newX, int newY, int oldX, int oldY) {
		// difference between new and old x and y locations
		int dX = Math.abs(oldX - newX);
		int dY = Math.abs(oldY - newY);
		
		// starting positions for x and y
		int x = oldX;
		int y = oldY;
		
		// iterator for keeping track of number of times to loop
		int n = 1 + dX + dY;
		
		// determines the sign (and hence direction) to move between locations
		int xInc = (newX > oldX) ? 1: -1;
		int yInc = (newY > oldY) ? 1: -1;
		
		int error = dX - dY;
		
		// each square is size one, so endpoints are offset by 0.5
		dX *= 2;
		dY *= 2;
		
		// for the entire length of the line
	    for (; n > 0; --n)
	    {
	    	// checks to see if the car collides with a wall
	    	for (int i = 0; i < wallLocs.length; i++) {
		    	// check if this x and y location corresponds to a wall
		    	if (wallLocs[i][0] == y && wallLocs[i][1] == x) {
		    		xCrash = x;
		    		yCrash = y;
		    		return true;
					}	    		
	    	}

	        if (error > 0)
	        {
	            x += xInc;
	            error -= dY;
	        }
	        else
	        {
	            y += yInc;
	            error += dX;
	        }
	    }
	    
		// car has not collided with a wall
		return false;
	}
	
	
	
}

