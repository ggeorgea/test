import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//TODO check when to use method check end of game

public class RandomMethods {

	/**
	 * Method should be called every time a road is placed
	 * and a player has the longest road card already
	 * 
	 * @param player1 the player who just placed a road
	 * @param player2 the player who has the longest road card right now
	 */
	public void checkLongestRoad(Player player1, Player player2) {
		
		if (player2 == null) {
			
			player1.setHasLongestRoad(true);
			int victoryPoints = player1.getVictoryPoints();
			player1.setVictoryPoints(victoryPoints + 2);
		}
		else if (player1.getLongestRoad() > player2.getLongestRoad()) {
						
			player1.setHasLongestRoad(true);
			int victoryPoints = player1.getVictoryPoints();
			player1.setVictoryPoints(victoryPoints + 2);
			
			player2.setHasLongestRoad(false);
			victoryPoints = player2.getVictoryPoints();
			player2.setVictoryPoints(victoryPoints - 2);			
		}
	}
	
	/**
	 * Method should be called every time a knight is played
	 * and a player has the largest army card already
	 * 
	 * @param player1 the player that just played a knight card
	 * @param player2 the player with the largest army card
	 */
	public void checkLargestArmy(Player player1, Player player2) {
		
		if (player1.getLargestArmy() > player2.getLargestArmy()) {
			
			player1.setHasLargestArmy(true);
			int victoryPoints = player1.getVictoryPoints();
			player1.setVictoryPoints(victoryPoints + 2);
			
			player2.setHasLargestArmy(false);
			victoryPoints = player2.getVictoryPoints();
			player2.setVictoryPoints(victoryPoints - 2);
		}
	}
	
	/**
	 * Method should be called every time a player completes an action
	 * like builds something, plays a card...
	 * 
	 * @param player the player that just did an action
	 */
	public void checkEndOfGame(Player player) {
		
		if (player.getVictoryPoints() >= 10) {
			
			endGame(player);
		}
	}
	
	/**
	 * Method should only be called once the game has finished
	 * 
	 * @param player the player who has won
	 */
	public void endGame(Player player) {
		
		System.out.println(player.getName() + " Wins!");
	}
	
	//TODO robber stuff
	//G: note, i added a robber text field to each of the hexes, to be filled with " " if the robber is elsewhere, and "R" if the robber is present
	//here so no errors
	ArrayList<Road> validRoads = null;
	
	//TODO update build road method to how buildcity is
	/**
	 * Method should be called when a player wants to build a road
	 * 
	 * @param player the player wanting to build the road
	 * @param road the road to be built
	 * @param roadBuilding if the player is using the road building card to
	 * place their road
	 */
	public void buildRoad(Player player, Road road, boolean roadBuilding) {
		
		ArrayList<ResourceCard> cards = player.getResourceCards();
		
		ResourceCard brick = null;
		ResourceCard lumber = null;
		int i = 0;
		
		while (brick == null || lumber == null) {
			
			ResourceCard card = cards.get(i);
			
			if (brick == null && card.getResource().equals("brick")) {
				
				brick = card;
			}
			if (lumber == null && card.getResource().equals("lumber")) {
				
				lumber = card;
			}
			
			i++;
		}
		
		if ((lumber != null && brick != null) || roadBuilding) {
			
			int roadsLeft = 15 - player.getNoRoads();
			
			if (roadsLeft > 0) {
				if (validRoads.contains(road)) {
				
					if (!roadBuilding) {
						cards.remove(brick);
						cards.remove(lumber);
						player.setResourceCards(cards);
					}
					
					player.setNoRoads(player.getNoRoads() - 1);
				
					//what about valid settlements?
					updateValidRoads(road);
					updateRoadLength(player);
					
					checkEndOfGame(player);
				}
				else {
					
					System.out.println("Not a valid road!");
				}
			}
			else {
				
				System.out.println("Not any roads left to place!");
			}
		}
		else {
			
			System.out.println("Not enough resources!");
		}
	}
	
