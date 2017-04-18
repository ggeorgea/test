package game;

import java.util.ArrayList;
import java.util.Scanner;

import java.io.IOException;

import intergroup.Events.Event;
import intergroup.Events.Event.Error;
import intergroup.Messages.Message;
import intergroup.board.Board;
import intergroup.board.Board.MultiSteal;
import intergroup.board.Board.PlayableDevCard;
import intergroup.board.Board.Steal;

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
	public static void buyDevelopmentCard(Player player, Game game1, Message enter) throws IOException {

		//checks the player has the correct resources to buy a development card
		ArrayList<ResourceCard> resources = hasDevelopmentCardResources(player);
		ArrayList<DevelopmentCard> developmentCards = game1.getDevelopmentCards();
		ArrayList<DevelopmentCard> playerDevCards = player.getDevelopmentCards();
		
		//checks if a development card can be bought
		if (resources.size() != 3) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("You do not have enough resources").build()).build()).build(), player.getpSocket().getClientSocket());
			return;
		}
		else if (developmentCards.size() <= 0) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("There are no development cards left").build()).build()).build(), player.getpSocket().getClientSocket());
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
			
			int card = -1;
			
			if (developmentCard.getType().equals(KNIGHT)) {
				card = 0;
			}
			if (developmentCard.getType().equals(ROAD_BUILDING)) {
				card = 1;
			}
			if (developmentCard.getType().equals(MONOPOLY)) {
				card = 2;
			}
			if (developmentCard.getType().equals(YEAR_OF_PLENTY)) {
				card = 3;
			}
			
			int playerNum = 0;
			
			for (int i = 0; i < game1.getPlayers().size(); i++) {
				if (game1.getPlayers().get(i).equals(player)) {
					playerNum = i;
				}
			}
			
			Message m = null;
			
			if (card != -1) {
				m = Message.newBuilder().setEvent(Event.newBuilder().setInstigator(Board.Player.newBuilder().setIdValue(playerNum).build()).setDevCardBought(Board.DevCard.newBuilder().setPlayableDevCardValue(card).build()).build()).build();
			}
			else {
				m = Message.newBuilder().setEvent(Event.newBuilder().setInstigator(Board.Player.newBuilder().setIdValue(playerNum).build()).setDevCardBought(Board.DevCard.newBuilder().setVictoryPointValue(1).build()).build()).build();
			}
			
			ArrayList<Player> players = game1.getPlayers();
			
			for (int i = 0; i < players.size(); i++) {
				Catan.printToClient(m, players.get(i));
			}
			
			String type = developmentCard.getType();
			
			//if the development card is a victory point card, it is 
			//played immediately
			if (type.equals(VICTORY_POINT)) {
				
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
	public static void playDevelopmentCard(Player player, Game game1, Message enter, boolean hasPlayedDevCard, Scanner scanner) throws IOException {
		
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
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("You can only play one development card on your turn.").build()).build()).build(), player.getpSocket().getClientSocket());
			return;
		}
		if (playCards.size() <= 0) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("You don't have any development cards to play.").build()).build()).build(), player.getpSocket().getClientSocket());
			return;
		}
		
		int card = enter.getRequest().getPlayDevCardValue();
		DevelopmentCard play = null;
		
		for (int i = 0; i < playCards.size(); i++) {
			
			if (playCards.get(i).getType().equals(KNIGHT) && card == 0) {
				
				play = playCards.get(i);
				break;
			}
			if (playCards.get(i).getType().equals(ROAD_BUILDING) && card == 1) {
				
				play = playCards.get(i);
				break;
			}
			if (playCards.get(i).getType().equals(MONOPOLY) && card == 2) {
				
				play = playCards.get(i);
				break;
			}
			if (playCards.get(i).getType().equals(YEAR_OF_PLENTY) && card == 3) {
				
				play = playCards.get(i);
				break;
			}
		}
		
		if (play == null) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("you do not have this dev card").build()).build()).build(), player.getpSocket().getClientSocket());
			return;
		}
		
		String type = play.getType();
		Message m = Message.newBuilder().build();
		
	 	//selects the correct method depending on the type of card being played
		if (type.equals(KNIGHT)) {
			
			playKnightCard(player, game1, scanner);
			m = Message.newBuilder().setEvent(Event.newBuilder().setDevCardPlayed(PlayableDevCard.KNIGHT).build()).build();
		}
		if (type.equals(ROAD_BUILDING)) {
			
			playRoadBuildingCard(player, game1, scanner);
			m = Message.newBuilder().setEvent(Event.newBuilder().setDevCardPlayed(PlayableDevCard.ROAD_BUILDING).build()).build();
		}
		if (type.equals(YEAR_OF_PLENTY)) {
			
			cardPlayed = playYearOfPlentyCard(player, game1, scanner);
			m = Message.newBuilder().setEvent(Event.newBuilder().setDevCardPlayed(PlayableDevCard.YEAR_OF_PLENTY).build()).build();
		}
		if (type.equals(MONOPOLY)) {
			
			playMonopolyCard(player, game1, scanner);
			m = Message.newBuilder().setEvent(Event.newBuilder().setDevCardPlayed(PlayableDevCard.MONOPOLY).build()).build();
		}

		if (type.equals(VICTORY_POINT)) {
			
			playVictoryPointCard(player);
		}
		
	 	//if the card has been played, it is removed from the player's hand
		if (cardPlayed) {
			
			cards.remove(play);
			player.setDevelopmentCards(cards);
			
			ArrayList<Player> players = game1.getPlayers();
			for (int i = 0; i < players.size(); i++) {
				Catan.printToClient(m, players.get(i));
			}
		}
	}
	
 	//plays a knight card
	public static void playKnightCard(Player player, Game game1, Scanner scanner) {
		
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
				
				Catan.printToClient("You now have the largest army!", player);
				
				for (int i = 0; i < players.size(); i++) {
					if (players.get(i) != player) {
						Catan.printToClient("Player " + player.getName() + " now has the largest army!", players.get(i));
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
			
			Catan.printToClient("Where do you want to place your road?", player);
			Message enter = Message.newBuilder().build();
			
			boolean success = false;
			
			while (!success) {
				
				enter = Catan.getPBMsg(player.getpSocket().getClientSocket());
				
				if (enter.getRequest().getBodyCase().getNumber() == 3) {
					success = true;
				}
				else {
					Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not a road building request").build()).build()).build(), player.getpSocket().getClientSocket());
				}
			}
			
			Road.buildRoad(player, game1, scanner, enter, roadBuilding);
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
		
		Catan.printToClient("What resource do you want?", player);
		Message enter = Message.newBuilder().build();
		
		boolean success = false;
		
		while (!success) {
			
			enter = Catan.getPBMsg(player.getpSocket().getClientSocket());
			
			if (enter.getRequest().getBodyCase().getNumber() == 12) {
				success = true;
			}
			else {
				Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not a resource request").build()).build()).build(), player.getpSocket().getClientSocket());
			}
		}
		
		int choice = enter.getRequest().getChooseResourceValue();
		
		if (choice == 0) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not a resource").build()).build()).build(), player.getpSocket().getClientSocket());
			return false;
		}
		
		ArrayList<ResourceCard> brick = game1.getBrick();
		ArrayList<ResourceCard> lumber = game1.getLumber();
		ArrayList<ResourceCard> wool = game1.getWool();
		ArrayList<ResourceCard> ore = game1.getOre();
		ArrayList<ResourceCard> grain = game1.getGrain();
		
		//checks if resources can be taken
		//if not the card is not played
		if (brick.size() <= 0 && lumber.size() <= 0 && wool.size() <= 0 && ore.size() <= 0 && grain.size() <= 0) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("no resources left in bank. Cannot play card").build()).build()).build(), player.getpSocket().getClientSocket());
			return false;
		}
		
		//lets the player take a card from the bank
		switch (choice) {
			case 1 :
			if (brick.size() <= 0) {
				
				Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("no brick cards left").build()).build()).build(), player.getpSocket().getClientSocket());
				return false;
			}
			
			cards.add(brick.get(0));
			game1.getBrick().remove(brick);
			break;
			case 2 :
			if (lumber.size() <= 0) {
				
				Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("no lumber cards left").build()).build()).build(), player.getpSocket().getClientSocket());
				return false;			
			}
			
			cards.add(lumber.get(0));
			game1.getLumber().remove(lumber);
			break;
			case 3 :
			if (wool.size() <= 0) {
				
				Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("no wool cards left").build()).build()).build(), player.getpSocket().getClientSocket());
				return false;
			}
			
			cards.add(wool.get(0));
			game1.getWool().remove(wool);
			break;
			case 4 :			
			if (grain.size() <= 0) {
				
				Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("no grain cards left").build()).build()).build(), player.getpSocket().getClientSocket());
				return false;
			}
			
			cards.add(grain.get(0));
			game1.getGrain().remove(grain);
			break;
			case 5 :
			if (ore.size() <= 0) {
				
				Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("no ore cards left").build()).build()).build(), player.getpSocket().getClientSocket());
				return false;
			}
			
			cards.add(ore.get(0));
			game1.getOre().remove(ore);
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
		int resourceValue = 0;
		
		switch (resource) {
			case BRICK :
			resourceValue = 1;
			break;
			case LUMBER :
			resourceValue = 2;
			break;
			case WOOL :
			resourceValue = 3;
			break;
			case GRAIN :
			resourceValue = 4;
			break;
			case ORE :
			resourceValue = 5;
			break;			
		}
		
		if (resource.equals("")) {
			return;
		}
		
		ArrayList<ResourceCard> cards = player.getResourceCards();
		
		ArrayList<Player> players = game1.getPlayers();
		ArrayList<Steal> thefts = new ArrayList<Steal>();
		
		//takes the resources of specified type from each of the other
		//players' hands and gives them to the player
		for (int i = 0; i < players.size(); i++) {
			
			Player player2 = players.get(i);
			
			if (!player2.equals(player)) {
				
				ArrayList<ResourceCard> player2Cards = player2.getResourceCards();
				
				for (int j = 0; j < player2Cards.size(); j++) {
					
					ResourceCard card = player2Cards.get(i);
					
					if (card.getResource().equals(resource)) {
						
						Steal theft = Steal.newBuilder().setVictim(Board.Player.newBuilder().setIdValue(i).build()).setResourceValue(resourceValue).build();
						thefts.add(theft);
						
						player2Cards.remove(card);
						cards.add(card);
					}
				}
				
				player2.setResourceCards(player2Cards);
			}			
		}
		
		player.setResourceCards(cards);
		game1.setPlayers(players);
		
		Message m = Message.newBuilder().setEvent(Event.newBuilder().setMonopolyResolution(MultiSteal.newBuilder().addAllThefts(thefts).build()).build()).build();
		for (int i = 0; i < players.size(); i++) {
			Catan.printToClient(m, players.get(i));
		}
	}
	
	//lets the player choose the resource for monopoly
	public static String chooseResourceMonopoly(Scanner scanner, Player player) throws IOException {
		
		Catan.printToClient("What resource do you want?", player);
		Message enter = Message.newBuilder().build();
		
		boolean success = false;
		
		while (!success) {
			
			enter = Catan.getPBMsg(player.getpSocket().getClientSocket());
			
			if (enter.getRequest().getBodyCase().getNumber() == 12) {
				success = true;
			}
			else {
				Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not a resource request").build()).build()).build(), player.getpSocket().getClientSocket());
			}
		}
		
		int choice = enter.getRequest().getChooseResourceValue();
		
		if (choice == 0) {
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not a resource").build()).build()).build(), player.getpSocket().getClientSocket());
			return "";
		}
		
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