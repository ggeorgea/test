package game;

/**
 * Class to store coordinates
 */
public class Coordinate {

	private int x;
	private int y;
	
//-----Constructors-----//
	
	public Coordinate() {
		
	}
	
	public Coordinate(int x, int y) {
		
		this.x = x;
		this.y = y;
	}

//-----Getters and Setters-----//
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}	
}