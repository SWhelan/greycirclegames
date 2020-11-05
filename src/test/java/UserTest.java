import greycirclegames.DatabaseConnector;
import greycirclegames.User;
import spark.utils.Assert;

public class UserTest {
    
    public void testHashPassword() {
    	DatabaseConnector.getInstance().setTestDatabase();
    	String test = User.hashPassword("salt", "password");
    	Assert.isTrue(test.equals(User.hashPassword("salt", "password")), "This method should return the same thing every time the same inputs are used.");
    	Assert.isTrue(!test.equals(User.hashPassword("salt2", "password")), "This method should return something different for every different input combination.");
    }
    
    public void testGenerateSalt() {
    	DatabaseConnector.getInstance().setTestDatabase();
    	Assert.isTrue(!User.generateSalt().equals(User.generateSalt()), "This method should return a different value on every call.");
    }
    
    public void testCheckPassword() {
    	DatabaseConnector.getInstance().setTestDatabase();
    	User user = new User();
    	user.setSalt(User.generateSalt());
    	user.setPassword(User.hashPassword(user.getSalt(), "password"));
    	Assert.isTrue(user.passwordMatches("password"), "Method should return true for password matches.");
    	Assert.isTrue(!user.passwordMatches("not the right password"), "Method should return false for incorrect passwords.");
    }  
    
}
