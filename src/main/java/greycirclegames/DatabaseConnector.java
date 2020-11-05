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
	
	private MongoClient client;
	private MongoDatabase db;
	private MongoClientURI uri;
	
	private static final DatabaseConnector instance = new DatabaseConnector();

    private DatabaseConnector() {
    	
    }

    public static DatabaseConnector getInstance() {
        return instance;
    }
	
	public MongoDatabase getMongoDB() {
		return db;
	}
	
	/**
	 * Use this only when you need a specific database. To get the defalt see {@link DatabaseConnector#getMongoDB}
	 * @param url format driver://username:password@hostname.com:port/dbname
	 * @return MongoDatabase of that url
	 */
	private void setSpecifiedMongoDB(String url) {
		uri = new MongoClientURI(url + "?retryWrites=false"); // Additional parameter required after updating mongo driver. Probably should update mongo DB versions on heroku to match so that we don't need this query param.
		client = new MongoClient(uri);
		db = client.getDatabase(uri.getDatabase());
	}
	
	public void setTestDatabase() {
		if (db != null && client != null) {
			client.close();
		}
		setDatabase("TEST_MONGO", "test.db.url");
	}

	public void setDefaultDatabase() {
		setDatabase("PRODUCTION_MONGO", "dev.db.url");
	}
	
	private void setDatabase(String environmentVariable, String localConfigVariable) {
		Optional<String> mongoDatabaseURI = Optional.empty();
		ProcessBuilder processBuilder = new ProcessBuilder();
		String environmentURI = processBuilder.environment().get(environmentVariable);
		if (environmentURI != null) {
			mongoDatabaseURI = Optional.of(environmentURI);
		} else {
			mongoDatabaseURI = ConfigurationHandler.getConfig(localConfigVariable);
		}
		if (mongoDatabaseURI.isEmpty()) {
			return;
		}
		setSpecifiedMongoDB(mongoDatabaseURI.get());
	}
}