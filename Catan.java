import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class that contains the main method for the game
 */
public class Catan {

	private static final boolean END_GAME = true;

	public static void main(String[] args) throws Exception {
		Board board1 = new Board();
		/*
		 * //GENERAL //the above methods set up a board, with roads,
		 * intersections and hexes, all set up in the right places, with random
		 * terrains and probabilities //the ways these items on the board should
		 * be interacted with are as follows: //(there is a 2d array that can be
		 * interacted with directly, but the x and y coordinates are plus five
		 * each, so i suggest we stick to these to maintain some modularity)
		 * //(and the road hashmap called roadmap needs to be accessed with the
		 * coordinates in a specific order, the method I suggest here just
		 * adjusts input so it is in the correct order)
		 * 
		 * //ROADS //firstly, for roads, use the getRoadFromCo method //this
		 * methods takes two coordinates and returns a road or null that links
		 * those coordinates, null if there is no road //that road can be
		 * modified by modifying the object returned of course //(if you wish to
		 * replace the road with another, using the setRoadFromCo method, you
		 * should iterate through the ArrayList and replace the old road there
		 * too!) //for example: Player player1 = new Player();
		 * player1.setName("!"); Road road1 = board1.getRoadFromCo(new
		 * Coordinate(1,0),new Coordinate(1,1)); road1.setOwner(player1); //as
		 * you can now see, the road on the right of the central hex, now is
		 * owned by the player whose name is "!"
		 * 
		 * //INTERSECTIONS //for intersections, use the
		 * getLocationFromCoordinate method, that takes a single coordinate
		 * //this is a little more tricky as the location class that this
		 * returns actually has three things in it, a type, that should say
		 * "Intersection", if your coordinates are correct //and the thing that
		 * you want which is the contains field, so cast the contains field of
		 * the returned object to an intersection and modify that //for example
		 * Intersection inter1 = (Intersection)
		 * board1.getLocationFromCoordinate(new Coordinate(1,0)).getContains();
		 * inter1.setOwner(player1); //as you can see, the intersection in the
		 * bottom right of the central hex is now owned by player1
		 * 
		 * //HEXES //to get the hex at a point on the board you do pretty much
		 * the same thing as an intersection //the type of location should be
		 * "hex", if there is nothing in that coordinate, it should be "empty"
		 * by the way //for example Hex hex1 = (Hex)
		 * board1.getLocationFromCoordinate(new Coordinate(0,0)).getContains();
		 * hex1.setisRobberHere("R"); //it should now appear that there is also
		 * a robber on the central hex! //of course if I wasnt being lazy there
		 * would be seperate methods for hexes and intersections that did the
		 * casting for you or something, but im not sure thats even better
		 */

		boolean keepPlaying = true;

		// will let the players play another game if they wish
		while (keepPlaying) {
			System.out.println("----------SETTLERS OF CATAN----------\n\n");
			Scanner scanner = new Scanner(new File("./src/test.txt"));

			// sets board
			board1 = Setup.getMeABoard();
			Game game1 = new Game();
			game1.setBoard(board1);

			Map.printMap(game1.getBoard());

			// sets development cards
			ArrayList<DevelopmentCard> developmentCards = Setup
					.getDevCardDeck();
			game1.setDevelopmentCards(developmentCards);

			// sets resource cards
			ArrayList<ResourceCard> ore = Setup.getResourceCardDeck("ore");
			ArrayList<ResourceCard> grain = Setup.getResourceCardDeck("grain");
			ArrayList<ResourceCard> lumber = Setup
					.getResourceCardDeck("lumber");
			ArrayList<ResourceCard> wool = Setup.getResourceCardDeck("wool");
			ArrayList<ResourceCard> brick = Setup.getResourceCardDeck("brick");

			game1.setOre(ore);
			game1.setGrain(grain);
			game1.setLumber(lumber);
			game1.setWool(wool);
			game1.setBrick(brick);

			// sets players
			ArrayList<Player> players = Setup.setPlayers(scanner);
			game1.setPlayers(players);

			// roll dice for each player
			// changes player order with largest dice roll first
			Setup.getPlayerOrder(game1, scanner);

			// place roads and settlements
			Setup.setInitialRoadsAndSettlements(game1, scanner);

			// pass from automated set up to actually playing the game
			System.out.println("-----now in manual mode-------");
			scanner = new Scanner(System.in);

			boolean hasEnded = !END_GAME;

			// will keep letting players take turns until someone wins
			while (!hasEnded) {
				for (int i = 0; i < game1.getPlayers().size(); i++) {

					// lets the player have a turn
					hasEnded = Turn.newTurn(game1.getPlayers().get(i), game1,
							scanner);

					// if a player has won then no other player takes their turn
					if (hasEnded) {
						break;
					}
				}
			}

			keepPlaying = playAgain(scanner);
		}

		System.out.println("Goodbye!");

	}

