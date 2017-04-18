package completeCatan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import java.io.IOException;
 
/**
 * Class contains all the methods to set up the game and board
 */
public class Setup {
 
	private static final int NO_KNIGHTS = 14;
	private static final int NO_VICTORY_CARDS = 5;
 	private static final int NO_RESOURCE = 19;
	
	private static final String INTERSECTION = "intersection";
	private static final String TOWN = "t";
	private static final String HEX = "hex";
	private static final String ROBBER = "R";
	
	private static final String PASTURE = "P";
	private static final String FOREST = "F";
	private static final String MOUNTAIN = "M";
	private static final String HILL = "H";
	private static final String GRASS = "G";
	
	private static final String KNIGHT = "knight";
	private static final String ROAD_BUILDING = "road_building";
	private static final String YEAR_OF_PLENTY = "year of plenty";
	private static final String MONOPOLY = "monopoly";
	private static final String VICTORY_POINT = "victory point";
 
//-----Methods to initialise players and their order for the game-----//
 
 	//gets an array list of players with unique identifiers
 	public static ArrayList<Player> setPlayers(Scanner scanner, ArrayList<PlayerSocket> socketArray) throws IOException {
 
 		//gets how many players there are
 		int n = socketArray.size() + 1;
 
 		ArrayList<Player> players = new ArrayList<Player>();
 
 		//gets each player to select a name/colour
 		for (int i = 0; i < n; i++) {
 
 			Player player = new Player();
 			players.add(player);
 			
 		}
 		
 		//this used to be combined with the previous loop, but they're split so that when the player is asked for their socket
 		//there will be a player to ask
 		for (int i = 0; i < n; i++) {
 
 			Player player = players.get(i);
 			
 			//pairs players with clients
 			if (i < socketArray.size()) {
 				player.setpSocket(socketArray.get(i));
 			}
 			
 			selectPlayerName(player, players, i, scanner);
 			
 		}
 
 		return players;
 	}
 
 	//asks the client how many clients there are
 	public static int requestClients(Scanner scanner) {
 
 		System.out.println("How many Clients should connect? 2 or 3");
 
 		int noPlayers = scanner.nextInt();
 
 		//makes sure the player inputs the correct value
 		if (!(noPlayers == 2 || noPlayers == 3)) {
 			System.out.println("Invalid number of clients. Please choose again.");
 			return requestClients(scanner);
 		}
 
 		return noPlayers;
 	}
 
 	//asks each player to choose a unique identifier
 	public static void selectPlayerName(Player player, ArrayList<Player> players, int n, Scanner scanner) throws IOException {
 
		Catan.printToClient("Select a character to be your player name.", player);
 		Catan.printToClient("Select from: W-White, R-Red, G-Green, B-Blue, O-Orange, Y-Yellow", player);
 
 		String name = Catan.getInputFromClient(player, scanner).toUpperCase();
 		char c = name.toCharArray()[0];
 		String check = "";
 
 		switch(c) {
 		case 'W' :
 			check = "W";
 			break;
 		case 'R' :
 			check = "R";
 			break;
 		case 'G' :
 			check = "G";
 			break;
 		case 'B' :
 			check = "B";
 			break;
 		case 'O' :
 			check = "O";
 			break;
 		case 'Y' :
 			check = "Y";
 			break;
 		default :
 			Catan.printToClient("Invalid character. Please choose again.", player);
 			selectPlayerName(player, players, n, scanner);
 			return;
 		}
 
 		//ensures the player selects a character that has not already been chosen
 		for (int i = 0; i < n; i++) {
 			if (players.get(i).getName().equals(check)) {
 				
 				Catan.printToClient("Another player is already using this character. Please choose again.", player);
 				selectPlayerName(player, players, n, scanner);
 				return;
 			}
 		}
 
 		player.setName(check);
 	}
 
 	//gets each player to roll the dice to determine the player order
 	public static ArrayList<Player> getPlayerOrder(Game game1, Scanner scanner) throws IOException {
 
 		ArrayList<Player> players = game1.getPlayers();
 
 		//asks each player to roll the dice
 		for (int i = 0; i < players.size(); i++) {
 			Dice.rollDice(players.get(i), scanner, game1);
 		}
 
 		//bubble sort to decide player order based on
 		//highest dice roll
 		for (int i = 0; i < players.size(); i++) {
 			for (int j = 1; j < players.size()-i; j++) {
 
 				Player player1 = players.get(j-1);
 				Player player2 = players.get(j);
 
 				if (player1.getCurrentRoll() < player2.getCurrentRoll()) {
 
 					Player temp = player1;
 					player1 = player2;
 					player2 = temp;
 
 					players.set(j-1, player1);
 					players.set(j, player2);
 				}
 			}
 		}
 
 		//prints player order
		for (int i = 0; i < players.size(); i++) {
			Catan.printToClient("Player order: ", players.get(i));
			
 			for (int j = 0; j < players.size(); j++) {
 				Catan.printToClient((j+1)+ ": " + players.get(j).getName(), players.get(i));
 			}
		}		
 
 		return players;
 	}
 
//-----Methods to initialise the development card deck for the game-----//
 
