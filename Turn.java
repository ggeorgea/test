import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Class contains all the methods to allow players to do things on their turn
 */
public class Turn {
	
	private static final boolean END_GAME = true;
	private static final int END_TURN = 8;

	//why is the robber initialized as 7? 
	//private static final int ROBBER = 7;

	//create an instance of robber and a coordinate where it begins 
	Coordinate c = new Coordinate(); 
	Robber robber = new Robber(c, null, null);

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
			try{
			System.out.println("Player " + player.getName() + ": What do you want to do?");
			System.out.println("1: Build a road, settlement, city or development card?");
			System.out.println("2: Play a development card?");
			System.out.println("3: Trade with the bank, ports or other players?");
			System.out.println("4: Show your hand?");
			System.out.println("5: Print map?");
			System.out.println("6: Length of your Longest Road?");
			System.out.println("7: Count your victory pionts");
			System.out.println("8: End turn?");
			
			choice = scanner.nextInt();
			int ch = 0;
			switch(choice) {
			case 1 :
				build(player, game1, scanner);
				break;
			case 2 :
				playDevelopmentCard(player, game1, scanner);
				break;
			case 3 : 
				trade(player, scanner);
				break;
			case 4 :
				System.out.print("(");
				Iterator<ResourceCard> it = player.getResourceCards().iterator();
				while(it.hasNext()){
					System.out.print(it.next().getResource());
					if(it.hasNext()){
						System.out.print(", ");
					}
				}
				System.out.print(")\n");
				break;
			case 5 :
				Map.printMap(game1.getBoard());
				break;
			case 6 :
				System.out.println("Your Longest Road Length is: "+player.getLongestRoad());
				break;
			case 7:
				System.out.println("You have "+player.getVictoryPoints()+" victory points");
				break;
			case 8:
				break;
			default :
				System.out.println("Invalid choice. Please choose again");
			}
			
			hasEnded = checkEndOfGame(player);
			}
			catch(InputMismatchException e){
				System.out.println("Invalid choice. Please choose again");
				scanner.nextLine();
			}
		}
		
		updateDevelopmentCards(player);
		
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
	
	//updates development cards so ones bought this turn can be played next turn
	public static void updateDevelopmentCards(Player player) {
		
		ArrayList<DevelopmentCard> developmentCards = player.getDevelopmentCards();
		
		for (int i = 0; i < developmentCards.size(); i++) {
		
			developmentCards.get(i).setHidden(false);
		}
	}
	
