
public class Road {

	private Coordinate coordinateA;
	private Coordinate coordinateB;
	private Player owner;
	
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
	
	public Player getOwner() {
	
		return owner;
	}
	
	public void setOwner(Player owner) {
	
		this.owner = owner;
	}	
}
