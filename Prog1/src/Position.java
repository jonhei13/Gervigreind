public class Position {

	public int x;
	public int y;
	
	public Position(int x, int y) {
		this.x = x; this.y = y;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public boolean equals(Object o){
		Position p = (Position)o;
		return this.x == p.x && this.y == p.y;
	}
	public int hashCode() {
		return ( x *21143) ^ ( y *45127) ;

	}
}

