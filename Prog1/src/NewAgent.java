import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import com.sun.xml.internal.bind.v2.TODO;

import java.awt.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewAgent implements Agent
{
	private int cost = 0;
	private int maxX;
	private int maxY;
	private int startX;
	private int startY;
    private String direction;
	private int[][] map;
	private int initSizeOfDirts = 0;
	private ArrayList<Position> dirts = new ArrayList<Position>();
	private ArrayList<Position> obstacles = new ArrayList<Position>();
    private HashMap<Integer,State> hashMap = new HashMap<Integer, State>();
    private ArrayList<String> Moves = new ArrayList<>();
    private Node root;
    private ArrayList<Stack<String>> MyList = new ArrayList<>();
	private ArrayList<String> Commands = new ArrayList<>();
	private ArrayList<ArrayList<String>> MyFinalList = new ArrayList<>();
	private boolean noDirts = false;
	private Node currNode;
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
            if (newY != maxY) {
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
            if (newX != maxX){
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
	public boolean turnThreeTimes(Node node)
	{
		return true;
	}
	private Queue<Node> insertBFS(Node node, State state, Queue<Node> Frontier) {

		Position pos = stateGo(state);
		if (!hashMap.containsValue(state)) {
			if (!obstacles.contains(pos)) {
				State CenterState = new State(pos, state.orientation, true);
				node.center = new Node(null, null, null, node, CenterState, "GO");
				Frontier.add(node.center);
			}
			State LeftState = new State(state.position, stateLeft(state), true);
			State RightState = new State(state.position, stateRight(state), true);
			node.right = new Node(null, null, null, node, RightState, "TURN_RIGHT");
			node.left = new Node(null, null, null, node, LeftState, "TURN_LEFT");

			Frontier.add(node.left);
			Frontier.add(node.right);
		}

		hashMap.put(state.hashCode(), node.getState());

		//	}
		return Frontier;
	}

	private Stack<Node> insertDFS(Node node, State state, Stack<Node> Frontier) {

		Position pos = stateGo(state);
		if (!hashMap.containsValue(state)) {
			if (!obstacles.contains(pos)) {
				State CenterState = new State(pos, state.orientation, true);
				node.center = new Node(null, null, null, node, CenterState, "GO");
				Frontier.push(node.center);
			}
			State LeftState = new State(state.position, stateLeft(state), true);
			State RightState = new State(state.position, stateRight(state), true);
			node.right = new Node(null, null, null, node, RightState, "TURN_RIGHT");
			node.left = new Node(null, null, null, node, LeftState, "TURN_LEFT");
			System.out.println(RightState.position);

			Frontier.push(node.left);
			Frontier.push(node.right);
		}

		hashMap.put(state.hashCode(), node.getState());

		//	}
		return Frontier;
	}

	public void BFSsearch(State Thestate)
	{
		Queue<Node> Frontier = new LinkedList<>();
		Node node = new Node(null,null,null,null, Thestate, "");
		Frontier.add(node);
		BFSsearch(node,Frontier);
	}

	private void BFSsearch(Node node, Queue<Node> Frontier)
	{
		currNode = node;
		State Thestate = currNode.state;
		while(!Frontier.isEmpty())
		{
			if (dirts.contains(currNode.getState().position)) {
				Thestate = currNode.state;
				dirts.remove(currNode.getState().position);

				if (!noDirts)
					Moves.add("SUCK");
				while (currNode.getParent() != null) {
					Moves.add(currNode.getMove());
					currNode = currNode.getParent();
				}

				if (!dirts.isEmpty()) {
					MyFinalList.add(Moves);
					Moves = new ArrayList<>();
					hashMap = new HashMap<>();
					BFSsearch(Thestate);
				}
				else if(dirts.isEmpty() && !noDirts){
					noDirts = true;
					MyFinalList.add(Moves);
					Position homePos = new Position(startX, startY);
					if (!homePos.equals(Thestate.position)) {
						dirts.add(homePos);
						Moves = new ArrayList<>();
						hashMap = new HashMap<>();
						BFSsearch(Thestate);
					}
				}
				else if(dirts.isEmpty() && noDirts){
					MyFinalList.add(Moves);
					break;
				}
				break;
			}
			else {
				currNode = Frontier.poll();
				State S = currNode.getState();
				insertBFS(currNode, S, Frontier);
			}
		}
		if(Frontier.isEmpty()) {
			noDirts = true;
			MyFinalList.add(Moves);
			dirts = new ArrayList<>();
			Position homePos = new Position(startX, startY);
			if (!homePos.equals(Thestate.position)) {
				dirts.add(homePos);
				Moves = new ArrayList<>();
				hashMap = new HashMap<>();
				BFSsearch(Thestate);
			}
		}
	}
	public void BfsCommands()
	{
		for (int i = 0; i < MyFinalList.size();i++){
			for(int k = MyFinalList.get(i).size()-1; k >= 0 ; k--){
				Commands.add(MyFinalList.get(i).get(k));
			}
		}
	}
	public void DFSsearch(State Thestate)
	{
		Stack<Node> Frontier = new Stack<>();
		Node node = new Node(null,null,null,null, Thestate, "");
		Frontier.push(node);
		DFSsearch(node, Frontier);
	}
	private void DFSsearch(Node node, Stack<Node> Frontier)
	{
		currNode = node;
		while(!Frontier.isEmpty()) {
			if (dirts.contains(currNode.getState().position)) {
				dirts.remove(currNode.getState().position);
				State Thestate = currNode.state;
				if (!noDirts)
					Moves.add("SUCK");
				while (currNode.getParent() != null) {
					Moves.add(currNode.getMove());
					currNode = currNode.getParent();
				}

				if (!dirts.isEmpty()) {
					MyFinalList.add(Moves);
					Moves = new ArrayList<>();
					hashMap = new HashMap<>();
					BFSsearch(Thestate);
				} else {
					noDirts = true;
					MyFinalList.add(Moves);
					Position homePos = new Position(startX, startY);
					if (!homePos.equals(Thestate.position)) {
						dirts.add(homePos);
						Moves = new ArrayList<>();
						hashMap = new HashMap<>();
						BFSsearch(Thestate);
					}
				}
			} else {
				currNode = Frontier.pop();
				State S = currNode.getState();
				Frontier = insertDFS(currNode, S, Frontier);
			}
		}
	}
	public void DfsCommands()
	{
		for (int i = 0; i < MyFinalList.size();i++){
			for(int k = MyFinalList.get(i).size()-1; k >= 0 ; k--){
				Commands.add(MyFinalList.get(i).get(k));
			}
		}
	}
	
	public void UcSearch(State Thestate){
		ArrayList<Node> Frontier = new ArrayList<>();
		Node node = new Node(null,null,null,null, Thestate, "");
		Frontier.add(node);
		UcSearch(Frontier);
	}
	
	private void UcSearch(ArrayList<Node> Frontier){
		//currNode = node;
		Position homePos = new Position(startX, startY);
		ArrayList<Node> explored = new ArrayList<>();
		
		while(!Frontier.isEmpty()) {
			currNode = Frontier.remove(Frontier.size()-1);
			//System.out.println(currNode);
			System.out.println(currNode.move + " " + currNode.state.position + " " + currNode.cost);
			
			if((currNode.state.position).equals(homePos) && dirts.isEmpty()){
				while (currNode.getParent() != null) {
					if (dirts.contains(currNode.getState().position)) {
						dirts.remove(currNode.getState().position);
						Moves.add("SUCK");
					}
					System.out.println(currNode.getMove());
					Moves.add(currNode.getMove());
					currNode = currNode.getParent();
				}
				MyFinalList.add(Moves);
				break;
			}
			
			if (dirts.contains(currNode.getState().position)) {
				currNode.setCost(currNode.getCost()+1);
			}
			
			State currState = currNode.getState();
			Position goPos = stateGo(currState);
			
			
			
			if (!obstacles.contains(goPos) && !goPos.equals(currState.position)) {
				State CenterState = new State(goPos, currState.orientation, true);
				currNode.center = new Node(null, null, null, currNode, CenterState, "GO");
			}
			
			State RightState = new State(currState.position, stateRight(currState), true);	
			currNode.right = new Node(null, null, null, currNode, RightState, "TURN_RIGHT");
			State LeftState = new State(currState.position, stateLeft(currState), true);
			currNode.left = new Node(null, null, null, currNode, LeftState, "TURN_LEFT");
			
			explored.add(currNode);
			
			
			if(!explored.contains(currNode.left)){
				Frontier = UcsFrontierInsert(Frontier, currNode.left);
			}
			
			if(currNode.center != null){// && !explored.contains(currNode.center)){
				Frontier = UcsFrontierInsert(Frontier, currNode.center);
			}
			
			if(!explored.contains(currNode.right)){
				Frontier = UcsFrontierInsert(Frontier, currNode.right);
			}
			
			
		}
	}
	
	public ArrayList<Node> UcsFrontierInsert(ArrayList<Node> Frontier, Node n){
		if(!Frontier.contains(n)){
			Frontier.add(n);
		}
		else{
			int indx = Frontier.indexOf(n);
			if(Frontier.get(indx).cost > n.cost){
				Frontier.set(indx, n);
			}
		}
		return Frontier;
	}
	
	public void UcsCommands(){
		for (int i = 0; i < MyFinalList.size();i++){
			for(int k = MyFinalList.get(i).size()-1; k >= 0 ; k--){
				Commands.add(MyFinalList.get(i).get(k));
			}
		}
	}
	
	public boolean UcsIsExplored(ArrayList<Node> explored, Node n){
		return false;
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
						maxX = Integer.parseInt(words[1]);
						maxY = Integer.parseInt(words[2]);
					}
				}
			} else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}
		initMap(dirts, obstacles, maxX, maxY, startX - 1, startY - 1);

		//"bfs", "dfs", "uniform" or "A"
		TypeOfSearch("uniform");



		Commands.add(0, "TURN_ON");
		Commands.add(Commands.size(), "TURN_OFF");
    }

    public void TypeOfSearch(String search)
	{
		Position pos = new Position(startX, startY);
		State state = new State(pos, Orientation.valueOf(direction), true);
		if (search == "bfs") {
			BFSsearch(state);
			BfsCommands();
		}
		else if (search == "dfs") {
			DFSsearch(state);
			DfsCommands();
		}
		else if (search == "uniform") {
			UcSearch(state);
			UcsCommands();
		}
		else if (search == "A") {

		}
		else
			System.err.println(search + " is not a valid search.");
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

		System.out.println(Commands);
		String Action = Commands.remove(0);
        System.out.println(Action);
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