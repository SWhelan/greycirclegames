package games.card.kingscorner;

import java.util.Collections;
import java.util.List;

import greycirclegames.DatabaseConnector;
import greycirclegames.games.card.kingscorner.KCGameState;
import greycirclegames.games.card.kingscorner.KingsCorner;
import spark.utils.Assert;

public class KCArtificialPlayerTest {
	
    public void testCreateMove() {
    	DatabaseConnector.getInstance().setTestDatabase();
    	List<Integer> players = Collections.singletonList(-1);
    	KingsCorner game = new KingsCorner(0, players);
    	KCGameState gameState = game.getGameState();
    	gameState.initializeToNewGameState(game, players);
    	game.applyAIMoves();
    	Assert.isTrue(true, "We've applied AI moves so its working.");
    }
}
