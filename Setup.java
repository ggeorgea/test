import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Class contains all the methods to set up the game and board
 */
public class Setup {

	private static final int NO_KNIGHTS = 14;
	private static final int NO_VICTORY_CARDS = 5;
	private static final int NO_RESOURCE = 19;
	
//-----Methods to initialise players and their order for the game-----//
	
	
	//gets an array list of players with unique identifiers
	public static ArrayList<Player> setPlayers(Scanner scanner) {
			
		int n = requestPlayers(scanner);
			
		ArrayList<Player> players = new ArrayList<Player>();
			
		for (int i = 0; i < n; i++) {
				
			Player player = new Player();
				
			selectPlayerName(player, players, i, scanner);
			players.add(player);			
		}
			
		return players;
	}
		
	//asks the client how many players there are
	public static int requestPlayers(Scanner scanner) {
			
		System.out.println("How many players want to play? Enter 3 or 4.");
			
		int noPlayers = scanner.nextInt();
			
		if (!(noPlayers == 3 || noPlayers == 4)) {
			System.out.println("Invalid number of players. Please choose again.");
			return requestPlayers(scanner);
		}
			
		return noPlayers;
	}
		
	//asks each player to choose a unique identifier
	public static void selectPlayerName(Player player, ArrayList<Player> players, int n, Scanner scanner) {
			
		System.out.println("Player " + (n+1) + ": Select a character to be your player name.");
		System.out.println("Select from: W-White, R-Red, G-Green, B-Blue, O-Orange, Y-Yellow");
			
		String name = scanner.next().toUpperCase();
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
			System.out.println("Invalid character. Please choose again.");
			selectPlayerName(player, players, n, scanner);
			return;
		}
			
		for (int i = 0; i < players.size(); i++) {
							
			if (players.get(i).getName().equals(check)) {
				System.out.println("Another player is already using this character. Please choose again.");
				selectPlayerName(player, players, n, scanner);
				return;
			}
		}
			
