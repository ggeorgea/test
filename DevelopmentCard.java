import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to store information about development cards
 * and methods to allow players to buy and play development cards
 */
public class DevelopmentCard {

	private String type;
	private boolean hidden;
	
//-----Constructors-----//
	
	public DevelopmentCard() {
		
	}
	
	public DevelopmentCard(String type, boolean hidden) {
	
		this.type = type;
		this.hidden = hidden;
	}

//-----Getters and Setters-----//
	
	public String getType() {
	
		return type;
	}
	
	public void setType(String type) {
	
		this.type = type;
	}
	
	public boolean isHidden() {
	
		return hidden;
	}
	
	public void setHidden(boolean hidden) {
	
		this.hidden = hidden;
	}
	
//-----Methods to buy a development card-----//
	
	//lets the player buy a development card
	public static void buyDevelopmentCard(Player player, Game game1, Scanner scanner) {
			
		//checks the player has the correct resources to buy a development card
		ArrayList<ResourceCard> resources = hasDevelopmentCardResources(player);
		ArrayList<DevelopmentCard> developmentCards = game1.getDevelopmentCards();
		ArrayList<DevelopmentCard> playerDevCards = player.getDevelopmentCards();
		
		//checks if a development card can be bought
		if (resources.size() != 3) {
				
			System.out.println("You do not have enough resources to buy a development card");
			return;
		}
		else if (developmentCards.size() <= 0) {
			
			System.out.println("There are no development cards left in the deck");
			return;			
		}
		
		//if a development card can be bought, the resources are removed from
		//the player's hand and the development card is added
		else {
			
			ArrayList<ResourceCard> cards = player.getResourceCards();
			
			for (int i = 0; i < 3; i++) {
				cards.remove(resources.get(i));
			}
			
			player.setResourceCards(cards);
			
			DevelopmentCard developmentCard = developmentCards.get(0);
			developmentCards.remove(developmentCard);
			game1.setDevelopmentCards(developmentCards);
			
			playerDevCards.add(developmentCard);
			
			System.out.println("Player " + player.getName() + " bought a development card");
			
			String type = developmentCard.getType();
			
			//if the development card is a victory point card, it is 
			//played immediately
			if (type.equals("victory point")) {
				
				playVictoryPointCard(player);
				playerDevCards.remove(developmentCard);
			}
			
			player.setDevelopmentCards(playerDevCards);
		}
	}
		
	//checks if the player enough resources to build the settlement
	public static ArrayList<ResourceCard> hasDevelopmentCardResources(Player player) {
		
		ArrayList<ResourceCard> cards = player.getResourceCards();
		ArrayList<ResourceCard> resources = new ArrayList<ResourceCard>();
		
		ResourceCard ore = null;
		ResourceCard wool = null;
		ResourceCard grain = null;
		int i = 0;
		
		//checks the player has the correct number of resources
		try {
			while (ore == null || wool == null || grain == null) {
				
				ResourceCard card = cards.get(i);
			
				if (ore == null && card.getResource().equals("ore")) {
				
					ore = card;
				}
				if (wool == null && card.getResource().equals("wool")) {
				
					wool = card;
				}
				if (grain == null && card.getResource().equals("grain")) {
				
					grain = card;
				}
			
				i++;
			}
		}
		catch(IndexOutOfBoundsException e) {
 			return new ArrayList<ResourceCard>();
 		}
		
		//if the player has the resources then they are returned
		if (ore != null && wool != null && grain != null) {
			
			resources.add(ore);
			resources.add(wool);
			resources.add(grain);
		}
		
		return resources;
	}
	
	
//-----Methods to play a development card-----//
		
