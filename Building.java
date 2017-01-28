
/**
 * Class to store information about types of building
 */
public class Building {

	private String type;
	private int victoryPoints;
	
//----Constructors----//
	
	public Building() {
		
	}
	
	public Building(String type, int victoryPoints) {
		
		this.type = type;
		this.victoryPoints = victoryPoints;
	}
	
//----Getters and Setters----//
	
	public String getType() {
		
		return type;
	}
	
	public void setType(String type) {
		
		this.type = type;
	}
	
	public int getVictoryPoints() {
	
		return victoryPoints;
	}
	
	public void setVictoryPoints(int victoryPoints) {
	
		this.victoryPoints = victoryPoints;
	}	
}