	/**
	 * Method should be called when a road is placed to update
	 * where new roads can be built
	 * 
	 * @param road the road just placed
	 */
	public void updateValidRoads(Road road) {
		
		validRoads.remove(road);
		//remove nearby roads too?
	}
	
	/**
	 * Method should be called to update the player's longest road
	 * and to check if the player has the longest road in the game
	 * 
	 * @param player the player that just built a road
	 */
	public void updateRoadLength(Player player) {
		
		//update the player's longest road if necessary
		
		if (player.getLongestRoad() >= 5) {
			//check if a player has longest road already
			//if they do use that player
			//else 
			Player player2 = null;
			
			checkLongestRoad(player, player2);			
		}
	}
	
	//here so no errors
	ArrayList<Building> validSettlements = null;
	
	//TODO update build settlement method to how buildcity is
	/**
	 * Method should be called when the player wants to build a road
	 * 
	 * @param player the player wanting to build the settlement
	 * @param settlement the settlement to be built
	 */
	public void buildSettlement(Player player, Building settlement) {
		
		ArrayList<ResourceCard> cards = player.getResourceCards();
		
		ResourceCard brick = null;
		ResourceCard lumber = null;
		ResourceCard wool = null;
		ResourceCard grain = null;
		int i = 0;
		
		while (brick == null || lumber == null || wool == null || grain == null) {
			
			ResourceCard card = cards.get(i);
			
			if (brick == null && card.getResource().equals("brick")) {
				
				brick = card;
			}
			if (lumber == null && card.getResource().equals("lumber")) {
				
				lumber = card;
			}
			if (wool == null && card.getResource().equals("wool")) {
				
				wool = card;
			}
			if (grain == null && card.getResource().equals("grain")) {
				
				grain = card;
			}
			
			i++;
		}
		
		if (brick != null && lumber != null && wool != null && grain != null) {
			
			int settlementsLeft = 5 - player.getNoSettlements();
			
			if (settlementsLeft > 0) {
				if (validSettlements.contains(settlement)) {
					
					cards.remove(brick);
					cards.remove(lumber);
					cards.remove(wool);
					cards.remove(grain);
					player.setResourceCards(cards);
					
					player.setNoSettlements(player.getNoSettlements() - 1);
					player.setVictoryPoints(player.getVictoryPoints() + 1);
					
					updateValidSettlements(settlement);
					
					checkEndOfGame(player);					
				}
				else {
					
					System.out.println("Not a valid settlement!");
				}
			}
			else {
				
				System.out.println("Not any settlements left to place!");
			}
		}
		else {
			
			System.out.println("Not enough resources!");
		}
	}
	
	/**
	 * Method should be called to update where settlements can be placed
	 * 
	 * @param settlement the settlement just played
	 */
	public void updateValidSettlements(Building settlement) {
		
		validSettlements.remove(settlement);
		//will be different for every player?
	}
	
	/**
	 * Method should be called when a player wants to build a city
	 * 
	 * @param player the player wanting to build the city
	 * @param city the city to be placed
	 */
	public void buildCity(Player player, Building city) {
		
		ArrayList<ResourceCard> cards = player.getResourceCards();
		
		ArrayList<ResourceCard> ore = new ArrayList<ResourceCard>();
		ArrayList<ResourceCard> grain = new ArrayList<ResourceCard>();
		
		for (int i = 0; i < cards.size(); i++) {
			
			ResourceCard card = cards.get(i);
			
			if (card.getResource().equals("ore")) {
				
				ore.add(card);
			}
			
			if (card.getResource().equals("grain")) {
				
				grain.add(card);
			}
		}
		
		if (ore.size() >= 3 && grain.size() >= 2) {
			
			int citiesLeft = 4 - player.getNoCities();
			
			if (citiesLeft > 0) {
				if (player.getNoSettlements() > 0) {
				
					//check the city they want to place is on a player settlement
				
					cards.remove(ore.get(0));
					cards.remove(ore.get(1));
					cards.remove(ore.get(2));
					cards.remove(grain.get(0));
					cards.remove(grain.get(1));
					player.setResourceCards(cards);
				
					player.setNoCities(player.getNoCities() + 1);
					player.setNoSettlements(player.getNoSettlements() - 1);
					player.setVictoryPoints(player.getVictoryPoints() + 1);
				
					checkEndOfGame(player);
				}
				else {
					
					System.out.println("No settlements to convert to a city!");
				}
			}
			else {
				
				System.out.println("Not any cities left to place!");
			}
		}
		else {
			
			System.out.println("Not enough resources!");
		}
	}
	
