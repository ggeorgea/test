package game;
import intergroup.Events.Event;
import intergroup.Events.Event.Error;
import intergroup.Messages.Message;

import java.io.IOException;
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
		
		Dice.rollDice(player, scanner, game1);

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
				while(!success){
				try {
					enter = Catan.getPBMsg(player.getpSocket().getClientSocket());
					if(!enter.getEvent().equals(Event.getDefaultInstance())) {
						Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not an appropriate message for this time").build()).build()).build(), player.getpSocket().getClientSocket());
						continue;}
					num = enter.getRequest().getBodyCase().getNumber();
					if(!(num==1||num==6||num==9||num==10||num==11||num==12||num==14)){
						success=true;
					}
					else{
						Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not an appropriate message for this time").build()).build()).build(), player.getpSocket().getClientSocket());
					}
				} catch (IOException e) {
				}
				}
				switch(num){
				case 2:
					//buydevcard
					DevelopmentCard.buyDevelopmentCard(player, game1, enter);
					break;
				case 3:
					//buildroad
					Road.buildRoad(player, game1, enter, false);
					break;
				case 4:
					//buildsettlement
					Building.buildSettlement(player, game1, enter);
					break;
				case 5:
					//buildcity
					Building.buildCity(player, game1, scanner);
					break;
				case 7:
					//play dev card
					DevelopmentCard.playDevelopmentCard(player, game1, enter, hasPlayedDevCard);
					hasPlayedDevCard = true;
					break;
				case 8:
					trade(player, enter, game1);
					break;
				case 13:
					choice = END_TURN;
					break;
				case 15:
					//TODO chatmessage, treating it as a normal choice for now!
					String chatMsg = enter.getRequest().getChatMessage();
					switch (Integer.parseInt(chatMsg)) {
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
					
					break;
				}
				
				//choice  = Integer.parseInt(Catan.getInputFromClient(player, scanner));

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
		return hasEnded;
	}

//-----Method to build and buy things for the turn-----//

	private static void trade(Player player, Message enter, Game game1) {
		// TODO Auto-generated method stub
		
	}

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

	public static void trade(Player player, Scanner scanner, Game game1){
		boolean bank = Trade.tradeBankOrPlayer(player, scanner);
		if (bank) {
			Trade.tradeBank(player, scanner, game1);
		}
		else {
			Trade.tradePlayer(player, scanner, game1);
		}
	}
}
