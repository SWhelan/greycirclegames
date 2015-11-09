/**
 * 
 */
package cardswithfriends;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import com.mongodb.BasicDBList;
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

	public static Player getUserByUserName(String userName) {
		DB db = DatabaseConnector.getMongoDB();
		DBCollection coll = db.getCollection("users");
		DBObject query = new BasicDBObject("UserName", userName);
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

	public static List<KingsCorner> getKCGamesforUser(int userId) {
		DB db = DatabaseConnector.getMongoDB();
		DBCollection coll = db.getCollection("kcgames");
		DBObject query = new BasicDBObject("Players._id", userId);

		DBCursor cursor = coll.find(query);

		LinkedList<KingsCorner> gamesList = new LinkedList<>();
		while(cursor.hasNext()) {
			System.out.println("Found a game for user: " + userId);
			BasicDBObject obj = (BasicDBObject)cursor.next();
			gamesList.add(new KingsCorner(obj));
		}

		if (gamesList.isEmpty()) {
			System.out.println("No games found for user: " + userId);
		}
		return gamesList;
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

	//OTHER
	////////////////////////////////////////////////////////////
	/**
	 * @param userId the id of the user to get friends from
	 * @return a list of userID's indicating who the user is friends with
	 */
	public static BasicDBList getFriendsForUser(int userId) {
		User user = getUser(userId);
		return user.getFriends();
	}

	/**
	 * adds user2 to user1's friend list
	 * 
	 * @param userID1
	 * @param userID2
	 * @return whether adding the friend succeeded
	 */
	public static boolean addFriend(int userID1, int userID2){
		try {
			User u1 = getUser(userID1);
			u1.addFriend(userID2);
			updateUser(u1);
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean removeFriend(int userID1, int userID2) {
		try {
			User u1 = getUser(userID1);
			u1.destroyFriendship(userID2);
			updateUser(u1);
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void runTJsTestCode() {

		System.out.println("Running jank db test code");

		//Test user
		//////////////////////////////////////////////////////////////////////////////////////
		//    	BasicDBList frands = new BasicDBList();
		//    	frands.add(6);
		//    	frands.add(7);
		//    	frands.add(42);
		//    	
		//    	User user = new User(43,"goduser", "word", "salt", "email@gmail.com", frands);
		//    	DBHandler.createUser(user);
		//    	User u = getUser(43);
		//    	u.setUserName("user2");
		//    	updateUser(u);
		//    	deleteUser(43);
		//////////////////////////////////////////////////////////////////////////////////////

		//Test kcgame
		//////////////////////////////////////////////////////////////////////////////////////
		//    	ArrayList<Player> playerList = new ArrayList<Player>();
		//    	BasicDBList frands = new BasicDBList();
		//    	frands.add(6);
		//    	frands.add(7);
		//    	frands.add(8);
		//    	
		//    	Player p1 = new User(43,"username", "word", "salt", "43email@gmail.com", new BasicDBList());    	
		//    	
		//    	playerList.add(p1);
		//    	playerList.add(new User(44,"username", "word", "salt", "44email@gmail.com", frands));
		//    	playerList.add(new User(45,"username", "word", "salt", "45email@gmail.com", frands));
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
		//    	
		//    	LinkedList<KingsCorner> l = (LinkedList<KingsCorner>) getKCGamesforUser(45);
		//    	
		//    	KingsCorner kc = getKCGame(142);
		//    	kc.setIsActive(false);
		//    	updateKCGame(kc);
		//    	deleteKCGame(142);
		//////////////////////////////////////////////////////////////////////////////////////    	


		//Test Leaderboard
		//////////////////////////////////////////////////////////////////////////////////////

		//    	Leaderboard leaderboard = new Leaderboard();
		//    	replaceLeaderboard(leaderboard);
		//    	LeaderBoard getLeadBoard()
		//    	updateLeaderBoard(LeaderBoard leaderBoard) 
		//    	deleteLeaderBoard() 	

		int c = 7;
		int x = c;
		//////////////////////////////////////////////////////////////////////////////////////







	}
}