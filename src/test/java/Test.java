import java.util.HashSet;
import java.util.List;
import java.util.Set;

import greycirclegames.User;
import greycirclegames.games.card.Card;
import greycirclegames.games.card.Pile;
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
    	Assert.isTrue(user.passwordMatches("password"), "Method should return true for password matches.");
    	Assert.isTrue(!user.passwordMatches("not the right password"), "Method should return false for incorrect passwords.");
    }  
    
    /*
     * TEST Pile
     */
    public void testPileMakeDeck(){
    	Pile p = Pile.makeDeck("Test Deck");
    	
    	Assert.isTrue(p.size()==52, "Deck should be 52 cards");
    	
    	List<Card> cards = p.getCards();
    	Set<Card> cardSet = new HashSet<Card>(cards);
    	Assert.isTrue(cardSet.size()==52, "Deck should be 52 unique cards");
    }
    
}
