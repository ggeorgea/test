package game;

import java.util.ArrayList;

import intergroup.Events.Event;
import intergroup.Messages.Message;
import intergroup.board.Board;
import intergroup.lobby.Lobby.GameWon;

/**
 * Class to store the state of the Catan game
 */
public class Game {

	private String id;
	private game.Board board;

	private ArrayList<ResourceCard> ore = new ArrayList<ResourceCard>();
	private ArrayList<ResourceCard> grain = new ArrayList<ResourceCard>();
	private ArrayList<ResourceCard> lumber = new ArrayList<ResourceCard>();
	private ArrayList<ResourceCard> wool = new ArrayList<ResourceCard>();
	private ArrayList<ResourceCard> brick = new ArrayList<ResourceCard>();

	private ArrayList<DevelopmentCard> developmentCards = new ArrayList<DevelopmentCard>();
	private ArrayList<Player> players = new ArrayList<Player>();
	
	private static final boolean END_GAME = true;

//-----Constructors-----//
	public Game() {

	}
	
	public Game(String id, game.Board board, ArrayList<ResourceCard> ore, ArrayList<ResourceCard> grain,
		ArrayList<ResourceCard> lumber, ArrayList<ResourceCard> wool, ArrayList<ResourceCard> brick,
		ArrayList<DevelopmentCard> developmentCards, ArrayList<Player> players) {

		this.id = id;
		this.board = board;
		this.ore = ore;
		this.grain = grain;
		this.lumber = lumber;
		this.wool = wool;
		this.brick = brick;
		this.developmentCards = developmentCards;
		this.players = players;
	}

//-----Getters and Setters-----//
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public game.Board getBoard() {
		return board;
	}

	public void setBoard(game.Board board) {
		this.board = board;
	}

	public ArrayList<ResourceCard> getOre() {
		return ore;
	}

	public void setOre(ArrayList<ResourceCard> ore) {
		this.ore = ore;
	}

	public ArrayList<ResourceCard> getGrain() {
		return grain;
	}

	public void setGrain(ArrayList<ResourceCard> grain) {
		this.grain = grain;
	}

	public ArrayList<ResourceCard> getLumber() {
		return lumber;
	}

	public void setLumber(ArrayList<ResourceCard> lumber) {
		this.lumber = lumber;
	}

	public ArrayList<ResourceCard> getWool() {
		return wool;
	}

	public void setWool(ArrayList<ResourceCard> wool) {
		this.wool = wool;
	}

	public ArrayList<ResourceCard> getBrick() {
		return brick;
	}

	public void setBrick(ArrayList<ResourceCard> brick) {
		this.brick = brick;
	}

	public ArrayList<DevelopmentCard> getDevelopmentCards() {
		return developmentCards;
	}

	public void setDevelopmentCards(ArrayList<DevelopmentCard> developmentCards) {
		this.developmentCards = developmentCards;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
//-----Methods to check if the game has ended-----//
	
	//checks if the game has ended
	public static boolean checkEndOfGame(Player player, Game game1) {
		
		if (player.getVictoryPoints() >= 10) {
			
			endGame(player, game1);
			return END_GAME;
		}
		
		return !END_GAME;
	}
	
	//prints a statement ending the game
	public static void endGame(Player player, Game game1) {
		
		ArrayList<Player> players = game1.getPlayers();
		
		int playerNum = 0;
		
		for (int i = 0; i < players.size(); i++) {
			if(players.get(i).equals(player)) {
				playerNum = i;
			}
		}
		
		Message m = Message.newBuilder().setEvent(Event.newBuilder().setGameWon(GameWon.newBuilder().setWinner(Board.Player.newBuilder().setIdValue(playerNum).build()).build()).build()).build();

		for (int i = 0; i < players.size(); i++) {
			Catan.printToClient(m, players.get(i));
		}
	}
}
