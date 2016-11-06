import java.util.ArrayList;
import java.util.Iterator;


public class TestingMainClass {

	public static void main(String[] args) {

		
		
		Hex testHex1 = new Hex();
		Hex testHex2 = new Hex();
		Hex testHex3 = new Hex();
		testHex1.setCoordinate(new Coordinate(0,0));
		testHex1.setTerrain("D");
		testHex2.setCoordinate(new Coordinate(0,3));
		testHex2.setTerrain("F");
		testHex3.setCoordinate(new Coordinate(2,1));
		testHex3.setTerrain("M");	

		Board board1 = getHexBoard(3);
		//this fills the arraylist with hexes
		for(int r = 0; r<board1.getHexes().size();r++){
			ArrayList<Hex> Hexes =  board1.getHexes();
			Hex current =  Hexes.get(r);
			current.setTerrain("M");
			Hexes.set(r, current);
			board1.setHexes(Hexes);
		}
		
		printMap(board1);

	}
	
	public static void printMap(Board board1){

	Iterator<Hex> anIt = board1.getHexes().iterator();
	//this big print out is gonna get very complicated, it does look quite nice as ascii
	//i suggest we lose any semblance that we can keep it looking neat as ascii, and instead try to make it neat as code!
	System.out.println(""
			
			+ "                                                \n"
			+ "              /''''\\     /''''\\     /''''\\        \n"
			+ "             / -2,2 \\   / 0,3  \\   / 2,4  \\       \n"
			+ "             |  "
			+anIt.next().getTerrain()
			+"   |   |  "
			+anIt.next().getTerrain()
			+"   |   |  "
			+anIt.next().getTerrain()
			+"   |       \n" 
			+ "             |      |   |      |   |      |       \n"
			+ "             \\      /   \\      /   \\      /       \n"
			+ "              \\..../     \\..../     \\..../        \n"
			+ "                                                    \n"
			+ "          /''''\\     /''''\\     /''''\\     /''''\\    \n"
			+ "         / -3,0 \\   / -1,1 \\   / 1,2  \\   / 3,3  \\    \n"
			+ "         |  "
			+anIt.next().getTerrain()
			+"   |   |  "
			+anIt.next().getTerrain()
			+"   |   |  "
			+anIt.next().getTerrain()
			+"   |   |  "
			+anIt.next().getTerrain()
			+"   |    \n"
			+ "         |      |   |      |   |      |   |      |    \n"
			+ "         \\      /   \\      /   \\      /   \\      /    \n"
			+ "          \\..../     \\..../     \\..../     \\..../     \n"
			+ "                                                          \n"
			+ "     /''''\\     /''''\\     /''''\\     /''''\\     /''''\\     \n"
			+ "    / -4,2 \\   /-2,-1 \\   / 0,0  \\   /  2,1 \\   /  4,2 \\     \n"
			+ "    | "
			+anIt.next().getTerrain()
			+"    |   |  "
			+anIt.next().getTerrain()
			+"   |   | "
			+anIt.next().getTerrain()
			+"    |   | "
			+anIt.next().getTerrain()
			+"    |   | "
			+anIt.next().getTerrain()
			+"    |     \n"
			+ "    |      |   |      |   |      |   |      |   |      |     \n"
			+ "    \\      /   \\      /   \\      /   \\      /   \\      /     \n"
			+ "     \\..../     \\..../     \\..../     \\..../     \\..../     \n"
			+ "                                                          \n"
			+ "          /''''\\     /''''\\     /''''\\     /''''\\      \n"
			+ "         /-3,-3 \\   /-1,-2 \\   / 1,-1 \\   / 3,0  \\     \n"
			+ "         |  "
			+anIt.next().getTerrain()
			+"   |   |  "
			+anIt.next().getTerrain()
			+"   |   |  "
			+anIt.next().getTerrain()
			+"   |   |  "
			+anIt.next().getTerrain()
			+"   |     \n"
			+ "         |      |   |      |   |      |   |      |     \n"
			+ "         \\      /   \\      /   \\      /   \\      /    \n"
			+ "          \\..../     \\..../     \\..../     \\..../    \n"
			+ "                                                   \n"
			+ "              /''''\\     /''''\\     /''''\\     \n"
			+ "             /-2,-4 \\   / 0,-3 \\   / 2,-2 \\    \n"
			+ "             | "
			+anIt.next().getTerrain()
			+"    |   |  "
			+anIt.next().getTerrain()
			+"   |   |  "
			+anIt.next().getTerrain()
			+"   |    \n"
			+ "             |      |   |      |   |      |    \n"
			+ "             \\      /   \\      /   \\      /    \n"
			+ "              \\..../     \\..../     \\..../    \n"
			+ "                                            \n" 
			+ "                                          \n");
	}
	
	
	
	//This will make hexes and fill in the coordinates as long as the board is a hexagon
	public static Board getHexBoard(int Edgesize){
	Board board1 = new Board();
	ArrayList<Hex> hexes1 = new ArrayList<Hex>();
	boolean past = false;
	int startx = -(Edgesize-1);
	int starty = Edgesize -1;
	int len = Edgesize;
	for(int b = 0; b<(2*Edgesize)-1; b++){
		int xof = 0;
		int yof = 0;
		for (int h = 0; h< len; h++){
			Hex hexa = new Hex();
			hexa.setCoordinate(new Coordinate(startx+xof,starty+yof));
			if (((startx+xof) == 0) && ((starty + yof) ==0)){
				past = true;
			}
			xof +=2;
			yof+=1;
			hexes1.add(hexa);
		}
		if (!past){
			startx -= 1; 
			starty -= 2;
			len ++;
		}
		else{
			startx ++;
			starty --;
			len--;
			}
	}
	board1.setHexes(hexes1);
	return board1;
	}
}