	//lets the player select a development card to play
 	public static void playDevelopmentCard(Player player, Game game1, Scanner scanner, boolean hasPlayedDevCard) {
	 		
 		ArrayList<DevelopmentCard> cards = player.getDevelopmentCards();
 		ArrayList<DevelopmentCard> playCards = new ArrayList<DevelopmentCard>();
 		boolean cardPlayed = true;
	 		 		
 		for (int i = 0; i < cards.size(); i++) {
 			
 			DevelopmentCard card = cards.get(i);
 			
 			if (!card.isHidden()) {
 				playCards.add(card);
 			}
 		}
	 		
 		if (hasPlayedDevCard) {
 			System.out.println("You can only play one development card on your turn");
 			return;
 		}
 		if (playCards.size() <= 0) {
 			System.out.println("You do not have any development cards in your hand to play");
 			return;
 		}
	 		
 		System.out.println("What development card do you want to play?");
 		
 		for (int i = 0; i < playCards.size(); i++) {
 			
 			System.out.println((i+1) + ": " + playCards.get(i).getType());
 		}
 		
 		int choice = scanner.nextInt();
 		DevelopmentCard play = playCards.get(choice);		
 		String type = play.getType();
 		
 		//selects the correct method depending on the type of card being played
 		if (type.equals("knight")) {
 			
			playKnightCard(player, game1, scanner);
 		}
 		if (type.equals("road building")) {
 			
			playRoadBuildingCard(player, game1, scanner);
 		}
 		if (type.equals("year of plenty")) {
 			
 			cardPlayed = playYearOfPlentyCard(player, game1, scanner);
 		}
 		if (type.equals("monopoly")) {
 			
 			playMonopolyCard(player, game1, scanner);
 		}
 		//should not ever be needed since victory point cards are played immediately
 		//here in case
 		if (type.equals("victory point")) {
 			
			playVictoryPointCard(player);
 		}
 		
 		//if the card has been played, it is removed from the player's hand
 		if (cardPlayed) {
 			
 			cards.remove(play);
 			player.setDevelopmentCards(cards);
 		}
 	}
 	
 	//plays a knight card
	public static void playKnightCard(Player player, Game game1, Scanner scanner) {
		
		Turn.moveRobber(player, game1, scanner);
		//TODO card steal?
		
		player.setLargestArmy(player.getLargestArmy() + 1);
 				
		//checks for largest army
		checkLargestArmy(player, game1);		
 	}
	
	//updates who has the largest army card
	public static void checkLargestArmy(Player player, Game game1) {
		
		int noKnights = player.getLargestArmy();
		
		//will only check if the player who just played a knight card
		//has at least three knight cards
		if (noKnights >= 3) {
			
			boolean larger = false;
			ArrayList<Player> players = game1.getPlayers();
	
			//checks if there is another player with more knights than the
			//current player
			for (Player p : players) {
				
				if (p.getLargestArmy() >= noKnights && !p.equals(player)) {
					
					larger = true;
				}
				//if there is a player that has less knights than the player
				//that has the largest army card, the points are taken away from them
				else if (!p.equals(player) && p.hasLargestArmy()) {
					
					p.setHasLargestArmy(false);
					p.setVictoryPoints(p.getVictoryPoints()-2);
				}
			}
			
			//if there is no player with more knights and the player does not
			//already have largest army, they are given the victory points
			if (!larger && !player.hasLargestArmy()) {
				
				player.setHasLargestArmy(true); 
				player.setVictoryPoints(player.getVictoryPoints()+2); 
				System.out.println("player " + player.getName() + " now has the largest army!");
			}
		}		
	}
 	
	//plays a road building card
	public static void playRoadBuildingCard(Player player, Game game1, Scanner scanner) {
 		
 		boolean roadBuilding = true;
 		
 		//lets the player build two roads
 		for (int i = 0; i < 2; i++) {
 			
 			Road.buildRoad(player, game1, scanner, roadBuilding);
 		}
 	}
	
	//plays a year of plenty card
	public static boolean playYearOfPlentyCard(Player player, Game game1, Scanner scanner) {
		
		ArrayList<ResourceCard> cards = player.getResourceCards();
		boolean hasResource = true;
		
		//lets the player choose two resources from the bank
		for (int i = 0; i < 2; i++) {
			
			hasResource = chooseResourceYOP(cards, game1, scanner);
		}
		
		player.setResourceCards(cards);
		
		//if there are no resources in the bank the card cannot be played
		return hasResource;
	}
	
