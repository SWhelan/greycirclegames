package cardswithfriends;

import com.mongodb.ReflectionDBObject;

public abstract class Move extends ReflectionDBObject {
	protected Player player;
	protected Pile origin;
	protected Pile moving;
	protected Pile destination;

	//default constructor
	public Move(){}
	
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
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public Pile getOrigin() {
		return origin;
	}
	public void setOrigin(Pile origin) {
		this.origin = origin;
	}
	public Pile getMoving() {
		return moving;
	}
	public void setMoving(Pile moving) {
		this.moving = moving;
	}
	public Pile getDestination() {
		return destination;
	}
	public void setDestination(Pile destination) {
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
	
	public class Test{
		public boolean namesEqual(String userName, String originName, String movingName, String destinationName){
			return userName.equals(player.getUserName()) &&
					origin.getName().equals(originName) &&
					moving.getName().equals(movingName) &&
					destination.getName().equals(movingName);
		}
	}
}