	//here so no errors
	Game game = new Game();
	
	/**
	 * Method should be played when a player wants to buy a development card
	 * 
	 * @param player the player wanting to buy the card
	 * @throws IOException 
	 */
	public void buyDevelopmentCard(Player player) throws IOException {
		
		ArrayList<ResourceCard> cards = player.getResourceCards();
		
		ArrayList<ResourceCard> ore = new ArrayList<ResourceCard>();
		ArrayList<ResourceCard> wool = new ArrayList<ResourceCard>();
		ArrayList<ResourceCard> grain = new ArrayList<ResourceCard>();
		
		for (int i = 0; i < cards.size(); i++) {
			
			ResourceCard card = cards.get(i);
			
			if (card.getResource().equals("ore")) {
				
				ore.add(card);
			}
			
			if (card.getResource().equals("wool")) {
				
				wool.add(card);
			}
			
			if (card.getResource().equals("grain")) {
				
				grain.add(card);
			}
		}
		
		if (ore.size() >= 1 && wool.size() >= 1 && grain.size() >= 1) {
			
			ArrayList<DevelopmentCard> developmentCards = game.getDevelopmentCards();
			
			if (developmentCards.size() > 0) {
				
				cards.remove(ore.get(0));
				cards.remove(wool.get(0));
				cards.remove(grain.get(0));
				player.setResourceCards(cards);
				
				//gets the first dev card. Should be a random card though
				DevelopmentCard developmentCard = developmentCards.get(0);
				developmentCards.remove(developmentCard);
				game.setDevelopmentCards(developmentCards);
				
				ArrayList<DevelopmentCard> playerDevelopmentCards = player.getDevelopmentCards();
				playerDevelopmentCards.add(developmentCard);
				player.setDevelopmentCards(playerDevelopmentCards);
				
				checkDevelopmentCard(player, developmentCard);
				
				checkEndOfGame(player);
			}
			
			else {
				
				System.out.println("No devlopment cards left in the deck!");
			}
		}
		else {			
			
			System.out.println("Not enough resources!");
		}
	}
	
	/**
	 * Should be played when a development card is bought
	 * 
	 * @param player the player that bought the development card
	 * @param developmentCard the card that was bought
	 * @throws IOException 
	 */
	public void checkDevelopmentCard(Player player, DevelopmentCard developmentCard) throws IOException {
		
		String type = developmentCard.getType();
		
		if (!developmentCard.isHidden()) {
		
			if (type.equals("knight")) {
				
				playKnightCard(player, developmentCard);
			}
			if (type.equals("progress")) {
			
				playProgressCard(player, developmentCard);
			}
			if (type.equals("victory point")) {
			
				playVictoryPointCard(player, developmentCard);
			}
		}
	}
	
	/**
	 * Method should be used when a play wants to play a knight card
	 * @param player the player wanting to play the knight card
	 * @param knight the knight card being played
	 */
	public void playKnightCard(Player player, DevelopmentCard knight) {
		
		ArrayList<DevelopmentCard> developmentCards = player.getDevelopmentCards();
		
		developmentCards.remove(knight);
		player.setDevelopmentCards(developmentCards);
		
		//TODO do some robber stuff
		
		player.setLargestArmy(player.getLargestArmy() + 1);
		
		if (player.getLargestArmy() >= 3) { 
			//check if a player has largest army already
			//if they do use that player
			//else 
			Player player2 = null;
			
			checkLargestArmy(player, player2);	
			checkEndOfGame(player);
		}
	}
	
