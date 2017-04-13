package completeCatan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;

/**
 * Class that allows the game to communicate with clients
 */
public class PlayerSocket {
	
	Socket clientSocket;
	PrintWriter out;
	BufferedReader in;

	PlayerSocket() {
		
	}
	
    PlayerSocket(Socket ClientSocket) throws IOException{
    	    
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

    public void sendMessage(String Message){
    
    	String  outputLine = Message;
    	out.println(outputLine);
    }
    
    public String getMessage() throws IOException{
    
    	String inputLine;
    	inputLine = in.readLine();
    	return inputLine;
    }
    
    public String Communicate(String Message) throws IOException{
    
    	String inputLine;
    	String  outputLine = Message;
    	out.println(outputLine);
    	inputLine = in.readLine();
    	   
    	return inputLine;
    }
}
