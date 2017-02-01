package Prog1;

import java.util.Collection;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class NewAgent implements Agent
{	
	private int counter = 0;
	private int cost = 0;
	private int x;
	private int y;
	private int startX;
	private int startY;
	private int[][] map;
	private Node root;
	private ArrayList<Position> dirts = new ArrayList<Position>();
	private ArrayList<Position> obstacles = new ArrayList<Position>();
	private ArrayList<State> visited = new ArrayList<State>();

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
		if(node.getParent().getMove() == move && node.getParent().getParent().getMove() == move)
			return true;
		return false;
	}
    public void init(Collection<String> percepts) {
		/*
			Possible percepts are:
			- "(SIZE x y)" denoting the size of the environment, where x,y are integers
			- "(HOME x y)" with x,y >= 1 denoting the initial position of the robot
			- "(ORIENTATION o)" with o in {"NORTH", "SOUTH", "EAST", "WEST"} denoting the initial orientation of the robot
			- "(AT o x y)" with o being "DIRT" or "OBSTACLE" denoting the position of a dirt or an obstacle
			Moving north increases the y coordinate and moving east increases the x coordinate of the robots position.
			The robot is turned off initially, so don't forget to turn it on.
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
						//System.out.println("Orientation: " + words[1]);
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
