
public class Hex {

	private Coordinate coordinate;
	private String terrain;
	private int number;
	
	public Hex() {
		
	}
	
	public Hex(Coordinate coordinate, String terrain, int number) {
		
		this.coordinate = coordinate;
		this.terrain = terrain;
		this.number = number;
	}

	public Coordinate getCoordinate() {
	
		return coordinate;
	}
	
	public void setCoordinate(Coordinate coordinate) {
	
		this.coordinate = coordinate;
	}
	
	public String getTerrain() {
	
		return terrain;
	}
	
	public void setTerrain(String terrain) {
	
		this.terrain = terrain;
	}
	
	public int getNumber() {
	
		return number;
	}
	
	public void setNumber(int number) {
	
		this.number = number;
	}	
}
