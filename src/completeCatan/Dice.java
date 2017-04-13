package completeCatan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Class used to roll the dice
 */
public class Dice {

	public static int valueDice = 0; 
	
	public static final String ROBBER = "R";
	public static final int DICE_SIDES = 6;
	
	//rolls the two dice
	public static void rollDice(Player player, Scanner scanner, Game game1) throws IOException {

		Random random = new Random();
		
		//prompts player to roll the dice and scans input
		Catan.printToClient("It is your turn. Press 'R' to roll", player);
		
		String enter = Catan.getInputFromClient(player, scanner).toUpperCase();
		
		//makes sure the user enters the correct input
		if (!(enter.equals(ROBBER))) {
			
			Catan.printToClient("Invalid input. Please roll again.", player);
			rollDice(player, scanner, game1);
			return;
		}
			
		//rolls the two dice and adds the sum
		//this preserves the probability of rolling values from
		//the actual game
		int dice1 = random.nextInt(DICE_SIDES) + 1;
		int dice2 = random.nextInt(DICE_SIDES) + 1;
		int diceRoll = dice1 + dice2;
		
		valueDice = diceRoll;
		
		//tells the player what they rolled
		Catan.printToClient("You rolled: " + diceRoll, player);
		
		ArrayList<Player> players = game1.getPlayers();
		
		//notifies other players of the dice roll
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i) != player) {
				Catan.printToClient("Player " + player.getName() + " rolled: " + diceRoll, players.get(i));
			}
		}
			
		player.setCurrentRoll(diceRoll);
	}

	//method to return the value of the dice 
	public int getCurrentValueOfDice(){
		return valueDice;
	}
}