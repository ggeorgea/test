package game;

import java.net.Socket;

import intergroup.Events.Event;
import intergroup.Requests.Request;

public class Client {

	public static void resolveEvent(Event event, Socket mySocket) {
		switch (event.getTypeCase().name()) {
		case "CHATMESSAGE":
			// TODO this should not happen...
			break;
		case "ERROR":
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
			break;
		case "GAMEWON":
			break;
		case "BEGINGAME":
			break;
		case "LOBBYUPDATE":
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
