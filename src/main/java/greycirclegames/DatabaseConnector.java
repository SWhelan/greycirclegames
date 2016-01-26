package greycirclegames;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

/**
 * 
 * @author Sarah
 *
 */
public class DatabaseConnector {
	// Format: driver://username:password@hostname.com:port/dbname
	private static final String BETA_DATABASE_URI = "mongodb://heroku_t3f98vjn:inbmdktab4630err6k64igtg2a@ds033125.mongolab.com:33125/heroku_t3f98vjn";
	private static String MONGO_DATABASE_URI = BETA_DATABASE_URI;
	// To connect to mongo shell:
	// mongo host:port/dbname -u dbuser -p dbpassword
	// mongo ds033125.mongolab.com:33125/heroku_t3f98vjn -u heroku_t3f98vjn -p inbmdktab4630err6k64igtg2a

	private static MongoClient client;
	private static MongoDatabase db;
	private static MongoClientURI uri;
	
	public static MongoDatabase getMongoDB(){
		if(db == null){
			if(client == null){
				ProcessBuilder processBuilder = new ProcessBuilder();
				String environmentURI = processBuilder.environment().get("MONGOLAB_URI");
				if (environmentURI != null) {
					MONGO_DATABASE_URI = environmentURI;
				}
				uri = new MongoClientURI(MONGO_DATABASE_URI);
				client = new MongoClient(uri);
			}
			db = client.getDatabase(uri.getDatabase());
		}
		return db;
	}
}