package greycirclegames.games;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.ReflectionDBObject;

import greycirclegames.ArtificialPlayer;
import greycirclegames.DBHandler;
import greycirclegames.GameHistory;
import greycirclegames.GameHistoryEntry;
import greycirclegames.NotificationAndEmailHandler;
import greycirclegames.Player;

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
public abstract class Game<M extends Move, S extends GameState, A extends ArtificialPlayer> extends ReflectionDBObject{
	protected int _id; // Mongo :/	
	protected S gameState;
	protected List<M> moves;
	protected boolean isActive;
	protected Integer winner_id;
	public List<Integer> players;
    private int calledEndTurnCount = 0;
	public int currentPlayerIndex;
	protected boolean tie;

	public Game(){
		// Default constructor for database
	}

	/**
	 * Make a completely new game, with a random new game state
	 * @param _id	The id of the game - this should be a unique value and should be input from the database.
	 * @param players The list of players of this game in turn order.
	 */
	public Game(int _id, List<Integer> players){
		this._id = _id;
		this.players = players;
		currentPlayerIndex = 0;
		this.gameState = newGameState(players);
		this.moves = new LinkedList<M>();
		isActive = true;
		winner_id = null;
		this.tie = false;
		NotificationAndEmailHandler.newGame(this.get_id(), this.players, this.getGameTypeIdentifier(), this.getRootUrlRoute() + "/" + Integer.toString(this.get_id()), DBHandler.getUser(this.players.get(0)));
	}
	
	protected void gameFromDBObject(DBObject obj) {
		this._id = (Integer)obj.get("_id");
		this.currentPlayerIndex = (Integer)obj.get("CurrentPlayerIndex");
		this.tie = (boolean)obj.get("Tie");
		BasicDBList players = (BasicDBList)obj.get("Players");
		this.players = new ArrayList<Integer>();
		for (Object id : players) {
			this.players.add((Integer)id);
		}
		BasicDBList moves = (BasicDBList)obj.get("Moves");		
		this.moves = new LinkedList<M>();
		for (Object move : moves) {		
			this.moves.add(makeMoveFromDB((BasicDBObject) move));		
		}
		this.gameState = makeGameStateFromDB((BasicDBObject)obj.get("GameState"));
		this.isActive = (Boolean)obj.get("IsActive");
		this.winner_id = (Integer)obj.get("Winner_id");
	}
	
