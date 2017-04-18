package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import intergroup.Events.Event;
import intergroup.Events.Event.Error;
import intergroup.Messages.Message;
import intergroup.board.Board;
import intergroup.board.Board.Point;
import intergroup.board.Board.Steal;
import intergroup.resource.Resource;

/**
 * Class to store information and methods concerning 
 * the robber
 */
public class Robber {
	
	private static final String GRAIN = "grain";
	private static final String WOOL = "wool";
	private static final String BRICK = "brick";
	private static final String LUMBER = "lumber";
	private static final String ORE = "ore";
	
	private static final String HEX = "hex";
	private static final String ROBBER = "R";

//-----Methods to play the robber for the turn-----//

	//checks if any players have more than seven cards when the robber
	//is activated
	public static void checkCardRemoval(Game game1, Scanner scanner) throws IOException {

		ArrayList<Player> players = game1.getPlayers();

		for (int i = 0; i < players.size(); i++) {

			Player player = players.get(i);
			ArrayList<ResourceCard> cards = player.getResourceCards();

			//if the player has more than seven cards, they must
			//remove cards from their hand
			if (cards.size() > 7) {

				cardRemoval(player, cards, game1, scanner);
				player.setResourceCards(cards);
			}
		}

		game1.setPlayers(players);
	}

	//asks players with more than seven cards to choose the cards they remove
	public static void cardRemoval(Player player, ArrayList<ResourceCard> cards, Game game1, Scanner scanner) throws IOException {

		//calculates how many cards to be removed
		int noCardsToRemove = cards.size()/2;
		
		Catan.printToClient("Please choose resources to discard.", player);
		Message enter = Message.newBuilder().build();
		
		boolean success = false;
		
		while (!success) {
			
			enter = Catan.getPBMsg(player.getpSocket().getClientSocket());
			
			if (enter.getRequest().getBodyCase().getNumber() == 10) {
				success = true;
			}
			else {
				Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not a move robber request").build()).build()).build(), player.getpSocket().getClientSocket());
			}
		}
		
		int noBrick = enter.getRequest().getDiscardResources().getBrick();
		int noOre  = enter.getRequest().getDiscardResources().getOre();
		int noLumber = enter.getRequest().getDiscardResources().getLumber();
		int noWool = enter.getRequest().getDiscardResources().getWool();
		int noGrain = enter.getRequest().getDiscardResources().getGrain();
		
		if (noBrick + noWool + noLumber + noWool + noGrain != noCardsToRemove) {
			
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not the correct amount of resources").build()).build()).build(), player.getpSocket().getClientSocket());
			cardRemoval(player, cards, game1, scanner);
			return;
		}
		else {
			
			ArrayList<ResourceCard> brick = game1.getBrick();
			ArrayList<ResourceCard> ore = game1.getOre();
			ArrayList<ResourceCard> lumber = game1.getLumber();
			ArrayList<ResourceCard> wool = game1.getWool();
			ArrayList<ResourceCard> grain = game1.getGrain();
			
			for (int i = 0; i < noBrick; i++) {
				for (int j = 0; j < cards.size(); j++) {
					if (cards.get(j).getResource().equals(BRICK)) {
						
						brick.add(cards.get(j));
						cards.remove(cards.get(j));
					}
				}
			}
			
			for (int i = 0; i < noOre; i++) {
				for (int j = 0; j < cards.size(); j++) {
					if (cards.get(j).getResource().equals(BRICK)) {
						
						ore.add(cards.get(j));
						cards.remove(cards.get(j));
					}
				}
			}
			
			for (int i = 0; i < noLumber; i++) {
				for (int j = 0; j < cards.size(); j++) {
					if (cards.get(j).getResource().equals(BRICK)) {
						
						lumber.add(cards.get(j));
						cards.remove(cards.get(j));
					}
				}
			}
			
			for (int i = 0; i < noWool; i++) {
				for (int j = 0; j < cards.size(); j++) {
					if (cards.get(j).getResource().equals(BRICK)) {
						
						wool.add(cards.get(j));
						cards.remove(cards.get(j));
					}
				}
			}
			
			for (int i = 0; i < noGrain; i++) {
				for (int j = 0; j < cards.size(); j++) {
					if (cards.get(j).getResource().equals(BRICK)) {
						
						grain.add(cards.get(j));
						cards.remove(cards.get(j));
					}
				}
			}			
			
			game1.setBrick(brick);
			game1.setOre(ore);
			game1.setLumber(lumber);
			game1.setWool(wool);
			game1.setGrain(grain);	
			
			Message m = Message.newBuilder().setEvent(Event.newBuilder().setCardsDiscarded(Resource.Counts.newBuilder().setBrick(noBrick).setOre(noOre).setLumber(noLumber).setGrain(noGrain).setWool(noWool).build()).build()).build();
			
			ArrayList<Player> players = game1.getPlayers();
			
			for (int i = 0; i < players.size(); i++) {
				Catan.printToClient(m, players.get(i));	
			}

		}
	}

