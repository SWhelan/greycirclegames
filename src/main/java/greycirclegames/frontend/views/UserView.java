package greycirclegames.frontend.views;

import greycirclegames.User;

public class UserView {
	public String userName;
	public String email;
	
	
	public UserView(User user){
		this.userName = user.getUserName();
		this.email = user.getEmail();
	}

}
