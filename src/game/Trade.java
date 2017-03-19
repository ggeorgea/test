package game;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to store methods associated with trade
 */
public class Trade {
	
	private static final int DIRECT_TRADE_NUMBER = 4;
	private static final int STANDARD_TRADE_NUMBER = 3;
	private static final int SPECIAL_TRADE_NUMBER = 2;
	
	public static final String WOOL = "wool";
	public static final String LUMBER = "lumber";
	public static final String ORE = "ore";
	public static final String BRICK = "brick";
	public static final String GRAIN = "grain";
	
	private static final String ACCEPT_TRADE = "accept";
	private static final String REJECT_TRADE = "reject";
	private static final String COUNTER_TRADE = "counter";

//-----Method asking player who they want to trade with-----//
	
	public static boolean tradeBankOrPlayer(Scanner scanner) {
		
		System.out.println("Press 'B' to trade with the bank, and 'P' to trade with other players:");
		
		String choice = scanner.next().toUpperCase();
		
		switch (choice) {
		case "B" :
			return true;
		case "P" :
			return false;
		default :
			System.out.println("Invalid choice. Please choose again");
			return tradeBankOrPlayer(scanner);
		}
	}
	
//-----Methods allowing the player to trade with the bank-----//

	public static void tradeBank(Player player, Scanner scanner, Game game1){
		//Board board1 = game1.getBoard();

		player.updatePlayerPorts(player, game1);
		
		ArrayList<ResourceCard> resourceType = new ArrayList<ResourceCard>();
		resourceType.addAll(game1.getBrick());
		resourceType.addAll(game1.getGrain());
		resourceType.addAll(game1.getLumber());
		resourceType.addAll(game1.getOre());
		resourceType.addAll(game1.getWool());

		boolean hasStandard = false; if(player.getStandardPorts().size() > 0) hasStandard = true;
		boolean hasSpecial = false; if(player.getSpecialPorts().size() > 0) hasSpecial = true;
		
		System.out.println("Trading options:\nDirectly with bank (4:1 receiving a resource of your choice) - press 4");
		if(hasStandard) System.out.println("Through a standard port (3:1 receiving a resource of your choice) - press 3");
		if(hasSpecial) System.out.println("Through a special port (2:1 receiving a specific resource) - press 2");
		int choice = scanner.nextInt();
		switch (choice) {
			case 4 :
				tradeDirect(player, scanner, game1, resourceType);
				break;
			case 3 :
				if(hasStandard) tradeStandard(player, scanner, game1, resourceType);
				else System.out.println("Invalid choice. Please choose again"); tradeBank(player, scanner, game1);
				break;
			case 2 :
				if(hasSpecial) tradeSpecial(player, scanner, game1, resourceType);
				else System.out.println("Invalid choice. Please choose again"); tradeBank(player, scanner, game1);
				break;
			default:
				System.out.println("Invalid choice. Please choose again."); tradeBank(player, scanner, game1);
				break;
		}
	}

	public static ResourceCard selectTradeResource(Player player, Scanner scanner, Game game1, ArrayList<Integer> canTrade, ArrayList<ResourceCard> resourceType){
			
		System.out.println("Please select resource to trade with the bank:");
		
		int selection = scanner.nextInt();
		
		if(selection > canTrade.size()) {
			
			System.out.println("Invlaid selection. Please choose again.");
			return selectTradeResource(player, scanner, game1, canTrade, resourceType);
		}
		else {
			
			int choice = canTrade.get(selection - 1);
			ResourceCard tradeChoice = resourceType.get(choice);
			
			return tradeChoice;
		}
	}

