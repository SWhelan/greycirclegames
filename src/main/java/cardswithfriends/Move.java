package cardswithfriends;

import java.io.Serializable;

public abstract class Move implements Serializable{
	private static final long serialVersionUID = 1L;
	private final Player player;
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
	public Move(Player player, Pile origin, Pile moving, Pile destination){
		this.player = player;
		this.origin = origin;
		this.moving = moving;
		this.destination = destination;
	}
	
	public String getPlayerName() {
		return player.getUserName();
	}

	public String getOriginName() {
		return origin.getName();
	}

	public String getMovingName() {
		return moving.getName();
	}

	public String getDestinationName() {
		return destination.getName();
	}
	
	public abstract boolean isValid();
	
	public abstract void apply();
	
	@Override
	public String toString(){
		//Player # placed card on pile X or Moved PIle X onto Pile Y
		return this.player.getUserName() + " moved " + this.moving.getName() + " from " + this.origin.getName() + " onto " + this.destination.getName();
	}
	
	public class Test{
		public boolean namesEqual(String userName, String originName, String movingName, String destinationName){
			return userName.equals(player.getUserName()) &&
					origin.getName().equals(originName) &&
					moving.getName().equals(movingName) &&
					destination.getName().equals(movingName);
		}
	}
}
