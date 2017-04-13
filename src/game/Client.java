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
	
	
	public void resolveEvent(Event event, Socket mySocket) {
		switch (event.getTypeCase().name()) {
		case "CHATMESSAGE":
			System.out.println("player "+event.getInstigator().getIdValue()+ ": "+event.getChatMessage());
			break;
		case "ERROR":
			System.out.println("Error: "+event.getError().getDescription());
			break;
		case "ROLLED":
			System.out.println( "player "+event.getInstigator().getIdValue()+" rolled a "+(event.getRolled().getA() + event.getRolled().getB())+"!");
			break;
		case "ROADBUILT":
			break;
		case "SETTLEMENTBUILT":
			break;
		case "CITYBUILT":
			break;
		case "DEVCARDBOUGHT":
			break;
		case "DEVCARDPLAYED":
			break;
		case "ROBBERMOVED":
			break;
		case "RESOURCESTOLEN":
			break;
		case "RESOURCECHOSEN":
			break;
		case "CARDSDISCARDED":
			break;
		case "BANKTRADE":
			break;
		case "PLAYERTRADE":
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
				//F", "M","G", "P", "H","?"
//				enum Kind {
//				  GENERIC = 0;
//				  BRICK = 1;
//				  LUMBER = 2;
//				  WOOL = 3;
//				  GRAIN = 4;
//				  ORE = 5;
//				}
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
			break;
		case "TYPE_NOT_SET	":
			break;
		default:
		}

	}
/*
	public static void resolveRequest(Request request, Socket mySocket) {
		switch (request.getBodyCase().name()) {

		case "ROLLDICE":
			break;
		case "BUYDEVCARD":
			break;
		case "BUILDROAD":
			break;
		case "BUILDSETTLEMENT":
			break;
		case "BUILDCITY":
			break;
		case "MOVEROBBER":
			break;
		case "PLAYDEVCARD":
			break;
		case "INITIATETRADE":
			break;
		case "SUBMITTRADERESPONSE":
			break;
		case "DISCARDRESOURCES":
			break;
		case "SUBMITTARGETPLAYER":
			break;
		case "CHOOSERESOURCE":
			break;
		case "ENDTURN":
			break;
		case "JOINLOBBY":
			break;
		case "CHATMESSAGE":
			break;
		case "BODY_NOT_SET":
			break;

		default:
		}

	}*/

}
