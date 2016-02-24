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
			User molly = new User("Molly", "henderson.molly4@gmail.com", password);
			User sarah = new User("Sarah", "slw96@case.edu", password);
			DBHandler.addFriend(molly.get_id(), sarah.get_id());
			DBHandler.addFriend(sarah.get_id(), molly.get_id());
			
			for(int i = 0; i < 5; i++){
				User test = new User("Test" + Integer.toString(i), "", password);
				DBHandler.addFriend(test.get_id(), sarah.get_id());
				DBHandler.addFriend(sarah.get_id(), test.get_id());
			}
		}
	}

}
