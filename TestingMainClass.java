import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class TestingMainClass {

	public static void main(String[] args) {
		Board board1 = getMeABoard(3);
		Game game1 = new Game();
		game1.setBoard(board1);
		printMap(game1.getBoard());

	}
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
	
	public static void putNewRoad(int x, int y ,int off1, int off2, int off3, int off4, HashMap roadmap){
		String keys = new StringBuilder().append(x+off1).append(y+off2).append(x+off3).append(y+off4).toString();
		if(!roadmap.containsKey(keys)){
			Player noPlater = new Player();
			noPlater.setName(" ");
			roadmap.put(keys, new Road( new Coordinate(x+off1,y+off2),new Coordinate(x+off3,x+off4), noPlater));
		}
	}
	
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
	
	public static void setUpInter(int x, int y,  location[][] boardLocations, Board board1){
		if (boardLocations[x][y].getType().equals("empty")){
			Player noPlayer = new Player();
			Building noBuild = new Building();
			noBuild.setType(" ");
			noPlayer.setName(" ");
			Intersection newIn = new Intersection(new Coordinate(x-5,y-5),noPlayer,noBuild);
			boardLocations[x][y].setContains(newIn);
			boardLocations[x][y].setType("Intersection");
			//System.out.print("("+x+", "+y+") ");
			
		}
	}
	
	public static Board getMeABoard(int Edgesize){
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
		return board1;
	}
	
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
				// this puts the robber on the hex
				// hexer.setisRobberHere("R");
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
		hexwDes.setisRobberHere("R");
		board1.getHexes().set(givenDesert, hexwDes);
		board1.getHexes().set(given7, hexw7);
		return board1;
	}

	// This will make hexes and fill in the coordinates as long as the board is
	// a hexagon
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
		

		String getPort = "P";
		//Below is a printout of the board 
		System.out
				.println(
						"                                         "+getPort+"                           \n"                                                                                    
								+"                    "+board1.getBuildings().get(ton).getOwner().getName()+board1.getBuildings().get(btn).getBuilding().getType()+"            "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"   /        "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"                                      \n"
								+"             "+getPort+"   "+ board1.getRoads().get(roadOwn).getOwner().getName() +"  /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +"      "+ board1.getRoads().get(roadOwn).getOwner().getName() +"  /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +"      "+ board1.getRoads().get(roadOwn).getOwner().getName() +"  /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +"        \n"
								+"               \\   /  \\          /  \\          /  \\          \n"
								+"                  /    \\        /    \\        /    \\          \n"
								+"             "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  / -2,2 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  /  0,3 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  /  2,4 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"     \n" 
								+"                |    "+board1.getHexes().get(rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |        \n"
								+"                |        |    |        |    |        |          \n"
								+"              "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +"       \n"
								+"                |        |    |        |    |        |          \n"
								+"                |    "+board1.getHexes().get(nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |          \n"
								+"             "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"       \n"
								+"                  \\    /        \\    /        \\    /             \n"
								+"          "+ board1.getRoads().get(roadOwn).getOwner().getName() +"  /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  / "+ board1.getRoads().get(roadOwn).getOwner().getName() +"  /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  / "+ board1.getRoads().get(roadOwn).getOwner().getName() +"  /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  / "+ board1.getRoads().get(roadOwn).getOwner().getName() +"  /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +"    "+getPort+"      \n"
								+"            /  \\    \\/    /  \\    \\/    /  \\    \\/    /  \\    /    \n"
								+"           /    \\        /    \\        /    \\        /    \\       \n"
								+"      "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  / -3,0 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  / -1,1 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  /  1,2 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  /  3,3 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"   \n"
								+"         |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |        \n"
								+"         |        |    |        |    |        |    |        |         \n"
								+"   "+getPort+" - "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +"      \n"
								+"         |        |    |        |    |        |    |        |          \n"
								+"         |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |         \n"
								+"      "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /   "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+" \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"        \n"
								+"           \\    /        \\    /        \\    /        \\    /               \n"
								+"   "+ board1.getRoads().get(roadOwn).getOwner().getName() +"  /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  / "+ board1.getRoads().get(roadOwn).getOwner().getName() +"  /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  /  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  / "+ board1.getRoads().get(roadOwn).getOwner().getName() +"  /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  / "+ board1.getRoads().get(roadOwn).getOwner().getName() +"  /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +"          \n"
								+"     /  \\    \\/    /  \\    \\/    /  \\    \\/    /  \\    \\/    /  \\              \n"
								+"    /    \\        /    \\        /    \\        /    \\        /    \\             \n"
								+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+" /-4,-2 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  /-2,-1 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  /  0,0 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  /  2,1 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  /  4,2 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"        \n"
								+"  |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |           \n"
								+"  |        |    |        |    |        |    |        |    |        |           \n"
								+"1 |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" - "+getPort+"      \n"
								+"  |        |    |        |    |        |    |        |    |        |           \n"
								+"  |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |           \n"
								+board1.getBuildings().get(++btn).getBuilding().getType()+" \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"       \n"
								+"    \\    /        \\    /        \\    /        \\    /        \\    /          \n"    
								+"   "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  /  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  /  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  /  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  /  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  /  "+ board1.getRoads().get(roadOwn).getOwner().getName() +"       \n"
								+"      \\/    /  \\    \\/    /  \\    \\/    /  \\    \\/    /  \\    \\/         \n"
								+"           /    \\        /    \\        /    \\        /    \\             \n"
								+"       "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+" /-3,-3 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  /-1,-2 \\   "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+" / 1,-1 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  /  3,0 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"        \n"
								+"         |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |         \n"
								+"         |        |    |        |    |        |    |        |        \n"
								+"   "+getPort+" - "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +"     \n"
								+"         |        |    |        |    |        |    |        |        \n"
								+"         |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |     \n"
								+"       "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+" \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"   \n"      
								+"           \\    /        \\    /        \\    /        \\    /         \n" 
								+"          "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  / "+ board1.getRoads().get(roadOwn).getOwner().getName() +"  /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  / "+ board1.getRoads().get(roadOwn).getOwner().getName() +"  /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  / "+ board1.getRoads().get(roadOwn).getOwner().getName() +"  /\\  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  /  "+ board1.getRoads().get(roadOwn).getOwner().getName() +"     \n"
								+"             \\/    /  \\    \\/    /  \\    \\/    /  \\    \\/  \\       \n"
								+"                  /    \\        /    \\        /    \\         "+getPort+"          \n"
								+"             "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  /-2,-4 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  / 0,-3 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  / 2,-2 \\  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"    \n"
								+"                |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |      \n"
								+"                |        |    |        |    |        |      \n"
								+"              "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ board1.getRoads().get(roadOwn).getOwner().getName() +"   \n"
								+"                |        |    |        |    |        |      \n"
								+"                |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |      \n"
								+"             "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"  \\      /  "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"   \n"
								+"                  \\    /        \\    /        \\    /       \n"
								+"                "+ board1.getRoads().get(roadOwn).getOwner().getName() +"  \\  /  "+ board1.getRoads().get(roadOwn).getOwner().getName() +"     "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  /  "+ board1.getRoads().get(roadOwn).getOwner().getName() +"     "+ board1.getRoads().get(roadOwn).getOwner().getName() +" \\  /  "+ board1.getRoads().get(roadOwn).getOwner().getName() +"     \n"
								+"                 /  \\/            \\/ \\          \\/        \n"
								+"                "+getPort+"   "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"            "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"   "+getPort+"        "+board1.getBuildings().get(++ton).getOwner().getName()+board1.getBuildings().get(++btn).getBuilding().getType()+"                   \n"

						);

	}

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
}





