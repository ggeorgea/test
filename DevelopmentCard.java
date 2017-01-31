
/**
 * Class to store information about 
 * development cards
 */
public class DevelopmentCard {

	private String type;
	private boolean hidden;
	
//-----Constructors-----//
	
	public DevelopmentCard() {
		
	}
	
	public DevelopmentCard(String type, boolean hidden) {
	
		this.type = type;
		this.hidden = hidden;
	}

//-----Getters and Setters-----//
	
	public String getType() {
	
		return type;
	}
	
	public void setType(String type) {
	
		this.type = type;
	}
	
	public boolean isHidden() {
	
		return hidden;
	}
	
	public void setHidden(boolean hidden) {
	
		this.hidden = hidden;
	}	
}