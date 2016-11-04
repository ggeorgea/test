
public class DevelopmentCard {

	private int type;
	private boolean hidden;
	public DevelopmentCard(){}
	public DevelopmentCard(int type, boolean hidden) {
		super();
		this.type = type;
		this.hidden = hidden;
	}

	public int getType() {
	
		return type;
	}
	
	public void setType(int type) {
	
		this.type = type;
	}
	
	public boolean isHidden() {
	
		return hidden;
	}
	
	public void setHidden(boolean hidden) {
	
		this.hidden = hidden;
	}	
}
