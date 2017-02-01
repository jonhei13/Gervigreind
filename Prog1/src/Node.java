package Prog1;

public class Node
{
	private Node left;
	private Node center;
	private Node right;
	private Node parent;
	private State state;
	private String move;
	
	public Node(Node left, Node center, Node right, Node parent, State state, String move)
	{
		this.left = left;
		this.right = right;
		this.parent = parent;
		this.state = state;
		this.move = move;
	}
	public void insert(Node node, State state)
	{
		node.left = new Node(null, null, null, node, stateLeft(state), "TURN_LEFT");
		node.center = new Node(null, null, null, node, stateGo(state), "GO");
		node.right = new Node(null, null, null, node, stateRight(state), "TURN_RIGHT");
	}
	public State stateLeft(State state)
	{
		if(state.orientation.equals("NORTH"))
		{
			state.orientation = Orientation.WEST;
			return state;
		}
		else if(state.orientation.equals("WEST"))
		{
			state.orientation = Orientation.SOUTH;
			return state;
		}
		else if(state.orientation.equals("SOUTH"))
		{
			state.orientation = Orientation.EAST;
			return state;
		}
		else
		{
			state.orientation = Orientation.NORTH;
			return state;
		}
	}
	public State stateGo(State state)
	{
		if(state.orientation.equals("NORTH"))
		{
			state.position.y = state.position.y-1;
			return state;
		}
		else if(state.orientation.equals("WEST"))
		{
			state.position.y = state.position.x-1;
			return state;
		}
		else if(state.orientation.equals("SOUTH"))
		{
			state.position.y = state.position.y+1;
			return state;
		}
		else
		{
			state.position.y = state.position.x+1;
			return state;
		}
	}
	public State stateRight(State state)
	{
		if(state.orientation.equals("NORTH"))
		{
			state.orientation = Orientation.EAST;
			return state;
		}
		else if(state.orientation.equals("WEST"))
		{
			state.orientation = Orientation.NORTH;
			return state;
		}
		else if(state.orientation.equals("SOUTH"))
		{
			state.orientation = Orientation.WEST;
			return state;
		}
		else
		{
			state.orientation = Orientation.SOUTH;
			return state;
		}
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
}
