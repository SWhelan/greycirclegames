package cardswithfriends;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class GameState implements Serializable{
	//The current turn
	public int turnNumber;
	public Map<Player, Pile> userHands;

	protected abstract void initializeToNewGameState(List<Player> players);

}
