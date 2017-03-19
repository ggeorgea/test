package game;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Class used to roll the dice
 */
public class Dice {

	public static int valueDice = 0; 
	
	//rolls the two dice
	public static void rollDice(Player player, Scanner scanner, Game game1) {

		Random random = new Random();
		
		//prompts player to roll the dice and scans input
		Catan.printToClient("It is your turn. Press 'R' to roll", player);
		String enter = scanner.next().toUpperCase();
			
		//makes sure the user enters the correct input
		if (!(enter.equals("R"))) {
			Catan.printToClient("Invalid input. Please roll again.", player);
			rollDice(player, scanner, game1);
			return;
		}
			
		//rolls the two dice and adds the sum
		//this preserves the probability of rolling values from
		//the actual game
		int dice1 = random.nextInt(6) + 1;
		int dice2 = random.nextInt(6) + 1;
		int diceRoll = dice1 + dice2;
		valueDice = diceRoll;
		Catan.printToClient("You rolled: " + diceRoll, player);
		ArrayList<Player> players = game1.getPlayers();
		for(int i = 0; i < players.size(); i++){
			if(players.get(i) != player){
				PlayerSocket socket = players.get(i).getpSocket();
				if(socket != null) socket.sendMessage("Player " + player.getName() + " rolled: " + diceRoll);
			}
		}
			
		player.setCurrentRoll(diceRoll);
	}

	//method to return the value of the dice 
	public int getCurrentValueOfDice(){

		return valueDice;
	}
}