import java.util.ArrayList;


public class Player {

	private String name;
	private int victoryPoints;
	private int currentRoll;
	
	private int noRoads;
	private int noSettlements;
	private int noCities;
	
	private int longestRoad;
	private int largestArmy;
	private boolean hasLongestRoad = false;
	private boolean hasLargestArmy = false;
	
	private ArrayList<ResourceCard> resourceCards = new ArrayList<ResourceCard>();
	private ArrayList<DevelopmentCard> developmentCards = new ArrayList<DevelopmentCard>();
	
	public Player() {
		
	}
	
	public Player(String name, int victoryPoints, int currentRoll, int noRoads,
			int noSettlements, int noCities, int longestRoad, int largestArmy,
			boolean hasLongestRoad, boolean hasLargestArmy,
			ArrayList<ResourceCard> resourceCards,
			ArrayList<DevelopmentCard> developmentCards) {
		
		this.name = name;
		this.victoryPoints = victoryPoints;
		this.currentRoll = currentRoll;
		this.noRoads = noRoads;
		this.noSettlements = noSettlements;
		this.noCities = noCities;
		this.longestRoad = longestRoad;
		this.largestArmy = largestArmy;
		this.hasLongestRoad = hasLongestRoad;
		this.hasLargestArmy = hasLargestArmy;
		this.resourceCards = resourceCards;
		this.developmentCards = developmentCards;
	}

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
	
	public int getCurrentRoll() {
		
		return currentRoll;
	}

	public void setCurrentRoll(int currentRoll) {
		
		this.currentRoll = currentRoll;
	}

	public int getNoRoads() {
		
		return noRoads;
	}

	public void setNoRoads(int noRoads) {
		
		this.noRoads = noRoads;
	}
	
	public int getNoSettlements() {
		
		return noSettlements;
	}
	
	public void setNoSettlements(int noSettlements) {
		
		this.noSettlements = noSettlements;
	}
	
	public int getNoCities() {
		
		return noCities;
	}
	
	public void setNoCities(int noCities) {
		
		this.noCities = noCities;
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
	
	public boolean hasLongestRoad() {
		
		return hasLongestRoad;
	}

	public void setHasLongestRoad(boolean hasLongestRoad) {
		
		this.hasLongestRoad = hasLongestRoad;
	}

	public boolean hasLargestArmy() {
		
		return hasLargestArmy;
	}

	public void setHasLargestArmy(boolean hasLargestArmy) {
		
		this.hasLargestArmy = hasLargestArmy;
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
