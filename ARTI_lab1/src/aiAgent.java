import java.util.ArrayList;
import java.util.Collection;

public class aiAgent implements Agent{
	
	ArrayList<String> preMoves = new ArrayList<String>();
	private boolean lastTurn;
	private static boolean RIGHT_TURN = true;
	private static boolean LEFT_TURN = false;
	private String lastMove;
	private static String[] actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };
	
	@Override
	public String nextAction(Collection<String> percepts) {
		// TODO Auto-generated method stub	
		
		String returnAction;
		
		ArrayList<String> Obsticle = new ArrayList<String>();
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
			if (UnoObsticle == "SUCK"){
				return actions[5];
			}
			else if (UnoObsticle == "BUMP"){
				//TODO;
			}
			else{
				return actions[4];
			}
		}
		System.out.println("");

		
		
		//return actions[random.nextInt(actions.length)];
		return null;
	}
}