package completeCatan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Class to store information about development cards
 * and methods to allow players to buy and play development cards
 */
public class DevelopmentCard {

	private String type;
	private boolean hidden;
	
	private static final String KNIGHT = "knight";
	private static final String ROAD_BUILDING = "road_building";
	private static final String YEAR_OF_PLENTY = "year of plenty";
	private static final String MONOPOLY = "monopoly";
	private static final String VICTORY_POINT = "victory point";
	
	private static final String ORE = "ore";
	private static final String WOOL = "wool";
	private static final String GRAIN = "grain";
	private static final String LUMBER = "lumber";
	private static final String BRICK = "brick";
	
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
	public static void buyDevelopmentCard(Player player, Game game1, Scanner scanner) throws IOException {
			
		//checks the player has the correct resources to buy a development card
		ArrayList<ResourceCard> resources = hasDevelopmentCardResources(player);
		ArrayList<DevelopmentCard> developmentCards = game1.getDevelopmentCards();
		ArrayList<DevelopmentCard> playerDevCards = player.getDevelopmentCards();
		
		//checks if a development card can be bought
		if (resources.size() != 3) {
			
			Catan.printToClient("You do not have enough resources to buy a development card", player);
			return;
		}
		else if (developmentCards.size() <= 0) {
			
			Catan.printToClient("There are no development cards left in the deck", player);
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
			
			//tells the player their purchase was successful
			Catan.printToClient("You bought a development card", player);
			
			ArrayList<Player> players = game1.getPlayers();
			
			//notifies other players of the action
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i) != player) {
					
					PlayerSocket socket = player.getpSocket();
					
					if (socket != null) {
						socket.sendMessage("Player " + player.getName() + " bought a development card");
					}
				}
			}
			
			String type = developmentCard.getType();
			
			//if the development card is a victory point card, it is 
			//played immediately and the player is told
			if (type.equals(VICTORY_POINT)) {
				
				Catan.printToClient("It was a victory point card! You get an extra victory point!", player);
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
			
				if (ore == null && card.getResource().equals(ORE)) {
					ore = card;
				}
				if (wool == null && card.getResource().equals(WOOL)) {
					wool = card;
				}
				if (grain == null && card.getResource().equals(GRAIN)) {
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
 	public static void playDevelopmentCard(Player player, Game game1, Scanner scanner, boolean hasPlayedDevCard) throws IOException {
	 		
 		ArrayList<DevelopmentCard> cards = player.getDevelopmentCards();
 		ArrayList<DevelopmentCard> playCards = new ArrayList<DevelopmentCard>();
 		boolean cardPlayed = true;
	 		 		
 		//will check if the player has any cards to play
 		//these are non hidden cards
 		for (int i = 0; i < cards.size(); i++) {
 			
 			DevelopmentCard card = cards.get(i);
 			
 			if (!card.isHidden()) {
 				playCards.add(card);
 			}
 		}
	 		
 		//prints appropriate error statements if required
 		if (hasPlayedDevCard) {
 			
 			Catan.printToClient("You can only play one development card on your turn", player);
 			return;
 		}
 		if (playCards.size() <= 0) {
 			
 			Catan.printToClient("You do not have any development cards in your hand to play", player);
 			return;
 		}
	 	
 		Catan.printToClient("What development card do you want to play?", player);
 		
 		for (int i = 0; i < playCards.size(); i++) {
 			Catan.printToClient((i+1) + ": " + playCards.get(i).getType(), player);
 		}
 		
 		int choice = Integer.parseInt(Catan.getInputFromClient(player, scanner));
 		DevelopmentCard play = playCards.get(choice);	
 		
 		String type = play.getType();
 		
 		//selects the correct method depending on the type of card being played
 		if (type.equals(KNIGHT)) {
 			playKnightCard(player, game1, scanner);
 		}
 		if (type.equals(ROAD_BUILDING)) {
 			playRoadBuildingCard(player, game1, scanner);
 		}
 		if (type.equals(YEAR_OF_PLENTY)) {
 			cardPlayed = playYearOfPlentyCard(player, game1, scanner);
 		}
 		if (type.equals(MONOPOLY)) {
 			playMonopolyCard(player, game1, scanner);
 		}
 		
 		//should not ever be needed since victory point cards are played immediately
 		//here in case
 		if (type.equals(VICTORY_POINT)) {
 			playVictoryPointCard(player);
 		}
 		
 		//if the card has been played, it is removed from the player's hand
 		//this is because Year of Plenty may not be able to be played
 		//if the bank does not have enough resources
 		if (cardPlayed) {
 			
 			cards.remove(play);
 			player.setDevelopmentCards(cards);
 		}
 	}
 	
 	//plays a knight card
	public static void playKnightCard(Player player, Game game1, Scanner scanner) throws IOException {
		
		//moves the robber
		Robber.moveRobber(player, game1, scanner);
		
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
				
				//tells the player they are now in possession of the card
				Catan.printToClient("You now have the largest army!", player);
				
				//notifies all other players that the card has been passed on
				for (int i = 0; i < players.size(); i++) {
					if (players.get(i) != player) {
						
						PlayerSocket socket = players.get(i).getpSocket();
						
						if (socket != null) {
							socket.sendMessage("Player " + player.getName() + " now has the largest army!");
						}
					}
				}
			}
		}		
	}
 	
	//plays a road building card
	public static void playRoadBuildingCard(Player player, Game game1, Scanner scanner) throws IOException {
 		
 		boolean roadBuilding = true;
 		
 		//lets the player build two roads
 		for (int i = 0; i < 2; i++) {
 			Road.buildRoad(player, game1, scanner, roadBuilding);
 		}
 	}
	
	//plays a year of plenty card
	public static boolean playYearOfPlentyCard(Player player, Game game1, Scanner scanner) throws IOException {
		
		ArrayList<ResourceCard> cards = player.getResourceCards();
		boolean hasResource = true;
		
		//lets the player choose two resources from the bank
		for (int i = 0; i < 2; i++) {
			hasResource = chooseResourceYOP(cards, game1, scanner, player);
		}
		
		player.setResourceCards(cards);
		
		//if there are no resources in the bank the card cannot be played
		return hasResource;
	}
	
	//lets the player choose the resource for YOP
	public static boolean chooseResourceYOP(ArrayList<ResourceCard> cards, Game game1, Scanner scanner, Player player) throws IOException {
		
		ArrayList<ResourceCard> brick = game1.getBrick();
		ArrayList<ResourceCard> lumber = game1.getLumber();
		ArrayList<ResourceCard> wool = game1.getWool();
		ArrayList<ResourceCard> ore = game1.getOre();
		ArrayList<ResourceCard> grain = game1.getGrain();
		
		//checks if resources can be taken
		//if not the card is not played
		if (brick.size() <= 0 && lumber.size() <= 0 && wool.size() <= 0 && ore.size() <= 0 && grain.size() <= 0) {
			
			Catan.printToClient("There are no resources left in the bank. You cannot play this development card.", player);
			return false;
		}
		
		Catan.printToClient("Pick a resource", player);
		Catan.printToClient("1. Brick", player);
		Catan.printToClient("2. Lumber", player);
		Catan.printToClient("3. Wool", player);
		Catan.printToClient("4. Ore", player);
		Catan.printToClient("5. Grain", player);
		
		int choice = Integer.parseInt(Catan.getInputFromClient(player, scanner));
		
		//lets the player take a card from the bank
		switch (choice) {
		case 1 :
			if (brick.size() <= 0) {
				Catan.printToClient("There are no brick resource cards left. Please choose again.", player);
				chooseResourceYOP(cards, game1, scanner, player);
			}
			
			cards.add(brick.get(0));
			game1.getBrick().remove(brick);
			break;
		case 2 :
			if (lumber.size() <= 0) {
				Catan.printToClient("There are no lumber resource cards left. Please choose again.", player);
				chooseResourceYOP(cards, game1, scanner, player);
			}
			
			cards.add(lumber.get(0));
			game1.getLumber().remove(lumber);
			break;
		case 3 :
			if (wool.size() <= 0) {
				Catan.printToClient("There are no wool resource cards left. Please choose again.", player);
				chooseResourceYOP(cards, game1, scanner, player);
			}
			
			cards.add(wool.get(0));
			game1.getWool().remove(wool);
			break;
		case 4 :			
			if (ore.size() <= 0) {
				Catan.printToClient("There are no ore resource cards left. Please choose again.", player);
				chooseResourceYOP(cards, game1, scanner, player);
			}
			
			cards.add(ore.get(0));
			game1.getOre().remove(ore);
			break;
		case 5 :
			if (grain.size() <= 0) {
				Catan.printToClient("There are no grain resource cards left. Please choose again.", player);
				chooseResourceYOP(cards, game1, scanner, player);
			}
			
			cards.add(grain.get(0));
			game1.getGrain().remove(grain);
			break;
		default :
			Catan.printToClient("Invalid choice. Please choose again", player);
			chooseResourceYOP(cards, game1, scanner, player);
		}
		
		return true;
	}
	
	//plays a monopoly card
	public static void playMonopolyCard(Player player, Game game1, Scanner scanner) throws IOException {
		
		String resource = chooseResourceMonopoly(scanner, player);
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
	public static String chooseResourceMonopoly(Scanner scanner, Player player) throws IOException {
		
		Catan.printToClient("Pick a resource", player);
		Catan.printToClient("1. Brick", player);
		Catan.printToClient("2. Lumber", player);
		Catan.printToClient("3. Wool", player);
		Catan.printToClient("4. Ore", player);
		Catan.printToClient("5. Grain", player);
		
		int choice = Integer.parseInt(Catan.getInputFromClient(player, scanner));
		String resource = "";
		
		switch (choice) {
		case 1 :
			resource = BRICK;
			break;
		case 2 : 
			resource = LUMBER;
			break;
		case 3 :
			resource = WOOL;
			break;
		case 4 :
			resource = ORE;
			break;
		case 5 :
			resource = GRAIN;
			break;
		default :
			Catan.printToClient("Invalid choice. Please choose again", player);
			chooseResourceMonopoly(scanner, player);
		}
		
		return resource;
	}
	
	//plays a victory point card
	public static void playVictoryPointCard(Player player) {
		player.setVictoryPoints(player.getVictoryPoints() + 1);
	}
}