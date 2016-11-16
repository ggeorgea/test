import java.util.ArrayList;
import java.util.Scanner;

public class Catan {
	public static void main(String[] args) {
		
		System.out.println("----------SETTLERS OF CATAN----------\n\n");
		
		Scanner scanner = new Scanner(System.in);
		
		//sets board
		Board board1 = Setup.getMeABoard();
		Game game1 = new Game();
		game1.setBoard(board1);
		
		Map.printMap(game1.getBoard());

		//sets development cards
		ArrayList<DevelopmentCard> developmentCards = Setup.getDevCardDeck();
		game1.setDevelopmentCards(developmentCards);
		
		//sets resource cards
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
		
		//sets players
		ArrayList<Player> players = Setup.setPlayers(scanner);
		game1.setPlayers(players);
				
		//roll dice for each player
		//changes player order with largest dice roll first
		Setup.getPlayerOrder(game1, scanner);
		
		//place roads and settlements
		Setup.setInitialRoadsAndSettlements(game1, scanner);
		
		/*
		//GENERAL
		//the above methods set up a board, with roads, intersections and hexes, all set up in the right places, with random terrains and probabilities
		//the ways these items on the board should be interacted with are as follows:
		//(there is a 2d array that can be interacted with directly, but the x and y coordinates are plus five each, so i suggest we stick to these to maintain some modularity)
		//(and the road hashmap called roadmap needs to be accessed with the coordinates in a specific order, the method I suggest here just adjusts input so it is in the correct order)
		
		//ROADS
		//firstly, for roads, use the getRoadFromCo method
		//this methods takes two coordinates and  returns a road or null that links those coordinates, null if there is no road
		//that road can be modified by modifying the object returned of course
		//(if you wish to replace the road with another, using the setRoadFromCo method, you should iterate through the ArrayList and replace the old road there too!)
		//for example:
		Player player1 = new Player();
		player1.setName("!");
		Road road1 = board1.getRoadFromCo(new Coordinate(1,0),new Coordinate(1,1));
		road1.setOwner(player1);
		//as you can now see, the road on the right of the central hex, now is owned by the player whose name is "!"
		
		//INTERSECTIONS
		//for intersections, use the getLocationFromCoordinate method, that takes a single coordinate
		//this is a little more tricky as the location class that this returns actually has three things in it, a type, that should say "Intersection", if your coordinates are correct
		//and the thing that you want which is the contains field, so cast the contains field of the returned object to an intersection and modify that
		//for example
		Intersection inter1 = (Intersection) board1.getLocationFromCoordinate(new Coordinate(1,0)).getContains();
		inter1.setOwner(player1);
		//as you can see, the intersection in the bottom right of the central hex is now owned by player1
		
		//HEXES
		//to get the hex at a point on the board you do pretty much the same thing as an intersection
		//the type of location should be "hex", if there is nothing in that coordinate, it should be "empty" by the way
		//for example
		Hex hex1 = (Hex) board1.getLocationFromCoordinate(new Coordinate(0,0)).getContains();
		hex1.setisRobberHere("R");
		//it should now appear that there is also a robber on the central hex!
		//of course if I wasnt being lazy there would be seperate methods for hexes and intersections that did the casting for you or something, but im not sure thats even better
		*/
		
		Map.printMap(game1.getBoard());
	}
}