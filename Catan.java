import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class that contains the main method for the game
 */
public class Catan {

	private static final boolean END_GAME = true;

	public static void main(String[] args) throws Exception {
		
		Board board1 = new Board();
		
		/*
		 * //GENERAL //the above methods set up a board, with roads,
		 * intersections and hexes, all set up in the right places, with random
		 * terrains and probabilities //the ways these items on the board should
		 * be interacted with are as follows: //(there is a 2d array that can be
		 * interacted with directly, but the x and y coordinates are plus five
		 * each, so i suggest we stick to these to maintain some modularity)
		 * //(and the road hashmap called roadmap needs to be accessed with the
		 * coordinates in a specific order, the method I suggest here just
		 * adjusts input so it is in the correct order)
		 * 
		 * //ROADS //firstly, for roads, use the getRoadFromCo method //this
		 * methods takes two coordinates and returns a road or null that links
		 * those coordinates, null if there is no road //that road can be
		 * modified by modifying the object returned of course //(if you wish to
		 * replace the road with another, using the setRoadFromCo method, you
		 * should iterate through the ArrayList and replace the old road there
		 * too!) //for example: Player player1 = new Player();
		 * player1.setName("!"); Road road1 = board1.getRoadFromCo(new
		 * Coordinate(1,0),new Coordinate(1,1)); road1.setOwner(player1); //as
		 * you can now see, the road on the right of the central hex, now is
		 * owned by the player whose name is "!"
		 * 
		 * //INTERSECTIONS //for intersections, use the
		 * getLocationFromCoordinate method, that takes a single coordinate
		 * //this is a little more tricky as the location class that this
		 * returns actually has three things in it, a type, that should say
		 * "Intersection", if your coordinates are correct //and the thing that
		 * you want which is the contains field, so cast the contains field of
		 * the returned object to an intersection and modify that //for example
		 * Intersection inter1 = (Intersection)
		 * board1.getLocationFromCoordinate(new Coordinate(1,0)).getContains();
		 * inter1.setOwner(player1); //as you can see, the intersection in the
		 * bottom right of the central hex is now owned by player1
		 * 
		 * //HEXES //to get the hex at a point on the board you do pretty much
		 * the same thing as an intersection //the type of location should be
		 * "hex", if there is nothing in that coordinate, it should be "empty"
		 * by the way //for example Hex hex1 = (Hex)
		 * board1.getLocationFromCoordinate(new Coordinate(0,0)).getContains();
		 * hex1.setisRobberHere("R"); //it should now appear that there is also
		 * a robber on the central hex! //of course if I wasnt being lazy there
		 * would be seperate methods for hexes and intersections that did the
		 * casting for you or something, but im not sure thats even better
		 */

		boolean keepPlaying = true;

		// will let the players play another game if they wish
		while (keepPlaying) {
			
			System.out.println("----------SETTLERS OF CATAN----------\n\n");
			Scanner scanner = new Scanner(new File("./src/test.txt"));

			//sets up board
			board1 = Setup.getMeABoard();
			Game game1 = new Game();
			game1.setBoard(board1);

			Map.printMap(game1.getBoard());

			//sets up development cards
			ArrayList<DevelopmentCard> developmentCards = Setup
					.getDevCardDeck();
			game1.setDevelopmentCards(developmentCards);

			//sets up resource cards
			ArrayList<ResourceCard> ore = Setup.getResourceCardDeck("ore");
			ArrayList<ResourceCard> grain = Setup.getResourceCardDeck("grain");
			ArrayList<ResourceCard> lumber = Setup.getResourceCardDeck("lumber");
			ArrayList<ResourceCard> wool = Setup.getResourceCardDeck("wool");
			ArrayList<ResourceCard> brick = Setup.getResourceCardDeck("brick");

			game1.setOre(ore);
			game1.setGrain(grain);
			game1.setLumber(lumber);
			game1.setWool(wool);
			game1.setBrick(brick);

			//sets up players
			ArrayList<Player> players = Setup.setPlayers(scanner);
			game1.setPlayers(players);

			//roll dice for each player
			//changes player order with largest dice roll first
			Setup.getPlayerOrder(game1, scanner);

			//place roads and settlements
			Setup.setInitialRoadsAndSettlements(game1, scanner);


			//pass from automated set up to actually playing the game
			System.out.println("-----now in manual mode-------");
			scanner = new Scanner(System.in);
			
			boolean hasEnded = !END_GAME;

			// will keep letting players take turns until someone wins
			while (!hasEnded) {
				for (int i = 0; i < game1.getPlayers().size(); i++) {

					//  for a test of longest road:
					/*
					if (i>0){
						System.out.println("-----now in manual mode-------");
						scanner = new Scanner(System.in);
					}
					*/
					
					// lets the player have a turn
					hasEnded = Turn.newTurn(game1.getPlayers().get(i), game1,
							scanner);

					// if a player has won then no other player takes their turn
					if (hasEnded) {
						break;
					}
				}
			}

			keepPlaying = playAgain(scanner);
		}

		System.out.println("Goodbye!");
	}

	//asks the players if they want to play again
	public static boolean playAgain(Scanner scanner) {

		System.out.println("Do you want to play again?");
		System.out.println("Y or N");

		boolean keepPlaying = false;
		String choice = scanner.next().toUpperCase();
		char c = choice.toCharArray()[0];

		switch (c) {
		case 'Y':
			keepPlaying = true;
			break;
		case 'N':
			keepPlaying = false;
			break;
		default:
			System.out.println("Invalid choice. Please choose again");
			keepPlaying = playAgain(scanner);
		}

		return keepPlaying;
	}

	//TODO delete these methods from this class
	
/* code modified/copied in build road method in turn class
	// ************ Look for the player with the longest road ********
	public static Player longestRoad(Game game1) {
		
		ArrayList<Player> players = game1.getPlayers();
		// initial longest road to be compared with the players longest road
		int longestRoad = 0;

		Player hasRoad = players.get(0);

		// iterate the list of player and find the longest road
		for (Player p : players) {
			if (p.getLongestRoad() > longestRoad) {
				longestRoad = p.getLongestRoad();
				hasRoad = p;
			}
		}
		
		hasRoad.setHasLongestRoad(true); 
		hasRoad.setVictoryPoints(hasRoad.getVictoryPoints()+2); 
		System.out.println("player" + hasRoad.getName() + "has the longest road.");
		return hasRoad;
	}*/
	
	//Eventually we will need to write a method that keeps track of victory points
	//we need a method to remove the largest army card when a player exceeds the current holder 

	//been transferred to turn class
	/*public static Player checkLargestArmy(Game game1){ 

		ArrayList<Player> players = game1.getPlayers();
		
		int armySize = 0 ; 
		Player largestArmy = players.get(0);

		//iterate the list of player and find the longest road 
		for (Player p : players){ 
			if(p.getLargestArmy() > armySize){ 
				armySize = p.getLargestArmy(); 
				largestArmy = p; 
			}
		}
		
		largestArmy.setHasLargestArmy(true); 
		largestArmy.setVictoryPoints(largestArmy.getVictoryPoints()+2);
		System.out.println("player" + largestArmy.getName() + "has the largest army.");
		return largestArmy; 
	}*/
}