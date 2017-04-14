package completeCatan;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import java.io.IOException;

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
	
	private static final int CARD_REMOVAL = 7;

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
			if (cards.size() > CARD_REMOVAL) {

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

		Catan.printToClient("Please select " + noCardsToRemove + " cards to remove", player);
		for (int i = 0; i < noCardsToRemove; i++) {

			//asks the player to choose a card to remove
			for (int j = 0; j < cards.size(); j++) {

				Catan.printToClient(j + ": " + cards.get(j).getResource(), player);
			}

			//removes the card they choose
			try {
				int choice = Integer.parseInt(Catan.getInputFromClient(player, scanner));

				if (choice < 0 || choice >= cards.size()) {
					
					Catan.printToClient("Invalid choice. Please choose again", player);
					cardRemoval(player, cards, game1, scanner);
				}

				ResourceCard card = cards.get(choice);

				switch (card.getResource()) {
				case ORE :
					ArrayList<ResourceCard> ore = game1.getOre();
					ore.add(card);
					break;
				case LUMBER :
					ArrayList<ResourceCard> lumber = game1.getLumber();
					lumber.add(card);
					break;
				case BRICK :
					ArrayList<ResourceCard> brick = game1.getBrick();
					brick.add(card);
					break;
				case WOOL :
					ArrayList<ResourceCard> wool = game1.getWool();
					wool.add(card);
					break;
				case GRAIN :
					ArrayList<ResourceCard> grain = game1.getGrain();
					grain.add(card);
					break;
				}

				cards.remove(choice);
			}
			catch(InputMismatchException e) {
				
				Catan.printToClient("Invalid choice. Please choose again", player);
				scanner.nextLine();
				cardRemoval(player,cards,game1,scanner);
			}
		}
	}

	//allows the player to move the robber and steal a card from a player
	public static void moveRobber(Player player, Game game1, Scanner scanner) throws IOException {

		try {
			Catan.printToClient("Please select where to place the robber", player);

			Catan.printToClient("Select X coordinate", player);
			int x = Integer.parseInt(Catan.getInputFromClient(player, scanner));

			Catan.printToClient("Select Y coordinate", player);
			int y = Integer.parseInt(Catan.getInputFromClient(player, scanner));
			Coordinate a = new Coordinate(x, y);

			//checks the coordinates are in the correct range
			if((!((2*y <= x+8) && (2*y >= x-8) && (y <= 2*x+8) && (y >= 2*x-8) && (y >= -x-8) && (y <= -x+8)))
					|| (!game1.getBoard().getLocationFromCoordinate(a).getType().equals(HEX))) {

				Catan.printToClient("Invalid coordinates. Please choose again", player);
				moveRobber(player, game1, scanner);
				return;
			}

			Hex hex1 = (Hex) game1.getBoard().getLocationFromCoordinate(a).getContains();
			hex1.setisRobberHere(ROBBER);
			game1.getBoard().setRobber(a);
				
			robberStealCard(player,  game1, scanner);
		}
		catch(InputMismatchException e) {
			
			scanner.nextLine();
			moveRobber(player,game1,scanner);
		}
	}

	//lets the player steal a card from another player
	public static void robberStealCard(Player player, Game game1, Scanner scanner) throws IOException {

		//choose a player to steal a card from
		Player target = null;
		ArrayList<Player> players = game1.getPlayers();
		players.remove(player);
			
		//ensures correct user input
		while (true) {

			target = null;
			Catan.printToClient("Which player do you want to steal from?", player);
			
			for (int i = 0; i < players.size(); i++) {
				System.out.println(i+1 + ": " + players.get(i));
			}

			int choice = Integer.parseInt(Catan.getInputFromClient(player, scanner));
			
			if (choice <= 0 || choice > players.size()) {
				Catan.printToClient("Invalid player choice. Please choose again.", player);
				continue;
			}
			else {
				target = players.get(choice-1);
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
			transferRandomCard(target, player);
		}
		else {
			
			Catan.printToClient("Cannot steal from this player. They must have a settlement near the robber", player);
			robberStealCard(player, game1, scanner);
		}
	}
	
	//method to return all coord given hex 
	public static ArrayList<Coordinate> getNearbyCoordinates(Coordinate coordinate){ 
	
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
		
	//randomly picks a card from the other player's hand to gives
	//it to the current player
	public static void transferRandomCard(Player from, Player to ){ 
	
		Random r = new Random(); 
		ArrayList<ResourceCard> fromCards = from.getResourceCards();
		ArrayList<ResourceCard> toCards = to.getResourceCards();
		
		int index = r.nextInt(fromCards.size());
		ResourceCard card = fromCards.get(index);
		
	    fromCards.remove(card);
	    toCards.add(card);
	    
	    Catan.printToClient("Stolen from you: 1x " + card.getResource(), from);
	    Catan.printToClient("You have stolen: 1x " + card.getResource(), to);
	}
}