package games.card.kingscorner;

import greycirclegames.ArtificialPlayer;
import greycirclegames.DatabaseConnector;
import greycirclegames.games.card.Card;
import greycirclegames.games.card.CardBasedMove;
import greycirclegames.games.card.Pile;
import greycirclegames.games.card.kingscorner.KCMove;
import greycirclegames.games.card.kingscorner.KCPile;
import spark.utils.Assert;

public class KCMoveTest {
	
	public void testKCMoveIsValidGoodData() {
    	DatabaseConnector.getInstance().setTestDatabase();
		Pile origin = new Pile("Origin");
		origin.add(Card.make(10, Card.Suit.CLUB));
		origin.add(Card.make(11, Card.Suit.HEART));
		origin.add(Card.make(8, Card.Suit.CLUB));

		Pile moving = new Pile("Moving");
		moving.add(Card.make(11, Card.Suit.HEART));

		Pile dest = new Pile("Destination");
		dest.addOne(Card.make(13, Card.Suit.DIAMOND));
		dest.addOne(Card.make(12, Card.Suit.SPADE));

		CardBasedMove m = new KCMove(-1, origin, moving, dest);
		Assert.isTrue(m.isValid(), "This should be a valid move.");
	}

	public void testKCMoveIsValidBadDataOriginNotContains() {
    	DatabaseConnector.getInstance().setTestDatabase();
		Pile origin = new Pile("Origin");
		origin.add(Card.make(10, Card.Suit.CLUB));
		//origin.add(Card.make(11, Card.Suit.HEART));
		origin.add(Card.make(8, Card.Suit.CLUB));

		Pile moving = new Pile("Moving");
		moving.add(Card.make(11, Card.Suit.HEART));

		Pile dest = new Pile("Destination");
		dest.addOne(Card.make(13, Card.Suit.DIAMOND));
		dest.addOne(Card.make(12, Card.Suit.SPADE));

		CardBasedMove m = new KCMove(-1, origin, moving, dest);
		Assert.isTrue(!m.isValid(), "This should not be a valid move.");
	}

	public void testKCMoveIsValidBadDataCardsNotCompatible() {
    	DatabaseConnector.getInstance().setTestDatabase();
		Pile origin = new Pile("Origin");
		origin.add(Card.make(10, Card.Suit.CLUB));
		origin.add(Card.make(11, Card.Suit.HEART));
		origin.add(Card.make(8, Card.Suit.CLUB));

		Pile moving = new Pile("Moving");
		moving.add(Card.make(11, Card.Suit.HEART));

		Pile dest = new Pile("Destination");
		dest.addOne(Card.make(13, Card.Suit.SPADE));
		dest.addOne(Card.make(12, Card.Suit.DIAMOND));

		CardBasedMove m = new KCMove(-1, origin, moving, dest);
		Assert.isTrue(!m.isValid(), "This should not be a valid move.");
	}
	
	public void testKCMoveIsValidDrawPile() {
    	DatabaseConnector.getInstance().setTestDatabase();
		Pile origin = new Pile(KCPile.DRAW_PILE.getPrettyName());
		origin.add(Card.make(10, Card.Suit.CLUB));
		origin.add(Card.make(11, Card.Suit.HEART));
		origin.add(Card.make(8, Card.Suit.CLUB));

		Pile moving = new Pile("Moving");
		moving.add(Card.make(8, Card.Suit.CLUB));
		
		Pile dest = new Pile("Destination");
		dest.addOne(Card.make(13, Card.Suit.SPADE));
		dest.addOne(Card.make(12, Card.Suit.DIAMOND));

		int originalLength = dest.size();
		KCMove m = new KCMove(-1, origin, moving, dest);
		m.apply();
		Assert.isTrue(dest.size() == originalLength + 1, "This should add one card to the destination.");
	}
	
	public void testKCMoveToEmpty() {
    	DatabaseConnector.getInstance().setTestDatabase();
		Pile origin = new Pile("Origin");
		origin.add(Card.make(10, Card.Suit.CLUB));
		origin.add(Card.make(11, Card.Suit.HEART));
		origin.add(Card.make(8, Card.Suit.CLUB));

		Pile moving = new Pile("Moving");
		moving.add(Card.make(11, Card.Suit.HEART));

		Pile dest = new Pile(KCPile.NORTH_PILE.getPrettyName());

		KCMove m = new KCMove(-1, origin, moving, dest);
		Assert.isTrue(m.isValid(), "This move should be allowed.");
	}

	public void testKCMoveToEmptyCornerValid() {
    	DatabaseConnector.getInstance().setTestDatabase();
		Pile origin = new Pile("Origin");
		origin.add(Card.make(11, Card.Suit.HEART));
		origin.add(Card.make(8, Card.Suit.CLUB));
		origin.add(Card.make(13, Card.Suit.CLUB));

		Pile moving = new Pile("Moving");
		moving.add(Card.make(13, Card.Suit.CLUB));

		Pile dest = new Pile(KCPile.SOUTH_EAST_PILE.getPrettyName());

		KCMove m = new KCMove(-1, origin, moving, dest);
		Assert.isTrue(m.isValid(), "This move should be allowed.");
	}
	
