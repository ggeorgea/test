import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class TestingMainClass {

	public static void main(String[] args) {

		Board board1 = getHexBoard(3);
		// this fills the arraylist with hexes
		for (int r = 0; r < board1.getHexes().size(); r++) {
			ArrayList<Hex> Hexes = board1.getHexes();
			Hex current = Hexes.get(r);
			current.setTerrain("M");
			current.setNumber(11);
			Hexes.set(r, current);
			board1.setHexes(Hexes);
		}

		Game game1 = new Game();
		board1 = setRandHexNumbersAndRob(board1);
		game1.setBoard(board1);
		//this prints the board
		printMap(game1.getBoard());
	}

	public static Board setRandHexNumbersAndRob(Board board1){
		int[] hexnumbers = {7,5,2,6,3,8,10,9,12,11,4,8,10,9,4,5,6,3,11};
		ArrayList<Integer> x1 = new ArrayList<Integer>();
		for(int i=0; i<hexnumbers.length; i++)
		{
		    x1.add(hexnumbers[i]);
		}
		Collections.shuffle(x1);
		Iterator numberIt = x1.iterator();
		for(int y = 0; y<hexnumbers.length;y++){
			int toset = (int)numberIt.next();
			Hex hexer = board1.getHexes().get(y);
			hexer.setNumber(toset);
			if(toset == 7){
				//this puts the robber on the hex
				hexer.setisRobberHere("R");
			}
			board1.getHexes().set(y, hexer);
		}
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
		//these are just for iterating through the robber fields, the terrain fields and then number fields
		int rn = 0;
		int tn = 0;
		int nn = 0;
		//these must be replaced to iterate through roads and cities
		int roadOwn = 1;
		String townOrCityOwn = "3t";
		System.out
				.println("                    "
						+ townOrCityOwn
						+ "            "
						+ townOrCityOwn
						+ "            "
						+ townOrCityOwn
						+ "                                      \n"
						+ "                 "
						+ roadOwn
						+ "  /\\  "
						+ roadOwn
						+ "      "
						+ roadOwn
						+ "  /\\  "
						+ roadOwn
						+ "      "
						+ roadOwn
						+ "  /\\  "
						+ roadOwn
						+ "        \n"
						+ "                   /  \\          /  \\          /  \\          \n"
						+ "                  /    \\        /    \\        /    \\          \n"
						+ "             "
						+ townOrCityOwn
						+ "  / -2,2 \\  "
						+ townOrCityOwn
						+ "  /  0,3 \\  "
						+ townOrCityOwn
						+ "  /  2,4 \\  "
						+ townOrCityOwn
						+ "     \n"
						+ "                |    "
						+board1.getHexes().get(rn).getisRbberHere()
						+ "   |    |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |    |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |        \n"
						+ "                |        |    |        |    |        |          \n"
						+ "              "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ "       \n"
						+ "                |        |    |        |    |        |          \n"
						+ "                |    "
						+ board1.getHexes().get(nn).getnumString()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |          \n"
						+ "             "
						+ townOrCityOwn
						+ "  \\      /  "
						+ townOrCityOwn
						+ "  \\      /  "
						+ townOrCityOwn
						+ "  \\      /  "
						+ townOrCityOwn
						+ "       \n"
						+ "                  \\    /        \\    /        \\    /             \n"
						+ "          "
						+ roadOwn
						+ "  /\\  "
						+ roadOwn
						+ " \\  / "
						+ roadOwn
						+ "  /\\  "
						+ roadOwn
						+ " \\  / "
						+ roadOwn
						+ "  /\\  "
						+ roadOwn
						+ " \\  / "
						+ roadOwn
						+ "  /\\  "
						+ roadOwn
						+ "      \n"
						+ "            /  \\    \\/    /  \\    \\/    /  \\    \\/    /  \\        \n"
						+ "           /    \\        /    \\        /    \\        /    \\       \n"
						+ "      "
						+ townOrCityOwn
						+ "  / -3,0 \\  "
						+ townOrCityOwn
						+ "  / -1,1 \\  "
						+ townOrCityOwn
						+ "  /  1,2 \\  "
						+ townOrCityOwn
						+ "  /  3,3 \\  "
						+ townOrCityOwn
						+ "   \n"
						+ "         |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |    |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |    |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |    |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |        \n"
						+ "         |        |    |        |    |        |    |        |         \n"
						+ "       "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ "      \n"
						+ "         |        |    |        |    |        |    |        |          \n"
						+ "         |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |         \n"
						+ "      "
						+ townOrCityOwn
						+ "  \\      /  "
						+ townOrCityOwn
						+ "  \\      /   "
						+ townOrCityOwn
						+ " \\      /  "
						+ townOrCityOwn
						+ "  \\      /  "
						+ townOrCityOwn
						+ "        \n"
						+ "           \\    /        \\    /        \\    /        \\    /               \n"
						+ "   "
						+ roadOwn
						+ "  /\\  "
						+ roadOwn
						+ " \\  / "
						+ roadOwn
						+ "  /\\  "
						+ roadOwn
						+ " \\  /  "
						+ roadOwn
						+ " /\\  "
						+ roadOwn
						+ " \\  / "
						+ roadOwn
						+ "  /\\  "
						+ roadOwn
						+ " \\  / "
						+ roadOwn
						+ "  /\\  "
						+ roadOwn
						+ "          \n"
						+ "     /  \\    \\/    /  \\    \\/    /  \\    \\/    /  \\    \\/    /  \\              \n"
						+ "    /    \\        /    \\        /    \\        /    \\        /    \\             \n"
						+ townOrCityOwn
						+ " /-4,-2 \\  "
						+ townOrCityOwn
						+ "  /-2,-1 \\  "
						+ townOrCityOwn
						+ "  /  0,0 \\  "
						+ townOrCityOwn
						+ "  /  2,1 \\  "
						+ townOrCityOwn
						+ "  /  4,2 \\  "
						+ townOrCityOwn
						+ "        \n"
						+ "  |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |    |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |    |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |    |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |    |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |           \n"
						+ "  |        |    |        |    |        |    |        |    |        |           \n"
						+ "1 |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ "      \n"
						+ "  |        |    |        |    |        |    |        |    |        |           \n"
						+ "  |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |           \n"
						+ townOrCityOwn
						+ " \\      /  "
						+ townOrCityOwn
						+ "  \\      /  "
						+ townOrCityOwn
						+ "  \\      /  "
						+ townOrCityOwn
						+ "  \\      /  "
						+ townOrCityOwn
						+ "  \\      /  "
						+ townOrCityOwn
						+ "       \n"
						+ "    \\    /        \\    /        \\    /        \\    /        \\    /          \n"
						+ "   "
						+ roadOwn
						+ " \\  /  "
						+ roadOwn
						+ " /\\  "
						+ roadOwn
						+ " \\  /  "
						+ roadOwn
						+ " /\\  "
						+ roadOwn
						+ " \\  /  "
						+ roadOwn
						+ " /\\  "
						+ roadOwn
						+ " \\  /  "
						+ roadOwn
						+ " /\\  "
						+ roadOwn
						+ " \\  /  "
						+ roadOwn
						+ "       \n"
						+ "      \\/    /  \\    \\/    /  \\    \\/    /  \\    \\/    /  \\    \\/         \n"
						+ "           /    \\        /    \\        /    \\        /    \\             \n"
						+ "       "
						+ townOrCityOwn
						+ " /-3,-3 \\  "
						+ townOrCityOwn
						+ "  /-1,-2 \\   "
						+ townOrCityOwn
						+ " / 1,-1 \\  "
						+ townOrCityOwn
						+ "  /  3,0 \\  "
						+ townOrCityOwn
						+ "        \n"
						+ "         |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |    |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |    |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |    |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |         \n"
						+ "         |        |    |        |    |        |    |        |        \n"
						+ "       "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ "     \n"
						+ "         |        |    |        |    |        |    |        |        \n"
						+ "         |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |     \n"
						+ "       "
						+ townOrCityOwn
						+ " \\      /  "
						+ townOrCityOwn
						+ "  \\      /  "
						+ townOrCityOwn
						+ "  \\      /  "
						+ townOrCityOwn
						+ "  \\      /  "
						+ townOrCityOwn
						+ "   \n"
						+ "           \\    /        \\    /        \\    /        \\    /         \n"
						+ "          "
						+ roadOwn
						+ " \\  / "
						+ roadOwn
						+ "  /\\  "
						+ roadOwn
						+ " \\  / "
						+ roadOwn
						+ "  /\\  "
						+ roadOwn
						+ " \\  / "
						+ roadOwn
						+ "  /\\  "
						+ roadOwn
						+ " \\  /  "
						+ roadOwn
						+ "     \n"
						+ "             \\/    /  \\    \\/    /  \\    \\/    /  \\    \\/       \n"
						+ "                  /    \\        /    \\        /    \\          \n"
						+ "             "
						+ townOrCityOwn
						+ "  /-2,-4 \\  "
						+ townOrCityOwn
						+ "  / 0,-3 \\  "
						+ townOrCityOwn
						+ "  / 2,-2 \\  "
						+ townOrCityOwn
						+ "    \n"
						+ "                |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |    |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |    |    "
						+board1.getHexes().get(++rn).getisRbberHere()
						+ "   |      \n"
						+ "                |        |    |        |    |        |      \n"
						+ "              "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ " |    "
						+ board1.getHexes().get(++tn).getTerrain()
						+ "   |  "
						+ roadOwn
						+ "   \n"
						+ "                |        |    |        |    |        |      \n"
						+ "                |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getnumString()
						+ "  |      \n"
						+ "             "
						+ townOrCityOwn
						+ "  \\      /  "
						+ townOrCityOwn
						+ "  \\      /  "
						+ townOrCityOwn
						+ "  \\      /  "
						+ townOrCityOwn
						+ "   \n"
						+ "                  \\    /        \\    /        \\    /       \n"
						+ "                "
						+ roadOwn
						+ "  \\  /  "
						+ roadOwn
						+ "     "
						+ roadOwn
						+ " \\  /  "
						+ roadOwn
						+ "     "
						+ roadOwn
						+ " \\  /  "
						+ roadOwn
						+ "     \n"
						+ "                    \\/            \\/            \\/        \n"
						+ "                    " + townOrCityOwn
						+ "            " + townOrCityOwn + "            "
						+ townOrCityOwn + "                   \n");

	}

}
