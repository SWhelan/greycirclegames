package greycirclegames.games;

import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.ReflectionDBObject;

import greycirclegames.Player;
import greycirclegames.User;
import greycirclegames.games.card.kingscorner.KCArtificialPlayer;

/**
 * The Game holds all the information about the game.
 * Game stores more the "meta" information of the game:
 * The information we still need, but that is not necessary for the game mechanics.
 * Each subclass of Game should have at least one corresponding subclass of GameState,
 * of which Game contains an instance of that GameState object to represent the state
 * of the game "on the board" - the information necessary for game mechanics.
 * @author George
 *
 */
public abstract class Game<M extends Move, S extends GameState> extends ReflectionDBObject{

	/**
	 * The id of a game
	 * This starts with an underscore because of database.
	 */
	protected int _id;
	/**
	 * Game State saves information about board or card piles etc
	 */
	protected S gameState;
	/**
	 * List of moves that have been played to save game history
	 */
	protected List<M> moves;
	/**
	 * True if the game is still being played
	 * False if the game has been won or cancelled 
	 */
	protected boolean isActive;
	/**
	 * The id of the winner of the game (null if no winner)
	 */
	protected Integer winner_id;
	/**
	 * List of players in order of turn.
	 */
	public List<Player> players;
	/**
	 * The INDEX of the players list NOT THE PLAYER'S ID  
	 */
	public int currentPlayerIndex;

	public Game(){
		// Default constructor for database
	}

	/**
	 * Make a completely new game, with a random new game state
	 * @param _id	The id of the game - this should be a unique value and should be input from the database.
	 * @param players The list of players of this game in turn order.
	 */
	public Game(int _id, List<Player> players){
		this._id = _id;
		this.players = players;
		currentPlayerIndex = 0;
		this.gameState = newGameState(players);
		this.moves = new LinkedList<M>();
		isActive = true;
		winner_id = null;
	}

	/**
	 * Abstract method for creating a game state for a completely new game
	 * @param players the list of players in turn order
	 * @return a game state for a new game
	 */
	protected abstract S newGameState(List<Player> players);

	/**
	 * Each type of game has a string identifier.
	 * For instance, all KingCorner objects have the game type identifier "King's Corner"
	 * which is a constant in the constants file.
	 * @return
	 */
	protected abstract String getGameTypeIdentifier();
	
	/**
	 * Returns whether or not this game is over, ie someone won the game.
	 * This method analyzes the game state to determine if someone has won, so
	 * it may return true even if isActive is true.  However, if this function returns
	 * true, isActive should be set to false.
	 * @return	True if a player has won.
	 */
	public abstract boolean gameIsOver();
	
	/**
	 * Once the game is over determine who won.
	 * @return
	 */
	protected abstract int determineWinnerId();
	
	/**
	 * Apply a move to the game.  User data will be captured, and from that we will
	 * construct a move and pass it here to apply it to this game.
	 * @param move	A move to be applied to this game.
	 * @return	Returns true if the move was successfully passed.  Would return false if, for example, the move was invalid.
	 */
	public boolean applyMove(M move){
		//If the game is not over and the move is valid.
		if(!gameIsOver() && move.isValid()){
			//Apply the move and save it to the list of moves
			move.apply();
			addMove(move);
			//Check if the move has made a player win
			if(gameIsOver()){
				changeToWinState();
			}
			return true;
		}
		return false;
	}

	/**
	 * Add a move to the list of applied moves.
	 * @param m
	 */
	public void addMove(M m){
		moves.add(m);
	}

	/**
	 * Sets the game to a win state - meaning someone has won.
	 */
	protected void changeToWinState(){
		isActive = false;
		this.winner_id = determineWinnerId();
	}

	/**
	 * Sets the game to a non-winning end state.
	 * For example, if a player quits the game, the whole game might end.
	 * Currently, this is not implemented on the front-end.
	 */
	protected void changeToNonWinningEndState(){
		isActive = false;
		winner_id = null;
	}

	/**
	 * Returns the winning player of the game, if there is one.
	 * @return	The winning Player, or null if there is none.
	 */
	public Player getWinner(){
		if(gameIsOver()){
			return getCurrentPlayerObject();
		}
		return null;
	}
	
	/**
	 * Gets the Player object of the current player.
	 * @return	The current Player
	 */
	public Player getCurrentPlayerObject(){
		return players.get(currentPlayerIndex);
	}

	/**
	 * Checks if the player is an AI for the King's Corner game.
	 * @param p	A player
	 * @return	True if the player is an AI.
	 */
	public static boolean isAI(Player p){
		int id = p.get_id();
		return id < 0;
	}

	/**
	 * Create a game from a DBObject
	 * @param obj the database object
	 */
	public void gameFromDBObject(DBObject obj) {
		this._id = (Integer)obj.get("_id");
		this.currentPlayerIndex = (Integer)obj.get("CurrentPlayerIndex");

		BasicDBList players = (BasicDBList)obj.get("Players");
		this.players = new LinkedList<Player>();
		//We have to consider if a player is an Artificial Player or user when
		//constructing from the database
		for (Object player : players) {
			int playerId = (Integer)((BasicDBObject) player).get("_id");
			if(playerId < 0){
				this.players.add(new KCArtificialPlayer(playerId));
			} else {
				this.players.add(new User((BasicDBObject)player));
			}
		}
		this.isActive = (Boolean)obj.get("IsActive");
		this.winner_id = (Integer)obj.get("Winner_id");
	}

	public Integer get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public S getGameState() {
		return gameState;
	}

	public void setGameState(S gameState) {
		this.gameState = gameState;
	}

	public List<M> getMoves() {
		return moves;
	}

	public void setMoves(List<M> moves) {
		this.moves = moves;
	}
	
	/* 
	 * Methods MUST be get/setFieldName to be saved in DB
	 * DB looks at methods not fields.
	 */
	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getWinner_id() {
		return winner_id;
	}

	public void setWinner_id(Integer winner_id) {
		this.winner_id = winner_id;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}

	public void setCurrentPlayerIndex(int currentPlayerIndex) {
		this.currentPlayerIndex = currentPlayerIndex;
	}
}