	public static ArrayList<ResourceCard> selectGainResource(Player player, Scanner scanner, Game game1){
		
		ArrayList<ResourceCard> gameResources = new ArrayList<ResourceCard>();
		gameResources.addAll(game1.getBrick());
		gameResources.addAll(game1.getGrain());
		gameResources.addAll(game1.getLumber());
		gameResources.addAll(game1.getOre());
		gameResources.addAll(game1.getWool());
		
		System.out.println("Please select a resource to gain from trading with the bank:");
		System.out.println("Brick - press 1\nGrain - press 2\nLumber - press 3\nOre - press 4\nWool - press 5");
		
		int choice = scanner.nextInt();
		
		switch (choice) {
		case 1 :
			return game1.getBrick();
		case 2 :
			return game1.getGrain();
		case 3 :
			return game1.getLumber();
		case 4 :
			return game1.getOre();
		case 5 :
			return game1.getWool();
		default :
			System.out.println("Invalid choice. Please choose again.");
			return selectGainResource(player, scanner, game1);
		}
	}

	public static void carryOutTrade(Player player, ResourceCard tradeChoice, ArrayList<ResourceCard> gainChoice, String tradeType){
		
		ArrayList<ResourceCard> playerResources = player.getResourceCards();
		ResourceCard removeResource = tradeChoice;
		
		int tradeNumber = 0;;
		
		switch (tradeType) {
		case "direct" :
			tradeNumber = DIRECT_TRADE_NUMBER; break;
		case "standard" :
			tradeNumber = STANDARD_TRADE_NUMBER; break;
		case "special" :
			tradeNumber = SPECIAL_TRADE_NUMBER; break;
		}
		
		for (int i = 0; i < tradeNumber; i++) {
			
			playerResources.remove(removeResource);
		}
		
		ResourceCard addResource = gainChoice.get(0);
		playerResources.add(addResource);
		player.setResourceCards(playerResources);
	}

	public static void tradeDirect(Player player, Scanner scanner, Game game1, ArrayList<ResourceCard> resourceType){
		
		ArrayList<ResourceCard> resources = player.getResourceCards();
		int[] resourceCount = {0, 0, 0, 0, 0}; //array positions indicate brick, grain, lumber, ore, wool respectively
		int bankAmount = 0;
		for (int i = 0; i < resources.size(); i++) {
			
			switch (resources.get(i).getResource()) {
			case "brick" : 
				resourceCount[0]++;
				bankAmount = game1.getBrick().size();
				break;
			case "grain" : 
				resourceCount[1]++; 
				bankAmount = game1.getGrain().size();
				break;
			case "lumber" : 
				resourceCount[2]++; 
				bankAmount = game1.getLumber().size();
				break;
			case "ore" : 
				resourceCount[3]++; 
				bankAmount = game1.getOre().size();
				break;
			case "wool" : 
				resourceCount[4]++; 
				bankAmount = game1.getWool().size();
				break;
			default : 
				break;
			}
		}
		
		ArrayList<Integer> canTrade = new ArrayList<Integer>();
		System.out.println("You can trade the following resources:");
		
		for (int i = 0; i < resourceCount.length; i++) {
			if (resourceCount[i] >= DIRECT_TRADE_NUMBER) {
				
				System.out.println(resourceType.get(i).getResource() + ": " + resourceCount[i] + " available to trade - press " + (i+1));
				canTrade.add(i);
			}
		}
		
		if (canTrade.size() != 0) {
			
			ResourceCard tradeChoice = selectTradeResource(player, scanner, game1, canTrade, resourceType);
			ArrayList<ResourceCard> gainChoice = selectGainResource(player, scanner, game1);
			if(bankAmount >= DIRECT_TRADE_NUMBER) carryOutTrade(player, tradeChoice, gainChoice, "direct");
			else System.out.println("Not enough resources available in bank to trade. Trade cancelled.");
		}
		else {
			
			System.out.println("No resources available to trade. Trade cancelled.");
			return;
		}
	}

