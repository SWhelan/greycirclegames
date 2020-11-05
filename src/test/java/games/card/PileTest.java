package games.card;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import greycirclegames.games.card.Card;
import greycirclegames.games.card.Pile;
import spark.utils.Assert;

public class PileTest {
	
    public void testPileMakeDeck() {
    	Pile p = Pile.makeDeck("Test Deck");
    	
    	Assert.isTrue(p.size() == 52, "Deck should be 52 cards");
    	
    	List<Card> cards = p.getCards();
    	Set<Card> cardSet = new HashSet<Card>(cards);
    	Assert.isTrue(cardSet.size() == 52, "Deck should be 52 unique cards");
    }
}
