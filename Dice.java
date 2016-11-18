import java.util.Random;
import java.util.Scanner;

public class Dice {

	//rolls the two dice
	public static void rollDice(Player player, Scanner scanner) {
			
		Random random = new Random();
		
		System.out.println("Player " + player.getName() + ": press 'R' to roll.");
		String enter = scanner.next().toUpperCase();
			
		if (!(enter.equals("R"))) {
				
			System.out.println("Invalid input. Please roll again.");
			rollDice(player, scanner);
			return;
		}
			
		//rolls the two dice and adds the sum
		//this preserves the probability of rolling values from
		//the actual game
		int dice1 = random.nextInt(6) + 1;
		int dice2 = random.nextInt(6) + 1;
		int diceRoll = dice1 + dice2;
			
		System.out.println("Player " + player.getName() + " rolls: " + (diceRoll));
			
		player.setCurrentRoll(diceRoll);
	}
}