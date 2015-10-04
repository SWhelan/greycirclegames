package cardswithfriends;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class DatabaseConnector {
	// Should get this value from a config file for real production.
	private static final String MONGO_DATABASE_URL = "mongodb://heroku_97xwzb3p:fi9enecud10l9u2tqmeekq1nq1@ds051943.mongolab.com:51943/heroku_97xwzb3p";
	private static MongoClient client;
	private static DB db; 
	
    public static DB getMongoDB(){
    	if(db == null){
    		MongoClientURI uri  = new MongoClientURI(MONGO_DATABASE_URL); 
    		MongoClient client = new MongoClient(uri);
        	db = client.getDB(uri.getDatabase());
    	}
    	return db;
    }
    
    //Should be closed on app shutdown
    public static void closeConnection(){
    	client.close();
    }
}
