package completeCatan;

import java.util.ArrayList;
import java.util.Scanner;

import java.io.IOException;

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
	
	private static final int BANK = 1;
	private static final int PLAYER = 2;

//-----Method asking player who they want to trade with-----//
	
	public static boolean tradeBankOrPlayer(Player player, Scanner scanner) throws IOException {
		
		Catan.printToClient("Who do you want to trade with?", player);
		Catan.printToClient("1: Bank", player);
		Catan.printToClient("2: Another Player", player);
		
		int choice = Integer.parseInt(Catan.getInputFromClient(player, scanner));
		
		switch (choice) {
		case BANK :
			return true;
		case PLAYER :
			return false;
		default:
			Catan.printToClient("Invalid choice. Please choose again.", player);
			return tradeBankOrPlayer(player, scanner);
		}
	}
	
//-----Methods allowing the player to trade with the bank-----//

	//allows the player to trade with the bank
	public static void tradeBank(Player player, Scanner scanner, Game game1) throws IOException {

		player.updatePlayerPorts(player, game1);
		
		ArrayList<ResourceCard> resourceType = new ArrayList<ResourceCard>();
		resourceType.addAll(game1.getBrick());
		resourceType.addAll(game1.getGrain());
		resourceType.addAll(game1.getLumber());
		resourceType.addAll(game1.getOre());
		resourceType.addAll(game1.getWool());

		boolean hasStandard = false; 
		if (player.getStandardPorts().size() > 0) {
			hasStandard = true;
		}
		
		boolean hasSpecial = false; 
		if (player.getSpecialPorts().size() > 0) {
			hasSpecial = true;
		}
		
		Catan.printToClient("Trading options:", player);
		Catan.printToClient("4: Directly with bank (4:1 receiving a resource of your choice)", player);
		
		if (hasStandard) {
			Catan.printToClient("3: Through a standard port (3:1 receiving a resource of your choice)", player);
		}
		if (hasSpecial) {
			Catan.printToClient("2: Through a special port (2:1 receiving a specific resource)", player);
		}
		
		int choice = Integer.parseInt(Catan.getInputFromClient(player, scanner));
		
		switch (choice) {
		case 4 :
			tradeDirect(player, scanner, game1, resourceType);
			break;
		case 3 :
			if (hasStandard) {
				tradeStandard(player, scanner, game1, resourceType);
			}
			else {
				Catan.printToClient("Invalid choice. Please choose again", player);
				tradeBank(player, scanner, game1);
			}
			break;
		case 2 :
			if (hasSpecial) {
				tradeSpecial(player, scanner, game1, resourceType);
			}
			else {
				Catan.printToClient("Invalid choice. Please choose again", player);
				tradeBank(player, scanner, game1);
			}
			break;
		default :
			Catan.printToClient("Invalid choice. Please choose again", player);
			tradeBank(player, scanner, game1);
			break;
		}
	}

	//prompts the user to select what resource they want to trade
	public static ResourceCard selectTradeResource(Player player, Scanner scanner, Game game1, ArrayList<Integer> canTrade, 
			ArrayList<ResourceCard> resourceType) throws IOException {
			
		Catan.printToClient("Please select resource to trade with the bank:", player);
		
		int selection = Integer.parseInt(Catan.getInputFromClient(player, scanner));
		
		if (selection > canTrade.size()) {
			
			Catan.printToClient("Invalid choice. Please choose again", player);
			return selectTradeResource(player, scanner, game1, canTrade, resourceType);
		}
		else {
			
			int choice = canTrade.get(selection - 1);
			ResourceCard tradeChoice = resourceType.get(choice);
			
			return tradeChoice;
		}
	}

	//asks the user to select which resource they wish to gain
	public static ArrayList<ResourceCard> selectGainResource(Player player, Scanner scanner, Game game1) throws IOException {
		
		ArrayList<ResourceCard> gameResources = new ArrayList<ResourceCard>();
		gameResources.addAll(game1.getBrick());
		gameResources.addAll(game1.getGrain());
		gameResources.addAll(game1.getLumber());
		gameResources.addAll(game1.getOre());
		gameResources.addAll(game1.getWool());
		
		Catan.printToClient("Please select a resource to gain from trading with the bank:", player);
		Catan.printToClient("1: brick", player);
		Catan.printToClient("2: grain", player);
		Catan.printToClient("3: lumber", player);
		Catan.printToClient("4: ore", player);
		Catan.printToClient("5: wool", player);
		
		int choice = Integer.parseInt(Catan.getInputFromClient(player, scanner));
		
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
			Catan.printToClient("Invalid choice. Please choose again.", player);
			return selectGainResource(player, scanner, game1);
		}
	}

	//carries out the trade if it is legal
	public static void carryOutTrade(Player player, Game game1, ResourceCard tradeChoice, ArrayList<ResourceCard> gainChoice, String tradeType) {
		
		ArrayList<ResourceCard> playerResources = player.getResourceCards();
		ResourceCard removeResource = tradeChoice;
		
		int tradeNumber = 0;;
		
//		switch (tradeType) {
//		case "direct" :
//			tradeNumber = DIRECT_TRADE_NUMBER; 
//			break;
//		case "standard" :
//			tradeNumber = STANDARD_TRADE_NUMBER; 
//			break;
//		case "special" :
//			tradeNumber = SPECIAL_TRADE_NUMBER; 
//			break;
//		}
		
		if(tradeType.equals("standard")){
			tradeNumber = STANDARD_TRADE_NUMBER;
		}
		else if(tradeType.equals("special")){
			tradeNumber = SPECIAL_TRADE_NUMBER;
		}
		else{
			tradeNumber = DIRECT_TRADE_NUMBER;
		}
		
//		for (int i = 0; i < tradeNumber; i++) {
//			playerResources.remove(removeResource);
//		}
		
		for (int i = 0; i < tradeNumber; i++) {
			playerResources.remove(removeResource);
			returnToBank(removeResource, game1);
		}
		
		ResourceCard addResource = gainChoice.get(0);
		playerResources.add(addResource);
		player.setResourceCards(playerResources);
		
		Catan.printToClient("Trade completed!", player);
		
		ArrayList<Player> players = game1.getPlayers();
		
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i) != player) {
				Catan.printToClient("Player " + player.getName() + "traded with the bank", players.get(i));
			}
		}
	}
	
	public static void returnToBank(ResourceCard removeResource, Game game1) {
		
		ArrayList<ResourceCard> temp;
		
		switch(removeResource.getResource()){
		case "ore" :
			temp = game1.getOre();
			temp.add(removeResource);
			game1.setOre(temp);
			break;
		case "grain" :
			temp = game1.getGrain();
			temp.add(removeResource);
			game1.setGrain(temp);
			break;
		case "lumber" :
			temp = game1.getLumber();
			temp.add(removeResource);
			game1.setLumber(temp);
			break;
		case "wool" :
			temp = game1.getWool();
			temp.add(removeResource);
			game1.setWool(temp);
			break;
		case "brick" :
			temp = game1.getBrick();
			temp.add(removeResource);
			game1.setBrick(temp);
			break;
		}
	}

	//if the player is trading directly with the bank
	public static void tradeDirect(Player player, Scanner scanner, Game game1, ArrayList<ResourceCard> resourceType) throws IOException {
		
		ArrayList<ResourceCard> resources = player.getResourceCards();
		int[] resourceCount = {0, 0, 0, 0, 0}; //array positions indicate brick, grain, lumber, ore, wool respectively
		int bankAmount = 0;
		
		for (int i = 0; i < resources.size(); i++) {
			
			switch (resources.get(i).getResource()) {
			case BRICK : 
				resourceCount[0]++;
				bankAmount = game1.getBrick().size();
				break;
			case GRAIN : 
				resourceCount[1]++; 
				bankAmount = game1.getGrain().size();
				break;
			case LUMBER : 
				resourceCount[2]++; 
				bankAmount = game1.getLumber().size();
				break;
			case ORE : 
				resourceCount[3]++; 
				bankAmount = game1.getOre().size();
				break;
			case WOOL : 
				resourceCount[4]++; 
				bankAmount = game1.getWool().size();
				break;
			default : 
				break;
			}
		}
		
		ArrayList<Integer> canTrade = new ArrayList<Integer>();
		Catan.printToClient("You can trade the following resources:", player);
		
		int numPrinted = 0;
		for (int i = 0; i < resourceCount.length; i++) {
			if (resourceCount[i] >= DIRECT_TRADE_NUMBER) {
				Catan.printToClient((numPrinted+1) + ": " + resourceType.get(i).getResource() + " (" + resourceCount[i] + " available)", player);
				canTrade.add(i);
				numPrinted++;
			}
		}
		
		if (canTrade.size() != 0) {
			
			ResourceCard tradeChoice = selectTradeResource(player, scanner, game1, canTrade, resourceType);
			ArrayList<ResourceCard> gainChoice = selectGainResource(player, scanner, game1);
			
			//makes sure the trade comprises of two different resources
			if (tradeChoice.getResource().equals(gainChoice.get(0).getResource())) {
				
				Catan.printToClient("You cannot trade 4 of one resource for 1 of the same resource. Trade cancelled", player);
				return;
			}
			
			if (bankAmount >= DIRECT_TRADE_NUMBER) {
				carryOutTrade(player, game1, tradeChoice, gainChoice, "direct");
			}
			else {
				
				Catan.printToClient("Not enough resources available in bank to trade. Trade cancelled.", player);
				return;
			}
		}
		else {
			
			Catan.printToClient("No resources available to trade. Trade cancelled.", player);
			return;
		}
	}

	//if the user has a standard port they can trade 3:1
	public static void tradeStandard(Player player, Scanner scanner, Game game1, ArrayList<ResourceCard> resourceType) throws IOException {
		
		ArrayList<ResourceCard> resources = player.getResourceCards();
		int[] resourceCount = {0, 0, 0, 0, 0}; //array positions indicate brick, grain, lumber, ore, wool respectively
		resourceCount = countPlayerResources(resourceCount, resources);
		ArrayList<Integer> canTrade = new ArrayList<Integer>();
		
		Catan.printToClient("You can trade the following resources:", player);
		
		for (int i = 0; i < resources.size(); i++) {
			if (resourceCount[i] >= STANDARD_TRADE_NUMBER) {
				
				Catan.printToClient((i+1) + ": " + resourceType.get(i).getResource() + " (" + resourceCount[i] + " available)", player);
				canTrade.add(i);
			}
		}
		
		if (canTrade.size() != 0) {
			
			ResourceCard tradeChoice = selectTradeResource(player, scanner, game1, canTrade, resourceType);
			ArrayList<ResourceCard> gainChoice = selectGainResource(player, scanner, game1);
			int bankAmount = 0;
			
			switch(gainChoice.get(0).getResource()) {
			case BRICK : 
				bankAmount = game1.getBrick().size();
				break;
			case GRAIN : 
				bankAmount = game1.getGrain().size();
				break;
			case LUMBER : 
				bankAmount = game1.getLumber().size();
				break;
			case ORE : 
				bankAmount = game1.getOre().size();
				break;
			case WOOL : 
				bankAmount = game1.getWool().size();
				break;
			default : 
				break;
			}
			
			//makes sure the trade comprises of two different resources
			if (tradeChoice.getResource().equals(gainChoice.get(0).getResource())) {
				
				Catan.printToClient("You cannot trade 3 of one resource for 1 of the same resource. Trade cancelled", player);
				return;
			}
			
			if (bankAmount >= STANDARD_TRADE_NUMBER) {
				carryOutTrade(player, game1, tradeChoice, gainChoice, "standard");
			}
			else {
				
				Catan.printToClient("Not enough resources available in bank to trade. Trade cancelled.", player);
				return;
			}
		}
		else {
			
			Catan.printToClient("No resources available to trade. Trade cancelled.", player);
			return;
		}
	}

	//if the user has a special port they can trade 2:1
	public static void tradeSpecial(Player player, Scanner scanner, Game game1, ArrayList<ResourceCard> resourceType) throws IOException {
		
		ArrayList<Port> ownedPorts = player.getSpecialPorts();
		ArrayList<Port> tradePorts = new ArrayList<Port>();
		ArrayList<String> portTypes = new ArrayList<String>();
		ArrayList<ResourceCard> resources = player.getResourceCards();
		
		for (int i = 0; i < ownedPorts.size(); i++) {
			for (int j = 0; j < resources.size(); j++) {
				if (ownedPorts.get(i).getResource().equals(resources.get(j).getResource())) {
					
					String current = resources.get(j).getResource();
					int count = 0;
					
					for (int k = 0; i < player.getResourceCards().size(); i++) {
						if (player.getResourceCards().get(k).equals(current)) {
							count++;
						}
					}
					if (count >= 2 && !(portTypes.contains(ownedPorts.get(i).getResource()))) {
						portTypes.add(ownedPorts.get(i).getResource());
					}
				}
			}
		}
		
		for (int i = 0; i < ownedPorts.size(); i++) {
			for (int j = 0; j < portTypes.size(); j++) {
				if (ownedPorts.get(i).getResource().equals(portTypes.get(j))) {
					tradePorts.add(ownedPorts.get(i));
				}
			}
		}
		
		Catan.printToClient("Please select which 'special' port type you would like to trade with:", player);
		
		for (int i = 0; i < tradePorts.size(); i++) {
			Catan.printToClient((i+1) + ": " + tradePorts.get(i).getResource(), player);
		}
		
		int tradeChoice = Integer.parseInt(Catan.getInputFromClient(player, scanner));
		
		ResourceCard portChoice = selectTradeResource(player, scanner, game1, null, resources);
		ResourceCard tradePort = null;
		
		switch (portChoice.getResource().toLowerCase()) {
		case BRICK :
			portChoice = game1.getBrick().get(0);
			break;
		case GRAIN :
			portChoice = game1.getGrain().get(0);
			break;
		case LUMBER :
			portChoice = game1.getLumber().get(0);
			break;
		case ORE :
			portChoice = game1.getOre().get(0);
			break;
		case WOOL :
			portChoice = game1.getWool().get(0);
			break;
		}
		
		String[] resourceTypes = {BRICK, GRAIN, LUMBER, ORE, WOOL};
		
		Catan.printToClient("Please select which resource you would like to gain from the bank:", player);
		
		for (int i = 0; i < resourceTypes.length; i++) {
			Catan.printToClient((i+1) + ": " + resourceTypes[i], player);
		}
		
		int gainChoice = Integer.parseInt(Catan.getInputFromClient(player, scanner));
		
		ArrayList<ResourceCard> gainResource = null;
		int bankAmount = 0;
		
		switch (gainChoice) {
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
		
		//makes sure the trade comprises of two different resources
		if (portChoice.getResource().equals(gainResource.get(0).getResource())) {
			
			Catan.printToClient("You cannot trade 2 of one resource for 1 of the same resource. Trade cancelled", player);
			return;
		}
		
		if (bankAmount >= SPECIAL_TRADE_NUMBER) {
			carryOutTrade(player, game1, tradePort, gainResource, "special");
		}
		else {
			
			Catan.printToClient("Not enough resources available in bank to trade. Trade cancelled.", player);
			return;
		}
	}

	//checks how many of a resource the player has
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

	//checks if they player has a standard port
	public static boolean hasStandardPort(Player player, Game game1) {
		
		ArrayList<Port> ports = player.getStandardPorts();
		
		if (ports.size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	//checks if the player has a special port
	public static boolean hasSpecialPort(Player player, Game game1) {
		
		ArrayList<Port> ports = player.getSpecialPorts();
		
		if (ports.size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//checks if a player owns a port
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
	public static void tradePlayer(Player player, Scanner scanner, Game game1) throws IOException {

		ArrayList<Player> players = game1.getPlayers();

		//asks the player to choose a player to trade with
		Catan.printToClient("Who do you want to trade with?", player);
		
		for (int i = 0; i < players.size(); i++) {
			Catan.printToClient((i+1) + ": " + players.get(i).getName(), player);
		}

		int choice = Integer.parseInt(Catan.getInputFromClient(player, scanner));

		//checks for correct input
		if (choice > players.size()+1 || choice <= 0) {
			
			Catan.printToClient("Invalid choice. Please choose again.", player);
			tradePlayer(player, scanner, game1);
			return;
		}
		
		if (players.get(choice-1).equals(player)) {
			
			Catan.printToClient("You cannot trade with yourself. Please choose again.", player);
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
			Catan.printToClient("Offer rejected. Trading stopped.", playerTrade);
			Catan.printToClient("Offer rejected. Trading stopped.", player);
		}
		else if (trade.equals(ACCEPT_TRADE)) {
			Catan.printToClient("Offer accepted. Trading stopped.", playerTrade);
			Catan.printToClient("Offer accepted. Trading stopped.", player);
			
			//notifies other players of the trade that occurred
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i) != player && players.get(i) != playerTrade) {
					Catan.printToClient("Player " + player.getName() + " and Player "
							+ playerTrade.getName() + " traded resources", players.get(i));
				}
			}
		}

		players.add(player);
		game1.setPlayers(players);
	}

	//lets the player propose a trade with another player
	public static String proposeTrade(Player player, Player playerTrade, Scanner scanner) throws IOException {

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

			Catan.printToClient("What do you want to trade from your hand?", player);

			choice = chooseResources(playerResources, scanner, player);
			playerToTrade.add(playerResources.get(choice));
			playerResources.remove(choice);

			choice = chooseMoreResources(scanner, player);
		}

		choice = 1;

		//lets the player choose what resources they want from the other player's hand
		while (choice == 1) {

			Catan.printToClient("What do you want from " + playerTrade.getName() + "'s hand?", player);
			
			choice = chooseResources(playerTradeResources, scanner, player);
			playerTradeToTrade.add(playerTradeResources.get(choice));

			choice = chooseMoreResources(scanner, player);
		}

		boolean validTrade = checkOffer(playerToTrade, playerTradeToTrade);
		boolean tradeRestore = false;
		
		//checks if the trade is valid
		if (!validTrade) {
			
			Catan.printToClient("You cannot trade the same resources i.e 1 wool for 2 wool. Please choose again.", playerTrade);
			restoreResources(player, playerTrade, playerToTrade, playerTradeToTrade, tradeRestore);
			
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
				tradeRestore = true;
				restoreResources(player, playerTrade, playerToTrade, playerTradeToTrade, tradeRestore);
				offer = REJECT_TRADE;
			}
		}

		//otherwise the resources have to be added back in to both players' hands
		else {
			tradeRestore = false;
			restoreResources(player, playerTrade, playerToTrade, playerTradeToTrade, tradeRestore);
		}

		return offer;
	}

	//lets the player decide what resources to trade
	public static int chooseResources(ArrayList<ResourceCard> playerResources, Scanner scanner, Player player) throws IOException {

		//asks the player to choose a resource to trade
		for (int i = 0; i < playerResources.size(); i++) {
			Catan.printToClient((i+1) + ": " + playerResources.get(i).getResource(), player);
		}

		int choice = Integer.parseInt(Catan.getInputFromClient(player, scanner));

		//checks for correct input
		if (choice > playerResources.size()+1 || choice <= 0) {
			
			Catan.printToClient("Invalid input. Please choose again", player);
			return chooseResources(playerResources, scanner, player);
		}

		return choice-1;
	}

	//asks the user if they want to trade more than one resource
	public static int chooseMoreResources(Scanner scanner, Player player) throws IOException {

		Catan.printToClient("Do you want to trade more cards?", player);
		Catan.printToClient("1: Yes", player);
		Catan.printToClient("2: No", player);

		int choice = Integer.parseInt(Catan.getInputFromClient(player, scanner));

		if (choice != 1 && choice != 2) {
			
			Catan.printToClient("Invalid choice. Please choose again.", player);
			return chooseMoreResources(scanner, player);
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
			ArrayList<ResourceCard> playerTradeToTrade, Scanner scanner) throws IOException {
		
		//prints out the trade that has been proposed
		Catan.printToClient("Player " + player.getName() + " has proposed a trade: ", playerTrade);

		for (int i = 0; i < playerToTrade.size(); i++) {
			Catan.printToClient("1x " + playerToTrade.get(i).getResource(), playerTrade);
		}

		Catan.printToClient("from their hand for:", playerTrade);

		for (int i = 0; i < playerTradeToTrade.size(); i++) {
			Catan.printToClient("1x " + playerTradeToTrade.get(i).getResource(), playerTrade);
		}

		Catan.printToClient("from your hand.", playerTrade);

		//asks the player if they want to accept the offer
		int choice = chooseOffer(scanner, playerTrade);

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
	public static int chooseOffer(Scanner scanner, Player playerTrade) throws IOException {

		Catan.printToClient("What do you want to do?", playerTrade);
		Catan.printToClient("1: Accept Trade", playerTrade);
		Catan.printToClient("2: Reject Trade", playerTrade);
		Catan.printToClient("3: Propose a Counter-offer", playerTrade);

		int choice = Integer.parseInt(Catan.getInputFromClient(playerTrade, scanner));

		//checks for correct input
		if (choice != 1 && choice != 2 && choice != 3) {
			
			Catan.printToClient("Invalid input. Please choose again.", playerTrade);
			return chooseOffer(scanner, playerTrade);
		}

		return choice;
	}
	
	//checks whether the player has the resources to be traded
	public static boolean hasResources(Player player, ArrayList<ResourceCard> resourcesToTrade) {
		
		ArrayList<ResourceCard> resources = player.getResourceCards();
		boolean hasResource = true;
		
		for (int i = 0; i < resourcesToTrade.size(); i++) {
		
			String resource = resourcesToTrade.get(i).getResource();
			
			for (int j = 0; j < resources.size(); j++) {
				
				if (resources.get(j).getResource().equals(resource)) {
					hasResource = resources.remove(resources.get(j));
					break;
				}
				else {
					hasResource = false;
				}
			}			
			
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
			ArrayList<ResourceCard> playerTradeToTrade, boolean tradeRestore) {

		ArrayList<ResourceCard> playerResources = player.getResourceCards();
		ArrayList<ResourceCard> playerTradeResources = playerTrade.getResourceCards();

		for (int i = 0; i < playerToTrade.size(); i++) {

			ResourceCard card = playerToTrade.get(i);
			playerResources.add(card);
		}

		if (tradeRestore) {
			for (int i = 0; i < playerTradeToTrade.size(); i++) {

				ResourceCard card = playerTradeToTrade.get(i);
				playerTradeResources.add(card);
			}

			player.setResourceCards(playerResources);
			playerTrade.setResourceCards(playerTradeResources);
		}
	}
}