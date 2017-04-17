package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import intergroup.EmptyOuterClass.Empty;
import intergroup.Events.Event;
import intergroup.Events.Event.Error;
import intergroup.Messages.Message;
import intergroup.board.Board;
import intergroup.resource.Resource;
import intergroup.resource.Resource.Counts;
import intergroup.trade.Trade.WithPlayer;

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
	
	private static final String BANK = "B";
	private static final String PLAYER = "P";

//-----Method asking player who they want to trade with-----//
	
	public static boolean tradeBankOrPlayer(Player player, Scanner scanner) {
		
		Catan.printToClient("Press 'B' to trade with the bank, and 'P' to trade with other players:", player);
		
		String choice = Catan.getInputFromClient(player, scanner).toUpperCase();
		
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

	public static void tradeBank(Player player, Scanner scanner, Message enter, Game game1) throws IOException {
		
		player.updatePlayerPorts(player, game1);
		
		ArrayList<ResourceCard> resourceType = new ArrayList<ResourceCard>();
		resourceType.addAll(game1.getBrick());
		resourceType.addAll(game1.getGrain());
		resourceType.addAll(game1.getLumber());
		resourceType.addAll(game1.getOre());
		resourceType.addAll(game1.getWool());

		boolean hasStandard = false; 
		if(player.getStandardPorts().size() > 0) {
			hasStandard = true;
		}
		boolean hasSpecial = false; 
		if(player.getSpecialPorts().size() > 0) {
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
				Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("Invalid choice. Please request again").build()).build()).build(), player.getpSocket().getClientSocket());
			}
			break;
		case 2 :
			if (hasSpecial) {
				tradeSpecial(player, scanner, game1, resourceType);
			}
			else {
				Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("Invalid choice. Please request again").build()).build()).build(), player.getpSocket().getClientSocket());
			}
			break;
		default :
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("Invalid choice. Please request again").build()).build()).build(), player.getpSocket().getClientSocket());
			break;
		}
	}

	public static ResourceCard selectTradeResource(Player player, Scanner scanner, Game game1, ArrayList<Integer> canTrade, ArrayList<ResourceCard> resourceType){
			
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

	public static ArrayList<ResourceCard> selectGainResource(Player player, Scanner scanner, Game game1){
		
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

	public static void carryOutTrade(Player player, ResourceCard tradeChoice, ArrayList<ResourceCard> gainChoice, String tradeType){
		
		ArrayList<ResourceCard> playerResources = player.getResourceCards();
		ResourceCard removeResource = tradeChoice;
		
		int tradeNumber = 0;;
		
		switch (tradeType) {
		case "direct" :
			tradeNumber = DIRECT_TRADE_NUMBER; 
			break;
		case "standard" :
			tradeNumber = STANDARD_TRADE_NUMBER; 
			break;
		case "special" :
			tradeNumber = SPECIAL_TRADE_NUMBER; 
			break;
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
		Catan.printToClient("You can trade the following resources:", player);
		
		for (int i = 0; i < resourceCount.length; i++) {
			if (resourceCount[i] >= DIRECT_TRADE_NUMBER) {
				
				Catan.printToClient((i+1) + ": " + resourceType.get(i).getResource() + " (" + resourceCount[i] + " available)", player);
				canTrade.add(i);
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
			
			//makes sure the trade comprises of two different resources
			if (tradeChoice.getResource().equals(gainChoice.get(0).getResource())) {
				
				Catan.printToClient("You cannot trade 4 of one resource for 1 of the same resource. Trade cancelled", player);
				return;
			}
			
			if (bankAmount >= DIRECT_TRADE_NUMBER) {
				carryOutTrade(player, tradeChoice, gainChoice, "direct");
			}
			else Catan.printToClient("Not enough resources available in bank to trade. Trade cancelled.", player);
		}
		else {
			
			Catan.printToClient("No resources available to trade. Trade cancelled.", player);
			return;
		}
	}

	public static void tradeStandard(Player player, Scanner scanner, Game game1, ArrayList<ResourceCard> resourceType){
		
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
				carryOutTrade(player, tradeChoice, gainChoice, "standard");
			}
			else {
				Catan.printToClient("Not enough resources available in bank to trade. Trade cancelled.", player);
			}
		}
		else {
			
			Catan.printToClient("No resources available to trade. Trade cancelled.", player);
			return;
		}
	}

	public static void tradeSpecial(Player player, Scanner scanner, Game game1, ArrayList<ResourceCard> resourceType){
		
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
			carryOutTrade(player, tradePort, gainResource, "special");
		}
		else {
			Catan.printToClient("Not enough resources available in bank to trade. Trade cancelled.", player);
		}
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
		
		game.Board board1 = game1.getBoard();
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
	public static void tradePlayer(Player player, Scanner scanner, Message enter, Game game1) throws IOException {

		ArrayList<Player> players = game1.getPlayers();
		int tradeID = enter.getRequest().getInitiateTrade().getPlayer().getOther().getIdValue();
		Player playerTrade = players.get(tradeID);
		
		int noOffBrick = enter.getRequest().getInitiateTrade().getPlayer().getOffering().getBrick();
		int noOffOre = enter.getRequest().getInitiateTrade().getPlayer().getOffering().getOre();
		int noOffLumber = enter.getRequest().getInitiateTrade().getPlayer().getOffering().getLumber();
		int noOffWool = enter.getRequest().getInitiateTrade().getPlayer().getOffering().getWool();
		int noOffGrain = enter.getRequest().getInitiateTrade().getPlayer().getOffering().getGrain();
		
		int noWantBrick = enter.getRequest().getInitiateTrade().getPlayer().getWanting().getBrick();
		int noWantOre = enter.getRequest().getInitiateTrade().getPlayer().getWanting().getOre();
		int noWantLumber = enter.getRequest().getInitiateTrade().getPlayer().getWanting().getLumber();
		int noWantWool = enter.getRequest().getInitiateTrade().getPlayer().getWanting().getWool();
		int noWantGrain = enter.getRequest().getInitiateTrade().getPlayer().getWanting().getGrain();
		
		ArrayList<ResourceCard> playerToTrade = new ArrayList<ResourceCard>();
		ArrayList<ResourceCard> playerCards = player.getResourceCards();
		
		for (int i = 0; i < noOffBrick; i++) {
			for (int j = 0; j < playerCards.size(); j++) {
				if (playerCards.get(j).getResource().equals(BRICK)) {
					playerToTrade.add(playerCards.get(j));
				}
			}
		}
		
		for (int i = 0; i < noOffOre; i++) {
			for (int j = 0; j < playerCards.size(); j++) {
				if (playerCards.get(j).getResource().equals(BRICK)) {
					playerToTrade.add(playerCards.get(j));
				}
			}
		}
		
		for (int i = 0; i < noOffLumber; i++) {
			for (int j = 0; j < playerCards.size(); j++) {
				if (playerCards.get(j).getResource().equals(BRICK)) {
					playerToTrade.add(playerCards.get(j));
				}
			}
		}
		
		for (int i = 0; i < noOffWool; i++) {
			for (int j = 0; j < playerCards.size(); j++) {
				if (playerCards.get(j).getResource().equals(BRICK)) {
					playerToTrade.add(playerCards.get(j));
				}
			}
		}
		
		for (int i = 0; i < noOffGrain; i++) {
			for (int j = 0; j < playerCards.size(); j++) {
				if (playerCards.get(j).getResource().equals(BRICK)) {
					playerToTrade.add(playerCards.get(j));
				}
			}
		}
		
		ArrayList<ResourceCard> playerTradeToTrade = new ArrayList<ResourceCard>();
		ArrayList<ResourceCard> playerTradeCards = playerTrade.getResourceCards();
		
		for (int i = 0; i < noWantBrick; i++) {
			for (int j = 0; j < playerTradeCards.size(); j++) {
				if (playerTradeCards.get(j).getResource().equals(BRICK)) {
					playerTradeToTrade.add(playerTradeCards.get(j));
				}
			}
		}
		
		for (int i = 0; i < noWantOre; i++) {
			for (int j = 0; j < playerTradeCards.size(); j++) {
				if (playerTradeCards.get(j).getResource().equals(BRICK)) {
					playerTradeToTrade.add(playerTradeCards.get(j));
				}
			}
		}
		
		for (int i = 0; i < noWantLumber; i++) {
			for (int j = 0; j < playerTradeCards.size(); j++) {
				if (playerTradeCards.get(j).getResource().equals(BRICK)) {
					playerTradeToTrade.add(playerTradeCards.get(j));
				}
			}
		}
		
		for (int i = 0; i < noWantWool; i++) {
			for (int j = 0; j < playerTradeCards.size(); j++) {
				if (playerTradeCards.get(j).getResource().equals(BRICK)) {
					playerTradeToTrade.add(playerTradeCards.get(j));
				}
			}
		}
		
		for (int i = 0; i < noWantGrain; i++) {
			for (int j = 0; j < playerTradeCards.size(); j++) {
				if (playerTradeCards.get(j).getResource().equals(BRICK)) {
					playerTradeToTrade.add(playerTradeCards.get(j));
				}
			}
		}		
		
		int playerNum = 0;
		
		for (int i = 0; i < game1.getPlayers().size(); i++) {
			if (game1.getPlayers().get(i).equals(player)) {
				playerNum = i;
			}
		}
		
		//TODO event for player trade
		Message m = Message.newBuilder().setEvent(Event.newBuilder().setInstigator(Board.Player.newBuilder().setIdValue(playerNum).build()).setPlayerTradeInitiated(WithPlayer.newBuilder().setOther(Board.Player.newBuilder().setIdValue(tradeID).build()).setOffering(Resource.Counts.newBuilder().setBrick(noOffBrick).setLumber(noOffLumber).setOre(noOffOre).setGrain(noOffGrain).setWool(noOffWool).build()).setWanting(Resource.Counts.newBuilder().setBrick(noWantBrick).setLumber(noWantLumber).setOre(noWantOre).setGrain(noWantGrain).setWool(noWantWool).build()).build()).build()).build();
		
		Catan.printToClient(m, playerTrade);
		
		Message reply = Message.newBuilder().build();
		
		boolean success = false;
		
		while (!success) {
			
			reply = Catan.getPBMsg(playerTrade.getpSocket().getClientSocket());
			
			if (reply.getRequest().getBodyCase().getNumber() == 9) {
				success = true;
			}
			else {
				Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not a trade response request").build()).build()).build(), player.getpSocket().getClientSocket());
			}
		}
		
		int response = reply.getRequest().getSubmitTradeResponseValue();
		
		if (response == 0) {
			Catan.printToClient("Offer rejected. Trading stopped.", playerTrade);
			Catan.printToClient("Offer rejected. Trading stopped.", player);
			
			//TODO reject event
			Message reject = Message.newBuilder().setEvent(Event.newBuilder().setInstigator(Board.Player.newBuilder().setIdValue(playerNum).build()).setPlayerTradeRejected(Empty.newBuilder().build()).build()).build();
			
			for (int i = 0; i < players.size(); i++) {
				Catan.printToClient(reject, players.get(i));
			}
		}
		else {
			Catan.printToClient("Offer accepted. Trading stopped.", playerTrade);
			Catan.printToClient("Offer accepted. Trading stopped.", player);
			tradePlayerResources(player, playerTrade, playerToTrade, playerTradeToTrade);
			
			//TODO accept event
			Message accept = Message.newBuilder().setEvent(Event.newBuilder().setInstigator(Board.Player.newBuilder().setIdValue(playerNum).build()).setPlayerTradeAccepted(WithPlayer.newBuilder().setOther(Board.Player.newBuilder().setIdValue(tradeID).build()).setOffering(Resource.Counts.newBuilder().setBrick(noOffBrick).setLumber(noOffLumber).setOre(noOffOre).setGrain(noOffGrain).setWool(noOffWool).build()).setWanting(Resource.Counts.newBuilder().setBrick(noWantBrick).setLumber(noWantLumber).setOre(noWantOre).setGrain(noWantGrain).setWool(noWantWool).build()).build()).build()).build();
			
			for (int i = 0; i < players.size(); i++) {
				Catan.printToClient(accept, players.get(i));
			}
		}
	}

	//lets the player propose a trade with another player
	public static String proposeTrade(Player player, Player playerTrade, Scanner scanner) {

		ArrayList<ResourceCard> playerResources = player.getResourceCards();
		ArrayList<ResourceCard> playerTradeResources = playerTrade.getResourceCards();
		
		/*
		ArrayList<ResourceCard> playerTradeResources = new ArrayList<ResourceCard>();
		playerTradeResources.add(new ResourceCard(WOOL));
		playerTradeResources.add(new ResourceCard(LUMBER));
		playerTradeResources.add(new ResourceCard(ORE));
		playerTradeResources.add(new ResourceCard(BRICK));
		playerTradeResources.add(new ResourceCard(GRAIN));	*/

		ArrayList<ResourceCard> playerToTrade = new ArrayList<ResourceCard>();
		ArrayList<ResourceCard> playerTradeToTrade = new ArrayList<ResourceCard>();

		int choice = 1;

		//lets the player choose what to trade from their own hand
		while (choice == 1) {

			Catan.printToClient("What do you want to trade from your hand?", playerTrade);

			choice = chooseResources(playerResources, scanner, player);
			playerToTrade.add(playerResources.get(choice));
			playerResources.remove(choice);

			choice = chooseMoreResources(scanner, player);
		}

		choice = 1;

		//lets the player choose what resources they want from the other player's hand
		while (choice == 1) {

			Catan.printToClient("What do you want from " + playerTrade.getName() + "'s hand?", playerTrade);
			
			choice = chooseResources(playerTradeResources, scanner, player);
			playerTradeToTrade.add(playerTradeResources.get(choice));

			choice = chooseMoreResources(scanner, player);
		}

		boolean validTrade = checkOffer(playerToTrade, playerTradeToTrade);

		//checks if the trade is valid
		if (!validTrade) {
			
			Catan.printToClient("You cannot trade the same resources i.e 1 wool for 2 wool. Please choose again.", playerTrade);
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
	public static int chooseResources(ArrayList<ResourceCard> playerResources, Scanner scanner, Player player) {

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
	public static int chooseMoreResources(Scanner scanner, Player player) {

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
			ArrayList<ResourceCard> playerTradeToTrade, Scanner scanner) {
		
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
	public static int chooseOffer(Scanner scanner, Player playerTrade) {

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