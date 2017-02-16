package game;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Stores information about roads
 * and contains methods to let a player build roads
 */
public class Road {

	private Coordinate coordinateA;
	private Coordinate coordinateB;
	private Player owner;
	
	private static final int NO_ROADS = 15;
	
//-----Constructors-----//
	
	public Road() {
		
	}
	
	public Road(Coordinate coordinateA, Coordinate coordinateB, Player owner) {
		
		this.coordinateA = coordinateA;
		this.coordinateB = coordinateB;
		this.owner = owner;
	}
	
//-----Getters and Setters-----//
	
	public Coordinate getCoordinateA() {
	
		return coordinateA;
	}
	
	public void setCoordinateA(Coordinate coordinateA) {
	
		this.coordinateA = coordinateA;
	}
	
	public Coordinate getCoordinateB() {
	
		return coordinateB;
	}
	
	public void setCoordinateB(Coordinate coordinateB) {
	
		this.coordinateB = coordinateB;
	}
	
	public Player getOwner() {
	
		return owner;
	}
	
	public void setOwner(Player owner) {
	
		this.owner = owner;
	}
	
//-----Methods to build a road-----//
	
	//lets the player build a road
	public static void buildRoad(Player player, Game game1, Scanner scanner, boolean roadBuilding) {

		//gets the resources needed to build a road
		ArrayList<ResourceCard> resources = hasRoadResources(player, roadBuilding);		
			
		//asks the player for coordinates for the road
		Road road = getRoadCoordinates(player, game1, scanner, roadBuilding);
		int roadsLeft = NO_ROADS - player.getNoRoads();
		
		//checks that the player can buy and place the road at the specified coordinates
		if (resources.size() != 2) {
				
			System.out.println("You do not have enough resources to build a road");
			return;
		}
		else if (roadsLeft <= 0) {
				
			System.out.println("You do not have any roads left to place");
			return;
		}
		else if (road == null) {
				
			System.out.println("Invalid coordinates. Please choose again");
			buildRoad(player, game1, scanner, roadBuilding);
			return;
		}
		else if (road.getOwner().getName() != null) {
					
			System.out.println("A road has already been placed here. Please choose again");
			buildRoad(player, game1, scanner, roadBuilding);
			return;
		}
 		else if (!checkConnected(road,player,game1)) {
	 			
 			System.out.println("Road must be placed beside your other roads " +
 				"and settlements. Please choose again");
			buildRoad(player, game1, scanner, roadBuilding);
 			return;
 		 }
			
		//if the road is valid, the resource cards are removed from the player's hand
		//and the road is placed on the board
		else {
	
			ArrayList<ResourceCard> cards = player.getResourceCards();
			
			if (!roadBuilding) {
				
				cards.remove(resources.get(0));
				cards.remove(resources.get(1));
				player.setResourceCards(cards);
			}
			
			road.setOwner(player);
			player.setNoRoads(player.getNoRoads() - 1);
						
			System.out.println("Player " + player.getName() + " placed road at: (" + road.getCoordinateA().getX() 
					+ "," + road.getCoordinateA().getY() + "),(" + road.getCoordinateB().getX() + "," + road.getCoordinateB().getY() + ")");
			
			//checks if the player has the longest road
			checkLongestRoad(player, game1, road);
		}
	}

	//checks if the player has the required resources to build a road
	public static ArrayList<ResourceCard> hasRoadResources(Player player, boolean roadBuilding) {
	
		ArrayList<ResourceCard> cards = player.getResourceCards();
		ArrayList<ResourceCard> resources = new ArrayList<ResourceCard>();
		
		ResourceCard brick = null;
		ResourceCard lumber = null;
		int i = 0;
		
		//loops through the players resource cards to find the cards needed to buy a road
		try {
			while (brick == null || lumber == null) {
			
				ResourceCard card = cards.get(i);
			
				if (brick == null && card.getResource().equals("brick")) {
				
					brick = card;
				}
				if (lumber == null && card.getResource().equals("lumber")) {
					
					lumber = card;
				}
					
				i++;
			}
		}
		catch(IndexOutOfBoundsException e) {
			return new ArrayList<ResourceCard>();
		}
			
		//if the player has the resources, the road can be built
		if ((brick != null && lumber != null) || roadBuilding) {
			
			resources.add(brick);
			resources.add(lumber);
		}
			
		return resources;
	}
		
