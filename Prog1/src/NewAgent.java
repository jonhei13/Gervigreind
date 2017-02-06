import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import com.sun.xml.internal.bind.v2.TODO;

import java.awt.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewAgent implements Agent
{
	//"bfs", "dfs" or "uniform"s
	private String TYPEOFSEARCH = "dfs";

	private int maxX;
	private int maxY;
	private int startX;
	private int startY;
    private String direction;
	private ArrayList<Position> dirts = new ArrayList<Position>();
	private ArrayList<Position> obstacles = new ArrayList<Position>();
    private HashMap<Integer,State> hashMap = new HashMap<Integer, State>();
    private ArrayList<String> Moves = new ArrayList<>();
    private ArrayList<Stack<String>> MyList = new ArrayList<>();
	private ArrayList<String> Commands = new ArrayList<>();
	private ArrayList<ArrayList<String>> MyFinalList = new ArrayList<>();
	private boolean noDirts = false;

	/*
		init(Collection<String> percepts) is called once before you have to select the first action. Use it to find a plan. Store the plan and just execute it step by step in nextAction.
	*/

	private Orientation stateLeft(State state) {
		if(state.orientation.equals(Orientation.NORTH)) {
			return Orientation.WEST;
		}
		else if(state.orientation.equals(Orientation.WEST)) {
			return Orientation.SOUTH;
		}
		else if(state.orientation.equals(Orientation.SOUTH)) {
			return Orientation.EAST;
		}
		else {
			return Orientation.NORTH;
		}
	}

	private Position stateGo(State state) {
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

	private Orientation stateRight(State state) {
		if(state.orientation.equals(Orientation.NORTH)) {
			return Orientation.EAST;
		}
		else if(state.orientation.equals(Orientation.WEST)) {
			return Orientation.NORTH;
		}
		else if(state.orientation.equals(Orientation.SOUTH)) {
			return Orientation.WEST;
		}
		else {
			return Orientation.SOUTH;
		}
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

		hashMap.put(state.hashCode(), node.state);

		return Frontier;
	}

	private LinkedList<Node> insertDFS(Node node, State state, LinkedList<Node> Frontier) {
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

			Frontier.push(node.left);
			Frontier.push(node.right);
		}

		hashMap.put(state.hashCode(), node.state);

		return Frontier;
	}

	public void BFSsearch(State Thestate) {
		Queue<Node> Frontier = new LinkedList<>();
		Node node = new Node(null,null,null,null, Thestate, "");
		Frontier.add(node);
		BFSsearch(node,Frontier);
	}

	private void BFSsearch(Node node, Queue<Node> Frontier) {
		Node currNode = node;
		State Thestate = currNode.state;
		while(!Frontier.isEmpty()) {
			if (dirts.contains(currNode.state.position)) {
				Thestate = currNode.state;
				dirts.remove(currNode.state.position);

				if (!noDirts)
					Moves.add("SUCK");
				while (currNode.parent != null) {
					Moves.add(currNode.move);
					currNode = currNode.parent;
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
				State S = currNode.state;
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

	public void ConvertToCommands() {
		for (int i = 0; i < MyFinalList.size();i++){
			for(int k = MyFinalList.get(i).size()-1; k >= 0 ; k--){
				Commands.add(MyFinalList.get(i).get(k));
			}
		}
	}

	public void DFSsearch(State Thestate) {
		LinkedList<Node> Frontier = new LinkedList<>();
		Node node = new Node(null,null,null,null, Thestate, "");
		Frontier.push(node);
		DFSsearch(node, Frontier);
	}

	private void DFSsearch(Node node, LinkedList<Node> Frontier) {
		Node currNode = node;
		while(!Frontier.isEmpty()) {
			if (dirts.contains(currNode.state.position)) {
				dirts.remove(currNode.state.position);
				State Thestate = currNode.state;
				if (!noDirts)
					Moves.add("SUCK");
				while (currNode.parent != null) {
					Moves.add(currNode.move);
					currNode = currNode.parent;
				}

				if (!dirts.isEmpty()) {
					MyFinalList.add(Moves);
					Moves = new ArrayList<>();
					hashMap = new HashMap<>();
					DFSsearch(Thestate);
				}
				else {
					noDirts = true;
					MyFinalList.add(Moves);
					Position homePos = new Position(startX, startY);
					if (!homePos.equals(Thestate.position)) {
						dirts.add(homePos);
						Moves = new ArrayList<>();
						hashMap = new HashMap<>();
						DFSsearch(Thestate);
					}
				}
			}
			else {
				currNode = Frontier.pop();
				State S = currNode.state;
				Frontier = insertDFS(currNode, S, Frontier);
			}
		}
	}
	
	public void UcSearch(State Thestate) {
		LinkedList<Node> Frontier = new LinkedList<>();
		Node node = new Node(null,null,null,null, Thestate, "");
		Frontier.add(node);
		UcSearch(node, Frontier);
	}
	
	private void UcSearch(Node node, LinkedList<Node> Frontier) {
		Node currNode = node;
		State Thestate = currNode.state;
		LinkedList<Node> explored = new LinkedList<>();
		while(!Frontier.isEmpty()) {
			if (dirts.contains(currNode.state.position)) {
				Thestate = currNode.state;
				dirts.remove(currNode.state.position);

				if (!noDirts)
					Moves.add("SUCK");
				while (currNode.parent != null) {
					Moves.add(currNode.move);
					currNode = currNode.parent;
				}

				if (!dirts.isEmpty()) {
					MyFinalList.add(Moves);
					Moves = new ArrayList<>();
					hashMap = new HashMap<>();
					UcSearch(Thestate);
				}
				else if(dirts.isEmpty() && !noDirts){
					noDirts = true;
					MyFinalList.add(Moves);
					Position homePos = new Position(startX, startY);
					if (!homePos.equals(Thestate.position)) {
						dirts.add(homePos);
						Moves = new ArrayList<>();
						hashMap = new HashMap<>();
						UcSearch(Thestate);
					}
				}
				else if(dirts.isEmpty() && noDirts){
					MyFinalList.add(Moves);
					break;
				}
				break;
			}
			else {
				explored.add(currNode);
				currNode = Frontier.poll();
				State S = currNode.state;
				insertUCS(currNode, S, Frontier, explored);
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
				UcSearch(Thestate);
			}
		}
	}
	
	private Queue<Node> insertUCS(Node node, State state, LinkedList<Node> Frontier, LinkedList<Node> explored) {
		Position pos = stateGo(state);
		if (!hashMap.containsValue(state)) {
			if (!obstacles.contains(pos)) {
				State CenterState = new State(pos, state.orientation, true);
				node.center = new Node(null, null, null, node, CenterState, "GO");
				UcsFrontierInsert(Frontier, node.center, explored);
			}
			State LeftState = new State(state.position, stateLeft(state), true);
			State RightState = new State(state.position, stateRight(state), true);
			node.right = new Node(null, null, null, node, RightState, "TURN_RIGHT");
			node.left = new Node(null, null, null, node, LeftState, "TURN_LEFT");

			UcsFrontierInsert(Frontier, node.left, explored);
			
			if (!obstacles.contains(pos)) {
				State CenterState = new State(pos, state.orientation, true);
				node.center = new Node(null, null, null, node, CenterState, "GO");
				UcsFrontierInsert(Frontier, node.center, explored);
			}
			
			UcsFrontierInsert(Frontier, node.right, explored);
		}

		hashMap.put(state.hashCode(), node.state);

		return Frontier;
	}
	
	public LinkedList<Node> UcsFrontierInsert(LinkedList<Node> Frontier, Node n, LinkedList<Node> explored){
		if(explored.contains(n)){
			return Frontier;
		}
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
					Matcher o = Pattern.compile("\\(\\s*ORIENTATION\\s+([A-Z]+)\\s*\\)").matcher(percept);
					if (o.matches()) {
						String word = percept;
						word = word.replace("(", "");
						word = word.replace(")", "");

						String[] words = word.split(" ");
                        direction = words[1];
					}
				}
				else {
					System.out.println("other percept:" + percept);
					
					if(percept.startsWith("(AT DIRT")) {
						String word = percept;
						word = word.replace("(", "");
						word = word.replace(")", "");
						
						String[] words = word.split(" ");

						int xCord = Integer.parseInt(words[2]);
						int yCord = Integer.parseInt(words[3]);
						
						Position dirt = new Position(xCord, yCord);
						
						dirts.add(dirt);
					}
					else if(percept.startsWith("(AT OBSTACLE")) {
						String word = percept;
						word = word.replace("(", "");
						word = word.replace(")", "");
						
						String[] words = word.split(" ");

						int xCord = Integer.parseInt(words[2]);
						int yCord = Integer.parseInt(words[3]);
						
						Position obstacle = new Position(xCord, yCord);
						
						obstacles.add(obstacle);
					}
					else if(percept.startsWith("(SIZE")) {
						String word = percept;
						word = word.replace("(", "");
						word = word.replace(")", "");
						
						String[] words = word.split(" ");
						maxX = Integer.parseInt(words[1]);
						maxY = Integer.parseInt(words[2]);
					}
				}
			}
			else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}

		TypeOfSearch(TYPEOFSEARCH);

		Commands.add(0, "TURN_ON");
		Commands.add(Commands.size(), "TURN_OFF");
    }

    public void TypeOfSearch(String search) {
		Position pos = new Position(startX, startY);
		State state = new State(pos, Orientation.valueOf(direction), true);
		if (search.equals("bfs")) {
			BFSsearch(state);
			ConvertToCommands();
		}
		else if (search.equals("dfs")) {
			DFSsearch(state);
			ConvertToCommands();
		}
		else if (search.equals("uniform")) {
			UcSearch(state);
			ConvertToCommands();
		}
		else
			System.err.println(search + " is not a valid search.");
	}

    public String nextAction(Collection<String> percepts) {
		System.out.print("perceiving:");
		for(String percept:percepts) {
			System.out.print("'" + percept + "', ");
		}

		System.out.println("");
		System.out.println(Commands);
		String Action = Commands.remove(0);
        System.out.println(Action);

        return Action;
	}
}