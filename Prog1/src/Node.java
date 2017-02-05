public class Node
{
	public Node left;
	public Node center;
	public Node right;
	public Node parent;
	public State state;
	public String move;
	
	public Node(Node left, Node center, Node right, Node parent, State state, String move)
	{
		this.left = left;
		this.right = right;
		this.parent = parent;
		this.state = state;
		this.move = move;
		this.center = center;
	}
	public Node getParent()
	{
		return parent;
	}
	public State getState()
	{
		return state;
	}
	public String getMove()
	{
		return move;
	}
	public Node getleft()
	{
		return this.left;
	}
	public Node getright()
	{
		return this.right;
	}
	public Node getcenter()
	{
		return this.center;
	}
}
