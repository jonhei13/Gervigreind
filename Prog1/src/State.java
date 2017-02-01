package Prog1;

public class State {

	//orientation
	//turned on/off
	//hvaða dirt eru eftir
	//position
	//hvaða hlutir breytast
	//mögulega legal actions úr þessu state-i
	
	public Orientation orientation;
	public Position position;
	public boolean turned_on;

	public State(Position position, Orientation orientation, boolean turned_on) {
		this.position = position;
		this.orientation = orientation;
		this.turned_on = turned_on;
	}

	public String toString() {
		return "State{position: " + position + ", orientation: " + orientation + ", on:" + turned_on + "}";
	}
	
}