	// //this finds the players first couple of settlemetns
	// ArrayList<Intersection> setts = player.getFirstSettlements();
	// Intersection sett1 = setts.get(0);
	// Intersection sett2 = setts.get(1);
	// //this finds two roads coming out from those settleements, it doesnt
	// matter which
	// Road startRoad1 = getRoadFromInt(game1,sett1,player,null,null);
	// Road startRoad2 = getRoadFromInt(game1,sett2,player,null,null);
	// //this finds the two ends of each of the two roads
	// Intersection r1EndA =(Intersection)
	// game1.getBoard().getLocationFromCoordinate(startRoad1.getCoordinateA()).getContains();
	// Intersection r1EndB =(Intersection)
	// game1.getBoard().getLocationFromCoordinate(startRoad1.getCoordinateB()).getContains();
	// Intersection r2EndA =(Intersection)
	// game1.getBoard().getLocationFromCoordinate(startRoad2.getCoordinateA()).getContains();
	// Intersection r2EndB =(Intersection)
	// game1.getBoard().getLocationFromCoordinate(startRoad2.getCoordinateB()).getContains();
	// // Coordinate r1EndB = startRoad1.getCoordinateB();
	// // Coordinate r2EndA = startRoad2.getCoordinateA();
	// // Coordinate r2EndB = startRoad2.getCoordinateB();
	// //this searches the tree starting at each end of each road
	// int left1 = BeginSearchTree(player,r1EndA,r1EndB,game1);
	// int right1 = BeginSearchTree(player,r1EndB,r1EndA,game1);
	// int left2 = BeginSearchTree(player,r2EndA,r2EndB,game1);
	// int right2 = BeginSearchTree(player,r2EndB,r2EndA,game1);
	// //this makes the two total lengths
	// int first = left1+right1+1;
	// int second = right2+left2+1;
	// //and returns the longsr
	// int longest = 0;
	// if(first>second){
	// longest = first;
	// }
	// else{
	// longest = second;
	// }

	static int id = 0;
	static boolean loop = false;

