package cardswithfriends;

public class User implements Player{
	private int userId;
	private String userName;
	
	public User(int userId, String userName){
		this.userId = userId;
		this.userName = userName;
	}
	
	public int getPlayerId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	
}
