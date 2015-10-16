package cardswithfriends;

import java.util.HashMap;
import java.util.Map;

public class KCGameState extends GameState{
	
	public Map<Integer, Pile> piles;
	
	public Map<Player, Pile> userHands = new HashMap<Player, Pile>();

	public KCGameState(KCGameStateGenerator kc){
		//Initialize to a pre-existing game
	}

	public KCGameState() {
		super();
		//Initialize to a new game
	}

	public Map<Player, Pile> getPlayerHands() {
		return userHands;
	}

	public void setPlayerHands(Map<Player, Pile> userHands) {
		this.userHands = userHands;
	}

	@Override
	protected String toDBForm() {
		// TODO Auto-generated method stub
		return null;
	}

	public static class KCGameStateGenerator{
	}
	
	public static KingsCorner getKCGame(int gameID) {
		return DBHandler.getKCGame(gameID);
	}
	
//	public static KingsCorner updateKCGame(int gameID, GameState gameState) {
//		return DBHandler.updateKCGame(gameID, gameState);
//	}
	
}
