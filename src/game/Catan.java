package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
import intergroup.board.Board.PlayableDevCard;
import intergroup.board.Board.Player;
import intergroup.board.Board.Point;
import intergroup.resource.Resource.Counts;
import intergroup.resource.Resource.Kind;
import intergroup.trade.Trade.Response;
import intergroup.trade.Trade.WithBank;
import intergroup.trade.Trade.WithPlayer;

/**
 * Class that contains the main method for the game
 */
public class Catan {

	private static Coordinate[] swapToProtoOrder(Coordinate[] coords){
		if((coords[0].getX()+coords[0].getY())>(coords[1].getX()+coords[1].getY())){
			Coordinate keepCoord = coords[0];
			coords[0]  = coords[1];
			coords[1] = keepCoord;
		}
		return coords;
	}
	
	private static final boolean END_GAME = true;

	public static void main(String[] args) throws Exception {

	
		
		Board board1 = new Board();

		/*
		 * //GENERAL //the above methods set up a board, with roads,
		 * intersections and hexes, all set up in the right places, with random
		 * terrains and probabilities //the ways these items on the board should
		 * be interacted with are as follows: //(there is a 2d array that can be
		 * interacted with directly, but the x and y coordinates are plus five
		 * each, so i suggest we stick to these to maintain some modularity)
		 * //(and the road hashmap called roadmap needs to be accessed with the
		 * coordinates in a specific order, the method I suggest here just
		 * adjusts input so it is in the correct order)
		 *
		 * //ROADS //firstly, for roads, use the getRoadFromCo method //this
		 * methods takes two coordinates and returns a road or null that links
		 * those coordinates, null if there is no road //that road can be
		 * modified by modifying the object returned of course //(if you wish to
		 * replace the road with another, using the setRoadFromCo method, you
		 * should iterate through the ArrayList and replace the old road there
		 * too!) //for example: Player player1 = new Player();
		 * player1.setName("!"); Road road1 = board1.getRoadFromCo(new
		 * Coordinate(1,0),new Coordinate(1,1)); road1.setOwner(player1); //as
		 * you can now see, the road on the right of the central hex, now is
		 * owned by the player whose name is "!"
		 *
		 * //INTERSECTIONS //for intersections, use the
		 * getLocationFromCoordinate method, that takes a single coordinate
		 * //this is a little more tricky as the location class that this
		 * returns actually has three things in it, a type, that should say
		 * "Intersection", if your coordinates are correct //and the thing that
		 * you want which is the contains field, so cast the contains field of
		 * the returned object to an intersection and modify that //for example
		 * Intersection inter1 = (Intersection)
		 * board1.getLocationFromCoordinate(new Coordinate(1,0)).getContains();
		 * inter1.setOwner(player1); //as you can see, the intersection in the
		 * bottom right of the central hex is now owned by player1
		 *
		 * //HEXES //to get the hex at a point on the board you do pretty much
		 * the same thing as an intersection //the type of location should be
		 * "hex", if there is nothing in that coordinate, it should be "empty"
		 * by the way //for example Hex hex1 = (Hex)
		 * board1.getLocationFromCoordinate(new Coordinate(0,0)).getContains();
		 * hex1.setisRobberHere("R"); //it should now appear that there is also
		 * a robber on the central hex! //of course if I wasnt being lazy there
		 * would be seperate methods for hexes and intersections that did the
		 * casting for you or something, but im not sure thats even better
		 */

		boolean keepPlaying = true;

		// will let the players play another game if they wish
		while (keepPlaying) {

			System.out.println("----------SETTLERS OF CATAN----------\n\n");
			Scanner scanner = new Scanner(System.in);


//NETWORKING CODE
//------------------------------------------------------
			System.out.println("activate client mode? (y)");
			String answer = scanner.nextLine();
			int defaultPortNumber = 6789;

			if (answer.equals("y")) {
				
				//CLIENT SIDE
				System.out.println("please enter the address where you wish to connect\nPlease note that to enter a command, enter \"c\"");
				String hostName = scanner.nextLine();
				int portNumber = defaultPortNumber;
				
		        try {
		        	
		        	Client client = new Client();
	        		Socket kkSocket = new Socket(hostName, portNumber);	  
	        		//TODO implement lobby join request
//        			//join
//        			case "JOINLOBBY":
//        				break;
	        		
	        		String fromServer;
		            String fromUser;
	     
		            
		            
		            while(true){
		            	if(kkSocket.getInputStream().available()!=0){
			            Message m1 = getPBMsg(kkSocket);		
			         //  System.out.println(Event.getDescriptor().getFullName()+", "+m1.hasField( m1.getDescriptorForType().findFieldByName("ChatMessage")));

			            
				            if(m1.getTypeCase().name().equals("EVENT")){
					          if(m1.getEvent().getTypeCase().name().equals("CHATMESSAGE")){
					            	//DEALING WITH EVENTS
					            	//TODO move to a seperate class/method so all kinds of events/requests can be dealt with correctly
					            	fromServer = m1.getEvent().getChatMessage();				            	
					              	System.out.println(fromServer);		
					                if (fromServer.equals("Goodbye!")) {
					                    break;
					                }		
				            	}
					          else{
					        	  client.resolveEvent(m1.getEvent(),kkSocket);
					          }
				            }			    
			            // CLIENTS DONT RECEIVE REQUESTS :(
//				            else if(m1.getTypeCase().name().equals("REQUEST")){
//			            	//DEALING WITH REQUESTS
//			            	//TODO move to a seperate class/method so all kinds of events/requests can be dealt with correctly
//			            	if(m1.getRequest().getBodyCase().name().equals("CHATMESSAGE")){
//				            	fromUser = scanner.nextLine();
//				                System.out.println("Client: " + fromUser);
//			                	sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setChatMessage(fromUser).build()).build(),kkSocket);
//			            	}
//			            	else{
//			            		Client.resolveRequest(m1.getRequest(),kkSocket);
//			            	}
//			            	
//			            	
//			            	
//			            	}
				            else{
				            	System.out.println(":(");
				            }
		            }
		            	else if (System.in.available()!=0){
		            		String instruction = scanner.nextLine();
		            		if(instruction.equals("c")){
		            			System.out.println("creating a message: to see options type \"P\", to cancel, type \"E\"");
		            			String instruction2 = scanner.nextLine();
		            			if(instruction2.equals("p")){
		            				System.out.println( "ROLLDICE, BUYDEVCARD, BUILDROAD, BUILDSETTLEMENT, BUILDCITY, MOVEROBBER, PLAYDEVCARD, INITIATETRADE, \n"
		            					+ "SUBMITTRADERESPONSE, DISCARDRESOURCES, SUBMITTARGETPLAYER, CHOOSERESOURCE, ENDTURN\nanything else will be sent as a chat message!");
		            					instruction2 = scanner.nextLine();
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
		            				System.out.println("Please select where to build a road");
		            				System.out.println("road start: Select X coordinate");
		            				int x1 = scanner.nextInt();
		            				System.out.println("road start: Select Y coordinate");
		            				int y1 = scanner.nextInt();
		            				System.out.println("road end: Select X coordinate");
		            				int x2 = scanner.nextInt();
		            				System.out.println("road end: Select Y coordinate");
		            				int y2 = scanner.nextInt();
		            				sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setBuildRoad(Edge.newBuilder().setA(Point.newBuilder().setX(x1).setY(y1).build()).setB(Point.newBuilder().setX(x2).setY(y2).build()).build()).build()).build(),kkSocket);          						            				
		            				break;		            		
		            			case "BUILDSETTLEMENT":
		            				System.out.println("Please select where to build a settlement");
		            				System.out.println("Select X coordinate");
		            				int x = scanner.nextInt();
		            				System.out.println("Select Y coordinate");
		            				int y = scanner.nextInt();
		            				sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setBuildSettlement(Point.newBuilder().setX(x).setY(y).build()).build()).build(),kkSocket);          						            				
		            				break;
		            			case "BUILDCITY":
		             				System.out.println("Please select where to build a city");
		            				System.out.println("Select X coordinate");
		            				 x = scanner.nextInt();
		            				System.out.println("Select Y coordinate");
		            				 y = scanner.nextInt();
		            				sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setBuildCity(Point.newBuilder().setX(x).setY(y).build()).build()).build(),kkSocket);          						            				
		            				break;
		            			case "MOVEROBBER":
		             				System.out.println("Please select where to build a city");
		            				System.out.println("Select X coordinate");
		            				 x = scanner.nextInt();
		            				System.out.println("Select Y coordinate");
		            				 y = scanner.nextInt();
		            				sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setMoveRobber(Point.newBuilder().setX(x).setY(y).build()).build()).build(),kkSocket);  
		            				break;		            		
		            			case "PLAYDEVCARD":
		            				System.out.println("which type of dev card do you wish to try and play: ");
		            				boolean correct = false;
		            				while(!correct){
			            				String response = scanner.nextLine();
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
			            						System.out.println("not a type of dev card, try again");
			            				}
		            				}		
		            				break;
		            			case "INITIATETRADE":
		            				System.out.println("do you wish to trade with a PLAYER or with a BANK");
		            				String ans = scanner.nextLine();
		            				switch(ans){
		            				case ("PLAYER"):
		            					boolean finished1 = false;
		            					int brick1 = 0;
		            					int lumber1 = 0;
		            					int wool1 = 0;
		            					int grain1 = 0;
		            					int ore1 = 0;
 		            				  while(!finished1){
		            					  System.out.println("pick a number of a specific resource to ask for, if you have finished choosing, enter -1");
		            					  int numChos = scanner.nextInt();
		            					  if(numChos==-1){
		            						  finished1 = true;
		            						  break;		         
		            					  }
		            					  System.out.println("please enter the specific resource, from: brick, lumber, wool, grain, ore");
		            					  String recChos = scanner.nextLine();
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
		            						  System.out.println("not a resource, try again");
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
		            					  System.out.println("pick a number of a specific resource to request, if you have finished choosing, enter -1");
		            					  int numChos = scanner.nextInt();
		            					  if(numChos==-1){
		            						  finished1 = true;
		            						  break;		         
		            					  }
		            					  System.out.println("please enter the specific resource, from: brick, lumber, wool, grain, ore");
		            					  String recChos = scanner.nextLine();
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
		            						  System.out.println("not a resource, try again");
		            						  break;
		            					  }
 		            				  }
 		            				  System.out.println("finally, enter the number of the player you want to trade with:");
 		            				  int pNum = scanner.nextInt();
		            					
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
		            					  System.out.println("pick a number of a specific resource to ask for, if you have finished choosing, enter -1");
		            					  int numChos = scanner.nextInt();
		            					  if(numChos==-1){
		            						  finished1 = true;
		            						  break;		         
		            					  }
		            					  System.out.println("please enter the specific resource, from: brick, lumber, wool, grain, ore");
		            					  String recChos = scanner.nextLine();
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
		            						  System.out.println("not a resource, try again");
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
		            					  System.out.println("pick a number of a specific resource to request, if you have finished choosing, enter -1");
		            					  int numChos = scanner.nextInt();
		            					  if(numChos==-1){
		            						  finished1 = true;
		            						  break;		         
		            					  }
		            					  System.out.println("please enter the specific resource, from: brick, lumber, wool, grain, ore");
		            					  String recChos = scanner.nextLine();
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
		            						  System.out.println("not a resource, try again");
		            						  break;
		            					  }
 		            				  }
		            					sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setInitiateTrade(intergroup.trade.Trade.Kind.newBuilder().setBank(WithBank.newBuilder().setWanting(Counts.newBuilder().setBrick(brick1).setGrain(grain1).setLumber(lumber1).setOre(ore1).setWool(wool1).build()).setOffering(Counts.newBuilder().setBrick(brick2).setGrain(grain2).setLumber(lumber2).setOre(ore2).setWool(wool2).build()).build()).build()).build()).build(),kkSocket);
		            					break;
		            				default:
		            					System.out.println("sorry that is not an option");

		            					break;
		            				}
		            				
		            				break;
		            			case "SUBMITTRADERESPONSE":
		            				System.out.println("do you ACCEPT?");
		            				String respo = scanner.nextLine();
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
		            					  System.out.println("pick a number of a specific resource to discard, if you have finished discarding, enter -1");
		            					  int numChos = scanner.nextInt();
		            					  if(numChos==-1){
		            						  finished = true;
		            						  break;		         
		            					  }
		            					  System.out.println("please enter the specific resource, from: brick, lumber, wool, grain, ore");
		            					  String recChos = scanner.nextLine();
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
		            						  System.out.println("not a resource, try again");
		            						  break;
		            					  }
		            				  }
		            					sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setDiscardResources(Counts.newBuilder().setBrick(brick).setGrain(grain).setLumber(lumber).setOre(ore).setWool(wool).build()).build()).build(),kkSocket);
		            				break;
		            			case "SUBMITTARGETPLAYER":
		            				System.out.println("please submit the int for the player you wish to select, e.g. player 2 > 2");
		            				int pNu = scanner.nextInt();
	            					sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setSubmitTargetPlayer(intergroup.board.Board.Player.newBuilder().setIdValue(pNu)).build()).build(),kkSocket);
		            				break;
		            			
		            			case "CHOOSERESOURCE":		          
		            				System.out.println("please enter your chosen resource Kind: BRICK, LUMBER, WOOL, GRAIN, ORE, antthing else will count as 'generic");
		            				String chosen = scanner .nextLine();
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
					                System.out.println("Client: " + instruction2);
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

				clientsToFind = Setup.requestClients(scanner);
				ArrayList<PlayerSocket> SocketArray = new ArrayList<PlayerSocket>();

				if (clientsToFind != 0) {

			    	InetAddress iAddress = InetAddress.getLocalHost();
			    	String currentIp = iAddress.getHostAddress();

					System.out.println("Your ip address is : " + currentIp + ". The other players should connect now");

					int portNumber = defaultPortNumber;
	                ServerSocket serverSocket = new ServerSocket(portNumber);

	                for (int j = 0; j < clientsToFind; j++) {

						 Socket clientSocket = serverSocket.accept();
						 PlayerSocket foundConnect = new PlayerSocket(clientSocket);
						 SocketArray.add(foundConnect);
						 System.out.println("Player connected!");
					}

					System.out.println("All players connected!");
				}

