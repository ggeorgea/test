package game;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class contains all the methods to allow players to do things on their turn
 */
public class Turn {

	private static final boolean END_GAME = true;
	private static final int END_TURN = 8;

	private static final int ROBBER = 7;

	//TODO do we need this?
	//create an instance of robber and a coordinate where it begins
	//Coordinate c = new Coordinate();
	//Robber robber = new Robber(c, null, null);

//-----Methods to actually let the player have their turn-----//

	//allows the player to have their turn
	public static boolean newTurn(Player player, Game game1, Scanner scanner){

		ArrayList<Player> players = game1.getPlayers();
		
		Map.printMap(game1.getBoard(), players);
		
		Dice.rollDice(player, scanner);

		if (player.getCurrentRoll() != ROBBER) {
			ResourceAllocation.resourceAllocation(player.getCurrentRoll(), game1, scanner);
		}
		else {
			Robber.checkCardRemoval(game1, scanner);
			Robber.moveRobber(player, game1, scanner);
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
					Player.printHand(player, scanner);
					break;
				case 5 :
					Map.printMap(game1.getBoard(), players);
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
