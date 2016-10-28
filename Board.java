import java.util.ArrayList;

public class Board {

	int size;
	Coordinate robber;
	ArrayList<Hex> hexes = new ArrayList<Hex>();
	ArrayList<Port> ports = new ArrayList<Port>();
	ArrayList<Road> roads = new ArrayList<Road>();
	ArrayList<Intersection> buildings = new ArrayList<Intersection>();
}
