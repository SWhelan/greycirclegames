package cardswithfriends;

import java.io.Serializable;
	
public class User implements Player,Serializable {
	private static final long serialVersionUID = -8255733091222689114L;
	private int userId;
	private String userName;
	private String password;
	private String salt;
	private String email;
	
	public User(int userId, String userName){
		this.userId = userId;
		this.userName = userName;
	}
	@Override
	public int getPlayerId() {
		return userId;
	}
	
	@Override
	public String getUserName() {
		return userName;
	}
	
	public static User getUser(int userID) {
		return DBHandler.getUser(userID);
	}
}
