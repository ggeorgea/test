import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Class contains all the methods to allow players to do things on their turn
 */
public class Turn {
	
	private static final boolean END_GAME = true;
	private static final int END_TURN = 4;
	private static final int ROBBER = 7;
	private static final int NO_ROADS = 15;
	private static final int NO_SETTLEMENTS = 5;
	private static final int NO_CITIES = 4;
	
	//allows the player to have their turn
	public static boolean newTurn(Player player, Game game1, Scanner scanner) {
		
		Map.printMap(game1.getBoard());
		
		Dice.rollDice(player, scanner);
		
		if (player.getCurrentRoll() != ROBBER) {
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
			System.out.println("4: End turn?");
			System.out.println("5: Show your hand?");
			System.out.println("6: Print map?");
			System.out.println("7: Length of your Longest Road?");

			
			choice = scanner.nextInt();
		
			
			int ch = 0;
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
			case 6 :
				Map.printMap(game1.getBoard());
				break;
			case 7 :
				System.out.println("Your Longest Road Length is: "+player.getLongestRoad());
				break;
			case 5:
				System.out.print("(");
				Iterator<ResourceCard> it = player.getResourceCards().iterator();
				while(it.hasNext()){
					System.out.print(it.next().getResource());
					if(it.hasNext()){
						System.out.print(", ");
					}
				}
				System.out.print(")\n");
				continue;
				
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
		Board board1 = game1.getBoard();
		
		ArrayList<Hex> hexes = game1.getBoard().getHexes();
		
		for (int i = 0; i < hexes.size(); i++) {
			
			Hex hex = hexes.get(i);
			
			if (hex.getNumber() == hexValue) {
				if (!(hex.getisRbberHere().equals("R"))) {
					
					//checks the intersections around the hex
					//if a player owns one they get resources
					//1 for settlement, 2 for city
					
					/*if (player owns settlement) {
						String terrain = hex.getTerrain();
						getResources(player, terrain, 1);
					}*/
					/*if (player owns city) {
					String terrain = hex.getTerrain();
					getResources(player, terrain, 2);
					}*/
				

					int coX = hex.getCoordinate().getX();
					int coY = hex.getCoordinate().getY();
					
					String terrain = hex.getTerrain();
					
					Coordinate coNe;
					 
					 coNe = new Coordinate(coX+1,coY);
					 testLocForRex(coNe,terrain,game1);
					 
					 coNe = new Coordinate(coX,coY+1);
					 testLocForRex(coNe,terrain,game1);
	
					 coNe = new Coordinate(coX+1,coY+1);
					 testLocForRex(coNe,terrain,game1);

					 coNe = new Coordinate(coX-1,coY-1);
					 testLocForRex(coNe,terrain,game1);

					 coNe = new Coordinate(coX-1,coY);
					 testLocForRex(coNe,terrain,game1);
			
					 coNe = new Coordinate(coX,coY-1);				
					 testLocForRex(coNe,terrain,game1);

			
					
	
					
				}
			}
		}
	}
	
	//part of the resource allocation method
	public static void testLocForRex(Coordinate co1, String terrain, Game game1) {
		Board board1 = game1.getBoard();
		Location loc = board1.getLocationFromCoordinate(co1);
		if(loc.getType().equals("Intersection")&&
				(! (((Intersection) loc.getContains()).getOwner().getName()==null))){
			
			Intersection build = (Intersection) loc.getContains();
			System.out.println(terrain + " produces a resource at "+ co1.getX()+co1.getY()+" for " + build.getOwner().getName());
			if(build.getBuilding().getType().equals("c")){
				getResources(build.getOwner(),terrain,game1,2);
			}
			else{
				getResources(build.getOwner(),terrain,game1,1);

			}
		}
		
	}

	
	//gives the resources to a player
	public static void getResources(Player player, String terrain, Game game1, int n) {
		
		ArrayList<ResourceCard> cards = player.getResourceCards();
		
		for (int i = 0; i < n; i++) {
		
			ResourceCard card = null;

			switch(terrain) {
			case "P" : 
				ArrayList<ResourceCard> wool = game1.getWool();
				card = wool.get(0);
				wool.remove(0);
				game1.setWool(wool);
				break;
			case "F" :
				ArrayList<ResourceCard> lumber = game1.getLumber();
				card = lumber.get(0);
				lumber.remove(0);
				game1.setLumber(lumber);
				break;
			case "M" :
				ArrayList<ResourceCard> ore = game1.getOre();
				card = ore.get(0);
				ore.remove(0);
				game1.setOre(ore);
				break;
			case "H" :
				ArrayList<ResourceCard> brick = game1.getBrick();
				card = brick.get(0);
				brick.remove(0);
				game1.setBrick(brick);
				break;
			case "G" :
				ArrayList<ResourceCard> grain = game1.getGrain();
				card = grain.get(0);
				grain.remove(0);
				game1.setGrain(grain);
				break;
			}

			if (card != null) {
				cards.add(card);
			}
		}
		
		player.setResourceCards(cards);
	}
	
//-----Methods to play the robber for the turn-----//
	
	//checks if any players have more than seven cards when the robber
	//is activated
	public static void checkCardRemoval(Game game1, Scanner scanner) {
		
		ArrayList<Player> players = game1.getPlayers();
		
		for (int i = 0; i < players.size(); i++) {
			
			Player player = players.get(i);
			ArrayList<ResourceCard> cards = player.getResourceCards();
			
			if (cards.size() > ROBBER) {
					
				cardRemoval(player, cards, game1, scanner);
				player.setResourceCards(cards);
			}
		}
		
		game1.setPlayers(players);
	}
	
	//asks players with more than seven cards to choose the cards they remove
	public static void cardRemoval(Player player, ArrayList<ResourceCard> cards, Game game1, Scanner scanner) {
		try{
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
		catch(InputMismatchException e){
			System.out.println("Invalid choice. Please choose again");
			scanner.nextLine();
			cardRemoval(player,cards,game1,scanner);
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
			 road3 = board1.getRoadFromCo(road.getCoordinateB(), new Coordinate(x2+1,y2));
			 road4 = board1.getRoadFromCo(road.getCoordinateB(), new Coordinate(x2,y2-1));
		}
		if(
				((road1!=null)&&(road1.getOwner().equals(player)))
				||((road2!=null)&&road2.getOwner().equals(player))
				||((road3!=null)&&road3.getOwner().equals(player))
				||((road4!=null)&&road4.getOwner().equals(player))
				){
		return true;}
		else{return false;}
	}
	
	
	//lets the player build a road
	public static void buildRoad(Player player, Game game1, Scanner scanner) {

		ArrayList<ResourceCard> resources = hasRoadResources(player);
		int roadsLeft = NO_ROADS - player.getNoRoads();
		Road road = getRoadCoordinates(player, game1, scanner);
		 
		//RELEASE TEST
		/*
		if (resources.size() != 2) {
			
			System.out.println("You do not have enough resources to build a road");
			return;
		}
		else if (roadsLeft <= 0) {
			
			System.out.println("You do not have any roads left to place");
			return;
		}
		else*/ if (road == null) {
				
			System.out.println("Invalid coordinates. Please choose again");
			buildRoad(player, game1, scanner);
			return;
		}
		else if (road.getOwner().getName() != null) {
				
			System.out.println("A road has already been placed here. Please choose again");
			buildRoad(player, game1, scanner);
			return;
		}
		else if (!checkConnected(road,player,game1)) {
			
			System.out.println("Road must be placed beside your other roads " +
				"and settlements. Please choose again");
			buildRoad(player, game1, scanner);
			return;
		 }
		else {
			//RELEASE TEST
			/*
			ArrayList<ResourceCard> cards = player.getResourceCards();
			cards.remove(resources.get(0));
			cards.remove(resources.get(1));
			player.setResourceCards(cards);
			*/
			road.setOwner(player);
			player.setNoRoads(player.getNoRoads() - 1);
			
			System.out.println("Player " + player.getName() + " placed road at: (" + road.getCoordinateA().getX() 
					+ "," + road.getCoordinateA().getY() + "),(" + road.getCoordinateB().getX() + "," + road.getCoordinateB().getY() + ")");
			
			
			Catan.CheckPlayerLongestRoad(player, game1, road);
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
		try{
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
		if(!((2*y1 <= x1 +8)&&(2*y1>=x1-8)&&(y1<=2*x1+8)&&(y1>=2*x1-8)&&(y1>=-x1-8)&&(y1<=-x1+8))){
			
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
		try{
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
				||(!game1.getBoard().getLocationFromCoordinate(a).getType().equals("Intersection"))
				){
			
			System.out.println("Invalid coordinates. Please choose again");
			buildSettlement(player, game1, scanner);
			return null;
		}
		
		
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
		Coordinate a = new Coordinate(x, y);

		//checks the coordinates are in the correct range
		if(!((2*y <= x +8)&&(2*y>=x-8)&&(y<=2*x+8)&&(y>=2*x-8)&&(y>=-x-8)&&(y<=-x+8))
				||(!game1.getBoard().getLocationFromCoordinate(a).getType().equals("Intersection"))
				){
			
			System.out.println("Invalid coordinates. Please choose again");
			buildCity(player, game1, scanner);
			return null;
		}
		
		
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
		
		try{
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
		catch(IndexOutOfBoundsException e)
		{
			return new ArrayList<ResourceCard>();
		}
		if (ore != null && wool != null && grain != null) {
			
			resources.add(ore);
			resources.add(wool);
			resources.add(grain);
		}
		
		return resources;
	}
}