	//lets the player choose the resource for YOP
	public static boolean chooseResourceYOP(ArrayList<ResourceCard> cards, Game game1, Scanner scanner) {
		
		ArrayList<ResourceCard> brick = game1.getBrick();
		ArrayList<ResourceCard> lumber = game1.getLumber();
		ArrayList<ResourceCard> wool = game1.getWool();
		ArrayList<ResourceCard> ore = game1.getOre();
		ArrayList<ResourceCard> grain = game1.getGrain();
		
		//checks if resources can be taken
		//if not the card is not played
		if (brick.size() <= 0 && lumber.size() <= 0 && wool.size() <= 0 && ore.size() <= 0 && grain.size() <= 0) {
			
			System.out.println("There are no resources left in the bank. You cannot play this development card.");
			return false;
		}
		
		System.out.println("Pick a resource");
		System.out.println("1. Brick");
		System.out.println("2. Lumber");
		System.out.println("3. Wool");
		System.out.println("4. Ore");
		System.out.println("5. Grain");
		
		int choice = scanner.nextInt();
		
		//lets the player take a card from the bank
		switch (choice) {
		case 1 :
			if (brick.size() <= 0) {
				System.out.println("There are no brick resource cards left. Please choose again.");
				chooseResourceYOP(cards, game1, scanner);
			}
			
			cards.add(brick.get(0));
			game1.getBrick().remove(brick);
			break;
		case 2 :
			if (lumber.size() <= 0) {
				System.out.println("There are no lumber resource cards left. Please choose again.");
				chooseResourceYOP(cards, game1, scanner);
			}
			
			cards.add(lumber.get(0));
			game1.getLumber().remove(lumber);
			break;
		case 3 :
			if (wool.size() <= 0) {
				System.out.println("There are no wool resource cards left. Please choose again.");
				chooseResourceYOP(cards, game1, scanner);
			}
			
			cards.add(wool.get(0));
			game1.getWool().remove(wool);
			break;
		case 4 :			
			if (ore.size() <= 0) {
				System.out.println("There are no ore resource cards left. Please choose again.");
				chooseResourceYOP(cards, game1, scanner);
			}
			
			cards.add(ore.get(0));
			game1.getOre().remove(ore);
			break;
		case 5 :
			if (grain.size() <= 0) {
				System.out.println("There are no grain resource cards left. Please choose again.");
				chooseResourceYOP(cards, game1, scanner);
			}
			
			cards.add(grain.get(0));
			game1.getGrain().remove(grain);
			break;
		default :
			System.out.println("Invalid choice. Please choose again");
			chooseResourceYOP(cards, game1, scanner);
		}
		
		return true;
	}
	
	//plays a monopoly card
	public static void playMonopolyCard(Player player, Game game1, Scanner scanner) {
		
		String resource = chooseResourceMonopoly(scanner);
		ArrayList<ResourceCard> cards = player.getResourceCards();
		
		ArrayList<Player> players = game1.getPlayers();
		players.remove(player);
		
		//takes the resources of specified type from each of the other
		//players' hands and gives them to the player
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
	
		player.setResourceCards(cards);
	}
	
	//lets the player choose the resource for monopoly
	public static String chooseResourceMonopoly(Scanner scanner) {
		
		System.out.println("Pick a resource");
		System.out.println("1. Brick");
		System.out.println("2. Lumber");
		System.out.println("3. Wool");
		System.out.println("4. Ore");
		System.out.println("5. Grain");
		
		int choice = scanner.nextInt();
		String resource = "";
		
		switch (choice) {
		case 1 :
			resource = "brick";
			break;
		case 2 : 
			resource = "lumber";
			break;
		case 3 :
			resource = "wool";
			break;
		case 4 :
			resource = "ore";
			break;
		case 5 :
			resource = "grain";
			break;
		default :
			System.out.println("Invalid choice. Please choose again");
			chooseResourceMonopoly(scanner);
		}
		
		return resource;
	}
	
	//plays a victory point card
	public static void playVictoryPointCard(Player player) {
				
		player.setVictoryPoints(player.getVictoryPoints() + 1);
	}
}