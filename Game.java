package com.gameboard;

import java.util.HashSet;
import java.util.Scanner;

public class Game {

	public static void main(String[] args) {
		String command = null;
		Scanner in = null;
		boolean playerCreated = false;
		Board myBoard = new Board(10);
		Player p1 = null;
		while (true) {
			System.out.println("ENTER A COMMAND");

			// Accept the input command using the scanner class
			in = new Scanner(System.in);
			command = in.nextLine();
			
			String[] tokens = command.split(" ");
			
			//Check for PLACE command, Ex: PLACE 0 0 NORTH (without ",")
			if (tokens[0].equals("PLACE")) {
				
				//Remove this check in case of multiple players
				if (!playerCreated) {
					try {
						
						int x = Integer.parseInt(tokens[1]);
						int y = Integer.parseInt(tokens[2]);
						
						if (x > 9 || y > 9) {
							throw new NumberFormatException();
						}
						
						if (!tokens[3].equals("NORTH")
								&& !tokens[3].equals("SOUTH")
								&& !tokens[3].equals("WEST")
								&& !tokens[3].equals("EAST")) {
							throw new NumberFormatException();
						}
						
						Location loc = new Location(x, y);

						/*
						 * Create a new player when PLACE command is called. For
						 * multiple players one can perhaps use an array of
						 * Players objects to store each player.
						 */
						p1 = new Player(tokens[3], loc);

						// Add the player to the board
						myBoard.addPlayer(loc);
						playerCreated = true;
						
					} catch (NumberFormatException ex) {
						System.out
								.println("ENTER A VALID POSITION AND DIRECTION");
					}
				} else {
					System.out
							.println("ONLY ONE PLAYER ALLOWED IN THIS SCENARIO");
				}

			}

			/*
			 * In case of multiple players, perhaps one could ask which player
			 * the user wants to move and then perform the operation on that
			 * player
			 */

			else if (tokens[0].equals("TURN")) {
				if (playerCreated) {
					// Make the player turn left
					if (tokens[1].equals("LEFT")) {
						p1.actionTurnLeft();
					} else if (tokens[1].equals("RIGHT")) {
						// Make the player turn right
						p1.actionTurnRight();
					} else {
						System.out.println("ENTER LEFT OR RIGHT");
					}
				} else {
					System.out.println("PLACE A PLAYER FIRST");
				}
			} else if (tokens[0].equals("FORWARD")) {
				// Make the player move forward
				if (playerCreated) {
					myBoard.actionForward(p1);
				} else {
					System.out.println("PLACE A PLAYER FIRST");
				}
			} else if (tokens[0].equals("STATUS")) {
				// Display the status of player
				if (playerCreated) {
					p1.status();
				} else {
					System.out.println("PLACE A PLAYER FIRST");
				}
			} else {
				System.out.println("INCORRECT COMMAND");
			}
		}

	}

}

//This class represents the game player
class Player {
	// Stores the direction in which the player is currently facing
	private String currentDirection;

	// Stores the current position of the player
	private Location playerLocation;

	Player(String direction, Location playerLocation) {
		this.currentDirection = direction;
		this.playerLocation = playerLocation;
	}

	// This method makes the player turn left
	public void actionTurnLeft() {
		if (currentDirection.equals("NORTH")) {
			currentDirection = new String("WEST");
		} else if (currentDirection.equals("WEST")) {
			currentDirection = new String("SOUTH");
		} else if (currentDirection.equals("SOUTH")) {
			currentDirection = new String("EAST");
		} else if (currentDirection.equals("EAST")) {
			currentDirection = new String("NORTH");
		}
	}

	// This method makes the player turn right
	public void actionTurnRight() {
		if (currentDirection.equals("NORTH")) {
			currentDirection = new String("EAST");
		} else if (currentDirection.equals("EAST")) {
			currentDirection = new String("SOUTH");
		} else if (currentDirection.equals("SOUTH")) {
			currentDirection = new String("WEST");
		} else if (currentDirection.equals("WEST")) {
			currentDirection = new String("NORTH");
		}
	}

	// This method prints the status of the player
	public void status() {
		System.out.println("Avator is at (" + playerLocation.getxCoordinate()
				+ ", " + playerLocation.getyCoordinate() + ") facing "
				+ currentDirection);
	}

