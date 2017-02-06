import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to store methods associated with resource allocation
 */
public class ResourceAllocation {

	private static final int SETTLEMENT_RESOURCES = 1;
	private static final int CITY_RESOURCES = 2;
	
	//-----Methods to perform the resource allocation for the turn-----//
	
	//code to get the resources from a dice roll
	public static void resourceAllocation(int hexValue, Game game1, Scanner scanner) {
			
		ArrayList<Player> players = game1.getPlayers();
		ArrayList<Hex> hexes = game1.getBoard().getHexes();
		Board board = game1.getBoard();
			
		resetNewResources(players);
		
		for (int i = 0; i < hexes.size(); i++) {
				
			Hex hex = hexes.get(i);
				
			//if the hex value is the same as the dice roll and the robber
			//is not there, resources can be given out
			if (hex.getNumber() == hexValue) {
				if (!(hex.getisRobberHere().equals("R"))) {
						
					//checks each intersection around the hex for 
					//settlements and cities and gives the owners 
					//resources
						
					int x = hex.getCoordinate().getX();
					int y = hex.getCoordinate().getY();
					
					Coordinate nearbyIntersection;
					
					nearbyIntersection = new Coordinate(x, y-1);
					if(!getIntersectionResources(board, nearbyIntersection, hex, game1)) {
						return;
					}

					nearbyIntersection = new Coordinate(x, y+1);
					if(!getIntersectionResources(board, nearbyIntersection, hex, game1)) {
						return;
					}
						
					nearbyIntersection = new Coordinate(x-1, y);
					if(!getIntersectionResources(board, nearbyIntersection, hex, game1)) {
						return;
					}
						
					nearbyIntersection = new Coordinate(x+1, y);
					if(!getIntersectionResources(board, nearbyIntersection, hex, game1)) {
						return;
					}
						
					nearbyIntersection = new Coordinate(x-1, y-1);
					if(!getIntersectionResources(board, nearbyIntersection, hex, game1)) {
						return;
					}
						
					nearbyIntersection = new Coordinate(x+1, y+1);
					if(!getIntersectionResources(board, nearbyIntersection, hex, game1)) {
						return;
					}
				}
			}
		}
			
		setResources(players);
		game1.setPlayers(players);
	}
		
	//resets the 'newResourceCards' field in each player object
	public static void resetNewResources(ArrayList<Player> players) {
		
		for (int i = 0; i < players.size(); i++) {
			
			ArrayList<ResourceCard> newResourceCards = new ArrayList<ResourceCard>();
			players.get(0).setNewResourceCards(newResourceCards);
		}
	}
	
	//gets the resources for the settlement/city
	//returns false if the bank runs out of resource cards
	public static boolean getIntersectionResources(Board board, Coordinate nearbyIntersection, Hex hex, Game game1) {
		
		String type = ((Intersection) board.getLocationFromCoordinate(nearbyIntersection).getContains()).getBuilding().getType();
		Player owner = ((Intersection) board.getLocationFromCoordinate(nearbyIntersection).getContains()).getOwner();
		String terrain = hex.getTerrain();
		
		//if any player owns an intersection around the hex they get resources
		//1 resource for a settlement and 1 for a settlement
		if (type.equals("t")) {
			if (!getResources(owner, terrain, game1, SETTLEMENT_RESOURCES)) {
				System.out.println("There are not enough resources left. No player gets new resources this turn.");
				return false;
			}
		}
		else if (type.equals("c")) {
			if (!getResources(owner, terrain, game1, CITY_RESOURCES)) {
				System.out.println("There are not enough resources left. No player gets new resources this turn.");
				return false;
			}
		}
		
		return true;
	}
	
	//gives the resources to a player
	//returns false if the bank runs out of cards
	public static boolean getResources(Player player, String terrain, Game game1, int n) {
		
		ArrayList<ResourceCard> newResourceCards = player.getNewResourceCards();
		
		for (int i = 0; i < n; i++) {
		
			ResourceCard card = null;
			switch(terrain) {
			case "P" : 
				ArrayList<ResourceCard> wool = game1.getWool();
				
				if (wool.size() <= 0) {
					return false;
				}
					
				card = wool.get(0);
				wool.remove(0);
				game1.setWool(wool);
				break;
			case "F" :
				ArrayList<ResourceCard> lumber = game1.getLumber();
				
				if (lumber.size() <= 0) {
					return false;
				}
				
				card = lumber.get(0);
				lumber.remove(0);
				game1.setLumber(lumber);
				break;
			case "M" :
				ArrayList<ResourceCard> ore = game1.getOre();
				
				if (ore.size() <= 0) {
					return false;
				}
				
				card = ore.get(0);
				ore.remove(0);
				game1.setOre(ore);
				break;
			case "H" :
				ArrayList<ResourceCard> brick = game1.getBrick();
				
				if (brick.size() <= 0) {
					return false;
				}
				
				card = brick.get(0);
				brick.remove(0);
				game1.setBrick(brick);
				break;
			case "G" :
				ArrayList<ResourceCard> grain = game1.getGrain();
				
				if (grain.size() <= 0) {
					return false;
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
		return true;
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
				System.out.println("Player " + player.getName() + " gets: ");
			
				for (int j = 0; j < newResourceCards.size(); j++) {
							
					cards.add(newResourceCards.get(j));
					System.out.print("1x " + newResourceCards.get(j).getResource());
				}
					
				System.out.println();
				player.setResourceCards(cards);
			}
		}
	}
}