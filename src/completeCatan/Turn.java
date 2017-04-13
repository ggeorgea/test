package completeCatan;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import java.io.IOException;
 
/**
 * Class contains all the methods to allow players to do things on their turn
 */
public class Turn {
 
 	private static final boolean END_GAME = true;
 	private static final int END_TURN = 8;
 
 	private static final int ROBBER = 7;
 
//-----Methods to actually let the player have their turn-----//
 
 	//allows the player to have their turn
 	public static boolean newTurn(Player player, Game game1, Scanner scanner) throws IOException{
 
 		ArrayList<Player> players = game1.getPlayers();
 		Map.printMap(game1.getBoard(), players);
 		Dice.rollDice(player, scanner, game1);
 
 		if (player.getCurrentRoll() != ROBBER) {
 			ResourceAllocation.resourceAllocation(player.getCurrentRoll(), game1, scanner);
 		}
 		else {
 			
 			Robber.checkCardRemoval(game1, scanner);
 			Robber.moveRobber(player, game1, scanner);
 		}
 
 		int choice = 0;
 		boolean hasPlayedDevCard = false;
 		boolean hasEnded = !END_GAME;
 
 		while (choice != END_TURN && !hasEnded) {
 			try{
 				Catan.printToClient("What do you want to do?", player);
 				Catan.printToClient("1: Build a road, settlement, city or development card?", player);
 				Catan.printToClient("2: Play a development card?", player);
 				Catan.printToClient("3: Trade with the bank, ports or other players?", player);
 				Catan.printToClient("4: See your hand?", player);
 				Catan.printToClient("5: Print map?", player);
 				Catan.printToClient("6: Length of your Longest Road?", player);
 				Catan.printToClient("7: Count your victory pionts", player);
 				Catan.printToClient("8: End turn?", player);
 
 				choice = scanner.nextInt();
 				
				switch (choice) {
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
 					Catan.printToClient("Your Longest Road Length is: " + player.getLongestRoad(), player);
 					break;
 				case 7:
 					Catan.printToClient("You have " + player.getVictoryPoints() + " victory points", player);
 					break;
 				case 8:
 					break;
 				default :
 					Catan.printToClient("Invalid choice. Please choose again", player);
 				}
 
 				hasEnded = Game.checkEndOfGame(player, game1);
 			}
 			catch(InputMismatchException e) {
 				Catan.printToClient("Invalid choice. Please choose again", player);
 				scanner.nextLine();
 			}
 		}
 
 		Player.updateDevelopmentCards(player);
 		return hasEnded;
 	}
 
 //-----Method to build and buy things for the turn-----//
 
 	//asks the player if they want to build something
 	public static void build(Player player, Game game1, Scanner scanner) throws IOException {
 
 		Catan.printToClient("What do you want to build?", player);
 		Catan.printToClient("1. Road", player);
 		Catan.printToClient("2. Settlement", player);
 		Catan.printToClient("3. City", player);
 		Catan.printToClient("4. Development Card", player);
 
		int choice = Integer.parseInt(Catan.getInputFromClient(player, scanner));
 
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
 			Catan.printToClient("Invalid choice, Please choose again", player);
 			build(player, game1, scanner);
 		}
 	}
 
 //-----Method to allow the player to trade-----//
 
 	public static void trade(Player player, Scanner scanner, Game game1) throws IOException{
 		
 		boolean bank = Trade.tradeBankOrPlayer(player, scanner);
		if (bank) {
			Trade.tradeBank(player, scanner, game1);
		}
		else {
			Trade.tradePlayer(player, scanner, game1);
		}
 	}
}