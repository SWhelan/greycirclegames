package cardswithfriends;

import java.util.HashMap;
import java.util.Map;

public abstract class GameState {
	//The current turn
	protected int turnNumber;
	protected Map<Player, Pile> userHands;

	public int getTurnNumber() {
		return turnNumber;
	}
	
	protected abstract String toDBForm();
}
