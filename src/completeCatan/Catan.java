package completeCatan;
 
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
 
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
 
 /**
  * Class that contains the main method for the game
  */
 public class Catan {
 
 	private static final boolean END_GAME = true;
 	
 	private static final String ORE = "ore";
 	private static final String GRAIN = "grain";
 	private static final String LUMBER = "lumber";
 	private static final String WOOL = "wool";
 	private static final String BRICK = "brick";
 	
 	private static final String YES = "y";
 	private static final char YES_CHAR = 'Y';
 	private static final char NO_CHAR = 'N';
 
 	public static void main(String[] args) throws Exception {
 
 		Board board1 = new Board();
  
 		boolean keepPlaying = true;
 
 		// will let the players play another game if they wish
 		while (keepPlaying) {
 
 			System.out.println("----------SETTLERS OF CATAN----------\n\n");
 			Scanner scanner = new Scanner(System.in);
 
 			ArrayList<PlayerSocket> socketArray = connectClients(scanner);
 			
 			//sets up board
 			board1 = Setup.getMeABoard();
 			Game game1 = new Game();
 			game1.setBoard(board1);
 
 			//sets up development cards
 			ArrayList<DevelopmentCard> developmentCards = Setup.getDevCardDeck();
 			game1.setDevelopmentCards(developmentCards);
 
 			//sets up resource cards
 			ArrayList<ResourceCard> ore = Setup.getResourceCardDeck(ORE);
 			ArrayList<ResourceCard> grain = Setup.getResourceCardDeck(GRAIN);
 			ArrayList<ResourceCard> lumber = Setup.getResourceCardDeck(LUMBER);
 			ArrayList<ResourceCard> wool = Setup.getResourceCardDeck(WOOL);
 			ArrayList<ResourceCard> brick = Setup.getResourceCardDeck(BRICK);
 
 			game1.setOre(ore);
 			game1.setGrain(grain);
 			game1.setLumber(lumber);
 			game1.setWool(wool);
 			game1.setBrick(brick);
 
 			//sets up players
 			ArrayList<Player> players = Setup.setPlayers(scanner, socketArray);
 			game1.setPlayers(players);
 
 			//roll dice for each player
 			//changes player order with largest dice roll first
 			Setup.getPlayerOrder(game1, scanner);
 
 			Map.printMap(game1.getBoard(), players);
 			
 			//place roads and settlements
 			Setup.setInitialRoadsAndSettlements(game1, scanner);
 
 			//pass from automated set up to actually playing the game
 			scanner = new Scanner(System.in);
 
 			boolean hasEnded = !END_GAME;
 
 			//will keep letting players take turns until someone wins
 			while (!hasEnded) {
 				for (int i = 0; i < game1.getPlayers().size(); i++) {
 						
 					//lets the player have a turn
 					hasEnded = Turn.newTurn(game1.getPlayers().get(i), game1, scanner);
 
 						//if a player has won then no other player takes their turn
 						if (hasEnded) {
 							break;
 						}
 					}
 				}
 
 				keepPlaying = playAgain(scanner, game1);
 			}
 
 		System.out.println("Goodbye!");
 	}
 	
 	//asks the players if they want to play again
	public static boolean playAgain(Scanner scanner, Game game1) throws IOException {
 
		Catan.printToClient("Do you want to play again? Y/N", game1.getPlayers().get(0));
 
 		boolean keepPlaying = false;
 		String choice = getInputFromClient(game1.getPlayers().get(0), scanner).toUpperCase();
 		char c = choice.toCharArray()[0];
 
 		switch (c) {
 		case YES_CHAR:
 			keepPlaying = true;
 			break;
 		case NO_CHAR:
 			keepPlaying = false;
 			break;
 		default:
			Catan.printToClient("Invalid choice. Please choose again", game1.getPlayers().get(0));
			keepPlaying = playAgain(scanner, game1);
 		}
 
 		return keepPlaying;
 	}
 
 	//allows messages to be sent to the correct player
 	public static void printToClient(String message, Player player) {
 
 		PlayerSocket socket = player.getpSocket();
 
 		if (socket != null) {
 			socket.sendMessage(message);
 		}
 		else {
 			System.out.println(message);
 		}
 	}
 
 	//allows messages to be received from the correct player
 	public static String getInputFromClient(Player player, Scanner scanner) throws IOException {
 
 		PlayerSocket socket = player.getpSocket();
 
 		if (socket != null) {
 			return socket.getMessage();
 		}
 		else {
 			return scanner.next();
 		}
 	}
 	
 	//allows the server to host the game, and the clients to connect
 	public static ArrayList<PlayerSocket> connectClients(Scanner scanner) throws IOException {
 		
 		ArrayList<PlayerSocket> socketArray = null;

		System.out.println("activate client mode? Y/N");
		String answer = scanner.nextLine().toLowerCase();
		int defaultPortNumber = 6789;

		if (answer.equals(YES)) {

			System.out.println("Please enter the address where you wish to connect");
			String hostName = scanner.nextLine();
			int portNumber = defaultPortNumber;

		    try (Socket kkSocket = new Socket(hostName, portNumber);
		    		PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
		            BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()))) {

		        String fromServer;
		        String fromUser;

		        while (true) {
		            	
		        	if (in.ready()) {
		            		
		         		fromServer = in.readLine();
			           	System.out.println(fromServer);
	
			            if (fromServer.equals("Goodbye!")) {
			                break;
			            }
		               
		          	}
		            if (System.in.available()!= 0) {
		                	
			            fromUser = scanner.nextLine();
			                
			            if (fromUser != null) {
			                 out.println(fromUser);
			            }
		            }
		        }
		    }
		    catch (UnknownHostException e) {
		        System.err.println("Don't know about host " + hostName);
		        System.exit(1);
		    }
		    catch (IOException e) {
		        System.err.println("Couldn't get I/O for the connection to " + hostName);
		        System.exit(1);
		    }
		}
		else {

			int clientsToFind;

			clientsToFind = Setup.requestClients(scanner);
			socketArray = new ArrayList<PlayerSocket>();

			if (clientsToFind != 0) {

		    	InetAddress iAddress = InetAddress.getLocalHost();
		    	String currentIp = iAddress.getHostAddress();

				System.out.println("Your ip address is : " + currentIp + ". The other players should connect now");

				int portNumber = defaultPortNumber;
	            ServerSocket serverSocket = new ServerSocket(portNumber);

	            for (int j = 0; j < clientsToFind; j++) {

	            	Socket clientSocket = serverSocket.accept();
					PlayerSocket foundConnect = new PlayerSocket(clientSocket);
					socketArray.add(foundConnect);
					System.out.println("Player connected!");
				}

				System.out.println("All players connected!");
			}
		}
					
		return socketArray;
 	}
}