import java.util.ArrayList;

public class Board {

	private int size;
	private Coordinate robber;
	private ArrayList<Hex> hexes = new ArrayList<Hex>();
	private ArrayList<Port> ports = new ArrayList<Port>();
	private ArrayList<Road> roads = new ArrayList<Road>();
	private ArrayList<Intersection> buildings = new ArrayList<Intersection>();
	
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
}
