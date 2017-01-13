


public class Robber{

	//robber will be put on different coordinates based on where the player has placed it 
	private Coordinate  c = new Coordinate(); 
	// p will represent the current player that moved the robber
	//if the player is null then no one is responsible for moving the robber at the moment 
	private Player  p = new Player(); 



	//new logic
	//player will roll dice and if they land on 7 robber will be activated or if someone plays knight card
	//robber will only perform actions if he is activated otherwise it cannot move 

	boolean activated = false; 

	public Robber(Coordinate c , Player p, Boolean activate){ 

		this.c = c ; 
		this.p = p; 
		this.activated = activate;
	}

	public Player getCurrentPlayer(){
		return p;
	}

	public Coordinate getCurrentCoordinate(){ 
		return c; 
	}

	public boolean getActivationStatus(){
		return activated;
	}

	public void setToActivated(){
		activated = true;
	}


}