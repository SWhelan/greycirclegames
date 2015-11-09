package cardswithfriends;

import java.util.LinkedList;
import java.util.List;

import com.mongodb.ReflectionDBObject;

/**
 * The Game holds all the information about the game.
 * @author George
 *
 */
public abstract class Game extends ReflectionDBObject{
	
	//The id of a game
	protected int _id;
	//The game state
	protected GameState gameState;
	//The list of moves
	protected List<Move> moves;
	//The list of players
	protected List<Player> players;
	protected boolean isActive;

	//default constructor, only used when reinitializing from the database
	public Game(){}
	
	//Make a completely new game
	public Game(int _id, List<Player> players){
		this._id = _id;
		this.gameState = newGameState(players);
		this.moves = new LinkedList<Move>();
		this.players = players;
		isActive = true;
	}
	
	//Make a pre-existing game
	public Game(int _id, GameState gameState, List<Move> moves, List<Player> players, boolean active){
		this._id = _id;
		this.gameState = gameState;
		this.moves = moves;
		this.players = players;
		this.isActive = active;
	}
	
	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	//A game state for a completely new game
	protected abstract GameState newGameState(List<Player> players);
	
	//Convert this game to the save format
	protected abstract boolean save();

	//Apply a move to the game
	public abstract boolean applyMove(Move move);

	//Get the _id
	public Integer get_id() {
		return _id;
	}
	//Get the game state
	public GameState getGameState() {
		return gameState;
	}
	//Get the moves that have happened in the game
	public final List<Move> getMoves() {
		return moves;
	}
	public final List<Player> getPlayers() {
		return players;
	}
	//Set the _id
	public void set_id(int _id) {
		this._id = _id;
	}
	
	
	//Set the gameState
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	
	
	//Set the Moves
	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}
	//Set the Players
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
		
	
	
	
	public final void addMove(Move m){
		moves.add(m);
	}
	
	public abstract boolean gameIsOver();
	
}