	/* 
	 * Methods MUST be get/setFieldName (even for booleans)
	 * to be saved DB looks at methods not fields.
	 */
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
	public void addMove(M m){
		moves.add(m);
	}
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean hasSingleMoveTurns() {
		return false;
	}
	public Integer getWinner_id() {
		return winner_id;
	}
	public void setWinner_id(Integer winner_id) {
		this.winner_id = winner_id;
	}
	public List<Integer> getPlayers() {
		return players;
	}
	public void setPlayers(List<Integer> players) {
		this.players = players;
	}
	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}
	public void setCurrentPlayerIndex(int currentPlayerIndex) {
		this.currentPlayerIndex = currentPlayerIndex;
	}
	public boolean getTie() {
		return tie;
	}
	public void setTie(boolean tie) {
		this.tie = tie;
	}

	/**
	 * Each type of game has a string identifier.
	 * For instance, all KingCorner objects have the game type identifier "King's Corner"
	 * which is a constant in the constants file.
	 * @return
	 */
	public abstract String getGameTypeIdentifier();
	
	/**
	 * @return The url route prepended in to access this type of game
	 */
	public abstract String getRootUrlRoute();	
	
	/**
	 * Abstract method for creating a game state for a completely new game
	 * @param players the list of players in turn order
	 * @return a game state for a new game
	 */
	protected abstract S newGameState(List<Integer> players);
	
	public abstract S makeGameStateFromDB(BasicDBObject dbObject);
	
	public abstract M makeMoveFromDB(BasicDBObject move);

	public abstract A makeArtificialPlayer(int playerId);
	
	/**
	 * Returns whether or not this game is over, ie someone won the game.
	 * This method analyzes the game state to determine if someone has won, so
	 * it may return true even if isActive is true.  However, if this function returns
	 * true, isActive should be set to false.
	 * @return	True if a player has won.
	 */
	public abstract boolean gameIsOver();
	
	protected abstract int determineWinnerId();
	
	/**
	 * Override this if anything at all needs to happen once a turn is over
	 */
	public void applyEndTurn() {
		
	}
	
	/**
	 * Override this if the game allows skipping turns and
	 * return true if the current turn should be skipped 
	 * @return false unless overriden
	 */
    public boolean shouldSkipThisTurn() {
        return false;
    }
	
	/**
	 * @return	The winning Player, or null if there is none.
	 */
	public Player getWinner(){
		if(gameIsOver()){
			return getCurrentPlayerObject();
		}
		return null;
	}
	
	public Player getCurrentPlayerObject(){
		int currentPlayerId = this.players.get(this.getCurrentPlayerIndex());
		if(currentPlayerId < 0){
			return makeArtificialPlayer(currentPlayerId);
		} else {
			return DBHandler.getUser(currentPlayerId);
		}
	}
	
	/**
	 * Apply a move to the game.  User data will be captured, and from that we will
	 * construct a move and pass it here to apply it to this game.
	 * @param move A move to be applied to this game.
	 * @return true if the move was successfully passed.  Would return false if, for example, the move was invalid.
	 */
	public boolean applyMove(M move){
		//If the game is not over and the move is valid.
		if(!gameIsOver() && move.isValid()){
			//Apply the move and save it to the list of moves
			move.apply();
			addMove(move);
			//Check if the move has made a player win
			if(gameIsOver()){
				handleGameOver();
			}
			return true;
		}
		return false;
	}
	
    /**
     * If the current move is an AI, we can call this method to play the AI's moves.
     * Also, if the next player(s) is also an AI, this method will play all those
     * AI's moves.
     * @return  True if any AI moves were made.
     */
    @SuppressWarnings("unchecked") // This was the only "problem" in the whole repo + the cast is definitely fine
	public boolean applyAIMoves(){
        boolean result = false;
        M m = null;
        // While the game is still active and the current player is an AI
        while(this.isActive && players.get(currentPlayerIndex) < 0){
            result = true;

            A ai = makeArtificialPlayer(players.get(currentPlayerIndex));
            boolean hasMove = true;
            //Get as many moves from the player as we can (until null)
            while(this.isActive && hasMove){
                m = (M)ai.createMove(this);
                if(m != null){
                    this.applyMove(m);
                    if(this.hasSingleMoveTurns()) {
                        hasMove = false;
                    }
                }else{
                    hasMove = false;
                }               
            }
            if(this.isActive){
            	//End the turn (also increments currentPlayerIndex)
            	this.endTurn();
            }
        }
        return result;
    }
    
	public void endTurn(){
		if(gameIsOver() || calledEndTurnCount > 1){
			handleGameOver();
		} else {
			applyEndTurn();
			currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

            if(shouldSkipThisTurn()) {
                // take care of the next person's turn
                calledEndTurnCount++;
                NotificationAndEmailHandler.turn(this.get_id(), this.getPlayers(), this.getCurrentPlayerObject(), this.getGameTypeIdentifier(), this.getRootUrlRoute() + "/" + Integer.toString(this.get_id()), calledEndTurnCount > 0);
                endTurn();
            } else {
                calledEndTurnCount = 0;
                NotificationAndEmailHandler.turn(this.get_id(), this.getPlayers(), this.getCurrentPlayerObject(), this.getGameTypeIdentifier(), this.getRootUrlRoute() + "/" + Integer.toString(this.get_id()), calledEndTurnCount > 0);
            }
        }
	}
	
    public void handleGameOver(){
    	changeToWinState();
        updateGameHistory();
		NotificationAndEmailHandler.gameOver(this.get_id(), this.getPlayers(), this.getGameTypeIdentifier(), this.getRootUrlRoute() + "/" + this.get_id(), this.getWinner(), this.getCurrentPlayerObject());
    }
    
	protected void changeToWinState(){
		isActive = false;
		this.winner_id = determineWinnerId();
		if(this.winner_id == 0){
			this.tie = true;
		}
	}
	
	public void updateGameHistory(){		
		for(Integer firstPlayerId : this.players){
			if(firstPlayerId > 0){
				for(Integer secondPlayerId : this.players){
					if(secondPlayerId != firstPlayerId){
						if(secondPlayerId >= -1){
							GameHistory history = DBHandler.getGameHistory(firstPlayerId, secondPlayerId);
							if(history == null){
								history = new GameHistory(firstPlayerId, secondPlayerId);
								DBHandler.createGameHistory(history);
							}
							GameHistoryEntry historyEntry = history.getEntryForGameType(this.getGameTypeIdentifier());
							historyEntry.increaseGameCount();
							if(this.tie){
								historyEntry.increaseNumTie();
							} else {
								if(this.winner_id == firstPlayerId){
									historyEntry.increaseNumWins();
								} else {
									historyEntry.increaseNumLost();
								}
							}
							DBHandler.updateGameHistory(history);
						}
					}
				}
			}
		}
	}
	
	public static boolean listHasMoreThanOneHuman(List<Integer> players){
		int count = 0;
		for(Integer id : players){
			if(id > 0){
				count = count + 1;
			}
		}
		return count > 1;
	}
}
