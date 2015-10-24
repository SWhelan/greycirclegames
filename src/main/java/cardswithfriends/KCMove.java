package cardswithfriends;

public class KCMove extends Move {
	private static final long serialVersionUID = 1L;

	KCMove(User user, Pile origin, Pile moving, Pile destination){
		super(user, origin, moving, destination);
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

	private boolean cardsAreCompatible(Card movingBottom, Card destTop) {
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
