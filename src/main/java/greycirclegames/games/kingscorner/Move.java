package greycirclegames.games.kingscorner;

import com.mongodb.ReflectionDBObject;

import greycirclegames.Player;

/**
 * A Move encapsulates a move of a card game.
 * @author George
 *
 */
public abstract class Move extends ReflectionDBObject {
	//The player who is proposing this move.
	protected Player player;
	//The pile where the moved cards are coming from.
	protected Pile origin;
	//The pile which is moving (often may be a single card.
	protected Pile moving;
	//The pile to which we are moving the moving cards to.
	protected Pile destination;

	//default constructor
	public Move(){}
	
	/**
	 * Constructor.
	 * @param user	The player who is proposing this move
	 * @param origin the pile that the card or entire pile was moved from
	 * @param moving the card or pile being moved
	 * @param destination the pile to place the card or pile onto
	 */
	public Move(Player player, Pile origin, Pile moving, Pile destination){
		this.player = player;
		this.origin = origin;
		this.moving = moving;
		this.destination = destination;
	}
	
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
	 * Gets the origin pile.
	 * @return
	 */
	public Pile getOrigin() {
		return origin;
	}
	
	/**
	 * Sets the origin pile.
	 * @param origin
	 */
	public void setOrigin(Pile origin) {
		this.origin = origin;
	}
	
	/**
	 * Gets the moving pile.
	 * @return
	 */
	public Pile getMoving() {
		return moving;
	}
	
	/**
	 * Sets the moving pile.
	 * @param moving
	 */
	public void setMoving(Pile moving) {
		this.moving = moving;
	}
	
	/**
	 * Gets the destination pile.
	 * @return
	 */
	public Pile getDestination() {
		return destination;
	}
	
	/**
	 * Sets the destination pile.
	 * @param destination
	 */
	public void setDestination(Pile destination) {
		this.destination = destination;
	}

	/**
	 * Gets the name of the player proposing this move.
	 * @return
	 */
	public String getPlayerName() {
		return player.getUserName();
	}

	/**
	 * Gets the name of the origin pile.
	 * @return
	 */
	public String getOriginName() {
		return origin.getName();
	}

	/**
	 * Gets the name of the moving pile.
	 * @return
	 */
	public String getMovingName() {
		return moving.getName();
	}

	/**
	 * Gets the name of the destination pile.
	 * @return
	 */
	public String getDestinationName() {
		return destination.getName();
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
	
	/**
	 * Generates a string describing the move.
	 */
	@Override
	public String toString(){
		//Player # placed card on pile X or Moved PIle X onto Pile Y
		StringBuilder builder = new StringBuilder();
		if(origin.getName().equals("Draw Pile")){
			builder.append("a card ");
		}else{
			int count = 0;
			for(Card e : this.moving.getCards()) {
				builder.append(e.toString());
				if(this.moving.size() <= 1 || count == this.moving.size() - 1){
					builder.append(" ");
				} else if(count == this.moving.size() - 2){
					builder.append(", and ");
				} else if(count != this.moving.size() - 1){
					builder.append(", ");
				}
				count++;
			};
		}
		return this.player.getUserName() + " moved " + builder.toString() + "from " + this.origin.getName() + " onto " + this.destination.getName();
	}
	
	/**
	 * An inner test class for testing.
	 * @author George
	 *
	 */
	public class Test{
		public boolean namesEqual(String userName, String originName, String movingName, String destinationName){
			return userName.equals(player.getUserName()) &&
					origin.getName().equals(originName) &&
					moving.getName().equals(movingName) &&
					destination.getName().equals(movingName);
		}
	}
}
