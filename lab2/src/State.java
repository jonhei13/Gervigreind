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
        return s.position.equals(this.position) && s.orientation.equals(this.orientation);
    }
    public int hashCode(){
        int res = 31;
        int result = position.x;
        result = res * result + position.x;
        result = res * result + position.y;
        return result;
    }

}
