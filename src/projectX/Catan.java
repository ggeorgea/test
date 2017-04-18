package projectX;

//import Box;
//import CustomOutputStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

import intergroup.EmptyOuterClass.Empty;
import intergroup.Events.Event;
import intergroup.Events.Event.Error;
import intergroup.Messages.Message;
import intergroup.Requests.Request;
import intergroup.board.Board.Edge;
import intergroup.board.Board.Hex;
import intergroup.board.Board.Harbour;
import intergroup.lobby.Lobby.*;
import intergroup.board.Board.PlayableDevCard;
import intergroup.board.Board.Player;
import intergroup.board.Board.Point;
import intergroup.lobby.Lobby.GameSetup;
import intergroup.lobby.Lobby.GameSetup.Builder;
import intergroup.lobby.Lobby.GameSetup.PlayerSetting;
import intergroup.lobby.Lobby.Join;
import intergroup.resource.Resource.Counts;
import intergroup.resource.Resource.Kind;
import intergroup.trade.Trade.Response;
import intergroup.trade.Trade.WithBank;
import intergroup.trade.Trade.WithPlayer;
/**
 * Class that contains the main method for the game
 * When testing the game this class's main method was executed
 * Run this class 
 */
public class Catan {

    static int writInd = -1;
    static  Box b1 = new Box();
	static PrintStream out1 = new PrintStream( new CustomOutputStream(b1.getjTextArea1()));

    public static int getNextInt(){
    	
    	while(true){
    		try{
    		return Integer.parseInt(getNextLine());
    		}
    		catch(NumberFormatException  e){
    			
    		}
    	}
    	
  			
    
    }
    public static boolean nextLineExist(){
    	if(b1.getWriting().size()-1>writInd){
    		return true;
    	}
    	else{
    		return false;    	
    		}
    }
    
	public static String getNextLine(){
//		out1.print("(w)");
		while(true){	
			if(b1.getWriting().size()-1>writInd){
				writInd++;
				String next = b1.getWriting().get(writInd);
				System.out.println(next);				
				
				return next;
			}
		}
		
		
	}
	
	private static Coordinate[] swapToProtoOrder(Coordinate[] coords){
		if((coords[0].getX()+coords[0].getY())>(coords[1].getX()+coords[1].getY())){
			Coordinate keepCoord = coords[0];
			coords[0]  = coords[1];
			coords[1] = keepCoord;
		}
		return coords;
	}
	