 	//gets the dev cards and shuffles the deck
 	public static ArrayList<DevelopmentCard> getDevCardDeck() {
 
 		ArrayList<DevelopmentCard> developmentCards = new ArrayList<DevelopmentCard>();
 
 		ArrayList<DevelopmentCard> knights = getKnightCards();
 		ArrayList<DevelopmentCard> progress = getProgressCards();
 		ArrayList<DevelopmentCard> victory = getVictoryPointCards();
 
 		developmentCards.addAll(knights);
 		developmentCards.addAll(progress);
 		developmentCards.addAll(victory);
 
 		Collections.shuffle(developmentCards);
 		return developmentCards;
 	}
 
 	//instantiates correct number of knights
 	public static ArrayList<DevelopmentCard> getKnightCards() {
 
 		ArrayList<DevelopmentCard> knights = new ArrayList<DevelopmentCard>();
 
 		//creates the correct number of knight cards and add them to the deck
 		for (int i = 0; i < NO_KNIGHTS; i++) {
 
			DevelopmentCard knight = new DevelopmentCard(KNIGHT, true);
 			knights.add(knight);
 		}
 
 		return knights;
 	}
 
 	//instantiates correct number of progress cards
 	public static ArrayList<DevelopmentCard> getProgressCards() {
 
 		ArrayList<DevelopmentCard> progress = new ArrayList<DevelopmentCard>();
 		int noProgress = 2;
 
 		//creates the correct number of road building cards and adds
 		//them to the deck
 		for (int i = 0; i < noProgress; i++) {
 
			DevelopmentCard roadBuilding = new DevelopmentCard(ROAD_BUILDING, true);
 			progress.add(roadBuilding);
 		}
 
 		noProgress += 2;
 
 		//creates the correct number of YOP cards and adds them to the deck
 		for (int i = 2; i < noProgress; i++) {
 
 			DevelopmentCard yearOfPlenty = new DevelopmentCard(YEAR_OF_PLENTY, true);
 			progress.add(yearOfPlenty);
 		}
 
 		noProgress += 2;
 
 		//creates the correct number of monopoly cards and adds them to the deck
 		for (int i = 4; i < noProgress; i++) {
 
 			DevelopmentCard monopoly = new DevelopmentCard(MONOPOLY, true);
 			progress.add(monopoly);
 		}
 
 		return progress;
 	}
 
 	//instantiates correct number of victory point cards
 	public static ArrayList<DevelopmentCard> getVictoryPointCards() {
 
 		ArrayList<DevelopmentCard> victory = new ArrayList<DevelopmentCard>();
 
 		//creates the correct number of victory point cards and adds them 
 		//to the deck
 		for (int i = 0; i < NO_VICTORY_CARDS; i++) {
 
			DevelopmentCard victoryPoint = new DevelopmentCard(VICTORY_POINT, true);
 			victory.add(victoryPoint);
 		}
 
 		return victory;
 	}
 
//-----Methods to initialise the resource card decks for the game-----//
 
 	//gets the deck of resource cards
 	public static ArrayList<ResourceCard> getResourceCardDeck(String resource) {
 
 		ArrayList<ResourceCard> resourceCards = new ArrayList<ResourceCard>();
 
 		for (int i = 0; i < NO_RESOURCE; i++) {
 
 			ResourceCard card = new ResourceCard(resource);
 			resourceCards.add(card);
 		}
 
 		return resourceCards;
 	}
 
//-----Methods to get the initial placement of roads and settlements for the game-----//
 
 	//gets the players to set the initial roads and settlements before turn 1
 	public static void setInitialRoadsAndSettlements(Game game1, Scanner scanner) throws IOException {
 
 		ArrayList<Player> players = game1.getPlayers();
 		Board board1 = game1.getBoard();
 
 		for (int i = 0; i < players.size(); i++) {
 
 			//each player places road, then settlement
 			Road road = placeRoad(players.get(i), board1, scanner, game1);
 			placeSettlement(players.get(i), road, board1, scanner, game1);
 		}
 
 		for (int i = players.size()-1; i >= 0; i--) {
 
 			//each player places road, then settlement
 			Road road = placeRoad(players.get(i), board1, scanner, game1);
 			Intersection settlement = placeSettlement(players.get(i), road, board1, scanner, game1);
 			initialResourceAllocation(players.get(i), settlement, game1);
 		}
 
 		game1.setPlayers(players);
 		game1.setBoard(board1);
}

