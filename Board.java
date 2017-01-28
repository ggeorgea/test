import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that keeps track of the state of the board
 */
public class Board {

	private int size;
	private Coordinate robber;
	private Robber theRobber;
	private ArrayList<Hex> hexes = new ArrayList<Hex>();
	private ArrayList<Port> ports = new ArrayList<Port>();
	private ArrayList<Road> roads = new ArrayList<Road>();
	private ArrayList<Intersection> buildings = new ArrayList<Intersection>();
	
	//this location 2D array is used for getting locations with just coordinates
	private Location[][] boardLocations;
	
	//this hashmap is used for getting roads with their endpoint coordinates
	private HashMap roadmap;
	
//----Constructors----//
	
	public Board() {	
	
	}
	
	public Board(int size, Coordinate robber, ArrayList<Hex> hexes,
			ArrayList<Port> ports, ArrayList<Road> roads,
			ArrayList<Intersection> buildings, Location[][] boardLocations) {
		
		this.size = size;
		this.robber = robber;
		this.hexes = hexes;
		this.ports = ports;
		this.roads = roads;
		this.buildings = buildings;
		this.boardLocations = boardLocations;
	}

//----Getters and Setters----//
	
	public int getSize() {
		
		return size;
	}
	
	public void setSize(int size) {
		
		this.size = size;
	}
	
	public Coordinate getRobber() {
		
		return robber;
	}
	
	public void setRobber(Coordinate robber) {
		
		this.robber = robber;
	}
	
	public Robber getTheRobber() {
		
		return theRobber;
	}

	public void setTheRobber(Robber theRobber) {
		
		this.theRobber = theRobber;
	}
	
	public ArrayList<Hex> getHexes() {
		
		return hexes;
	}
	
	public void setHexes(ArrayList<Hex> hexes) {
		
		this.hexes = hexes;
	}
	
	public ArrayList<Port> getPorts() {
		
		return ports;
	}
	
	public void setPorts(ArrayList<Port> ports) {
		
		this.ports = ports;
	}
	
	public ArrayList<Road> getRoads() {
		
		return roads;
	}
	
	public void setRoads(ArrayList<Road> roads) {
		
		this.roads = roads;
	}
	
	public ArrayList<Intersection> getBuildings() {
		
		return buildings;
	}
	
	public void setBuildings(ArrayList<Intersection> buildings) {
		
		this.buildings = buildings;
	}
	
	public Location[][] getBoardLocations() {
		
		return boardLocations;
	}

	public void setBoardLocations(Location[][] boardLocations) {
		
		this.boardLocations = boardLocations;
	}
	
	public HashMap getRoadmap() {
		
		return roadmap;
	}

	public void setRoadmap(HashMap roadmap) {
		
		this.roadmap = roadmap;
	}
	
//----Methods to ensure that the hash map and the 2D array are accessed properly----//	
	
	//returns a road from the hash map based when given coordinates
	public Road getRoadFromCo(Coordinate coA, Coordinate coB) {
		
		Coordinate cFirst;
		Coordinate cSecond;
		
		if (coA.getY() == coB.getY()) {
			if (coA.getX() < coB.getX()) {
				cFirst = coA;
				cSecond = coB;
			}
			else {
				cFirst = coB;
				cSecond = coA;
			}
		}
		else if (coA.getY() > coB.getY()) {
			cFirst = coA;
			cSecond = coB;
		}
		else {
			cFirst = coB;
			cSecond = coA;
		}
		
		String keys = new StringBuilder().append(cFirst.getX()).append(cFirst.getY()).append(cSecond.getX()).append(cSecond.getY()).toString();
		return (Road) this.getRoadmap().get(keys);
	}
	
	//creates a road at the given coordinates and puts it in the hash map
	public void setRoadFromCo(Road road1, Coordinate coA, Coordinate coB) {
		
		Coordinate cFirst;
		Coordinate cSecond;
		
		if (coA.getY() == coB.getY()) {
			if (coA.getX() < coB.getX()) {
				cFirst = coA;
				cSecond = coB;
			}
			else {
				cFirst = coB;
				cSecond = coA;
			}
		}
		else if (coA.getY() > coB.getY()) {
			cFirst = coA;
			cSecond = coB;
		}
		else {
			cFirst = coB;
			cSecond = coA;
		}
		
		String keys = new StringBuilder().append(cFirst.getX()).append(cFirst.getY()).append(cSecond.getX()).append(cSecond.getY()).toString();
		this.getRoadmap().put(keys, road1);
	}
	
	//gets a location from the array based of coordinates
	public Location getLocationFromCoordinate(Coordinate coA) {
		
		return boardLocations[coA.getX()+5][coA.getY()+5];
	}
	
	//puts a location into the array at the given coordinates
	public void setLocationFromCoordinate(Coordinate coA, Location location1) {
		
		 boardLocations[coA.getX()+5][coA.getY()+5] = location1;
	}
}