//-----Methods to perform the resource allocation for the turn-----//
	
	//code to get the resources from a dice roll
	public static void resourceAllocation(int hexValue, Game game1, Scanner scanner) {
		
		ArrayList<Player> players = game1.getPlayers();
		ArrayList<Hex> hexes = game1.getBoard().getHexes();
		Board board = game1.getBoard();
		
		resetNewResources(players);
		
		for (int i = 0; i < hexes.size(); i++) {
			
			Hex hex = hexes.get(i);
			
			if (hex.getNumber() == hexValue) {
				if (!(hex.getisRbberHere().equals("R"))) {
					
					//checks the intersections around the hex
					//if a player owns one they get resources
					//1 for settlement, 2 for city
					
					//TODO checks intersections for players
					
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
	public static boolean getIntersectionResources(Board board, Coordinate nearbyIntersection, Hex hex, Game game1) {
		
		String type = board.getLocationFromCoordinate(nearbyIntersection).getType();
		Player owner = ((Intersection) board.getLocationFromCoordinate(nearbyIntersection).getContains()).getOwner();
		
		if (type.equals("settlement")) {
			String terrain = hex.getTerrain();
			if (!getResources(owner, terrain, game1, 1)) {
				System.out.println("There are not enough resources left. No player gets new resources this turn.");
				return false;
			}
		}
		else if (type.equals("city")) {
			String terrain = hex.getTerrain();
			if (!getResources(owner, terrain, game1, 2)) {
				System.out.println("There are not enough resources left. No player gets new resources this turn.");
				return false;
			}
		}
		
		return true;
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
	
	//adds the cards in 'newResourceCards' to 'resourceCards' for each player
	public static void setResources(ArrayList<Player> players) {
		
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
			try{
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
			catch(InputMismatchException e){
				System.out.println("Invalid choice. Please choose again");
				scanner.nextLine();
				cardRemoval(player,cards,game1,scanner);
			}
		}
	}
	
	//allows the player to move the robber and steal a card from a player
	public static void moveRobber(Player player, Game game1, Scanner scanner) {
		try{
		System.out.println("Player " + player.getName() + ": Please select where to place the robber");

		System.out.println("Select X coordinate");
		int x = scanner.nextInt();

		System.out.println("Select Y coordinate");
		int y = scanner.nextInt();
		Coordinate a = new Coordinate(x, y);
		//checks the coordinates are in the correct range
		if((!((2*y <= x +8)&&(2*y>=x-8)&&(y<=2*x+8)&&(y>=2*x-8)&&(y>=-x-8)&&(y<=-x+8)))
				||
				(!game1.getBoard().getLocationFromCoordinate(a).getType().equals("hex"))){

			System.out.println("Invalid coordinates. Please choose again");
			moveRobber(player, game1, scanner);
			return;
		}



		Hex hex1 = (Hex) game1.getBoard().getLocationFromCoordinate(a).getContains();
		hex1.setisRobberHere("R");
		game1.getBoard().setRobber(a);
		//TODO gets the hex and puts the robber there
		//TODO lets the player steal a card
		}
		catch(InputMismatchException e){
			scanner.nextLine();
			 moveRobber(player,game1,scanner);
		}
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
			buildRoad(player, game1, scanner, false);
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
	
//----Methods to build a road----//
	

	//checks if a road is connected to your other roads
	public static boolean checkConnected(Road road, Player player, Game game1){
	//	game1.getBoard().getLocationFromCoordinate(road.getCoordinateA())
		//vertical
		Board board1 = game1.getBoard();
		//note: coordinateA is always the higher up coordinate on the printed represenation
		int x1 = road.getCoordinateA().getX();
		int y1 = road.getCoordinateA().getY();
		int x2 = road.getCoordinateB().getX();
		int y2 = road.getCoordinateB().getY();
		Road road1;
		Road road2;
		Road road3;
		Road road4;
		if(road.getCoordinateA().getX()==road.getCoordinateB().getX()){
			 road1= board1.getRoadFromCo(road.getCoordinateA(), new Coordinate(x1-1,y1));
			 road2= board1.getRoadFromCo(road.getCoordinateA(), new Coordinate(x1+1,y1+1));
			 road3= board1.getRoadFromCo(road.getCoordinateB(), new Coordinate(x2+1,y2));
			 road4= board1.getRoadFromCo(road.getCoordinateB(), new Coordinate(x2-1,y2-1));

		}
		//negslope
		else if(road.getCoordinateA().getY()==road.getCoordinateB().getY()){
			 road1 = board1.getRoadFromCo(road.getCoordinateA(), new Coordinate(x1,y1+1));
			 road2 = board1.getRoadFromCo(road.getCoordinateA(), new Coordinate(x1-1,y1-1));
			 road3 = board1.getRoadFromCo(road.getCoordinateB(), new Coordinate(x2+1,y2+1));
			 road4 = board1.getRoadFromCo(road.getCoordinateB(), new Coordinate(x2,y2-1));
		}
		//poslope
		else{
			 road1 = board1.getRoadFromCo(road.getCoordinateA(), new Coordinate(x1,y1+1));
			 road2 = board1.getRoadFromCo(road.getCoordinateA(), new Coordinate(x1+1,y1));
			 road3 = board1.getRoadFromCo(road.getCoordinateB(), new Coordinate(x2-1,y2));
			 road4 = board1.getRoadFromCo(road.getCoordinateB(), new Coordinate(x2,y2-1));
		}
		if(
			      ((road1!=null)&&(road1.getOwner().equals(player)))
				||((road2!=null)&&(road2.getOwner().equals(player)))
				||((road3!=null)&&(road3.getOwner().equals(player)))
				||((road4!=null)&&(road4.getOwner().equals(player)))
				){
		return true;}
		else{return false;}
	}
	
	
	//lets the player build a road
	public static void buildRoad(Player player, Game game1, Scanner scanner, boolean roadBuilding) {

		//COMMENTS FOR LONGEST ROAD TEST
		///*
		//gets the resources needed to build a road
		ArrayList<ResourceCard> resources = hasRoadResources(player, roadBuilding);
		//*/
		
		
		//asks the player for coordinates for the road
		Road road = getRoadCoordinates(player, game1, scanner, roadBuilding);
		int roadsLeft = NO_ROADS - player.getNoRoads();
		//COMMENTS FOR LONGEST ROAD TEST
		///*
		//checks that the player can buy and place the road at the specified coordinates
		if (resources.size() != 2) {
			
			System.out.println("You do not have enough resources to build a road");
			return;
		}
		
		else
			//*/
			if (roadsLeft <= 0) {
			
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
			//COMMENTS FOR LONGEST ROAD TEST
			///*
			ArrayList<ResourceCard> cards = player.getResourceCards();
			
			if (!roadBuilding) {
			
				cards.remove(resources.get(0));
				cards.remove(resources.get(1));
				player.setResourceCards(cards);
			}
			//*/
			road.setOwner(player);
			player.setNoRoads(player.getNoRoads() - 1);
						
			System.out.println("Player " + player.getName() + " placed road at: (" + road.getCoordinateA().getX() 
					+ "," + road.getCoordinateA().getY() + "),(" + road.getCoordinateB().getX() + "," + road.getCoordinateB().getY() + ")");
			
			
			
			
			//checks for longest road
			int oldlong = player.getLongestRoad();
			LongestRoad.CheckPlayerLongestRoad(player, game1, road);
			int newLong = player.getLongestRoad();
			//has to have improved and be longer than 4 to matter
			if(newLong>oldlong && newLong>4){
				boolean longer = false;
				ArrayList<Player> players = game1.getPlayers();
				//checks to see if any other players have a longer or equal road already
				for (Player p : players) {
					if (p.getLongestRoad() >= newLong && !p.equals(player)) {
						longer = true;
					}
					//if p doesnt have the longest road, but thinks they do
					else if(!p.equals(player)&&p.hasLongestRoad()){
						p.setHasLongestRoad(false);
						p.setVictoryPoints(p.getVictoryPoints()-2);
					}
				}
				if(!longer&&!player.hasLongestRoad()){
					player.setHasLongestRoad(true); 
					player.setVictoryPoints(player.getVictoryPoints()+2); 
					System.out.println("player " + player.getName() + " now has the longest road!");
				}
			}
			
			
			//TODO check longest road
			//TODO update road length
		}
	}

	//checks if the player has the required resources to build a road
	public static ArrayList<ResourceCard> hasRoadResources(Player player, boolean roadBuilding) {
	
		ArrayList<ResourceCard> cards = player.getResourceCards();
		ArrayList<ResourceCard> resources = new ArrayList<ResourceCard>();
		
		ResourceCard brick = null;
		ResourceCard lumber = null;
		int i = 0;
		try{
		//loops through the players resource cards to find the cards needed to buy a road
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
		catch(IndexOutOfBoundsException e)
		{
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
		if(!((2*y1 <= x1 +8)&&(2*y1>=x1-8)&&(y1<=2*x1+8)&&(y1>=2*x1-8)&&(y1>=-x1-8)&&(y1<=-x1+8))) {
			
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
	
//----Methods to build a settlement----//
	
	//lets the player build a settlement
	public static void buildSettlement(Player player, Game game1, Scanner scanner) {
		
		//checks if the player has the resources
		ArrayList<ResourceCard> resources = hasSettlementResources(player);

		//asks the player for coordinates to place the settlement
		Intersection settlement = getSettlementCoordinates(player, game1, scanner);
		int settlementsLeft = NO_SETTLEMENTS - player.getNoSettlements();
		
		//checks that the player can buy and place a settlement at the specified coordinates
		if (resources.size() != 4) {
			
			System.out.println("You do not have enough resources to build a settlement");
			return;
		}
		else if (settlementsLeft <= 0) {
			
			System.out.println("You do not have enough settlements left to place");
			return;
		}
		
		
		ArrayList<Intersection> illegal = settlement.getIllegal();
		for (int i = 0; i < illegal.size(); i++) {
			Intersection inter = illegal.get(i);
			if (inter.getOwner().getName() != null) {
				System.out.println("Settlement must be placed more than two roads away. " +
				 		"Please choose again");
				buildSettlement(player, game1, scanner);
				return;
			}
		}


		boolean foundSuitible = false;
		do{
			Road tryRoad;
			int j = settlement.getCoordinate().getX();
			int g = settlement.getCoordinate().getY();
			tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new Coordinate(j,g+1));
			if(tryRoad!=null&&tryRoad.getOwner().equals(player)){
				foundSuitible = true;
				break;
			}
			tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new Coordinate(j,g-1));
			if(tryRoad!=null&&tryRoad.getOwner().equals(player)){
				foundSuitible = true;
				break;
			}
			tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new Coordinate(j+1,g));
			if(tryRoad!=null&&tryRoad.getOwner().equals(player)){
				foundSuitible = true;
				break;
			}
			tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new Coordinate(j-1,g));
			if(tryRoad!=null&&tryRoad.getOwner().equals(player)){
				foundSuitible = true;
				break;
			}
			tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new Coordinate(j+1,g+1));
			if(tryRoad!=null&&tryRoad.getOwner().equals(player)){
				foundSuitible = true;
				break;
			}
			tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new Coordinate(j-1,g-1));
			if(tryRoad!=null&&tryRoad.getOwner().equals(player)){
				foundSuitible = true;
				break;
			}
		}while(false);





		if (!foundSuitible) {

		 	System.out.println("Settlement must be placed beside one of your roads. " +
		 		"Please choose again");
		 	buildSettlement(player, game1, scanner);
		 	return;
		 }
		
		
		else if (settlement.getOwner().getName() != null) {
			
			System.out.println("A settlement has already been placed here. Please choose again");
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
			settlement.setBuilding(new Building("t", 1));
			player.setNoSettlements(player.getNoSettlements() + 1);
			player.setVictoryPoints(player.getVictoryPoints() + 1);
			
			System.out.println("Player " + player.getName() + " placed settlement at: (" + settlement.getCoordinate().getX() 
					+ "," + settlement.getCoordinate().getY() + ")");
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
		try{
		//checks the player has the specified resources needed to build a settlement
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
	}
	catch(IndexOutOfBoundsException e)
	{
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
		
		System.out.println("Please select where to place your settlement");
		
		System.out.println("Select X coordinate");
		int x = scanner.nextInt();
		
		System.out.println("Select Y coordinate");
		int y = scanner.nextInt();
		Coordinate a = new Coordinate(x, y);
		
		//checks the coordinates are in the correct range
		if(!((2*y <= x +8)&&(2*y>=x-8)&&(y<=2*x+8)&&(y>=2*x-8)&&(y>=-x-8)&&(y<=-x+8))
 				||(!game1.getBoard().getLocationFromCoordinate(a).getType().equals("Intersection"))) {
			
			System.out.println("Invalid coordinates. Please choose again");
			buildSettlement(player, game1, scanner);
			return null;
		}
		
		//if the coordinates are valid, the intersection is found and returned
		Intersection settlement = (Intersection) game1.getBoard().getLocationFromCoordinate(a).getContains();
		
		return settlement;
	}
	
//----Methods to build a city----//
	
	//lets the player build a city
	public static void buildCity(Player player, Game game1, Scanner scanner) {
		
		//checks the player has the corret resources to build the city
		ArrayList<ResourceCard> resources = hasCityResources(player);
		
		//asks the player for coordinates to place the city
		Intersection city = getCityCoordinates(player, game1, scanner);
		int citiesLeft = NO_CITIES - player.getNoCities();
		
		//checks the player has enough resources and the coordinates are valid
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
		
		//if the city can be placed, the resource cards are removed from the player's hand,
		//the settlement is upgraded and victory points are updated
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
			
			if (card.getResource().equals("ore")) {
				
				ore.add(card);
			}
			
			if (card.getResource().equals("grain")) {
				
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

		System.out.println("Please select the settlement you want to upgrade to a city");
		
		System.out.println("Select X coordinate");
		int x = scanner.nextInt();
		
		System.out.println("Select Y coordinate");
		int y = scanner.nextInt();
		Coordinate a = new Coordinate(x, y);
		
		//checks the coordinates are in the correct range
		if(!((2*y <= x +8)&&(2*y>=x-8)&&(y<=2*x+8)&&(y>=2*x-8)&&(y>=-x-8)&&(y<=-x+8))
 				||(!game1.getBoard().getLocationFromCoordinate(a).getType().equals("Intersection"))) {
			
			System.out.println("Invalid coordinates. Please choose again");
			buildCity(player, game1, scanner);
			return null;
		}
		
		//finds the intersection at the coordinates and returns it
		Intersection city = (Intersection) game1.getBoard().getLocationFromCoordinate(a).getContains();
		
		return city;
	}
	
//----Methods to buy a development card----//
	
	//lets the player buy a development card
	public static void buyDevelopmentCard(Player player, Game game1, Scanner scanner) {
		
		//checks the player has the correct resources to buy a development card
		ArrayList<ResourceCard> resources = hasDevelopmentCardResources(player);
		ArrayList<DevelopmentCard> developmentCards = game1.getDevelopmentCards();
		ArrayList<DevelopmentCard> playerDevCards = player.getDevelopmentCards();
		
		//checks if a development card can be bought
		if (resources.size() != 3) {
			
			System.out.println("You do not have enough resources to buy a development card");
			return;
		}
		else if (developmentCards.size() <= 0) {
			
			System.out.println("There are no development cards left in the deck");
			return;			
		}
		
		//if a development card can be bought, the resources are removed from
		//the player's hand and the development card is added
		else {
			
			ArrayList<ResourceCard> cards = player.getResourceCards();
			
			for (int i = 0; i < 3; i++) {
				cards.remove(resources.get(i));
			}
			
			player.setResourceCards(cards);
			
			DevelopmentCard developmentCard = developmentCards.get(0);
			developmentCards.remove(developmentCard);
			game1.setDevelopmentCards(developmentCards);
			
			playerDevCards.add(developmentCard);
			
			System.out.println("Player " + player.getName() + " bought a development card");
			
			String type = developmentCard.getType();
			
			//if the development card is a victory point card, it is 
			//played immediately
			if (type.equals("victory point")) {
				
				playVictoryPointCard(player);
				playerDevCards.remove(developmentCard);
			}
			
			player.setDevelopmentCards(playerDevCards);
		}
	}
	
	//checks if the player enough resources to build the settlement
	public static ArrayList<ResourceCard> hasDevelopmentCardResources(Player player) {
		
		ArrayList<ResourceCard> cards = player.getResourceCards();
		ArrayList<ResourceCard> resources = new ArrayList<ResourceCard>();
		
		ResourceCard ore = null;
		ResourceCard wool = null;
		ResourceCard grain = null;
		int i = 0;
		
		//checks the player has the correct number of resources
		try {
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
		}
		catch(IndexOutOfBoundsException e) {
 			return new ArrayList<ResourceCard>();
 		}
		
		//if the player has the resources then they are returned
		if (ore != null && wool != null && grain != null) {
			
			resources.add(ore);
			resources.add(wool);
			resources.add(grain);
		}
		
		return resources;
	}
	
//----Methods to play a development card----//
	
	//lets the player select a development card to play
 	public static void playDevelopmentCard(Player player, Game game1, Scanner scanner) {
 		
 		ArrayList<DevelopmentCard> cards = player.getDevelopmentCards();
 		boolean cardPlayed = true;
 		
 		System.out.println("What development card do you want to play?");
 		
 		for (int i = 0; i < cards.size(); i++) {
 			
 			System.out.println((i+1) + ": " + cards.get(i).getType());
 		}
 		
 		int choice = scanner.nextInt();
 		DevelopmentCard play = cards.get(choice);
 		
 		//if the card is hidden then it cannot be played on that turn
 		if (play.isHidden()) {
 			System.out.println("You cannot play a development card you bought this turn. Please choose again");
 			playDevelopmentCard(player, game1, scanner);
 			return;
 		}
 		
 		String type = play.getType();
 		
 		//selects the correct method depending on the type of card being played
 		if (type.equals("knight")) {
 			
			playKnightCard(player, game1, scanner);
 		}
 		if (type.equals("road building")) {
 			
			playRoadBuildingCard(player, game1, scanner);
 		}
 		if (type.equals("year of plenty")) {
 			
 			cardPlayed = playYearOfPlentyCard(player, game1, scanner);
 		}
 		if (type.equals("monopoly")) {
 			
 			playMonopolyCard(player, game1, scanner);
 		}
 		//should not ever be needed since victory point cards are played immediately
 		//here in case
 		if (type.equals("victory point")) {
 			
			playVictoryPointCard(player);
 		}
 		
 		//if the card has been played, it is removed from the player's hand

 		if (cardPlayed) {
 			
 			cards.remove(play);
 			player.setDevelopmentCards(cards);
 		}
 	}
 	
 	//plays a knight card
	public static void playKnightCard(Player player, Game game1, Scanner scanner) {
		
		moveRobber(player, game1, scanner);
		//TODO card steal?
		
		player.setLargestArmy(player.getLargestArmy() + 1);
 		
		//TODO check for largest army
		checkEndOfGame(player);
 	}
 	
	//plays a road building card
	public static void playRoadBuildingCard(Player player, Game game1, Scanner scanner) {
 		
 		boolean roadBuilding = true;
 		
 		//lets the player build two roads
 		for (int i = 0; i < 2; i++) {
 			
 			buildRoad(player, game1, scanner, roadBuilding);
 		}
 	}
	
	//plays a year of plenty card
	public static boolean playYearOfPlentyCard(Player player, Game game1, Scanner scanner) {
		
		ArrayList<ResourceCard> cards = player.getResourceCards();
		boolean hasResource = true;
		
		//lets the player choose two resources from the bank
		for (int i = 0; i < 2; i++) {
			
			hasResource = chooseResourceYOP(cards, game1, scanner);
		}
		
		player.setResourceCards(cards);
		
		//if there are no resources in the bank the card cannot be played
		return hasResource;
	}
	
	//lets the player choose the resource for YOP
	public static boolean chooseResourceYOP(ArrayList<ResourceCard> cards, Game game1, Scanner scanner) {
		
		ArrayList<ResourceCard> brick = game1.getBrick();
		ArrayList<ResourceCard> lumber = game1.getLumber();
		ArrayList<ResourceCard> wool = game1.getWool();
		ArrayList<ResourceCard> ore = game1.getOre();
		ArrayList<ResourceCard> grain = game1.getGrain();
		
		//checks if resources can be taken
		//if not the card is not played
		if (brick.size() <= 0 && lumber.size() <= 0 && wool.size() <= 0 && ore.size() <= 0 && grain.size() <= 0) {
			
			System.out.println("There are no resources left in the bank. You cannot play this development card.");
			return false;
		}
		
		System.out.println("Pick a resource");
		System.out.println("1. Brick");
		System.out.println("2. Lumber");
		System.out.println("3. Wool");
		System.out.println("4. Ore");
		System.out.println("5. Grain");
		
		int choice = scanner.nextInt();
		
		//lets the player take a card from the bank
		switch (choice) {
		case 1 :
			if (brick.size() <= 0) {
				System.out.println("There are no brick resource cards left. Please choose again.");
				chooseResourceYOP(cards, game1, scanner);
			}
			
			cards.add(brick.get(0));
			game1.getBrick().remove(brick);
			break;
		case 2:
			if (lumber.size() <= 0) {
				System.out.println("There are no lumber resource cards left. Please choose again.");
				chooseResourceYOP(cards, game1, scanner);
			}
			
			cards.add(lumber.get(0));
			game1.getLumber().remove(lumber);
			break;
		case 3:
			if (wool.size() <= 0) {
				System.out.println("There are no wool resource cards left. Please choose again.");
				chooseResourceYOP(cards, game1, scanner);
			}
			
			cards.add(wool.get(0));
			game1.getWool().remove(wool);
			break;
		case 4 :			
			if (ore.size() <= 0) {
				System.out.println("There are no ore resource cards left. Please choose again.");
				chooseResourceYOP(cards, game1, scanner);
			}
			
			cards.add(ore.get(0));
			game1.getOre().remove(ore);
			break;
		case 5 :
			if (grain.size() <= 0) {
				System.out.println("There are no grain resource cards left. Please choose again.");
				chooseResourceYOP(cards, game1, scanner);
			}
			
			cards.add(grain.get(0));
			game1.getGrain().remove(grain);
			break;
		default :
			System.out.println("Invalid choice. Please choose again");
			chooseResourceYOP(cards, game1, scanner);
		}
		
		return true;
	}
	
	//plays a monopoly card
	public static void playMonopolyCard(Player player, Game game1, Scanner scanner) {
		
		String resource = chooseResourceMonopoly(scanner);
		ArrayList<ResourceCard> cards = player.getResourceCards();
		
		ArrayList<Player> players = game1.getPlayers();
		players.remove(player);
		
		//takes the resources of specified type from each of the other
		//players' hands and gives them to the player
		for (int i = 0; i < players.size(); i++) {
			
			Player player2 = players.get(i);
			ArrayList<ResourceCard> player2Cards = player2.getResourceCards();
			
			for (int j = 0; j < player2Cards.size(); j++) {
				
				ResourceCard card = player2Cards.get(i);
				
				if (card.getResource().equals(resource)) {
					
					player2Cards.remove(card);
					cards.add(card);
				}
			}
			
			player2.setResourceCards(player2Cards);
		}
	
		player.setResourceCards(cards);
	}
	
	//lets the player choose the resource for monopoly
	public static String chooseResourceMonopoly(Scanner scanner) {
		
		System.out.println("Pick a resource");
		System.out.println("1. Brick");
		System.out.println("2. Lumber");
		System.out.println("3. Wool");
		System.out.println("4. Ore");
		System.out.println("5. Grain");
		
		int choice = scanner.nextInt();
		String resource = "";
		
		switch (choice) {
		case 1 :
			resource = "brick";
			break;
		case 2 : 
			resource = "lumber";
			break;
		case 3 :
			resource = "wool";
			break;
		case 4 :
			resource = "ore";
			break;
		case 5 :
			resource = "grain";
			break;
		default :
			System.out.println("Invalid choice. Please choose again");
			chooseResourceMonopoly(scanner);
		}
		
		return resource;
	}
	
	//plays a victory point card
	public static void playVictoryPointCard(Player player) {
				
		player.setVictoryPoints(player.getVictoryPoints() + 1);
		
		checkEndOfGame(player);
	}

	
	
	
	//methods found in old turn class
	public static void checkIfPortSettled(Player player, Intersection settlement){
		int xCoord = settlement.getCoordinate().getX();
		int yCoord = settlement.getCoordinate().getY();
		for(int i = 0; i < board1.getPorts().size(); i++){
			if((xCoord == board1.getPorts().getCoordinateA().getX()) && (yCoord == board1.getPorts().getCoordinateA().getY())){
				board1.getPorts().get(i).setOwner(player);
			}
		}
	}

	public static void trade(Player player, Scanner scanner){
		ArrayList<Port> settledPorts = new ArrayList<Port>();
		for(int i = 0; i < 9; i++){

		}
		boolean bank = tradeBankOrPlayer(scanner);
		if(bank){
			boolean hasStandardPort = checkStandardPorts(player);
		}
		else{

		}
	}

	public static boolean tradeBankOrPlayer(Scanner scanner){
		System.out.println("Press 'B' to trade with the bank, and 'P' to trade with other players:");
		char choice = scanner.next().toUpperCase();
		switch (choice) {
			case 'B' :
				return true;
				break;
			case 'P' :
				return false;
				break;
			default :
				System.out.println("Invalid choice. Please choose again");
				tradeBankOrPlayer(scanner);
		}
	}

	public static boolean hasStandardPort(Player player){

		return false;
	}


}




