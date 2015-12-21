package greycirclegames;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import greycirclegames.games.board.circles.Circles;
import greycirclegames.games.card.kingscorner.KingsCorner;

/**
 * @ author Thomas Pech
 */

public class DBHandler {
	// CRUD operations for java classes

	// CREATE
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
	
	public static void createCirclesGame(Circles game) {
		DB db = DatabaseConnector.getMongoDB();
		DBCollection coll = db.getCollection("circlesgames");
		coll.save(game);
	}

	// READ
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
	
	public static Circles getCirclesGame(int gameID) {
		DB db = DatabaseConnector.getMongoDB();
		DBCollection coll = db.getCollection("circlesgames");
		DBObject query = new BasicDBObject("_id", gameID);
		DBCursor cursor = coll.find(query);
		DBObject obj = cursor.next();
		return new Circles(obj);
	}

	public static List<KingsCorner> getKCGamesforUser(int userId) {
		DB db = DatabaseConnector.getMongoDB();
		DBCollection coll = db.getCollection("kcgames");
		DBObject query = new BasicDBObject("Players._id", userId);

		DBCursor cursor = coll.find(query);

		LinkedList<KingsCorner> gamesList = new LinkedList<>();
		while(cursor.hasNext()) {
			BasicDBObject obj = (BasicDBObject)cursor.next();
			gamesList.add(new KingsCorner(obj));
		}
		return gamesList;
	}
	
	// TODO generics or something/helper methods etc to remove duplicated code	
	public static List<Circles> getCirclesGamesforUser(int userId) {
		DB db = DatabaseConnector.getMongoDB();
		DBCollection coll = db.getCollection("circlesgames");
		DBObject query = new BasicDBObject("Players._id", userId);

		DBCursor cursor = coll.find(query);

		LinkedList<Circles> gamesList = new LinkedList<>();
		while(cursor.hasNext()) {
			BasicDBObject obj = (BasicDBObject)cursor.next();
			gamesList.add(new Circles(obj));
		}
		return gamesList;
	}

	public static Leaderboard getLeaderboard() {
		DB db = DatabaseConnector.getMongoDB();
		DBCollection coll = db.getCollection("leaderboard");
		BasicDBObject obj = (BasicDBObject) coll.findOne();

		if (obj == null) {
			//leaderboard DNE, so create, insert, and return
			Leaderboard lb = new Leaderboard();
			coll.save(lb);
			return lb;
		}
		else {
			return new Leaderboard(obj);
		}
	}

	// UPDATE
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

	public static void updateLeaderboard(Leaderboard leaderboard) {
		DB db = DatabaseConnector.getMongoDB();
		DBCollection coll = db.getCollection("leaderboard");
		coll.drop(); // delete old leaderboard
		coll.save(leaderboard);
	}

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

	public static int getNextGameID() {
		return getNextID("nextGameID");
	}

	// DELETE
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

	// OTHER
	/**
	 * @param userId the id of the user to get friends from
	 * @return a list of userID's indicating who the user is friends with
	 */
	public static BasicDBList getFriendsForUser(int userId) {
		User user = getUser(userId);
		if(user == null){
			return null;
		}
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

}