package greycirclegames;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
		create(user, "users");
	}

	public static void createKCGame(KingsCorner game) {
		create(game, "kcgames");
	}
	
	public static void createCirclesGame(Circles game) {
		create(game, "circlesgames");
	}
	
	public static void create(DBObject obj, String collectionName){
		DB db = DatabaseConnector.getMongoDB();
		DBCollection coll = db.getCollection(collectionName);
		coll.save(obj);
	}

	// READ
	public static User getUser(int userId) {
		return getUserByField("_id", userId);
	}

	public static User getUserByEmail(String email){
		return getUserByField("Email", email);
	}

	public static User getUserByUsername(String username) {
		return getUserByField("Username", username);
	}
	
	public static User getUserByField(String fieldName, Object value){
		DB db = DatabaseConnector.getMongoDB();
		DBCollection coll = db.getCollection("users");
		DBObject query = new BasicDBObject(fieldName, value);
		DBCursor cursor = coll.find(query);
		try {
			DBObject obj = cursor.next();
			return new User(obj);
		} catch (NoSuchElementException e){
			return null;
		}
	}

	public static long numUsers(){
		DB db = DatabaseConnector.getMongoDB();
		DBCollection coll = db.getCollection("users");
		return coll.count();
	}
	
	public static KingsCorner getKCGame(int gameId) {
		return new KingsCorner(getGame(gameId, "kcgames"));
	}
	
	public static Circles getCirclesGame(int gameId) {
		return new Circles(getGame(gameId, "circlesgames"));
	}

	public static DBObject getGame(int gameId, String collectionName){
		DB db = DatabaseConnector.getMongoDB();
		DBCollection coll = db.getCollection(collectionName);
		DBObject query = new BasicDBObject("_id", gameId);
		DBCursor cursor = coll.find(query);
		DBObject obj = cursor.next();
		return obj;
	}
	
	public static List<KingsCorner> getKCGamesforUser(int userId) {
		return getGames(userId, "kcgames").stream().map(e -> new KingsCorner(e)).collect(Collectors.toList());
	}
	
	public static List<Circles> getCirclesGamesforUser(int userId) {
		return getGames(userId, "circlesgames").stream().map(e -> new Circles(e)).collect(Collectors.toList());
	}
	
	public static List<BasicDBObject> getGames(int userId, String collectionName){
		DB db = DatabaseConnector.getMongoDB();
		DBCollection coll = db.getCollection(collectionName);
		DBObject query = new BasicDBObject("Players", userId);

		DBCursor cursor = coll.find(query);

		LinkedList<BasicDBObject> gamesList = new LinkedList<>();
		while(cursor.hasNext()) {
			gamesList.add((BasicDBObject)cursor.next());
		}
		return gamesList;
	}
	

	// UPDATE
	public static void updateUser(User user) {
		update(user, "users");
	}

	public static void updateKCGame(KingsCorner game) {
		update(game, "kcgames");
	}
	
	public static void updateCirclesGame(Circles game) {
		update(game, "circlesgames");
	}

	public static void update(DBObject obj, String collectionName){
		DB db = DatabaseConnector.getMongoDB();
		DBCollection coll = db.getCollection(collectionName);
		coll.save(obj);	
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

	public static void dropAllCollections(){
		DB db = DatabaseConnector.getMongoDB();
		db.getCollectionNames().stream().forEach(e -> {
			if(!e.equals("system.indexes")){
				db.getCollection(e).drop();
			}
		});
	}
	
}