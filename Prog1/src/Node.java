public class Node
{
	public Node left;
	public Node center;
	public Node right;
	public Node parent;
	public State state;
	public String move;
	public int cost;
	
	public Node(Node left, Node center, Node right, Node parent, State state, String move) {
		this.left = left;
		this.right = right;
		this.parent = parent;
		this.state = state;
		this.move = move;
		this.center = center;
		if(parent != null){
			this.cost = parent.cost+1;
		}
		else
			this.cost = 0;
	}

	public State getState() {
		return state;
	}

	public boolean equals(Object o){
		Node n = (Node) o;
		return this.state.equals(n.state) && this.move.equals(n.move);
	}
}
