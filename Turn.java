import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class contains all the methods to allow players to do things on their turn
 */
public class Turn {
	
	private static final boolean END_GAME = true;
	private static final int END_TURN = 4;

	//why is the robber initialized as 7? 
	//private static final int ROBBER = 7;

	//create an instance of robber and a coordinate where it begins 
	Coordinate c = new coordinate(); 
	Robber robber = newRobber(c, null);

	

	private static final int NO_ROADS = 15;
	private static final int NO_SETTLEMENTS = 5;
	private static final int NO_CITIES = 4;
	
	//allows the player to have their turn
	public static boolean newTurn(Player player, Game game1, Scanner scanner) {
		
		Map.printMap(game1.getBoard());
		
		Dice.rollDice(player, scanner);
		
		if (player.getCurrentRoll() != 7) {
			resourceAllocation(player.getCurrentRoll(), game1, scanner);
		}
		else {
			checkCardRemoval(game1, scanner);
			moveRobber(player, game1, scanner);
		}
		
		int choice = 0;
		boolean hasEnded = !END_GAME;
		
		while (choice != END_TURN && !hasEnded) {
			
			System.out.println("Player " + player.getName() + ": What do you want to do?");
			System.out.println("1: Build a road, settlement, city or development card?");
			System.out.println("2: Play a development card?");
			System.out.println("3: Trade with the bank, ports or other players?");
			System.out.println("4: End turn?");
			
			choice = scanner.nextInt();
			
			switch(choice) {
			case 1 :
				build(player, game1, scanner);
				break;
			case 2 :
				//play dev card
				break;
			case 3 : 
				//trade
				break;
			case 4 :
				break;
			default :
				System.out.println("Invalid choice. Please choose again");
			}
			
			hasEnded = checkEndOfGame(player);
		}
		
		return hasEnded;
	}

	//checks if the game has ended
	public static boolean checkEndOfGame(Player player) {
		
		if (player.getVictoryPoints() >= 10) {
			
			endGame(player);
			return END_GAME;
		}
		
		return !END_GAME;
	}
	
	//prints a statement ending the game
	public static void endGame(Player player) {
		
		System.out.println("Player " + player.getName() + " Wins!");
	}
	
//-----Methods to perform the resource allocation for the turn-----//
	
	//TODO what if the bank doesn't have resources?
	//code to get the resources from a dice roll
	public static void resourceAllocation(int hexValue, Game game1, Scanner scanner) {
		
		ArrayList<Player> players = game1.getPlayers();
		ArrayList<Hex> hexes = game1.getBoard().getHexes();
		
		//resets cards
		for (int i = 0; i < players.size(); i++) {
			
			ArrayList<ResourceCard> newResourceCards = new ArrayList<ResourceCard>();
			players.get(0).setNewResourceCards(newResourceCards);
		}
		
		for (int i = 0; i < hexes.size(); i++) {
			
			Hex hex = hexes.get(i);
			
			if (hex.getNumber() == hexValue) {
				if (!(hex.getisRbberHere().equals("R"))) {
					
					//checks the intersections around the hex
					//if a player owns one they get resources
					//1 for settlement, 2 for city
					
					/*if (player owns settlement) {
						String terrain = hex.getTerrain();
						if (!getResources(player, terrain, 1)) {
							System.out.println("There are not enough resources left. No player gets new resources this turn.");
							return;
						}
						
					}*/
					/*if (player owns city) {
						String terrain = hex.getTerrain();
						if (!getResources(player, terrain, 2)) {
							System.out.println("There are not enough resources left. No player gets new resources this turn.");
							return;
						}
					}*/
				}
			}
		}
		
		//sets resource cards
		for (int i = 0; i < players.size(); i++) {
			
			Player player = players.get(i);
			ArrayList<ResourceCard> newResourceCards = player.getNewResourceCards();
			ArrayList<ResourceCard> cards = player.getResourceCards();
			
			for (int j = 0; j < newResourceCards.size(); j++) {
				
				cards.add(newResourceCards.get(j));
			}
			
			player.setResourceCards(cards);
		}
	}
	
	//gives the resources to a player
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
	
//-----Methods to play the robber for the turn-----//
	
	//checks if any players have more than seven cards when the robber
	//is activated
	public static void checkCardRemoval(Game game1, Scanner scanner) {
		
		ArrayList<Player> players = game1.getPlayers();
		
		for (int i = 0; i < players.size(); i++) {
			
			Player player = players.get(i);
			ArrayList<ResourceCard> cards = player.getResourceCards();
			
			if (cards.size() > 7) {
					
				cardRemoval(player, cards, game1, scanner);
				player.setResourceCards(cards);
			}
		}
		
		game1.setPlayers(players);
	}
	
