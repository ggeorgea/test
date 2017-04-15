package game;

import java.util.ArrayList;
import java.util.Scanner;

import java.io.IOException;

import intergroup.Events.Event;
import intergroup.Events.Event.Error;
import intergroup.Messages.Message;
import intergroup.board.Board;

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
	public static void buildSettlement(Player player, Game game1, Scanner scanner, Message enter) throws IOException {
				
		//checks if the player has the resources
		ArrayList<ResourceCard> resources = hasSettlementResources(player);

		//asks the player for coordinates to place the settlement
		Intersection settlement = getSettlementCoordinates(player, game1, scanner, enter);
		int settlementsLeft = NO_SETTLEMENTS - player.getNoSettlements();
			
		//checks that the player can buy and place a settlement at the specified coordinates
		if (resources.size() != 4) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("You do not have enough resources to build a settlement").build()).build()).build(), player.getpSocket().getClientSocket());	
			return;
		}
		else if (settlementsLeft <= 0) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("You do not have enough settlements left to place").build()).build()).build(), player.getpSocket().getClientSocket());
			return;
		}		
		if (!checkIllegalCoordinates(settlement)) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("Settlement must be places more than two roads away. Please request again").build()).build()).build(), player.getpSocket().getClientSocket());
			return;
		}		
		if (!checkSuitableCoordinates(player, game1, settlement)) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("Settlement must be placed beside a road. Please request again").build()).build()).build(), player.getpSocket().getClientSocket());
		 	return;
		}
		else if (settlement.getOwner().getName() != null) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("A settlement has already been placed here. Please request again").build()).build()).build(), player.getpSocket().getClientSocket());
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
			
			Trade.checkIfPortSettled(player, settlement, game1);
			
			int playerNum = 0;
			
			for (int i = 0; i < game1.getPlayers().size(); i++) {
				if (game1.getPlayers().get(i).equals(player)) {
					playerNum = i;
				}
			}
			
			Message m = Message.newBuilder().setEvent(Event.newBuilder().setInstigator(Board.Player.newBuilder().setIdValue(playerNum).build()).setSettlementBuilt(Board.Point.newBuilder().setX(settlement.getCoordinate().getX()).setY(settlement.getCoordinate().getY()).build()).build()).build();
			Catan.printToClient(m, player);
			
			ArrayList<Player> players = game1.getPlayers();
			
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i) != player) {
					Catan.printToClient(m, players.get(i));
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
	public static Intersection getSettlementCoordinates(Player player, Game game1, Scanner scanner, Message enter) throws IOException {
			
		int x = enter.getRequest().getBuildSettlement().getX();
		int y = enter.getRequest().getBuildSettlement().getY();
		Coordinate a = new Coordinate(x, y);
		
		//checks the coordinates are in the correct range
		if (!((2*y <= x+8) && (2*y >= x-8) && (y <= 2*x+8) && (y >= 2*x-8) && (y >= -x-8) && (y <= -x+8))
				||(!game1.getBoard().getLocationFromCoordinate(a).getType().equals(INTERSECTION))) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("Invalid coordinates. Please request again.").build()).build()).build(), player.getpSocket().getClientSocket());
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
	public static void buildCity(Player player, Game game1, Scanner scanner, Message enter) throws IOException {
		
		//checks the player has the correct resources to build the city
		ArrayList<ResourceCard> resources = hasCityResources(player);
		
		//asks the player for coordinates to place the city
		Intersection city = getCityCoordinates(player, game1, scanner, enter);
		int citiesLeft = NO_CITIES - player.getNoCities();
		
		//checks the player has enough resources and the coordinates are valid
		if (resources.size() != 5) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("You do not have enough resources to build a city").build()).build()).build(), player.getpSocket().getClientSocket());
			return;
		}
		else if (citiesLeft <= 0) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("You do not have enough cities left to place").build()).build()).build(), player.getpSocket().getClientSocket());
			return;
		}
		else if (player.getNoSettlements() <= 0) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("You do not have any settlements to upgrade").build()).build()).build(), player.getpSocket().getClientSocket());
			return;
		}
		else if (!(city.getOwner().getName().equals(player))) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("You can only upgrade a settlement you own. Please request again.").build()).build()).build(), player.getpSocket().getClientSocket());
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
		
			int playerNum = 0;
			
			for (int i = 0; i < game1.getPlayers().size(); i++) {
				if (game1.getPlayers().get(i).equals(player)) {
					playerNum = i;
				}
			}
			
			Message m = Message.newBuilder().setEvent(Event.newBuilder().setInstigator(Board.Player.newBuilder().setIdValue(playerNum).build()).setCityBuilt(Board.Point.newBuilder().setX(city.getCoordinate().getX()).setY(city.getCoordinate().getY()).build()).build()).build();

			Catan.printToClient(m, player);
			
			ArrayList<Player> players = game1.getPlayers();
			
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i) != player) {
					Catan.printToClient(m, players.get(i));
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
	public static Intersection getCityCoordinates(Player player, Game game1, Scanner scanner, Message enter) throws IOException {
		
		int x = enter.getRequest().getBuildCity().getX();
		int y = enter.getRequest().getBuildCity().getY();		
		Coordinate a = new Coordinate(x, y);
		
		//checks the coordinates are in the correct range
		if (!((2*y <= x+8) && (2*y >= x-8) && (y <= 2*x+8) && (y >= 2*x-8) && (y >= -x-8) && (y <= -x+8))
 				||(!game1.getBoard().getLocationFromCoordinate(a).getType().equals(INTERSECTION))) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("Invalid coordinates. Please request again.").build()).build()).build(), player.getpSocket().getClientSocket());
			return null;
		}
		
		//finds the intersection at the coordinates and returns it
		Intersection city = (Intersection) game1.getBoard().getLocationFromCoordinate(a).getContains();
		
		return city;
	}
}