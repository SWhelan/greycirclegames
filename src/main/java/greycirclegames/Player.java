package greycirclegames;

/**
 * A player who can play in a game.
 * In a Game object, we really only need to know the identifier and
 * sometimes name of a player, so this interface hides other information.
 * For instance, we do not need to know if a player is a human or AI for most
 * if not all aspects of the game.
 * @author George
 *
 */
public interface Player {
	/**
	 * Gets the id of the player.
	 * Negative player ids are AI's.
	 * @return	The player id.
	 */
	public Integer get_id();	
	//Players must override the hashCode and equals functions
	//To be based on the player ids.
	public abstract int hashCode();
	public abstract boolean equals(Object o);
	
	/**
	 * Gets the name of the player.
	 * For computer players, this is something like "Computer player 1"
	 * @return	Name of the player.
	 */
	public String getUsername();
	
	/**
	 * @return how often to check the status of the game in seconds
	 */
	public int getRefreshRateForViewing();
	
}