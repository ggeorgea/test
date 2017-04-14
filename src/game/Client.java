package game;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import intergroup.Events.Event;
import intergroup.Requests.Request;
import intergroup.board.Board.Harbour;
import intergroup.board.Board.Hex;
import intergroup.lobby.Lobby.GameSetup.PlayerSetting;

public class Client {

	ArrayList<String> lobbyMem = new ArrayList<String>();
//	ArrayList<String> lobbyUp = new ArrayList<String>();
	Game game = new Game();
	Board board = new Board();
	int myID = -1;
	Player myPlayer;
	
	
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
				if(event.getInstigator().getIdValue()!=myID){
					
				}else{
					//TODO me
				}
				break;
			case "RESOURCECHOSEN":
				if(event.getInstigator().getIdValue()!=myID){
					
				}else{
					//TODO me
				}
				break;
			case "CARDSDISCARDED":
				if(event.getInstigator().getIdValue()!=myID){
					
				}else{
					//TODO me
				}
				break;
			case "BANKTRADE":
				if(event.getInstigator().getIdValue()!=myID){
					
				}else{
					//TODO me
				}
				break;
			case "PLAYERTRADE":
				if(event.getInstigator().getIdValue()!=myID){
					
				}else{
					//TODO me
				}
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
				if(event.getInstigator().getIdValue()!=myID){
					
				}else{
					//TODO me
				}
				break;
			case "TYPE_NOT_SET	":
				break;
			default:
			}

	}

}
