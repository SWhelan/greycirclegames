package cardswithfriends;

import java.io.Serializable;

public abstract class Move implements Serializable{
	private final Player user;
	protected final Pile origin;
	protected final Pile moving;
	protected final Pile destination;

	/**
	 * 
	 * @param user
	 * @param origin the pile that the card or entire pile was moved from
	 * @param moving the card or pile being moved
	 * @param destination the pile to place the card or pile onto
	 */
	protected Move(Player user, Pile origin, Pile moving, Pile destination){
		this.user = user;
		this.origin = origin;
		this.moving = moving;
		this.destination = destination;
	}
	
	public abstract boolean isValid();
	
	public abstract void apply();
	
	@Override
	public String toString(){
		//Player # placed card on pile X or Moved PIle X onto Pile Y
		return this.user.getUserName() + " moved " + this.moving.toString() + " from " + this.origin.toString() + " onto " + this.destination.toString();
	}
}
