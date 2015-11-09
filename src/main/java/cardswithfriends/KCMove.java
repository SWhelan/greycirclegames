package cardswithfriends;

import com.mongodb.BasicDBObject;

public class KCMove extends Move {

	public KCMove(Player player, Pile origin, Pile moving, Pile destination){
		super(player, origin, moving, destination);
	}

	public KCMove(BasicDBObject obj) {
		this.origin = new Pile((BasicDBObject)obj.get("Origin"));
		this.moving = new Pile((BasicDBObject)obj.get("Moving"));
		this.destination = new Pile((BasicDBObject)obj.get("Destination"));
		this.player = new User((BasicDBObject)obj.get("Player"));
	}

	@Override
	public boolean isValid() {
		//If everything in moving is in origin, and if the bottom card of moving works with the top of destination, we good
		if(origin.containsAll(moving)){
			Card movingBottom = moving.getBottom();
			Card destTop = destination.getTop();
			if(cardsAreCompatible(movingBottom, destTop)){
				return true;
			}
		}
		return false;
	}

	private static boolean cardsAreCompatible(Card movingBottom, Card destTop) {
		boolean numbersCompatible = movingBottom.getNumber() == destTop.getNumber() - 1;
		boolean suitsCompatible = movingBottom.isRed() ^ destTop.isRed();
		return numbersCompatible && suitsCompatible;
	}

	@Override
	public void apply() {
		//Remove moving from origin, add to destination
		if(!isValid()){
			throw new IllegalStateException("Cannot apply an invalid move.");
		}
		origin.removeAll(moving);
		destination.addAll(moving);
	}

	
}
