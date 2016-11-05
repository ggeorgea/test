import java.util.ArrayList;

public class Game {

	private String id;
	private Board board;
	private ArrayList<ResourceCard> resourceCards = new ArrayList<ResourceCard>();
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
	
	public ArrayList<ResourceCard> getResourceCards() {
	
		return resourceCards;
	}
	
	public void setResourceCards(ArrayList<ResourceCard> resourceCards) {
	
		this.resourceCards = resourceCards;
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
