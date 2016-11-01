import java.util.ArrayList;


public class Player {

	private String name;
	private int victoryPoints;
	
	private int longestRoad;
	private int largestArmy;
	
	private ArrayList<ResourceCard> resourceCards = new ArrayList<ResourceCard>();
	private ArrayList<DevelopmentCard> developmentCards = new ArrayList<DevelopmentCard>();
	
	public String getName() {
	
		return name;
	}
	
	public void setName(String name) {
	
		this.name = name;
	}
	
	public int getVictoryPoints() {
	
		return victoryPoints;
	}
	
	public void setVictoryPoints(int victoryPoints) {
	
		this.victoryPoints = victoryPoints;
	}
	
	public int getLongestRoad() {
	
		return longestRoad;
	}
	
	public void setLongestRoad(int longestRoad) {
	
		this.longestRoad = longestRoad;
	}
	
	public int getLargestArmy() {
	
		return largestArmy;
	}
	
	public void setLargestArmy(int largestArmy) {
	
		this.largestArmy = largestArmy;
	}
	
	public ArrayList<ResourceCard> getResourceCards() {
	
		return resourceCards;
	}
	
	public void setResourceCards(ArrayList<ResourceCard> resourceCards) {
	
		this.resourceCards = resourceCards;
	}
	
	public ArrayList<DevelopmentCard> getDevelopmentCards() {
	
		return developmentCards;
	}
	
	public void setDevelopmentCards(ArrayList<DevelopmentCard> developmentCards) {
	
		this.developmentCards = developmentCards;
	}
}
