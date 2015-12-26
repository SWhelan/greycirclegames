package greycirclegames;

public class ArtificialPlayer implements Player {
	protected int _id;
	protected String userName;
	
	public ArtificialPlayer(int id){
		this._id = id;
		this.userName = "Computer Player " + Integer.toString(Math.abs(this._id));
	}
	
	@Override
	public Integer get_id() {
		return _id;
	}

	@Override
	public String getUserName() {
		return userName;
	}

	@Override
	public void updateWin(String game) {
		// Do nothing as it is a computer player
	}

	@Override
	public void updateLoss(String game) {
		// Do nothing as it is a computer player
	}

}