	public static void tradeStandard(Player player, Scanner scanner, Game game1, ArrayList<ResourceCard> resourceType){
		
		ArrayList<ResourceCard> resources = player.getResourceCards();
		int[] resourceCount = {0, 0, 0, 0, 0}; //array positions indicate brick, grain, lumber, ore, wool respectively
		resourceCount = countPlayerResources(resourceCount, resources);
		ArrayList<Integer> canTrade = new ArrayList<Integer>();
		
		System.out.println("You can trade the following resources:");
		
		for (int i = 0; i < resources.size(); i++) {
			if (resourceCount[i] >= STANDARD_TRADE_NUMBER) {
				
				System.out.println(resourceType.get(i).getResource() + ": " + resourceCount[i] + " available to trade - press " + i+1);
				canTrade.add(i);
			}
		}
		
		if (canTrade.size() != 0) {
			
			ResourceCard tradeChoice = selectTradeResource(player, scanner, game1, canTrade, resourceType);
			ArrayList<ResourceCard> gainChoice = selectGainResource(player, scanner, game1);
			int bankAmount = 0;
			switch(gainChoice.get(0).getResource()) {
			case "brick" : 
				bankAmount = game1.getBrick().size();
				break;
			case "grain" : 
				bankAmount = game1.getGrain().size();
				break;
			case "lumber" : 
				bankAmount = game1.getLumber().size();
				break;
			case "ore" : 
				bankAmount = game1.getOre().size();
				break;
			case "wool" : 
				bankAmount = game1.getWool().size();
				break;
			default : 
				break;
			}
			if(bankAmount >= STANDARD_TRADE_NUMBER) carryOutTrade(player, tradeChoice, gainChoice, "standard");
			else System.out.println("Not enough resources available in bank to trade. Trade cancelled.");
		}
		else {
			
			System.out.println("No resources available to trade. Trade cancelled.");
			return;
		}
	}

	public static void tradeSpecial(Player player, Scanner scanner, Game game1, ArrayList<ResourceCard> resourceType){
		ArrayList<Port> ownedPorts = player.getSpecialPorts();
		ArrayList<Port> tradePorts = new ArrayList<Port>();
		ArrayList<String> portTypes = new ArrayList<String>();
		ArrayList<ResourceCard> resources = player.getResourceCards();
		
		for(int i = 0; i < ownedPorts.size(); i++){
			for(int j = 0; j < resources.size(); j++){
				if(ownedPorts.get(i).getResource().equals(resources.get(j).getResource())){
					String current = resources.get(j).getResource();
					int count = 0;
					for(int k = 0; i < player.getResourceCards().size(); i++){
						if(player.getResourceCards().get(k).equals(current)) count++;
					}
					if(count >= 2 && !(portTypes.contains(ownedPorts.get(i).getResource()))){
						portTypes.add(ownedPorts.get(i).getResource());
					}
				}
			}
		}
		for(int i = 0; i < ownedPorts.size(); i++){
			for(int j = 0; j < portTypes.size(); j++){
				if(ownedPorts.get(i).getResource().equals(portTypes.get(j))){
					tradePorts.add(ownedPorts.get(i));
				}
			}
		}
		System.out.println("You can trade with the following 'special' port type(s). Please select which one you would like to trade with:");
		for(int i = 0; i < tradePorts.size(); i++){
			System.out.println((i+1) + ": " + tradePorts.get(i).getResource());
		}
		int tradeChoice = scanner.nextInt();
		
		ResourceCard portChoice = selectTradeResource(player, scanner, game1, null, resources);
		ResourceCard tradePort = null;
		switch(portChoice.getResource().toLowerCase()){
			case "brick" :
				portChoice = game1.getBrick().get(0);
				break;
			case "grain" :
				portChoice = game1.getGrain().get(0);
				break;
			case "lumber" :
				portChoice = game1.getLumber().get(0);
				break;
			case "ore" :
				portChoice = game1.getOre().get(0);
				break;
			case "wool" :
				portChoice = game1.getWool().get(0);
				break;
		}
		
		String[] resourceTypes = {"brick", "grain", "lumber", "ore", "wool"};
		System.out.println("please select which resource you would like to gain from the bank:");
		for(int i = 0; i < resourceTypes.length; i++){
			System.out.println((i+1) + ": " + resourceTypes[i]);
		}
		int gainChoice = scanner.nextInt();
		
		ArrayList<ResourceCard> gainResource = null;
		int bankAmount = 0;
		switch(gainChoice){
			case 1 :
				gainResource = game1.getBrick();
				bankAmount = gainResource.size();
				break;
			case 2 :
				gainResource = game1.getGrain();
				bankAmount = gainResource.size();
				break;
			case 3 :
				gainResource = game1.getLumber();
				bankAmount = gainResource.size();
				break;
			case 4 :
				gainResource = game1.getOre();
				bankAmount = gainResource.size();
				break;
			case 5 :
				gainResource = game1.getWool();
				bankAmount = gainResource.size();
				break;
		}
		if(bankAmount >= SPECIAL_TRADE_NUMBER) carryOutTrade(player, tradePort, gainResource, "special");
		else System.out.println("Not enough resources available in bank to trade. Trade cancelled.");
	}

