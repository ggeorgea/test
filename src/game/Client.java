package game;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import intergroup.Events.Event;
import intergroup.Requests.Request;
import intergroup.board.Board.Harbour;
import intergroup.board.Board.Hex;
import intergroup.board.Board.ResourceAllocation;
import intergroup.board.Board.Steal;
import intergroup.lobby.Lobby.GameSetup.PlayerSetting;

public class Client {

	ArrayList<String> lobbyMem = new ArrayList<String>();
//	ArrayList<String> lobbyUp = new ArrayList<String>();
	Game game = new Game();
	Board board = new Board();
	int myID = -1;
	Player myPlayer;
	boolean gameStarted= false;
	int mybrick = 0;
	int mylumber = 0;
	int mywool = 0;
	int mygrain = 0;
	int myore = 0;
	int myUnplayedKnights = 0;
	int myPlayedKnights = 0;
	int myMonopolyCards = 0;
	int victoryPointsCards = 0;
	int yearOfPlentyCards = 0;
	int myRoadBuildingCards = 0;

	public void adjustRecourses(int brickAdj, int lumberAdj, int woolAdj, int grainAdj, int oreAdj){
		mybrick+=brickAdj;
		mylumber+=lumberAdj;
		mywool+=woolAdj;
		mygrain+=grainAdj;
		myore+=oreAdj;
	}
	public void resolveEvent(Event event, Socket mySocket) {
		switch (event.getTypeCase().name()) {
			case "INITIALALLOCATION":
			for(ResourceAllocation rA : event.getInitialAllocation().getResourceAllocationList()){
				if(rA.getPlayer().getIdValue()==myID){
					mybrick += rA.getResources().getBrick();
					mylumber += rA.getResources().getLumber();
					mywool += rA.getResources().getWool();
					mygrain += rA.getResources().getGrain();
					myore += rA.getResources().getOre();
					System.out.println("you got "+rA.getResources().getBrick()+", lumber: "+rA.getResources().getLumber()+", wool: "+rA.getResources().getWool()+", grain: "+rA.getResources().getGrain()+", ore:"+rA.getResources().getOre());
				}
			}
			gameStarted = true;
			break;
			case "CHATMESSAGE":
			System.out.println("player "+event.getInstigator().getIdValue()+ ": "+event.getChatMessage());
			break;
			case "ERROR":
			System.out.println("Error: "+event.getError().getDescription());
			break;
			case "ROLLED":
			if(event.getInstigator().getIdValue()!=myID){
				System.out.println( "player "+event.getInstigator().getIdValue()+" rolled a "+(event.getRolled().getA() + event.getRolled().getB())+"!");
			}else{
				System.out.println( "You rolled a "+(event.getRolled().getA() + event.getRolled().getB())+"!");
			}
			if(event.getRolled().getResourceAllocationCount()!=0){
				for(ResourceAllocation rA : event.getInitialAllocation().getResourceAllocationList()){
					if(rA.getPlayer().getIdValue()==myID){
						mybrick += rA.getResources().getBrick();
						mylumber += rA.getResources().getLumber();
						mywool += rA.getResources().getWool();
						mygrain += rA.getResources().getGrain();
						myore += rA.getResources().getOre();
						System.out.println("you got "+rA.getResources().getBrick()+", lumber: "+rA.getResources().getLumber()+", wool: "+rA.getResources().getWool()+", grain: "+rA.getResources().getGrain()+", ore:"+rA.getResources().getOre());
					}
				}
			}

			break;
			case "ROADBUILT":
			int rx1 = event.getRoadBuilt().getA().getX();
			int ry1 = event.getRoadBuilt().getA().getY();
			int rx2 = event.getRoadBuilt().getB().getX();
			int ry2 = event.getRoadBuilt().getB().getY();


			Player roadplayer = null;
			for(Player p : game.getPlayers()){
				if(p.getID()==event.getInstigator().getIdValue()){
					roadplayer=p;
					break;
				}
			}		
			Road road = game.getBoard().getRoadFromCo(new Coordinate(rx1,ry1), new Coordinate(rx2,ry2));
			road.setOwner(roadplayer);
			roadplayer.setNoRoads(roadplayer.getNoRoads() - 1);
			System.out.println("player "+roadplayer.getID()+" placed a road at ("+rx1+", "+ry1+"),("+rx2+", "+ry2+")");

			if(roadplayer.getID()==myID){
				mybrick --;
				mylumber --;
			}
			break;
			case "SETTLEMENTBUILT":
			int bx = event.getSettlementBuilt().getX();
			int by = event.getSettlementBuilt().getY();
			Intersection settlement = (Intersection)board.getLocationFromCoordinate(new Coordinate(bx,by)).getContains();
			Player thisplayer = null;
			for(Player p : game.getPlayers()){
				if(p.getID()==event.getInstigator().getIdValue()){
					thisplayer=p;
					break;
				}
			}			
			settlement.setOwner(thisplayer);
			settlement.setBuilding(new Building("t", 1));
			thisplayer.setNoSettlements(thisplayer.getNoSettlements() + 1);
			thisplayer.setVictoryPoints(thisplayer.getVictoryPoints() + 1);		
			Trade.checkIfPortSettled(thisplayer, settlement, game);		

			System.out.println("player "+thisplayer.getID()+" placed a settlement at ("+bx+", "+by+")");
			if(thisplayer.getID()==myID){
				mybrick --;
				mylumber --;
				mygrain --;
				mywool --;
			}
			break;
			case "CITYBUILT":
			int cx = event.getCityBuilt().getX();
			int cy = event.getCityBuilt().getY();
			Intersection city = (Intersection) game.getBoard().getLocationFromCoordinate(new Coordinate (cx,cy)).getContains();

			Player cityPlayer = null;
			for(Player p : game.getPlayers()){
				if(p.getID()==event.getInstigator().getIdValue()){
					cityPlayer=p;
					break;
				}
			}		
			city.setBuilding(new Building("c", 2));
			cityPlayer.setNoCities(cityPlayer.getNoCities() + 1);
			cityPlayer.setNoSettlements(cityPlayer.getNoSettlements() - 1);
			cityPlayer.setVictoryPoints(cityPlayer.getVictoryPoints() + 1); 
			System.out.println("player "+cityPlayer.getID()+" placed a settlement at ("+cx+", "+cy+")");
			if(cityPlayer.getID()==myID){
				mygrain -=2;
				myore -=3;
			}
			break;
			case "DEVCARDBOUGHT":
			int typeOfEvent = event.getDevCardBought().getCardCase().getNumber();
			if(event.getInstigator().getIdValue()!=myID){
				switch(typeOfEvent){
					case 1:
					System.out.println("player "+event.getInstigator().getIdValue()+" bought a dev card");
					break;
					case 3:
					System.out.println("player "+event.getInstigator().getIdValue()+" bought a victory ponint dev card");
					break;
					case 2:
					System.out.println("error: should not know type of card");
					break;
				}
			}else{
				switch(typeOfEvent){
					case 1:
					System.out.println("error: should know type of card");
					break;
					case 3:
					System.out.println("you bought a victory ponint dev card");
					break;
					case 2:		
					int playableType = event.getDevCardBought().getPlayableDevCardValue();
					switch(playableType){
							  //KNIGHT 
						case 0:
						myUnplayedKnights++;
						System.out.println("you bought a knight");
							 // ROAD_BUILDING = 
						case 1:
						myRoadBuildingCards++;
						System.out.println("you bought a road building card");
							 // MONOPOLY = 
						case 2:
						myMonopolyCards++;
						System.out.println("you bought a monopoly card");
							//  YEAR_OF_PLENTY = 
						case 3: 							
						yearOfPlentyCards++;
						System.out.println("you bought a year Of Plenty Card");
					}

					break;					
				}				
				mywool --;
				mygrain --;
				myore --;
			}
			break;
			case "DEVCARDPLAYED":
			int playableType = event.getDevCardPlayed().getNumber();

			if(event.getInstigator().getIdValue()!=myID){
				int instID = event.getInstigator().getIdValue();
				switch(playableType){
					//KNIGHT 
					case 0:
					System.out.println("player "+instID+" played a knight");
					// ROAD_BUILDING = 
					case 1:
					System.out.println("player "+instID+" played a road building card");
					// MONOPOLY = 
					case 2:
					System.out.println("player "+instID+" played a monopoly card");
					//  YEAR_OF_PLENTY = 
					case 3: 
					System.out.println("player "+instID+" played a year Of Plenty Card");
				}

			}else{
				switch(playableType){
						  //KNIGHT 
					case 0:
					myUnplayedKnights--;
					myPlayedKnights++;
					System.out.println("you played a knight");
						 // ROAD_BUILDING = 
					case 1:
					myRoadBuildingCards--;
					System.out.println("you bought a road building card");
						 // MONOPOLY = 
					case 2:
					myMonopolyCards--;
					System.out.println("you played a monopoly card");
						//  YEAR_OF_PLENTY = 
					case 3: 							
					yearOfPlentyCards--;
					System.out.println("you played a year Of Plenty Card");
				}
			}
			break;
			case "ROBBERMOVED":
			int nx = event.getRobberMoved().getX();
			int ny = event.getRobberMoved().getY();
			game.getBoard().setRobber(new Coordinate(nx,ny));
			System.out.println("the robber was moved to: ("+nx+", "+ny+") by player"+ event.getInstigator().getIdValue());
			break;
			case "RESOURCESTOLEN":
			int quant = event.getResourceStolen().getQuantity();
			int reco = event.getResourceStolen().getResourceValue();

			if(event.getInstigator().getIdValue()==myID){
				switch(reco){
					//BRICK
					case  1:
					System.out.println("you stole brick X"+quant+" from player "+event.getResourceStolen().getVictim().getIdValue());
					mybrick+=quant;
					break;
					//LUMBER
					case  2:	
					System.out.println("you stole LUMBER X"+quant+" from player "+event.getResourceStolen().getVictim().getIdValue());
					mylumber+=quant;
					break;
					//WOOL
					case  3:	
					System.out.println("you stole WOOL X"+quant+" from player "+event.getResourceStolen().getVictim().getIdValue());
					mywool+=quant;
					break;
					//GRAIN
					case  4:	
					System.out.println("you stole GRAIN X"+quant+" from player "+event.getResourceStolen().getVictim().getIdValue());
					mygrain+=quant;
					break;
					//ORE
					case  5:	
					System.out.println("you stole ORE X"+quant+" from player "+event.getResourceStolen().getVictim().getIdValue());
					myore+=quant;
					break;
				}
			}else{
				if(event.getResourceStolen().getVictim().getIdValue() == myID){
					switch(reco){
						//BRICK
						case  1:
						System.out.println("you lost brick X"+quant+" to player "+event.getInstigator().getIdValue());
						mybrick-=quant;
						break;
						//LUMBER
						case  2:	
						System.out.println("you lost LUMBER X"+quant+" to player "+event.getInstigator().getIdValue());
						mylumber-=quant;
						break;
						//WOOL
						case  3:	
						System.out.println("you lost WOOL X"+quant+" to player "+event.getInstigator().getIdValue());
						mywool-=quant;
						break;
						//GRAIN
						case  4:	
						System.out.println("you lost GRAIN X"+quant+" to player "+event.getInstigator().getIdValue());
						mygrain-=quant;
						break;
						//ORE
						case  5:	
						System.out.println("you lost ORE X"+quant+" to player "+event.getInstigator().getIdValue());
						myore-=quant;
						break;
					}
				}
				else{
					System.out.println("player "+event.getInstigator().getIdValue()+" stole "+quant+" resources from player"+ event.getResourceStolen().getVictim().getIdValue());
				}
			}
			break;
			case "RESOURCECHOSEN":
				//what is this for???
			if(event.getInstigator().getIdValue()!=myID){
				switch(event.getResourceChosenValue()){
					//GENERIC
					case  0:
					System.out.println("player "+event.getInstigator().getIdValue()+" chose generic");
					break;
					//BRICK
					case  1:
					System.out.println("player "+event.getInstigator().getIdValue()+" chose brick");
					break;
					//LUMBER
					case  2:					
					System.out.println("player "+event.getInstigator().getIdValue()+" chose lumber");
					break;
					//WOOL
					case  3:						
					System.out.println("player "+event.getInstigator().getIdValue()+" chose wool");
					break;
					//GRAIN
					case  4:						
					System.out.println("player "+event.getInstigator().getIdValue()+" chose grain");
					break;
					//ORE
					case  5:						
					System.out.println("player "+event.getInstigator().getIdValue()+" chose ore");
					break;

				}
			}else{
				switch(event.getResourceChosenValue()){
					//GENERIC
					case  0:
					System.out.println("you chose generic");
					break;
					//BRICK
					case  1:
					System.out.println("you chose brick");
					break;
					//LUMBER
					case  2:					
					System.out.println("you chose lumber");
					break;
					//WOOL
					case  3:						
					System.out.println("you chose wool");
					break;
					//GRAIN
					case  4:						
					System.out.println("you chose grain");
					break;
					//ORE
					case  5:						
					System.out.println("you chose ore");
					break;

				}
			}
			break;
			case "CARDSDISCARDED":
			
			int brick = event.getCardsDiscarded().getBrick();
			int lumber = event.getCardsDiscarded().getLumber();
			int wool = event.getCardsDiscarded().getWool();
			int grain = event.getCardsDiscarded().getGrain();
			int ore = event.getCardsDiscarded().getOre();

			if(event.getInstigator().getIdValue()!=myID){
				System.out.println("player "+event.getInstigator().getIdValue()+"discarded brick: "+brick+", lumber: "+lumber+", wool: "+wool+", grain: "+grain+", ore:"+ore);

			}else{
				System.out.println("you discarded brick: "+brick+", lumber: "+lumber+", wool: "+wool+", grain: "+grain+", ore:"+ore);
				mybrick -= brick;
				mylumber -= lumber;
				mywool -= wool;
				mygrain-= grain;
				myore -= ore;
			}
			break;
			case "BANKTRADE":
			int brickO = event.getBankTrade().getOffering().getBrick();
			int lumberO = event.getBankTrade().getOffering().getLumber();
			int woolO = event.getBankTrade().getOffering().getWool();
			int grainO = event.getBankTrade().getOffering().getGrain();
			int oreO = event.getBankTrade().getOffering().getOre();
			int brickW = event.getBankTrade().getWanting().getBrick();
			int lumberW = event.getBankTrade().getWanting().getLumber();
			int woolW = event.getBankTrade().getWanting().getWool();
			int grainW = event.getBankTrade().getWanting().getGrain();
			int oreW = event.getBankTrade().getWanting().getOre();

			if(event.getInstigator().getIdValue()!=myID){
				System.out.println("player "+event.getInstigator().getIdValue()+" traded with the bank, GIVING: brick "+brickO+", lumber "+lumberO+", wool "+woolO+", grain "+grainO+", ore "+oreO+" GETTING: brick "+brickW+", lumber "+lumberW+", wool "+woolW+", grain "+grainW+", ore "+oreW);
			}else{
				System.out.println("you traded with the bank, GIVING: brick "+brickO+", lumber "+lumberO+", wool "+woolO+", grain "+grainO+", ore "+oreO+" GETTING: brick "+brickW+", lumber "+lumberW+", wool "+woolW+", grain "+grainW+", ore "+oreW);
				mybrick += brickW-brickO;
				mylumber += lumberW- lumberO;
				mywool += woolW-woolO;
				mygrain += grainW-grainO;
				myore  += oreW-oreO;
			}
			break;	
			case "PLAYERTRADEACCEPTED":
			brickO = event.getBankTrade().getOffering().getBrick();
			lumberO = event.getBankTrade().getOffering().getLumber();
			woolO = event.getBankTrade().getOffering().getWool();
			grainO = event.getBankTrade().getOffering().getGrain();
			oreO = event.getBankTrade().getOffering().getOre();
			brickW = event.getBankTrade().getWanting().getBrick();
			lumberW = event.getBankTrade().getWanting().getLumber();
			woolW = event.getBankTrade().getWanting().getWool();
			grainW = event.getBankTrade().getWanting().getGrain();
			oreW = event.getBankTrade().getWanting().getOre();
			if(event.getInstigator().getIdValue()!=myID){
				if(event.getPlayerTradeAccepted().getOther().getIdValue()!=myID){
					System.out.println("player "+event.getInstigator().getIdValue()+" traded with player "+ event.getPlayerTradeAccepted().getOther().getIdValue()+", GIVING: brick "+brickO+", lumber "+lumberO+", wool "+woolO+", grain "+grainO+", ore "+oreO+" GETTING: brick "+brickW+", lumber "+lumberW+", wool "+woolW+", grain "+grainW+", ore "+oreW);
				}
				else{
					System.out.println("player "+event.getInstigator().getIdValue()+" traded with you , GIVING: brick "+brickO+", lumber "+lumberO+", wool "+woolO+", grain "+grainO+", ore "+oreO+" GETTING: brick "+brickW+", lumber "+lumberW+", wool "+woolW+", grain "+grainW+", ore "+oreW);
					mybrick -= brickW-brickO;
					mylumber -= lumberW- lumberO;
					mywool -= woolW-woolO;
					mygrain -= grainW-grainO;
					myore  -= oreW-oreO;
					mybrick += brickW-brickO;
					mylumber += lumberW- lumberO;
					mywool += woolW-woolO;
					mygrain += grainW-grainO;
					myore  += oreW-oreO;
				}
			}else{
				System.out.println("you traded with player "+ event.getPlayerTradeAccepted().getOther().getIdValue()+", GIVING: brick "+brickO+", lumber "+lumberO+", wool "+woolO+", grain "+grainO+", ore "+oreO+" GETTING: brick "+brickW+", lumber "+lumberW+", wool "+woolW+", grain "+grainW+", ore "+oreW);

			}
			break;			
			case "PLAYERTRADEINITIATED":
			brickO = event.getBankTrade().getOffering().getBrick();
			lumberO = event.getBankTrade().getOffering().getLumber();
			woolO = event.getBankTrade().getOffering().getWool();
			grainO = event.getBankTrade().getOffering().getGrain();
			oreO = event.getBankTrade().getOffering().getOre();
			brickW = event.getBankTrade().getWanting().getBrick();
			lumberW = event.getBankTrade().getWanting().getLumber();
			woolW = event.getBankTrade().getWanting().getWool();
			grainW = event.getBankTrade().getWanting().getGrain();
			oreW = event.getBankTrade().getWanting().getOre();
			if(event.getInstigator().getIdValue()!=myID){
				if(event.getPlayerTradeAccepted().getOther().getIdValue()!=myID){
					System.out.println("player "+event.getInstigator().getIdValue()+" wants to trade with player "+ event.getPlayerTradeAccepted().getOther().getIdValue()+", GIVING: brick "+brickO+", lumber "+lumberO+", wool "+woolO+", grain "+grainO+", ore "+oreO+" GETTING: brick "+brickW+", lumber "+lumberW+", wool "+woolW+", grain "+grainW+", ore "+oreW);
				}
				else{
					System.out.println("player "+event.getInstigator().getIdValue()+" wants to trade with you , GIVING: brick "+brickO+", lumber "+lumberO+", wool "+woolO+", grain "+grainO+", ore "+oreO+" GETTING: brick "+brickW+", lumber "+lumberW+", wool "+woolW+", grain "+grainW+", ore "+oreW);
					System.out.println("what is your response?");
				}
			}else{
				System.out.println("you want to trade with player "+ event.getPlayerTradeAccepted().getOther().getIdValue()+", GIVING: brick "+brickO+", lumber "+lumberO+", wool "+woolO+", grain "+grainO+", ore "+oreO+" GETTING: brick "+brickW+", lumber "+lumberW+", wool "+woolW+", grain "+grainW+", ore "+oreW);

			}				
			break;			
			case "PLAYERTRADEREJECTED":
			System.out.println("the trade was rejected");
			break;			
			case "TURNENDED":
			System.out.println("turn over");
			break;
			case "GAMEWON":
			System.out.println("THANK YOU FOR PLAYING");
			break;
			case "BEGINGAME":
			List<Hex> hexes = event.getBeginGame().getHexesList();
			List<Harbour> habours = event.getBeginGame().getHarboursList();
			List<PlayerSetting> playerSettings = event.getBeginGame().getPlayerSettingsList();
				//intergroup.board.Board.Player ownPlayer = event.getBeginGame().getOwnPlayer();
			ArrayList<Player> players = new ArrayList<Player>();
			board = Setup.getMeABoard();
			for(PlayerSetting pSett : playerSettings){
				Player p1 = new Player();
				p1.setName(pSett.getUsername());
				p1.setColour(pSett.getColourValue());
				p1.setID(pSett.getPlayer().getIdValue());
				players.add(p1);
			}
			for(Hex hex:hexes){
				int x  = hex.getLocation().getX();
				int y = hex.getLocation().getY();
				Location l1 = board.getLocationFromCoordinate(new Coordinate(x,y));
				game.Hex boardHex = (game.Hex)l1.getContains();
				boardHex.setNumber(hex.getNumberToken());
				int terrVal = hex.getTerrainValue();
				String terrain = "";
				switch(terrVal){
					case 0:
					terrain = "H";
					break;
					case 1:
					terrain = "P";
					break;
					case 2:
					terrain = "M";
					break;
					case 3:
					terrain = "G";
					break;
					case 4:
					terrain = "F";
					break;
					case 5:
					terrain = "D";
					break;
					default:
					System.out.println("error allocating hex terrain");
				}
				boardHex.setTerrain(terrain);				
			}
			for(Harbour habour: habours){
				int x1  = habour.getLocation().getA().getX();
				int y1 = habour.getLocation().getA().getY();
				int x2  = habour.getLocation().getB().getX();
				int y2 = habour.getLocation().getB().getY();
				Port boardPort = new Port();
				for( Port tryPort: board.getPorts()){
					Road r1 = board.getRoadFromCo(new Coordinate(x1,y1), new Coordinate(x2,y2));
					if(tryPort.getCoordinateA()==r1.getCoordinateA()&&tryPort.getCoordinateB()==r1.getCoordinateB()){
						boardPort = tryPort;
						break;
					}
				}

				int rec = habour.getResourceValue();
				String recourse = "";
				switch(rec){
					case 0:
					recourse = "?";
					break;
					case 1:
					recourse = "H";
					break;
					case 2:
					recourse = "F";
					break;
					case 3:
					recourse = "P";
					break;
					case 4:
					recourse = "G";
					break;
					case 5:
					recourse = "M";
					break;
					default:
					System.out.println("error allocating port resource");
				}
				boardPort.setResource(recourse);
				myID = event.getBeginGame().getOwnPlayer().getIdValue();


			}
			game.setBoard(board);
			game.setPlayers(players);
			for(Player p : game.getPlayers()){
				if(p.getID()==myID){
					myPlayer=p;
					break;
				}
			}		
			break;
			case "LOBBYUPDATE":
			ArrayList<String> lobbyNames = new ArrayList<String>();
			int count = event.getLobbyUpdate().getUsernameCount();
			for(int i = 0; i<count; i++){
				lobbyNames.add(event.getLobbyUpdate().getUsername(i));
			}
			for(String name : lobbyNames){
				if(!lobbyMem.contains(name)){
					System.out.println(name+" is in the lobby!");
				}
			}
			lobbyMem = lobbyNames;

			break;
			case "MONOPOLYRESOLUTION":					
			for(int i = 0; i<event.getMonopolyResolution().getTheftsCount();i++){
				Steal thisSteal = event.getMonopolyResolution().getThefts(i);
				int Aquant = thisSteal.getQuantity();
				int Areco = thisSteal.getResourceValue();

				if(event.getInstigator().getIdValue()==myID){
					switch(Areco){
						//BRICK
						case  1:
						System.out.println("you stole brick X"+Aquant+" from player "+thisSteal.getVictim().getIdValue());
						mybrick+=Aquant;
						break;
						//LUMBER
						case  2:	
						System.out.println("you stole LUMBER X"+Aquant+" from player "+thisSteal.getVictim().getIdValue());
						mylumber+=Aquant;
						break;
						//WOOL
						case  3:	
						System.out.println("you stole WOOL X"+Aquant+" from player "+thisSteal.getVictim().getIdValue());
						mywool+=Aquant;
						break;
						//GRAIN
						case  4:	
						System.out.println("you stole GRAIN X"+Aquant+" from player "+thisSteal.getVictim().getIdValue());
						mygrain+=Aquant;
						break;
						//ORE
						case  5:	
						System.out.println("you stole ORE X"+Aquant+" from player "+thisSteal.getVictim().getIdValue());
						myore+=Aquant;
						break;
					}
				}else{
					if(thisSteal.getVictim().getIdValue() == myID){
						switch(Areco){
							//BRICK
							case  1:
							System.out.println("you lost brick X"+Aquant+" to player "+event.getInstigator().getIdValue());
							mybrick-=Aquant;
							break;
							//LUMBER
							case  2:	
							System.out.println("you lost LUMBER X"+Aquant+" to player "+event.getInstigator().getIdValue());
							mylumber-=Aquant;
							break;
							//WOOL
							case  3:	
							System.out.println("you lost WOOL X"+Aquant+" to player "+event.getInstigator().getIdValue());
							mywool-=Aquant;
							break;
							//GRAIN
							case  4:	
							System.out.println("you lost GRAIN X"+Aquant+" to player "+event.getInstigator().getIdValue());
							mygrain-=Aquant;
							break;
							//ORE
							case  5:	
							System.out.println("you lost ORE X"+Aquant+" to player "+event.getInstigator().getIdValue());
							myore-=Aquant;
							break;
						}
					}
					else{
						System.out.println("player "+event.getInstigator().getIdValue()+" stole "+Aquant+" resources from player"+thisSteal.getVictim().getIdValue());
					}
				}					
			}					
			break;
			case "TYPE_NOT_SET	":
			break;
			default:
		}

	}

}
