package greycirclegames.frontend.views;

import greycirclegames.User;

public class UserView {
	public String userName;
	public String email;
	public boolean emailForNewFriend;
	public boolean emailForNewGame;
	public boolean emailForTurn;
	public boolean emailForGameOver;
	
	public UserView(User user){
		this.userName = user.getUserName();
		this.email = user.getEmail();
		this.emailForNewFriend = user.getEmailForNewFriend();
		this.emailForNewGame = user.getEmailForNewGame();
		this.emailForTurn = user.getEmailForTurn();
		this.emailForGameOver = user.getEmailForGameOver();
	}

}