	//allows the player to move the robber and steal a card from a player
	public static void moveRobber(Player player, Game game1, Scanner scanner) {

		try {
			
			Catan.printToClient("Please send a move robber request.", player);
			Message enter = Message.newBuilder().build();
			
			boolean success = false;
			
			while (!success) {
				
				enter = Catan.getPBMsg(player.getpSocket().getClientSocket());
				
				if (enter.getRequest().getBodyCase().getNumber() == 6) {
					success = true;
				}
				else {
					Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not a move robber request").build()).build()).build(), player.getpSocket().getClientSocket());
				}
			}
			
			int x = enter.getRequest().getMoveRobber().getX();
			int y = enter.getRequest().getMoveRobber().getY();
			Coordinate a = new Coordinate(x, y);

			//checks the coordinates are in the correct range
			if((!((2*y <= x+8) && (2*y >= x-8) && (y <= 2*x+8) && (y >= 2*x-8) && (y >= -x-8) && (y <= -x+8)))
				|| (!game1.getBoard().getLocationFromCoordinate(a).getType().equals(HEX))) {

				Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("Invalid coordinates.").build()).build()).build(), player.getpSocket().getClientSocket());
				return;
			}

			Hex hex1 = (Hex) game1.getBoard().getLocationFromCoordinate(a).getContains();
			hex1.setisRobberHere(ROBBER);
			game1.getBoard().setRobber(a);
		
			ArrayList<Player> players = game1.getPlayers();
			Message m = Message.newBuilder().setEvent(Event.newBuilder().setRobberMoved(Point.newBuilder().setX(x).setY(y).build()).build()).build();
		
			for (int i = 0; i < players.size(); i++) {
				Catan.printToClient(m, players.get(i));
			}			
		
			robberStealCard(player,  game1, scanner);
		}
		catch(InputMismatchException e) {
		
			scanner.nextLine();
			moveRobber(player,game1,scanner);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void robberStealCard(Player player, Game game1, Scanner scanner) throws IOException {

		//choose a player to steal a card from
		Player target = null;
	
		while (true) {

			target = null;
		
			Catan.printToClient("Who do you want to steal from?", player);
			Message enter = Message.newBuilder().build();
		
			boolean success = false;
		
			while (!success) {
			
				enter = Catan.getPBMsg(player.getpSocket().getClientSocket());
			
				if (enter.getRequest().getBodyCase().getNumber() == 11) {
					success = true;
				}
				else {
					Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not a target player request").build()).build()).build(), player.getpSocket().getClientSocket());
				}
			}
		
			int id = enter.getRequest().getSubmitTargetPlayer().getIdValue();

			ArrayList<Player> allPlayers = game1.getPlayers();
		
			if (id >= 0 && id < allPlayers.size()-1) {
				target = allPlayers.get(id);
			}
		
			if (target == null || target == player) { 
			
				Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("incorrect player choice").build()).build()).build(), player.getpSocket().getClientSocket());
				continue;
			} 
			else {
				break;
			}
		}
	
		ArrayList<Hex> hexes = game1.getBoard().getHexes();
		boolean allowedToSteal = false; 
	
		for (int i = 0; i < hexes.size(); i++) {
		
			Hex hex = hexes.get(i);
		
			//if the hex value is the same as the dice roll and the robber
			//is not there, resources can be given out
			if ((hex.getisRobberHere().equals(ROBBER))) {
				for (Coordinate c : getNearbyCoordinates(hex.getCoordinate())) { 
				
					Player ownerOfLocation = ((Intersection) game1.getBoard().getLocationFromCoordinate(c).getContains()).getOwner();	
				
					if (ownerOfLocation == target) { 
						allowedToSteal = true; 
					}
				}
			}
		}
		if (allowedToSteal) { 
			transferRandomCard(target, player, game1);
		}
	}

	//method to return all coord given hex 
	public static ArrayList<Coordinate> getNearbyCoordinates(Coordinate coordinate) { 
	
		ArrayList<Coordinate> nearbyCoordinates = new ArrayList<>(); 
		int x = coordinate.getX();
		int y = coordinate.getY();
	
		nearbyCoordinates.add(new Coordinate(x, y-1));
		nearbyCoordinates.add(new Coordinate(x, y+1));
		nearbyCoordinates.add(new Coordinate(x-1, y));
		nearbyCoordinates.add(new Coordinate(x+1, y));
		nearbyCoordinates.add(new Coordinate(x-1, y-1));
		nearbyCoordinates.add(new Coordinate(x+1, y+1));
		
		return nearbyCoordinates; 
	
	}

	public static void transferRandomCard(Player from, Player to, Game game1) { 
	
		Random r = new Random(); 
		ArrayList<ResourceCard> fromCards = from.getResourceCards();
		ArrayList<ResourceCard> toCards = to.getResourceCards();
		int index = r.nextInt(fromCards.size());
		ResourceCard card = fromCards.get(index);
		fromCards.remove(card);
		toCards.add(card);
	
		ArrayList<Player> players = game1.getPlayers();
		
		int playerNum = 0;
		int playerStealNum = 0;
	
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).equals(from)) {
				playerNum = i;
			}
			else if (players.get(i).equals(to)) {
				playerStealNum = i;
			}
		}
	
		int resource = 0;
	
		switch (card.getResource()) {
		case BRICK : 
			resource = 1;
			break;
		case LUMBER :
			resource = 2;
			break;
		case WOOL :
			resource = 3;
			break;
		case GRAIN :
			resource = 4;
			break;
		case ORE :
			resource = 5;
			break;
		}

		Message m = Message.newBuilder().setEvent(Event.newBuilder().setInstigator(Board.Player.newBuilder().setIdValue(playerNum).build()).setResourceStolen(Steal.newBuilder().setVictim(Board.Player.newBuilder().setIdValue(playerStealNum).build()).setResourceValue(resource).build()).build()).build();
	
		for (int i = 0; i < players.size(); i++) {
			Catan.printToClient(m, players.get(i));
		}
	}
}