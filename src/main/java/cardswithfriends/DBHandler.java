/**
 * 
 */
package cardswithfriends;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

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
		DBObject query = new BasicDBObject("Email", email);
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
    	return new KingsCorner(obj);
	}
	
	public static List<KingsCorner> getKCGamesforUser(int userId){
		return null;
	}

	public static Leaderboard getLeadboard() {
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
		DB db = DatabaseConnector.getMongoDB();
    	DBCollection coll = db.getCollection("kcgames");
    	coll.save(game);
	}

	public static void replaceLeaderboard(Leaderboard leaderboard) {
	}
	
	//in the update section because it creates/updates and gets
	private static int getNextID(String idName) {
		DB db = DatabaseConnector.getMongoDB();
    	DBCollection coll = db.getCollection("ids");
    	DBObject query = new BasicDBObject("name", idName);
    	DBCursor cursor = coll.find(query);
    	DBObject obj;
    	
    	try {
    		obj = cursor.next();
    	}
    	catch (NoSuchElementException e) {
    		///create the id if it does not exist (start at 1)
    		obj = new BasicDBObject("name",idName).append("nextID", 1);
    		coll.save(obj);
    	}
    	
    	int nextID = (Integer)obj.get("nextID");
    	obj.put("nextID", nextID + 1);
    	coll.save(obj);
    	
    	return nextID;
	}
	
	public static int getNextUserID() {
		return getNextID("nextUserID");
	}
	
	public static int getNextKCGameID() {
		return getNextID("nextKCGameID");
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
		DB db = DatabaseConnector.getMongoDB();
    	DBCollection coll = db.getCollection("kcgames");
    	DBObject query = new BasicDBObject("_id", gameID);
    	coll.remove(query);
	}

    public static void runTJsTestCode() {
    	
    	System.out.println("Running jank db test code");
    	
//Test user
//////////////////////////////////////////////////////////////////////////////////////
//        User user = new User(42,"goduser", "word", "salt", "email@gmail.com");
//    	DBHandler.createUser(user);
//    	User u = getUser(42);
//    	u.setUserName("user2");
//    	updateUser(u);
//    	deleteUser(42);
//////////////////////////////////////////////////////////////////////////////////////

//Test kcgame
//////////////////////////////////////////////////////////////////////////////////////
//    	ArrayList<Player> playerList = new ArrayList<Player>();
//    	Player p1 = new User(43,"username", "word", "salt", "43email@gmail.com");
//    	playerList.add(p1);
//    	playerList.add(new User(44,"username", "word", "salt", "44email@gmail.com"));
//    	playerList.add(new User(45,"username", "word", "salt", "45email@gmail.com"));
//    	
//    	Pile pile1 = new Pile("pile one");
//    	Pile pile2 = new Pile("pile two");
//    	Pile pile3 = new Pile("pile three");
//
//    	KingsCorner game = new KingsCorner(142, playerList);
//    	game.addMove(new KCMove(p1, pile1, pile2, pile3));
//    	
//    	DBHandler.createKCGame(game);
//    	try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//    	KingsCorner kc = getKCGame(142);
//    	kc.turnOrder = null;
//    	updateKCGame(kc);
//    	deleteKCGame(142);
//////////////////////////////////////////////////////////////////////////////////////

    	
    	
    	
//Test Leaderboard
//////////////////////////////////////////////////////////////////////////////////////

//    	createLeadBoard(LeaderBoard leaderBoard);
//    	LeaderBoard getLeadBoard()
//    	updateLeaderBoard(LeaderBoard leaderBoard) 
//    	deleteLeaderBoard() 
    	
    	
    	
    	int c = 7;
    	int x = c;
//////////////////////////////////////////////////////////////////////////////////////


    	
    	
    	
//    	User getUserByEmail(String email
//    	List<KingsCorner> getKCGamesforUser(int userId
    	

    }

	public static List<Player> getFriendsForUser(int userId) {
		// TODO Auto-generated method stub
		return new LinkedList<Player>();
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