	private static final boolean END_GAME = true;
	//Main method to run the game
	
	
	
	
	
	
	public static void main(String[] args) throws Exception {

		
		   //     Box b1 = new Box();
		        
		        
		        /* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
		    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            javax.swing.UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (ClassNotFoundException ex) {
		    java.util.logging.Logger.getLogger(Box.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
		    java.util.logging.Logger.getLogger(Box.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
		    java.util.logging.Logger.getLogger(Box.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
		    java.util.logging.Logger.getLogger(Box.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>
		
		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
		    public void run() {
		        
		        b1.setVisible(true);
		       
		    }
		});
		
		Thread thread2 = new Thread(new Runnable() {
		@Override
		public void run() {
				try{
					
//					int writInd = -1;
//		    	Scanner scan1 = new Scanner(System.in);
//		    		try {
//		    			
//		    			
//		    			while(true){
//							if(System.in.available()!=0){					
//					    		out1.print("\n"+scan1.nextLine());
//							}
//				if(b1.getWriting().size()-1>writInd){
//				String next = b1.getWriting().get(writInd+1);
//				out1.print("\n"+next);
//				writInd++;
//			}
//							
//							
//							
//						}
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
			    	

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		Board board1 = new Board();
		
		
		boolean keepPlaying = true;

		// will let the players play another game if they wish
		while (keepPlaying) {

			out1.print("\n"+"----------SETTLERS OF CATAN----------\n\n");
			//Scanner scanner = new Scanner(System.in);

//NETWORKING CODE
//------------------------------------------------------
			out1.print("\n"+"activate client mode? (y)");
			String answer = getNextLine();
			int defaultPortNumber = 6789;

			if (answer.equals("y")) {
				out1.print("\n"+"please enter a userName:");
				String userName = getNextLine();
				//CLIENT SIDE
				out1.print("\n"+"please enter the address where you wish to connect\nPlease note that to enter a command, enter \"c\"\nto see the map, enter \"map\"\nto see your resources, enter \"resources\"\nto see your development cards, enter \"cards\"\nto see the usernames and colours of players, enter \"players\"");
				String hostName = getNextLine();
				int portNumber = defaultPortNumber;
				
				try {

					Client client = new Client();
					Socket kkSocket = new Socket(hostName, portNumber);	  
					Message joinRequest = Message.newBuilder().setRequest(Request.newBuilder().setJoinLobby(Join.newBuilder().setUsername(userName).build()).build()).build();
					sendPBMsg(joinRequest,kkSocket);
					String fromServer;
					String fromUser;
					while(true){
						if(kkSocket.getInputStream().available()!=0){
							Message m1 = getPBMsg(kkSocket);		
							if(m1.getTypeCase().name().equals("EVENT")){
								if(m1.getEvent().getTypeCase().name().equals("CHATMESSAGE")){
									fromServer = m1.getEvent().getChatMessage();				            	
									out1.print("\n"+fromServer);		
									if (fromServer.equals("Goodbye!")) {
										break;
									}		
								}
								else{
									client.resolveEvent(m1.getEvent(),kkSocket);
								}
							}			    
							else{
							//	out1.print("\n"+":(");
							}
						}
						else if (nextLineExist()){
							String instruction = getNextLine();
							if(instruction.equals("map")){
								ArrayList<projectX.Player> emptyP = new ArrayList<projectX.Player>();
								projectX.Player playerE = new projectX.Player();
								playerE.setpSocket(null);
								emptyP.add(playerE);
								Map.printMap(client.game.getBoard(), emptyP);
							}
							if(instruction.equals("players")){
								for(projectX.Player p : client.game.getPlayers()){
									out1.print("\n"+"player "+p.getID()+" , aka: "+p.getUserName()+" playing as: "+p.getName());
								}
							}
							if(instruction.equals("resources")){
								out1.print("\n"+"You have: brick "+client.mybrick+", wool "+client.mywool+", grain "+client.mygrain+", lumber "+client.mylumber+", ore "+client.myore);
							}
							if(instruction.equals("cards")){
								out1.print("\n"+"You have: unplayedKnights "+client.myUnplayedKnights+", played knights "+client.myPlayedKnights+", monopoly cards "+client.myMonopolyCards+", victory point cards "+client.victoryPointsCards+", year of plenty cards "+client.yearOfPlentyCards+", road building cards "+client.myRoadBuildingCards+"");
							}
							if(instruction.equals("c")){
								out1.print("\n"+"creating a message: to see options type \"P\", to cancel, type \"E\"");
								String instruction2 = getNextLine();
								if(instruction2.equals("p")){
									out1.print("\n"+ "ROLLDICE, BUYDEVCARD, BUILDROAD, BUILDSETTLEMENT, BUILDCITY, MOVEROBBER, PLAYDEVCARD, INITIATETRADE, \n"
										+ "SUBMITTRADERESPONSE, DISCARDRESOURCES, SUBMITTARGETPLAYER, CHOOSERESOURCE, ENDTURN\nanything else will be sent as a chat message!");
									instruction2 = getNextLine();
								}
								switch (instruction2) {		            			
									case "E":
									break;
									case "ROLLDICE":
									sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setRollDice(Empty.newBuilder().build()).build()).build(),kkSocket);
									break;
									case "BUYDEVCARD":
									sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setBuyDevCard(Empty.newBuilder().build()).build()).build(),kkSocket);
									break;
									case "ENDTURN":
									sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setEndTurn(Empty.newBuilder().build()).build()).build(),kkSocket);          				
									break;		            						            			
									case "BUILDROAD":
									out1.print("\n"+"Please select where to build a road");
									out1.print("\n"+"road start: Select X coordinate");
									int x1 = getNextInt();
									out1.print("\n"+"road start: Select Y coordinate");
									int y1 = getNextInt();
									out1.print("\n"+"road end: Select X coordinate");
									int x2 = getNextInt();
									out1.print("\n"+"road end: Select Y coordinate");
									int y2 = getNextInt();
									sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setBuildRoad(Edge.newBuilder().setA(Point.newBuilder().setX(x1).setY(y1).build()).setB(Point.newBuilder().setX(x2).setY(y2).build()).build()).build()).build(),kkSocket);          						            				
									break;		            		
									case "BUILDSETTLEMENT":
									out1.print("\n"+"Please select where to build a settlement");
									out1.print("\n"+"Select X coordinate");
									int x = getNextInt();
									out1.print("\n"+"Select Y coordinate");
									int y = getNextInt();
									sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setBuildSettlement(Point.newBuilder().setX(x).setY(y).build()).build()).build(),kkSocket);          						            				
									break;
									case "BUILDCITY":
									out1.print("\n"+"Please select where to build a city");
									out1.print("\n"+"Select X coordinate");
									x = getNextInt();
									out1.print("\n"+"Select Y coordinate");
									y = getNextInt();
									sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setBuildCity(Point.newBuilder().setX(x).setY(y).build()).build()).build(),kkSocket);          						            				
									break;
									case "MOVEROBBER":
									out1.print("\n"+"Please select where to build a city");
									out1.print("\n"+"Select X coordinate");
									x = getNextInt();
									out1.print("\n"+"Select Y coordinate");
									y = getNextInt();
									sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setMoveRobber(Point.newBuilder().setX(x).setY(y).build()).build()).build(),kkSocket);  
									break;		            		
									case "PLAYDEVCARD":
									out1.print("\n"+"which type of dev card do you wish to try and play: ");
									boolean correct = false;
									while(!correct){
										String response = getNextLine();
										switch(response){
											case "KNIGHT":
											correct = true;
											sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setPlayDevCard(PlayableDevCard.KNIGHT).build()).build(),kkSocket);
											break;
											case "ROAD_BUILDING" :
											correct = true;
											sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setPlayDevCard(PlayableDevCard.ROAD_BUILDING).build()).build(),kkSocket);
											break;
											case "MONOPOLY" :
											correct = true;
											sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setPlayDevCard(PlayableDevCard.MONOPOLY).build()).build(),kkSocket);
											break;
											case "YEAR_OF_PLENTY":
											correct = true;
											sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setPlayDevCard(PlayableDevCard.YEAR_OF_PLENTY).build()).build(),kkSocket);
											break;
											default:
											out1.print("\n"+"not a type of dev card, try again");
										}
									}		
									break;
									case "INITIATETRADE":
									out1.print("\n"+"do you wish to trade with a PLAYER or with a BANK");
									String ans = getNextLine();
									switch(ans){
										case ("PLAYER"):
										boolean finished1 = false;
										int brick1 = 0;
										int lumber1 = 0;
										int wool1 = 0;
										int grain1 = 0;
										int ore1 = 0;
										while(!finished1){
											out1.print("\n"+"pick a number of a specific resource to ask for, if you have finished choosing, enter -1");
											int numChos = getNextInt();
											if(numChos==-1){
												finished1 = true;
												break;		         
											}
											out1.print("\n"+"please enter the specific resource, from: brick, lumber, wool, grain, ore");
											String recChos = getNextLine();
											switch(recChos){
												case "brick":
												brick1 += numChos;
												break;
												case "lumber":
												lumber1 += numChos;
												break;
												case "wool":
												wool1 += numChos;
												break;
												case "grain":
												grain1+=numChos;
												break;
												case "ore":
												ore1+=numChos;
												break;
												default :
												out1.print("\n"+"not a resource, try again");
												break;
											}
										}
										finished1 = false;
										int brick2 = 0;
										int lumber2 = 0;
										int wool2 = 0;
										int grain2 = 0;
										int ore2 = 0;
										finished1 = false;
										while(!finished1){
											out1.print("\n"+"pick a number of a specific resource to request, if you have finished choosing, enter -1");
											int numChos = getNextInt();
											if(numChos==-1){
												finished1 = true;
												break;		         
											}
											out1.print("\n"+"please enter the specific resource, from: brick, lumber, wool, grain, ore");
											String recChos = getNextLine();
											switch(recChos){
												case "brick":
												brick2 += numChos;
												break;
												case "lumber":
												lumber2  += numChos;
												break;
												case "wool":
												wool2 += numChos;
												break;
												case "grain":
												grain2 +=numChos;
												break;
												case "ore":
												ore2 +=numChos;
												break;
												default :
												out1.print("\n"+"not a resource, try again");
												break;
											}
										}
										out1.print("\n"+"finally, enter the number of the player you want to trade with:");
										int pNum = getNextInt();

										sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setInitiateTrade(intergroup.trade.Trade.Kind.newBuilder().setPlayer(WithPlayer.newBuilder().setOther(Player.newBuilder().setIdValue(pNum).build()).setWanting(Counts.newBuilder().setBrick(brick1).setGrain(grain1).setLumber(lumber1).setOre(ore1).setWool(wool1).build()).setOffering(Counts.newBuilder().setBrick(brick2).setGrain(grain2).setLumber(lumber2).setOre(ore2).setWool(wool2).build()).build()).build()).build()).build(),kkSocket);
										break;
										case("BANK"):
										finished1 = false;
										brick1 = 0;
										lumber1 = 0;
										wool1 = 0;
										grain1 = 0;
										ore1 = 0;
										while(!finished1){
											out1.print("\n"+"pick a number of a specific resource to ask for, if you have finished choosing, enter -1");
											int numChos = getNextInt();
											if(numChos==-1){
												finished1 = true;
												break;		         
											}
											out1.print("\n"+"please enter the specific resource, from: brick, lumber, wool, grain, ore");
											String recChos = getNextLine();
											switch(recChos){
												case "brick":
												brick1 += numChos;
												break;
												case "lumber":
												lumber1 += numChos;
												break;
												case "wool":
												wool1 += numChos;
												break;
												case "grain":
												grain1+=numChos;
												break;
												case "ore":
												ore1+=numChos;
												break;
												default :
												out1.print("\n"+"not a resource, try again");
												break;
											}
										}
										finished1 = false;
										brick2 = 0;
										lumber2 = 0;
										wool2 = 0;
										grain2 = 0;
										ore2 = 0;
										finished1 = false;
										while(!finished1){
											out1.print("\n"+"pick a number of a specific resource to request, if you have finished choosing, enter -1");
											int numChos = getNextInt();
											if(numChos==-1){
												finished1 = true;
												break;		         
											}
											out1.print("\n"+"please enter the specific resource, from: brick, lumber, wool, grain, ore");
											String recChos = getNextLine();
											switch(recChos){
												case "brick":
												brick2 += numChos;
												break;
												case "lumber":
												lumber2  += numChos;
												break;
												case "wool":
												wool2 += numChos;
												break;
												case "grain":
												grain2 +=numChos;
												break;
												case "ore":
												ore2 +=numChos;
												break;
												default :
												out1.print("\n"+"not a resource, try again");
												break;
											}
										}
										sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setInitiateTrade(intergroup.trade.Trade.Kind.newBuilder().setBank(WithBank.newBuilder().setWanting(Counts.newBuilder().setBrick(brick1).setGrain(grain1).setLumber(lumber1).setOre(ore1).setWool(wool1).build()).setOffering(Counts.newBuilder().setBrick(brick2).setGrain(grain2).setLumber(lumber2).setOre(ore2).setWool(wool2).build()).build()).build()).build()).build(),kkSocket);
										break;
										default:
										out1.print("\n"+"sorry that is not an option");

										break;
									}
									break;
									case "SUBMITTRADERESPONSE":
									out1.print("\n"+"do you ACCEPT?");
									String respo = getNextLine();
									if(respo.equals("ACCEPT")){
										sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setSubmitTradeResponse(Response.ACCEPT).build()).build(),kkSocket);
									}
									else{
										sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setSubmitTradeResponse(Response.REJECT).build()).build(),kkSocket);
									}
									break;
									case "DISCARDRESOURCES":
									int brick = 0;
									int lumber = 0;
									int wool = 0;
									int grain = 0;
									int ore = 0;
									boolean finished = false;
									while(!finished){
										out1.print("\n"+"pick a number of a specific resource to discard, if you have finished discarding, enter -1");
										int numChos = getNextInt();
										if(numChos==-1){
											finished = true;
											break;		         
										}
										out1.print("\n"+"please enter the specific resource, from: brick, lumber, wool, grain, ore");
										String recChos = getNextLine();
										switch(recChos){
											case "brick":
											brick += numChos;
											break;
											case "lumber":
											lumber += numChos;
											break;
											case "wool":
											wool += numChos;
											break;
											case "grain":
											grain+=numChos;
											break;
											case "ore":
											ore+=numChos;
											break;
											default :
											out1.print("\n"+"not a resource, try again");
											break;
										}
									}
									sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setDiscardResources(Counts.newBuilder().setBrick(brick).setGrain(grain).setLumber(lumber).setOre(ore).setWool(wool).build()).build()).build(),kkSocket);
									break;
									case "SUBMITTARGETPLAYER":
									out1.print("\n"+"please submit the int for the player you wish to select, e.g. player 2 > 2");
									int pNu = getNextInt();
									sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setSubmitTargetPlayer(intergroup.board.Board.Player.newBuilder().setIdValue(pNu)).build()).build(),kkSocket);
									break;

									case "CHOOSERESOURCE":		          
									out1.print("\n"+"please enter your chosen resource Kind: BRICK, LUMBER, WOOL, GRAIN, ORE, antthing else will count as 'generic");
									String chosen = getNextLine();
									switch(chosen){
										case "GENERIC" :
										sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setChooseResource(Kind.GENERIC).build()).build(),kkSocket);
										break;
										case "BRICK" :
										sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setChooseResource(Kind.BRICK).build()).build(),kkSocket);
										break;
										case "LUMBER" :		 
										sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setChooseResource(Kind.LUMBER).build()).build(),kkSocket);
										break;
										case "WOOL" :
										sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setChooseResource(Kind.WOOL).build()).build(),kkSocket);
										break;
										case "GRAIN" :
										sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setChooseResource(Kind.GRAIN).build()).build(),kkSocket);
										break;
										case "ORE":
										sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setChooseResource(Kind.ORE).build()).build(),kkSocket);

										break;
										default:
										sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setChooseResource(Kind.GENERIC).build()).build(),kkSocket);
									}
									break;		           
									default:
									out1.print("\n"+"Client: " + instruction2);
									sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setChatMessage(instruction2).build()).build(),kkSocket);
								}
							}
						}			          

					}
				}
				catch (UnknownHostException e) {
					System.err.println("Don't know about host " + hostName);
					System.exit(1);
				}
				catch (IOException e) {
					System.err.println("Couldn't get I/O for the connection to " +
						hostName);
					System.exit(1);
				}
			}
			else {
				//SERVER SIDE
				int clientsToFind;
				ArrayList<String> unsernames = new ArrayList<String>();
				clientsToFind = Setup.requestClients(new Scanner(System.in));
				ArrayList<PlayerSocket> SocketArray = new ArrayList<PlayerSocket>();

				if (clientsToFind != 0) {

					InetAddress iAddress = InetAddress.getLocalHost();
					String currentIp = iAddress.getHostAddress();

					out1.print("\n"+"Your ip address is : " + currentIp + ". The other players should connect now");

					int portNumber = defaultPortNumber;
					ServerSocket serverSocket = new ServerSocket(portNumber);

					for (int j = 0; j < clientsToFind; j++) {

						Socket clientSocket = serverSocket.accept();
						PlayerSocket foundConnect = new PlayerSocket(clientSocket);
						SocketArray.add(foundConnect);
						System.out.println("!!!!");

						boolean success = false;
						while (!success) {
							try {
								
								Message enter = Catan.getPBMsg(clientSocket);

								if (enter.getRequest().getBodyCase().getNumber() == 14) {
									success = true;
									unsernames.add(enter.getRequest().getJoinLobby().getUsername());
									for(PlayerSocket sok : SocketArray){
										Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setLobbyUpdate(Usernames.newBuilder().addAllUsername(unsernames).build()).build()).build(), sok.getClientSocket());
									}
								}
								else{
									Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not a lobby join request").build()).build()).build(),clientSocket);
								}
							} 
							catch (IOException e) {
							}
						}

						out1.print("\n"+"Player connected!");
					}

					out1.print("\n"+"All players connected!");
				}
//---------------------------------------------------------
//STANDARD GAME CODE STARTS HERE
				//scanner = new Scanner(new File("./src/test.txt"));
				//sets up board
				board1 = Setup.getMeABoard();
				Game game1 = new Game();
				game1.setBoard(board1);
				//sets up development cards
				ArrayList<DevelopmentCard> developmentCards = Setup
				.getDevCardDeck();
				game1.setDevelopmentCards(developmentCards);

				//sets up resource cards
				ArrayList<ResourceCard> ore = Setup.getResourceCardDeck("ore");
				ArrayList<ResourceCard> grain = Setup.getResourceCardDeck("grain");
				ArrayList<ResourceCard> lumber = Setup.getResourceCardDeck("lumber");
				ArrayList<ResourceCard> wool = Setup.getResourceCardDeck("wool");
				ArrayList<ResourceCard> brick = Setup.getResourceCardDeck("brick");

				game1.setOre(ore);
				game1.setGrain(grain);
				game1.setLumber(lumber);
				game1.setWool(wool);
				game1.setBrick(brick);

				//sets up players, ALSO USES THE NETWORKING'S SOCKET ARRAY!
				ArrayList<projectX.Player> players = Setup.setPlayers(new Scanner(System.in),SocketArray,unsernames);
				game1.setPlayers(players);

				//roll dice for each player
				//changes player order with largest dice roll first
				Setup.getPlayerOrder(game1, new Scanner(System.in));
				Map.printMap(game1.getBoard(), game1.getPlayers());
				SendGameStars(game1);
				
				
				//place roads and settlements
				Setup.setInitialRoadsAndSettlements(game1, new Scanner(System.in));

				//pass from automated set up to actually playing the game
				out1.print("\n"+"-----now in manual mode-------");
				Scanner scanner = new Scanner(System.in);

				boolean hasEnded = !END_GAME;

				// will keep letting players take turns until someone wins
				while (!hasEnded) {
					for (int i = 0; i < game1.getPlayers().size(); i++) {

						// lets the player have a turn
						hasEnded = Turn.newTurn(game1.getPlayers().get(i), game1,
							scanner);

						// if a player has won then no other player takes their turn
						if (hasEnded) {
							break;
						}
					}
				}

				keepPlaying = playAgain(scanner, game1);
			}
		}

		out1.print("\n"+"Goodbye!");
		
				}
				catch(Exception e){
					
				}
		    		
		    		
		    	}
		}
		);
		thread2.run();
		
		
	}

	private static void SendGameStars(Game game1) {
		
		ArrayList<Harbour> habs = new ArrayList<Harbour>();
		ArrayList<Hex> hexes = new ArrayList<Hex>();
		ArrayList<PlayerSetting> plas = new ArrayList<PlayerSetting>();
		ArrayList<projectX.Hex> boardHexes = game1.getBoard().getHexes();
		ArrayList<Port> boadPorts = game1.getBoard().getPorts();
		ArrayList<projectX.Player> boardPlayers = game1.getPlayers();
		for(projectX.Hex h:boardHexes){
			int terrNum = 0;
			switch(h.getTerrain()){
				case "P":
				terrNum = 1;
				break;
				case "F":
				terrNum = 4;
				break;
				case "M":
				terrNum = 2;
				break;
				case "H":
				terrNum = 0;
				break;
				case "G":
				terrNum = 3;
				break;
			}
			intergroup.board.Board.Hex.Builder proHex = Hex.newBuilder();
			proHex.setLocation(Point.newBuilder().setX(h.getCoordinate().getX()).setY(h.getCoordinate().getY()).build());
			proHex.setNumberToken(h.getNumber());
			proHex.setTerrainValue(terrNum);
			hexes.add(proHex.build());
		}
		
		for(Port p : boadPorts){
			intergroup.board.Board.Harbour.Builder proHab = Harbour.newBuilder();
			Coordinate[] loc  = swapToProtoOrder(new Coordinate[]{p.getCoordinateA(),p.getCoordinateB()});
			proHab.setLocation(Edge.newBuilder().setA(Point.newBuilder().setX(loc[0].getX()).setY(loc[0].getY())).setB(Point.newBuilder().setX(loc[1].getX()).setY(loc[1].getY()).build()).build());
			int recNo = 0;
			switch(p.getResource()){
				case "?":
				break;
				case "H":
				recNo = 1;
				break;
				case "F":
				recNo = 2;
				break;
				case "P":
				recNo = 3;
				break;
				case "G":
				recNo = 4;
				break;
				case "M":
				recNo = 5;
				break;
				
			}
			proHab.setResourceValue(recNo);
			habs.add(proHab.build());
		}
		for(projectX.Player play : boardPlayers){
			int colSet = 0;
			switch(play.getName()){
				case "W":
				colSet = 3;
				break;
				case "R":
				colSet = 0;
				break;
				case "G":
				colSet = 4;
				break;
				case "B":
				colSet = 1;
				break;
				case "O":
				colSet = 2;
				break;
				case "Y":
				colSet = 5;
				break;
			}
			
			intergroup.lobby.Lobby.GameSetup.PlayerSetting.Builder proSet = PlayerSetting.newBuilder().setUsername(play.getUserName()).setPlayer(Player.newBuilder().setIdValue(play.getID()).build());
			proSet.setColourValue(colSet);
			plas.add(proSet.build());
		}
		
		for(projectX.Player p: game1.getPlayers()){
			Builder set = GameSetup.newBuilder().addAllHarbours(habs).addAllHexes(hexes).addAllPlayerSettings(plas);
			set.setOwnPlayer(Player.newBuilder().setIdValue(p.getID()).build());
			GameSetup setup = set.build();
			Message bigGameSetup = Message.newBuilder().setEvent(Event.newBuilder().setBeginGame(setup).build()).build();
			printToClient(bigGameSetup,p);
		}
		
	}

	//asks the players if they want to play again
	public static boolean playAgain(Scanner scanner, Game game1) {

		printToClient("Do you want to play again? Y/N", game1.getPlayers().get(0));

		boolean keepPlaying = false;
		String choice = getInputFromClient(game1.getPlayers().get(0), scanner).toUpperCase();
		char c = choice.toCharArray()[0];

		switch (c) {
			case 'Y':
			keepPlaying = true;
			break;
			case 'N':
			keepPlaying = false;
			break;
			default:
			Catan.printToClient("Invalid choice. Please choose again", game1.getPlayers().get(0));
			keepPlaying = playAgain(scanner, game1);
		}

		return keepPlaying;
	}
	
	//sends a protobuf message through a socket, this could be an event, a request etc...
	public static void sendPBMsg(Message mssg, Socket sock) throws IOException {

	 	mssg.writeDelimitedTo(sock.getOutputStream());
	 	
	 }

	//this sends a simple request message for when we have sections using our old text based input
	 public static void requestGenericPBMsg(Socket sock) throws IOException {

	 	sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setChatMessage("").build()).build(),sock);
	 }

	//this receives a protobuff method, the opposite of the send PBMsg method
	 public static Message getPBMsg( Socket sock) throws IOException {

    	Message m1 = Message.parseDelimitedFrom(sock.getInputStream());
    	return m1;
    }

	//prints a message to the specified player
    public static void printToClient(String message, projectX.Player player)  {

    	Message m = Message.newBuilder().setEvent(Event.newBuilder().setChatMessage(message).build()).build();
    	PlayerSocket socket = player.getpSocket();

    	if (socket != null) {
    		try {
    			sendPBMsg(m,socket.getClientSocket());
    		} 
    		catch (IOException e) {

    			e.printStackTrace();
    		}
    	}
    	else {
    		System.out.println(message);
    	}
    }

	//sends a protobuf to the specified player
    public static void printToClient(Message message, projectX.Player player)  {


    	PlayerSocket socket = player.getpSocket();

    	if (socket != null) {
    		try {
    			sendPBMsg(message,socket.getClientSocket());
    		} 
    		catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	else {
    		System.out.println(message.toString());
    	}
    }

	//gets input from a specified player in the form of a string
    public static String getInputFromClient(projectX.Player player, Scanner scanner)  {

    	PlayerSocket socket = player.getpSocket();

    	if (socket != null) {

    		Message m3 = null;

    		try {
    			requestGenericPBMsg(socket.getClientSocket());
    			m3 = getPBMsg(socket.getClientSocket());
    		} 
    		catch (IOException e) {
    			e.printStackTrace();
    		}

    		return m3.getEvent().getChatMessage();		
    	}
    	else {

    		return scanner.next();
    	}
    }

    public static Message getSpecificRequestMssg(int protoCaseNum,projectX.Player player){
    	Message enter= Message.getDefaultInstance();
    	boolean success = false;
    	while(!success){
    		try {
    			enter = Catan.getPBMsg(player.getpSocket().getClientSocket());
    			if(enter.getRequest().getBodyCase().getNumber()==protoCaseNum){
    				success=true;
    			}
    			else{
    				Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setError(Error.newBuilder().setDescription("not the correct request for this time").build()).build()).build(), player.getpSocket().getClientSocket());
    			}
    		} catch (IOException e) {
    		}
    	}
    	return enter;
    }
}
