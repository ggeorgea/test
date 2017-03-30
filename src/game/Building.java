
package game;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to store information about types of building
 * and methods to allow players to build settlements
 * and cities
 */
public class Building {

	private String type;
	private int victoryPoints;
	
	private static final int NO_SETTLEMENTS = 5;
	private static final int NO_CITIES = 4;
	
	private static final String GRAIN = "grain";
	private static final String LUMBER = "lumber";
	private static final String ORE = "ore";
	private static final String WOOL = "wool";
	private static final String BRICK = "brick";
	
	private static final String INTERSECTION = "intersection";
	private static final String TOWN = "t";
	private static final String CITY = "c";
	
//-----Constructors-----//
	
	public Building() {
		
	}
	
	public Building(String type, int victoryPoints) {
		
		this.type = type;
		this.victoryPoints = victoryPoints;
	}
	
//-----Getters and Setters-----//
	
	public String getType() {
		
		return type;
	}
	
	public void setType(String type) {
		
		this.type = type;
	}
	
	public int getVictoryPoints() {
	
		return victoryPoints;
	}
	
	public void setVictoryPoints(int victoryPoints) {
	
		this.victoryPoints = victoryPoints;
	}
	
//-----Methods to build a settlement-----//
	
	//lets the player build a settlement
	public static void buildSettlement(Player player, Game game1, Scanner scanner) {
				
		//checks if the player has the resources
		ArrayList<ResourceCard> resources = hasSettlementResources(player);

		//asks the player for coordinates to place the settlement
		Intersection settlement = getSettlementCoordinates(player, game1, scanner);
		int settlementsLeft = NO_SETTLEMENTS - player.getNoSettlements();
			
		//checks that the player can buy and place a settlement at the specified coordinates
		if (resources.size() != 4) {
			
			Catan.printToClient("You do not have enough resources to build a settlement", player);	
			return;
		}
		else if (settlementsLeft <= 0) {
			
			Catan.printToClient("You do not have enough settlements left to place", player);
			return;
		}		
		if (!checkIllegalCoordinates(settlement)) {
			
			Catan.printToClient("Settlement must be placed more than two roads away. Please choose again", player);
			buildSettlement(player, game1, scanner);
			return;
		}		
		if (!checkSuitableCoordinates(player, game1, settlement)) {
			
			Catan.printToClient("Settlement must be placed beside one of your roads. Please choose again", player);
		 	buildSettlement(player, game1, scanner);
		 	return;
		}
		else if (settlement.getOwner().getName() != null) {
			
			Catan.printToClient("A settlement has already been placed here. Please choose again", player);
			buildSettlement(player, game1, scanner);
			return;
		}
		
		//if the settlement is valid, the resources are removed from the player's hand,
		//the settlement is placed on the board and victory points are updated
		else {
				
			ArrayList<ResourceCard> cards = player.getResourceCards();
			
			for (int i = 0; i < 4; i++) {
				cards.remove(resources.get(i));
			}
				
			player.setResourceCards(cards);
				
			settlement.setOwner(player);
			settlement.setBuilding(new Building(TOWN, 1));
			player.setNoSettlements(player.getNoSettlements() + 1);
			player.setVictoryPoints(player.getVictoryPoints() + 1);
			
			//TODO check commit to ensure what was lost
			Trade.checkIfPortSettled(player, settlement, game1);
			
			Catan.printToClient("You placed a settlement at: (" + settlement.getCoordinate().getX() 
					+ "," + settlement.getCoordinate().getY() + ")", player);
			
			ArrayList<Player> players = game1.getPlayers();
			
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i) != player) {
					
					PlayerSocket socket = players.get(i).getpSocket();
					
					if (socket != null) {
						socket.sendMessage("Player " + player.getName() + " placed settlement at: (" + settlement.getCoordinate().getX() 
							+ "," + settlement.getCoordinate().getY() + ")");
					}
				}
			}
		}
	}
		
	//checks if the player has enough resources to build the settlement
	public static ArrayList<ResourceCard> hasSettlementResources(Player player) {
			
		ArrayList<ResourceCard> cards = player.getResourceCards();
		ArrayList<ResourceCard> resources = new ArrayList<ResourceCard>();
		
		ResourceCard brick = null;
		ResourceCard lumber = null;
		ResourceCard wool = null;
		ResourceCard grain = null;
		int i = 0;
		
		//checks the player has the specified resources needed to build a settlement
		try {
			while (brick == null || lumber == null || wool == null || grain == null) {
			
				ResourceCard card = cards.get(i);
				
				if (brick == null && card.getResource().equals(BRICK)) {
					
					brick = card;
				}
				if (lumber == null && card.getResource().equals(LUMBER)) {
					
					lumber = card;
				}
				if (wool == null && card.getResource().equals(WOOL)) {
				
					wool = card;
				}
				if (grain == null && card.getResource().equals(GRAIN)) {
				
					grain = card;
				}
				
				i++;
			}
		}
		catch(IndexOutOfBoundsException e) {
			return new ArrayList<ResourceCard>();
		}
			
		//if the player has all the resources they are returned
		if (brick != null && lumber != null && wool != null && grain != null) {
				
			resources.add(brick);
			resources.add(lumber);
			resources.add(wool);
			resources.add(grain);
		}
			
		return resources;
	}
		
	//asks the player for the coordinates for the settlement they want to build
	public static Intersection getSettlementCoordinates(Player player, Game game1, Scanner scanner) {
			
		Catan.printToClient("Please select where to place your settlement", player);

		Catan.printToClient("Select X coordinate", player);
		int x = Integer.parseInt(Catan.getInputFromClient(player, scanner));
		
		Catan.printToClient("Select Y coordinate", player);
		int y = Integer.parseInt(Catan.getInputFromClient(player, scanner));
		Coordinate a = new Coordinate(x, y);
		
		//checks the coordinates are in the correct range
		if (!((2*y <= x+8) && (2*y >= x-8) && (y <= 2*x+8) && (y >= 2*x-8) && (y >= -x-8) && (y <= -x+8))
				||(!game1.getBoard().getLocationFromCoordinate(a).getType().equals(INTERSECTION))) {
			
			Catan.printToClient("Invalid coordinates. Please choose again", player);
			buildSettlement(player, game1, scanner);
			return null;
		}
			
		//if the coordinates are valid, the intersection is found and returned
		Intersection settlement = (Intersection) game1.getBoard().getLocationFromCoordinate(a).getContains();
		
		return settlement;
	}
		
	//checks the settlement is two intersections away from another settlement
	public static boolean checkIllegalCoordinates(Intersection settlement) {
		
		ArrayList<Intersection> illegal = settlement.getIllegal();
		
		for (int i = 0; i < illegal.size(); i++) {
			
			Intersection inter = illegal.get(i);
			
			if (inter.getOwner().getName() != null) {
				return false;
			}
		}
		
		return true;
	}
	
	//checks the settlement is next to the player's road
	public static boolean checkSuitableCoordinates(Player player, Game game1, Intersection settlement) {
		
		boolean foundSuitable = false;
		
		do {
			Road tryRoad;
			int j = settlement.getCoordinate().getX();
			int g = settlement.getCoordinate().getY();
			
			tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new Coordinate(j,g+1));
			if (tryRoad != null && tryRoad.getOwner().equals(player)) {
				foundSuitable = true;
				return foundSuitable;
			}
			
			tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new Coordinate(j,g-1));
			if (tryRoad != null && tryRoad.getOwner().equals(player)) {
				foundSuitable = true;
				return foundSuitable;
			}
			
			tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new Coordinate(j+1,g));
			if (tryRoad != null && tryRoad.getOwner().equals(player)) {
			foundSuitable = true;
			return foundSuitable;
			}
			
			tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new Coordinate(j-1,g));
			if (tryRoad != null && tryRoad.getOwner().equals(player)) {
				foundSuitable = true;
				return foundSuitable;
			}
			
			tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new Coordinate(j+1,g+1));
			if (tryRoad != null && tryRoad.getOwner().equals(player)) {
				foundSuitable = true;
				return foundSuitable;
			}
			
			tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new Coordinate(j-1,g-1));
			if (tryRoad != null && tryRoad.getOwner().equals(player)) {
				foundSuitable = true;
				return foundSuitable;
			}
		} while (false);
		
		return foundSuitable;		
	}
		
