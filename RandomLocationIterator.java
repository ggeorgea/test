import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Class to randomise locations
 */
public class RandomLocationIterator implements Iterator{
	
	int Xindex =0;
	int Yindex = 0;
	int XLen = 0;
	int YLen = 0;
	ArrayList<ArrayList<location>> shuffledAL= new ArrayList<ArrayList<location>>();

	public RandomLocationIterator(Board board1) {
		
		ArrayList<ArrayList<location>> shuffledAL= new ArrayList<ArrayList<location>>();
		location[][] boardClone = board1.getBoardLocations().clone();
		ArrayList<location[]> boardCloneX = new ArrayList<location[]>();
		
		for (int g = 0; g < boardClone.length;g++) {
			boardCloneX.add(boardClone[g]);
		}
		
		Collections.shuffle(boardCloneX);
		for (int u = 0;u<boardCloneX.size();u++) {
			ArrayList<location> CloneY = new ArrayList<location>();
			for (int j = 0; j< boardCloneX.get(u).length;j++) {
				CloneY.add(boardCloneX.get(u)[j]);
			}
			Collections.shuffle(CloneY);
			shuffledAL.add(CloneY);
		}
		this.shuffledAL=shuffledAL;
		this.XLen = shuffledAL.size();
		this.YLen = shuffledAL.get(0).size();
	}
	
	public location next() {
		
		location nextLoc = shuffledAL.get(Xindex).get(Yindex);
		//finish makinng here
		
		Yindex++;
		if (Yindex == YLen) {
			Yindex = 0;
			Xindex++;
		}
		return nextLoc;
	}
	
	public location getNextHex() {
		if(!hasNext()){
			return null;
		}
		location nextLoc = shuffledAL.get(Xindex).get(Yindex);
		boolean found = false;
		if (nextLoc.getType().equals("hex")) {
			found = true;
		}
		
		//finish making here
		Yindex++;
		if (Yindex == YLen) {
			Yindex = 0;
			Xindex++;
		}
		if (found) {
		return nextLoc;
		}
		else if (!hasNext()) {
			return null;
		}
		else {
			return getNextHex();
		}
	}
	
	public boolean hasNext() {
		
		if ( !  ((Xindex == XLen-1 && Yindex >= YLen)|| (Xindex>=XLen) ) ) {
			return true;
		}
		else {
			return false;
		}
	}
}
