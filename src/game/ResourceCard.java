package game;

/**
 * Class to store information about resource cards
 */
public class ResourceCard {

	private String resource;
	
//-----Constructors-----//	
	public ResourceCard() {
		
	}
	
	public ResourceCard(String resource) {
		this.resource = resource;
	}
	
//-----Getters and Setters-----//
	
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}	
}