	public static int[] countPlayerResources(int[] resourceCount, ArrayList<ResourceCard> resources){
		
		for (int i = 0; i < resources.size(); i++) {
			
			switch (resources.get(i).getResource()) {
			case "brick" : 
				resourceCount[0]++; 
				break;
			case "grain" : 
				resourceCount[1]++; 
				break;
			case "lumber" : 
				resourceCount[2]++; 
				break;
			case "ore" : 
				resourceCount[3]++; 
				break;
			case "wool" : 
				resourceCount[4]++; 
				break;
			default : 
				break;
			}
		}
		
		return resourceCount;
	}

	public static boolean hasStandardPort(Player player, Game game1) {
		
		ArrayList<Port> ports = player.getStandardPorts();
		
		if (ports.size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean hasSpecialPort(Player player, Game game1) {
		
		ArrayList<Port> ports = player.getSpecialPorts();
		
		if (ports.size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static void checkIfPortSettled(Player player, Intersection settlement, Game game1) {
		
		Board board1 = game1.getBoard();
		int xCoord = settlement.getCoordinate().getX();
		int yCoord = settlement.getCoordinate().getY();
		
		for (int i = 0; i < board1.getPorts().size(); i++) {
			if ((xCoord == board1.getPorts().get(i).getCoordinateA().getX()) && (yCoord == board1.getPorts().get(i).getCoordinateA().getY())) {
				
				board1.getPorts().get(i).setOwner(player);
			}
		}
	}
	
//-----Methods for player to player trade-----//

	//allows the player to trade with other players
	public static void tradePlayer(Player player, Scanner scanner, Game game1) {

		ArrayList<Player> players = game1.getPlayers();
		players.remove(player);

		//asks the player to choose a player to trade with
		System.out.println("Who do you want to trade with?");

		for (int i = 0; i < players.size(); i++) {
			System.out.println((i+1) + ": " + players.get(i).getName());
		}

		int choice = scanner.nextInt();

		//checks for correct input
		if (choice > players.size()+1 || choice <= 0) {
			System.out.println("Invalid choice. Please choose again.");
			tradePlayer(player, scanner, game1);
			return;
		}

		//gets the player to trade with
		Player playerTrade = players.get(choice-1);
		String trade = COUNTER_TRADE;
		int tradeNumber = 0;

		//lets the players trade until someone accepts or rejects
		while (trade.equals(COUNTER_TRADE)) {

			//if it is an even trade number, the player whose turn it
			//is makes the offer
			if (tradeNumber%2 == 0) {
				trade = proposeTrade(player, playerTrade, scanner);
			}

			//if it is an odd trade number, the player who is being
			//traded with makes the offer
			else {
				trade = proposeTrade(playerTrade, player, scanner);
			}
		}

		if (trade.equals(REJECT_TRADE)) {
			System.out.println("Offer rejected. Trading stopped.");
		}
		else if (trade.equals(ACCEPT_TRADE)) {
			System.out.println("Offer accepted. Trading stopped.");
		}

		players.add(player);
		game1.setPlayers(players);
	}

	//lets the player propose a trade with another player
	public static String proposeTrade(Player player, Player playerTrade, Scanner scanner) {

		ArrayList<ResourceCard> playerResources = player.getResourceCards();
		//ArrayList<ResourceCard> playerTradeResources = playerTrade.getResourceCards();
		
		ArrayList<ResourceCard> playerTradeResources = new ArrayList<ResourceCard>();
		playerTradeResources.add(new ResourceCard(WOOL));
		playerTradeResources.add(new ResourceCard(LUMBER));
		playerTradeResources.add(new ResourceCard(ORE));
		playerTradeResources.add(new ResourceCard(BRICK));
		playerTradeResources.add(new ResourceCard(GRAIN));	

		ArrayList<ResourceCard> playerToTrade = new ArrayList<ResourceCard>();
		ArrayList<ResourceCard> playerTradeToTrade = new ArrayList<ResourceCard>();

		int choice = 1;

		//lets the player choose what to trade from their own hand
		while (choice == 1) {

			System.out.println("What do you want to trade from your hand?");

			choice = chooseResources(playerResources, scanner);
			playerToTrade.add(playerResources.get(choice));
			playerResources.remove(choice);

			choice = chooseMoreResources(scanner);
		}

		choice = 1;

		//lets the player choose what resources they want from the other player's hand
		while (choice == 1) {

			System.out.println("What do you want from " + playerTrade.getName() + "'s hand?");	
			
			choice = chooseResources(playerTradeResources, scanner);
			playerTradeToTrade.add(playerTradeResources.get(choice));
			//playerTradeResources.remove(choice);

			choice = chooseMoreResources(scanner);
		}

		boolean validTrade = checkOffer(playerToTrade, playerTradeToTrade);

		//checks if the trade is valid
		if (!validTrade) {
			System.out.println("You cannot trade the same resources i.e 1 wool for 2 wool. Please choose again.");
			restoreResources(player, playerTrade, playerToTrade, playerTradeToTrade);
			return proposeTrade(player, playerTrade, scanner);
		}

		//asks the other player if the offer is to be accepted
		String offer = printOffer(player, playerTrade, playerToTrade, playerTradeToTrade, scanner);

		//if the offer is accepted, the trade takes place
		if (offer.equals(ACCEPT_TRADE)) {
			if (hasResources(playerTrade, playerTradeToTrade)) {
				tradePlayerResources(player, playerTrade, playerToTrade, playerTradeToTrade);
			}
			else {
				offer = REJECT_TRADE;
			}
		}

		//otherwise the resources have to be added back in to both players' hands
		else {
			restoreResources(player, playerTrade, playerToTrade, playerTradeToTrade);
		}

		return offer;
	}

	//lets the player decide what resources to trade
	public static int chooseResources(ArrayList<ResourceCard> playerResources, Scanner scanner) {

		//asks the player to choose a resource to trade
		for (int i = 0; i < playerResources.size(); i++) {
			System.out.println(i+1 + ": " + playerResources.get(i).getResource());
		}

		int choice = scanner.nextInt();

		//checks for correct input
		if (choice > playerResources.size()+1 || choice <= 0) {
			System.out.println("Invalid input. Please choose again");
			return chooseResources(playerResources, scanner);
		}

		return choice-1;
	}

	//asks the user if they want to trade more than one resource
	public static int chooseMoreResources(Scanner scanner) {

		System.out.println("Do you want to trade more cards?");
		System.out.println("1: Yes");
		System.out.println("2: No");

		int choice = scanner.nextInt();

		if (choice != 1 && choice != 2) {
			System.out.println("Invalid choice. Please choose again.");
			return chooseMoreResources(scanner);
		}

		return choice;
	}

	//checks the offer is valid against the rules
	//cannot trade same resources i.e. 1 wool for 2 wool
	public static boolean checkOffer(ArrayList<ResourceCard> playerToTrade, ArrayList<ResourceCard> playerTradeToTrade) {

		//checks each pair of cards from both array lists
		for (int i = 0; i < playerToTrade.size(); i++) {

			String card1 = playerToTrade.get(i).getResource();

			for (int j = 0; j < playerTradeToTrade.size(); j++) {

				String card2 = playerTradeToTrade.get(j).getResource();

				//if two cards have the same type the trade is invalid
				if (card1.equals(card2)) {
					return false;
				}
			}
		}

		//otherwise the trade is valid
		return true;
	}

	//prints the offer proposed by the player
	public static String printOffer(Player player, Player playerTrade, ArrayList<ResourceCard> playerToTrade, 
			ArrayList<ResourceCard> playerTradeToTrade, Scanner scanner) {
		
		//prints out the trade that has been proposed
		System.out.println("Player " + playerTrade.getName() + ": Player " + player.getName() + " has proposed a trade: ");

		for (int i = 0; i < playerToTrade.size(); i++) {
			System.out.print("1x " + playerToTrade.get(i).getResource() + " ");
		}

		System.out.print("from their hand for ");

		for (int i = 0; i < playerTradeToTrade.size(); i++) {
			System.out.print("1x " + playerTradeToTrade.get(i).getResource() + " ");
		}

		System.out.println("from your hand");

		//asks the player if they want to accept the offer
		int choice = chooseOffer(scanner);

		//returns the correct string based on the player's choice
		switch (choice) {
		case 1 :
			return ACCEPT_TRADE;
		case 2 :
			return REJECT_TRADE;
		case 3 :
			return COUNTER_TRADE;
		default :
			return REJECT_TRADE;
		}
	}

	//lets the other player during trade accept or reject the offer
	public static int chooseOffer(Scanner scanner) {

		System.out.println("What do you want to do?");
		System.out.println("1: Accept Trade");
		System.out.println("2: Reject Trade");
		System.out.println("3: Propose a Counter-offer");

		int choice = scanner.nextInt();

		//checks for correct input
		if (choice != 1 && choice != 2 && choice != 3) {
			System.out.println("Invalid input. Please choose again.");
			return chooseOffer(scanner);
		}

		return choice;
	}
	
	//checks whether the player has the resources to be traded
	public static boolean hasResources(Player player, ArrayList<ResourceCard> resourcesToTrade) {
		
		ArrayList<ResourceCard> resources = player.getResourceCards();
		boolean hasResource = true;
		
		for (int i = 0; i < resourcesToTrade.size(); i++) {
		
			hasResource = resources.remove(resourcesToTrade.get(i));
			
			if(!hasResource) {
				break;
			}
		}
		
		return hasResource;
	}

	//trades the resources between players
	public static void tradePlayerResources(Player player, Player playerTrade, ArrayList<ResourceCard> playerToTrade,
			ArrayList<ResourceCard> playerTradeToTrade) {

		ArrayList<ResourceCard> playerResources = player.getResourceCards();
		ArrayList<ResourceCard> playerTradeResources = playerTrade.getResourceCards();

		for (int i = 0; i < playerToTrade.size(); i++) {

			ResourceCard card = playerToTrade.get(i);

			playerResources.remove(card);
			playerTradeResources.add(card);
		}

		for (int i = 0; i < playerTradeToTrade.size(); i++) {

			ResourceCard card = playerTradeToTrade.get(i);

			playerTradeResources.remove(card);
			playerResources.add(card);
		}

		player.setResourceCards(playerResources);
		playerTrade.setResourceCards(playerTradeResources);
	}

	//puts the trade resources back into players' hands
	public static void restoreResources(Player player, Player playerTrade, ArrayList<ResourceCard> playerToTrade, 
			ArrayList<ResourceCard> playerTradeToTrade) {

		ArrayList<ResourceCard> playerResources = player.getResourceCards();
		ArrayList<ResourceCard> playerTradeResources = playerTrade.getResourceCards();

		for (int i = 0; i < playerToTrade.size(); i++) {

			ResourceCard card = playerToTrade.get(i);
			playerResources.add(card);
		}

		for (int i = 0; i < playerTradeToTrade.size(); i++) {

			ResourceCard card = playerTradeToTrade.get(i);
			playerTradeResources.add(card);
		}

		player.setResourceCards(playerResources);
		playerTrade.setResourceCards(playerTradeResources);
	}
}