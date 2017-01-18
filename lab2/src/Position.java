public class Position {

    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public boolean equals(Object o){
    	Position p = (Position)o;
        return this.x == p.x && this.y == p.y;
    }
    public int hashCode() {
    	int hash = this.x*this.y % (this.x+this.y);
    	hash = hash*77 + this.x;
    	hash = hash*77 + this.y;
    	return hash;
    }
}

