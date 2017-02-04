package Prog1;


import java.awt.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewAgent implements Agent
{	
	private int counter = 0;
	private int cost = 0;
	private int x;
	private int y;
	private int startX;
	private int startY;
    private String direction;
	private int[][] map;
	private int initSizeOfDirts = 0;
	private ArrayList<Position> dirts = new ArrayList<Position>();
	private ArrayList<Position> obstacles = new ArrayList<Position>();
	private ArrayList<State> visited = new ArrayList<State>();
    private HashMap<Integer,State> hashMap = new HashMap<Integer, State>();
    private Queue<Node> Frontier = new LinkedList<Node>();
    private Stack<String> BFSMoves = new Stack<String>();
    private Node root;

	/*
		init(Collection<String> percepts) is called once before you have to select the first action. Use it to find a plan. Store the plan and just execute it step by step in nextAction.
	*/
	
	public void initMap(ArrayList<Position> dirts, ArrayList<Position> obstacles, int xSize, int ySize, int sX, int sY)
	{
		map = new int[xSize][ySize];
		
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[i].length; j++)
			{
				if(i == sX && j == sY)
				{
					map[i][j] = 5;
				}
				else
				{
					map[i][j] = 1;
				}
			}
		}

		for(Position dirt:dirts)
		{
			int x = dirt.x - 1;
			int y = dirt.y - 1;
		
			//System.out.println(dirt + " - " + x + "," + y);
			initSizeOfDirts++;
			map[x][y] = 2;
		}
		
		for(Position obstacle:obstacles)
		{
			int x = obstacle.x - 1;
			int y = obstacle.y - 1;
			
			map[x][y] = 3;
		}

		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[i].length; j++)
			{
				System.out.print(map[i][j]);
			}
			System.out.println();
		}
	}
	
	public ArrayList<Position> getDirts()
	{
		return dirts;
	}
	
	public ArrayList<Position> getObstacles()
	{
		return obstacles;
	}


	private Orientation stateLeft(State state)
	{
		if(state.orientation.equals(Orientation.NORTH))
		{
			//state.orientation = Orientation.WEST;
			return Orientation.WEST;
		}
		else if(state.orientation.equals(Orientation.WEST))
		{
		//	state.orientation = Orientation.SOUTH;
			return Orientation.SOUTH;
		}
		else if(state.orientation.equals(Orientation.SOUTH))
		{
		//	state.orientation = Orientation.EAST;
			return Orientation.EAST;
		}
		else
		{
		//	state.orientation = Orientation.NORTH;
			return Orientation.NORTH;
		}
	}

	private Position stateGo(State state)
	{
        int newX = state.position.x;
        int newY = state.position.y;
        if (state.orientation.equals(Orientation.NORTH)) {
            if (newY != y) {
                newY++;
            }
        }
        else if (state.orientation.equals(Orientation.WEST)){
            if (newX != 1){
                newX--;
            }
        }
        else if (state.orientation.equals(Orientation.SOUTH)){
            if (newY != 1) {
                newY--;
            }
        }
        else if (state.orientation.equals(Orientation.EAST)){
            if (newX != x){
                newX++;
            }
        }
        Position pos = new Position(newX, newY);
        return pos;

	}

	private Orientation stateRight(State state)
	{
		if(state.orientation.equals(Orientation.NORTH))
		{
		//	state.orientation = Orientation.EAST;
			return Orientation.EAST;
		}
		else if(state.orientation.equals(Orientation.WEST))
		{
		//	state.orientation = Orientation.NORTH;
			return Orientation.NORTH;
		}
		else if(state.orientation.equals(Orientation.SOUTH))
		{
		//	state.orientation = Orientation.WEST;
			return Orientation.WEST;
		}
		else
		{
			//state.orientation = Orientation.SOUTH;
			return Orientation.SOUTH;
		}
	}

    private void insert(Node node, State state) {

        Position pos = stateGo(state);
        State LeftState = new State(state.position, stateLeft(state), true);
        State RightState = new State(state.position, stateRight(state), true);


        if (!obstacles.contains(pos)) {
            State CenterState = new State(pos, state.orientation, true);
            if (!hashMap.containsValue(CenterState)){
                hashMap.put(CenterState.hashCode(), CenterState);
                node.center = new Node(null, null, null, node, CenterState, "GO");
                Frontier.add(node.center);
            }
        }

        if (!hashMap.containsValue(LeftState)){
            hashMap.put(LeftState.hashCode(), LeftState);
            node.left = new Node(null, null, null, node, LeftState, "TURN_LEFT");
            Position Leftpos = stateGo(LeftState);
            if (!obstacles.contains(Leftpos))
                Frontier.add(node.left);
        }

        if (!hashMap.containsValue(RightState)){
            hashMap.put(RightState.hashCode(), RightState);
            node.right = new Node(null, null, null, node, RightState, "TURN_RIGHT");
            Position Rightpos = stateGo(RightState);
            if (!obstacles.contains(Rightpos))
                Frontier.add(node.right);
        }
    }

	public void BFSsearch(State Thestate)
	{
		root = new Node(null,null,null,null, Thestate, "");
        
        BFSsearch(root);
	}

	private void BFSsearch(Node node)
	{
		Frontier.add(node);
		
		if(dirts.contains(node.getState().position))
		{
			System.out.println("Node State: " + node.getState().position);
			System.out.println("Dirts pos: "  + dirts);
			System.out.println("Obstacles pos: "  + obstacles);
            if(dirts.size() == initSizeOfDirts)
            	BFSMoves.push("TURN_OFF");
			dirts.remove(node.getState().position);
            State rootState = node.getState();
            BFSMoves.push("SUCK");
            Frontier.clear();
			while(node.getParent() != null)
			{
                BFSMoves.push(node.getMove());
                System.out.println("Adding move: " + node.getMove() + " to BFSMoves");
				node = node.getParent();
			}
			if(!dirts.isEmpty())
				BFSsearch(rootState);
			else
				BFSMoves.push("TURN_ON");
		}
		else if(Frontier.isEmpty())
		{
			return;
		}
		else {
            Node N = Frontier.poll();
            State S = N.getState();
            insert(N, S);
            BFSsearch(N);
        }
	}

    public void init(Collection<String> percepts) {
		/*
			et to turn it on.Possible percepts are:
			- "(SIZE x y)" denoting the size of the environment, where x,y are integers
			- "(HOME x y)" with x,y >= 1 denoting the initial position of the robot
			- "(ORIENTATION o)" with o in {"NORTH", "SOUTH", "EAST", "WEST"} denoting the initial orientation of the robot
			- "(AT o x y)" with o being "DIRT" or "OBSTACLE" denoting the position of a dirt or an obstacle
			Moving north increases the y coordinate and moving east increases the x coordinate of the robots position.
			The robot is turned off initially, so don't forg
		*/
		Pattern perceptNamePattern = Pattern.compile("\\(\\s*([^\\s]+).*");
		for (String percept:percepts) {
			Matcher perceptNameMatcher = perceptNamePattern.matcher(percept);
			if (perceptNameMatcher.matches()) {
				String perceptName = perceptNameMatcher.group(1);
				if (perceptName.equals("HOME")) {
					Matcher m = Pattern.compile("\\(\\s*HOME\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						
						startX = Integer.parseInt(m.group(1));
						startY = Integer.parseInt(m.group(2));
					}
				}
				else if(perceptName.equals("ORIENTATION"))
				{
					//String or = perceptNameMatcher.group(2);
					Matcher o = Pattern.compile("\\(\\s*ORIENTATION\\s+([A-Z]+)\\s*\\)").matcher(percept);
					if (o.matches()) {
						String word = percept;
						word = word.replace("(", "");
						word = word.replace(")", "");

						String[] words = word.split(" ");
						//System.out.println("Orientation: " + words[1]);
                        direction = words[1];
					}
				}
				else {
					System.out.println("other percept:" + percept);
					
					if(percept.startsWith("(AT DIRT"))
					{	
						String word = percept;
						word = word.replace("(", "");
						word = word.replace(")", "");
						
						String[] words = word.split(" ");
						//System.out.println("At dirt: " + words[2] + "," + words[3]);
												
						int xCord = Integer.parseInt(words[2]);
						int yCord = Integer.parseInt(words[3]);
						
						Position dirt = new Position(xCord, yCord);
						
						dirts.add(dirt);
					}
					else if(percept.startsWith("(AT OBSTACLE"))
					{	
						String word = percept;
						word = word.replace("(", "");
						word = word.replace(")", "");
						
						String[] words = word.split(" ");
						//System.out.println("At obstacle: " + words[2] + "," + words[3]);
						
						int xCord = Integer.parseInt(words[2]);
						int yCord = Integer.parseInt(words[3]);
						
						Position obstacle = new Position(xCord, yCord);
						
						obstacles.add(obstacle);
					}
					else if(percept.startsWith("(SIZE"))
					{	
						String word = percept;
						word = word.replace("(", "");
						word = word.replace(")", "");
						
						String[] words = word.split(" ");
						//System.out.println("Size: " + words[1] + "," + words[2]);
						x = Integer.parseInt(words[1]);
						y = Integer.parseInt(words[2]);
					}
				}
			} else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}
		Position pos = new Position(startX, startY);
		State state = new State(pos, Orientation.valueOf(direction), true);
        hashMap.put(state.hashCode(), state);

		initMap(dirts, obstacles, x, y, startX - 1, startY - 1);

		BFSsearch(state);
        hashMap.get(1);
        
		

    }

    public String nextAction(Collection<String> percepts) {

		String per = "";

		System.out.print("perceiving:");
		for(String percept:percepts) {
			System.out.print("'" + percept + "', ");
			per = percept;
		}
		System.out.println("");
		String[] actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };

		for(String m : BFSMoves)
		{
			System.out.println("Move: " + m);
		}

        String Action = BFSMoves.pop();
        if (Action.equals("TURN_ON"))
            return actions[0];
        else if (Action.equals("TURN_RIGHT"))
            return actions[2];
        else if (Action.equals("TURN_LEFT"))
            return actions[3];
        else if (Action.equals("GO"))
            return actions[4];
        else if (Action.equals("SUCK"))
            return actions[5];
        else
			return actions[1];
	}
}
