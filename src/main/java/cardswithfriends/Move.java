package cardswithfriends;

public abstract class Move {
	private final Player user;
	private final Pile origin;
	private final Pile moving;
	private final Pile destination;

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
		return this.user.getPlayerName() + " moved " + this.moving.toString() + " onto " + this.destination.toString();
	}
}
