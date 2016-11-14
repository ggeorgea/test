import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class TestingMainClass {

	public static void main(String[] args) {
				
		Scanner scanner = new Scanner(System.in);
		
		//sets board
		Board board1 = getMeABoard();
		Game game1 = new Game();
		game1.setBoard(board1);
		printMap(game1.getBoard());

		
		//sets development cards
		ArrayList<DevelopmentCard> developmentCards = getDevCardDeck();
		game1.setDevelopmentCards(developmentCards);
		
		//sets resource cards
		ArrayList<ResourceCard> ore = getResourceCardDeck("ore");
		ArrayList<ResourceCard> grain = getResourceCardDeck("grain");
		ArrayList<ResourceCard> lumber = getResourceCardDeck("lumber");
		ArrayList<ResourceCard> wool = getResourceCardDeck("wool");
		ArrayList<ResourceCard> brick = getResourceCardDeck("brick");
		
		game1.setOre(ore);
		game1.setGrain(grain);
		game1.setLumber(lumber);
		game1.setWool(wool);
		game1.setBrick(brick);
		
		//sets players
		ArrayList<Player> players = setPlayers(scanner);
		game1.setPlayers(players);
				
		//roll dice for each player
		//changes player order with largest dice roll first
		getPlayerOrder(game1, scanner);
		
		//place roads and settlements
		setInitialRoadsAndSettlements(game1, scanner);
		
		
		/*
		//GENERAL
		//the above methods set up a board, with roads, intersections and hexes, all set up in the right places, with random terrains and probabilities
		//the ways these items on the board should be interacted with are as follows:
		//(there is a 2d array that can be interacted with directly, but the x and y coordinates are plus five each, so i suggest we stick to these to maintain some modularity)
		//(and the road hashmap called roadmap needs to be accessed with the coordinates in a specific order, the method I suggest here just adjusts input so it is in the correct order)
		
		//ROADS
		//firstly, for roads, use the getRoadFromCo method
		//this methods takes two coordinates and  returns a road or null that links those coordinates, null if there is no road
		//that road can be modified by modifying the object returned of course
		//(if you wish to replace the road with another, using the setRoadFromCo method, you should iterate through the ArrayList and replace the old road there too!)
		//for example:
		Player player1 = new Player();
		player1.setName("!");
		Road road1 = board1.getRoadFromCo(new Coordinate(1,0),new Coordinate(1,1));
		road1.setOwner(player1);
		//as you can now see, the road on the right of the central hex, now is owned by the player whose name is "!"
		
		//INTERSECTIONS
		//for intersections, use the getLocationFromCoordinate method, that takes a single coordinate
		//this is a little more tricky as the location class that this returns actually has three things in it, a type, that should say "Intersection", if your coordinates are correct
		//and the thing that you want which is the contains field, so cast the contains field of the returned object to an intersection and modify that
		//for example
		Intersection inter1 = (Intersection) board1.getLocationFromCoordinate(new Coordinate(1,0)).getContains();
		inter1.setOwner(player1);
		//as you can see, the intersection in the bottom right of the central hex is now owned by player1
		
		//HEXES
		//to get the hex at a point on the board you do pretty much the same thing as an intersection
		//the type of location should be "hex", if there is nothing in that coordinate, it should be "empty" by the way
		//for example
		Hex hex1 = (Hex) board1.getLocationFromCoordinate(new Coordinate(0,0)).getContains();
		hex1.setisRobberHere("R");
		//it should now appear that there is also a robber on the central hex!
		//of course if I wasnt being lazy there would be seperate methods for hexes and intersections that did the casting for you or something, but im not sure thats even better
		*/
		
		printMap(game1.getBoard());

	}
	
	//rolls the two dice
	public static void rollDice(Player player, Scanner scanner) {
		
		Random random = new Random();
		
		System.out.println("Player " + player.getName() + ": press 'R' to roll.");
		String enter = scanner.next().toUpperCase();
		
		if (!(enter.equals("R"))) {
			
			System.out.println("Invalid input. Please roll again.");
			rollDice(player, scanner);
		}
		
		int dice1 = random.nextInt(6) + 1;
		int dice2 = random.nextInt(6) + 1;
		
		System.out.println("Player " + player.getName() + " rolls: " + (dice1+dice2));
		
		player.setCurrentRoll(dice1+dice2);
	}
	
	//gets each player to roll the dice to determine the player order
	public static ArrayList<Player> getPlayerOrder(Game game1, Scanner scanner) {
		
		ArrayList<Player> current = game1.getPlayers();
		
		for (int i = 0; i < current.size(); i++) {
						
			rollDice(current.get(i), scanner);			
		}		
		
		for (int i = 0; i < current.size(); i++) {
			for (int j = 1; j < current.size()-i; j++) {
				
				Player player1 = current.get(j-1);
				Player player2 = current.get(j);
				
				if (player1.getCurrentRoll() < player2.getCurrentRoll()) {
					
					Player temp = player1;
					player1 = player2;
					player2 = temp;
					
					current.set(j-1, player1);
					current.set(j, player2);
					
				}
			}
		}

		System.out.println("Player order: ");
		
		for (int i = 0; i < current.size(); i++) {
			
			System.out.println((i+1)+ ": " + current.get(i).getName());
		}
		
		return current;
	}
	
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
			placeSettlement(players.get(i), road, board1, scanner);
		}
		
		game1.setPlayers(players);
		game1.setBoard(board1);
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
		if (x1 < -4 || x1 > 4 || y1 < -4 || y1 > 4 || x2 < -4 || x2 > 4 || y2 < -4 || y2 > 4) {
			System.out.println("Invalid coordinates. Please choose again");
			placeRoad(player, board1, scanner);
		}
		
		Coordinate a = new Coordinate(x1, y1);
		Coordinate b = new Coordinate(x2, y2);
		
		Road road = board1.getRoadFromCo(a, b);
		
		if (road == null) {
			System.out.println("Invalid coordinates. Please choose again");
			road = placeRoad(player, board1, scanner);
		}
		
		if (road.getOwner().getName() != null) {
			System.out.println("A road has already been placed here. Please choose again");
			road = placeRoad(player, board1, scanner);
		}
		
		System.out.println("Player " + player.getName() + " placed road at: (" + x1 + "," + y1 + "),(" + x2 + "," + y2 + ")");
		road.setOwner(player);
		
		player.setNoRoads(player.getNoRoads() + 1);
		return road;
	}
	
	//TODO needs to be two roads away from another settlement
	
	//lets a player place a settlement free of charge
	//needs to be beside road just placed?
	public static void placeSettlement(Player player, Road road, Board board1, Scanner scanner) {
		
		System.out.println("Player " + player.getName() + ": Please select where to place your settlement");
		
		System.out.println("Select X coordinate");
		int x = scanner.nextInt();
		
		System.out.println("Select Y coordinate");
		int y = scanner.nextInt();
		
		//checks the coordinates are in the correct range
		if (x < -4 || x > 4 || y < -4 || y > 4) {
			System.out.println("Invalid coordinates. Please choose again");
			placeSettlement(player, road, board1, scanner);
		}
		
		Coordinate a = new Coordinate(x, y);
		
		Intersection settlement = (Intersection) board1.getLocationFromCoordinate(a).getContains();
		ArrayList<Intersection> illegal = settlement.getIllegal();
		
		if (!(road.getCoordinateA().getX() == x && road.getCoordinateA().getY() == y) 
				&& !(road.getCoordinateB().getX() == x && road.getCoordinateB().getY() == y)) {
			
			System.out.println("Settlement must be placed beside road. Please choose again");
			placeSettlement(player, road, board1, scanner);
		}
			
		if (settlement.getOwner().getName() != null) {
			System.out.println("A settlement has already been placed here. Please choose again");
			placeSettlement(player, road, board1, scanner);
		}
		
		for (int i = 0; i < illegal.size(); i++) {
			
			Intersection inter = illegal.get(i);
			
			if (inter.getOwner().getName() != null) {
				System.out.println("Settlement must be placed more than two roads away.");
				placeSettlement(player, road, board1, scanner);
			}
		}
		
		System.out.println("Player " + player.getName() + " placed settlement at: (" + x + "," + y + ")");
		settlement.setOwner(player);
		
		player.setNoSettlements(player.getNoSettlements() + 1);
	}
	
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
		
		for (int i = 0; i < 14; i++) {
			
			DevelopmentCard knight = new DevelopmentCard("knight", true);
			knights.add(knight);
		}
		
		return knights;
	}
	
	//instantiates correct number of progress cards
	public static ArrayList<DevelopmentCard> getProgressCards() {
		
		ArrayList<DevelopmentCard> progress = new ArrayList<DevelopmentCard>();
		
		for (int i = 0; i < 2; i++) {
			
			DevelopmentCard roadBuilding = new DevelopmentCard("road building", true);
			progress.add(roadBuilding);
		}
		for (int i = 2; i < 4; i++) {
			
			DevelopmentCard yearOfPlenty = new DevelopmentCard("year of plenty", true);
			progress.add(yearOfPlenty);
		}
		for (int i = 4; i < 6; i++) {
			
			DevelopmentCard monopoly = new DevelopmentCard("monopoly", true);
			progress.add(monopoly);
		}
		
		return progress;
	}
	
	//instantiates correct number of victory point cards
	public static ArrayList<DevelopmentCard> getVictoryPointCards() {
		
		ArrayList<DevelopmentCard> victory = new ArrayList<DevelopmentCard>();
		
		for (int i = 0; i < 5; i++) {
			
			DevelopmentCard victoryPoint = new DevelopmentCard("victory point", true);
			victory.add(victoryPoint);
		}
		
		return victory;
	}
	
	//gets the deck of resource cards
	public static ArrayList<ResourceCard> getResourceCardDeck(String resource) {
		
		ArrayList<ResourceCard> resourceCards = new ArrayList<ResourceCard>();
		
		for (int i = 0; i < 19; i++) {
			
			ResourceCard card = new ResourceCard(resource);
			resourceCards.add(card);
		}
		
		return resourceCards;
	}
	
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
		System.out.println("Select from: !, #, &, %, *, @");
		
		String name = scanner.next();
		char c = name.toCharArray()[0];
		String check = "";
		
		switch(c) {
		case '!' :
			check = "!";
			break;
		case '#' :
			check = "#";
			break;
		case '&' :
			check = "&";
			break;
		case '%' :
			check = "%";
			break;
		case '*' :
			check = "*";
			break;
		case '@' :
			check = "@";
			break;
		default :
			System.out.println("Invalid character. Please choose again.");
			selectPlayerName(player, players, n, scanner);
		}
		
		for (int i = 0; i < players.size(); i++) {
						
			if (players.get(i).getName().equals(check)) {
				
				System.out.println("Another player is already using this character. Please choose again.");
				selectPlayerName(player, players, n, scanner);
			}
		}
		
		player.setName(check);
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
	
	
	//this method makes a 2d array which is then used for accessing the hexes and intersections by coordinate, it also adds the hexes and the intersections(by calling another method) around the hexes
	
	public static void setUpLocations(Board board1){
		location[][] boardLocations = new location[11][11];
		board1.setBoardLocations(boardLocations);
		for(int xv = 0; xv<11; xv++){
			for(int yv = 0; yv<11; yv++){
				boardLocations[xv][yv]=new location(new Coordinate(xv-5,yv-5),"empty", null);
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
	
	public static void setUpInter(int x, int y,  location[][] boardLocations, Board board1){
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
		return board1;
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

	
	//the big thing that prints the map, if this doesnt work, its probably because you were using the set methods that take coordinates
	
	
	public static void printMap(Board board1) {

		// this big print out is gonna get very complicated, it does look quite
		// nice as ascii
		// i suggest we lose any semblance that we can keep it looking neat as
		// ascii, and instead try to make it neat as code!
		// these are just for iterating through the robber fields, the terrain
		// fields and then number fields
		int rn = 0;
		int tn = 0;
		int nn = 0;
		int ton = 0;
		int btn = 0;
		
		// these must be replaced to iterate through roads and cities
		int roadOwn = 0;
		int getPort = 0;

		//String getPort = "P";
		//Below is a printout of the board 
		System.out
		.println(
				"                                         "+board1.getPorts().get(getPort++).getResource()+"                           \n"                                                                                    
						+"                    "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"            "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"   /        "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"                                      \n"
						+"             "+board1.getPorts().get(getPort++).getResource()+"   "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"  /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"      "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"  /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"      "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"  /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"        \n"
						+"               \\   /  \\          /  \\          /  \\          \n"
						+"                  /    \\        /    \\        /    \\          \n"
						+"             "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  / -2,2 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  /  0,3 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  /  2,4 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"     \n" 
						+"                |    "+board1.getHexes().get(rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |        \n"
						+"                |        |    |        |    |        |          \n"
						+"              "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"       \n"
						+"                |        |    |        |    |        |          \n"
						+"                |    "+board1.getHexes().get(nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |          \n"
						+"             "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"       \n"
						+"                  \\    /        \\    /        \\    /             \n"
						+"          "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"  /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  / "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"  /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  / "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"  /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  / "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"  /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"    "+board1.getPorts().get(getPort++).getResource()+"      \n"
						+"            /  \\    \\/    /  \\    \\/    /  \\    \\/    /  \\    /    \n"
						+"           /    \\        /    \\        /    \\        /    \\       \n"
						+"      "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  / -3,0 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  / -1,1 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  /  1,2 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  /  3,3 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"   \n"
						+"         |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |        \n"
						+"         |        |    |        |    |        |    |        |         \n"
						+"   "+board1.getPorts().get(getPort++).getResource()+" - "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"      \n"
						+"         |        |    |        |    |        |    |        |          \n"
						+"         |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |         \n"
						+"      "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /   "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+" \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"        \n"
						+"           \\    /        \\    /        \\    /        \\    /               \n"
						+"   "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"  /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  / "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"  /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  /  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  / "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"  /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  / "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"  /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"          \n"
						+"     /  \\    \\/    /  \\    \\/    /  \\    \\/    /  \\    \\/    /  \\              \n"
						+"    /    \\        /    \\        /    \\        /    \\        /    \\             \n"
						+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+" /-4,-2 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  /-2,-1 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  /  0,0 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  /  2,1 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  /  4,2 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"        \n"
						+"  |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |           \n"
						+"  |        |    |        |    |        |    |        |    |        |           \n"
						+ 
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName())+" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" - "+board1.getPorts().get(getPort++).getResource()+"      \n"
						+"  |        |    |        |    |        |    |        |    |        |           \n"
						+"  |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |           \n"
						+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"       \n"
						+"    \\    /        \\    /        \\    /        \\    /        \\    /          \n"    
						+"   "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  /  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  /  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  /  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  /  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  /  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"       \n"
						+"      \\/    /  \\    \\/    /  \\    \\/    /  \\    \\/    /  \\    \\/         \n"
						+"           /    \\        /    \\        /    \\        /    \\             \n"
						+"       "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+" /-3,-3 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  /-1,-2 \\   "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+" / 1,-1 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  /  3,0 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"        \n"
						+"         |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |         \n"
						+"         |        |    |        |    |        |    |        |        \n"
						+"   "+board1.getPorts().get(getPort++).getResource()+" - "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"     \n"
						+"         |        |    |        |    |        |    |        |        \n"
						+"         |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |     \n"
						+"       "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+" \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"   \n"      
						+"           \\    /        \\    /        \\    /        \\    /         \n" 
						+"          "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  / "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"  /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  / "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"  /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  / "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"  /\\  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  /  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"     \n"
						+"             \\/    /  \\    \\/    /  \\    \\/    /  \\    \\/  \\       \n"
						+"                  /    \\        /    \\        /    \\         "+board1.getPorts().get(getPort++).getResource()+"          \n"
						+"             "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  /-2,-4 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  / 0,-3 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  / 2,-2 \\  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"    \n"
						+"                |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |      \n"
						+"                |        |    |        |    |        |      \n"
						+"              "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"   \n"
						+"                |        |    |        |    |        |      \n"
						+"                |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |      \n"
						+"             "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"   \n"
						+"                  \\    /        \\    /        \\    /       \n"
						+"                "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"  \\  /  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"     "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  /  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"     "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" \\  /  "+  
((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"     \n"
						+"                 /  \\/            \\/ \\          \\/        \n"
						+"                "+board1.getPorts().get(getPort++).getResource()+"   "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"            "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"   "+board1.getPorts().get(getPort++).getResource()+"        "+ 
((board1.getBuildings().get(++ton).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+board1.getBuildings().get(++btn).getBuilding().getType()+"                   \n"

				
				);
		System.out.println("robber is at: "+board1.getRobber().getX()+", "+board1.getRobber().getY());


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
}