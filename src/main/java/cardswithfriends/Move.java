package cardswithfriends;

public class Move {
	private User user;
	private Pile origin;
	private Pile moving;
	private Pile destination;

	/**
	 * 
	 * @param user
	 * @param origin the pile that the card or entire pile was moved from
	 * @param moving the card or pile being moved
	 * @param destination the pile to place the card or pile onto
	 */
	private Move(User user, Pile origin, Pile moving, Pile destination){
		this.user = user;
		this.origin = origin;
		this.moving = moving;
		this.destination = destination;
	}
	
	public static Move makeMoveIfValid(User user, Pile origin, Pile moving, Pile destination){
		if(isValid(moving, destination)){
			return new Move(user, origin, moving, destination);
		} else {
			return null;
		}
	}
	
	private static boolean isValid(Pile moving, Pile destination){
		// Should probably not do this.
		return true;
	}
	
	@Override
	public String toString(){
		//User # placed card on pile X or Moved PIle X onto Pile Y
		return this.user.getUserName() + " moved " + this.moving.toString() + " onto " + this.destination.toString();
	}
}
