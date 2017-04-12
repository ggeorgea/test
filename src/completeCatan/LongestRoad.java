package completeCatan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class that contains the method to calculate a 
 * player's longest road
 */
public class LongestRoad {
	
	static int id = 0;
	static boolean loop = false;
	
	//checks if the player has the longest road
	public static void checkPlayerLongestRoad(Player player, Game game1, Road road1) {

		//the unique id for each node visit that takes place
		id = 0;
		loop = false;

		Board board1 = game1.getBoard();
		
		int currLongest = player.getLongestRoad(); //old longest road
		int longest = 0;                           //longest road found from this point

		//this is one end of the road
		Intersection intA = (Intersection) game1.getBoard()
				.getLocationFromCoordinate(road1.getCoordinateA())
				.getContains();
		
		//this is the other
		Intersection intB = (Intersection) game1.getBoard()
				.getLocationFromCoordinate(road1.getCoordinateB())
				.getContains();

		Road end1A = null;
		Road end1B = null;
		Road end2A = null;
		Road end2B = null;
		
		// this sets end1A to be the first new road coming out of intA that is not the road placed (i.e. that does not go to intB)
		end1A = getRoadFromInt(game1, intA, player, intB, null);
		
		// if there was such a road, this looks for another road, to see if there are two roads coming out of that same end
		if (end1A != null) {
			end1B = getRoadFromInt(game1, intA, player, intB, getOtherInt(board1, end1A, intA));
		}
		
		//this sets end2a to be the first new road coming out of the other end (intB) that doesnt go to intA
		end2A = getRoadFromInt(game1, intB, player, intA, null);
		
		// if there was such a road, this looks for another road, to see if there are two roads coming out of that same end
		if (end2A != null) {
			end2B = getRoadFromInt(game1, intB, player, intA, getOtherInt(board1, end2A, intB));
		}

		// connecting, i.e. if this road links to other  roads together
		if ((end1A != null || end1B != null) && (end2A != null || end2B != null)) {
			
			id = 0;
			
			//this maps a point to a sector for the purpose of finding overlap
			HashMap<Intersection, String> sectorMap = new HashMap<Intersection, String>();
					
			//END 1
			int longests1 = 0;
			
			HashMap<Intersection, Integer> distancesMaps1 = new HashMap<Intersection, Integer>();
			HashMap<Intersection, ArrayList<Integer>> visitorsMaps1 = new HashMap<Intersection, ArrayList<Integer>>();
			ArrayList<Integer> namesLists1 = new ArrayList<Integer>();
			
			namesLists1.add(new Integer(0));
			namesLists1.add(new Integer(-1));
			
			ArrayList<Integer> idArray1s1 = new ArrayList<Integer>();
			ArrayList<Integer> idArray2s1 = new ArrayList<Integer>();
			
			idArray1s1.add(new Integer(id));
			idArray2s1.add(new Integer(id - 1));
			visitorsMaps1.put(intB, idArray1s1);

			longests1 = Branch(game1, player, intA, intB, road1,
					(ArrayList<Integer>) namesLists1.clone(),  1,
					distancesMaps1, visitorsMaps1, "s1", sectorMap);
						
			// END2
			int longests2 = 0;
			
			HashMap<Intersection, Integer> distancesMaps2 = new HashMap<Intersection, Integer>();
			HashMap<Intersection, ArrayList<Integer>> visitorsMaps2 = new HashMap<Intersection, ArrayList<Integer>>();
			ArrayList<Integer> namesLists2 = new ArrayList<Integer>();
			
			namesLists2.add(new Integer(id));
			namesLists2.add(new Integer(id-1));

			ArrayList<Integer> idArray1s2 = new ArrayList<Integer>();
			ArrayList<Integer> idArray2s2 = new ArrayList<Integer>();
			
			idArray1s2.add(new Integer(id));
			idArray2s2.add(new Integer(id-1 ));
			visitorsMaps2.put(intA, idArray1s2);
			
			longests2 = Branch(game1, player, intB, intA, road1,
					(ArrayList<Integer>) namesLists2.clone(),  1,
					distancesMaps2, visitorsMaps2, "s2", sectorMap);

			//deciding on what to return
			if (loop) {
				
				if (longests2 > longests1) {
					longest = longests2;
				} 
				else {
					longest = longests1;
				}
			} 
			else {
				longest = -1 + longests1 + longests2;
			}

		} 
		
		//this is what happens if only one end connected to anything, this works!
		else {
			
			// setup
			id = 0;
			
			//this hashmap will record the distances to various points, using points as keys, longest distances as values, will update with longer distances
			HashMap<Intersection, Integer> distancesMap = new HashMap<Intersection, Integer>();
			
			//this hashmap will map points to an arraylist of ints
			HashMap<Intersection, ArrayList<Integer>> visitorsMap = new HashMap<Intersection, ArrayList<Integer>>();
			
			//this arraylist will be cloned to represent all the things that have been visited by a route, all routes have visited 0 and -1 (the two start intersections)
			ArrayList<Integer> namesList = new ArrayList<Integer>();
			
			namesList.add(new Integer(0));
			namesList.add(new Integer(-1));
			
			// end 1 is not empty, end 2 is this means we  will be branching intA
			if ((end1A != null || end1B != null)) {							
				
				// this adds the ids of the starting points to the visitorsmap hashmap
				//each node gets its own arraylist, which is why we get two, of course 
				//these array lists should not really be added to
				ArrayList<Integer> idArray1 = new ArrayList<Integer>();
				ArrayList<Integer> idArray2 = new ArrayList<Integer>();
				
				idArray1.add(new Integer(id));
				idArray2.add(new Integer(id - 1));
				visitorsMap.put(intB, idArray1);
				visitorsMap.put(intA, idArray2);
				
				//the starting length of the branch coming out of end1A, this includes the original road
				int toplen = 1;
				
				//the starting length of the branch coming out of end1B
				int botLen = 1;
				
				// looking at the first branch possibility
				if (end1A != null) {
					
					//branch using the road end1A fro intA to the intersection
					//that road end1A leads to, and moving the length to two 
					toplen = Branch(game1, player, getOtherInt(board1, end1A, intA), intA, end1A,
							(ArrayList<Integer>) namesList.clone(), toplen + 1,
							distancesMap, visitorsMap, null, null);
				}
				
				// looking at the other branch possibility
				if (end1B != null) {
					
					toplen = Branch(game1, player, getOtherInt(board1, end1B, intA), intA, end1B,
							(ArrayList<Integer>) namesList.clone(), botLen + 1,
							distancesMap, visitorsMap, null, null);
				}
				
				// getting which one of those is better!
				if (toplen > botLen) {
					longest = toplen;
				} 
				else {
					longest = botLen;
				}
			} 
			
			//end 2 is empty
			else {
				
				id = 0;
				
				// putting the ids in a map, getting them the right way
				// around(pointless?)
				ArrayList<Integer> idArray1 = new ArrayList<Integer>();
				ArrayList<Integer> idArray2 = new ArrayList<Integer>();
				
				idArray1.add(new Integer(id));
				idArray2.add(new Integer(id - 1));
				visitorsMap.put(intA, idArray1);
				visitorsMap.put(intB, idArray2);
				
				// length of each branch+initial road
				int toplen = 1;
				int botLen = 1;
				
				// looking at the first branch possibility road end2A from intersection intB to a new int
				if (end2A != null) {
					
					System.out.println("A");
					toplen = Branch(game1, player, getOtherInt(board1, end2A, intB), intB, end2A,
							(ArrayList<Integer>) namesList.clone(), toplen + 1,
							distancesMap, visitorsMap, null, null);
				}
				
				// looking at the other branch possibility
				if (end2B != null) {
					
					System.out.println("B");
					botLen = Branch(game1, player, getOtherInt(board1, end2B, intB), intB, end2B,
							(ArrayList<Integer>) namesList.clone(), botLen + 1,
							distancesMap, visitorsMap, null, null);
				}
				
				// getting which one of those is better!
				if (toplen > botLen) {
					longest = toplen;
				} 
				else {
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
			int currdist, HashMap<Intersection, Integer> distancesMap, HashMap<Intersection, ArrayList<Integer>> visitorsMap,
			String sector, HashMap<Intersection, String> sectorMap) {
		
		//this is code for when the built road connected two other roads
		if (sector != null) {
			
			//this only needs to trigger once to let the program know that a crossover has occured
			//this gets the first branch to come across this place's sector id
			String thisSector = (String) sectorMap.get(pointInt);
			
			//if null we are the first and add ourselves
			if (thisSector == null) {
				sectorMap.put(pointInt, sector);
			} 
			
			//otherwise if it a different sector, we have a loop!
			else if (!thisSector.equals(sector)) {
				loop = true;
			}
		}
		
		// some testing code!
		for (int p = 1; p < currdist; p++) {
			System.out.print("   ");
		}
		
		System.out.print("branching to " + pointInt.getCoordinate().getX()
				+ "," + pointInt.getCoordinate().getY() + " from "
				+ fromInt.getCoordinate().getX() + ","
				+ fromInt.getCoordinate().getY() + "\n");
		//end of testing code!
		
		int thisId = ++id;
		
		//adds a new id to the idarray carried with this branch head
		idArray.add(thisId);
		
		//this is an iterator over the id array so we can see who has visited the point
		Iterator<Integer> it = idArray.iterator();
		boolean visited = false;
		
		//this gets the integer array stored in the visitorsMap hashMap for the point in question
		ArrayList<Integer> visitors = (ArrayList<Integer>) visitorsMap.get(pointInt);
		
		//if this is the first time this has been visited, all is well. 
		//but we must add our id to a new idarraay for this place
		if (visitors == null) {
			
			ArrayList<Integer> newVisitors = new ArrayList<Integer>();
			newVisitors.add(new Integer(thisId));
			visitorsMap.put(pointInt, newVisitors);
		} 
		else {
			
			//we must check every id that this head has ever been against every id that has ever visited the node
			while (it.hasNext()) {
				
				int myId = ((Integer) it.next()).intValue();
				Iterator<Integer> vIt = visitors.iterator();
				
				while (vIt.hasNext()) {
					
					Integer Vnext = (Integer) vIt.next();
					
					//all obvious except !(loop&& (Vnext.intValue()!=-1)) which is there 
					//because if there is a loop we no longer count the original road as a part!
					if (myId == (Vnext).intValue()) {
						visited = true;
						break;
					}
				}
				if (visited == true) {
					break;
				}
			}
		}
		
		//we cannot go over the same spot twice so we return now
		//THIS MIGHT NEED TO CHANGE AS WE MAY WELL BE ABLE TO AS LONG AS WE CAN FIND SOMEWHERE UNVISITED TO GO NEXT!
		//ACTUALLY IT WONT BECAUSE EVERY NODE HAS ONLY 3 BRANCHES, and any other visitor must have exited with one!
		if (visited) {
			
			System.out.println("returning due to loop: "+currdist);
			return currdist;
		}
		
		Integer hMapDist = (Integer) distancesMap.get(pointInt);
		
		if (hMapDist == null) {
			
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
	
	//returns the other intersection of a road where one intersection is given as a parameter
	//WORKS
	public static Intersection getOtherInt(Board board1, Road road1, Intersection int1) {
		
		Intersection endA = (Intersection) board1.getLocationFromCoordinate(
				road1.getCoordinateA()).getContains();
		Intersection endB = (Intersection) board1.getLocationFromCoordinate(
				road1.getCoordinateB()).getContains();
		
		if (endA.equals(int1)) {
			return endB;
		} 
		else {
			return endA;
		}
	}
	
	//returns a road from an intersection, in the game an intersection given, belonging to the player give, that does not go to either of the two other intersections specified
	//WORKS	
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

			return startRoad1;
		}
		
		// not working
		startRoad1 = game1.getBoard().getRoadFromCo(sett1.getCoordinate(),
				new Coordinate(j - 1, g));

		if ((startRoad1 != null && startRoad1.getOwner().equals(player))
				&& ((int1Ill == null) || !int1Ill.equals((Intersection) game1
						.getBoard()
						.getLocationFromCoordinate(new Coordinate(j - 1, g))
						.getContains()))
				&& ((int2Ill == null) || !int2Ill.equals((Intersection) game1
						.getBoard()
						.getLocationFromCoordinate(new Coordinate(j - 1, g))
						.getContains()))) {

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
			
			return startRoad1;
		}
		
		return null;
	}
}
