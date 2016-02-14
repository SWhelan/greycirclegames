package greycirclegames;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

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
	
	public static void createGameHistory(GameHistory history) {
		history.set_id(getNextGameHistoryID());
		create(history, "gamehistories");
	}
	
	public static MongoCollection<DBObject> getCollection(String collectionName){
		MongoDatabase db = DatabaseConnector.getMongoDB();
		return db.getCollection(collectionName, DBObject.class);	
	}
	
	public static void create(DBObject obj, String collectionName){
		getCollection(collectionName).insertOne(obj);
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
		DBObject result = getCollection("users").find(eq(fieldName, value)).first();
		if(result == null){
			return null;
		}
		return new User(getCollection("users").find(eq(fieldName, value)).first());
	}

	public static long numUsers(){
		return getCollection("users").count();
	}
	
	public static KingsCorner getKCGame(int gameId) {
		return new KingsCorner(getGame(gameId, "kcgames"));
	}
	
	public static Circles getCirclesGame(int gameId) {
		return new Circles(getGame(gameId, "circlesgames"));
	}
	
	public static GameHistory getGameHistory(Integer firstPlayerId, Integer secondPlayerId) {
		BasicDBObject thing = (BasicDBObject)getCollection("gamehistories").find(and(eq("UserId1", firstPlayerId), eq("UserId2", secondPlayerId))).first();
		if(thing == null){
			return null;
		}
		return new GameHistory(thing);
	}
	
	public static DBObject getGame(int gameId, String collectionName){	
		return getCollection(collectionName).find(eq("_id", gameId)).first();
	}
	
	public static List<KingsCorner> getKCGamesforUser(int userId) {
		return getGames(userId, "kcgames").stream().map(e -> new KingsCorner(e)).collect(Collectors.toList());
	}
	
	public static List<Circles> getCirclesGamesforUser(int userId) {
		return getGames(userId, "circlesgames").stream().map(e -> new Circles(e)).collect(Collectors.toList());
	}
	
	public static List<BasicDBObject> getGames(int userId, String collectionName){
		List<DBObject> list = new ArrayList<DBObject>();
		getCollection(collectionName).find(eq("Players", userId)).iterator().forEachRemaining(list::add);
		return list.stream().map(e -> (BasicDBObject)e).collect(Collectors.toList());
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

	public static void updateGameHistory(GameHistory history) {
		update(history, "gamehistories");
	}
	
	public static void update(DBObject obj, String collectionName){		
		getCollection(collectionName).findOneAndReplace(eq("_id", obj.get("_id")), obj);
	}
	
	private static int getNextID(String idName) {		
		MongoCursor<DBObject> iterator = getCollection("ids").find(eq("name", idName)).iterator();
		int nextId;
		if(iterator.hasNext()){
			nextId = (int) iterator.next().get("nextID");
		} else {
			nextId = 1;
			// We couldn't find it create the record and start at 1
			BasicDBObject obj = new BasicDBObject("name",idName).append("nextID", nextId);
			create(obj, "ids");
		}
		// Always update the id
		BasicDBObject obj = new BasicDBObject("name",idName).append("nextID", nextId + 1);
		getCollection("ids").findOneAndReplace(eq("name", idName), obj);
		
		// return the non incremented id
		return nextId;
	}

	public static int getNextUserID() {
		return getNextID("nextUserID");
	}

	public static int getNextGameID() {
		return getNextID("nextGameID");
	}
	
	public static int getNextGameHistoryID(){
		return getNextID("nextGameHistoryID");
	}

	public static void deleteUser(int userId) {
		getCollection("users").findOneAndDelete(eq("_id", userId));
	}

	public static void deleteKCGame(int gameId) {
		getCollection("kcgames").findOneAndDelete(eq("_id", gameId));
	}


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
		MongoDatabase db = DatabaseConnector.getMongoDB();
		db.listCollectionNames().iterator().forEachRemaining(e -> {
			if(!e.equals("system.indexes")){
				db.getCollection(e).drop();
			}
		});
	}
	
}