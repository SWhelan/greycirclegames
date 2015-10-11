package cardswithfriends;

import java.io.Serializable;
import java.util.List;

public class Game implements Serializable {
	private static final long serialVersionUID = -9150883982784712022L;
	private int gameId;
	private GameState gameState;
	private List<Move> moves;
	
	public boolean applyMove(){
		return true;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public List<Move> getMoves() {
		return moves;
	}

	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}
	
	public static Game getGame(int gameID) {
		return DBHandler.getGame(gameID);
	}
	
}
