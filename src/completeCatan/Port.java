package completeCatan;


/**
 * Class that stores information about ports
 */
public class Port {

	private Coordinate coordinateA;
	private Coordinate coordinateB;
	private String resource;
	private Player owner;

//-----Constructors-----//
	
	public Port() {

	}

	public Port(Coordinate coordinateA, Coordinate coordinateB, String resource, Player owner) {

		this.coordinateA = coordinateA;
		this.coordinateB = coordinateB;
		this.resource = resource;
		this.owner = owner;
	}

//-----Getters and Setters-----//
	
	public Coordinate getCoordinateA() {
		return coordinateA;
	}

	public void setCoordinateA(Coordinate coordinateA) {
		this.coordinateA = coordinateA;
	}

	public Coordinate getCoordinateB() {
		return coordinateB;
	}

	public void setCoordinateB(Coordinate coordinateB) {
		this.coordinateB = coordinateB;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}
}