	public Location getLocation() {
		return this.playerLocation;
	}

	public void setLocation(Location location) {
		this.playerLocation = location;
	}

	public String getDirection() {
		return this.currentDirection;
	}
}

//This class represents the game board
class Board {
	// Stores the size of the board
	private int dimension;

	/*
	 * Stores the unique identifier of position for each player being added to
	 * this board. This will be useful when dealing with multiple players. A
	 * HashSet is used in order to leverage its property of giving constant time
	 * performance for add, remove and contains operations, thereby not
	 * degrading performance with increase in no. of players.
	 */
	private HashSet<Integer> playerLocation;

	Board(int dimension) {
		this.dimension = dimension;
		this.playerLocation = new HashSet<Integer>();
	}

	/*
	 * This method will add a player to a location on the board. It also checks
	 * for an existing player at that location (although this check is not being
	 * used for this scenario and will be valid for multiple players)
	 */
	public void addPlayer(Location location) {
		Integer uniquePostionIdentifier = getPostionIdentifier(
				location.getxCoordinate(), location.getyCoordinate());
		if (!playerLocation.contains(uniquePostionIdentifier)) {
			playerLocation.add(uniquePostionIdentifier);
		} else {
			/*
			 * Cannot add this player at this location on board as another
			 * player already exists here. This scenario will be valid when
			 * dealing with multiple players.
			 */
		}
	}

	/*
	 * This method will move the player forward depending on the current
	 * direction of the player. It will check for the boundary condition and
	 * move the player only if that move is valid. It also has a check to see if
	 * there is an existing player present at the position where the current
	 * player is trying to move (although this check is not being used for this
	 * scenario and will be valid for multiple players).
	 */
	public void actionForward(Player player) {
		int oldX = player.getLocation().getxCoordinate();
		int oldY = player.getLocation().getyCoordinate();
		String direction = player.getDirection();
		int newX = oldX;
		int newY = oldY;
		boolean boundaryCheck = false;
		boolean playerCheck = false;

		// Check for boundary condition

		if ((oldX == 0 && direction.equals("NORTH"))
				|| (oldY == 0 && direction.equals("WEST"))
				|| (oldX == dimension - 1 && direction.equals("SOUTH"))
				|| (oldY == dimension - 1 && direction.equals("EAST"))) {
			boundaryCheck = true;
		}

		/* 
		 * Check if a player already exists, this scenario will be valid when
		 * dealing with multiple players
		 */

		if (playerLocation.contains(new Integer(
				getPostionIdentifier(oldX, oldY)))) {
			playerCheck = true;
		}

		/*
		 *  Add player check here for multiple players and move the player only
		 *  if no player exists at that position.
		 */

		if (!boundaryCheck) {
			if (direction.equals("NORTH")) {
				newX = oldX - 1;
			} else if (direction.equals("WEST")) {
				newY = oldY - 1;
			} else if (direction.equals("SOUTH")) {
				newX = oldX + 1;
			} else if (direction.equals("EAST")) {
				newY = oldY + 1;
			}

			/* 
			 * Update the unique identifier in the HashSet based on the new
			 * location
			 */
			updatePostionIdentifier(oldX, oldY, newX, newY);
		}

		// Update the position of the player
		player.setLocation(new Location(newX, newY));
	}

	// This method returns a unique identifier based on the (x,y) position
	private int getPostionIdentifier(int xCoordinate, int yCoordinate) {
		return ((xCoordinate * dimension) + (yCoordinate + 1));
	}

	/*
	 * This method updates the position of a player in playerLocation HashSet
	 * Since in a HashSet the add and remove operation give constant time
	 * performance this operation will not cause an increase in time complexity
	 * if multiple players are added in future.
	 */
	private void updatePostionIdentifier(int oldX, int oldY, int newX, int newY) {
		Integer oldPosition = getPostionIdentifier(oldX, oldY);
		Integer newPosition = getPostionIdentifier(newX, newY);
		playerLocation.remove(oldPosition);
		playerLocation.add(newPosition);
	}
}

// Class to store the (x,y) position of a player on board
class Location {
	private int xCoordinate;
	private int yCoordinate;

	public int getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	Location(int xCoordinate, int yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}

}
