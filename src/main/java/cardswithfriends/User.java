package cardswithfriends;

public class User implements Player{
	private int userId;
	private String userName;
	
	public User(int userId, String userName){
		this.userId = userId;
		this.userName = userName;
	}
	@Override
	public int getPlayerId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	
	@Override
	public String getPlayerName() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
