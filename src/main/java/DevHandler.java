import com.mongodb.BasicDBList;

import greycirclegames.DBHandler;
import greycirclegames.User;

public class DevHandler {

	public static void main(String[] args){
		dropDev();
	}	
	
	public static void dropDev(){
		DBHandler.dropAllCollections();
	}
	
	public static void addDefaultUsers() {
		if(DBHandler.numUsers() == 0){
			System.out.println("Creating default users.");
			String password = "password";
			String salt = User.generateSalt();
			User molly = new User(DBHandler.getNextUserID(), "Molly", User.hashPassword(salt, password), salt, "henderson.molly4@gmail.com", new BasicDBList(), true, true, true, true, true, 0);
			DBHandler.createUser(molly);
			salt = User.generateSalt();
			User sarah = new User(DBHandler.getNextUserID(), "Sarah", User.hashPassword(salt, password), salt, "slw96@case.edu", new BasicDBList(), true, true, true, true, true, 0);
			DBHandler.createUser(sarah);
			DBHandler.addFriend(molly.get_id(), sarah.get_id());
			DBHandler.addFriend(sarah.get_id(), molly.get_id());
			
			for(int i = 0; i < 5; i++){
				salt = User.generateSalt();
				User test = new User(DBHandler.getNextUserID(), "Test" + Integer.toString(i), User.hashPassword(salt, password), salt, "test" + Integer.toString(i) +"@test.com", new  BasicDBList(), false, false, false, false, false, 0);
				DBHandler.createUser(test);
				DBHandler.addFriend(test.get_id(), sarah.get_id());
				DBHandler.addFriend(sarah.get_id(), test.get_id());
			}
		}
	}

}