	public static void CheckPlayerLongestRoad(Player player, Game game1,
			Road road1) {

		// Road roadh = game1.getBoard().getRoads().get(20);
		// System.out.println("this road is owned by "+roadh.getOwner().getName());

		id = 0;

		Board board1 = game1.getBoard();
		int currLongest = player.getLongestRoad();
		int longest = 0;

		Intersection intA = (Intersection) game1.getBoard()
				.getLocationFromCoordinate(road1.getCoordinateA())
				.getContains();
		Intersection intB = (Intersection) game1.getBoard()
				.getLocationFromCoordinate(road1.getCoordinateB())
				.getContains();
		// System.out.println(intA.getCoordinate().getX()+","+intA.getCoordinate().getY()+" is a place");
		// System.out.println(intB.getCoordinate().getX()+","+intB.getCoordinate().getY()+" is a place");

		Road end1A = null;
		Road end1B = null;
		Road end2A = null;
		Road end2B = null;
		end1A = getRoadFromInt(game1, intA, player, intB, null);
		if (end1A != null) {

			end1B = getRoadFromInt(game1, intA, player, intB,
					getOtherInt(board1, end1A, intA));
		}
		end2A = getRoadFromInt(game1, intB, player, intA, null);
		if (end2A != null) {
			end2B = getRoadFromInt(game1, intB, player, intA,
					getOtherInt(board1, end2A, intB));

		}
		// if (end1A==null){System.out.println("end1A Null");}
		// if (end1B==null){System.out.println("end1B Null");}
		// if (end2A==null){System.out.println("end2A Null");}
		// if (end2B==null){System.out.println("end2B Null");}

		// connecting
		if ((end1A != null || end1B != null)
				&& (end2A != null || end2B != null)) {
			// DO SOME COMPLICATED THINGS
			// STARTCOPY
			// setup
			id = 0;
			HashMap sectorMap = new HashMap();

			// END1
			int longests1 = 0;
			HashMap distancesMaps1 = new HashMap();
			HashMap visitorsMaps1 = new HashMap();
			ArrayList<Integer> namesLists1 = new ArrayList();
			namesLists1.add(new Integer(0));
			namesLists1.add(new Integer(-1));
			ArrayList<Integer> idArray1s1 = new ArrayList<Integer>();
			ArrayList<Integer> idArray2s1 = new ArrayList<Integer>();
			idArray1s1.add(new Integer(id));
			idArray2s1.add(new Integer(id - 1));
			visitorsMaps1.put(intB, idArray1s1);
			visitorsMaps1.put(intA, idArray2s1);
			int toplens1 = 0;
			int botLens1 = 0;
			if (end1A != null) {
				toplens1 = Branch(game1, player,
						getOtherInt(board1, end1A, intA), intA, end1A,
						(ArrayList<Integer>) namesLists1.clone(), toplens1 + 1,
						distancesMaps1, visitorsMaps1, "s1", sectorMap);
			}
			if (end1B != null) {
				toplens1 = Branch(game1, player,
						getOtherInt(board1, end1B, intA), intA, end1B,
						(ArrayList<Integer>) namesLists1.clone(), botLens1 + 1,
						distancesMaps1, visitorsMaps1, "s1", sectorMap);
			}
			if (toplens1 > botLens1) {
				longests1 = toplens1;
			}

			// END2
			int longests2 = 0;
			HashMap distancesMaps2 = new HashMap();
			HashMap visitorsMaps2 = new HashMap();
			ArrayList<Integer> namesLists2 = new ArrayList();
			namesLists2.add(new Integer(id));
			namesLists2.add(new Integer(id-1));
			ArrayList<Integer> idArray1s2 = new ArrayList<Integer>();
			ArrayList<Integer> idArray2s2 = new ArrayList<Integer>();
			idArray1s2.add(new Integer(id));
			idArray2s2.add(new Integer(id-1 ));
			visitorsMaps2.put(intA, idArray1s2);
			visitorsMaps2.put(intB, idArray2s2);
			int toplens2 = 0;
			int botLens2 = 0;
			if (end2A != null) {
				toplens2 = Branch(game1, player,
						getOtherInt(board1, end2A, intB), intB, end2A,
						(ArrayList<Integer>) namesLists2.clone(), toplens2 + 1,
						distancesMaps2, visitorsMaps2, "s2", sectorMap);
			}
			if (end2B != null) {
				toplens2 = Branch(game1, player,
						getOtherInt(board1, end2B, intB), intB, end2B,
						(ArrayList<Integer>) namesLists2.clone(), botLens2 + 1,
						distancesMaps2, visitorsMaps2, "s2", sectorMap);
			}
			if (toplens2 > botLens2) {
				longests2 = toplens2;
			}

			if (loop) {
				if (longests2 > longests1) {
					longest = longests2;
				} else {
					longest = longests1;
				}
			} else {
				longest = 1 + longests1 + longests2;
			}

		} else {
			// setup
			id = 0;
			HashMap distancesMap = new HashMap();
			HashMap visitorsMap = new HashMap();
			// //id visited this point
			// ArrayList<Integer> idArray1 = new ArrayList<Integer>();
			// idArray1.add(id);
			// visitorsMap.put(intA, id);
			ArrayList<Integer> namesList = new ArrayList();
			namesList.add(new Integer(0));
			// this wll never be used?
			namesList.add(new Integer(-1));
			// end 1 is empty
			if ((end1A != null || end1B != null)) {
				// putting the ids in a map, getting them the right way
				// around(pointless?)
				ArrayList<Integer> idArray1 = new ArrayList<Integer>();
				ArrayList<Integer> idArray2 = new ArrayList<Integer>();
				idArray1.add(new Integer(id));
				idArray2.add(new Integer(id - 1));
				visitorsMap.put(intB, idArray1);
				visitorsMap.put(intA, idArray2);
				// length of each branch+inital road
				int toplen = 1;
				int botLen = 1;
				// looking at the other branch possibility
				if (end1A != null) {
					// System.out.println("A");
					toplen = Branch(game1, player,
							getOtherInt(board1, end1A, intA), intA, end1A,
							(ArrayList<Integer>) namesList.clone(), toplen + 1,
							distancesMap, visitorsMap, null, null);
				}
				// looking at the other branch possibility
				if (end1B != null) {
					// System.out.println("B");
					toplen = Branch(game1, player,
							getOtherInt(board1, end1B, intA), intA, end1B,
							(ArrayList<Integer>) namesList.clone(), botLen + 1,
							distancesMap, visitorsMap, null, null);
				}
				// getting which one of those is better!
				if (toplen > botLen) {
					longest = toplen;
				} else {
					longest = botLen;
				}
			} else {
				id = 0;
				// putting the ids in a map, getting them the right way
				// around(pointless?)
				ArrayList<Integer> idArray1 = new ArrayList<Integer>();
				ArrayList<Integer> idArray2 = new ArrayList<Integer>();
				idArray1.add(new Integer(id));
				idArray2.add(new Integer(id - 1));
				visitorsMap.put(intA, idArray1);
				visitorsMap.put(intB, idArray2);
				// length of each branch+inital road
				int toplen = 1;
				int botLen = 1;
				// looking at the other branch possibility
				if (end2A != null) {
					System.out.println("A");
					toplen = Branch(game1, player,
							getOtherInt(board1, end2A, intB), intB, end2A,
							(ArrayList<Integer>) namesList.clone(), toplen + 1,
							distancesMap, visitorsMap, null, null);
				}
				// looking at the other branch possibility
				if (end2B != null) {
					System.out.println("B");
					botLen = Branch(game1, player,
							getOtherInt(board1, end2B, intB), intB, end2B,
							(ArrayList<Integer>) namesList.clone(), botLen + 1,
							distancesMap, visitorsMap, null, null);
				}
				// getting which one of those is better!

				if (toplen > botLen) {
					longest = toplen;
				} else {
					longest = botLen;
				}
			}
		}
		if (longest > currLongest) {
			player.setLongestRoad(longest);
		}
		loop = false;
	}

