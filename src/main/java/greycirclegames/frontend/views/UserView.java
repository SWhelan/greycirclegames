package greycirclegames.frontend.views;

import greycirclegames.User;

public class UserView {
	public String username;
	public String email;
	public boolean emailForNewFriend;
	public boolean emailForNewGame;
	public boolean emailForTurn;
	public boolean emailForGameOver;
	
	public UserView(User user){
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.emailForNewFriend = user.getEmailForNewFriend();
		this.emailForNewGame = user.getEmailForNewGame();
		this.emailForTurn = user.getEmailForTurn();
		this.emailForGameOver = user.getEmailForGameOver();
	}

}
