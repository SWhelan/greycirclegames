package cardswithfriends;

import java.util.HashMap;
import java.util.Map;

public abstract class GameState {
	//The current turn
	protected int turnNumber;
	protected Map<User, Pile> userHands;
	
	public GameState(){
		turnNumber = 0;
		userHands = new HashMap<User, Pile>();
	}

	public int getTurnNumber() {
		return turnNumber;
	}
	
	protected abstract String toDBForm();
}
