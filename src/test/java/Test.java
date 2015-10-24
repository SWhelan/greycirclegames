import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import cardswithfriends.*;
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
    
    /*
     * TEST Card
     */
    public void testCardNumberTooLow(){
    	try{
    		Card.make(0, Card.Suit.DIAMOND);
    		Assert.isTrue(false, "Cannot make a card with number 0.");
    	}catch(IllegalArgumentException e){
    		
    	}
    }
    
    public void testCardNumberTooHigh(){
    	try{
    		Card.make(14, Card.Suit.DIAMOND);
    		Assert.isTrue(false, "Cannot make a card with number 14.");
    	}catch(IllegalArgumentException e){
    		
    	}
    }
    
    /*
     * TEST Pile
     */
    public void testPileMakeDeck(){
    	Pile p = Pile.makeDeck("Test Deck");
    	Assert.isTrue(p.size()==52, "Deck should be 52 cards");
    }
    
    /*
     * TEST KCGameState
     */
    
    public void testKCGameStateInitializeToNewGame(){
    	KCGameState kc = new KCGameState();
    	KCGameState.Test kct = kc.new Test();
    	List<Player> players = new LinkedList<Player>();
    	players.add(new User(0, "Test user 0"));
    	players.add(new User(1, "Test user 1"));
    	players.add(new User(2, "Test user 2"));
    	kct.testInitializeToNewGameState(players);
    	
    	Set<Card> allCards = new HashSet<Card>();
    	
    	//Test user hands
    	for(Player p: players){
    		Assert.isTrue(kc.userHands.get(p).size() == 7, "Initial hand should be size 7");
    		allCards.addAll(kc.userHands.get(p).getCards());
    	}
    	
    	int numCardsPlayed = 0;
    	for(Entry<Integer, Pile> e : kc.piles.entrySet()){
    		if(e.getKey() != PileIds.DRAW_PILE.getId()){
    			if(e.getValue().size() == 1){
    				numCardsPlayed++;
    			}else if(e.getValue().size() != 0){
    				Assert.isTrue(false, "Initial card pile should be only 1 or 0 size.");
    			}
    		}
    		allCards.addAll(e.getValue().getCards());
    	}
    	Assert.isTrue(numCardsPlayed == 4, "Four cards should be played.");
    	
    	Assert.isTrue(allCards.size() == 52, "All cards should be somewhere.");
    }
    
    /*
     * TEST KCMove
     */
    
    public void testKCMoveIsValidGoodData(){
    	Player p = new User(0, "Player");
    	Pile origin = new Pile("Origin");
    	origin.add(Card.make(10, Card.Suit.CLUB));
    	origin.add(Card.make(11, Card.Suit.HEART));
    	origin.add(Card.make(8, Card.Suit.CLUB));
    	
    	Pile moving = new Pile("Moving");
    	moving.add(Card.make(11, Card.Suit.HEART));
    	
    	Pile dest = new Pile("Destination");
    	dest.addOn(Card.make(13, Card.Suit.DIAMOND));
    	dest.addOn(Card.make(12, Card.Suit.SPADE));
    	
    	Move m = new KCMove(p, origin, moving, dest);
    	Assert.isTrue(m.isValid(), "This should be a valid move.");
    }
    
    public void testKCMoveIsValidBadDataOriginNotContains(){
    	Player p = new User(0, "Player");
    	Pile origin = new Pile("Origin");
    	origin.add(Card.make(10, Card.Suit.CLUB));
    	//origin.add(Card.make(11, Card.Suit.HEART));
    	origin.add(Card.make(8, Card.Suit.CLUB));
    	
    	Pile moving = new Pile("Moving");
    	moving.add(Card.make(11, Card.Suit.HEART));
    	
    	Pile dest = new Pile("Destination");
    	dest.addOn(Card.make(13, Card.Suit.DIAMOND));
    	dest.addOn(Card.make(12, Card.Suit.SPADE));
    	
    	Move m = new KCMove(p, origin, moving, dest);
    	Assert.isTrue(!m.isValid(), "This should not be a valid move.");
    }
    
    public void testKCMoveIsValidBadDataCardsNotCompatible(){
    	Player p = new User(0, "Player");
    	Pile origin = new Pile("Origin");
    	origin.add(Card.make(10, Card.Suit.CLUB));
    	origin.add(Card.make(11, Card.Suit.HEART));
    	origin.add(Card.make(8, Card.Suit.CLUB));
    	
    	Pile moving = new Pile("Moving");
    	moving.add(Card.make(11, Card.Suit.HEART));
    	
    	Pile dest = new Pile("Destination");
    	dest.addOn(Card.make(13, Card.Suit.SPADE));
    	dest.addOn(Card.make(12, Card.Suit.DIAMOND));
    	
    	Move m = new KCMove(p, origin, moving, dest);
    	Assert.isTrue(!m.isValid(), "This should not be a valid move.");
    }
    
    public void testKCMoveApply(){
    	Player p = new User(0, "Player");
    	Pile origin = new Pile("Origin");
    	origin.add(Card.make(10, Card.Suit.CLUB));
    	origin.add(Card.make(11, Card.Suit.HEART));
    	origin.add(Card.make(8, Card.Suit.CLUB));
    	
    	Pile moving = new Pile("Moving");
    	moving.add(Card.make(11, Card.Suit.HEART));
    	
    	Pile dest = new Pile("Destination");
    	dest.addOn(Card.make(13, Card.Suit.DIAMOND));
    	dest.addOn(Card.make(12, Card.Suit.SPADE));
    	
    	Move m = new KCMove(p, origin, moving, dest);
    	m.apply();
    	
    	Pile appliedOrigin = new Pile("Origin");
    	appliedOrigin.add(Card.make(10, Card.Suit.CLUB));
    	appliedOrigin.add(Card.make(8, Card.Suit.CLUB));
    	
    	Pile appliedDest = new Pile("Destination");
    	appliedDest.addOn(Card.make(13, Card.Suit.DIAMOND));
    	appliedDest.addOn(Card.make(12, Card.Suit.SPADE));
    	appliedDest.addOn(Card.make(11, Card.Suit.HEART));

    	Assert.isTrue(appliedDest.equals(dest), "Destination should have new card.");
    	Assert.isTrue(appliedOrigin.equals(origin), "Origin should not have moved card.");
    }
    
    /*
     * TEST KingsCorner
     */
    
    public void testKingsCornerEndTurn(){
    	List<Player> players = new LinkedList<Player>();
    	players.add(new User(0, "Test user 0"));
    	players.add(new User(1, "Test user 1"));
    	players.add(new User(2, "Test user 2"));
    	KingsCorner kc = new KingsCorner(0, players);
    	GameState gs = kc.getGameState();
    	
    	Player currentPlayer = kc.getCurrentPlayer();
    	kc.endTurn();
    	Pile prevPlayerHand = gs.userHands.get(currentPlayer);
    	Assert.isTrue(prevPlayerHand.size() == 8, "Ending a turn should draw a card.");
    	
    	
    }
}
