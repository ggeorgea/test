package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerSocket {
	Socket clientSocket;
	PrintWriter out;
	BufferedReader in;

        
       PlayerSocket(Socket ClientSocket) throws IOException{
    	    
    	     this.clientSocket = ClientSocket;
    		 this.out = new PrintWriter(clientSocket.getOutputStream(), true);
    		 this.in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
    	   }
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

