public class State {

	//orientation
	//turned on/off
	//hva�a dirt eru eftir
	//position
	//hva�a hlutir breytast
	//m�gulega legal actions �r �essu state-i
	
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
	public boolean equals(Object o){
		State s = (State) o;
		return s.position.equals(this.position) && s.orientation.equals(this.orientation) && s.orientation == this.orientation
				&& this.turned_on == s.turned_on;
	}
	public int hashCode() {
        return (position.hashCode() * 1221) ^ (orientation.
                hashCode() * 4512321) ^ (turned_on ? 5231 : 18307);

    }
}
