import java.util.ArrayList;

public class Game {

	private String id;
	private Board board;
	
	private ArrayList<ResourceCard> ore = new ArrayList<ResourceCard>();
	private ArrayList<ResourceCard> grain = new ArrayList<ResourceCard>();
	private ArrayList<ResourceCard> lumber = new ArrayList<ResourceCard>();
	private ArrayList<ResourceCard> wool = new ArrayList<ResourceCard>();

	private ArrayList<DevelopmentCard> developmentCards = new ArrayList<DevelopmentCard>();
	private ArrayList<Player> players = new ArrayList<Player>();
		
	public Game() {
		
	}
	
	public Game(String id, Board board, ArrayList<ResourceCard> resourceCards,
			ArrayList<Player> players) {
		
		this.id = id;
		this.board = board;
		this.resourceCards = resourceCards;
		this.players = players;
	}
	
	public String getId() {
	
		return id;
	}
	
	public void setId(String id) {
	
		this.id = id;
	}
	
	public Board getBoard() {
	
		return board;
	}
	
	public void setBoard(Board board) {
	
		this.board = board;
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
}
