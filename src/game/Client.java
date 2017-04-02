package game;

import intergroup.Events.Event;
import intergroup.Requests.Request;

public class Client {

	public static void resolveEvent(Event event) {
		switch(event.getTypeCase().name()){
			case "CHATMESSAGE":
				break;
			default:
		}
		
	}

	public static void resolveRequest(Request request) {
		switch(request.getBodyCase().name()){
			case "CHATMESSAGE":
				break;
			default:
		}
		
	}

}