	//asks the player for the coordinates for the road they want to build
	public static Road getRoadCoordinates(Player player, Game game1, Scanner scanner, boolean roadBuilding) {
		
		System.out.println("Please select where to place your road");
		
		System.out.println("Coordinate 1: ");
		System.out.println("Select X coordinate");
		int x1 = scanner.nextInt();
		
		System.out.println("Select Y coordinate");
		int y1 = scanner.nextInt();
			
		System.out.println("Coordinate 2: ");
		System.out.println("Select X coordinate");
		int x2 = scanner.nextInt();
		
		System.out.println("Select Y coordinate");
		int y2 = scanner.nextInt();
		
		//checks the coordinates are in the correct range
		if (!((2*y1 <= x1+8) && (2*y1 >= x1-8) && (y1 <= 2*x1+8) && (y1 >= 2*x1-8) && (y1 >= -x1-8) && (y1 <= -x1+8))) {
				
			System.out.println("Invalid coordinates. Please choose again");
			getRoadCoordinates(player, game1, scanner, roadBuilding);
			return null;
		}
		else {
		
			Coordinate a = new Coordinate(x1, y1);
			Coordinate b = new Coordinate(x2, y2);
		
			//finds the road at the specified coordinates and returns it
			Road road = game1.getBoard().getRoadFromCo(a, b);
			return road;
		}
	}
		
	//checks if a road is connected to your other roads
	public static boolean checkConnected(Road road, Player player, Game game1){
	
		//game1.getBoard().getLocationFromCoordinate(road.getCoordinateA());
		Board board1 = game1.getBoard();
		
		//note: coordinateA is always the higher up coordinate on the printed representation
		int x1 = road.getCoordinateA().getX();
		int y1 = road.getCoordinateA().getY();
		int x2 = road.getCoordinateB().getX();
		int y2 = road.getCoordinateB().getY();
		
		Road road1;
		Road road2;
		Road road3;
		Road road4;
		
		//vertical
		if (road.getCoordinateA().getX() == road.getCoordinateB().getX()) {
			 road1 = board1.getRoadFromCo(road.getCoordinateA(), new Coordinate(x1-1,y1));
			 road2 = board1.getRoadFromCo(road.getCoordinateA(), new Coordinate(x1+1,y1+1));
			 road3 = board1.getRoadFromCo(road.getCoordinateB(), new Coordinate(x2+1,y2));
			 road4 = board1.getRoadFromCo(road.getCoordinateB(), new Coordinate(x2-1,y2-1));
			}
		//negslope
		else if (road.getCoordinateA().getY() == road.getCoordinateB().getY()) {
			 road1 = board1.getRoadFromCo(road.getCoordinateA(), new Coordinate(x1,y1+1));
			 road2 = board1.getRoadFromCo(road.getCoordinateA(), new Coordinate(x1-1,y1-1));
			 road3 = board1.getRoadFromCo(road.getCoordinateB(), new Coordinate(x2+1,y2+1));
			 road4 = board1.getRoadFromCo(road.getCoordinateB(), new Coordinate(x2,y2-1));
		}
		//poslope
		else {
			 road1 = board1.getRoadFromCo(road.getCoordinateA(), new Coordinate(x1,y1+1));
			 road2 = board1.getRoadFromCo(road.getCoordinateA(), new Coordinate(x1+1,y1));
			 road3 = board1.getRoadFromCo(road.getCoordinateB(), new Coordinate(x2-1,y2));
			 road4 = board1.getRoadFromCo(road.getCoordinateB(), new Coordinate(x2,y2-1));
		}
		if (
				((road1!=null)&&(road1.getOwner().equals(player)))
				||((road2!=null)&&(road2.getOwner().equals(player)))
				||((road3!=null)&&(road3.getOwner().equals(player)))
				||((road4!=null)&&(road4.getOwner().equals(player)))
				) {
			return true;
		}
		else {
			return false;
		}
	}
		
	//updates the player with the longest road
	public static void checkLongestRoad(Player player, Game game1, Road road) {
		
		int oldlong = player.getLongestRoad();
		LongestRoad.CheckPlayerLongestRoad(player, game1, road);
		int newLong = player.getLongestRoad();
		
		//has to have improved and be longer than 4 to matter
		if (newLong > oldlong && newLong > 4) {
			
			boolean longer = false;
			ArrayList<Player> players = game1.getPlayers();
			
			//checks to see if any other players have a longer or equal road already
			for (Player p : players) {
				if (p.getLongestRoad() >= newLong && !p.equals(player)) {
					
					longer = true;
				}
				//if p doesnt have the longest road, but thinks they do
				else if (!p.equals(player) && p.hasLongestRoad()) {
					
					p.setHasLongestRoad(false);
					p.setVictoryPoints(p.getVictoryPoints()-2);
				}
			}
			if(!longer && !player.hasLongestRoad()) {
				
				player.setHasLongestRoad(true); 
				player.setVictoryPoints(player.getVictoryPoints()+2); 
				System.out.println("player " + player.getName() + " now has the longest road!");
			}
		}
	}
}