	public static int Branch(Game game1, Player player, Intersection pointInt,
			Intersection fromInt, Road carrying, ArrayList<Integer> idArray,
			int currdist, HashMap distancesMap, HashMap visitorsMap,
			String sector, HashMap sectorMap) {
		if (sector != null) {
			String thisSector = (String) sectorMap.get(pointInt);
			if (thisSector == null) {
				sectorMap.put(pointInt, sector);
			} else if (!thisSector.equals(sector)) {
				loop = true;
			}
		}
		for (int p = 1; p < currdist; p++) {
			System.out.print("   ");
		}
		System.out.print("branching to " + pointInt.getCoordinate().getX()
				+ "," + pointInt.getCoordinate().getY() + " from "
				+ fromInt.getCoordinate().getX() + ","
				+ fromInt.getCoordinate().getY() + "\n");
		int thisId = ++id;
		idArray.add(thisId);
		Iterator it = idArray.iterator();
		boolean visited = false;
		ArrayList<Integer> visitors = (ArrayList<Integer>) visitorsMap
				.get(pointInt);
		if (visitors == null) {
			ArrayList<Integer> newVisitors = new ArrayList<Integer>();
			newVisitors.add(new Integer(thisId));
			visitorsMap.put(pointInt, newVisitors);
		} else {
			while (it.hasNext()) {
				int myId = ((Integer) it.next()).intValue();
				Iterator vIt = visitors.iterator();
				while (vIt.hasNext()) {
					if (myId == ((Integer) vIt.next()).intValue()) {
						visited = true;
						break;
					}
				}
				if (visited == true) {
					break;
				}
			}
		}
		if (visited) {
			return currdist;
		}
		Integer hMapDist = (Integer) distancesMap.get(pointInt);
		// boolean discovered = false;
		if (hMapDist == null) {
			// discovered = true;
			distancesMap.put(pointInt, new Integer(currdist));
			hMapDist = -1;
		}
		if (hMapDist > currdist) {
			return currdist;
		}
		Road option1 = getRoadFromInt(game1, pointInt, player, fromInt, null);
		if (option1 == null) {
			return currdist;
		}
		Road option2 = getRoadFromInt(game1, pointInt, player, fromInt,
				getOtherInt(game1.getBoard(), option1, pointInt));
		// add branch!!!
		int option1Result = Branch(game1, player,
				getOtherInt(game1.getBoard(), option1, pointInt), pointInt,
				option1, (ArrayList<Integer>) idArray.clone(), currdist + 1,
				distancesMap, visitorsMap, sector, sectorMap);
		if (option2 == null) {
			return option1Result;
		}
		int option2Result = Branch(game1, player,
				getOtherInt(game1.getBoard(), option2, pointInt), pointInt,
				option2, (ArrayList<Integer>) idArray.clone(), currdist + 1,
				distancesMap, visitorsMap, sector, sectorMap);
		if (option1Result > option2Result) {
			return option1Result;
		}
		return option2Result;
	}

	public static Intersection getOtherInt(Board board1, Road road1,
			Intersection int1) {
		Intersection endA = (Intersection) board1.getLocationFromCoordinate(
				road1.getCoordinateA()).getContains();
		Intersection endB = (Intersection) board1.getLocationFromCoordinate(
				road1.getCoordinateB()).getContains();
		if (endA.equals(int1)) {
			return endB;
		} else {
			return endA;
		}
	}