//---------------------------------------------------------
//STANDARD GAME CODE STARTS HERE
				//scanner = new Scanner(new File("./src/test.txt"));
				//sets up board
				board1 = Setup.getMeABoard();
				Game game1 = new Game();
				game1.setBoard(board1);

			//	Map.printMap(game1.getBoard(), new ArrayList<Player>());

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
				ArrayList<game.Player> players = Setup.setPlayers(scanner,SocketArray);
				game1.setPlayers(players);

				//roll dice for each player
				//changes player order with largest dice roll first
				Setup.getPlayerOrder(game1, scanner);

				Map.printMap(game1.getBoard(), game1.getPlayers());

				
				//place roads and settlements
				Setup.setInitialRoadsAndSettlements(game1, scanner);


				//pass from automated set up to actually playing the game
				
				System.out.println("-----now in manual mode-------");
				scanner = new Scanner(System.in);

				boolean hasEnded = !END_GAME;

				// will keep letting players take turns until someone wins
				while (!hasEnded) {
					for (int i = 0; i < game1.getPlayers().size(); i++) {

						//  for a test of longest road:
						/*
						if (i>0){
							System.out.println("-----now in manual mode-------");
							scanner = new Scanner(System.in);
						}
						*/

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

		System.out.println("Goodbye!");
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
		/*
		byte[] toOutBy = mssg.toByteArray();		
	    int toLen = toOutBy.length;
	    byte[] bytesStr = new byte[4];
	 	bytesStr = ByteBuffer.allocate(4).putInt(toLen).array();
	 	int test = ((bytesStr[0] & 0xff) << 24) | ((bytesStr[1] & 0xff) << 16) |
		          ((bytesStr[2] & 0xff) << 8)  | (bytesStr[3] & 0xff);	
	 	
	 	sock.getOutputStream().write(bytesStr);
	 	sock.getOutputStream().write(toOutBy);*/
	 	mssg.writeDelimitedTo(sock.getOutputStream());
	 	
	}
	
	//this sends a simple request message for when we have sections using our old text based input
	public static void requestGenericPBMsg(Socket sock) throws IOException {
		
		sendPBMsg(Message.newBuilder().setRequest(Request.newBuilder().setChatMessage("").build()).build(),sock);
	}
	
	//this receives a protobuff method, the opposite of the send PBMsg method
	public static Message getPBMsg( Socket sock) throws IOException {
		/*
		byte[] fromLen = new byte[4];
	 	sock.getInputStream().read(fromLen);
	 	int fromLenInt = ((fromLen[0] & 0xff) << 24) | ((fromLen[1] & 0xff) << 16) |
    	          ((fromLen[2] & 0xff) << 8)  | (fromLen[3] & 0xff); 
    	byte[] fromServerby = new byte[fromLenInt];
    	
    	sock.getInputStream().read(fromServerby);
    	Message m1 = Message.parseFrom(fromServerby);*/
    	 Message m1 = Message.parseDelimitedFrom(sock.getInputStream());
    	
    	return m1;
	}

	//prints a message to the specified player
	public static void printToClient(String message, game.Player player)  {
		
		Message m = Message.newBuilder().setEvent(Event.newBuilder().setChatMessage(message).build()).build();
		PlayerSocket socket = player.getpSocket();
		
		if (socket != null) {
			try {
				sendPBMsg(m,socket.getClientSocket());
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.println(message);
		}
	}
	
	//sends a protobuf to the specified player
	public static void printToClient(Message message, game.Player player)  {
		
		
		PlayerSocket socket = player.getpSocket();
		
		if (socket != null) {
			try {
				sendPBMsg(message,socket.getClientSocket());
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.println(message.toString());
		}
	}
	
	

	//gets input from a specified player in the form of a string
	public static String getInputFromClient(game.Player player, Scanner scanner)  {

		PlayerSocket socket = player.getpSocket();

		if (socket != null) {
			
			Message m3 = null;
			
			try {
				requestGenericPBMsg(socket.getClientSocket());
				m3 = getPBMsg(socket.getClientSocket());
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return m3.getEvent().getChatMessage();		
		}
		else {

			return scanner.next();
		}
	}
	
	public static Message getSpecificRequestMssg(int protoCaseNum,game.Player player){
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
