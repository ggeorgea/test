import java.util.ArrayList;
import java.util.Scanner;

public class Turn {
	
	private static final int END_TURN = 4;
	private static final int ROBBER = 7;
	
	public static void newTurn(Player player, Game game1, Scanner scanner) {
		
		Dice.rollDice(player, scanner);
		
		if (player.getCurrentRoll() != ROBBER) {
			resourceAllocation(player.getCurrentRoll(), game1, scanner);
		}
		else {
			checkCardRemoval(game1, scanner);
			moveRobber(player, game1, scanner);
		}
		
		int choice = 0;
		
		while (choice != END_TURN) {
			System.out.println("Player " + player.getName() + ": What do you want to do?");
			System.out.println("1: Build a road, settlement, city or development card?");
			System.out.println("2: Play a development card?");
			System.out.println("3: Trade with the bank, ports or other players?");
			System.out.println("4: End turn?");
			
			choice = scanner.nextInt();
			
			switch(choice) {
			case 1 :
				//build stuff
				break;
			case 2 :
				//play dev card
				break;
			case 3 : 
				//trade
				break;
			case 4 :
				break;
			default :
				System.out.println("Invalid choice. Please choose again");
			}
		}
		
		Map.printMap(game1.getBoard());
	}

//-----Methods to perform the resource allocation for the turn-----//
	
	
	public static void resourceAllocation(int hexValue, Game game1, Scanner scanner) {
		
		ArrayList<Hex> hexes = game1.getBoard().getHexes();
		
		for (int i = 0; i < hexes.size(); i++) {
			
			Hex hex = hexes.get(i);
			
			if (hex.getNumber() == hexValue) {
				if (!(hex.getisRbberHere().equals("R"))) {
					
					//checks the intersections around the hex
					//if a player owns one they get resources
					//1 for settlement, 2 for city
					
					/*if (player owns settlement) {
						String terrain = hex.getTerrain();
						getResources(player, terrain, 1);
					}*/
					/*if (player owns city) {
					String terrain = hex.getTerrain();
					getResources(player, terrain, 2);
					}*/
				}
			}
		}
	}
	
	public void getResources(Player player, String terrain, Game game1, int n) {
		
		ArrayList<ResourceCard> cards = player.getResourceCards();
		
		for (int i = 0; i < n; i++) {
		
			ResourceCard card = null;

			switch(terrain) {
			case "P" : 
				ArrayList<ResourceCard> wool = game1.getWool();
				card = wool.get(0);
				wool.remove(0);
				game1.setWool(wool);
				break;
			case "F" :
				ArrayList<ResourceCard> lumber = game1.getLumber();
				card = lumber.get(0);
				lumber.remove(0);
				game1.setLumber(lumber);
				break;
			case "M" :
				ArrayList<ResourceCard> ore = game1.getOre();
				card = ore.get(0);
				ore.remove(0);
				game1.setOre(ore);
				break;
			case "H" :
				ArrayList<ResourceCard> brick = game1.getBrick();
				card = brick.get(0);
				brick.remove(0);
				game1.setBrick(brick);
				break;
			case "G" :
				ArrayList<ResourceCard> grain = game1.getGrain();
				card = grain.get(0);
				grain.remove(0);
				game1.setGrain(grain);
				break;
			}

			if (card != null) {
				cards.add(card);
			}
		}
		
		player.setResourceCards(cards);
	}
	
//-----Methods to play the robber for the turn-----//
	
	
	public static void checkCardRemoval(Game game1, Scanner scanner) {
		
		ArrayList<Player> players = game1.getPlayers();
		
		for (int i = 0; i < players.size(); i++) {
			
			Player player = players.get(i);
			ArrayList<ResourceCard> cards = player.getResourceCards();
			
			if (cards.size() > ROBBER) {
					
				cardRemoval(player, cards, game1, scanner);
				player.setResourceCards(cards);
			}
		}
		
		game1.setPlayers(players);
	}
	
	public static void cardRemoval(Player player, ArrayList<ResourceCard> cards, Game game1, Scanner scanner) {
		
		int noCardsToRemove = cards.size()/2;
		
		System.out.println("Player " + player.getName() + ": Please select " + noCardsToRemove + " cards to remove");
		
		for (int i = 0; i < noCardsToRemove; i++) {
			
			System.out.println("Card " + i);
			
			for (int j = 0; j < cards.size(); j++) {
				
				System.out.println(j + ": " + cards.get(j).getResource());
			}
			
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
	}
	
	public static void moveRobber(Player player, Game game1, Scanner scanner) {
		
		System.out.println("Player " + player.getName() + ": Please select where to place the robber");
		
		System.out.println("Select X coordinate");
		int x = scanner.nextInt();
		
		System.out.println("Select Y coordinate");
		int y = scanner.nextInt();
		
		//checks the coordinates are in the correct range
		if (x < -4 || x > 4 || y < -4 || y > 4) {
			
			System.out.println("Invalid coordinates. Please choose again");
			moveRobber(player, game1, scanner);
		}
		
		Coordinate a = new Coordinate(x, y);
		
		//gets the hex and puts the robber there
		//lets the player steal a card
	}
}
