import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class that stores information about players
 */
public class Player {

	private String name;
	private int victoryPoints;
	private int currentRoll;

	private int noRoads;
	private int noSettlements;
	private int noCities;

	private int longestRoad = 1;
	private int largestArmy;
	private boolean hasLongestRoad = false;
	private boolean hasLargestArmy = false;

	private ArrayList<ResourceCard> resourceCards = new ArrayList<ResourceCard>();
	private ArrayList<DevelopmentCard> developmentCards = new ArrayList<DevelopmentCard>();
	private ArrayList<ResourceCard> newResourceCards = new ArrayList<ResourceCard>();

	private ArrayList<Intersection> firstSettlements = new ArrayList<Intersection>();
	private ArrayList<Port> settledPorts = new ArrayList<Port>();
	private ArrayList<Port> standardPorts = new ArrayList<Port>();
	private ArrayList<Port> specialPorts = new ArrayList<Port>();

	//make an array list of roads
	//TODO do we need this?^^

//-----Constructors-----//

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

//-----Getters and Setters-----//

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

	//TODO: What is this??
	/*public int findLongestRoadLength(){

	}*/

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

	public ArrayList<ResourceCard> getNewResourceCards() {

		return newResourceCards;
	}

	public void setNewResourceCards(ArrayList<ResourceCard> newResourceCards) {

		this.newResourceCards = newResourceCards;
	}

	public ArrayList<Intersection> getFirstSettlements() {

		return firstSettlements;
	}

	public void setFirstSettlements(ArrayList<Intersection> firstSettlements) {

		this.firstSettlements = firstSettlements;
	}

	public ArrayList<Port> getSettledPorts(){

		return settledPorts;
	}

	public void setSettledPorts(ArrayList<Port> settledPorts){

		this.settledPorts = settledPorts;
	}

	public void setStandardPorts(ArrayList<Port> standardPorts){

		this.standardPorts = standardPorts;
	}

	public ArrayList<Port> getStandardPorts(){

		return standardPorts;
	}

	public void setSpecialPorts(ArrayList<Port> specialPorts){

		this.specialPorts = specialPorts;
	}

	public ArrayList<Port> getSpecialPorts(){

		return specialPorts;
	}

//-----Extra methods used in turn-----//

	//prints the player's hand
	public static void printResourceCards(Player player) {

		Iterator<ResourceCard> it = player.getResourceCards().iterator();
		System.out.print("(");

		while (it.hasNext()) {
			System.out.print(it.next().getResource());

			if (it.hasNext()) {
				System.out.print(", ");
			}
		}

		System.out.print(")\n");
	}

	//updates development cards so ones bought this turn can be played next turn
	public static void updateDevelopmentCards(Player player) {

		ArrayList<DevelopmentCard> developmentCards = player.getDevelopmentCards();

		for (int i = 0; i < developmentCards.size(); i++) {

			developmentCards.get(i).setHidden(false);
		}
	}
}