	/**
	 * Method should be used when a player wants to play a progress card
	 * 
	 * @param player the player wanting to play the progress card
	 * @param progress the card to be played
	 * @throws IOException
	 */
	public void playProgressCard(Player player, DevelopmentCard progress) throws IOException {
		
		ArrayList<DevelopmentCard> developmentCards = player.getDevelopmentCards();
		
		developmentCards.remove(progress);
		player.setDevelopmentCards(developmentCards);
		
		//should make a class for each dev card and use inheritance
		String type = progress.getType();
		
		if (type.equals("road building")) {
			
			playRoadBuildingCard(player, progress);
		}
		if (type.equals("year of plenty")) {
			
			playYearOfPlentyCard(player, progress);
		}
		if (type.equals("monopoly")) {
			
			playMonopolyCard(player, progress);
		}
	}
	
	/**
	 * Method should be used when a player wants to use the road building card
	 * 
	 * @param player the player wanting to play the road building card
	 * @param progress the progress card being played
	 */
	public void playRoadBuildingCard(Player player, DevelopmentCard progress) {
		
		boolean roadBuilding = true;
		
		for (int i = 0; i < 2; i++) {
			
			System.out.println("Pick a road to build");
				
			//will let player choose coord for roads
			Road road = null;
			buildRoad(player, road, roadBuilding);
		}
	}
	
	/**
	 * Method should be used when the player wants to play the year of plenty card
	 * 
	 * @param player the player wanting to play the card
	 * @param progress the progress card being played
	 * @throws IOException
	 */
	public void playYearOfPlentyCard(Player player, DevelopmentCard progress) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		ArrayList<ResourceCard> cards = player.getResourceCards();
		
		for (int i = 0; i < 2; i++) {
			
			System.out.println("Pick a resource");
			
			ResourceCard card = new ResourceCard();
			String resource = reader.readLine();
			
			if (resource.equals("brick") || resource.equals("lumber") || resource.equals("wool") 
					|| resource.equals("ore") || resource.equals("grain")) {
			
				card.setResource(resource);
				cards.add(card);				
			}
			else {
				
				System.out.println("Invalid reource type!");
			}
		}
		
		player.setResourceCards(cards);
		
		reader.close();
	}
	
	/**
	 * Method should be used when a player wants to play the monopoly card
	 * 
	 * @param player the player wanting to play the card
	 * @param progress the progress card being played
	 * @throws IOException
	 */
	public void playMonopolyCard(Player player, DevelopmentCard progress) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		ArrayList<ResourceCard> cards = player.getResourceCards();
		
		System.out.println("Pick a resouce");
		
		String resource = reader.readLine();
		
		if (resource.equals("brick") || resource.equals("lumber") || resource.equals("wool") 
				|| resource.equals("ore") || resource.equals("grain")) {
			
			ArrayList<Player> players = game.getPlayers();
			players.remove(player);
			
			for (int i = 0; i < players.size(); i++) {
				
				Player player2 = players.get(i);
				ArrayList<ResourceCard> player2Cards = player2.getResourceCards();
				
				for (int j = 0; j < player2Cards.size(); j++) {
					
					ResourceCard card = player2Cards.get(i);
					
					if (card.getResource().equals(resource)) {
						
						player2Cards.remove(card);
						cards.add(card);
					}
				}
				
				player2.setResourceCards(player2Cards);
			}
		}
		
		player.setResourceCards(cards);
		
		reader.close();
	}
	
	/**
	 * Method should be used when a player buys a victory point card
	 * 
	 * @param player the player wanting to play the card
	 * @param victoryPoint the card being played
	 */
	public void playVictoryPointCard(Player player, DevelopmentCard victoryPoint) {
		
		ArrayList<DevelopmentCard> developmentCards = player.getDevelopmentCards();
		
		developmentCards.remove(victoryPoint);
		player.setDevelopmentCards(developmentCards);
		
		player.setVictoryPoints(player.getVictoryPoints() + 1);
		
		checkEndOfGame(player);
	}
}