		player.setName(check);
	}
	
	//gets each player to roll the dice to determine the player order
	public static ArrayList<Player> getPlayerOrder(Game game1, Scanner scanner) {
			
		ArrayList<Player> players = game1.getPlayers();
			
		for (int i = 0; i < players.size(); i++) {
				
			Dice.rollDice(players.get(i), scanner);			
		}		
			
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

		System.out.println("Player order: ");
		
		for (int i = 0; i < players.size(); i++) {
				
			System.out.println((i+1)+ ": " + players.get(i).getName());
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
		
		for (int i = 0; i < NO_KNIGHTS; i++) {
				
			DevelopmentCard knight = new DevelopmentCard("knight", true);
			knights.add(knight);
		}
		
		return knights;
	}
		
	//instantiates correct number of progress cards
	public static ArrayList<DevelopmentCard> getProgressCards() {
		
		ArrayList<DevelopmentCard> progress = new ArrayList<DevelopmentCard>();
		int noProgress = 2;
		
		for (int i = 0; i < noProgress; i++) {
				
			DevelopmentCard roadBuilding = new DevelopmentCard("road building", true);
			progress.add(roadBuilding);
		}	
		
		noProgress += 2;
		
		for (int i = 2; i < noProgress; i++) {
			
			DevelopmentCard yearOfPlenty = new DevelopmentCard("year of plenty", true);
			progress.add(yearOfPlenty);
		}
		
		noProgress += 2;
		
		for (int i = 4; i < noProgress; i++) {
			
			DevelopmentCard monopoly = new DevelopmentCard("monopoly", true);
			progress.add(monopoly);
		}
			
		return progress;
	}
		
	//instantiates correct number of victory point cards
	public static ArrayList<DevelopmentCard> getVictoryPointCards() {
			
		ArrayList<DevelopmentCard> victory = new ArrayList<DevelopmentCard>();
		
		for (int i = 0; i < NO_VICTORY_CARDS; i++) {
			
			DevelopmentCard victoryPoint = new DevelopmentCard("victory point", true);
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
	public static void setInitialRoadsAndSettlements(Game game1, Scanner scanner) {
		
		ArrayList<Player> players = game1.getPlayers();
		Board board1 = game1.getBoard();
		
		for (int i = 0; i < players.size(); i++) {
			
			//each player places road, then settlement
			Road road = placeRoad(players.get(i), board1, scanner);
			placeSettlement(players.get(i), road, board1, scanner);
		}
		
		for (int i = players.size()-1; i >= 0; i--) {
			
			//each player places road, then settlement
			Road road = placeRoad(players.get(i), board1, scanner);
			Intersection settlement = placeSettlement(players.get(i), road, board1, scanner);
			initialResourceAllocation(players.get(i), settlement, game1);
		}
		
		game1.setPlayers(players);
		game1.setBoard(board1);
	}
	
	public static boolean checkNear(Board board1, int x1, int y1, int x2, int y2){
		Location loca1 = board1.getLocationFromCoordinate(new Coordinate(x1,y1));
		Location loca2 = board1.getLocationFromCoordinate(new Coordinate(x2,y2));
		if((!(loca1.getType().equals("Intersection")))||(!(loca2.getType().equals("Intersection")))||(!(((Intersection)loca1.getContains()).getOwner().getName()==null))||(!(((Intersection)loca2.getContains()).getOwner().getName()==null))){
			return false;
		}
		return true;
	}
	
	//lets the player place a road free of charge
	//also does not depend on nearby roads
	public static Road placeRoad(Player player, Board board1, Scanner scanner) {
		
		System.out.println("Player " + player.getName() + ": Please select where to place your road");
		
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
		//if (x1 < -4 || x1 > 4 || y1 < -4 || y1 > 4 || x2 < -4 || x2 > 4 || y2 < -4 || y2 > 4) {
		if((!((2*y1 <= x1 +8)||(2*y1>=x1-8)&&(y1<=2*x1+8)&&(y1>=2*x1-8)&&(y1>=-x1-8)&&(y1<=-x1+8)))&&(!((2*y2 <= x2 +8)||(2*y2>=x2-8)&&(y2<=2*x2+8)&&(y2>=2*x2-8)&&(y2>=-x2-8)))){
			System.out.println("Invalid coordinates. Please choose again");
			return placeRoad(player, board1, scanner);
		}
		else if(!checkNear(board1, x1,y1,x2,y2)){
			System.out.println("Invalid coordinates. Please choose again");
			return placeRoad(player, board1, scanner);
		}
		else {
		
			Coordinate a = new Coordinate(x1, y1);
			Coordinate b = new Coordinate(x2, y2);
		
			Road road = board1.getRoadFromCo(a, b);
			
			if (road == null) {
				
				System.out.println("Invalid coordinates. Please choose again");
				return placeRoad(player, board1, scanner);
			}
			else if (road.getOwner().getName() != null) {
				
				System.out.println("A road has already been placed here. Please choose again");
				return placeRoad(player, board1, scanner);
			}
			else {
				
				System.out.println("Player " + player.getName() + " placed road at: (" + x1 + "," + y1 + "),(" + x2 + "," + y2 + ")");
				road.setOwner(player);
		
				player.setNoRoads(player.getNoRoads() + 1);
				
				return road;
			}
		
		}
	}
	
	//TODO needs to be two roads away from another settlement
	
	//lets a player place a settlement free of charge
	//needs to be beside road just placed?
	public static Intersection placeSettlement(Player player, Road road, Board board1, Scanner scanner) {
		
		System.out.println("Player " + player.getName() + ": Please select where to place your settlement");
		
		System.out.println("Select X coordinate");
		int x = scanner.nextInt();
		
		System.out.println("Select Y coordinate");
		int y = scanner.nextInt();
		Coordinate a = new Coordinate(x, y);

		//checks the coordinates are in the correct range
		if((!((2*y <= x +8)||(2*y>=x-8)&&(y<=2*x+8)&&(y>=2*x-8)&&(y>=-x-8)&&(y<=-x+8)))
				||
				(!(board1.getLocationFromCoordinate(a).getType().equals("Intersection")))
				){
			
			System.out.println("Invalid coordinates. Please choose again");
			return placeSettlement(player, road, board1, scanner);
		}
		
		
		Intersection settlement = (Intersection) board1.getLocationFromCoordinate(a).getContains();
		ArrayList<Intersection> illegal = settlement.getIllegal();
		
		if (!(road.getCoordinateA().getX() == x && road.getCoordinateA().getY() == y) 
				&& !(road.getCoordinateB().getX() == x && road.getCoordinateB().getY() == y)) {
			
			System.out.println("Settlement must be placed beside road. Please choose again");
			return placeSettlement(player, road, board1, scanner);
		}
			
		if (settlement.getOwner().getName() != null) {
			
			System.out.println("A settlement has already been placed here. Please choose again");
			return placeSettlement(player, road, board1, scanner);
		}
		
		for (int i = 0; i < illegal.size(); i++) {
			
			Intersection inter = illegal.get(i);
			//System.out.println("A"+illegal.get(i).getCoordinate().getX()+","+illegal.get(i).getCoordinate().getY());
			if (inter.getOwner().getName() != null) {
				System.out.println("Settlement must be placed more than two roads away. Please choose again");
				return placeSettlement(player, road, board1, scanner);
			}
		}
		
		System.out.println("Player " + player.getName() + ""
				+ " placed settlement at: (" + x + "," + y + ")");
		settlement.setOwner(player);
		settlement.setBuilding(new Building("t",1));
		player.getFirstSettlements().add(settlement);
		player.setNoSettlements(player.getNoSettlements() + 1);
		return settlement;
	}
	
	//TODO doesn't work yet: need to find the nearby hexes
	//gets the resources based on the placement of the second settlement for the player
	public static void initialResourceAllocation(Player player, Intersection settlement, Game game1) {
		
		//gets the type of each hex bordering the intersection
		ArrayList<Hex> nearbyHexes = new ArrayList<Hex>();
		
		//adds one of each type of resource card to the players hand
		
		ArrayList<ResourceCard> resourceCards = player.getResourceCards();
		
		for (int i = 0; i < nearbyHexes.size(); i++) {
			
			String terrain = nearbyHexes.get(i).getTerrain();
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
				
				resourceCards.add(card);
			}
		}
		
		System.out.println("Player " + player.getName() + " gets: ");
		
		for (int i = 0; i < resourceCards.size(); i++) {
			
			System.out.print("1x " + resourceCards.get(i).getResource() + " ");
		}
		
 		player.setResourceCards(resourceCards);
 		
	}

//-----Methods to get the initialise the board and hexes for the game-----//
	
	//this is the method that adds random terrain from the catan box to each hex, it also calls all the other methods involved in making a board
	public static Board getMeABoard(){
		int Edgesize = 3;
		Board board1 = getHexBoard(Edgesize);

		// this generates a random arrangement of terrain
		int givenDesert = 0;
		Iterator<String> terrainSt = getTerrainArrangement();
		// this fills the arraylist with hexes, with random terrain
		for (int r = 0; r < board1.getHexes().size(); r++) {
			ArrayList<Hex> Hexes = board1.getHexes();
			String terr = terrainSt.next();
			Hex current = Hexes.get(r);
			current.setTerrain(terr);
			//current.setNumber(11);
			Hexes.set(r, current);
			board1.setHexes(Hexes);
			if (terr.equals("D")) {
				givenDesert = r;
			}
		}

		board1 = setRandHexNumbersAndRob(board1, givenDesert);
		setUpLocations(board1);
		makeIntersectionOrdering(board1);
		setRoadArrayAndMap(board1);
		addPorts(board1);
		setIllegals(board1);
		return board1;
	}
	
	//this method adds the hexes,with just coordinates set, to the arraylist in such a way to get them to print out prettily by just iterating through them
	public static Board getHexBoard(int Edgesize) {
		Board board1 = new Board();
		ArrayList<Hex> hexes1 = new ArrayList<Hex>();
		boolean past = false;
		int startx = -(Edgesize - 1);
		int starty = Edgesize - 1;
		int len = Edgesize;
		for (int b = 0; b < (2 * Edgesize) - 1; b++) {
			int xof = 0;
			int yof = 0;
			for (int h = 0; h < len; h++) {
				Hex hexa = new Hex();
				hexa.setCoordinate(new Coordinate(startx + xof, starty + yof));
				//System.out.println((startx+xof)+", "+(starty+yof));
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
			} else {
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
		for (int f = 0; f < terrains.length; f++) {
			s1.add(terrains[f]);
		}
		Collections.shuffle(s1);
		Iterator<String> terrainSt = s1.iterator();
		return terrainSt;
	}
	
	// this method adds a random number from the array of numbers needed on a catan board to each hex, since this happens after terrain allocation, it is also where the desert and the number 7 are matched up, and when the robber is allocated
	public static Board setRandHexNumbersAndRob(Board board1, int givenDesert) {
		int given7 = 0;
		int[] hexnumbers = { 7, 5, 2, 6, 3, 8, 10, 9, 12, 11, 4, 8, 10, 9, 4,
				5, 6, 3, 11 };
		ArrayList<Integer> x1 = new ArrayList<Integer>();
		for (int i = 0; i < hexnumbers.length; i++) {
			x1.add(hexnumbers[i]);
		}
		Collections.shuffle(x1);
		Iterator numberIt = x1.iterator();
		for (int y = 0; y < hexnumbers.length; y++) {
			int toset = (int) numberIt.next();
			Hex hexer = board1.getHexes().get(y);
			hexer.setNumber(toset);
			if (toset == 7) {
				given7 = y;
			}
			board1.getHexes().set(y, hexer);
		}
		Hex hexwDes = board1.getHexes().get(givenDesert);
		Hex hexw7 = board1.getHexes().get(given7);
		int otherNum = hexwDes.getNumber();
		String otherterr = hexw7.getTerrain();
		hexwDes.setNumber(7);
		hexw7.setTerrain(otherterr);
		hexw7.setNumber(otherNum);
		board1.setRobber(hexwDes.getCoordinate());
		hexwDes.setisRobberHere("R");
		board1.getHexes().set(givenDesert, hexwDes);
		board1.getHexes().set(given7, hexw7);
		return board1;
	}
	
	//this method makes a 2d array which is then used for accessing the hexes and intersections by coordinate, it also adds the hexes and the intersections(by calling another method) around the hexes
	public static void setUpLocations(Board board1){
		Location[][] boardLocations = new Location[11][11];
		board1.setBoardLocations(boardLocations);
		for(int xv = 0; xv<11; xv++){
			for(int yv = 0; yv<11; yv++){
				boardLocations[xv][yv]=new Location(new Coordinate(xv-5,yv-5),"empty", null);
			}
		}
		for(int ci = 0; ci<board1.getHexes().size();ci++ ){
			Hex thisHex = board1.getHexes().get(ci);
			int newX = thisHex.getCoordinate().getX()+5;
			int newY = thisHex.getCoordinate().getY()+5;
			boardLocations[newX][newY].setContains(thisHex);
			boardLocations[newX][newY].setType("hex");
			for(int rl = 0; rl < 6; rl++){
				switch(rl){
					case 0: setUpInter(newX+1,newY, boardLocations, board1);
						break;
					case 1: setUpInter(newX,newY+1, boardLocations, board1);
						break;
					case 2: setUpInter(newX+1,newY+1, boardLocations, board1);
						break;
					case 3: setUpInter(newX-1,newY-1, boardLocations, board1);
						break;
					case 4: setUpInter(newX-1,newY, boardLocations, board1);
						break;
					case 5: setUpInter(newX,newY-1, boardLocations, board1);
						break;
				}
			}
		}
	}
	
	//this method takes up an informal coordinate, and 2d array and adds a new intersection in the specified place
	public static void setUpInter(int x, int y,  Location[][] boardLocations, Board board1){
		if (boardLocations[x][y].getType().equals("empty")){
			Player noPlayer = new Player();
			Building noBuild = new Building();
			noBuild.setType(" ");
			noPlayer.setName(null);
			Intersection newIn = new Intersection(new Coordinate(x-5,y-5),noPlayer,noBuild);
			boardLocations[x][y].setContains(newIn);
			boardLocations[x][y].setType("Intersection");			
		}
	}
	
	//this method adds the intersections from the 2d array into our intersections arraylist for printing etc.. the tricky looking bits here are just part of an insertion sort to get the ordering right in a moderately fast, yet simple way
	public static void makeIntersectionOrdering(Board board1){		
		ArrayList<Intersection> interestionsal  = new ArrayList<Intersection>(); 
		for(int xval = 0; xval<board1.getBoardLocations().length;xval++){
			for(int yval = 0; yval<board1.getBoardLocations().length; yval++){
				if (board1.getBoardLocations()[xval][yval].getType().equals("Intersection")){
					if(interestionsal.size()==0){interestionsal.add((Intersection) board1.getBoardLocations()[xval][yval].getContains());}
					for (int i = 0; i < interestionsal.size(); i++) {
						Coordinate Cvalue = interestionsal.get(i).getCoordinate();
						int cVal = 2*(Cvalue.getY()+5)-(Cvalue.getX()+5);
						int opVal = 2*yval-xval;
						if((opVal>cVal)||((opVal==cVal)&&(xval<Cvalue.getX()))||(i+1==interestionsal.size())){
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
	public static void setRoadArrayAndMap(Board board1){
		ArrayList<Road> roads = new ArrayList<Road>();
		HashMap roadmap = makeRoadHashMap(board1);
		ArrayList<Intersection> intersections = board1.getBuildings();
		//check the 8 coords nearby to see if they have road in the hashmap
		for(int thit = 0; thit < intersections.size();thit++){
			int x = intersections.get(thit).getCoordinate().getX();
			int y = intersections.get(thit).getCoordinate().getY();
			Coordinate[] nearby = new Coordinate[6];
			nearby[0]=new Coordinate(x,y+1);
			nearby[1]=new Coordinate(x-1,y);
			nearby[2]=new Coordinate(x+1,y+1);
			nearby[3]=new Coordinate(x-1,y-1);
			nearby[4]=new Coordinate(x,y-1);
			nearby[5]=new Coordinate(x+1,y);
			for(int nean = 0; nean<6; nean++){
				
				String keys = new StringBuilder().append(x).append(y).append(nearby[nean].getX()).append(nearby[nean].getY()).toString();
				if(roadmap.containsKey(keys)){
				
					if(!roads.contains(roadmap.get(keys))){
						roads.add((Road)roadmap.get(keys));
					}
				}
			}
		}
		board1.setRoads(roads);
	}
	
	//this creates a hashmap that maps from coordinate pairs to roads, it does this basically by just making six for each hex (checking if each road has already been made)
	public static HashMap makeRoadHashMap(Board board1){
		HashMap roadmap = new HashMap();
		for(int hin = 0; hin<board1.getHexes().size();hin++){
			Hex currHex = board1.getHexes().get(hin);
			int x = currHex.getCoordinate().getX();
			int y = currHex.getCoordinate().getY();
			putNewRoad(x,y,1,1,1,0,roadmap);
			putNewRoad(x,y,1,0,0,-1,roadmap);
			putNewRoad(x,y,0,1,1,1,roadmap);
			putNewRoad(x,y,0,1,-1,0,roadmap);
			putNewRoad(x,y,-1,0,0-1,-1,roadmap);
			putNewRoad(x,y,-1,-1,0,-1,roadmap);
			for(int sid = 0; sid<6; sid++){
				
			}	 
		}
		board1.setRoadmap(roadmap);
		return roadmap;
	}
	
	
	//this method takes coordinates for a hex, and four offsets, two for each end of the road, and then adds a road to the hashmap if it doesnt exist already
	public static void putNewRoad(int x, int y ,int off1, int off2, int off3, int off4, HashMap roadmap){
		String keys = new StringBuilder().append(x+off1).append(y+off2).append(x+off3).append(y+off4).toString();
		if(!roadmap.containsKey(keys)){
			Player noPlater = new Player();
			noPlater.setName(null);
			roadmap.put(keys, new Road( new Coordinate(x+off1,y+off2),new Coordinate(x+off3,y+off4), noPlater));
		}
	}
	
	//this method adds the ports to an arrraylist in a way in which they can be printed nicely
	public static void addPorts(Board board1){
		Iterator<String> resIt = getPortArrangement();
		ArrayList<Port> ports = new ArrayList<Port>();
		board1.getPorts().add(new Port(new Coordinate(0,4),new Coordinate(1,4),resIt.next()));
		board1.getPorts().add(new Port(new Coordinate(-2,3),new Coordinate(-3,2),resIt.next()));
		board1.getPorts().add(new Port(new Coordinate(3,4),new Coordinate(4,4),resIt.next()));
		board1.getPorts().add(new Port(new Coordinate(-4,0),new Coordinate(-4,-1),resIt.next()));
		board1.getPorts().add(new Port(new Coordinate(5,3),new Coordinate(5,2),resIt.next()));
		board1.getPorts().add(new Port(new Coordinate(-4,-3),new Coordinate(-4,-4),resIt.next()));
		board1.getPorts().add(new Port(new Coordinate(4,0),new Coordinate(3,-1),resIt.next()));
		board1.getPorts().add(new Port(new Coordinate(-3,-5),new Coordinate(-2,-5),resIt.next()));
		board1.getPorts().add(new Port(new Coordinate(1,-3),new Coordinate(0,-4),resIt.next()));
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
	
	public static void setIllegals(Board board1){
		for(int j = 0; j< board1.getBoardLocations().length;j++){
			for(int g = 0; g<board1.getBoardLocations()[j].length;g++){
				Location thisLoc = board1.getBoardLocations()[j][g];
				if(thisLoc.getType().equals("Intersection")){
					Intersection thisint = (Intersection) thisLoc.getContains();
					ArrayList<Intersection> illegals = new ArrayList<Intersection>();

					Intersection returnedLoc  = isOwned(board1,j,g+1);
					if(returnedLoc!=null){
						illegals.add(returnedLoc);
					}
					
					 returnedLoc = isOwned(board1,j,g-1);
					if(returnedLoc!=null){
						illegals.add(returnedLoc);
					}
					
					 returnedLoc = isOwned(board1,j+1,g);
					if(returnedLoc!=null){
						illegals.add(returnedLoc);
					}
					
					 returnedLoc = isOwned(board1,j-1,g);
					if(returnedLoc!=null){
						illegals.add(returnedLoc);
					}
					
//					 returnedLoc = isOwned(board1,j+1,g-1);
//					if(returnedLoc!=null){
//						illegals.add(returnedLoc);
//					}
					
					 returnedLoc = isOwned(board1,j+1,g+1);
					if(returnedLoc!=null){
						illegals.add(returnedLoc);
					}
					
					 returnedLoc = isOwned(board1,j-1,g-1);
					if(returnedLoc!=null){
						illegals.add(returnedLoc);
					}
					thisint.setIllegal(illegals);

					
				}
			}
		}
	}
	
	public static Intersection isOwned(Board board1, int j, int g){
		try{
			Intersection orb = (Intersection) board1.getBoardLocations()[j][g].getContains();
				return orb;
		}
		catch(Exception e){
		}
		return null;
	}
}