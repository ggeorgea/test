
<<<<<<< HEAD
=======
/**
 * Class to store data on location
 */
>>>>>>> parent of 22cec3e... so this is strange, and i dont really understand why it is happening but apparently git gets confused with the names of files changing?
public class Location {
	
	private Coordinate coord;
	private String type;
	private Object contains;
	
<<<<<<< HEAD
=======
//----Constructors----//
	
>>>>>>> parent of 22cec3e... so this is strange, and i dont really understand why it is happening but apparently git gets confused with the names of files changing?
	public Location() {
		
	}
	
	public Location(Coordinate coord, String type, Object contains) {
		
		this.coord = coord;
		this.type = type;
		this.contains = contains;
	}

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