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

import intergroup.Events.Event;
import intergroup.Messages.Message;

/**
 * Class that contains the main method for the game
 */
public class Catan {

	private static final boolean END_GAME = true;

	public static void main(String[] args) throws Exception {

		
		//protobuff demonstration
//		Message m = Message.newBuilder().setEvent(Event.newBuilder().setChatMessage("aaa").build()).build();
//		byte[] m2 = m.toByteArray();
//		Message m3 = Message.parseFrom(m2);
//		System.out.println(m3.getEvent().getChatMessage());
//		
//		String astr = "ABC";
//		byte[] b1 = astr.getBytes();
//		System.out.println(new String(b1));
//		
//		
		



		
		
		
		
		
		
		
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


//FUNNY NETWORKING CODE
//------------------------------------------------------
			System.out.println("activate client mode? (y)");
			String answer = scanner.nextLine();
			int defaultPortNumber = 6789;

			if (answer.equals("y")) {

				System.out.println("please enter the address where you wish to connect");
				String hostName = scanner.nextLine();
				int portNumber = defaultPortNumber;

		        try (
		        		Socket kkSocket = new Socket(hostName, portNumber);
//		                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
//		                BufferedReader in = new BufferedReader(
//		                    new InputStreamReader(kkSocket.getInputStream()));
		            ) {

		        	String fromServer;
		            String fromUser;

		          //  while ((fromServer = in.readLine()) != null) {
		            while(true){

		            	Message m1 = getPBMsg(kkSocket);
		            	fromServer = m1.getEvent().getChatMessage();
		            	
		              	System.out.println(fromServer);

		                if (fromServer.equals("Goodbye!")) {
		                    break;
		                }
		                
		              //  if (!(in.ready())) {
		               if ((kkSocket.getInputStream().available())==0) {

			                fromUser = scanner.nextLine();
			                if (fromUser != null) {
			        //           System.out.println("Client: " + fromUser);
			                   //  out.println(fromUser);
			                	sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setChatMessage(fromUser).build()).build(),kkSocket);
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

				Map.printMap(game1.getBoard(), new ArrayList<Player>());

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
				ArrayList<Player> players = Setup.setPlayers(scanner,SocketArray);
				game1.setPlayers(players);

				//roll dice for each player
				//changes player order with largest dice roll first
				Setup.getPlayerOrder(game1, scanner);

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

		Catan.printToClient("Do you want to play again? Y/N", game1.getPlayers().get(0));

		boolean keepPlaying = false;
		String choice = scanner.next().toUpperCase();
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
	
	public static void sendPBMsg(Message mssg, Socket sock) throws IOException{
		//Socket sock = player.getpSocket().getClientSocket();
	   
		
		byte[] toOutBy = mssg.toByteArray();
		
	    int toLen = toOutBy.length;
	    byte[] bytesStr = new byte[4];
	 	bytesStr = ByteBuffer.allocate(4).putInt(toLen).array();
	 	int test = ((bytesStr[0] & 0xff) << 24) | ((bytesStr[1] & 0xff) << 16) |
		          ((bytesStr[2] & 0xff) << 8)  | (bytesStr[3] & 0xff);	 	
	 	//System.out.println(toLen+", "+test);
	 	sock.getOutputStream().write(bytesStr);
	 	sock.getOutputStream().write(toOutBy);
	}
	
	public static Message getPBMsg( Socket sock) throws IOException{
		//Socket sock = player.getpSocket().getClientSocket();

		byte[] fromLen = new byte[4];
	 	sock.getInputStream().read(fromLen);
    	int fromLenInt = ((fromLen[0] & 0xff) << 24) | ((fromLen[1] & 0xff) << 16) |
    	          ((fromLen[2] & 0xff) << 8)  | (fromLen[3] & 0xff);   
    	//System.out.println(fromLenInt);
    	byte[] fromServerby = new byte[fromLenInt];
    	sock.getInputStream().read(fromServerby);
    	Message m1 = Message.parseFrom(fromServerby);  	
    	return m1;
	}

	
	
	public static void printToClient(String message, Player player)  {
		Message m = Message.newBuilder().setEvent(Event.newBuilder().setChatMessage(message).build()).build();

		PlayerSocket socket = player.getpSocket();

		if (socket != null) {

			try {
				sendPBMsg(m,socket.getClientSocket());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.println(message);
		}
	}

	//TODO can be used to replace scanner statements
	//allows messages to be recieved from the correct player
	public static String getInputFromClient(Player player, Scanner scanner)  {

		PlayerSocket socket = player.getpSocket();

		if (socket != null) {
			Message m3 = null;
			try {
				m3 = getPBMsg(socket.getClientSocket());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return m3.getEvent().getChatMessage();
		
		}
		else {

			return scanner.next();
		}
	}
	
	
	
	
		
	//TODO can be used to replace System.out.println statements
	//allows messages to be sent to the correct player
//	public static void printToClient(String message, Player player) {
//
//		PlayerSocket socket = player.getpSocket();
//
//		if (socket != null) {
//
//			socket.sendMessage(message);
//		}
//		else {
//			System.out.println(message);
//		}
//	}
//
//	//TODO can be used to replace scanner statements
//	//allows messages to be recieved from the correct player
//	public static String getInputFromClient(Player player, Scanner scanner) throws IOException {
//
//		PlayerSocket socket = player.getpSocket();
//
//		if (socket != null) {
//
//			return socket.getMessage();
//		}
//		else {
//
//			return scanner.next();
//		}
//	}
}
