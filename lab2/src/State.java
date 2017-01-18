public class State {
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
    	int hash;
    	Orientation[] o = Orientation.values();
    	if(this.orientation == o[0])
    	{
    		hash = 1298617 ;
    	}
    	
    	else if(this.orientation == o[1])
    	{
    		hash = 1298641 ;
    	}
    	
    	else if(this.orientation == o[1])
    	{
    		hash = 1298651 ;
    	}
    	
    	else
    	{
    		hash = 1298653 ;
    	}
    	
    	if(this.turned_on)
    		hash = hash * 1299457;
    	else{
    		hash = hash * 15485849;
		}
    	
    	hash = hash*this.position.x^this.position.y;
    	hash = hash*this.position.y^this.position.x;
    	return hash;
    }

}
