package cardswithfriends;

import java.io.Serializable;
import java.util.Map;

public abstract class GameState implements Serializable{
	//The current turn
	protected int turnNumber;
	protected Map<Player, Pile> userHands;

	protected abstract void initializeToNewGameState();
}
