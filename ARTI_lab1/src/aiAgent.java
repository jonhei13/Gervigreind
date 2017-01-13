import java.util.ArrayList;
import java.util.Collection;

public class aiAgent implements Agent{
	
	ArrayList<String> preMoves = new ArrayList<String>();
	private boolean lastTurn;
	private static boolean RIGHT_TURN = true;
	private static boolean LEFT_TURN = false;
	private String lastAction;
    private boolean FirstBump;
	private int x;
    private int y;
	private static String[] actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };
    private static String Direction;

    aiAgent(){
        x = 0;
        y = 0;
        Direction = "NORTH";
        FirstBump = false;
        RIGHT_TURN = false;
        LEFT_TURN = false;
        lastTurn = false;
    }
	@Override
	public String nextAction(Collection<String> percepts) {
		// TODO Auto-generated method stub	
		
		String returnAction = null;
		
		ArrayList<String> Obsticle = new ArrayList<>();
		String UnoObsticle = "";
		System.out.print("perceiving:");
		for(String percept:percepts) {
			System.out.print("'" + percept + "', ");
			Obsticle.add(percept);
			UnoObsticle = percept;
		}
        System.out.println("");
        System.out.println(Direction);
		if (Obsticle.size() > 0) {
            for (int i = 0; i < Obsticle.size(); i++) {
                if (Obsticle.get(i).equals("DIRT")) {
                    return actions[5];
                } else if (Obsticle.get(i).equals("BUMP")) {
                    RIGHT_TURN = true;
                    FirstBump = true;
                    if (Direction.equals("NORTH")) {
                        Direction = "EAST";
                        return actions[2];
                    } else if (Direction.equals("SOUTH")) {
                        Direction = "WEST";
                        return actions[3];
                    }
                }
            }
        }
        else {
            if (Direction.equals("NORTH")) {
                y++;
            } else if (Direction.equals("WEST")) {
                x--;
            } else if (Direction.equals("EAST")) {
                x++;
            } else if (Direction.equals("SOUTH")) {
                y--;
            }
            if (FirstBump) {
                lastTurn = true;
                FirstBump = false;
                return actions[4];
            }
            if (lastTurn) {
                lastTurn = false;
                if (Direction.equals("EAST")) {
                    Direction = "SOUTH";
                    return actions[2];
                } else if (Direction.equals("WEST")) {
                    Direction = "NORTH";
                    return actions[3];
                }
            }
            return actions[4];

        }


		System.out.println("");

		lastAction = returnAction;
		return returnAction;
	}
}