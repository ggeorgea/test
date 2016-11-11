import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class TestingMainClass {

	public static void main(String[] args) {
		Board board1 = getMeABoard(3);
		

		Game game1 = new Game();

		game1.setBoard(board1);
		// this prints the board
		
		
		Hex centre =   (Hex) board1.getBoardLocations()[5][5].getContains();
		centre.setisRobberHere("!");
		//along the top
		for (int hi = 0; hi<board1.getBoardLocations().length  ;hi++){
			int chi = hi;
			for (int wi = 0; wi<=hi;wi++){
				//System.out.print("("+(10-chi) +", "+(10-wi) +")");
				System.out.print(board1.getBoardLocations()[10-chi][10-wi].getType()+"  ");
				chi--;
			}
			System.out.println();
		}
		for (int hi = board1.getBoardLocations().length-2; hi>=0  ;hi--){
			int chi = hi;
			for (int wi = 0; wi<=hi;wi++){
				//System.out.print("("+(10-chi) +", "+(10-wi) +")");
				System.out.print(board1.getBoardLocations()[chi][wi].getType()+"  ");
				chi--;
			}
			System.out.println();
		}
		
		
		printMap(game1.getBoard());

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
		// these must be replaced to iterate through roads and cities

		int roadOwn = 1;
		String townOrCityOwn = "3t";
		String getPort = "P";
		//Below is a printout of the board 
		System.out
				.println(
						"                                         "+getPort+"                           \n"                                                                                    
								+"                    "+townOrCityOwn+"            "+townOrCityOwn+"   /        "+townOrCityOwn+"                                      \n"
								+"             "+getPort+"   "+ roadOwn +"  /\\  "+ roadOwn +"      "+ roadOwn +"  /\\  "+ roadOwn +"      "+ roadOwn +"  /\\  "+ roadOwn +"        \n"
								+"               \\   /  \\          /  \\          /  \\          \n"
								+"                  /    \\        /    \\        /    \\          \n"
								+"             "+townOrCityOwn+"  / -2,2 \\  "+townOrCityOwn+"  /  0,3 \\  "+townOrCityOwn+"  /  2,4 \\  "+townOrCityOwn+"     \n" 
								+"                |    "+board1.getHexes().get(rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |        \n"
								+"                |        |    |        |    |        |          \n"
								+"              "+ roadOwn +" |    "+board1.getHexes().get(tn).getTerrain()+"   |  "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +"       \n"
								+"                |        |    |        |    |        |          \n"
								+"                |    "+board1.getHexes().get(nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |          \n"
								+"             "+townOrCityOwn+"  \\      /  "+townOrCityOwn+"  \\      /  "+townOrCityOwn+"  \\      /  "+townOrCityOwn+"       \n"
								+"                  \\    /        \\    /        \\    /             \n"
								+"          "+ roadOwn +"  /\\  "+ roadOwn +" \\  / "+ roadOwn +"  /\\  "+ roadOwn +" \\  / "+ roadOwn +"  /\\  "+ roadOwn +" \\  / "+ roadOwn +"  /\\  "+ roadOwn +"    "+getPort+"      \n"
								+"            /  \\    \\/    /  \\    \\/    /  \\    \\/    /  \\    /    \n"
								+"           /    \\        /    \\        /    \\        /    \\       \n"
								+"      "+townOrCityOwn+"  / -3,0 \\  "+townOrCityOwn+"  / -1,1 \\  "+townOrCityOwn+"  /  1,2 \\  "+townOrCityOwn+"  /  3,3 \\  "+townOrCityOwn+"   \n"
								+"         |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |        \n"
								+"         |        |    |        |    |        |    |        |         \n"
								+"   "+getPort+" - "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +"      \n"
								+"         |        |    |        |    |        |    |        |          \n"
								+"         |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |         \n"
								+"      "+townOrCityOwn+"  \\      /  "+townOrCityOwn+"  \\      /   "+townOrCityOwn+" \\      /  "+townOrCityOwn+"  \\      /  "+townOrCityOwn+"        \n"
								+"           \\    /        \\    /        \\    /        \\    /               \n"
								+"   "+ roadOwn +"  /\\  "+ roadOwn +" \\  / "+ roadOwn +"  /\\  "+ roadOwn +" \\  /  "+ roadOwn +" /\\  "+ roadOwn +" \\  / "+ roadOwn +"  /\\  "+ roadOwn +" \\  / "+ roadOwn +"  /\\  "+ roadOwn +"          \n"
								+"     /  \\    \\/    /  \\    \\/    /  \\    \\/    /  \\    \\/    /  \\              \n"
								+"    /    \\        /    \\        /    \\        /    \\        /    \\             \n"
								+townOrCityOwn+" /-4,-2 \\  "+townOrCityOwn+"  /-2,-1 \\  "+townOrCityOwn+"  /  0,0 \\  "+townOrCityOwn+"  /  2,1 \\  "+townOrCityOwn+"  /  4,2 \\  "+townOrCityOwn+"        \n"
								+"  |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |           \n"
								+"  |        |    |        |    |        |    |        |    |        |           \n"
								+"1 |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +" - "+getPort+"      \n"
								+"  |        |    |        |    |        |    |        |    |        |           \n"
								+"  |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |           \n"
								+townOrCityOwn+" \\      /  "+townOrCityOwn+"  \\      /  "+townOrCityOwn+"  \\      /  "+townOrCityOwn+"  \\      /  "+townOrCityOwn+"  \\      /  "+townOrCityOwn+"       \n"
								+"    \\    /        \\    /        \\    /        \\    /        \\    /          \n"    
								+"   "+ roadOwn +" \\  /  "+ roadOwn +" /\\  "+ roadOwn +" \\  /  "+ roadOwn +" /\\  "+ roadOwn +" \\  /  "+ roadOwn +" /\\  "+ roadOwn +" \\  /  "+ roadOwn +" /\\  "+ roadOwn +" \\  /  "+ roadOwn +"       \n"
								+"      \\/    /  \\    \\/    /  \\    \\/    /  \\    \\/    /  \\    \\/         \n"
								+"           /    \\        /    \\        /    \\        /    \\             \n"
								+"       "+townOrCityOwn+" /-3,-3 \\  "+townOrCityOwn+"  /-1,-2 \\   "+townOrCityOwn+" / 1,-1 \\  "+townOrCityOwn+"  /  3,0 \\  "+townOrCityOwn+"        \n"
								+"         |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |         \n"
								+"         |        |    |        |    |        |    |        |        \n"
								+"   "+getPort+" - "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +"     \n"
								+"         |        |    |        |    |        |    |        |        \n"
								+"         |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |     \n"
								+"       "+townOrCityOwn+" \\      /  "+townOrCityOwn+"  \\      /  "+townOrCityOwn+"  \\      /  "+townOrCityOwn+"  \\      /  "+townOrCityOwn+"   \n"      
								+"           \\    /        \\    /        \\    /        \\    /         \n" 
								+"          "+ roadOwn +" \\  / "+ roadOwn +"  /\\  "+ roadOwn +" \\  / "+ roadOwn +"  /\\  "+ roadOwn +" \\  / "+ roadOwn +"  /\\  "+ roadOwn +" \\  /  "+ roadOwn +"     \n"
								+"             \\/    /  \\    \\/    /  \\    \\/    /  \\    \\/  \\       \n"
								+"                  /    \\        /    \\        /    \\         "+getPort+"          \n"
								+"             "+townOrCityOwn+"  /-2,-4 \\  "+townOrCityOwn+"  / 0,-3 \\  "+townOrCityOwn+"  / 2,-2 \\  "+townOrCityOwn+"    \n"
								+"                |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRbberHere()+"   |      \n"
								+"                |        |    |        |    |        |      \n"
								+"              "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+ roadOwn +"   \n"
								+"                |        |    |        |    |        |      \n"
								+"                |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |      \n"
								+"             "+townOrCityOwn+"  \\      /  "+townOrCityOwn+"  \\      /  "+townOrCityOwn+"  \\      /  "+townOrCityOwn+"   \n"
								+"                  \\    /        \\    /        \\    /       \n"
								+"                "+ roadOwn +"  \\  /  "+ roadOwn +"     "+ roadOwn +" \\  /  "+ roadOwn +"     "+ roadOwn +" \\  /  "+ roadOwn +"     \n"
								+"                 /  \\/            \\/ \\          \\/        \n"
								+"                "+getPort+"   "+townOrCityOwn+"            "+townOrCityOwn+"   "+getPort+"        "+townOrCityOwn+"                   \n"
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
