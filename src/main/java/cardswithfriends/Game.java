package cardswithfriends;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * The Game holds a all the information about the game.
 * @author George
 *
 */
public abstract class Game implements Serializable {
	
	private static final long serialVersionUID = -9150883982784712022L;
	//The id of a game
	private final int gameId;
	//The game state
	private final GameState gameState;
	//The list of moves
	private final List<Move> moves;
	
	private final List<Player> players;
	
	//Make a completely new game
	public Game(int gameId, List<Player> players){
		this.gameId = gameId;
		this.gameState = newGameState(players);
		this.moves = new LinkedList<Move>();
		this.players = players;
	}
	
	//Make a pre-existing game
	public Game(int gameId, GameState gameState, List<Move> moves, List<Player> players){
		this.gameId = gameId;
		this.gameState = gameState;
		this.moves = moves;
		this.players = players;
	}
	
	//A game state for a completely new game
	protected abstract GameState newGameState(List<Player> players);
	
	//Convert this game to the save format
	protected abstract boolean save();

	//Apply a move to the game
	public abstract boolean applyMove(Move move);

	//Get the gameId
	public final int getGameId() {
		return gameId;
	}

	//Get the game state
	public final GameState getGameState() {
		return gameState;
	}

	//Get the moves that have happened in the game
	public final List<Move> getMoves() {
		return moves;
	}
	
	//Get a valid new game id -- will use database
	public final static int getNewGameId(){
		return -1;
	}
	
}
