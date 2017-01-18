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
    	int[] nums = {1299451 , 1299457 , 1299491 , 1299499 , 1299533 , 1299541 };
    	int res;
    	int hash = nums[(this.x+this.y) % 6];
    	int hash2 = nums[(this.x*this.y) % 6];
    	res = hash*this.x^this.y*hash2*hash*this.y^this.x*hash2;
    	return 1;

    }
}

