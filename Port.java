
public class Port {

	private Coordinate coordinateA;
	private Coordinate coordinateB;
	private String resource;
	
	public Port() {
		
	}
	
	public Port(Coordinate coordinateA, Coordinate coordinateB, String resource) {

		this.coordinateA = coordinateA;
		this.coordinateB = coordinateB;
		this.resource = resource;
	}
	
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
}