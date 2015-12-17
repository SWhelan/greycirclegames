package greycirclegames;

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
	// format is:  driver://username:password@hostname.com:port/dbname
	private static final String BETA_DATABASE_URI = "mongodb://heroku_t3f98vjn:inbmdktab4630err6k64igtg2a@ds033125.mongolab.com:33125/heroku_t3f98vjn";
	private static String MONGO_DATABASE_URI = BETA_DATABASE_URI;
	// To connect to mongo shell:
	// mongo host:port/dbname -u dbuser -p dbpassword
	// mongo ds033125.mongolab.com:33125/heroku_t3f98vjn -u heroku_t3f98vjn -p inbmdktab4630err6k64igtg2a@ds033125

	private static MongoClient client;
	private static DB db; 

	public static DB getMongoDB(){
		if(db == null){
			ProcessBuilder processBuilder = new ProcessBuilder();
			String environmentURI = processBuilder.environment().get("MONGOLAB_URI");
			if (environmentURI != null) {
				MONGO_DATABASE_URI = environmentURI;
			}
			MongoClientURI uri  = new MongoClientURI(MONGO_DATABASE_URI); 
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
