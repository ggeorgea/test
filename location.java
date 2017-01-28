
/**
 * Class to store data on location
 */
public class location {
	
	private Coordinate coord;
	private String type;
	private Object contains;
	
//----Constructors----//
	
	public location() {
		
	}
	
	public location(Coordinate coord, String type, Object contains) {
		
		this.coord = coord;
		this.type = type;
		this.contains = contains;
	}

//----Getters and Setters----//
	
	public Coordinate getCoord() {
		
		return coord;
	}

	public void setCoord(Coordinate coord) {
		
		this.coord = coord;
	}

	public String getType() {
		
		return type;
	}

	public void setType(String type) {
		
		this.type = type;
	}

	public Object getContains() {
		
		return contains;
	}

	public void setContains(Object contains) {
		
		this.contains = contains;
	}
}