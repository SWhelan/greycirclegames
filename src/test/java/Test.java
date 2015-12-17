import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import greycirclegames.Card;
import greycirclegames.GlobalConstants;
import greycirclegames.KCGameState;
import greycirclegames.KCMove;
import greycirclegames.KingsCorner;
import greycirclegames.Move;
import greycirclegames.Pile;
import greycirclegames.PileIds;
import greycirclegames.Player;
import greycirclegames.User;
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
     * TEST Card
     */
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
    		Assert.isTrue(false, "Cannot make a card with number 0.");
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
    		Assert.isTrue(false, "Cannot make a card with number 14.");
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
    		Assert.isTrue(kc.userHands.get(Integer.toString(p.get_id())).size() == GlobalConstants.INITIAL_NUM_CARDS, "Initial hand should be size 7");
    		allCards.addAll(kc.userHands.get(Integer.toString(p.get_id())).getCards());
    	}
    	
    	int numCardsPlayed = 0;
    	for(Entry<String, Pile> e : kc.piles.entrySet()){
    		if(!e.getKey().equals(Integer.toString(PileIds.DRAW_PILE.ordinal()))){
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
    	moving.add(Card.make(10, Card.Suit.CLUB));
    	
    	Pile dest = new Pile("Destination");
    	dest.addOn(Card.make(13, Card.Suit.DIAMOND));
    	dest.addOn(Card.make(12, Card.Suit.SPADE));
    	
    	Move m = new KCMove(p, origin, moving, dest);
    	m.apply();
    	
    	Pile appliedOrigin = new Pile("Origin");
    	appliedOrigin.add(Card.make(8, Card.Suit.CLUB));
    	
    	Pile appliedDest = new Pile("Destination");
    	appliedDest.addOn(Card.make(13, Card.Suit.DIAMOND));
    	appliedDest.addOn(Card.make(12, Card.Suit.SPADE));
    	appliedDest.addOn(Card.make(11, Card.Suit.HEART));
    	appliedDest.addOn(Card.make(10, Card.Suit.CLUB));

    	Assert.isTrue(appliedDest.equals(dest), "Destination should have new card.");
    	Assert.isTrue(appliedOrigin.equals(origin), "Origin should not have moved card.");
    	try{
    		m.apply();
    		Assert.isTrue(false, "Should not be able to apply an invalid move.");
    	}catch(IllegalStateException e){
    		
    	}
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
    	KCGameState gs = (KCGameState) kc.getGameState();
    	
    	Pile drawPile = gs.piles.get(Integer.toString(PileIds.DRAW_PILE.ordinal()));
    	Card topCard = drawPile.getTop();
    	
    	Player currentPlayer = kc.getCurrentPlayerObject();
    	Pile currentPlayerHand = gs.userHands.get(Integer.toString(currentPlayer.get_id()));
    	Assert.isTrue(!currentPlayerHand.contains(topCard), "The card in the draw pile should not be in a player's hand.");
    	kc.endTurn();
    	Assert.isTrue(currentPlayerHand.size() == 8, "Ending a turn should draw a card.");
    	Assert.isTrue(currentPlayerHand.contains(topCard), "The top card in the draw pile should be drawn.");
    	Assert.isTrue(!currentPlayer.equals(kc.getCurrentPlayerObject()), "Current player should have been incremented.");
    }
    
    public void testKingsCornerApplyMove(){
    	List<Player> players = new LinkedList<Player>();
    	players.add(new User(0, "Test user 0"));
    	players.add(new User(1, "Test user 1"));
    	players.add(new User(2, "Test user 2"));
    	KingsCorner kc = new KingsCorner(0, players);
    	KCGameState gs = (KCGameState) kc.getGameState();
    	
    	Pile spoof = new Pile("Spoof North Pile");
    	spoof.addOn(Card.make(8, Card.Suit.CLUB));
    	gs.piles.put(Integer.toString(PileIds.NORTH_PILE.ordinal()), spoof);
    	Pile user0Hand = gs.userHands.get(Integer.toString(players.get(0).get_id()));
    	Card toMove = Card.make(7, Card.Suit.DIAMOND);
    	if(!user0Hand.contains(toMove)){
    		user0Hand.add(toMove);
    	}
    	
    	Pile moving = new Pile("Moving Pile");
    	moving.add(toMove);
    	
    	Move move = new KCMove(players.get(0), user0Hand, moving, spoof);
    	
    	Assert.isTrue(move.isValid(), "This should be a valid move.");
    	Assert.isTrue(kc.applyMove(move), "This is a valid move and the game should not be over, so return should be true.");
    	
    	Assert.isTrue(!user0Hand.contains(toMove), "Card should not be in user hand.");
    	Assert.isTrue(!user0Hand.containsAll(moving), "Card should not be in user hand.");
    	
    	Pile expected = new Pile("Expected pile.");
    	expected.addOn(Card.make(8, Card.Suit.CLUB));
    	expected.addOn(toMove);
    	Assert.isTrue(expected.equals(spoof), "We expect this pile.");
    	List<Move> moves = kc.getMoves();
    	Move mostRecent = moves.get(moves.size()-1);
    	
    	Assert.isTrue(mostRecent.getPlayerName().equals("Test user 0"), "Most recent's player is 0.");
    	Assert.isTrue(mostRecent.getOriginName().equals(user0Hand.getName()), "Most recent origin is user 0 hand.");
    	Assert.isTrue(mostRecent.getMovingName().equals("Moving Pile"), "Most recent moving pile.");
    	Assert.isTrue(mostRecent.getDestinationName().equals(spoof.getName()), "Most recent destination");
    }
    
    public void testKingsCornerApplyMoveInvalidMove(){
    	List<Player> players = new LinkedList<Player>();
    	players.add(new User(0, "Test user 0"));
    	players.add(new User(1, "Test user 1"));
    	players.add(new User(2, "Test user 2"));
    	KingsCorner kc = new KingsCorner(0, players);
    	KCGameState gs = (KCGameState) kc.getGameState();
    	
    	Pile spoof = new Pile("Spoof North Pile");
    	spoof.addOn(Card.make(10, Card.Suit.CLUB));
    	gs.piles.put(Integer.toString(PileIds.NORTH_PILE.ordinal()), spoof);
    	Pile user0Hand = gs.userHands.get(Integer.toString(players.get(0).get_id()));
    	Card toMove = Card.make(7, Card.Suit.DIAMOND);
    	if(!user0Hand.contains(toMove)){
    		user0Hand.add(toMove);
    	}
    	
    	Pile moving = new Pile("Moving Pile");
    	moving.add(toMove);
    	
    	Move move = new KCMove(players.get(0), user0Hand, moving, spoof);
    	
    	Assert.isTrue(!move.isValid(), "This should be an invalid move.");
    	Assert.isTrue(!kc.applyMove(move), "This is not a valid move, so return should be false.");
    }
    
    public void testKingsCornerGameOverConditions(){
    	List<Player> players = new LinkedList<Player>();
    	players.add(new User(0, "Test user 0"));
    	players.add(new User(1, "Test user 1"));
    	players.add(new User(2, "Test user 2"));
    	KingsCorner kc = new KingsCorner(0, players);
    	KCGameState gs = kc.getGameState();

    	Assert.isTrue(!kc.gameIsOver(), "The intial game should not be over");
    	Assert.isTrue(kc.getWinner() == null, "There is no winner.");
    	
    	gs.userHands.put(Integer.toString(players.get(0).get_id()), new Pile("Empty Pile"));
    	
    	Assert.isTrue(kc.gameIsOver(), "The game should be over");
    	Assert.isTrue(kc.getWinner().equals(players.get(0)), "The winner is player 0.");
    }
}
