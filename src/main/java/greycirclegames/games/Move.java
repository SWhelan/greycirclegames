package greycirclegames.games;

import com.mongodb.ReflectionDBObject;

import greycirclegames.Player;

public abstract class Move extends ReflectionDBObject {
	//The player who is proposing this move.
	protected Player player;
	
	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Sets the player.
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * Returns true if the move is valid.
	 * @return	True if the move is valid.
	 */
	public abstract boolean isValid();
	
	/**
	 * Applies the move, if valid.
	 */
	public abstract void apply();
}
