package greycirclegames.games;

import java.util.Map;

import greycirclegames.games.kingscorner.Pile;

public abstract class CardBasedGameState extends GameState {

	/**
	 *	A map of player id to Piles, representing the player's hand
	 */
	public Map<String, Pile> userHands;
	
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

}