 	//checks a road can be placed at the coordinate specified
 	public static boolean checkNear(Board board1, int x1, int y1, int x2, int y2) {
 		
 		Location loca1 = board1.getLocationFromCoordinate(new Coordinate(x1,y1));
 		Location loca2 = board1.getLocationFromCoordinate(new Coordinate(x2,y2));
 		
		if ((!(loca1.getType().equals(INTERSECTION))) || (!(loca2.getType().equals(INTERSECTION)))
 				|| (!(((Intersection)loca1.getContains()).getOwner().getName() == null)) 
 				|| (!(((Intersection)loca2.getContains()).getOwner().getName() == null))) {
			
 			return false;
 		}

 		return true;
 	}
 
 	//lets the player place a road free of charge
 	//also does not depend on nearby roads
 	public static Road placeRoad(Player player, Board board1, Scanner scanner, Game game1) throws IOException {
 
 		Catan.printToClient("Please select where to place your road:", player);
 
 		Catan.printToClient("Coordinate 1: ", player);
 		Catan.printToClient("Select X coordinate", player);
		int x1 = Integer.parseInt(Catan.getInputFromClient(player, scanner));
 
 		Catan.printToClient("Select Y coordinate", player);
		int y1 = Integer.parseInt(Catan.getInputFromClient(player, scanner));
 
 		Catan.printToClient("Coordinate 2: ", player);
 		Catan.printToClient("Select X coordinate", player);
		int x2 = Integer.parseInt(Catan.getInputFromClient(player, scanner));
 
 		Catan.printToClient("Select Y coordinate", player);
		int y2 = Integer.parseInt(Catan.getInputFromClient(player, scanner));
 
 		//checks the coordinates are in the correct range
 		if ((!((2*y1 <= x1+8) || (2*y1 >= x1-8) && (y1 <= 2*x1+8) && (y1 >= 2*x1-8) && (y1 >= -x1-8) 
 				&& (y1 <= -x1+8))) && (!((2*y2 <= x2+8) || (2*y2 >= x2-8) && (y2 <= 2*x2+8) && (y2 >= 2*x2-8) && (y2 >= -x2-8)))) {
			
 			Catan.printToClient("Invalid coordinates. Please choose again", player);
 			return placeRoad(player, board1, scanner, game1);
 		}
 		else if (!checkNear(board1, x1,y1,x2,y2)) {
			
 			Catan.printToClient("Invalid coordinates. Please choose again", player);
 			return placeRoad(player, board1, scanner, game1);
 		}
 		else {
 
 			Coordinate a = new Coordinate(x1, y1);
 			Coordinate b = new Coordinate(x2, y2);
 
 			Road road = board1.getRoadFromCo(a, b);
 
 			if (road == null) {
 
 				Catan.printToClient("Invalid coordinates. Please choose again", player);
 				return placeRoad(player, board1, scanner, game1);
 			}
 			else if (road.getOwner().getName() != null) {
 
 				Catan.printToClient("A road has already been placed here. Please choose again", player);
 				return placeRoad(player, board1, scanner, game1);
 			}
 			
 			//if the road can be placed then success messages are printed to all clients
 			else {
 
 				Catan.printToClient("You placed road at: (" + x1 + "," + y1 + "),(" + x2 + "," + y2 + ")", player);
				
 				ArrayList<Player> players = game1.getPlayers();
				
				for (int i = 0; i < players.size(); i++) {
					if (players.get(i) != player) {
						Catan.printToClient("Player " + player.getName() + " placed road at: (" + x1 + "," + y1 + "),(" + x2 + "," + y2 + ")", players.get(i));
 					}
 				}
				
 				road.setOwner(player);
 				player.setNoRoads(player.getNoRoads() + 1);
 
 				return road;
 			}
 		}
 	}
 
