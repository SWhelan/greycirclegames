package greycirclegames;

import java.util.Optional;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

/**
 * Format of database url: driver://username:password@hostname.com:port/dbname
 * To connect via mongo shell/command line: mongo host:port/dbname -u dbuser -p dbpassword
 */
public class DatabaseConnector {
	private static MongoClient client;
	private static MongoDatabase db;
	private static MongoClientURI uri;
	
	
	public static MongoDatabase getMongoDB() {
		return db;
	}
	
	/**
	 * Use this only when you need a specific database. To get the defalt see {@link DatabaseConnector#getMongoDB}
	 * @param url format driver://username:password@hostname.com:port/dbname
	 * @return MongoDatabase of that url
	 */
	private static MongoDatabase setSpecifiedMongoDB(String url) {
		uri = new MongoClientURI(url + "?retryWrites=false"); // Additional parameter required after updating mongo driver. Probably should update mongo DB versions on heroku to match so that we don't need this query param.
		client = new MongoClient(uri);
		db = client.getDatabase(uri.getDatabase());
		return db;
	}
	
	public static void setTestingDatabase() {
		setSpecifiedMongoDB(ConfigurationHandler.getConfig("test.db.url").get());
	}

	public static void setDefaultDatabase() {
		Optional<String> mongoDatabaseURI = Optional.empty();
		ProcessBuilder processBuilder = new ProcessBuilder();
		String environmentURI = processBuilder.environment().get("MONGOLAB_URI");
		if (environmentURI != null) {
			mongoDatabaseURI = Optional.of(environmentURI);
		} else {
			mongoDatabaseURI = ConfigurationHandler.getConfig("dev.db.url");
		}
		if (mongoDatabaseURI.isEmpty()) {
			return;
		}
		setSpecifiedMongoDB(mongoDatabaseURI.get());
	}
}