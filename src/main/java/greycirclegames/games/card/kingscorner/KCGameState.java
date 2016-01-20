package greycirclegames.games.card.kingscorner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mongodb.BasicDBObject;

import greycirclegames.ArtificialPlayer;
import greycirclegames.DBHandler;
import greycirclegames.GlobalConstants;
import greycirclegames.Player;
import greycirclegames.games.Game;
import greycirclegames.games.GameState;
import greycirclegames.games.Move;
import greycirclegames.games.card.Card;
import greycirclegames.games.card.CardBasedGameState;
import greycirclegames.games.card.Pile;

public class KCGameState extends CardBasedGameState {
	/**
	 * The non-user piles
	 */
	public Map<String, Pile> piles;
	
	public Map<String, Pile> getPiles() {
		return piles;
	}
	public void setPiles(Map<String, Pile> piles) {
		this.piles = piles;
	}

	//Initialize to new gamestate
	public KCGameState() {
		super();
	}

	/**
	 * Initialize to a Game State that was saved in the database.
	 * @param obj A DBObject containing all the necessary info for this game state.
	 */
	public KCGameState(BasicDBObject obj) {
		
		BasicDBObject piles = (BasicDBObject)obj.get("Piles");
		this.piles = new HashMap<String, Pile>();
		for (Entry<String, Object> entry : piles.entrySet()) {
			this.piles.put(entry.getKey(), new Pile((BasicDBObject)entry.getValue()));
		}
		
		BasicDBObject userHands = (BasicDBObject)obj.get("UserHands");
		this.userHands = new HashMap<String, Pile>();
		for (Entry<String, Object> entry : userHands.entrySet()) {
			this.userHands.put(entry.getKey(), new Pile((BasicDBObject)entry.getValue()));
		}
		
		this.turnNumber = (Integer)obj.get("TurnNumber");
	}
	
	public static KingsCorner getKCGame(int gameID) {
		return DBHandler.getKCGame(gameID);
	}

	@Override
	public void initializeToNewGameState(Game<? extends Move, ? extends GameState, ? extends ArtificialPlayer> game, List<Integer> players) {
		//Initialize game piles
		initializePiles();
		//Initialize user hands
		userHands = new HashMap<String, Pile>();
		for(Integer id : players){
			Player player = null;
			if(id < 0){
				player = ((KingsCorner)game).makeArtificialPlayerFromDB(id);
			} else {
				player = DBHandler.getUser(id);
			}
			userHands.put(Integer.toString(player.get_id()), new Pile(player.getUsername()+"'s Pile"));
		}
		
		Pile drawPile = piles.get(KCPileIds.DRAW_PILE.getKey());
		
		//Deal cards to users
		for(int i = 0; i < GlobalConstants.INITIAL_NUM_CARDS; i++){
			for(Pile p : userHands.values()){
				p.add(drawPile.removeTop());
			}
		}
		
		//Add cards to initial piles
		addCardTo(piles.get(KCPileIds.NORTH_PILE.getKey()), piles.get(KCPileIds.NORTH_WEST_PILE.getKey()), drawPile);
		addCardTo(piles.get(KCPileIds.EAST_PILE.getKey()), piles.get(KCPileIds.NORTH_EAST_PILE.getKey()), drawPile);
		addCardTo(piles.get(KCPileIds.SOUTH_PILE.getKey()), piles.get(KCPileIds.SOUTH_WEST_PILE.getKey()), drawPile);
		addCardTo(piles.get(KCPileIds.WEST_PILE.getKey()), piles.get(KCPileIds.SOUTH_EAST_PILE.getKey()), drawPile);
	}

	private void addCardTo(Pile notKing, Pile isKing, Pile drawPile) {
		Card c = drawPile.removeTop();
		//If the card is a king
		if(c.getNumber() != GlobalConstants.KING){
			notKing.add(c);
		}else{
			isKing.add(c);
		}
	}

	//Put empty piles for each game pile, except the draw pile which contains a full, shuffled deck.
	private void initializePiles() {
		piles = new HashMap<String, Pile>();
		Pile drawPile = Pile.makeDeck("Draw Pile");
		Pile.shuffle(drawPile);
		piles.put(KCPileIds.DRAW_PILE.getKey(), drawPile);
		piles.put(KCPileIds.EAST_PILE.getKey(), new Pile("East Pile"));
		piles.put(KCPileIds.NORTH_PILE.getKey(), new Pile("North Pile"));
		piles.put(KCPileIds.SOUTH_PILE.getKey(), new Pile("South Pile"));
		piles.put(KCPileIds.WEST_PILE.getKey(), new Pile("West Pile"));
		piles.put(KCPileIds.NORTH_EAST_PILE.getKey(), new Pile("Northeast Pile"));
		piles.put(KCPileIds.NORTH_WEST_PILE.getKey(), new Pile("Northwest Pile"));
		piles.put(KCPileIds.SOUTH_EAST_PILE.getKey(), new Pile("Southeast Pile"));
		piles.put(KCPileIds.SOUTH_WEST_PILE.getKey(), new Pile("Southwest Pile"));
	}
	
	/**
	 * Get only piles that all players should be allowed to see.
	 * Used for an AI to generate a move.
	 * @return
	 */
	public Map<Integer, Pile> getVisiblePiles() {
		Map<Integer, Pile> tablePiles = new HashMap<Integer, Pile>();
		tablePiles.put(KCPileIds.EAST_PILE.ordinal(), piles.get(KCPileIds.EAST_PILE.getKey()));
		tablePiles.put(KCPileIds.NORTH_PILE.ordinal(), piles.get(KCPileIds.NORTH_PILE.getKey()));
		tablePiles.put(KCPileIds.WEST_PILE.ordinal(), piles.get(KCPileIds.WEST_PILE.getKey()));
		tablePiles.put(KCPileIds.SOUTH_PILE.ordinal(), piles.get(KCPileIds.SOUTH_PILE.getKey()));
		tablePiles.put(KCPileIds.NORTH_EAST_PILE.ordinal(), piles.get(KCPileIds.NORTH_EAST_PILE.getKey()));
		tablePiles.put(KCPileIds.NORTH_WEST_PILE.ordinal(), piles.get(KCPileIds.NORTH_WEST_PILE.getKey()));
		tablePiles.put(KCPileIds.SOUTH_EAST_PILE.ordinal(), piles.get(KCPileIds.SOUTH_EAST_PILE.getKey()));
		tablePiles.put(KCPileIds.SOUTH_WEST_PILE.ordinal(), piles.get(KCPileIds.SOUTH_WEST_PILE.getKey()));
		return tablePiles;
	}
	
	/*public class Test{
		public void testInitializeToNewGameState(Game<? extends Move, ? extends GameState, ? extends ArtificialPlayer> game, List<Integer> players){
			initializeToNewGameState(game, players);
		}
	}*/
}
