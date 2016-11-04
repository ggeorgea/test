
public class Building {

	private String type;
	private int victoryPoints;
	public Building(){}
	public Building(String type, int victoryPoints) {
		super();
		this.type = type;
		this.victoryPoints = victoryPoints;
	}

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
