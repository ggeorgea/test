
public class Hex {

	private Coordinate coordinate;
	private String terrain;
	private int number;
	private String numString;
	private String isRobberHere = " ";
	
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
	
		switch (number) {
		case 2 :
			this.numString = " 2";
			break;
		case 3 :
			this.numString = " 3";
			break;
		case 4 :
			this.numString = " 4";
			break;
		case 5 :
			this.numString = " 5";
			break;
		case 6 :
			this.numString = " 6";
			break;
		case 7 :
			this.numString = " 7";
			break;
		case 8 :
			this.numString = " 8";
			break;
		case 9 :
			this.numString = " 9";
			break;
		case 10 :
			this.numString = "10";
			break;
		case 11 :
			this.numString = "11";
			break;
		case 12	:
			this.numString = "12";
			break;
		}
		
		this.number = number;
	}
	
	public String getnumString() {
	
		return numString;
	}
	
	public String getisRbberHere(){
		
		return isRobberHere;
	}
	
	public void setisRobberHere(String isRobberHere){
	
		this.isRobberHere=isRobberHere;
	}
}