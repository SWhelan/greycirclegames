package greycirclegames.games;

import com.mongodb.ReflectionDBObject;

public abstract class Move extends ReflectionDBObject {
	//The player who is proposing this move.
	protected Integer playerId;
	
	public Integer getPlayerId() {
		return this.playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
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
