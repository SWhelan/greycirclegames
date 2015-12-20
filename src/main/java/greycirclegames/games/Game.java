package greycirclegames.games;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.ReflectionDBObject;

import greycirclegames.Player;
import greycirclegames.User;
import greycirclegames.games.kingscorner.ArtificialPlayer;
import greycirclegames.games.kingscorner.KCMove;
import greycirclegames.games.kingscorner.Move;

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
public abstract class Game extends ReflectionDBObject{
	
	//The id of a game
	protected int _id;
	//The game state
	protected GameState gameState;
	//The list of moves
	protected List<Move> moves;
	//The list of players
	protected List<Player> players;
	//Whether or not the game is active ie curently playable.
	//The meaning of this may differ from game to game, 
	//but generally this means someone won or the game was cancelled
	protected boolean isActive;
	//Whether or not the outcome of this game has been updated in the leaderboard DB
	protected boolean updatedLeaderboard;
	//The id of the winner of the game (null if no winner)
	protected Integer winner_id;
	
	/**
	 * The turn order of the game in the current state.
	 * Order does matter here.
	 */
	public List<Player> turnOrder;
	// The index of turn order, NOT the id of the current player.
	public int currentPlayer;

	//default constructor, only used when reinitializing from the database
	public Game(){}
	
	//Make a completely new game
	public Game(int _id, List<Player> players){
		this._id = _id;
		turnOrder = new ArrayList<Player>();
		turnOrder.addAll(players);
		currentPlayer = 0;
		this.gameState = newGameState(players);
		this.moves = new LinkedList<Move>();
		this.players = players;
		isActive = true;
		updatedLeaderboard = false;
		winner_id = null;
	}
	
	/**
	 * Make a completely new game, with a random new game state
	 * @param _id	The id of the game - this should be a unique value and should be input from the database.
	 * @param players	The list of players of this game.  Order of this list is not important.
	 */
	public Game(int _id, GameState gameState, List<Move> moves, List<Player> players, boolean active, boolean updatedLeaderboard){
		this._id = _id;
		this.gameState = gameState;
		this.moves = moves;
		this.players = players;
		this.isActive = active;
		this.updatedLeaderboard = updatedLeaderboard;
	}
	
	/**
	 * Make a pre-existing game - used when reconstructing a game from the database
	 * @param _id	The id of the game.  Since this is an old game, it should already be assigned an id in the DB.
	 * @param gameState	A GameState object that reflects the current state of this game. 
	 * @param moves	The list of moves which have been applied to this game.
	 * @param players	The list of players involved in this game.
	 * @param active	Whether or not this game is active.
	 * @param updatedLeaderboard	Whether or not this game has been updated in the leaderboard.
	 */
	public boolean getUpdatedLeaderboard() {
		return updatedLeaderboard;
	}

	/**
	 * Sets whether or not the leaderboard has been updated.
	 * @param updatedLeaderboard	Whether or not the leaderboard has been updated.
	 */
	public void setUpdatedLeaderboard(boolean updatedLeaderboard) {
		this.updatedLeaderboard = updatedLeaderboard;
	}
	
	/**
	 * Returns true if this game has been updated in the leaderboard
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * Sets whether the game is active.
	 * @param isActive	Whether or not the game is active.
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	//A game state for a completely new game
	protected abstract GameState newGameState(List<Player> players);

	/**
	 * Apply a move to the game.  User data will be captured, and from that we will
	 * construct a move and pass it here to apply it to this game.
	 * @param move	A move to be applied to this game.
	 * @return	Returns true if the move was successfully passed.  Would return false if, for example, the move was invalid.
	 */
	public boolean applyMove(Move move){
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
	 * Get the id of the game.
	 */
	public Integer get_id() {
		return _id;
	}
	
	/**
	 * Get the game state of the game
	 * @return	The assosciated GameState object of this game.
	 */
	public GameState getGameState() {
		return gameState;
	}
	
	/**
	 * Get the moves that have happened in the game.
	 * @return	A list of moves that have been applied to this game.
	 */
	public final List<Move> getMoves() {
		return moves;
	}
	
	/**
	 * 
	 * @return	A list of players involved in this game.
	 */
	public final List<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Set the _id of the game.
	 * @param _id	The game's id.
	 */
	public void set_id(int _id) {
		this._id = _id;
	}
	
	/**
	 * Set the gameState
	 * @param gameState
	 */
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	/**
	 * Set the past moves of this game.
	 * @param moves	A list of moves previously applied to this game.
	 */
	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}
	
