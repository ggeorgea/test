import java.util.ArrayList;

public class Intersection {

	private Coordinate coordinate;
	private Player owner;
	private Building building;
	private ArrayList<Intersection> illegal = new ArrayList<Intersection>();
	
	public Intersection() {
		
	}
	
	public Intersection(Coordinate coordinate, Player owner, Building building) {
		
		this.coordinate = coordinate;
		this.owner = owner;
		this.building = building;
	}
	
	public Coordinate getCoordinate() {
	
		return coordinate;
	}
	
	public void setCoordinate(Coordinate coordinate) {
	
		this.coordinate = coordinate;
	}
	
	public Player getOwner() {
	
		return owner;
	}
	
	public void setOwner(Player owner) {
	
		this.owner = owner;
	}
	
	public Building getBuilding() {
	
		return building;
	}
	
	public void setBuilding(Building building) {
	
		this.building = building;
	}

	public ArrayList<Intersection> getIllegal() {
		
		return illegal;
	}

	public void setIllegal(ArrayList<Intersection> illegal) {
		
		this.illegal = illegal;
	}		
}