/**
 * 
 */
package cardswithfriends;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
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
	}
	
	public static void createKCGame(KingsCorner game) {
	}
	
	//Enforce singleton
//	public static void createLeadBoard(LeaderBoard leaderBoard) {
//	}
	
	//READ
	////////////////////////////////////////////////////////////
	public static User getUser(int userID) {
		return null;
	}
	
	public static User getUserByEmail(String email){
		return null;
	}
	
	public static KingsCorner getKCGame(int gameID) {
		return null;
	}
	
	public static List<KingsCorner> getKCGamesforUser(int userId){
		return null;
	}

//	public static LeaderBoard getLeadBoard() {
//		return null;
//	}
	
	//UPDATE
	////////////////////////////////////////////////////////////
	public static void updateUser(User user) {
	}
	
	public static void updateKCGame(KingsCorner game) {
	}

//	public static void updateLeaderBoard(LeaderBoard leaderBoard) {
//	}
	
	//DELETE
	////////////////////////////////////////////////////////////
	public static void deleteUser(int userID) {
	}
	
	public static void deleteKCGame(int gameID) {
	}
	
	public static void deleteLeaderBoard() {
	}

    public static void runTJsTestCode() {
    	
    	
    	System.out.println("TEST");
    	
    	
//    	  DBCollection coll = db.getCollection("test");
//        DBObject record = new BasicDBObject();
//        List<Person> persons= new ArrayList<Person>();
//        persons.add(new Person("Jack"));
//        record.put("person", persons);
//        coll.save(record);
        
        User user = new User(42,"goduser");
    	
        DB db = DatabaseConnector.getMongoDB();
    	DBCollection coll = db.getCollection("testCollection");
    	DBObject obj = new BasicDBObject("u1", user);
    	coll.insert(obj);
    	
    	
    	
    	
    	
    	
//    	DB db = DatabaseConnector.getMongoDB();
//    	DBCollection coll = db.getCollection("testCollection");
//    	DBObject obj = new BasicDBObject("_id", 5).append("name", "objName");
//    	coll.insert(obj);
//    	DBObject query = new BasicDBObject("_id", 5);
//    	DBCursor cursor = coll.find(query);
//    	DBObject d = cursor.next();
//    	System.out.println(d);
    	
    	
    	

    }
}
