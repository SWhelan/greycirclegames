package cardswithfriends;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * 
 * @author Sarah and Thomas
 *
 */
public class DatabaseConnector {
	// Should get this value from a config file for real production.
	//format is:  driver://username:password@hostname.com:PORT/db_name
	private static final String MONGO_DATABASE_URL = "mongodb://heroku_97xwzb3p:fi9enecud10l9u2tqmeekq1nq1@ds051943.mongolab.com:51943/heroku_97xwzb3p";
	//To connect to mongo shell:
	//mongo ds012345.mongolab.com:56789/dbname -u dbuser -p dbpassword
	//mongo ds051943.mongolab.com:51943/heroku_97xwzb3p -u heroku_97xwzb3p -p fi9enecud10l9u2tqmeekq1nq1
	
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
