package projectX;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.IOException;
import intergroup.Events.Event;
import intergroup.Events.Event.Error;
import intergroup.Messages.Message;
import intergroup .board.Board;
import intergroup.board.Board.Roll.Builder;

/**
 * Class used to roll the dice
 */
public class Dice {

	public static int valueDice = 0; 
	public static Builder rollTurnDice(Player player, Scanner scanner, Game game1) {

		Random random = new Random();
		
		//prompts player to roll the dice and scans input
		Catan.printToClient("It is your turn. please send the serve a roll dice request", player);
		
		Message enter;
		boolean success = false;
		
		while (!success) {
			try {
				
				enter = Catan.getPBMsg(player.getpSocket().getClientSocket());
				
				if (enter.getRequest().getBodyCase().getNumber() == 1) {
					success = true;
				}
				else{
					Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not a roll dice request").build()).build()).build(), player.getpSocket().getClientSocket());
				}
			} 
			catch (IOException e) {
			}
		}
		
		//rolls the two dice and adds the sum
		//this preserves the probability of rolling values from
		//the actual game
		int dice1 = random.nextInt(6) + 1;
		int dice2 = random.nextInt(6) + 1;
		int diceRoll = dice1 + dice2;
		
		valueDice = diceRoll;
		
		int playerNum = 0;
		
		for (int i = 0; i < game1.getPlayers().size(); i++) {
			if (game1.getPlayers().get(i).equals(player)) {
				playerNum = i;
			}
		}
		
		player.setCurrentRoll(diceRoll);
		return intergroup.board.Board.Roll.newBuilder().setA(dice1).setB(dice2);
	}
	
	
	//rolls the two dice
	public static void rollDice(Player player, Scanner scanner, Game game1) {

		Random random = new Random();
		
		//prompts player to roll the dice and scans input
		Catan.printToClient("It is your turn. please send the serve a roll dice request", player);
		
		Message enter;
		boolean success = false;
		
		while (!success) {
			try {
				
				enter = Catan.getPBMsg(player.getpSocket().getClientSocket());
				
				if (enter.getRequest().getBodyCase().getNumber() == 1) {
					success = true;
				}
				else{
					Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not a roll dice request").build()).build()).build(), player.getpSocket().getClientSocket());
				}
			} 
			catch (IOException e) {
			}
		}
		
		//rolls the two dice and adds the sum
		//this preserves the probability of rolling values from
		//the actual game
		int dice1 = random.nextInt(6) + 1;
		int dice2 = random.nextInt(6) + 1;
		int diceRoll = dice1 + dice2;
		
		valueDice = diceRoll;
		
		int playerNum = 0;
		
		for (int i = 0; i < game1.getPlayers().size(); i++) {
			if (game1.getPlayers().get(i).equals(player)) {
				playerNum = i;
			}
		}
		
		Message m = Message.newBuilder().setEvent(Event.newBuilder().setInstigator(Board.Player.newBuilder().setIdValue(playerNum).build()).setRolled(Board.Roll.newBuilder().setA(dice1).setB(dice2).build()).build()).build();
		Catan.printToClient(m, player);
		
		ArrayList<Player> players = game1.getPlayers();
		
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i) != player) {
				Catan.printToClient(m, players.get(i));
			}
		}
		
		player.setCurrentRoll(diceRoll);
	}

	//method to return the value of the dice 
	public int getCurrentValueOfDice(){
		return valueDice;
	}
}