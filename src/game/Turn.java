package game;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.IOException;

import intergroup.EmptyOuterClass.Empty;
import intergroup.Events.Event;
import intergroup.Events.Event.Error;
import intergroup.Messages.Message;
import intergroup.board.Board.Roll.Builder;

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
		
		Builder proRoll = Dice.rollTurnDice(player, scanner, game1);

		if (player.getCurrentRoll() != ROBBER) {
			proRoll.addAllResourceAllocation(ResourceAllocation.resourceAllocation(player.getCurrentRoll(), game1, scanner));
		}
		else {
			Robber.checkCardRemoval(game1, scanner);
			Robber.moveRobber(player, game1, scanner);
		}

		Message dicePlusAlloc = Message.newBuilder().setEvent(Event.newBuilder().setRolled(proRoll.build()).build()).build();
		for (int i = 0; i < players.size(); i++) {
			Catan.printToClient(dicePlusAlloc, players.get(i));		
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
				Catan.printToClient("PLEASE USE PROTOBUFF MESSAGES", player);
				
				int num =-1;
				
				Message enter = Message.newBuilder().build();
				
				boolean success = false;
				
				while (!success) {
					try {
						enter = Catan.getPBMsg(player.getpSocket().getClientSocket());
						
						if (!enter.getEvent().equals(Event.getDefaultInstance())) {
							
							Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not an appropriate message for this time").build()).build()).build(), player.getpSocket().getClientSocket());
							continue;
						}
						
						num = enter.getRequest().getBodyCase().getNumber();
						
						if (!(num == 1 || num == 6 || num == 9 || num == 10 || num == 11 || num == 12 || num == 14)) {
							success = true;
						}
						else{
							Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not an appropriate message for this time").build()).build()).build(), player.getpSocket().getClientSocket());
						}
					} 
					catch (IOException e) {
					}
				}
				
				switch(num){
				case 2:
					//buydevcard
					DevelopmentCard.buyDevelopmentCard(player, game1, enter);
					break;
				case 3:
					//buildroad
					Road.buildRoad(player, game1, scanner, enter, false);
					break;
				case 4:
					//buildsettlement
					game.Building.buildSettlement(player, game1, scanner, enter);
					break;
				case 5:
					//buildcity
					game.Building.buildCity(player, game1, scanner, enter);
					break;
				case 7:
					//play dev card
					DevelopmentCard.playDevelopmentCard(player, game1, enter, hasPlayedDevCard, scanner);
					hasPlayedDevCard = true;
					break;
				case 8:
					trade(player, scanner, game1, enter);
					break;
				case 13:
					choice = END_TURN;
					break;
				case 15:
					//TODO chatmessage, treating it as a normal choice for now!
					String chatMsg = enter.getRequest().getChatMessage();
					switch (Integer.parseInt(chatMsg)) {
					case 1 :
						build(player, game1, scanner, enter);
						break;
					case 2 :
						DevelopmentCard.playDevelopmentCard(player, game1, enter, hasPlayedDevCard, scanner);
						hasPlayedDevCard = true;
						break;
					case 3 :
						trade(player, scanner, game1, enter);
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
					
					break;
				}
				
				hasEnded = Game.checkEndOfGame(player, game1);
			}
			catch(InputMismatchException e) {
				Catan.printToClient("Invalid choice. Please choose again", player);
				scanner.nextLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Player.updateDevelopmentCards(player);
		
		//TODO turn ended event
		Message m = Message.newBuilder().setEvent(Event.newBuilder().setTurnEnded(Empty.newBuilder().build()).build()).build();
		Catan.printToClient(m, player);
		
		return hasEnded;
	}

//-----Method to build and buy things for the turn-----//

	//asks the player if they want to build something
	public static void build(Player player, Game game1, Scanner scanner, Message enter) throws IOException {

		Catan.printToClient("What do you want to build?", player);
		Catan.printToClient("1. Road", player);
		Catan.printToClient("2. Settlement", player);
		Catan.printToClient("3. City", player);
		Catan.printToClient("4. Development Card", player);

		int choice = Integer.parseInt(Catan.getInputFromClient(player, scanner));

		switch(choice) {
		case 1 :
			Road.buildRoad(player, game1, scanner, enter, false);
			break;
		case 2 :
			Building.buildSettlement(player, game1, scanner, enter);
			break;
		case 3 :
			Building.buildCity(player, game1, scanner, enter);
			break;
		case 4 :
			DevelopmentCard.buyDevelopmentCard(player, game1, enter);
			break;
		default :
			Catan.printToClient("Invalid choice, Please choose again", player);
			build(player, game1, scanner, enter);
		}
	}

//-----Method to allow the player to trade-----//

	public static void trade(Player player, Scanner scanner, Game game1, Message enter) throws IOException {
		
		boolean bank = Trade.tradeBankOrPlayer(player, scanner);
		int trade = enter.getRequest().getInitiateTrade().getTradeCase().getNumber();
		
		if (bank || trade == 1) {
			Trade.tradeBank(player, scanner, enter, game1);
		}
		else {
			Trade.tradePlayer(player, scanner, enter, game1);
		}
	}
}
