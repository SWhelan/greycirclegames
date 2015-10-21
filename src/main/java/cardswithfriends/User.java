package cardswithfriends;

import java.util.Set;
import java.io.Serializable;
	
public class User implements Player,Serializable {
	private static final long serialVersionUID = -8255733091222689114L;
	private int userId;
	private String userName;
	
	public User(int userId, String userName){
		this.userId = userId;
		this.userName = userName;
	}
	@Override
	public Integer getPlayerId() {
		return userId;
	}
	
	@Override
	public String getUserName() {
		return userName;
	}

	public Set<User> getFriends(){
		return null;
	}

	@Override
	public Integer getPlayerID() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static User getUser(int userID) {
		return DBHandler.getUser(userID);
	}
}
