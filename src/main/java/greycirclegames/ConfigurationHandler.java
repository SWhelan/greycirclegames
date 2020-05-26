package greycirclegames;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class ConfigurationHandler {
	
	private static Properties properties = null;
	
	private static Properties getProperties() {
		if (properties == null) {
			 try (InputStream file = new FileInputStream("config/local.properties")) {
				properties = new Properties();
				properties.load(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}
	
	public static Optional<String> getConfig(String key) {
		String value = getProperties().getProperty(key);
		return value == null ? Optional.empty() : Optional.of(value);
	}

	public static void main(String[] args) {
		
	}
	
}
