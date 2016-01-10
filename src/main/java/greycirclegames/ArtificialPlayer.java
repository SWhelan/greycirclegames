package greycirclegames;

import com.mongodb.ReflectionDBObject;

public class ArtificialPlayer extends ReflectionDBObject implements Player {
	protected int _id;
	protected String username;
	
	public ArtificialPlayer(int id){
		this._id = id;
		this.username = "Computer Player " + Integer.toString(Math.abs(this._id));
	}
	
	@Override
	public Integer get_id() {
		return _id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void updateWin(String game) {
		// Do nothing as it is a computer player
	}

	@Override
	public void updateLoss(String game) {
		// Do nothing as it is a computer player
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + _id;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArtificialPlayer other = (ArtificialPlayer) obj;
		if (_id != other._id)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
