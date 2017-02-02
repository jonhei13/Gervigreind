import com.sun.org.apache.xpath.internal.operations.Or;

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
	private ArrayList<Position> dirts = new ArrayList<Position>();
	private ArrayList<Position> obstacles = new ArrayList<Position>();
	private ArrayList<State> visited = new ArrayList<State>();
    private HashMap<Integer,State> hashMap = new HashMap<Integer, State>();
    private Queue<Node> Frontier = new LinkedList<Node>();

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
	
	public boolean Visited(State theState)
	{
		if(!visited.contains(theState))
			return false;
		return true;
	}
	public boolean TurnThreeTimes(Node node)
	{
		String move = node.getMove();
		if(node.getParent().getMove().equals(move)  && node.getParent().getParent().getMove().equals(move))
			return true;
		return false;
	}
	private int insert(Node node, State state, int counter) {

        int newX = state.position.x;
        int newY = state.position.y;
        if (state.orientation.equals(Orientation.NORTH)) {
            if (newY != 1) {
                newY--;
            }
        }
        else if (state.orientation.equals(Orientation.WEST)){
            if (newX != 1){
                newX--;
            }
        }
        else if (state.orientation.equals(Orientation.SOUTH)){
            if (newY != y) {
                newY++;
            }
        }
        else if (state.orientation.equals(Orientation.EAST)){
            if (newX != x){
                newX++;
            }
        }
        Position pos = new Position(newX, newY);
        State LeftState = new State(state.position, stateLeft(state), true);
        State CenterState = new State(pos, state.orientation, true);
        State RightState = new State(state.position, stateRight(state), true);

        hashMap.put(counter,LeftState);
        counter++;
        hashMap.put(counter,CenterState);
        counter++;
        hashMap.put(counter,RightState);
        counter++;

        node.left = new Node(null, null, null, node, LeftState, "TURN_LEFT");
        node.center = new Node(null, null, null, node, CenterState, "GO");
        node.right = new Node(null, null, null, node, RightState, "TURN_RIGHT");
        Frontier.add(node.left);
        Frontier.add(node.center);
        Frontier.add(node.right);
        return counter;
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
			state.orientation = Orientation.SOUTH;
			return Orientation.SOUTH;
		}
		else if(state.orientation.equals(Orientation.SOUTH))
		{
			state.orientation = Orientation.EAST;
			return Orientation.EAST;
		}
		else
		{
			state.orientation = Orientation.NORTH;
			return Orientation.NORTH;
		}
	}
	private State stateGo(State state)
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
	private Orientation stateRight(State state)
	{
		if(state.orientation.equals(Orientation.NORTH))
		{
			state.orientation = Orientation.EAST;
			return Orientation.EAST;
		}
		else if(state.orientation.equals(Orientation.WEST))
		{
			state.orientation = Orientation.NORTH;
			return Orientation.NORTH;
		}
		else if(state.orientation.equals(Orientation.SOUTH))
		{
			state.orientation = Orientation.WEST;
			return Orientation.WEST;
		}
		else
		{
			state.orientation = Orientation.SOUTH;
			return Orientation.SOUTH;
		}
	}
	public void BFSsearch(State Thestate)
	{
		Node root = new Node(null,null,null,null, Thestate, "");
		ArrayList<String> Moves = new ArrayList<String>();
		int counter = 0;
        BFSsearch(root, Moves, counter);
	}
	private void BFSsearch(Node node, ArrayList<String> Moves, int counter)
	{
		Frontier.add(node);
		
		if(dirts.contains(node.getState().position))
		{
			while(node.getParent() != null)
			{
				Moves.add(node.getMove());
				node = node.getParent();
			}
			return;
		}
		else if (Frontier.isEmpty())
		{
			return;
		}
		else
		{
			Node N = Frontier.poll();
			State S = N.getState();
		    counter = insert(N,S,counter);
			BFSsearch(N, Moves,counter);
		}
	}
	public void generateStates()
    {

        int size = (x * y * 4) - 2;
        ArrayList<Position> Positions = new ArrayList<Position>();
        int tempx = 1;
        int tempy = 1;
        int counter = 0;

        Position startpos = new Position(startX,startY);
        State state = new State(startpos, Orientation.valueOf(direction), false);
        hashMap.put(counter,state);
        counter++;

        for (int i = 1; i <= y; i++){
            for (int j = 1; j <= x; j++){
                Position pos = new Position(tempx,tempy);
                if (!obstacles.contains(pos)){
                    for (int k = 0; k < 4; k++){
                        State mystate = new State(pos, Orientation.values()[k], true);
                        hashMap.put(counter, mystate);
                        counter++;
                    }
                }
                tempx++;
            }
                tempx = 1;
                tempy++;
        }
        hashMap.get(1);


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
						//System.out.println("robot is at " + m.group(1) + "," + m.group(2));
						
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
						System.out.println("Orientation: " + words[1]);
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
						System.out.println("At dirt: " + words[2] + "," + words[3]);
												
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
						System.out.println("At obstacle: " + words[2] + "," + words[3]);
						
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
						System.out.println("Size: " + words[1] + "," + words[2]);
						x = Integer.parseInt(words[1]);
						y = Integer.parseInt(words[2]);
					}
				}
			} else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}
		Position pos = new Position(startX,startY);
		State state = new State(pos, Orientation.valueOf(direction), true);
        BFSsearch(state);
        hashMap.get(1);
		
		initMap(dirts, obstacles, x, y, startX - 1, startY - 1);

    }

    public String nextAction(Collection<String> percepts) {
    	
		System.out.print("perceiving:");
		for(String percept:percepts) {
			System.out.print("'" + percept + "', ");
		}
		System.out.println("");
		String[] actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };
    	
    	
    	if(counter == 0)
    	{
    		counter++;
    		return actions[0];
    	}
    	
    	else
    	{
    		return actions[4];
    	}
    	

		
	}
}
