package cardswithfriends;

import java.util.List;

public class Game {
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
	
}
