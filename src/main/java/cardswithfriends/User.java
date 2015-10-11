package cardswithfriends;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = -8255733091222689114L;
	
	private int userId;
	private String userName;
	
	public User(int userId, String userName){
		this.userId = userId;
		this.userName = userName;
	}
	
	public int getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	
	public static User getUser(int userID) {
		return DBHandler.getUser(userID);
	}
	
}
