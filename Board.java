import java.util.ArrayList;
import java.util.HashMap;

public class Board {

	private int size;
	private Coordinate robber;
	private ArrayList<Hex> hexes = new ArrayList<Hex>();
	private ArrayList<Port> ports = new ArrayList<Port>();
	private ArrayList<Road> roads = new ArrayList<Road>();
	private ArrayList<Intersection> buildings = new ArrayList<Intersection>();
	//this location 2d array is used for getting locations with just coordinates
	private location[][] boardLocations;
	//this hashmap is used for getting roads with their endpoint coordinates
	private HashMap roadmap;
	
	public Board() {
		
	}
	
	public Board(int size, Coordinate robber, ArrayList<Hex> hexes,
			ArrayList<Port> ports, ArrayList<Road> roads,
			ArrayList<Intersection> buildings, location[][] boardLocations) {
		
		this.size = size;
		this.robber = robber;
		this.hexes = hexes;
		this.ports = ports;
		this.roads = roads;
		this.buildings = buildings;
		this.boardLocations = boardLocations;
	}
	
	public HashMap getRoadmap() {
		return roadmap;
	}

	public void setRoadmap(HashMap roadmap) {
		this.roadmap = roadmap;
	}

	public location[][] getBoardLocations() {
		return boardLocations;
	}

	public void setBoardLocations(location[][] boardLocations) {
		this.boardLocations = boardLocations;
	}

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
	
	//these last four methods ensure that the hashmap and the 2d array are accessed properly
	
	public Road getRoadFromCo(Coordinate coA, Coordinate coB){
		Coordinate cFirst;
		Coordinate cSecond;
		if(coA.getY()==coB.getY()){
			if(coA.getX()<coB.getX()){
				cFirst = coA;
				cSecond = coB;
			}
			else{
				cFirst = coB;
				cSecond = coA;
			}
		}
		else if(coA.getY()>coB.getY()){
			cFirst = coA;
			cSecond = coB;
		}
		else{
			cFirst = coB;
			cSecond = coA;
		}
		String keys = new StringBuilder().append(cFirst.getX()).append(cFirst.getY()).append(cSecond.getX()).append(cSecond.getY()).toString();
		return (Road) this.getRoadmap().get(keys);
	}
	
	public location getLocationFromCoordinate(Coordinate coA){
		return boardLocations[coA.getX()+5][coA.getY()+5];
	}
	
	public void setLocationFromCoordinate(Coordinate coA, location location1){
		 boardLocations[coA.getX()+5][coA.getY()+5] = location1 ;
	}
	
	public void setRoadFromCo(Road road1, Coordinate coA, Coordinate coB){
		Coordinate cFirst;
		Coordinate cSecond;
		if(coA.getY()==coB.getY()){
			if(coA.getX()<coB.getX()){
				cFirst = coA;
				cSecond = coB;
			}
			else{
				cFirst = coB;
				cSecond = coA;
			}
		}
		else if(coA.getY()>coB.getY()){
			cFirst = coA;
			cSecond = coB;
		}
		else{
			cFirst = coB;
			cSecond = coA;
		}
		String keys = new StringBuilder().append(cFirst.getX()).append(cFirst.getY()).append(cSecond.getX()).append(cSecond.getY()).toString();
		this.getRoadmap().put(keys, road1);
	}
}
