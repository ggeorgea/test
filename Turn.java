import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Class contains all the methods to allow players to do things on their turn
 */
public class Turn {

	private static final boolean END_GAME = true;
	private static final int END_TURN = 8;

	//why is the robber initialized as 7?
	private static final int ROBBER = 7;

	//create an instance of robber and a coordinate where it begins
	Coordinate c = new Coordinate();
	Robber robber = new Robber(c, null, null);

	private static final String ACCEPT_TRADE = "accept";
	private static final String REJECT_TRADE = "reject";
	private static final String COUNTER_TRADE = "counter";

	private static final int DIRECT_TRADE_NUMBER = 4;
	private static final int STANDARD_TRADE_NUMBER = 3;
	private static final int SPECIAL_TRADE_NUMBER = 2;

//-----Methods to actually let the player have their turn-----//

	//allows the player to have their turn
	public static boolean newTurn(Player player, Game game1, Scanner scanner) {

		Map.printMap(game1.getBoard());

		Dice.rollDice(player, scanner);

		if (player.getCurrentRoll() != ROBBER) {
			ResourceAllocation.resourceAllocation(player.getCurrentRoll(), game1, scanner);
		}
		else {
			checkCardRemoval(game1, scanner);
			moveRobber(player, game1, scanner);
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
					Player.printResourceCards(player);
					break;
				case 5 :
					Map.printMap(game1.getBoard());
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

//-----Methods to play the robber for the turn-----//

	//checks if any players have more than seven cards when the robber
	//is activated
	public static void checkCardRemoval(Game game1, Scanner scanner) {

		ArrayList<Player> players = game1.getPlayers();

		for (int i = 0; i < players.size(); i++) {

			Player player = players.get(i);
			ArrayList<ResourceCard> cards = player.getResourceCards();

			//if the player has more than seven cards, they must
			//remove cards from their hand
			if (cards.size() > 7) {

				cardRemoval(player, cards, game1, scanner);
				player.setResourceCards(cards);
			}
		}

		game1.setPlayers(players);
	}

	//asks players with more than seven cards to choose the cards they remove
	public static void cardRemoval(Player player, ArrayList<ResourceCard> cards, Game game1, Scanner scanner) {

		//calculates how many cards to be removed
		int noCardsToRemove = cards.size()/2;

		System.out.println("Player " + player.getName() + ": Please select " + noCardsToRemove + " cards to remove");

		for (int i = 0; i < noCardsToRemove; i++) {

			System.out.println("Card " + i);

			//asks the player to choose a card to remove
			for (int j = 0; j < cards.size(); j++) {

				System.out.println(j + ": " + cards.get(j).getResource());
			}

			//removes the card they choose
			try {
				int choice = scanner.nextInt();

				if (choice < 0 || choice >= cards.size()) {
					System.out.println("Invalid choice. Please choose again");
					cardRemoval(player, cards, game1, scanner);
				}

				ResourceCard card = cards.get(choice);

				switch (card.getResource()) {
				case "ore" :
					ArrayList<ResourceCard> ore = game1.getOre();
					ore.add(card);
					break;
				case "lumber" :
					ArrayList<ResourceCard> lumber = game1.getLumber();
					lumber.add(card);
					break;
				case "brick" :
					ArrayList<ResourceCard> brick = game1.getBrick();
					brick.add(card);
					break;
				case "wool" :
					ArrayList<ResourceCard> wool = game1.getWool();
					wool.add(card);
					break;
				case "grain" :
					ArrayList<ResourceCard> grain = game1.getGrain();
					grain.add(card);
					break;
				}

				cards.remove(choice);
			}
			catch(InputMismatchException e) {
				System.out.println("Invalid choice. Please choose again");
				scanner.nextLine();
				cardRemoval(player,cards,game1,scanner);
			}
		}
	}

	//allows the player to move the robber and steal a card from a player
	public static void moveRobber(Player player, Game game1, Scanner scanner) {

		try {
			System.out.println("Player " + player.getName() + ": Please select where to place the robber");

			System.out.println("Select X coordinate");
			int x = scanner.nextInt();

			System.out.println("Select Y coordinate");
			int y = scanner.nextInt();
			Coordinate a = new Coordinate(x, y);

			//checks the coordinates are in the correct range
			if((!((2*y <= x+8) && (2*y >= x-8) && (y <= 2*x+8) && (y >= 2*x-8) && (y >= -x-8) && (y <= -x+8)))
					|| (!game1.getBoard().getLocationFromCoordinate(a).getType().equals("hex"))) {

				System.out.println("Invalid coordinates. Please choose again");
				moveRobber(player, game1, scanner);
				return;
			}

			Hex hex1 = (Hex) game1.getBoard().getLocationFromCoordinate(a).getContains();
			hex1.setisRobberHere("R");
			game1.getBoard().setRobber(a);
			//TODO gets the hex and puts the robber there
			//steal card
		}
		catch(InputMismatchException e){
			scanner.nextLine();
			moveRobber(player,game1,scanner);
		}
	}

	public static void robberStealCard(Player player, Game game1, Scanner scanner) {
		//choose a player to steal a card from
		System.out.println("Please select player to steal from ");
		String  name = scanner.nextLine();
		//check that the player chose is valid
		//the chosen player must have a settlement next to the current coordinates
		//randomly choose a card from the player and give to other player

	}
	//method that finds all nearbyPlayers from a specific coordinate
	public static ArrayList<Player> findNearbyPlayer(Coordinate c){

	}


//-----Methods to build and buy things for the turn-----//

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

//-----Methods to allow the player to trade-----//

	//methods found in old turn class
	public static void checkIfPortSettled(Player player, Intersection settlement, Game game1){
		Board board1 = game1.getBoard();
		int xCoord = settlement.getCoordinate().getX();
		int yCoord = settlement.getCoordinate().getY();
		for(int i = 0; i < board1.getPorts().size(); i++){
			if((xCoord == board1.getPorts().get(i).getCoordinateA().getX()) && (yCoord == board1.getPorts().get(i).getCoordinateA().getY())){
				board1.getPorts().get(i).setOwner(player);
			}
		}
	}

	public static void trade(Player player, Scanner scanner, Game game1){
		boolean bank = tradeBankOrPlayer(scanner);
		if(bank) tradeBank(player, scanner, game1);
		else tradePlayer(player, scanner, game1);
	}

	public static void tradeBank(Player player, Scanner scanner, Game game1){
		Board board1 = game1.getBoard();

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
		switch (choice){
			case 4 :
				tradeBank(player, scanner, game1);
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
			if(selection > canTrade.size()){
				System.out.println("Invlaid selection. Please choose again.");
				return selectTradeResource(player, scanner, game1, canTrade, resourceType);
			}
			else{
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
		switch (choice){
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
		switch (tradeType){
			case "direct" :
				tradeNumber = DIRECT_TRADE_NUMBER; break;
			case "standard" :
				tradeNumber = STANDARD_TRADE_NUMBER; break;
			case "special" :
				tradeNumber = SPECIAL_TRADE_NUMBER; break;
		}
		for(int i = 0; i < tradeNumber; i++){
			playerResources.remove(removeResource);
		}
		ResourceCard addResource = gainChoice.get(0);
		playerResources.add(addResource);
		player.setResourceCards(playerResources);
	}

	public static void tradeDirect(Player player, Scanner scanner, Game game1, ArrayList<ResourceCard> resourceType){
		ArrayList<ResourceCard> resources = player.getResourceCards();
		int[] resourceCount = {0, 0, 0, 0, 0}; //array positions indicate brick, grain, lumber, ore, wool respectively
		for(int i = 0; i < resources.size(); i++){
			switch (resources.get(i).getResource()){
				case "brick" : resourceCount[0]++; break;
				case "grain" : resourceCount[1]++; break;
				case "lumber" : resourceCount[2]++; break;
				case "ore" : resourceCount[3]++; break;
				case "wool" : resourceCount[4]++; break;
				default : break;
			}
		}
		ArrayList<Integer> canTrade = new ArrayList<Integer>();
		System.out.println("You can trade the following resources:");
		for(int i = 0; i < resources.size(); i++){
			if(resourceCount[i] >= DIRECT_TRADE_NUMBER){
				System.out.println(resourceType.get(i).getResource() + ": " + resourceCount[i] + " available to trade - press " + i+1);
				canTrade.add(i);
			}
		}
		if(canTrade.size() != 0){
			ResourceCard tradeChoice = selectTradeResource(player, scanner, game1, canTrade, resourceType);
			ArrayList<ResourceCard> gainChoice = selectGainResource(player, scanner, game1);
			carryOutTrade(player, tradeChoice, gainChoice, "direct");
		}
		else{
			System.out.println("No resources available to trade. Turn over.");
			newTurn(player, game1, scanner);
		}
	}

	public static void tradeStandard(Player player, Scanner scanner, Game game1, ArrayList<ResourceCard> resourceType){
		ArrayList<ResourceCard> resources = player.getResourceCards();
		int[] resourceCount = {0, 0, 0, 0, 0}; //array positions indicate brick, grain, lumber, ore, wool respectively
		resourceCount = countPlayerResources(resourceCount, resources);
		ArrayList<Integer> canTrade = new ArrayList<Integer>();
		System.out.println("You can trade the following resources:");
		for(int i = 0; i < resources.size(); i++){
			if(resourceCount[i] >= STANDARD_TRADE_NUMBER){
				System.out.println(resourceType.get(i).getResource() + ": " + resourceCount[i] + " available to trade - press " + i+1);
				canTrade.add(i);
			}
		}
		if(canTrade.size() != 0){
			ResourceCard tradeChoice = selectTradeResource(player, scanner, game1, canTrade, resourceType);
			ArrayList<ResourceCard> gainChoice = selectGainResource(player, scanner, game1);
			carryOutTrade(player, tradeChoice, gainChoice, "standard");
		}
		else{
			System.out.println("No resources available to trade. Turn over.");
			newTurn(player, game1, scanner);
		}
	}

	public static void tradeSpecial(Player player, Scanner scanner, Game game1, ArrayList<ResourceCard> resourceType){
		ArrayList<Port> ownedPorts = player.getSpecialPorts();
		ArrayList<String> portTypes = new ArrayList<String>();
		for(int i = 0; i < ownedPorts.size(); i++){
			if(!(portTypes.contains(ownedPorts.get(i).getResource()))){
				portTypes.add(ownedPorts.get(i).getResource());
			}
		}

		System.out.println("You own the following 'special' port type(s). Please select which one you would like to trade with:");
		for(int i = 0; i < portTypes.size(); i++){
			System.out.println(portTypes.get(i) + " port - press " + i+1);
		}
		String gainChoiceName = null;
		int choice = scanner.nextInt();
		switch (choice) {
			case 1 :
				if(portTypes.size() == 1) gainChoiceName = portTypes.get(0);
				else System.out.println("Invalid choice. Please choose again."); tradeSpecial(player, scanner, game1, resourceType);
			case 2 :
				if(portTypes.size() <= 2) gainChoiceName = portTypes.get(1);
				else System.out.println("Invalid choice. Please choose again."); tradeSpecial(player, scanner, game1, resourceType);
			case 3 :
				if(portTypes.size() <= 3) gainChoiceName = portTypes.get(2);
				else System.out.println("Invalid choice. Please choose again."); tradeSpecial(player, scanner, game1, resourceType);
			case 4 :
				if(portTypes.size() <= 4) gainChoiceName = portTypes.get(3);
				else System.out.println("Invalid choice. Please choose again."); tradeSpecial(player, scanner, game1, resourceType);
			case 5 :
				if(portTypes.size() <= 5) gainChoiceName = portTypes.get(4);
				else System.out.println("Invalid choice. Please choose again."); tradeSpecial(player, scanner, game1, resourceType);
			default :
				System.out.println("Invalid choice. Please choose again."); tradeSpecial(player, scanner, game1, resourceType);
		}
		ArrayList<ResourceCard> gainChoice = new ArrayList<ResourceCard>();
		switch (gainChoiceName.toUpperCase()){
			case "H" :
				gainChoice = game1.getBrick(); break;
			case "G" :
				gainChoice = game1.getGrain(); break;
			case "F" :
				gainChoice = game1.getLumber(); break;
			case "M" :
				gainChoice = game1.getOre(); break;
			case "P" :
				gainChoice = game1.getWool(); break;
		}

		ArrayList<ResourceCard> resources = player.getResourceCards();
		int[] resourceCount = {0, 0, 0, 0, 0}; //array positions indicate brick, grain, lumber, ore, wool respectively
		resourceCount = countPlayerResources(resourceCount, resources);
		ArrayList<Integer> canTrade = new ArrayList<Integer>();
		System.out.println("You can trade the following resources:");
		for(int i = 0; i < resources.size(); i++){
			if(resourceCount[i] >= SPECIAL_TRADE_NUMBER){
				System.out.println(resourceType.get(i).getResource() + ": " + resourceCount[i] + " available to trade - press " + i+1);
				canTrade.add(i);
			}
		}
		if(canTrade.size() != 0){
			ResourceCard tradeChoice = selectTradeResource(player, scanner, game1, canTrade, resourceType);
			carryOutTrade(player, tradeChoice, gainChoice, "special");
		}
		else{
			System.out.println("No resources available to trade. Turn over.");
			newTurn(player, game1, scanner);
		}
	}

	public static int[] countPlayerResources(int[] resourceCount, ArrayList<ResourceCard> resources){
		for(int i = 0; i < resources.size(); i++){
			switch (resources.get(i).getResource()){
				case "brick" : resourceCount[0]++; break;
				case "grain" : resourceCount[1]++; break;
				case "lumber" : resourceCount[2]++; break;
				case "ore" : resourceCount[3]++; break;
				case "wool" : resourceCount[4]++; break;
				default : break;
			}
		}
		return resourceCount;
	}

	public static boolean hasStandardPort(Player player, Game game1){
		ArrayList<Port> ports = player.getStandardPorts();
		if(ports.size() > 0) return true;
		else return false;
	}

	public static boolean hasSpecialPort(Player player, Game game1){
		ArrayList<Port> ports = player.getSpecialPorts();
		if(ports.size() > 0) return true;
		else return false;
	}

	public static boolean tradeBankOrPlayer(Scanner scanner){
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

//-----Methods for player to player trade-----//

	//allows the player to trade with other players
	public static void tradePlayer(Player player, Scanner scanner, Game game1) {

		ArrayList<Player> players = game1.getPlayers();
		players.remove(player);

		//asks the player to choose a player to trade with
		System.out.println("Who do you want to trade with?");

		for (int i = 0; i < players.size(); i++) {
			System.out.println((i+1) + ". " + players.get(i).getName());
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
		ArrayList<ResourceCard> playerTradeResources = playerTrade.getResourceCards();

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
			playerTradeResources.remove(choice);

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
			tradePlayerResources(player, playerTrade, playerToTrade, playerTradeToTrade);
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
			System.out.println(i+1 + ". " + playerResources.get(i).getResource());
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
		System.out.println("1. Yes");
		System.out.println("2. No");

		int choice = scanner.nextInt();

		if (choice != 1 || choice != 2) {
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
			System.out.print("1x " + playerToTrade.get(i) + " ");
		}

		System.out.print("from their hand for ");

		for (int i = 0; i < playerTradeToTrade.size(); i++) {
			System.out.print("1x " + playerTradeToTrade.get(i) + " ");
		}

		System.out.print("from your hand");

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
		System.out.println("1. Accept Trade");
		System.out.println("2. Reject Trade");
		System.out.println("3. Propose a Counter-offer");

		int choice = scanner.nextInt();

		//checks for correct input
		if (choice != 1 || choice != 2 || choice != 3) {
			System.out.println("Invalid input. Please choose again.");
			return chooseOffer(scanner);
		}

		return choice;
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
