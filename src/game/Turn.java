package game;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * Class contains all the methods to allow players to do things on their turn
 */
public class Turn {

	private static final boolean END_GAME = true;
	private static final int END_TURN = 8;

	//why is the robber initialized as 7?
	private static final int ROBBER = 7;

	//create an instance of robber and a coordinate where it begins
	Coordinate c = new Coordinate();
	Robber robber = new Robber(c, null, null);

//-----Methods to actually let the player have their turn-----//

	//allows the player to have their turn
	public static boolean newTurn(Player player, Game game1, Scanner scanner){

		Map.printMap(game1.getBoard());

		Dice.rollDice(player, scanner);

		if (player.getCurrentRoll() != ROBBER) {
			ResourceAllocation.resourceAllocation(player.getCurrentRoll(), game1, scanner);
		}
		else {
			checkCardRemoval(game1, scanner);
			moveRobber(player, game1, scanner);
			//TODO card steal?
		}

		int choice = 0;
		boolean hasPlayedDevCard = false;
		boolean hasEnded = !END_GAME;

		while (choice != END_TURN && !hasEnded) {
			try{
				System.out.println("Player " + player.getName() + ": What do you want to do?");
				System.out.println("1: Build a road, settlement, city or development card?");
				System.out.println("2: Play a development card?");
				System.out.println("3: Trade with the bank, ports or other players?");
				System.out.println("4: Show your hand?");
				System.out.println("5: Print map?");
				System.out.println("6: Length of your Longest Road?");
				System.out.println("7: Count your victory pionts");
				System.out.println("8: End turn?");

				choice = scanner.nextInt();

				switch(choice) {
				case 1 :
					build(player, game1, scanner);
					break;
				case 2 :
					DevelopmentCard.playDevelopmentCard(player, game1, scanner, hasPlayedDevCard);
					hasPlayedDevCard = true;
					break;
				case 3 :
					trade(player, scanner, game1);
					break;
				case 4 :
					Player.printResourceCards(player);
					break;
				case 5 :
					Map.printMap(game1.getBoard());
					break;
				case 6 :
					System.out.println("Your Longest Road Length is: " + player.getLongestRoad());
					break;
				case 7:
					System.out.println("You have " + player.getVictoryPoints() + " victory points");
					break;
				case 8:
					break;
				default :
					System.out.println("Invalid choice. Please choose again");
				}

				hasEnded = Game.checkEndOfGame(player);
			}
			catch(InputMismatchException e) {
				System.out.println("Invalid choice. Please choose again");
				scanner.nextLine();
			}
		}

		Player.updateDevelopmentCards(player);
		return hasEnded;
	}

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
			//TODO gets the hex and puts the robber there
			robberStealCard(player,  game1, scanner);
		}
		catch(InputMismatchException e){
			scanner.nextLine();
			moveRobber(player,game1,scanner);
		}
	}

	public static void robberStealCard(Player player, Game game1, Scanner scanner){
		//choose a player to steal a card from
		Player target = null;
		
		while(true) {
			target = null;
			System.out.println("Please select player to steal from ");
			String  name = scanner.nextLine();
			ArrayList<Player> allPlayers = game1.getPlayers();
			
			//check that the player chose is valid
			//check if player exists 
			for(Player current: allPlayers){ 
				if (current.getName().equals(name)){ 
					target = current; 
				}
			}
			if(target == null){ 
				System.out.println("invalid player choice");
				continue;
			} else {
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
				for (Coordinate c : getNearbyCoordinates(hex.getCoordinate())){ 
					Player ownerOfLocation = ((Intersection) game1.getBoard().getLocationFromCoordinate(c).getContains()).getOwner();	
					if(ownerOfLocation == target){ 
						allowedToSteal = true; 
					}
				}
				
			}
		}
		if( allowedToSteal){ 
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
	}

//-----Method to build and buy things for the turn-----//

	//asks the player if they want to build something
	public static void build(Player player, Game game1, Scanner scanner) {

		System.out.println("What do you want to build?");
		System.out.println("1. Road");
		System.out.println("2. Settlement");
		System.out.println("3. City");
		System.out.println("4. Development Card");

		int choice = scanner.nextInt();

		switch(choice) {
		case 1 :
			Road.buildRoad(player, game1, scanner, false);
			break;
		case 2 :
			Building.buildSettlement(player, game1, scanner);
			break;
		case 3 :
			Building.buildCity(player, game1, scanner);
			break;
		case 4 :
			DevelopmentCard.buyDevelopmentCard(player, game1, scanner);
			break;
		default :
			System.out.println("Invalid choice, Please choose again");
			build(player, game1, scanner);
		}
	}

//-----Method to allow the player to trade-----//

	public static void trade(Player player, Scanner scanner, Game game1){
		boolean bank = Trade.tradeBankOrPlayer(scanner);
		if(bank) Trade.tradeBank(player, scanner, game1);
		else Trade.tradePlayer(player, scanner, game1);
	}
}
