package games.card;

import greycirclegames.DatabaseConnector;
import greycirclegames.games.card.Card;
import spark.utils.Assert;

public class CardTest {
	
    public void testInit() {
    	DatabaseConnector.getInstance().setTestDatabase();
    }
	
    public void testCardNumberTooLow(){
    	//Zero case
    	try{
    		Card.make(0, Card.Suit.DIAMOND);
    		Assert.isTrue(false, "Cannot make a card with number 0.");
    	}catch(IllegalArgumentException e){
    		
    	}
    	//Negative case
    	try{
    		Card.make(-1, Card.Suit.DIAMOND);
    		Assert.isTrue(false, "Cannot make a card with number -1.");
    	}catch(IllegalArgumentException e){
    		
    	}
    }
    
    public void testCardNumberTooHigh(){
    	//14 case
    	try{
    		Card.make(14, Card.Suit.DIAMOND);
    		Assert.isTrue(false, "Cannot make a card with number 14.");
    	}catch(IllegalArgumentException e){
    		
    	}
    	//15 case
    	try{
    		Card.make(15, Card.Suit.DIAMOND);
    		Assert.isTrue(false, "Cannot make a card with number 15.");
    	}catch(IllegalArgumentException e){
    		
    	}
    }
    
    public void testCardNullSuit(){
    	try{
    		Card.make(1, null);
    		Assert.isTrue(false, "Cannot make a card with number 0.");
    	}catch(IllegalArgumentException e){
    		
    	}
    }
    
    public void testCardNominal(){
    	Card t = Card.make(10, Card.Suit.SPADE);
    	Assert.isTrue(t.getNumber() == 10, "Card should have value 10.");
    	Assert.isTrue(t.getSuit() == Card.Suit.SPADE, "Card should be spade.");
    }
    
    public void testCardIsRed(){
    	Card spade = Card.make(10, Card.Suit.SPADE);
    	Card club = Card.make(10, Card.Suit.CLUB);
    	Card diamond = Card.make(10, Card.Suit.DIAMOND);
    	Card heart = Card.make(10, Card.Suit.HEART);
    	
    	Assert.isTrue(diamond.isRed(), "Diamond is red");
    	Assert.isTrue(heart.isRed(), "Heart is red.");
    	Assert.isTrue(!spade.isRed(), "Spade is not red.");
    	Assert.isTrue(!club.isRed(), "Club is not red.");
    }
	
}
