import java.util.ArrayList;
import java.util.Collection;

public class aiAgent implements Agent{
	
	ArrayList<String> preMoves = new ArrayList<String>();
	private boolean lastTurn;
	private boolean turn180 = false;
	private static boolean RIGHT_TURN = true;
    private boolean FirstBump;
    private boolean WestBump;
    private boolean WestBump2;
    private boolean MovedForward;
	private int x;
    private int y;
    private int finalCordX;
    private int finalCordY;
	private static String[] actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };
    private static String Direction;
    private boolean firstAction = true;

    aiAgent(){
        x = 0;
        y = 0;
        finalCordX = -1;
        finalCordY = -1;
        Direction = "NORTH";
        FirstBump = false;
        RIGHT_TURN = false;
        lastTurn = false;
        WestBump = false;
        WestBump2 = false;
        MovedForward = false;
    }
	@Override
	public String nextAction(Collection<String> percepts) {
		// TODO Auto-generated method stub	

		if(finalCordX == 0 && finalCordY == 0) {
            return actions[1];
        }
		
		String returnAction = null;
		
		ArrayList<String> Obsticle = new ArrayList<>();
		System.out.print("perceiving:");
		for(String percept:percepts) {
			System.out.print("'" + percept + "', ");
			Obsticle.add(percept);
		}
        System.out.println("");
        System.out.println(Direction);
        if(turn180){
        	turn180 = false;
            Direction = "WEST";
        	return actions[2];
        }
		if (Obsticle.size() > 0) {
            for (int i = 0; i < Obsticle.size(); i++) {
                if (Obsticle.get(i).equals("DIRT")) {
                    return actions[5];
                } 
                else if (Obsticle.get(i).equals("BUMP")) {
	                FirstBump = true;
                    MovedForward = false;
	                if (Direction.equals("WEST") && !WestBump2){
                        WestBump = true;
                        Direction = "NORTH";
                        return actions[2];
                    }
                    if (WestBump){
                        WestBump = false;
                        WestBump2 = true;
                        Direction = "WEST";
                        return actions[3];
                    }
                    if (WestBump2){
                        WestBump2 = false;
                        Direction = "SOUTH";
                        FirstBump = false;
                        lastTurn  = false;
                        MovedForward = true;
                        return actions[3];
                    }
                    if(lastTurn){
	                	turn180 = true;
	                	if(Direction.equals("EAST")){
	                		Direction = "WEST";
	                		return actions[2];
	                	}
	                }
	                if (Direction.equals("NORTH")) {
	                    Direction = "EAST";
                        RIGHT_TURN = true;
	                    return actions[2];
	                }
	                else if (Direction.equals("SOUTH") && RIGHT_TURN) {
	                    Direction = "EAST";
                        RIGHT_TURN = false;
	                    return actions[3];
	                }
                }
            }
        }
        else {
            if (MovedForward){
                if (Direction.equals("NORTH")) {
                    y++;
                    finalCordY = y;
                }
                else if (Direction.equals("SOUTH")) {
                    y--;
                    finalCordY = y;
                }
                else if (Direction.equals("WEST")) {
                    x--;
                    finalCordX = x;
                }
                else if (Direction.equals("EAST")) {
                    x++;
                    finalCordX = x;
                }
            }
            if (FirstBump) {
                if (WestBump)
                    lastTurn = false;
                else
                    lastTurn = true;
                FirstBump = false;

                System.out.println(x + ", " + y);
                MovedForward = true;
                return actions[4];
            }
            else if (Direction.equals("WEST")){
                System.out.println(x + ", " + y);
                MovedForward = true;
                return actions[4];
            }
            if (lastTurn) {
                lastTurn = false;
                if (RIGHT_TURN) {
                    Direction = "SOUTH";
                    return actions[2];
                } else{
                    Direction = "NORTH";
                    return actions[3];
                }
            }
            System.out.println(x + ", " + y);
            MovedForward = true;
            WestBump = false;
            return actions[4];

        }


		System.out.println("");

		return returnAction;
	}
}