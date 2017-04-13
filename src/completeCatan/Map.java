package completeCatan;

import java.util.ArrayList;

/**
 * Class that contains the print statement for the Settlers Of Catan Board
 */
public class Map {
	
	//the big thing that prints the map, if this doesn't work, its probably because you were using the set methods that take coordinates
	public static void printMap(Board board1, ArrayList<Player> players) {

		//this big print out is gonna get very complicated, it does look quite
		//nice as ascii
		//i suggest we lose any semblance that we can keep it looking neat as
		//ascii, and instead try to make it neat as code!
		//these are just for iterating through the robber fields, the terrain
		//fields and then number fields
		
		int rn = 0;
		int tn = 0;
		int nn = 0;
		int ton = 0;
		int btn = 0;
		
		//these must be replaced to iterate through roads and cities
		int roadOwn = 0;
		int getPort = 0;
		
		for (int i = 0; i < players.size(); i++) {
			
			 rn = 0;
			 tn = 0;
			 nn = 0;
			 ton = 0;
			 btn = 0;		
			 roadOwn = 0;
			 getPort = 0;

			 
			Catan.printToClient(

					"                                         "+board1.getPorts().get(getPort++).getResource()+"                           \n"                                                                                    
							+"                    "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"            "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"   /        "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"                                      \n"
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
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  / -2,2 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  /  0,3 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  /  2,4 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"     \n" 
							+"                |    "+board1.getHexes().get(rn).getisRobberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |        \n"
							+"                |        |    |        |    |        |          \n"
							+"              "+  
	((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(tn).getTerrain()+"   |  "+  
	((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
	((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
	((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"       \n"
							+"                |        |    |        |    |        |          \n"
							+"                |    "+board1.getHexes().get(nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |          \n"
							+"             "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"       \n"
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
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  / -3,0 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  / -1,1 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  /  1,2 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  /  3,3 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"   \n"
							+"         |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |        \n"
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
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  \\      /   "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+" \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"        \n"
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
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+" /-4,-2 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  /-2,-1 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  /  0,0 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  /  2,1 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  /  4,2 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"        \n"
							+"  |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |           \n"
							+"  |        |    |        |    |        |    |        |    |        |           \n"
							+ 
	((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName())+" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
	((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
	((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
	((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
	((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
	((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" - "+board1.getPorts().get(getPort++).getResource()+"      \n"
							+"  |        |    |        |    |        |    |        |    |        |           \n"
							+"  |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |           \n"+
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+" \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"       \n"
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
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+" /-3,-3 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  /-1,-2 \\   "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+" / 1,-1 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  /  3,0 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"        \n"
							+"         |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |         \n"
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
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+" \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"   \n"      
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
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  /-2,-4 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  / 0,-3 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  / 2,-2 \\  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"    \n"
							+"                |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |    |    "+board1.getHexes().get(++rn).getisRobberHere()+"   |      \n"
							+"                |        |    |        |    |        |      \n"
							+"              "+  
	((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
	((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
	((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +" |    "+board1.getHexes().get(++tn).getTerrain()+"   |  "+  
	((board1.getRoads().get(roadOwn++).getOwner().getName()==null) ? " " : board1.getRoads().get(roadOwn-1).getOwner().getName()) +"   \n"
							+"                |        |    |        |    |        |      \n"
							+"                |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |    |    "+board1.getHexes().get(++nn).getnumString()+"  |      \n"
							+"             "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"  \\      /  "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"   \n"
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
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"            "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"   "+board1.getPorts().get(getPort++).getResource()+"        "+ 
	((board1.getBuildings().get(ton++).getOwner().getName()==null) ? " " : board1.getBuildings().get(ton-1).getOwner().getName())+((board1.getBuildings().get(btn++).getBuilding().getType()==null) ? " " : board1.getBuildings().get(btn-1).getBuilding().getType())+"                   \n"


	            ,players.get(i));

				Catan.printToClient("robber is at: "+ board1.getRobber().getX() + ", " + board1.getRobber().getY() + "\n", players.get(i));
		}
	}
}