package cardswithfriends;

import java.util.List;
import java.util.Map;

import com.mongodb.ReflectionDBObject;

public abstract class GameState extends ReflectionDBObject {
	//The current turn
	public int turnNumber;
	//Use the player _id rather than the player
	public Map<String, Pile> userHands;
	
	public int getTurnNumber() {
		return turnNumber;
	}
	public void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
	}
	public Map<String, Pile> getUserHands() {
		return userHands;
	}
	public void setUserHands(Map<String, Pile> userHands) {
		this.userHands = userHands; 
	}

	protected abstract void initializeToNewGameState(List<Player> players);
}
