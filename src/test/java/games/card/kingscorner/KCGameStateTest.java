package games.card.kingscorner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import greycirclegames.DatabaseConnector;
import greycirclegames.GlobalConstants;
import greycirclegames.games.card.Card;
import greycirclegames.games.card.Pile;
import greycirclegames.games.card.kingscorner.KCGameState;
import greycirclegames.games.card.kingscorner.KCPile;
import greycirclegames.games.card.kingscorner.KingsCorner;
import spark.utils.Assert;

public class KCGameStateTest {
	private static final List<Integer> players = new ArrayList<Integer>(Arrays.asList(-1, -2, -3));
	
    public void testInit() {
    	DatabaseConnector.getInstance().setTestDatabase();
    }
    
	public void testKCGameStateInitializeToNewGame(){    
		KCGameState gameState = new KCGameState();
		KingsCorner game = new KingsCorner(43, players);
		gameState.initializeToNewGameState(game, players);

		Set<Card> allCards = new HashSet<Card>();

		for(Integer p: players){
			Assert.isTrue(gameState.userHands.get(Integer.toString(p)).size() == GlobalConstants.INITIAL_NUM_CARDS, "Initial hand should be size 7");
			allCards.addAll(gameState.userHands.get(Integer.toString(p)).getCards());
		}

		int numCardsPlayed = 0;
		for(Entry<String, Pile> e : gameState.piles.entrySet()){
			if(!e.getKey().equals(KCPile.DRAW_PILE.getKey())){
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

	public void testGetVisiblePiles(){
		KCGameState gameState = new KCGameState();
		KingsCorner game = new KingsCorner(44, players);
		gameState.initializeToNewGameState(game, players);
		Map<Integer, Pile> visible = gameState.getVisiblePiles();
		Assert.isTrue(visible.size() == 8, "There are 9 piles that should be visible.");
	}
}