	public void testKCMoveToEmptyCornerInvalid(){
		Pile origin = new Pile("Origin");
		origin.add(Card.make(10, Card.Suit.CLUB));
		origin.add(Card.make(11, Card.Suit.HEART));
		origin.add(Card.make(8, Card.Suit.CLUB));

		Pile moving = new Pile("Moving");
		moving.add(Card.make(11, Card.Suit.HEART));

		Pile dest = new Pile(KCPile.NORTH_EAST_PILE.getPrettyName());

		KCMove m = new KCMove(-1, origin, moving, dest);
		Assert.isTrue(!m.isValid(), "This move should be allowed.");
	}
	
	public void testToStringSimple() {
    	DatabaseConnector.getInstance().setTestDatabase();
		Pile origin = new Pile("Origin");
		origin.add(Card.make(10, Card.Suit.CLUB));
		origin.add(Card.make(11, Card.Suit.HEART));
		origin.add(Card.make(8, Card.Suit.CLUB));

		Pile moving = new Pile("Moving");
		Card card = Card.make(11, Card.Suit.HEART);
		moving.add(card);
		
		
		Pile dest = new Pile(KCPile.NORTH_EAST_PILE.getPrettyName());

		KCMove m = new KCMove(-1, origin, moving, dest);
		String actual = m.toString();
		String expected = ArtificialPlayer.getDefaultUsername(-1) + " moved " + card.toString() + " from " + origin.getName() + " onto " + dest.getName();  
		Assert.isTrue(actual.equals(expected), "The toString method should be properly formatted.");
	}
	
	public void testToStringComplicated() {
    	DatabaseConnector.getInstance().setTestDatabase();
		Card card1 = Card.make(11, Card.Suit.HEART);
		Card card2 = Card.make(8, Card.Suit.CLUB); 
		
		Pile origin = new Pile("Origin");
		origin.add(Card.make(10, Card.Suit.CLUB));
		origin.add(card1);
		origin.add(card2);

		Pile moving = new Pile("Moving");
		moving.add(card1);
		moving.add(card2);
		
		
		Pile dest = new Pile(KCPile.NORTH_EAST_PILE.getPrettyName());

		KCMove m = new KCMove(-1, origin, moving, dest);
		String actual = m.toString();
		String expected = ArtificialPlayer.getDefaultUsername(-1) + " moved " + card1.toString() + " and " + card2.toString() + " from " + origin.getName() + " onto " + dest.getName();
		Assert.isTrue(actual.equals(expected), "The toString method should be properly formatted.");
	}
	

	public void testToStringFromDrawPile() {
    	DatabaseConnector.getInstance().setTestDatabase();
		Card card = Card.make(8, Card.Suit.CLUB);
		Pile origin = new Pile(KCPile.DRAW_PILE.getPrettyName());
		origin.add(Card.make(10, Card.Suit.CLUB));
		origin.add(Card.make(11, Card.Suit.HEART));
		origin.add(card);

		Pile moving = new Pile("Moving");
		moving.add(card);
		
		Pile dest = new Pile("Destination");
		dest.addOne(Card.make(13, Card.Suit.SPADE));
		dest.addOne(Card.make(12, Card.Suit.DIAMOND));

		KCMove m = new KCMove(-1, origin, moving, dest);
		String actual = m.toString();
		String expected = ArtificialPlayer.getDefaultUsername(-1) + " moved a card from " + origin.getName() + " onto " + dest.getName();
		Assert.isTrue(actual.equals(expected), "This should add one card to the destination.");
	}
	

	public void testKCMoveApply() {
    	DatabaseConnector.getInstance().setTestDatabase();
		Pile origin = new Pile("Origin");
		origin.add(Card.make(10, Card.Suit.CLUB));
		origin.add(Card.make(11, Card.Suit.HEART));
		origin.add(Card.make(8, Card.Suit.CLUB));

		Pile moving = new Pile("Moving");
		moving.add(Card.make(11, Card.Suit.HEART));
		moving.add(Card.make(10, Card.Suit.CLUB));

		Pile dest = new Pile("Destination");
		dest.addOne(Card.make(13, Card.Suit.DIAMOND));
		dest.addOne(Card.make(12, Card.Suit.SPADE));

		CardBasedMove m = new KCMove(-1, origin, moving, dest);
		m.apply();

		Pile appliedOrigin = new Pile("Origin");
		appliedOrigin.add(Card.make(8, Card.Suit.CLUB));

		Pile appliedDest = new Pile("Destination");
		appliedDest.addOne(Card.make(13, Card.Suit.DIAMOND));
		appliedDest.addOne(Card.make(12, Card.Suit.SPADE));
		appliedDest.addOne(Card.make(11, Card.Suit.HEART));
		appliedDest.addOne(Card.make(10, Card.Suit.CLUB));

		Assert.isTrue(appliedDest.equals(dest), "Destination should have new card.");
		Assert.isTrue(appliedOrigin.equals(origin), "Origin should not have moved card.");
		try {
			m.apply();
			Assert.isTrue(false, "Should not be able to apply an invalid move.");
		} catch(IllegalStateException e) {

		}
	}
}