 	//lets a player place a settlement free of charge
 	public static Intersection placeSettlement(Player player, Road road, Board board1, Scanner scanner, Game game1) throws NumberFormatException, IOException {
 
 		Catan.printToClient("Please select where to place your settlement", player);
 		
 		Catan.printToClient("Select X coordinate", player);
		int x = Integer.parseInt(Catan.getInputFromClient(player, scanner));
 
 		Catan.printToClient("Select Y coordinate", player);
		int y = Integer.parseInt(Catan.getInputFromClient(player, scanner));
		
 		Coordinate a = new Coordinate(x, y);
 
 		//checks the coordinates are in the correct range
 		if ((!((2*y <= x+8) || (2*y >= x-8) && (y <= 2*x+8) && (y >= 2*x-8) && (y >= -x-8) && (y <= -x+8)))
				|| (!(board1.getLocationFromCoordinate(a).getType().equals(INTERSECTION)))) {
 
 			Catan.printToClient("Invalid coordinates. Please choose again", player);
 			return placeSettlement(player, road, board1, scanner, game1);
 		}
 
 		Intersection settlement = (Intersection) board1.getLocationFromCoordinate(a).getContains();
 		ArrayList<Intersection> illegal = settlement.getIllegal();
 
 		if (!(road.getCoordinateA().getX() == x && road.getCoordinateA().getY() == y)
 				&& !(road.getCoordinateB().getX() == x && road.getCoordinateB().getY() == y)) {
 
 			Catan.printToClient("Settlement must be placed beside road. Please choose again", player);
 			return placeSettlement(player, road, board1, scanner, game1);
 		}
 
 		if (settlement.getOwner().getName() != null) {
 
 			Catan.printToClient("A settlement has already been placed here. Please choose again", player);
 			return placeSettlement(player, road, board1, scanner, game1);
 		}
 
 		for (int i = 0; i < illegal.size(); i++) {
 
 			Intersection inter = illegal.get(i);
 
 			if (inter.getOwner().getName() != null) {
 				
 				Catan.printToClient("Settlement must be placed more than two roads away. Please choose again", player);
 				return placeSettlement(player, road, board1, scanner, game1);
 			}
 		}
 
 		//prints success message to the player
 		Catan.printToClient("You placed settlement at: (" + x + "," + y + ")", player);
		
 		ArrayList<Player> players = game1.getPlayers();
		
 		//prints message notifying other players of the action
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i) != player) {
				Catan.printToClient("Player " + player.getName() + ""
							+ " placed settlement at: (" + x + "," + y + ")", players.get(i));
 			}
 		}
		
 		settlement.setOwner(player);
		settlement.setBuilding(new Building(TOWN,1));
 		player.getFirstSettlements().add(settlement);
 		player.setNoSettlements(player.getNoSettlements() + 1);
 		Trade.checkIfPortSettled(player, settlement, game1);

 		return settlement;
 	}
 
 	//gets the resources based on the placement of the second settlement for the player
 	public static void initialResourceAllocation(Player player, Intersection settlement, Game game1) {
 
 		ArrayList<ResourceCard> resourceCards = player.getResourceCards();
 
 		int x = settlement.getCoordinate().getX();
 		int y = settlement.getCoordinate().getY();
 
 		Coordinate nearbyHex;
 
 		nearbyHex = new Coordinate(x, y-1);
 		if (Board.coordInRange(nearbyHex)) {
 			resourceCards = getResources(player, resourceCards, nearbyHex, game1);
 		}
 
 		nearbyHex = new Coordinate(x, y+1);
 		if (Board.coordInRange(nearbyHex)) {
 			resourceCards = getResources(player, resourceCards, nearbyHex, game1);
 		}
 
 		nearbyHex = new Coordinate(x-1, y);
 		if (Board.coordInRange(nearbyHex)) {
 			resourceCards = getResources(player, resourceCards, nearbyHex, game1);
 		}
 
 		nearbyHex = new Coordinate(x+1, y);
 		if (Board.coordInRange(nearbyHex)) {
 			resourceCards = getResources(player, resourceCards, nearbyHex, game1);
 		}
 
 		nearbyHex = new Coordinate(x-1, y-1);
 		if (Board.coordInRange(nearbyHex)) {
 			resourceCards = getResources(player, resourceCards, nearbyHex, game1);
 		}
 
 		nearbyHex = new Coordinate(x+1, y+1);
 		if (Board.coordInRange(nearbyHex)) {
 			resourceCards = getResources(player, resourceCards, nearbyHex, game1);
 		}
 
 		//prints what each player gets
 		Catan.printToClient("You get:", player);
 		
 		for (int i = 0; i < resourceCards.size(); i++) {
 			Catan.printToClient("1x " + resourceCards.get(i).getResource(), player);
 		}
		
 		ArrayList<Player> players = game1.getPlayers();
		
		for (int i = 0; i < players.size(); i++) {
			
 			if (players.get(i) != player) {
				Catan.printToClient("Player " + player.getName() + " gets:", players.get(i));
				
 				for (int j = 0; j < resourceCards.size(); j++) {
 					
					Catan.printToClient("1x " + resourceCards.get(j).getResource(), players.get(i));
 				}
 			}
 		}
 		
		player.setResourceCards(resourceCards);
 	}
 
 	//gets the resources for a player from a hex
 	public static ArrayList<ResourceCard> getResources(Player player, ArrayList<ResourceCard> resourceCards, Coordinate nearbyHex, Game game1) {
 
 		Board board1 = game1.getBoard();
 		Location location = board1.getLocationFromCoordinate(nearbyHex);
 
		if (location.getType().equals(HEX)) {
 
 			Hex hex = (Hex) location.getContains();
 			String terrain = hex.getTerrain();
 
 			ResourceCard card = null;
 
 			//gets the correct resource card for the hex
 			switch(terrain) {
			case PASTURE :
 				ArrayList<ResourceCard> wool = game1.getWool();
 				card = wool.get(0);
 				wool.remove(0);
 				game1.setWool(wool);
 				break;
			case FOREST :
 				ArrayList<ResourceCard> lumber = game1.getLumber();
 				card = lumber.get(0);
 				lumber.remove(0);
 				game1.setLumber(lumber);
 				break;
			case MOUNTAIN :
 				ArrayList<ResourceCard> ore = game1.getOre();
 				card = ore.get(0);
 				ore.remove(0);
 				game1.setOre(ore);
 				break;
			case HILL :
 				ArrayList<ResourceCard> brick = game1.getBrick();
 				card = brick.get(0);
 				brick.remove(0);
 				game1.setBrick(brick);
 				break;
			case GRASS :
 				ArrayList<ResourceCard> grain = game1.getGrain();
 				card = grain.get(0);
 				grain.remove(0);
 				game1.setGrain(grain);
 				break;
 			}
 
 			//if the card is not null, it adds the card to the player's hand
 			if (card != null) {
 				resourceCards.add(card);
 			}
 		}
 
 		return resourceCards;
 	}
 
 //-----Methods to get the initialise the board and hexes for the game-----//
 
 	//this is the method that adds random terrain from the catan box to each hex, it also calls all the other methods involved in making a board
 	public static Board getMeABoard() {
 		
 		int Edgesize = 3;
 		Board board1 = getHexBoard(Edgesize);
 
 		// this generates a random arrangement of terrain
 		int givenDesert = 0;
 		Iterator<String> terrainSt = getTerrainArrangement();
		
 		// this fills the arraylist with hexes, with random terrain
 		for (int i = 0; i < board1.getHexes().size(); i++) {
			
 			ArrayList<Hex> Hexes = board1.getHexes();
 			String terr = terrainSt.next();
 			Hex current = Hexes.get(i);
 			
 			current.setTerrain(terr);
 			Hexes.set(i, current);
 			board1.setHexes(Hexes);
			
 			if (terr.equals("D")) {
 				givenDesert = i;
 			}
 		}
 
 		setUpLocations(board1);
 		board1 = setRandHexNumbersAndRob(board1, givenDesert);
 
 		makeIntersectionOrdering(board1);
 		setRoadArrayAndMap(board1);
 		addPorts(board1);
 		setIllegals(board1);
		
 		return board1;
 	}
 
 	//this method adds the hexes,with just coordinates set, to the arraylist in such a way to get them to print out prettily by just iterating through them
 	public static Board getHexBoard(int edgesize) {
 		
 		Board board1 = new Board();
 		ArrayList<Hex> hexes1 = new ArrayList<Hex>();
 		boolean past = false;
 		int startx = -(edgesize - 1);
 		int starty = edgesize - 1;
 		int len = edgesize;
 		
 		for (int i = 0; i < (2*edgesize) - 1; i++) {
			
 			int xof = 0;
 			int yof = 0;
			
 			for (int j = 0; j < len; j++) {
				
 				Hex hexa = new Hex();
 				hexa.setCoordinate(new Coordinate(startx + xof, starty + yof));
 		
 				if (((startx + xof) == 0) && ((starty + yof) == 0)) {
 					past = true;
 				}
				
 				xof += 2;
 				yof += 1;
 				hexes1.add(hexa);
 			}
 			if (!past) {
				
 				startx -= 1;
 				starty -= 2;
 				len++;
			} 
			else {
				
 				startx++;
 				starty--;
 				len--;
 			}
 		}
		
 		board1.setHexes(hexes1);
 		return board1;
 	}
 
 	//this is just a bit of modularity that shuffles the terrain needed in a catan game and returns an iterator over them
 	public static Iterator<String> getTerrainArrangement() {
 		
 		String[] terrains = { "F", "F", "F", "F", "M", "M", "M", "G", "G", "G",
 				"G", "P", "P", "P", "P", "H", "H", "H", "D", };
 		ArrayList<String> s1 = new ArrayList<String>();
 		
 		for (int i = 0; i < terrains.length; i++) {
 			s1.add(terrains[i]);
 		}
		
 		Collections.shuffle(s1);
 		Iterator<String> terrainSt = s1.iterator();
		
 		return terrainSt;
 	}
 
 	// this method adds a random number from the array of numbers needed on a catan board to each hex, since this happens after terrain allocation, it is also where the desert and the number 7 are matched up, and when the robber is allocated
 	public static Board setRandHexNumbersAndRob(Board board1, int givenDesert) {
 
 		int[] normalNumbers = {5,2,3,10,9,12,11,4,10,9,4,5,3,11};
 		int[] redNumbers = {6,8,8,6};
 		Hex hexwDes = board1.getHexes().get(givenDesert);
 		
 		board1.setRobber(hexwDes.getCoordinate());
 		hexwDes.setisRobberHere(ROBBER);
 		
 		ArrayList<Integer> normalAL = new ArrayList<Integer>();
 		for (int i = 0; i < normalNumbers.length; i++) {
 			normalAL.add(normalNumbers[i]);
 		}
		
 		Collections.shuffle(normalAL);
 		
 		ArrayList<Integer> redAL = new ArrayList<Integer>();
 		for (int i = 0; i < redNumbers.length; i++) {
 			redAL.add(redNumbers[i]);
 		}
		
 		Collections.shuffle(redAL);
 		RandomLocationIterator hexIt = new RandomLocationIterator(board1);
 		
 		ArrayList<ArrayList<Location>> shuffledAL= new ArrayList<ArrayList<Location>>();
 		Location[][] boardClone = board1.getBoardLocations().clone();
 		ArrayList<Location[]> boardCloneX = new ArrayList<Location[]>();
 		Collections.shuffle(boardCloneX);
		
 		for (int i = 0; i < boardCloneX.size(); i++) {
			
 			ArrayList<Location> CloneY = new ArrayList<Location>();
			
 			for (int j = 0; j< boardCloneX.get(i).length;j++) {
 				CloneY.add(boardCloneX.get(i)[j]);
 			}
			
 			Collections.shuffle(CloneY);
 			shuffledAL.add(CloneY);
 		}
		
 		Iterator redIt = redAL.iterator();
 		Iterator normIt = normalAL.iterator();
 		
 		while (redIt.hasNext()) {
			
 			int no = ((Integer) redIt.next()).intValue();
 			boolean found = false;
			
 			while (!found) {
				
 				Location thisLoc = hexIt.getNextHex();
 				Hex thisHex = (Hex) thisLoc.getContains();
				
				if (thisHex.getisRobberHere().equals(ROBBER)) {
					
 					thisHex.setNumber(7);
 					thisLoc = hexIt.getNextHex();
 					thisHex= (Hex) thisLoc.getContains();
 				}
 				if (thisHex.getNumber() == -1) {
					
 					found = true;				
 					thisHex.setNumber(no);
					
 					setHexNumberTest(thisLoc, board1, normIt, -1, +1);
 					setHexNumberTest(thisLoc, board1, normIt, +1, +2);
 					setHexNumberTest(thisLoc, board1, normIt, +2, +1);
 					setHexNumberTest(thisLoc, board1, normIt, +1, -1);
 					setHexNumberTest(thisLoc, board1, normIt, -1, -2);
 					setHexNumberTest(thisLoc, board1, normIt, -2, -1);	
 				}
 			}
 		}
 		while (normIt.hasNext()) {
			
 			int no = ((Integer) normIt.next()).intValue();
 			boolean found = false;
			
 			while (!found) {
				
 				Location thisLoc = hexIt.getNextHex();
 				Hex thisHex = (Hex) thisLoc.getContains();
				
 				if (thisHex.getisRobberHere().equals(ROBBER)) {
					
 					thisHex.setNumber(7);
 					thisLoc = hexIt.getNextHex();
 					thisHex= (Hex) thisLoc.getContains();
 				}
 				if (thisHex.getNumber() == -1) {
					
 					thisHex.setNumber(no);
 					found = true;
 				}
 			}
		}
		
 		Location robL = hexIt.getNextHex();
		
		while (robL != null) {
			
 			Hex robLoc = (Hex)(robL.getContains());
			
			if (robLoc.getNumber() == -1 && robLoc.getisRobberHere().equals(ROBBER)) {
				
 				//System.out.println("The robber was left out "+ robLoc.getisRobberHere()+robLoc.getNumber());
 				robLoc.setNumber(7);
 
 				break;
 			}
			
 			robL = hexIt.getNextHex();
 		}
 
 		return board1;
 	}
 	
 	public static void setHexNumberTest(Location thisLoc, Board board1, Iterator normIt, int xc, int yc) {
 		
 		int x = thisLoc.getCoord().getX() + xc;
 		int y = thisLoc.getCoord().getY() + yc;
 		
 		if(((2*y <= x+8) && (2*y >= x-8) && (y <= 2*x+8) && (y >= 2*x-8) && (y >= -x-8) && (y <= -x+8))) {
 
 			Location l1 = board1.getLocationFromCoordinate(new Coordinate(x, y));
			
			if(l1.getType().equals(HEX)
					&&((Hex)l1.getContains()).getNumber() == -1
					&& (!((Hex)l1.getContains()).getisRobberHere().equals(ROBBER))) {
				
 				((Hex)l1.getContains()).setNumber(((Integer)normIt.next()).intValue());
 			}
 		}
 	}
 	
 	//this method makes a 2d array which is then used for accessing the hexes and intersections by coordinate, it also adds the hexes and the intersections(by calling another method) around the hexes
 	public static void setUpLocations(Board board1) {
 		
 		Location[][] boardLocations = new Location[11][11];
 		board1.setBoardLocations(boardLocations);
		
		for (int i = 0; i < 11; i++) {
 			for (int j = 0; j < 11; j++) {
				
 				boardLocations[i][j]=new Location(new Coordinate(i-5, j-5),"empty", null);
 			}
 		}
		
		for (int i = 0; i < board1.getHexes().size(); i++) {
			
 			Hex thisHex = board1.getHexes().get(i);
			
 			int newX = thisHex.getCoordinate().getX()+5;
 			int newY = thisHex.getCoordinate().getY()+5;
			
 			boardLocations[newX][newY].setContains(thisHex);
			boardLocations[newX][newY].setType(HEX);
			
 			for (int j = 0; j < 6; j++) {
 				
 				switch(j){
				case 0: 
					setUpInter(newX+1, newY, boardLocations, board1);
					break;
				case 1: 
					setUpInter(newX, newY+1, boardLocations, board1);
					break;
				case 2: 
					setUpInter(newX+1, newY+1, boardLocations, board1);
					break;
				case 3: 
					setUpInter(newX-1, newY-1, boardLocations, board1);
					break;
				case 4: 
					setUpInter(newX-1, newY, boardLocations, board1);
					break;
				case 5:
					setUpInter(newX, newY-1, boardLocations, board1);
					break;
 				}
 			}
 		}
 	}
 
 	//this method takes up an informal coordinate, and 2d array and adds a new intersection in the specified place
 	public static void setUpInter(int x, int y,  Location[][] boardLocations, Board board1) {
 		
 		if (boardLocations[x][y].getType().equals("empty")) {
			
 			Player noPlayer = new Player();
 			Building noBuild = new Building();
			
 			noBuild.setType(" ");
 			noPlayer.setName(null);
 			Intersection newIn = new Intersection(new Coordinate(x-5, y-5),noPlayer,noBuild);
			
 			boardLocations[x][y].setContains(newIn);
			boardLocations[x][y].setType(INTERSECTION);
 		}
 	}
 
 	//this method adds the intersections from the 2d array into our intersections arraylist for printing etc.. the tricky looking bits here are just part of an insertion sort to get the ordering right in a moderately fast, yet simple way
 	public static void makeIntersectionOrdering(Board board1) {
 		
 		ArrayList<Intersection> interestionsal  = new ArrayList<Intersection>();
		
 		for (int xval = 0; xval < board1.getBoardLocations().length; xval++) {
 			for (int yval = 0; yval < board1.getBoardLocations().length; yval++) {
				if (board1.getBoardLocations()[xval][yval].getType().equals(INTERSECTION)) {
					
					if (interestionsal.size() == 0) {
						interestionsal.add((Intersection) board1.getBoardLocations()[xval][yval].getContains());
 					}
					
 					for (int i = 0; i < interestionsal.size(); i++) {
						
 						Coordinate Cvalue = interestionsal.get(i).getCoordinate();
						
 						int cVal = 2 * (Cvalue.getY()+5) - (Cvalue.getX()+5);
 						int opVal = 2 * yval - xval;
						
 						if ((opVal > cVal) || ((opVal == cVal) && (xval < Cvalue.getX())) || (i+1 == interestionsal.size())) {
							
 							interestionsal.add(i,(Intersection) board1.getBoardLocations()[xval][yval].getContains());
 							break;
 						}
 					}
 				}
 			}
 		}
 
 		board1.setBuildings(interestionsal);
 	}
 
 	//this method adds the roads from the hashmap to the arraylist of roads, so they can be printed prettily, it works by go through the arraylist of intersection, and only adding already unadded roads
 	public static void setRoadArrayAndMap(Board board1) {
 		
 		ArrayList<Road> roads = new ArrayList<Road>();
 		HashMap roadmap = makeRoadHashMap(board1);
 		ArrayList<Intersection> intersections = board1.getBuildings();
 		
 		//check the 8 coords nearby to see if they have road in the hashmap
 		for (int i = 0; i < intersections.size(); i++) {
 			
 			int x = intersections.get(i).getCoordinate().getX();
 			int y = intersections.get(i).getCoordinate().getY();
			
 			Coordinate[] nearby = new Coordinate[6];
			
 			nearby[0] = new Coordinate(x,y+1);
 			nearby[1] = new Coordinate(x-1,y);
 			nearby[2] = new Coordinate(x+1,y+1);
 			nearby[3] = new Coordinate(x-1,y-1);
 			nearby[4] = new Coordinate(x,y-1);
 			nearby[5] = new Coordinate(x+1,y);
 			
 			for (int j = 0; j < 6; j++) {
 
 				String keys = new StringBuilder().append(x).append(y).append(nearby[j].getX()).append(nearby[j].getY()).toString();
				
 				if (roadmap.containsKey(keys)) {
 					if (!roads.contains(roadmap.get(keys))) {
						roads.add((Road)roadmap.get(keys));
 					}
 				}
 			}
 		}
 		
 		board1.setRoads(roads);
 	}
 
 	//this creates a hashmap that maps from coordinate pairs to roads, it does this basically by just making six for each hex (checking if each road has already been made)
 	public static HashMap makeRoadHashMap(Board board1) {
 		
 		HashMap roadmap = new HashMap();
 		for (int i = 0; i < board1.getHexes().size(); i++) {
 			
 			Hex currHex = board1.getHexes().get(i);
 			int x = currHex.getCoordinate().getX();
 			int y = currHex.getCoordinate().getY();
			
 			putNewRoad(x, y, 1, 1, 1, 0, roadmap);
 			putNewRoad(x, y, 1, 0, 0, -1, roadmap);
 			putNewRoad(x, y, 0, 1, 1, 1, roadmap);
 			putNewRoad(x, y, 0, 1, -1, 0, roadmap);
 			putNewRoad(x, y,-1, 0, -1, -1, roadmap);
 			putNewRoad(x, y, -1, -1, 0, -1, roadmap);
 		}
		
 		board1.setRoadmap(roadmap);
 		return roadmap;
 	}
 
 	//this method takes coordinates for a hex, and four offsets, two for each end of the road, and then adds a road to the hashmap if it doesnt exist already
 	public static void putNewRoad(int x, int y ,int off1, int off2, int off3, int off4, HashMap roadmap) {
 		
 		String keys = new StringBuilder().append(x+off1).append(y+off2).append(x+off3).append(y+off4).toString();
		
 		if (!roadmap.containsKey(keys)) {
		
 			Player noPlater = new Player();
 			noPlater.setName(null);
 			roadmap.put(keys, new Road( new Coordinate(x+off1,y+off2),new Coordinate(x+off3,y+off4), noPlater));
 		}
 	}
 
 	//this method adds the ports to an arrraylist in a way in which they can be printed nicely
 	public static void addPorts(Board board1) {
 		
 		Iterator<String> resIt = getPortArrangement();
 		ArrayList<Port> ports = new ArrayList<Port>();
		
 		board1.getPorts().add(new Port(new Coordinate(0,4), new Coordinate(1,4),resIt.next(), null));
 		board1.getPorts().add(new Port(new Coordinate(-2,3), new Coordinate(-3,2),resIt.next(), null));
 		board1.getPorts().add(new Port(new Coordinate(3,4), new Coordinate(4,4),resIt.next(), null));
 		board1.getPorts().add(new Port(new Coordinate(-4,0), new Coordinate(-4,-1),resIt.next(), null));
 		board1.getPorts().add(new Port(new Coordinate(5,3), new Coordinate(5,2),resIt.next(), null));
 		board1.getPorts().add(new Port(new Coordinate(-4,-3), new Coordinate(-4,-4),resIt.next(), null));
 		board1.getPorts().add(new Port(new Coordinate(4,0), new Coordinate(3,-1),resIt.next(), null));
 		board1.getPorts().add(new Port(new Coordinate(-3,-5), new Coordinate(-2,-5),resIt.next(), null));
 		board1.getPorts().add(new Port(new Coordinate(1,-3), new Coordinate(0,-4),resIt.next(), null));
 	}
 
 	//this is also just a bit of modularity that shuffles the port types and returns an iterator
 	public static Iterator<String> getPortArrangement() {
 		
 		String[] Ports = { "F", "M","G", "P", "H","?","?","?","?" };
 		ArrayList<String> s1 = new ArrayList<String>();
 		
 		for (int f = 0; f < Ports.length; f++) {
			s1.add(Ports[f]);
 		}
 		
 		Collections.shuffle(s1);
 		Iterator<String> PortSt = s1.iterator();
 		return PortSt;
 	}
 
 	public static void setIllegals(Board board1) {
 		
 		for (int i = 0; i < board1.getBoardLocations().length; i++) {
 			for (int j = 0; j < board1.getBoardLocations()[i].length; j++) {
 				
 				Location thisLoc = board1.getBoardLocations()[i][j];
 				
				if (thisLoc.getType().equals(INTERSECTION)) {
 					Intersection thisint = (Intersection) thisLoc.getContains();
 					ArrayList<Intersection> illegals = new ArrayList<Intersection>();
 
 					Intersection returnedLoc  = isOwned(board1, i, j+1);
 					if (returnedLoc != null) {
 						illegals.add(returnedLoc);
 					}
 
					returnedLoc = isOwned(board1, i, j-1);
 					if (returnedLoc != null) {
 						illegals.add(returnedLoc);
 					}
 
					returnedLoc = isOwned(board1, i+1, j);
 					if (returnedLoc != null) {
 						illegals.add(returnedLoc);
 					}
 
					returnedLoc = isOwned(board1, i-1, j);
 					if (returnedLoc != null) {
 						illegals.add(returnedLoc);
 					}
 
					returnedLoc = isOwned(board1, i+1, j+1);
 					if (returnedLoc != null) {
 						illegals.add(returnedLoc);
 					}
 
					returnedLoc = isOwned(board1, i-1, j-1);
 					if (returnedLoc != null) {
 						illegals.add(returnedLoc);
 					}
					
 					thisint.setIllegal(illegals);
 				}
 			}
 		}
 	}
 
 	//checks if the intersection is owned
 	public static Intersection isOwned(Board board1, int i, int j) {
 		
 		try{
 			Intersection orb = (Intersection) board1.getBoardLocations()[i][j].getContains();
			return orb;
 		}
 		catch(Exception e) {
 		}
		
 		return null;
 	}
 }