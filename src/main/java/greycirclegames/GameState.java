package greycirclegames;

import java.util.List;
import java.util.Map;

import com.mongodb.ReflectionDBObject;

/**
 * GameState store the "state of the board" of a game.
 * In this abstract, we define values that should be necessary for any game.
 * Everything in GameState is meant to be public - this is a data structure
 * for compartmentalizing the more concrete (not-meta) information of a Game.
 * Having it one place is easier for management, but, technically we could 
 * get by with having all these methods in the Game class, it would just be 
 * very disorganized.
 * @author George
 *
 */
public abstract class GameState extends ReflectionDBObject {
	/**
	 * The current turn number of the game state.
	 */
	public int turnNumber;
	/**
	 *	A map of player id to Piles, representing the player's hand
	 */
	public Map<String, Pile> userHands;
	
	/**
	 * Gets the number of turns played in the game state.
	 * @return The number of turns in the game state.
	 */
	public int getTurnNumber() {
		return turnNumber;
	}
	
	/**
	 * Sets the number of turns in the game state.
	 * @param turnNumber The number of turns in the game state.
	 */
	public void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
	}
	
	/**
	 * Gets the user hands.  NOT A DEFENSIVE COPY:
	 * Move objects need direct access to the piles they are involved with,
	 * in order to apply the move in a way which affects the GameState.
	 * @return	A reference to the users hands.
	 */
	public Map<String, Pile> getUserHands() {
		return userHands;
	}
	
	/**
	 * Sets the user's hands.
	 * @param userHands	The user's hands.
	 */
	public void setUserHands(Map<String, Pile> userHands) {
		this.userHands = userHands; 
	}

	/**
	 * A function which initializes this game state object to a random start state
	 * involving the given list of players.
	 * @param players	A list of players involved in this game.
	 */
	protected abstract void initializeToNewGameState(List<Player> players);
}