	//asks players with more than seven cards to choose the cards they remove
	public static void cardRemoval(Player player, ArrayList<ResourceCard> cards, Game game1, Scanner scanner) {
		
		int noCardsToRemove = cards.size()/2;
		
		System.out.println("Player " + player.getName() + ": Please select " + noCardsToRemove + " cards to remove");
		
		for (int i = 0; i < noCardsToRemove; i++) {
			
			System.out.println("Card " + i);
			
			for (int j = 0; j < cards.size(); j++) {
				
				System.out.println(j + ": " + cards.get(j).getResource());
			}
			
			int choice = scanner.nextInt();
			
			if (choice < 0 || choice >= cards.size()) {
				System.out.println("Invalid choice. Please choose again");
				cardRemoval(player, cards, game1, scanner);
			}
			
			ResourceCard card = cards.get(choice);
			
			switch (card.getResource()) {
			case "ore" :
				ArrayList<ResourceCard> ore = game1.getOre();
				ore.add(card);
				break;
			case "lumber" :
				ArrayList<ResourceCard> lumber = game1.getLumber();
				lumber.add(card);
				break;
			case "brick" :
				ArrayList<ResourceCard> brick = game1.getBrick();
				brick.add(card);
				break;
			case "wool" :
				ArrayList<ResourceCard> wool = game1.getWool();
				wool.add(card);
				break;
			case "grain" :
				ArrayList<ResourceCard> grain = game1.getGrain();
				grain.add(card);
				break;				
			}
			
			cards.remove(choice);
		}
	}
	
	//allows the player to move the robber and steal a card from a player
	public static void moveRobber(Player player, Game game1, Scanner scanner) {
		
		System.out.println("Player " + player.getName() + ": Please select where to place the robber");
		
		System.out.println("Select X coordinate");
		int x = scanner.nextInt();
		
		System.out.println("Select Y coordinate");
		int y = scanner.nextInt();
		
		//checks the coordinates are in the correct range
		if (x < -4 || x > 4 || y < -4 || y > 4) {
			
			System.out.println("Invalid coordinates. Please choose again");
			moveRobber(player, game1, scanner);
		}
		
		c = new Coordinate(x, y);

		//TODO gets the hex and puts the robber there
		//TODO lets the player steal a card
	}
	
//-----Methods to build and buy things for the turn-----//
	
	//asks the player if they want to build something
	public static void build(Player player, Game game1, Scanner scanner) {
		
		System.out.println("What do you want to build?");
		System.out.println("1. Road");
		System.out.println("2. Settlement");
		System.out.println("3. City");
		System.out.println("4. Development Card");
		
		int choice = scanner.nextInt();
		
		switch(choice) {
		case 1 :
			buildRoad(player, game1, scanner);
			break;
		case 2 :
			buildSettlement(player, game1, scanner);
			break;
		case 3 :
			buildCity(player, game1, scanner);
			break;
		case 4 :
			buyDevelopmentCard(player, game1, scanner);
			break;
		default :
			System.out.println("Invalid choice, Please choose again");
			build(player, game1, scanner);
		}
	}
	
