package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;

import intergroup.Events.Event;
import intergroup.Messages.Message;

/**
 * Class that allows the game to communicate with clients
 */
public class PlayerSocket {
	
	Socket clientSocket;
	PrintWriter out;
	BufferedReader in;

//-----Constructors-----//	
	
	public PlayerSocket() {
		
	}
	
    public PlayerSocket(Socket ClientSocket) throws IOException {
    	
    	this.clientSocket = ClientSocket;
    	this.out = new PrintWriter(clientSocket.getOutputStream(), true);
    	this.in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
    }
    
//-----Getters and Setters-----//	
    
	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	} 
    
//-----Communication Methods-----//   
    
    public void sendMessage(String msg) {
    	
    	try {
			Catan.sendPBMsg(Message.newBuilder().setEvent(Event.newBuilder().setChatMessage(msg).build()).build(), clientSocket);
		} 
    	catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void requestMessage() throws IOException{
    	Catan.requestGenericPBMsg(clientSocket);
    }
    
    public String getMessage() throws IOException {
    	
    	Message m1 = Catan.getPBMsg(clientSocket);
    	return m1.getEvent().getChatMessage();
    }
      
    //TODO will this not need to be changed?
    public String Communicate(String Message) throws IOException{
    
    	String inputLine;
    	String  outputLine = Message;
    	out.println(outputLine);
    	inputLine = in.readLine();
    	   
    	return inputLine;
    }   
}