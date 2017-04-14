package game;

import java.util.ArrayList;
import java.util.Scanner;

import java.io.IOException;

import intergroup.Events.Event;
import intergroup.Events.Event.Error;
import intergroup.Messages.Message;
import intergroup.board.Board;

/**
 * Stores information about roads
 * and contains methods to let a player build roads
 */
public class Road {

	private Coordinate coordinateA;
	private Coordinate coordinateB;
	private Player owner;
	
	private static final int NO_ROADS = 15;
	
	private static final String BRICK = "brick";
	private static final String LUMBER = "lumber";
	
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
	public static void buildRoad(Player player, Game game1, Scanner scanner, boolean roadBuilding) throws IOException {

		//gets the resources needed to build a road
		ArrayList<ResourceCard> resources = hasRoadResources(player, roadBuilding);		
			
		//asks the player for coordinates for the road
		Road road = getRoadCoordinates(player, game1, scanner, roadBuilding);
		int roadsLeft = NO_ROADS - player.getNoRoads();
		
		//checks that the player can buy and place the road at the specified coordinates
		if (resources.size() != 2) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("You do not have enough resources to build a road").build()).build()).build(), player.getpSocket().getClientSocket());
			return;
		}
		else if (roadsLeft <= 0) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("You do not have any roads left to place").build()).build()).build(), player.getpSocket().getClientSocket());
			return;
		}
		else if (road == null) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("Invalid coordinates. Please request again").build()).build()).build(), player.getpSocket().getClientSocket());
			buildRoad(player, game1, scanner, roadBuilding);
			return;
		}
		else if (road.getOwner().getName() != null) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("A road has already been placed here. Please request again").build()).build()).build(), player.getpSocket().getClientSocket());
			buildRoad(player, game1, scanner, roadBuilding);
			return;
		}
 		else if (!checkConnected(road,player,game1)) {
	 		
 			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("Road must be placed beside other roads or settlements. Please request again").build()).build()).build(), player.getpSocket().getClientSocket());
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
			
			int playerNum = 0;
			
			for (int i = 0; i < game1.getPlayers().size(); i++) {
				if (game1.getPlayers().get(i).equals(player)) {
					playerNum = i;
				}
			}

			Message m = Message.newBuilder().setEvent(Event.newBuilder().setInstigator(Board.Player.newBuilder().setIdValue(playerNum).build()).setRoadBuilt(Board.Edge.newBuilder().setA(Board.Point.newBuilder().setX(road.getCoordinateA().getX()).setY(road.getCoordinateA().getY()).build()).setB(Board.Point.newBuilder().setX(road.getCoordinateB().getX()).setY(road.getCoordinateB().getY()).build()).build()).build()).build();
			Catan.printToClient(m, player);
			
			ArrayList<Player> players = game1.getPlayers();
			
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i) != player) {
						Catan.printToClient(m, players.get(i));
				}
			}
			
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
			
				if (brick == null && card.getResource().equals(BRICK)) {
					brick = card;
				}
				if (lumber == null && card.getResource().equals(LUMBER)) {
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
	public static Road getRoadCoordinates(Player player, Game game1, Scanner scanner, boolean roadBuilding) throws IOException {
		
		Catan.printToClient("Please send the server a build road request", player); //TODO asking for request
		
		Message enter = null;
		boolean success = false;
		
		while (!success) {
			
			enter = Catan.getPBMsg(player.getpSocket().getClientSocket());
				
			if (enter.getRequest().getBodyCase().getNumber() == 3) {
				success = true;
			}
			else {
				Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not a road build request").build()).build()).build(), player.getpSocket().getClientSocket());
			}
		}
		
		int x1 = enter.getRequest().getBuildRoad().getA().getX();
		int y1 = enter.getRequest().getBuildRoad().getA().getY();
		int x2 = enter.getRequest().getBuildRoad().getB().getX();
		int y2 = enter.getRequest().getBuildRoad().getB().getY();
				
		//checks the coordinates are in the correct range
		if (!((2*y1 <= x1+8) && (2*y1 >= x1-8) && (y1 <= 2*x1+8) && (y1 >= 2*x1-8) && (y1 >= -x1-8) && (y1 <= -x1+8))) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("Invalid coordinates. Please request again").build()).build()).build(), player.getpSocket().getClientSocket());
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
		game.Board board1 = game1.getBoard();
		
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
				
				Catan.printToClient("You now have the longest road!", player);
				
				for (int i = 0; i < players.size(); i++) {
					if (players.get(i) != player) {
						Catan.printToClient("Player " + player.getName() + " now has the longest road!", players.get(i));
					}
				}
			}
		}
	}
	//--------------------------------TURN CONSISTENT PROTOBUFF VERSIONS-----------------------------

	public static void buildRoad(Player player, Game game1, Message enter,
			boolean roadBuilding) {
		// TODO Auto-generated method stub
		
	}
}