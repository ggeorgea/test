package game;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;


/**
 * Class to store information and methods concerning 
 * the robber
 */
public class Robber{

//-----Methods to play the robber for the turn-----//

	//checks if any players have more than seven cards when the robber
	//is activated
	public static void checkCardRemoval(Game game1, Scanner scanner) {

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
	public static void cardRemoval(Player player, ArrayList<ResourceCard> cards, Game game1, Scanner scanner) {

		//calculates how many cards to be removed
		int noCardsToRemove = cards.size()/2;

		System.out.println("Player " + player.getName() + ": Please select " + noCardsToRemove + " cards to remove");

		for (int i = 0; i < noCardsToRemove; i++) {

			System.out.println("Card " + i);

			//asks the player to choose a card to remove
			for (int j = 0; j < cards.size(); j++) {

				System.out.println(j + ": " + cards.get(j).getResource());
			}

			//removes the card they choose
			try {
				int choice = scanner.nextInt();

				if (choice < 0 || choice >= cards.size()) {
					System.out.println("Invalid choice. Please choose again");
					cardRemoval(player, cards, game1, scanner);
				}

				ResourceCard card = cards.get(choice);

				switch (card.getResource()) {
				case "ore" :
					ArrayList<ResourceCard> ore = game1.getOre();
					ore.add(card);
					break;
				case "lumber" :
					ArrayList<ResourceCard> lumber = game1.getLumber();
					lumber.add(card);
					break;
				case "brick" :
					ArrayList<ResourceCard> brick = game1.getBrick();
					brick.add(card);
					break;
				case "wool" :
					ArrayList<ResourceCard> wool = game1.getWool();
					wool.add(card);
					break;
				case "grain" :
					ArrayList<ResourceCard> grain = game1.getGrain();
					grain.add(card);
					break;
				}

				cards.remove(choice);
			}
			catch(InputMismatchException e) {
				System.out.println("Invalid choice. Please choose again");
				scanner.nextLine();
				cardRemoval(player,cards,game1,scanner);
			}
		}
	}

	//allows the player to move the robber and steal a card from a player
	public static void moveRobber(Player player, Game game1, Scanner scanner){

		try {
			System.out.println("Player " + player.getName() + ": Please select where to place the robber");

			System.out.println("Select X coordinate");
			int x = scanner.nextInt();

			System.out.println("Select Y coordinate");
			int y = scanner.nextInt();
			Coordinate a = new Coordinate(x, y);

			//checks the coordinates are in the correct range
			if((!((2*y <= x+8) && (2*y >= x-8) && (y <= 2*x+8) && (y >= 2*x-8) && (y >= -x-8) && (y <= -x+8)))
					|| (!game1.getBoard().getLocationFromCoordinate(a).getType().equals("hex"))) {

				System.out.println("Invalid coordinates. Please choose again");
				moveRobber(player, game1, scanner);
				return;
			}

			Hex hex1 = (Hex) game1.getBoard().getLocationFromCoordinate(a).getContains();
			hex1.setisRobberHere("R");
			game1.getBoard().setRobber(a);
				
			robberStealCard(player,  game1, scanner);
		}
		catch(InputMismatchException e){
			scanner.nextLine();
			moveRobber(player,game1,scanner);
		}
	}

	public static void robberStealCard(Player player, Game game1, Scanner scanner) {

		//choose a player to steal a card from
		Player target = null;
			
		while (true) {

			target = null;
			System.out.println("Please select player to steal from ");

			String  name = scanner.nextLine();
			ArrayList<Player> allPlayers = game1.getPlayers();
				
			//check that the player chose is valid
			//check if player exists 
			for (Player current: allPlayers) { 
				if (current.getName().equals(name)) { 
					target = current; 
				}
			}
			if (target == null) { 
				System.out.println("invalid player choice");
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
				
			if ((hex.getisRobberHere().equals("R"))) {
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
		
	
	public static void transferRandomCard(Player from, Player to ){ 
	
		Random r = new Random(); 
		ArrayList<ResourceCard> fromCards = from.getResourceCards();
		ArrayList<ResourceCard> toCards = to.getResourceCards();
		int index = r.nextInt(fromCards.size());
		ResourceCard card = fromCards.get(index);
	    fromCards.remove(card);
	    toCards.add(card);
	    
	    System.out.println("You have stolen: 1x " + card.getResource());
	}

	
	
	//TODO do we still need this?
	
	/*
	//robber will be put on different coordinates based on where the player has placed it 
	private Coordinate  c = new Coordinate(); 
	// p will represent the current player that moved the robber
	//if the player is null then no one is responsible for moving the robber at the moment 
	private Player  p = new Player(); 



	//new logic
	//player will roll dice and if they land on 7 robber will be activated or if someone plays knight card
	//robber will only perform actions if he is activated otherwise it cannot move 

	boolean activated = false; 

	public Robber(Coordinate c , Player p, Boolean activate){ 

		this.c = c ; 
		this.p = p; 
		this.activated = activate;
	}

	public Player getCurrentPlayer(){
		return p;
	}

	public Coordinate getCurrentCoordinate(){ 
		return c; 
	}

	public boolean getActivationStatus(){
		return activated;
	}

	public void setToActivated(){
		activated = true;
	}*/
}