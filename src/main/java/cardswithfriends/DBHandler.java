/**
 * 
 */
package cardswithfriends;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author Thomas
 *
 */
public class DBHandler {
	//CRUD operations for java classes
	
	//CREATE
	////////////////////////////////////////////////////////////
	public static void createUser(User user) {
        DB db = DatabaseConnector.getMongoDB();
    	DBCollection coll = db.getCollection("users");
    	coll.save(user);
	}
	
	public static void createKCGame(KingsCorner game) {
		DB db = DatabaseConnector.getMongoDB();
    	DBCollection coll = db.getCollection("kcgames");
    	coll.save(game);
	}
	
	//READ
	////////////////////////////////////////////////////////////
	public static User getUser(int userID) {
		DB db = DatabaseConnector.getMongoDB();
    	DBCollection coll = db.getCollection("users");
    	DBObject query = new BasicDBObject("_id", userID);
    	DBCursor cursor = coll.find(query);
    	try {
    		DBObject obj = cursor.next();
    		return new User(obj);
    	} catch (NoSuchElementException e) {
    		return null;
    	}
	}
	
	public static User getUserByEmail(String email){
		DB db = DatabaseConnector.getMongoDB();
    	DBCollection coll = db.getCollection("users");
    	DBObject query = new BasicDBObject("email", email);
    	DBCursor cursor = coll.find(query);
    	try {
    		DBObject obj = cursor.next();
    		return new User(obj);
    	} catch (NoSuchElementException e){
    		return null;
    	}
	}
	
	public static KingsCorner getKCGame(int gameID) {
		DB db = DatabaseConnector.getMongoDB();
    	DBCollection coll = db.getCollection("kcgames");
    	DBObject query = new BasicDBObject("_id", gameID);
    	DBCursor cursor = coll.find(query);
    	DBObject obj = cursor.next();
    	return null;//new KingsCorner(obj);
	}
	
	public static List<KingsCorner> getKCGamesforUser(int userId){
		return null;
	}

	public static Leaderboard getLeadBoard() {
		return new Leaderboard();
	}
	
	//UPDATE
	////////////////////////////////////////////////////////////
	public static void updateUser(User user) {
		DB db = DatabaseConnector.getMongoDB();
    	DBCollection coll = db.getCollection("users");
    	coll.save(user);
	}
	
	public static void updateKCGame(KingsCorner game) {
	}

	public static void replaceLeaderBoard(Leaderboard leaderBoard) {
	}
	
	//DELETE
	////////////////////////////////////////////////////////////
	public static void deleteUser(int userID) {
		DB db = DatabaseConnector.getMongoDB();
    	DBCollection coll = db.getCollection("users");
    	DBObject query = new BasicDBObject("_id", userID);
    	coll.remove(query);
	}
	
	public static void deleteKCGame(int gameID) {
	}

    public static void runTJsTestCode() {
    	
    	System.out.println("TEST");
    	    
//        User user = new User(42,"goduser", "word", "salt", "email@gmail.com");
//    	DBHandler.createUser(user);
//    	User u = getUser(42);
//    	u.setUserName("user2");
//    	updateUser(u);
//    	deleteUser(42);

    	ArrayList<Player> playerList = new ArrayList<Player>();
    	playerList.add(new User(43,"username", "word", "salt", "43email@gmail.com"));
    	playerList.add(new User(44,"username", "word", "salt", "44email@gmail.com"));
    	playerList.add(new User(45,"username", "word", "salt", "45email@gmail.com"));
    	
    	KingsCorner game = new KingsCorner(142, playerList);
    	DBHandler.createKCGame(game);
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	KingsCorner kc = getKCGame(142);
    	kc.turnOrder = null;
    	updateKCGame(kc);
    	deleteKCGame(143);

  
    	

    	
    	int c = 7;
    	
//    	
//    	createLeadBoard(LeaderBoard leaderBoard);
//    	LeaderBoard getLeadBoard()
//    	updateLeaderBoard(LeaderBoard leaderBoard) 
//    	deleteLeaderBoard() 

    	
    	
    	
//    	User getUserByEmail(String email
//    	List<KingsCorner> getKCGamesforUser(int userId
    	

    }

	public static List<Player> getFriendsForUser(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean addFriend(Player user1, Player user2){
		// TODO Auto-generated method stub
		return true;
	}
	
	public static boolean removeFriend(Player user1, Player user2) {
		// TODO Auto-generated method stub
		return true;
	}
}
