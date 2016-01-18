import greycirclegames.DBHandler;
import greycirclegames.User;

public class DevHandler {

	public static void addDefaultUsers() {
		if(DBHandler.numUsers() == 0){
			System.out.println("Creating default users.");
			String password = "password";
			String salt = User.generateSalt();
			User molly = new User(DBHandler.getNextUserID(), "Molly", User.hashPassword(salt, password), salt, "molly.henderson4@gmail.com", null, false, false, false, false);
			DBHandler.createUser(molly);
			salt = User.generateSalt();
			User sarah = new User(DBHandler.getNextUserID(), "Sarah", User.hashPassword(salt, password), salt, "slw96@case.edu", null, false, false, false, false);
			DBHandler.createUser(sarah);
			DBHandler.addFriend(molly.get_id(), sarah.get_id());
			DBHandler.addFriend(sarah.get_id(), molly.get_id());
			
			for(int i = 0; i < 5; i++){
				salt = User.generateSalt();
				User test = new User(DBHandler.getNextUserID(), "Test " + Integer.toString(i), User.hashPassword(salt, password), salt, "test" + Integer.toString(i) +"@test.com", null, false, false, false, false);
				DBHandler.createUser(test);
			}
		}
	}

}
