
public class location {
private Coordinate Coord;
private String type;
private Object contains;
public Coordinate getCoord() {
	return Coord;
}
public void setCoord(Coordinate coord) {
	Coord = coord;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public Object getContains() {
	return contains;
}
public void setContains(Object contains) {
	this.contains = contains;
}
public location(Coordinate coord, String type, Object contains) {
	super();
	Coord = coord;
	this.type = type;
	this.contains = contains;
}
public location() {}
}
