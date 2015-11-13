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
		if(origin.getName().equals("Draw Pile")){
			return moving.size() == 1 && origin.getTop().equals(moving.getTop());
		}
		//If everything in moving is in origin, and if the bottom card of moving works with the top of destination, we good
		if(origin.containsAll(moving)){
			Card movingBottom = moving.getBottom();
			Card destTop = null;
			if(destination.size() > 0){
				destTop = destination.getTop();
			}
			boolean destIsCorner = destIsCorner(destination);
			if(cardsAreCompatible(movingBottom, destTop, destIsCorner)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean destIsCorner(Pile dest){
		return 	dest.getName().equals("Northeast Pile") ||
				dest.getName().equals("Northwest Pile") ||
				dest.getName().equals("Southeast Pile") ||
				dest.getName().equals("Southwest Pile");
	}

	private static boolean cardsAreCompatible(Card movingBottom, Card destTop, Boolean destIsCorner) {
		if(destTop == null){
			return (!destIsCorner || (destIsCorner && movingBottom.getNumber() == GlobalConstants.KING));
		}
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
