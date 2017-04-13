package completeCatan;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to store methods associated with resource allocation
 */
public class ResourceAllocation {

	private static final int SETTLEMENT_RESOURCES = 1;
	private static final int CITY_RESOURCES = 2;
	
	public static final String ROBBER = "R";
	public static final String TOWN = "t";
	public static final String CITY = "c";
	
	public static final String PASTURE = "P";
	public static final String FOREST = "F";
	public static final String MOUNTAIN = "M";
	public static final String GRASS = "G";
	public static final String HILL = "H";
	
	public static final String WOOL = "wool";
	public static final String LUMBER = "lumber";
	public static final String ORE = "ore";
	public static final String BRICK = "brick";
	public static final String GRAIN = "grain";
	
	//-----Methods to perform the resource allocation for the turn-----//
	
	//code to get the resources from a dice roll
	public static void resourceAllocation(int hexValue, Game game1, Scanner scanner) {
			
		ArrayList<Player> players = game1.getPlayers();
		ArrayList<Hex> hexes = game1.getBoard().getHexes();
		Board board = game1.getBoard();
		ArrayList<String> bankHasResources = new ArrayList<String>();
			
		resetNewResources(players);
		
		for (int i = 0; i < hexes.size(); i++) {
				
			Hex hex = hexes.get(i);
				
			//if the hex value is the same as the dice roll and the robber
			//is not there, resources can be given out
			if (hex.getNumber() == hexValue) {
				if (!(hex.getisRobberHere().equals(ROBBER))) {
						
					//checks each intersection around the hex for 
					//settlements and cities and gives the owners 
					//resources
						
					int x = hex.getCoordinate().getX();
					int y = hex.getCoordinate().getY();
					
					Coordinate nearbyIntersection;
					
					nearbyIntersection = new Coordinate(x, y-1);
					bankHasResources.add(getIntersectionResources(board, nearbyIntersection, hex, game1));

					nearbyIntersection = new Coordinate(x, y+1);
					bankHasResources.add(getIntersectionResources(board, nearbyIntersection, hex, game1));
						
					nearbyIntersection = new Coordinate(x-1, y);
					bankHasResources.add(getIntersectionResources(board, nearbyIntersection, hex, game1));
						
					nearbyIntersection = new Coordinate(x+1, y);
					bankHasResources.add(getIntersectionResources(board, nearbyIntersection, hex, game1));
						
					nearbyIntersection = new Coordinate(x-1, y-1);
					bankHasResources.add(getIntersectionResources(board, nearbyIntersection, hex, game1));
						
					nearbyIntersection = new Coordinate(x+1, y+1);
					bankHasResources.add(getIntersectionResources(board, nearbyIntersection, hex, game1));
				}
			}
		}
			
		//if only one player requires a resource that the bank does not have enough
		//of, that player gets all the resource that is left. Otherwise, no player
		//gets the resource
		for (int i = 0; i < bankHasResources.size(); i++) {
		
			int playersWithResources = 0;
		
			for (int j = 0; j < players.size(); j++) {
			
				if (players.get(j).getNewResourceCards().contains(bankHasResources.get(i))) {
					playersWithResources++;
				}				
			}
			
			if (playersWithResources <= 1) {
				bankHasResources.remove(i);
			}
		}
		
		removeIllegalResources(players, bankHasResources);
		setResources(players);
		game1.setPlayers(players);
	}
		
	//resets the 'newResourceCards' field in each player object
	public static void resetNewResources(ArrayList<Player> players) {
		
		for (int i = 0; i < players.size(); i++) {
			
			ArrayList<ResourceCard> newResourceCards = new ArrayList<ResourceCard>();
			players.get(i).setNewResourceCards(newResourceCards);
		}
	}
	