	/**
	 * Set the Players.
	 * @param players	A list of players involved in this game.
	 */
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
		
	/**
	 * When a game is over, we can update the leaderboard to report the players' 
	 * wins and losses.
	 */
	public void updateLeaderboard(){
		if(gameIsOver() && !updatedLeaderboard){
			assert winner_id != null : "The winner id should not be null here.";
			for(Player p : players){
				//If the player won
				if(p.get_id() == winner_id){
					//Reflect the win in the leaderboard and the player's history
					p.updateWin(getGameTypeIdentifier());
				}else{
					//Otherwise reflect the loss
					p.updateLoss(getGameTypeIdentifier());
				}
			}
			//Since we have updated the leaderboard, we do not want this process to happen again.
			this.updatedLeaderboard = true;
		}
	}
	
	//Each type of game has a string identifier.
	//For instance, all KingCorner objects have the game type identifier "King's Corner"
	//	which is a constant in the constants file.
	protected abstract String getGameTypeIdentifier();

	/**
	 * Returns the id of the winner of the game, or null if no one has won currently.
	 * @return	The id of the winner of the game, or null if no one has won currently.
	 */
	public Integer getWinner_id() {
		return winner_id;
	}

	/**
	 * Sets the winner of the game.
	 * @param winner_id	The id of the player who won this game.
	 */
	public void setWinner_id(Integer winner_id) {
		this.winner_id = winner_id;
	}

	/**
	 * Add a move to the list of applyed moves.
	 * @param m
	 */
	public final void addMove(Move m){
		moves.add(m);
	}
	
	/**
	 * Returns whether or not this game is over, ie someone won the game.
	 * This method analyzes the game state to determine if someone has won, so
	 * it may return true even if isActive is true.  However, if this function returns
	 * true, isActive should be set to false.
	 * @return	True if a player has won.
	 */
	public abstract boolean gameIsOver();
	
	
	public int getCurrentPlayer(){
		return currentPlayer;
	}
	public void setCurrentPlayer(int currentPlayer){
		this.currentPlayer = currentPlayer;
	}
	public List<Player> getTurnOrder(){
		return turnOrder;
	}
	public void setTurnOrder(List<Player> turnOrder){
		this.turnOrder = turnOrder;
	}
	
	/**
	 * Sets the game to a win state - meaning someone has won.
	 */
	protected void changeToWinState(){
		isActive = false;
		winner_id = getCurrentPlayerObject().get_id();
		this.updateLeaderboard();
	}
	
	/**
	 * Gets the Player object of the current player.
	 * @return	The current Player
	 */
	public Player getCurrentPlayerObject(){
		return turnOrder.get(currentPlayer);
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
	 * Checks if th player is an AI for the King's Corner game.
	 * @param p	A player
	 * @return	True if the player is an AI.
	 */
	public static boolean isAI(Player p){
		int id = p.get_id();
		return id < 0;
	}

	public void gameFromDBObject(DBObject obj) {
		this._id = (Integer)obj.get("_id");
		this.currentPlayer = (Integer)obj.get("CurrentPlayer");
		
		BasicDBList turnOrder = (BasicDBList)obj.get("TurnOrder");
		this.turnOrder = new LinkedList<Player>();
		//We have to consider if a player is an Artificial Player or user when
		//constructing from the database
		for (Object player : turnOrder) {
			int playerId = (Integer)((BasicDBObject) player).get("_id");
			if(playerId < 0){
				this.turnOrder.add(new ArtificialPlayer(playerId));
			} else {
				this.turnOrder.add(new User((BasicDBObject)player));
			}
		}
		
		BasicDBList moves = (BasicDBList)obj.get("Moves");
		this.moves = new LinkedList<Move>();
		for (Object move : moves) {
			this.moves.add(new KCMove((BasicDBObject)move));
		}
		this.players = this.turnOrder;
		this.isActive = (Boolean)obj.get("IsActive");
		this.winner_id = (Integer)obj.get("Winner_id");
		this.updatedLeaderboard = (Boolean)obj.get("UpdatedLeaderboard");
		
	}
}
