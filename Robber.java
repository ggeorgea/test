


public class Robber{

	//robber will be put on different coordinates based on where the player has placed it 
	private Coordinate  c = new Coordinate(); 
	// p will represent the current player that moved the robber
	//if the player is null then no one is responsible for moving the robber at the moment 
	private Player  p = new Player(); 


	public Robber(Coordinate c , Player p){ 

		this.c = c ; 
		this.p = p; 
	}

	public Player getCurrentPlayer(){
		return p;
	}

	public Coordinate getCurrentCoordinate(){ 
		return c; 
	}







}