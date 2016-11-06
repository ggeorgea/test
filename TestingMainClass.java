
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
		
		
		System.out.println(""
				+ "                                                \n"
				+ "              /''''\\     /''''\\     /''''\\        \n"
				+ "             /      \\   /      \\   /      \\       \n"
				+ "             |      |   |  "+testHex2.getTerrain()+"   |   |      |       \n" 
				+ "             |      |   |      |   |      |       \n"
				+ "             \\      /   \\      /   \\      /       \n"
				+ "              \\..../     \\..../     \\..../        \n"
				+ "                                                    \n"
				+ "          /''''\\     /''''\\     /''''\\     /''''\\    \n"
				+ "         /      \\   /      \\   /      \\   /      \\    \n"
				+ "         |      |   |      |   |      |   |      |    \n"
				+ "         |      |   |      |   |      |   |      |    \n"
				+ "         \\      /   \\      /   \\      /   \\      /    \n"
				+ "          \\..../     \\..../     \\..../     \\..../     \n"
				+ "                                                          \n"
				+ "     /''''\\     /''''\\     /''''\\     /''''\\     /''''\\     \n"
				+ "    /      \\   /      \\   /      \\   /      \\   /      \\     \n"
				+ "    |      |   |      |   |  "+testHex1.getTerrain()+"   |   |  "+testHex3.getTerrain()+"   |   |      |     \n"
				+ "    |      |   |      |   |      |   |      |   |      |     \n"
				+ "    \\      /   \\      /   \\      /   \\      /   \\      /     \n"
				+ "     \\..../     \\..../     \\..../     \\..../     \\..../     \n"
				+ "                                                          \n"
				+ "          /''''\\     /''''\\     /''''\\     /''''\\      \n"
				+ "         /      \\   /      \\   /      \\   /      \\     \n"
				+ "         |      |   |      |   |      |   |      |     \n"
				+ "         |      |   |      |   |      |   |      |     \n"
				+ "         \\      /   \\      /   \\      /   \\      /    \n"
				+ "          \\..../     \\..../     \\..../     \\..../    \n"
				+ "                                                   \n"
				+ "              /''''\\     /''''\\     /''''\\     \n"
				+ "             /      \\   /      \\   /      \\    \n"
				+ "             |      |   |      |   |      |    \n"
				+ "             |      |   |      |   |      |    \n"
				+ "             \\      /   \\      /   \\      /    \n"
				+ "              \\..../     \\..../     \\..../    \n"
				+ "                                            \n" 
				+ "                                          \n");

	}

}
