import greycirclegames.DBHandler;
import greycirclegames.User;

public class DevHandler {

	public static void main(String[] args){
		//dropDev();
		changePassword();
	}
	
	private static void changePassword() {
		User user = DBHandler.getUserByUsername("Sarah");
		String newSalt = User.generateSalt();
		String hashedNewPassword = User.hashPassword(newSalt, "password");
		user.setSalt(newSalt);
		user.setPassword(hashedNewPassword);
		DBHandler.updateUser(user);
	}

	public static void dropDev(){
		DBHandler.dropAllCollections();
	}
	
	public static void addDefaultUsers() {
		if(DBHandler.numUsers() == 0){
			System.out.println("Creating default users.");
			String password = "password";
			User sarah = new User("Sarah", "sarah@lifeinstillsllc.com", password);
			
			for(int i = 0; i < 5; i++){
				User test = new User("Test" + Integer.toString(i), "", password);
				DBHandler.addFriend(test.get_id(), sarah.get_id());
				DBHandler.addFriend(sarah.get_id(), test.get_id());
			}
		}
	}

}