	public static Road getRoadFromInt(Game game1, Intersection sett1,
			Player player, Intersection int1Ill, Intersection int2Ill) {
		Road startRoad1 = null;
		int j = sett1.getCoordinate().getX();
		int g = sett1.getCoordinate().getY();
		startRoad1 = game1.getBoard().getRoadFromCo(sett1.getCoordinate(),
				new Coordinate(j, g + 1));
		if ((startRoad1 != null && startRoad1.getOwner().equals(player))
				&& ((int1Ill == null) || !int1Ill.equals((Intersection) game1
						.getBoard()
						.getLocationFromCoordinate(new Coordinate(j, g + 1))
						.getContains()))
				&& ((int2Ill == null) || !int2Ill.equals((Intersection) game1
						.getBoard()
						.getLocationFromCoordinate(new Coordinate(j, g + 1))
						.getContains()))) {
			// System.out.println("returning a road between: "+startRoad1.getCoordinateA().getX()+","+startRoad1.getCoordinateA().getY()+" and "+startRoad1.getCoordinateB().getX()+", "+startRoad1.getCoordinateB().getY());
			return startRoad1;
		}
		startRoad1 = game1.getBoard().getRoadFromCo(sett1.getCoordinate(),
				new Coordinate(j, g - 1));
		if ((startRoad1 != null && startRoad1.getOwner().equals(player))
				&& ((int1Ill == null) || !int1Ill.equals((Intersection) game1
						.getBoard()
						.getLocationFromCoordinate(new Coordinate(j, g - 1))
						.getContains()))
				&& ((int2Ill == null) || !int2Ill.equals((Intersection) game1
						.getBoard()
						.getLocationFromCoordinate(new Coordinate(j, g - 1))
						.getContains()))) {
			// System.out.println("returning a road between: "+startRoad1.getCoordinateA().getX()+","+startRoad1.getCoordinateA().getY()+" and "+startRoad1.getCoordinateB().getX()+", "+startRoad1.getCoordinateB().getY());
			return startRoad1;
		}
		startRoad1 = game1.getBoard().getRoadFromCo(sett1.getCoordinate(),
				new Coordinate(j + 1, g));
		if ((startRoad1 != null && startRoad1.getOwner().equals(player))
				&& ((int1Ill == null) || !int1Ill.equals((Intersection) game1
						.getBoard()
						.getLocationFromCoordinate(new Coordinate(j + 1, g))
						.getContains()))
				&& ((int2Ill == null) || !int2Ill.equals((Intersection) game1
						.getBoard()
						.getLocationFromCoordinate(new Coordinate(j + 1, g))
						.getContains()))) {
			// System.out.println("returning a road between: "+startRoad1.getCoordinateA().getX()+","+startRoad1.getCoordinateA().getY()+" and "+startRoad1.getCoordinateB().getX()+", "+startRoad1.getCoordinateB().getY());

			return startRoad1;
		}
		// not working
		startRoad1 = game1.getBoard().getRoadFromCo(sett1.getCoordinate(),
				new Coordinate(j - 1, g));

		// if((startRoad1!=null)&&startRoad1.getOwner().equals(player)){
		// System.out.println("road:"+startRoad1.getCoordinateA().getX()+","+startRoad1.getCoordinateA().getY()+"  "+startRoad1.getCoordinateB().getX()+","+startRoad1.getCoordinateB().getY());
		// System.out.println(!((int2Ill==null)||int2Ill.equals((Intersection)game1.getBoard().getLocationFromCoordinate(new
		// Coordinate(j-1,g)).getContains())));}

		if ((startRoad1 != null && startRoad1.getOwner().equals(player))
				&& ((int1Ill == null) || !int1Ill.equals((Intersection) game1
						.getBoard()
						.getLocationFromCoordinate(new Coordinate(j - 1, g))
						.getContains()))
				&& ((int2Ill == null) || !int2Ill.equals((Intersection) game1
						.getBoard()
						.getLocationFromCoordinate(new Coordinate(j - 1, g))
						.getContains()))) {
			// System.out.println("returning a road between: "+startRoad1.getCoordinateA().getX()+","+startRoad1.getCoordinateA().getY()+" and "+startRoad1.getCoordinateB().getX()+", "+startRoad1.getCoordinateB().getY());

			return startRoad1;
		}
		// not working
		startRoad1 = game1.getBoard().getRoadFromCo(sett1.getCoordinate(),
				new Coordinate(j + 1, g + 1));
		if ((startRoad1 != null && startRoad1.getOwner().equals(player))
				&& ((int1Ill == null) || !int1Ill
						.equals((Intersection) game1
								.getBoard()
								.getLocationFromCoordinate(
										new Coordinate(j + 1, g + 1))
								.getContains()))
				&& ((int2Ill == null) || !int2Ill
						.equals((Intersection) game1
								.getBoard()
								.getLocationFromCoordinate(
										new Coordinate(j + 1, g + 1))
								.getContains()))) {
			// System.out.println("returning a road between: "+startRoad1.getCoordinateA().getX()+","+startRoad1.getCoordinateA().getY()+" and "+startRoad1.getCoordinateB().getX()+", "+startRoad1.getCoordinateB().getY());
			return startRoad1;
		}
		startRoad1 = game1.getBoard().getRoadFromCo(sett1.getCoordinate(),
				new Coordinate(j - 1, g - 1));

		if ((startRoad1 != null && startRoad1.getOwner().equals(player))
				&& (((int1Ill == null) || !int1Ill
						.equals((Intersection) game1
								.getBoard()
								.getLocationFromCoordinate(
										new Coordinate(j - 1, g - 1))
								.getContains())))
				&& (((int2Ill == null) || !int2Ill
						.equals((Intersection) game1
								.getBoard()
								.getLocationFromCoordinate(
										new Coordinate(j - 1, g - 1))
								.getContains())))) {
			// System.out.println("returning a road between: "+startRoad1.getCoordinateA().getX()+","+startRoad1.getCoordinateA().getY()+" and "+startRoad1.getCoordinateB().getX()+", "+startRoad1.getCoordinateB().getY());
			return startRoad1;
		}
		return null;
	}

