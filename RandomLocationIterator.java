import java.util.ArrayList;
import java.util.Collections;


public class RandomLocationIterator {
	int Xindex =0;
	
	int Yindex = 0;
	int XLen = 0;
	int YLen = 0;
	ArrayList<ArrayList<Location>> shuffledAL= new ArrayList<ArrayList<Location>>();

	public RandomLocationIterator(Board board1) {
		super();
		ArrayList<ArrayList<Location>> shuffledAL= new ArrayList<ArrayList<Location>>();
		Location[][] boardClone = board1.getBoardLocations().clone();
		ArrayList<Location[]> boardCloneX = new ArrayList<Location[]>();
		
		for(int g = 0; g < boardClone.length;g++){
			boardCloneX.add(boardClone[g]);
		}
		
		Collections.shuffle(boardCloneX);
		for(int u = 0;u<boardCloneX.size();u++)
		{
			ArrayList<Location> CloneY = new ArrayList<Location>();
			for(int j = 0; j< boardCloneX.get(u).length;j++){
				CloneY.add(boardCloneX.get(u)[j]);
			}
			Collections.shuffle(CloneY);
			shuffledAL.add(CloneY);

		}
		this.shuffledAL=shuffledAL;
		this.XLen = shuffledAL.size();
		this.YLen = shuffledAL.get(0).size();
	}
	
	public Location getNext(){
		Location nextLoc = shuffledAL.get(Xindex).get(Yindex);
		//finish makinng here
		
		Yindex++;
		if(Yindex == YLen){
			Yindex = 0;
			Xindex++;
		}
		return nextLoc;
	}
	
	public Location getNextHex(){
		Location nextLoc = shuffledAL.get(Xindex).get(Yindex);
		boolean found = false;
		if(nextLoc.getType().equals("hex")){
			found = true;
		}
		
		//finish making here
		Yindex++;
		if(Yindex == YLen){
			Yindex = 0;
			Xindex++;
		}
		if(found){
		return nextLoc;
		}
		else if(!hasNext()){
			return null;
		}
		else{
			return getNextHex();
		}
	}
	
	
	
	public boolean hasNext(){
		if(Xindex<XLen||Yindex<YLen){
			return true;
		}
		else{
			return false;
		}
	}
	

}