	//gets the resources for the settlement/city
	//returns the resource if the bank runs out of that resource card
	//null otherwise
	public static String getIntersectionResources(Board board, Coordinate nearbyIntersection, Hex hex, Game game1) {
		
		String type = ((Intersection) board.getLocationFromCoordinate(nearbyIntersection).getContains()).getBuilding().getType();
		Player owner = ((Intersection) board.getLocationFromCoordinate(nearbyIntersection).getContains()).getOwner();
		String terrain = hex.getTerrain();
		
		//if any player owns an intersection around the hex they get resources
		//1 resource for a settlement and 1 for a settlement
		if (type.equals(TOWN)) {
			
			String bankHasResources = getResources(owner, terrain, game1, SETTLEMENT_RESOURCES);
			
			if (bankHasResources != null) {
				
				ArrayList<Player> players = game1.getPlayers();
				
				for (int i = 0; i < players.size(); i++) {
					Catan.printToClient("There are no " + bankHasResources + " cards left. No player gets this resource this turn.", players.get(i));
				}
				
				return bankHasResources;
			}
		}
		else if (type.equals(CITY)) {
			
			String bankHasResources = getResources(owner, terrain, game1, CITY_RESOURCES);
			
			if (bankHasResources != null) {
				
				ArrayList<Player> players = game1.getPlayers();
				
				for (int i = 0; i < players.size(); i++) {
					Catan.printToClient("There are no " + bankHasResources + " cards left. No player gets this resource this turn.", players.get(i));
				}
				
				return bankHasResources;
			}
		}
		
		return null;
	}
	
	//gives the resources to a player
	//returns the resource if the bank runs out of that resource card
	//null otherwise
	public static String getResources(Player player, String terrain, Game game1, int n) {
		
		ArrayList<ResourceCard> newResourceCards = player.getNewResourceCards();
		
		for (int i = 0; i < n; i++) {
		
			ResourceCard card = null;
			switch(terrain) {
			case PASTURE : 
				ArrayList<ResourceCard> wool = game1.getWool();
				
				if (wool.size() <= 0) {
					return WOOL;
				}
					
				card = wool.get(0);
				wool.remove(0);
				game1.setWool(wool);
				break;
			case FOREST :
				ArrayList<ResourceCard> lumber = game1.getLumber();
				
				if (lumber.size() <= 0) {
					return LUMBER;
				}
				
				card = lumber.get(0);
				lumber.remove(0);
				game1.setLumber(lumber);
				break;
			case MOUNTAIN :
				ArrayList<ResourceCard> ore = game1.getOre();
				
				if (ore.size() <= 0) {
					return ORE;
				}
				
				card = ore.get(0);
				ore.remove(0);
				game1.setOre(ore);
				break;
			case HILL :
				ArrayList<ResourceCard> brick = game1.getBrick();
				
				if (brick.size() <= 0) {
					return BRICK;
				}
				
				card = brick.get(0);
				brick.remove(0);
				game1.setBrick(brick);
				break;
			case GRASS :
				ArrayList<ResourceCard> grain = game1.getGrain();
				
				if (grain.size() <= 0) {
					return GRAIN;
				}
				
				card = grain.get(0);
				grain.remove(0);
				game1.setGrain(grain);
				break;
			}

			if (card != null) {
				newResourceCards.add(card);
			}
		}
			
		player.setNewResourceCards(newResourceCards);
		return null;
	}
	
	//removes resource cards from the players 'newResourceCards' of the type of resource that the
	//bank does not have
	public static void removeIllegalResources(ArrayList<Player> players, ArrayList<String> illegalResources) {
		
		for (int i = 0; i < players.size(); i++) {
			
			Player player = players.get(i);
			ArrayList<ResourceCard> newResourceCards = player.getNewResourceCards();
			
			for (int j = 0; j < newResourceCards.size(); j++) {
				
				String card = newResourceCards.get(j).getResource();
				
				for (int k = 0; k < illegalResources.size(); k++) {
					
					String illegalResource = illegalResources.get(k);
					
					if (card.equals(illegalResource)) {
						
						newResourceCards.remove(j);
					}
				}
			}	
		}
	}
		
	//adds the cards in 'newResourceCards' to 'resourceCards' for each player
	public static void setResources(ArrayList<Player> players) {
			
		for (int i = 0; i < players.size(); i++) {
						
			Player player = players.get(i);
			ArrayList<ResourceCard> newResourceCards = player.getNewResourceCards();
			ArrayList<ResourceCard> cards = player.getResourceCards();
					
			//if the player gets resources, they are added to their hand and
			//the allocation printed
			if (newResourceCards.size() > 0) {
				Catan.printToClient("You get:", player);
			
				for (int j = 0; j < newResourceCards.size(); j++) {
							
					cards.add(newResourceCards.get(j));
					Catan.printToClient("1x " + newResourceCards.get(j).getResource(), player);
				}
					
				player.setResourceCards(cards);
			}
		}
	}
}