	//lets the player build a road
	public static void buildRoad(Player player, Game game1, Scanner scanner) {

		ArrayList<ResourceCard> resources = hasRoadResources(player);
		int roadsLeft = NO_ROADS - player.getNoRoads();
		Road road = getRoadCoordinates(player, game1, scanner);
		
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
			buildRoad(player, game1, scanner);
			return;
		}
		else if (road.getOwner().getName() != null) {
				
			System.out.println("A road has already been placed here. Please choose again");
			buildRoad(player, game1, scanner);
			return;
		}
		/*else if (coordinate not next to players roads/settlements) {
			
			System.out.println("Road must be placed beside your other roads " +
				"and settlements. Please choose again");
			buildRoad(player, game1, scanner);
			return;
		 }*/
		else {
			ArrayList<ResourceCard> cards = player.getResourceCards();
			cards.remove(resources.get(0));
			cards.remove(resources.get(1));
			player.setResourceCards(cards);
			
			road.setOwner(player);
			player.setNoRoads(player.getNoRoads() - 1);
			
			System.out.println("Player " + player.getName() + " placed road at: (" + road.getCoordinateA().getX() 
					+ "," + road.getCoordinateA().getY() + "),(" + road.getCoordinateB().getX() + "," + road.getCoordinateB().getY() + ")");
			//TODO update road length
			//TODO check end of game?
		}
	}

	//checks if the player has the required resources to build a road
	public static ArrayList<ResourceCard> hasRoadResources(Player player) {
	
		ArrayList<ResourceCard> cards = player.getResourceCards();
		ArrayList<ResourceCard> resources = new ArrayList<ResourceCard>();
		
		ResourceCard brick = null;
		ResourceCard lumber = null;
		int i = 0;
		
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
	
		if (brick != null && lumber != null) {
			
			resources.add(brick);
			resources.add(lumber);
		}
		
		return resources;
	}
	
	//asks the player for the coordinates for the road they want to build
	public static Road getRoadCoordinates(Player player, Game game1, Scanner scanner) {
		
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
		if (x1 < -4 || x1 > 4 || y1 < -4 || y1 > 4 || x2 < -4 || x2 > 4 || y2 < -4 || y2 > 4) {
			
			System.out.println("Invalid coordinates. Please choose again");
			buildRoad(player, game1, scanner);
			return null;
		}
		else {
		
			Coordinate a = new Coordinate(x1, y1);
			Coordinate b = new Coordinate(x2, y2);
		
			Road road = game1.getBoard().getRoadFromCo(a, b);
			return road;
		}
	}
	
	//lets the player build a settlement
	public static void buildSettlement(Player player, Game game1, Scanner scanner) {
		
		ArrayList<ResourceCard> resources = hasSettlementResources(player);
		int settlementsLeft = NO_SETTLEMENTS - player.getNoSettlements();
		Intersection settlement = getSettlementCoordinates(player, game1, scanner);
		
		if (resources.size() != 4) {
			
			System.out.println("You do not have enough resources to build a settlement");
			return;
		}
		else if (settlementsLeft <= 0) {
			
			System.out.println("You do not have enough settlements left to place");
			return;
		}
		else if (settlement.getOwner().getName() != null) {
			
			System.out.println("A settlement has already been placed here. Please choose again");
			buildSettlement(player, game1, scanner);
			return;
		}
		/*else if (settlement is not next to any of that players roads) {
		 
		 	System.out.println("Settlement must be placed beside one of your roads. " +
		 		"Please choose again");
		 	buildSettlement(player, game1, scanner);
		 	return;
		 }*/
		/*else if (settlement is not two roads away from another settlement) {
		 	
		 	System.out.println("Settlement must be placed more than two roads away. " +
		 		"Please choose again");
		 	buildSettlement(player, game1, scanner);
		 	return;
		 }*/
		else {
			
			ArrayList<ResourceCard> cards = player.getResourceCards();
			
			for (int i = 0; i < 4; i++) {
				cards.remove(resources.get(i));
			}
			
			player.setResourceCards(cards);
			
			settlement.setOwner(player);
			settlement.setBuilding(new Building("t", 1));
			player.setNoSettlements(player.getNoSettlements() + 1);
			player.setVictoryPoints(player.getVictoryPoints() + 1);
			
			System.out.println("Player " + player.getName() + " placed settlement at: (" + settlement.getCoordinate().getX() 
					+ "," + settlement.getCoordinate().getY() + ")");
			//TODO check end of game?
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
		
		while (brick == null || lumber == null || wool == null || grain == null) {
			
			ResourceCard card = cards.get(i);
			
			if (brick == null && card.getResource().equals("brick")) {
				
				brick = card;
			}
			if (lumber == null && card.getResource().equals("lumber")) {
				
				lumber = card;
			}
			if (wool == null && card.getResource().equals("wool")) {
				
				wool = card;
			}
			if (grain == null && card.getResource().equals("grain")) {
				
				grain = card;
			}
			
			i++;
		}
		
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
		
		System.out.println("Please select where to place your settlement");
		
		System.out.println("Select X coordinate");
		int x = scanner.nextInt();
		
		System.out.println("Select Y coordinate");
		int y = scanner.nextInt();
		
		//checks the coordinates are in the correct range
		if (x < -4 || x > 4 || y < -4 || y > 4) {
			
			System.out.println("Invalid coordinates. Please choose again");
			buildSettlement(player, game1, scanner);
			return null;
		}
		
		Coordinate a = new Coordinate(x, y);
		
		Intersection settlement = (Intersection) game1.getBoard().getLocationFromCoordinate(a).getContains();
		
		return settlement;
	}
	
	public static void buildCity(Player player, Game game1, Scanner scanner) {
		
		ArrayList<ResourceCard> resources = hasCityResources(player);
		int citiesLeft = NO_CITIES - player.getNoCities();
		Intersection city = getCityCoordinates(player, game1, scanner);
		
		if (resources.size() != 5) {
			
			System.out.println("You do not have enough resources to build a city");
			return;
		}
		else if (citiesLeft <= 0) {
			
			System.out.println("You do not have enough cities left to place");
			return;
		}
		else if (player.getNoSettlements() <= 0) {
			
			System.out.println("You do not have any settlements to upgrade to a city");
			return;
		}
		else if (!(city.getOwner().getName().equals(player))) {
			
			System.out.println("You can only upgrade a settlement that you own");
			buildCity(player, game1, scanner);
			return;
		}
		else {
			
			ArrayList<ResourceCard> cards = player.getResourceCards();
			
			for (int i = 0; i < 5; i++) {
				cards.remove(resources.get(i));
			}
			
			player.setResourceCards(cards);
			
			city.setBuilding(new Building("c", 2));
			player.setNoCities(player.getNoCities() + 1);
			player.setNoSettlements(player.getNoSettlements() - 1);
			player.setVictoryPoints(player.getVictoryPoints() + 1);
		
			System.out.println("Player " + player.getName() + " placed city at: (" + city.getCoordinate().getX() 
					+ "," + city.getCoordinate().getY() + ")");
			//TODO check end of game?
		}
	}
	
	public static ArrayList<ResourceCard> hasCityResources(Player player) {
		
		ArrayList<ResourceCard> cards = player.getResourceCards();
		ArrayList<ResourceCard> resources = new ArrayList<ResourceCard>();
		
		ArrayList<ResourceCard> ore = new ArrayList<ResourceCard>();
		ArrayList<ResourceCard> grain = new ArrayList<ResourceCard>();
		
		for (int i = 0; i < cards.size(); i++) {
			
			ResourceCard card = cards.get(i);
			
			if (card.getResource().equals("ore")) {
				
				ore.add(card);
			}
			
			if (card.getResource().equals("grain")) {
				
				grain.add(card);
			}
		}
		
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
	
	public static Intersection getCityCoordinates(Player player, Game game1, Scanner scanner) {

		System.out.println("Please select the settlement you want to upgrade to a city");
		
		System.out.println("Select X coordinate");
		int x = scanner.nextInt();
		
		System.out.println("Select Y coordinate");
		int y = scanner.nextInt();
		
		//checks the coordinates are in the correct range
		if (x < -4 || x > 4 || y < -4 || y > 4) {
			
			System.out.println("Invalid coordinates. Please choose again");
			buildCity(player, game1, scanner);
			return null;
		}
		
		Coordinate a = new Coordinate(x, y);
		
		Intersection city = (Intersection) game1.getBoard().getLocationFromCoordinate(a).getContains();
		
		return city;
	}
	
	public static void buyDevelopmentCard(Player player, Game game1, Scanner scanner) {
		
		ArrayList<ResourceCard> resources = hasDevelopmentCardResources(player);
		ArrayList<DevelopmentCard> developmentCards = game1.getDevelopmentCards();
		
		if (resources.size() != 3) {
			
			System.out.println("You do not have enough resources to buy a development card");
			return;
		}
		else if (developmentCards.size() <= 0) {
			
			System.out.println("There are no development cards left in the deck");
			return;			
		}
		else {
			
			ArrayList<ResourceCard> cards = player.getResourceCards();
			
			for (int i = 0; i < 3; i++) {
				cards.remove(resources.get(i));
			}
			
			player.setResourceCards(cards);
			
			DevelopmentCard developmentCard = developmentCards.get(0);
			developmentCards.remove(developmentCard);
			game1.setDevelopmentCards(developmentCards);
			
			player.getDevelopmentCards().add(developmentCard);
			
			System.out.println("Player " + player.getName() + " bought a development card");
			//TODO check type of development card?
			//TODO check end of game
		}
	}
	
	public static ArrayList<ResourceCard> hasDevelopmentCardResources(Player player) {
		
		ArrayList<ResourceCard> cards = player.getResourceCards();
		ArrayList<ResourceCard> resources = new ArrayList<ResourceCard>();
		
		ResourceCard ore = null;
		ResourceCard wool = null;
		ResourceCard grain = null;
		int i = 0;
		
		while (ore == null || wool == null || grain == null) {
			
			ResourceCard card = cards.get(i);
			
			if (ore == null && card.getResource().equals("ore")) {
				
				ore = card;
			}
			if (wool == null && card.getResource().equals("wool")) {
				
				wool = card;
			}
			if (grain == null && card.getResource().equals("grain")) {
				
				grain = card;
			}
			
			i++;
		}
		
		if (ore != null && wool != null && grain != null) {
			
			resources.add(ore);
			resources.add(wool);
			resources.add(grain);
		}
		
		return resources;
	}
}