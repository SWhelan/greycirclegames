import cardswithfriends.User;
import spark.utils.Assert;

public class Test {
    public void testing() {
		Assert.isTrue(true, "The first test!");
    }
    
    public void testHashPassword(){
    	String test = User.hashPassword("salt", "password");
    	Assert.isTrue(test.equals(User.hashPassword("salt", "password")), "This method should return the same thing every time the same inputs are used.");
    	Assert.isTrue(!test.equals(User.hashPassword("salt2", "password")), "This method should return something different for every different input combination.");
    }
    
    public void testGenerateSalt(){
    	Assert.isTrue(!User.generateSalt().equals(User.generateSalt()), "This method should return a different value on every call.");
    }
    
    public void testCheckPassword(){
    	User user = new User(1, "Test");
    	user.setSalt(User.generateSalt());
    	user.setPassword(User.hashPassword(user.getSalt(), "password"));
    	Assert.isTrue(user.checkPassword("password"), "Method should return true for password matches.");
    	Assert.isTrue(!user.checkPassword("not the right password"), "Method should return false for incorrect passwords.");
    }
}
