package cardswithfriends;

public class KCMove extends Move {
	
	private KCMove(User user, Pile origin, Pile moving, Pile destination){
		super(user, origin, moving, destination);
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		//If everything in moving is in origin, and if the bottom card of moving works with the top of destination, we good
		return false;
	}

	@Override
	public void apply() {
		// TODO Auto-generated method stub
		//Remove moving from origin, add to destination
	}

}