	// ATTEMPT 1
	// public static int PlayerLongestRoad(Player player, Game game1){
	// //this finds the players first couple of settlemetns
	// ArrayList<Intersection> setts = player.getFirstSettlements();
	// Intersection sett1 = setts.get(0);
	// Intersection sett2 = setts.get(1);
	// //this finds two roads coming out from those settleements, it doesnt
	// matter which
	// Road startRoad1 = getRoadFromInt(game1,sett1,player,null,null);
	// Road startRoad2 = getRoadFromInt(game1,sett2,player,null,null);
	// //this finds the two ends of each of the two roads
	// Intersection r1EndA =(Intersection)
	// game1.getBoard().getLocationFromCoordinate(startRoad1.getCoordinateA()).getContains();
	// Intersection r1EndB =(Intersection)
	// game1.getBoard().getLocationFromCoordinate(startRoad1.getCoordinateB()).getContains();
	// Intersection r2EndA =(Intersection)
	// game1.getBoard().getLocationFromCoordinate(startRoad2.getCoordinateA()).getContains();
	// Intersection r2EndB =(Intersection)
	// game1.getBoard().getLocationFromCoordinate(startRoad2.getCoordinateB()).getContains();
	// // Coordinate r1EndB = startRoad1.getCoordinateB();
	// // Coordinate r2EndA = startRoad2.getCoordinateA();
	// // Coordinate r2EndB = startRoad2.getCoordinateB();
	// //this searches the tree starting at each end of each road
	// int left1 = BeginSearchTree(player,r1EndA,r1EndB,game1);
	// int right1 = BeginSearchTree(player,r1EndB,r1EndA,game1);
	// int left2 = BeginSearchTree(player,r2EndA,r2EndB,game1);
	// int right2 = BeginSearchTree(player,r2EndB,r2EndA,game1);
	// //this makes the two total lengths
	// int first = left1+right1+1;
	// int second = right2+left2+1;
	// //and returns the longsr
	// if(first>second){
	// return first;
	// }
	// else{
	// return second;
	// }
	// }
	//
	// public static int BeginSearchTree(Player player, Intersection start,
	// Intersection end ,Game game1){
	// //this will represent the distance away from the start?
	// HashMap testMap1 = new HashMap();
	// //testMap.put(Intesection, longestDistance)
	// //this is encoding the inital values, there will need to be special value
	// upon finding those two
	// testMap1.put(start, new Integer(0));
	// testMap1.put(end, new Integer(-1));
	// // HashMap testMapB1= new HashMap();
	// // testMapB1 = (HashMap) testMap1.clone();
	// // HashMap testMapB2= new HashMap();
	// // testMapB2 = (HashMap) testMap1.clone();
	// //
	// //this saves the visitors to each place
	// HashMap testMaplist = new HashMap();
	//
	//
	// //this tries to get two roads branching out of our start point
	// Boolean try1fine = false;
	// //Location locStart = game1.getBoard().getLocationFromCoordinate(start);
	// Road roadBranch1 = getRoadFromInt(game1,start,player,end,null);
	// Intersection try1 = (Intersection)
	// game1.getBoard().getLocationFromCoordinate(roadBranch1.getCoordinateA()).getContains();
	// Intersection try2 = (Intersection)
	// game1.getBoard().getLocationFromCoordinate(roadBranch1.getCoordinateB()).getContains();
	// Road roadBranch2;
	// if(try1.equals(start)){
	// roadBranch2 = getRoadFromInt(game1,start,player,end,try2);
	// }
	// else{
	// roadBranch2 = getRoadFromInt(game1,start,player,end,try1);
	// try1fine = true;
	// }
	// int length1 = 0;
	// int length2 = 0;
	//
	// //this arrayList just tells us all the known ids of this branch
	// ArrayList<Integer> ar1 = new ArrayList<Integer>();
	// ar1.add(new Integer(0));
	// if(roadBranch1!=null){
	// Intersection b1Int;
	// if(try1fine){
	// b1Int = try1;
	// }
	// else{
	// b1Int = try2;
	// }
	// length1 = branch(1, b1Int,
	// roadBranch1,testMap1,player,game1,(ArrayList<Integer>)ar1.clone(),testMaplist);
	// }
	// if(roadBranch2!=null){
	// Intersection b2Int;
	// if(((Intersection)
	// game1.getBoard().getLocationFromCoordinate(roadBranch1.getCoordinateA()).getContains()).equals(start)){
	// b2Int = (Intersection)
	// game1.getBoard().getLocationFromCoordinate(roadBranch1.getCoordinateA()).getContains();
	// }
	// else{
	// b2Int = (Intersection)
	// game1.getBoard().getLocationFromCoordinate(roadBranch1.getCoordinateB()).getContains();
	//
	// }
	//
	//
	// length2 = branch(1,b2Int, roadBranch2,testMap1,player,
	// game1,(ArrayList<Integer>)ar1.clone(),testMaplist);
	// }
	//
	// if (length1>length2){
	// return length1;
	// }
	// else{
	// return length2;
	// }
	//
	// }
	//
	// static int id = 0;
	//
	//
	// public static int branch(int lengthcurr, Intersection bInt, Road
	// road,HashMap testMap1,Player player, Game game1, ArrayList<Integer>
	// clonedNameList,HashMap testMaplist){
	// //testMaplist.get();
	// int headid=++id;
	// ArrayList<Integer> visitors = (ArrayList<Integer>) testMaplist.get(bInt);
	// if (visitors == null){
	// visitors = new ArrayList<Integer>();
	// }
	// visitors.add(id);
	// testMaplist.put(bInt, visitors);
	// Iterator it = clonedNameList.iterator();
	// boolean visited = false;
	// while(it.hasNext()){
	// if(visitors.contains(it.next())){
	// visited = true;
	// }
	// }
	// if (visited==true){
	// return 0;
	// }
	// clonedNameList.add(new Integer(headid));
	// Integer prevLong = (Integer )testMap1.get(bInt);
	// boolean longer = false;
	// if(prevLong !=null && prevLong.intValue()==-1){
	// return 0;
	// }
	// else if(prevLong==null||prevLong.intValue()<lengthcurr )
	// {
	// longer = true;
	// testMap1.put(bInt, new Integer(lengthcurr));
	// }
	//
	// return 1;
	// }
	//
	// public static Road getRoadFromInt(Game game1, Intersection sett1,Player
	// player, Intersection int1Ill, Intersection int2Ill){
	// Road startRoad1 = null;
	// do{
	// int j = sett1.getCoordinate().getX();
	// int g = sett1.getCoordinate().getY();
	// startRoad1 = game1.getBoard().getRoadFromCo(sett1.getCoordinate(), new
	// Coordinate(j,g+1));
	// if((startRoad1!=null&&startRoad1.getOwner().equals(player))
	// &&(!int1Ill.equals(game1.getBoard().getLocationFromCoordinate(new
	// Coordinate(j,g+1)).getContains()))
	// &&(!int2Ill.equals(game1.getBoard().getLocationFromCoordinate(new
	// Coordinate(j,g+1)).getContains()))){
	// break;
	// }
	// startRoad1 = game1.getBoard().getRoadFromCo(sett1.getCoordinate(), new
	// Coordinate(j,g-1));
	// if((startRoad1!=null&&startRoad1.getOwner().equals(player))
	// &&(!int1Ill.equals(game1.getBoard().getLocationFromCoordinate(new
	// Coordinate(j,g-1)).getContains()))
	// &&(!int2Ill.equals(game1.getBoard().getLocationFromCoordinate(new
	// Coordinate(j,g-1)).getContains()))){ break;
	// }
	// startRoad1 = game1.getBoard().getRoadFromCo(sett1.getCoordinate(), new
	// Coordinate(j+1,g));
	// if((startRoad1!=null&&startRoad1.getOwner().equals(player))
	// &&!(int1Ill.equals(game1.getBoard().getLocationFromCoordinate(new
	// Coordinate(j+1,g)).getContains()))
	// &&!(int2Ill.equals(game1.getBoard().getLocationFromCoordinate(new
	// Coordinate(j+1,g)).getContains()))){ break;
	// }
	// startRoad1 = game1.getBoard().getRoadFromCo(sett1.getCoordinate(), new
	// Coordinate(j-1,g));
	// if((startRoad1!=null&&startRoad1.getOwner().equals(player))
	// &&!(int1Ill.equals(game1.getBoard().getLocationFromCoordinate(new
	// Coordinate(j-1,g)).getContains()))
	// &&!(int2Ill.equals(game1.getBoard().getLocationFromCoordinate(new
	// Coordinate(j-1,g)).getContains()))){ break;
	// }
	// startRoad1 = game1.getBoard().getRoadFromCo(sett1.getCoordinate(), new
	// Coordinate(j+1,g+1));
	// if((startRoad1!=null&&startRoad1.getOwner().equals(player))
	// &&!(int1Ill.equals(game1.getBoard().getLocationFromCoordinate(new
	// Coordinate(j+1,g+1)).getContains()))
	// &&!(int2Ill.equals(game1.getBoard().getLocationFromCoordinate(new
	// Coordinate(j+1,g+1)).getContains()))){ break;
	// }
	// startRoad1 = game1.getBoard().getRoadFromCo(sett1.getCoordinate(), new
	// Coordinate(j-1,g-1));
	// if((startRoad1!=null&&startRoad1.getOwner().equals(player))
	// &&!(int1Ill.equals(game1.getBoard().getLocationFromCoordinate(new
	// Coordinate(j-1,g-1)).getContains()))
	// &&!(int2Ill.equals(game1.getBoard().getLocationFromCoordinate(new
	// Coordinate(j-1,g-1)).getContains()))){ break;
	// }
	// }while(false);
	// return startRoad1;
	// }
	// /*
	//
	// boolean foundSuitible = false;
	// do{
	// Road tryRoad;
	// int j = settlement.getCoordinate().getX();
	// int g = settlement.getCoordinate().getY();
	// tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new
	// Coordinate(j,g+1));
	// if(tryRoad!=null&&tryRoad.getOwner().equals(player)){
	// foundSuitible = true;
	// break;
	// }
	// tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new
	// Coordinate(j,g-1));
	// if(tryRoad!=null&&tryRoad.getOwner().equals(player)){
	// foundSuitible = true;
	// break;
	// }
	// tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new
	// Coordinate(j+1,g));
	// if(tryRoad!=null&&tryRoad.getOwner().equals(player)){
	// foundSuitible = true;
	// break;
	// }
	// tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new
	// Coordinate(j-1,g));
	// if(tryRoad!=null&&tryRoad.getOwner().equals(player)){
	// foundSuitible = true;
	// break;
	// }
	// tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new
	// Coordinate(j+1,g+1));
	// if(tryRoad!=null&&tryRoad.getOwner().equals(player)){
	// foundSuitible = true;
	// break;
	// }
	// tryRoad = game1.getBoard().getRoadFromCo(settlement.getCoordinate(), new
	// Coordinate(j-1,g-1));
	// if(tryRoad!=null&&tryRoad.getOwner().equals(player)){
	// foundSuitible = true;
	// break;
	// }
	// }while(false);
	//
	// if (!foundSuitible) {
	//
	// System.out.println("Settlement must be placed beside one of your roads. "
	// +
	// "Please choose again");
	// buildSettlement(player, game1, scanner);
	// return;
	// }
	//
	//
	// */

	// asks the players if they want to play again
	public static boolean playAgain(Scanner scanner) {

		System.out.println("Do you want to play again?");
		System.out.println("Y or N");

		boolean keepPlaying = false;
		String choice = scanner.next().toUpperCase();
		char c = choice.toCharArray()[0];

		switch (c) {
		case 'Y':
			keepPlaying = true;
			break;
		case 'N':
			keepPlaying = false;
			break;
		default:
			System.out.println("Invalid choice. Please choose again");
			keepPlaying = playAgain(scanner);
		}

		return keepPlaying;
	}

	// ************ Look for the player with the longest road ********
	public static Player longestRoad(Game game1) {
		ArrayList<Player> players = game1.getPlayers();
		// initial longest road to be compared with the players longest road
		int longestRoad = 0;
		Player hasRoad = players.get(0);

		// iterate the list of player and find the longest road
		for (Player p : players) {
			if (p.getLongestRoad() > longestRoad) {
				longestRoad = p.getLongestRoad();
				hasRoad = p;
			}
		}
		hasRoad.setHasLongestRoad(true);
		System.out.println("player" + hasRoad.getName()
				+ "has the longest road.");
		return hasRoad;

	}

}