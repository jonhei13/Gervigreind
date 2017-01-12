import java.util.ArrayList;
import java.util.Collection;

public class aiAgent implements Agent{
	
	ArrayList<String> preMoves = new ArrayList<String>();
	private boolean lastTurn;
    private boolean FirstBump;
	private int x;
    private int y;
	private static boolean RIGHT_TURN;
	private static boolean LEFT_TURN ;
	private String lastMove;
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
		
		String returnAction;
		
		ArrayList<String> Obsticle = new ArrayList<>();
		String UnoObsticle = "";
		System.out.print("perceiving:");
		for(String percept:percepts) {
			System.out.print("'" + percept + "', ");
			Obsticle.add(percept);
			UnoObsticle = percept;
		}
		
		if (Obsticle.size() > 1){
			for (int i = 0; i < Obsticle.size(); i++){
				//TODO:

			}
		}
		else{
            System.out.println("");
            System.out.println(Direction);
			if (UnoObsticle.equals("SUCK")){
				return actions[5];
			}
			else if (UnoObsticle.equals("BUMP")){
                RIGHT_TURN = true;
                FirstBump = true;
                return actions[2];
			}
			else{
                if (Direction.equals("NORTH")){
                    y++;
                }
                else if (Direction.equals("WEST")){
                    x--;
                }
                else if (Direction.equals("EAST")){
                    x++;
                }
                else if (Direction.equals("SOUTH")){
                    y--;
                }
                if (FirstBump) {
                    lastTurn = true;
                    FirstBump = false;
                    Direction = "EAST";
                    return actions[4];
                }
                if (lastTurn){
                    if (Direction.equals("NORTH"))
                        Direction = "SOUTH";
                    else
                        Direction = "NORTH";
                    lastTurn = false;
                    return actions[2];
                }
				return actions[4];
			}
		}
		System.out.println("");

		
		
		//return actions[random.nextInt(actions.length)];
		return null;
	}
}