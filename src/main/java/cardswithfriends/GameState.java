package cardswithfriends;

import java.util.List;
import java.util.Map;

import com.mongodb.ReflectionDBObject;

public abstract class GameState extends ReflectionDBObject {
	//The current turn
	public int turnNumber;
	public Map<Player, Pile> userHands;
	
	public int getTurnNumber() {
		return turnNumber;
	}
	public void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
	}
	public Map<Player, Pile> getUserHands() {
		return userHands;
	}
	public void setUserHands(Map<Player, Pile> userHands) {
		this.userHands = userHands;
	}

	protected abstract void initializeToNewGameState(List<Player> players);
}