//-----Methods to build a city-----//
			
	//lets the player build a city
	public static void buildCity(Player player, Game game1, Scanner scanner) {
		
		//checks the player has the correct resources to build the city
		ArrayList<ResourceCard> resources = hasCityResources(player);
		
		//asks the player for coordinates to place the city
		Intersection city = getCityCoordinates(player, game1, scanner);
		int citiesLeft = NO_CITIES - player.getNoCities();
		
		//checks the player has enough resources and the coordinates are valid
		if (resources.size() != 5) {
			
			Catan.printToClient("You do not have enough resources to build a city", player);
			return;
		}
		else if (citiesLeft <= 0) {
			
			Catan.printToClient("You do not have enough cities left to place", player);
			return;
		}
		else if (player.getNoSettlements() <= 0) {
			
			Catan.printToClient("You do not have any settlements to upgrade to a city", player);
			return;
		}
		else if (!(city.getOwner().getName().equals(player))) {
			
			Catan.printToClient("You can only upgrade a settlement that you own", player);
			buildCity(player, game1, scanner);
			return;
		}
		
		//if the city can be placed, the resource cards are removed from the player's hand,
		//the settlement is upgraded and victory points are updated
		else {
			
			ArrayList<ResourceCard> cards = player.getResourceCards();
			
			for (int i = 0; i < 5; i++) {
				cards.remove(resources.get(i));
			}
			
			player.setResourceCards(cards);
			
			city.setBuilding(new Building(CITY, 2));
			player.setNoCities(player.getNoCities() + 1);
			player.setNoSettlements(player.getNoSettlements() - 1);
			player.setVictoryPoints(player.getVictoryPoints() + 1);
		
			Catan.printToClient("You placed a city at: (" + city.getCoordinate().getX() 
					+ "," + city.getCoordinate().getY() + ")", player);
			
			ArrayList<Player> players = game1.getPlayers();
			
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i) != player) {
					
					PlayerSocket socket = players.get(i).getpSocket();
					
					if (socket != null) {
						socket.sendMessage("Player " + player.getName() + " placed city at: (" + city.getCoordinate().getX() 
							+ "," + city.getCoordinate().getY() + ")");
					}
				}
			}
		}
	}
	
	//checks if the player has enough resources to build the city
	public static ArrayList<ResourceCard> hasCityResources(Player player) {
		
		ArrayList<ResourceCard> cards = player.getResourceCards();
		ArrayList<ResourceCard> resources = new ArrayList<ResourceCard>();
		
		ArrayList<ResourceCard> ore = new ArrayList<ResourceCard>();
		ArrayList<ResourceCard> grain = new ArrayList<ResourceCard>();
		
		//checks the player has the correct resources
		for (int i = 0; i < cards.size(); i++) {
			
			ResourceCard card = cards.get(i);
			
			if (card.getResource().equals(ORE)) {
					
				ore.add(card);
			}
			
			if (card.getResource().equals(GRAIN)) {
				
				grain.add(card);
			}
		}
		
		//if the player has enough of each resource, they are returned
		if (ore.size() >= 3 && grain.size() >= 2) {
			
			for (int i = 0; i < 3; i++) {
				resources.add(ore.get(i));
			}
			for (int i = 0; i < 2; i++) {
				resources.add(grain.get(i));
			}
		}
		
		return resources;
	}
	
	//asks the player for the coordinates for the settlement they want to build
	public static Intersection getCityCoordinates(Player player, Game game1, Scanner scanner) {
		Catan.printToClient("Please select the settlement you want to upgrade to a city", player);

		Catan.printToClient("Select X coordinate", player);
		int x = Integer.parseInt(Catan.getInputFromClient(player, scanner));
		
		Catan.printToClient("Select Y coordinate", player);
		int y = Integer.parseInt(Catan.getInputFromClient(player, scanner));
		Coordinate a = new Coordinate(x, y);
		
		//checks the coordinates are in the correct range
		if (!((2*y <= x+8) && (2*y >= x-8) && (y <= 2*x+8) && (y >= 2*x-8) && (y >= -x-8) && (y <= -x+8))
 				||(!game1.getBoard().getLocationFromCoordinate(a).getType().equals(INTERSECTION))) {
			
			Catan.printToClient("Invalid coordinates. Please choose again", player);
			buildCity(player, game1, scanner);
			return null;
		}
		
		//finds the intersection at the coordinates and returns it
		Intersection city = (Intersection) game1.getBoard().getLocationFromCoordinate(a).getContains();
		
		return city;
	}
}