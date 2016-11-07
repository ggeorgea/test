import java.util.ArrayList;
import java.util.Iterator;

public class TestingMainClass {

	public static void main(String[] args) {

		Hex testHex1 = new Hex();
		Hex testHex2 = new Hex();
		Hex testHex3 = new Hex();
		testHex1.setCoordinate(new Coordinate(0, 0));
		testHex1.setTerrain("D");
		testHex2.setCoordinate(new Coordinate(0, 3));
		testHex2.setTerrain("F");
		testHex3.setCoordinate(new Coordinate(2, 1));
		testHex3.setTerrain("M");

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

		printMap(board1);

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

		// Iterator<Hex> anIt = board1.getHexes().iterator();
		// this big print out is gonna get very complicated, it does look quite
		// nice as ascii
		// i suggest we lose any semblance that we can keep it looking neat as
		// ascii, and instead try to make it neat as code!
		String robberloc = "R";
		int tn = 0;
		int nn = 0;
		String townOrCityOwn = "3t";
		int roadOwn = 1;
		System.out
				.println("                                                            \n"
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
						+ robberloc
						+ "   |    |    "
						+ robberloc
						+ "   |    |    "
						+ robberloc
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
						+ board1.getHexes().get(nn).getNumber()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getNumber()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getNumber()
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
						+ "          / -3,0 \\      / -1,1 \\      /  1,2 \\      /  3,3 \\       \n"
						+ "         |    "
						+ robberloc
						+ "   |    |    "
						+ robberloc
						+ "   |    |    "
						+ robberloc
						+ "   |    |    "
						+ robberloc
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
						+ board1.getHexes().get(++nn).getNumber()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getNumber()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getNumber()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getNumber()
						+ "  |         \n"
						+ "          \\      /      \\      /      \\      /      \\      /            \n"
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
						+ "   /-4,-2 \\  "
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
						+ robberloc
						+ "   |    |    "
						+ robberloc
						+ "   |    |    "
						+ robberloc
						+ "   |    |    "
						+ robberloc
						+ "   |    |    "
						+ robberloc
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
						+ board1.getHexes().get(++nn).getNumber()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getNumber()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getNumber()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getNumber()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getNumber()
						+ "  |           \n"
						+ "   \\      /  "
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
						+ "          /-3,-3 \\      /-1,-2 \\      / 1,-1 \\      /  3,0 \\            \n"
						+ "         |    "
						+ robberloc
						+ "   |    |    "
						+ robberloc
						+ "   |    |    "
						+ robberloc
						+ "   |    |    "
						+ robberloc
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
						+ board1.getHexes().get(++nn).getNumber()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getNumber()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getNumber()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getNumber()
						+ "  |     \n"
						+ "          \\      /      \\      /      \\      /      \\      /       \n"
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
						+ robberloc
						+ "   |    |    "
						+ robberloc
						+ "   |    |    "
						+ robberloc
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
						+ board1.getHexes().get(++nn).getNumber()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getNumber()
						+ "  |    |    "
						+ board1.getHexes().get(++nn).getNumber()
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
						+ "                                                         \n");

	}

}
