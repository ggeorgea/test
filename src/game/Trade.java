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
		if (!(hasSpecial&&tradeSpecial(player,enter, scanner, game1, resourceType))) {	
			if(!(hasStandard&&tradeStandard(player, enter, scanner, game1, resourceType))){
				if(!tradeDirect(player, enter, scanner, game1, resourceType)){
				//	Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("Invalid trade").build()).build()).build(), player.getpSocket().getClientSocket());
					Catan.printToClient("Offer rejected incorrect resources. Trading stopped.", player);
					
					//TODO reject event
					Message reject = Message.newBuilder().setEvent(Event.newBuilder().setInstigator(Board.Player.newBuilder().setIdValue(player.getID()).build()).setPlayerTradeRejected(Empty.newBuilder().build()).build()).build();
					
					for (int i = 0; i < game1.getPlayers().size(); i++) {
						Catan.printToClient(reject,  game1.getPlayers().get(i));
					}
				}
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
	
	
	public static boolean carryOutTrade(Player player,  ResourceCard tradeChoice, ArrayList<ResourceCard> gainChoice, String tradeType, Message enter, Game game1){
		
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
			for (int j = 0; j < playerResources.size(); j++) {
				if (playerResources.get(j).getResource().equals(removeResource.getResource())) {
					playerResources.remove(playerResources.get(j));
					returnToBank(removeResource, game1);
					break;
				}
			}
		} 

		
		ResourceCard addResource = gainChoice.get(0);
		playerResources.add(addResource);
		player.setResourceCards(playerResources);
		
		
		
		Message acception = Message.newBuilder().setEvent(Event.newBuilder().setInstigator(intergroup.board.Board.Player.newBuilder().setIdValue(player.getID()).build()).setBankTrade(enter.getRequest().getInitiateTrade().getBank()).build()).build();
		for(Player k : game1.getPlayers()){
			Catan.printToClient(acception, k);
		}
		
		
		return true;
	}

	public static boolean tradeDirect(Player player, Message enter, Scanner scanner, Game game1, ArrayList<ResourceCard> resourceType){
		
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
				
				canTrade.add(i);
			}
		}
		
		if (canTrade.size() != 0) {
			
			
			
			
			int noOffBrick = enter.getRequest().getInitiateTrade().getBank().getOffering().getBrick();
			int noOffOre = enter.getRequest().getInitiateTrade().getBank().getOffering().getOre();
			int noOffLumber = enter.getRequest().getInitiateTrade().getBank().getOffering().getLumber();
			int noOffWool = enter.getRequest().getInitiateTrade().getBank().getOffering().getWool();
			int noOffGrain = enter.getRequest().getInitiateTrade().getBank().getOffering().getGrain();
			int noWantBrick = enter.getRequest().getInitiateTrade().getBank().getWanting().getBrick();
			int noWantOre = enter.getRequest().getInitiateTrade().getBank().getWanting().getOre();
			int noWantLumber = enter.getRequest().getInitiateTrade().getBank().getWanting().getLumber();
			int noWantWool = enter.getRequest().getInitiateTrade().getBank().getWanting().getWool();
			int noWantGrain = enter.getRequest().getInitiateTrade().getBank().getWanting().getGrain();
			
			ResourceCard tradeChoice = new ResourceCard();
			
			if (noOffBrick>0){
				tradeChoice = game1.getBrick().get(0);
			}
			if (noOffGrain>0){
				tradeChoice = game1.getGrain().get(0);
			}
			if (noOffLumber>0){
				tradeChoice = game1.getLumber().get(0);
			}
			if (noOffOre>0){
				tradeChoice = game1.getOre().get(0);
			}
			if (noOffWool>0){
				tradeChoice = game1.getWool().get(0);
			}
			
			
			ArrayList<ResourceCard> gainChoice = null;
			//int bankAmount = 0;
//			
//			switch (gainChoice) {
			if (noWantBrick>0){
				gainChoice = game1.getBrick();
				bankAmount = gainChoice.size();
			}
			if (noWantGrain>0){
				gainChoice = game1.getGrain();
				bankAmount = gainChoice.size();
			}
			if (noWantLumber>0){
				gainChoice = game1.getLumber();
				bankAmount = gainChoice.size();
			}
			if (noWantOre>0){
				gainChoice = game1.getOre();
				bankAmount = gainChoice.size();
			}
			if (noWantWool>0){
				gainChoice = game1.getWool();
				bankAmount = gainChoice.size();
			}
			
			//makes sure the trade comprises of two different resources
			if (tradeChoice.getResource().equals(gainChoice.get(0).getResource())) {
				return false;
			}
			
			//makes sure the trade comprises of two different resources
			if (tradeChoice.getResource().equals(gainChoice.get(0).getResource())) {
				return false;
			}
			
			if (bankAmount >= DIRECT_TRADE_NUMBER) {
				return carryOutTrade(player, tradeChoice, gainChoice, "direct",enter, game1);
			}			
			else return false;
		}
		else {		
			return false;
		}
	}

	public static boolean tradeStandard(Player player, Message enter, Scanner scanner, Game game1, ArrayList<ResourceCard> resourceType){
		
		ArrayList<ResourceCard> resources = player.getResourceCards();
		int[] resourceCount = {0, 0, 0, 0, 0}; //array positions indicate brick, grain, lumber, ore, wool respectively
		resourceCount = countPlayerResources(resourceCount, resources);
		ArrayList<Integer> canTrade = new ArrayList<Integer>();
		
		for (int i = 0; i < resources.size(); i++) {
			if (resourceCount[i] >= STANDARD_TRADE_NUMBER) {
				
				canTrade.add(i);
			}
		}
		
		if (canTrade.size() != 0) {
			
			int noOffBrick = enter.getRequest().getInitiateTrade().getBank().getOffering().getBrick();
			int noOffOre = enter.getRequest().getInitiateTrade().getBank().getOffering().getOre();
			int noOffLumber = enter.getRequest().getInitiateTrade().getBank().getOffering().getLumber();
			int noOffWool = enter.getRequest().getInitiateTrade().getBank().getOffering().getWool();
			int noOffGrain = enter.getRequest().getInitiateTrade().getBank().getOffering().getGrain();
			int noWantBrick = enter.getRequest().getInitiateTrade().getBank().getWanting().getBrick();
			int noWantOre = enter.getRequest().getInitiateTrade().getBank().getWanting().getOre();
			int noWantLumber = enter.getRequest().getInitiateTrade().getBank().getWanting().getLumber();
			int noWantWool = enter.getRequest().getInitiateTrade().getBank().getWanting().getWool();
			int noWantGrain = enter.getRequest().getInitiateTrade().getBank().getWanting().getGrain();
			
			ResourceCard tradeChoice = new ResourceCard();
			
			if (noOffBrick>0){
				tradeChoice = game1.getBrick().get(0);
			}
			if (noOffGrain>0){
				tradeChoice = game1.getGrain().get(0);
			}
			if (noOffLumber>0){
				tradeChoice = game1.getLumber().get(0);
			}
			if (noOffOre>0){
				tradeChoice = game1.getOre().get(0);
			}
			if (noOffWool>0){
				tradeChoice = game1.getWool().get(0);
			}
			
			
			ArrayList<ResourceCard> gainChoice = null;
			int bankAmount = 0;
			
//			switch (gainChoice) {
			if (noWantBrick>0){
				gainChoice = game1.getBrick();
				bankAmount = gainChoice.size();
			}
			if (noWantGrain>0){
				gainChoice = game1.getGrain();
				bankAmount = gainChoice.size();
			}
			if (noWantLumber>0){
				gainChoice = game1.getLumber();
				bankAmount = gainChoice.size();
			}
			if (noWantOre>0){
				gainChoice = game1.getOre();
				bankAmount = gainChoice.size();
			}
			if (noWantWool>0){
				gainChoice = game1.getWool();
				bankAmount = gainChoice.size();
			}
			
			//makes sure the trade comprises of two different resources
			if (tradeChoice.getResource().equals(gainChoice.get(0).getResource())) {
				return false;
			}
			if (bankAmount >= STANDARD_TRADE_NUMBER) {
				return carryOutTrade(player, tradeChoice, gainChoice, "standard",enter, game1);
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	public static boolean tradeSpecial(Player player, Message enter, Scanner scanner, Game game1, ArrayList<ResourceCard> resourceType){
		
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

		
		
		int noOffBrick = enter.getRequest().getInitiateTrade().getBank().getOffering().getBrick();
		int noOffOre = enter.getRequest().getInitiateTrade().getBank().getOffering().getOre();
		int noOffLumber = enter.getRequest().getInitiateTrade().getBank().getOffering().getLumber();
		int noOffWool = enter.getRequest().getInitiateTrade().getBank().getOffering().getWool();
		int noOffGrain = enter.getRequest().getInitiateTrade().getBank().getOffering().getGrain();
		
		
		ResourceCard portChoice = new ResourceCard();
		ResourceCard tradePort = null;
		
		if (noOffBrick>0){
			portChoice = game1.getBrick().get(0);
		}
		if (noOffGrain>0){
			portChoice = game1.getGrain().get(0);
		}
		if (noOffLumber>0){
			portChoice = game1.getLumber().get(0);
		}
		if (noOffOre>0){
			portChoice = game1.getOre().get(0);
		}
		if (noOffWool>0){
			portChoice = game1.getWool().get(0);
		}
		
		
		int noWantBrick = enter.getRequest().getInitiateTrade().getBank().getWanting().getBrick();
		int noWantOre = enter.getRequest().getInitiateTrade().getBank().getWanting().getOre();
		int noWantLumber = enter.getRequest().getInitiateTrade().getBank().getWanting().getLumber();
		int noWantWool = enter.getRequest().getInitiateTrade().getBank().getWanting().getWool();
		int noWantGrain = enter.getRequest().getInitiateTrade().getBank().getWanting().getGrain();

		ArrayList<ResourceCard> gainResource = null;
		int bankAmount = 0;	
//		switch (gainChoice) {
		if (noWantBrick>0){
			gainResource = game1.getBrick();
			bankAmount = gainResource.size();
		}
		if (noWantGrain>0){
			gainResource = game1.getGrain();
			bankAmount = gainResource.size();
		}
		if (noWantLumber>0){
			gainResource = game1.getLumber();
			bankAmount = gainResource.size();
		}
		if (noWantOre>0){
			gainResource = game1.getOre();
			bankAmount = gainResource.size();
		}
		if (noWantWool>0){
			gainResource = game1.getWool();
			bankAmount = gainResource.size();
		}
		
		//makes sure the trade comprises of two different resources
		if (portChoice.getResource().equals(gainResource.get(0).getResource())) {
			return false;
		}
		
		if (bankAmount >= SPECIAL_TRADE_NUMBER) {
			return carryOutTrade(player, tradePort, gainResource, "special",enter,game1);
		}
		else {
			return false;
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
		//////////////////////
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
			
			Message reject = Message.newBuilder().setEvent(Event.newBuilder().setInstigator(Board.Player.newBuilder().setIdValue(playerNum).build()).setPlayerTradeRejected(Empty.newBuilder().build()).build()).build();
			
			for (int i = 0; i < players.size(); i++) {
				Catan.printToClient(reject, players.get(i));
			}
		}
		else {
			Catan.printToClient("Offer accepted. Trading stopped.", playerTrade);
			Catan.printToClient("Offer accepted. Trading stopped.", player);
			tradePlayerResources(player, playerTrade, playerToTrade, playerTradeToTrade);
			
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