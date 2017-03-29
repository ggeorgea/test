package game;

import intergroup.Events.Event;
import intergroup.Messages.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerSocket {
	
	Socket clientSocket;
	PrintWriter out;
	BufferedReader in;

        
    public PlayerSocket(Socket ClientSocket) throws IOException {
    	
    	this.clientSocket = ClientSocket;
    	this.out = new PrintWriter(clientSocket.getOutputStream(), true);
    	this.in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
    }
    
    public void sendMessage(String msg){
    	try {
			Catan.sendPBMsg( Message.newBuilder().setEvent(Event.newBuilder().setChatMessage(msg).build()).build(), clientSocket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//    	String  outputLine = Message;
//    	out.println(outputLine);
    }
    
    public String getMessage() throws IOException{
    	Message m1 = Catan.getPBMsg(clientSocket);
    	return m1.getEvent().getChatMessage();
    	
//    	String inputLine;
//    	inputLine = in.readLine();
//    	
//   	return inputLine;
    }
      
    public String Communicate(String Message) throws IOException{
    
    	String inputLine;
    	String  outputLine = Message;
    	out.println(outputLine);
    	inputLine = in.readLine();
    	   
    	return inputLine;
    }

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
    
}