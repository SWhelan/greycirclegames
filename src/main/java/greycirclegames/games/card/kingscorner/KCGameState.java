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
		
		Pile drawPile = piles.get(KCPile.DRAW_PILE.getKey());
		
		//Deal cards to users
		for(int i = 0; i < GlobalConstants.INITIAL_NUM_CARDS; i++){
			for(Pile p : userHands.values()){
				p.add(drawPile.removeTop());
			}
		}
		
		//Add cards to initial piles
		addCardTo(piles.get(KCPile.NORTH_PILE.getKey()), piles.get(KCPile.NORTH_WEST_PILE.getKey()), drawPile);
		addCardTo(piles.get(KCPile.EAST_PILE.getKey()), piles.get(KCPile.NORTH_EAST_PILE.getKey()), drawPile);
		addCardTo(piles.get(KCPile.SOUTH_PILE.getKey()), piles.get(KCPile.SOUTH_WEST_PILE.getKey()), drawPile);
		addCardTo(piles.get(KCPile.WEST_PILE.getKey()), piles.get(KCPile.SOUTH_EAST_PILE.getKey()), drawPile);
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
		Pile drawPile = Pile.makeDeck(KCPile.DRAW_PILE.getPrettyName());
		Pile.shuffle(drawPile);
		piles.put(KCPile.DRAW_PILE.getKey(), drawPile);
		piles.put(KCPile.EAST_PILE.getKey(), new Pile(KCPile.EAST_PILE.getPrettyName()));
		piles.put(KCPile.NORTH_PILE.getKey(), new Pile(KCPile.NORTH_PILE.getPrettyName()));
		piles.put(KCPile.SOUTH_PILE.getKey(), new Pile(KCPile.SOUTH_PILE.getPrettyName()));
		piles.put(KCPile.WEST_PILE.getKey(), new Pile(KCPile.WEST_PILE.getPrettyName()));
		piles.put(KCPile.NORTH_EAST_PILE.getKey(), new Pile(KCPile.NORTH_EAST_PILE.getPrettyName()));
		piles.put(KCPile.NORTH_WEST_PILE.getKey(), new Pile(KCPile.NORTH_WEST_PILE.getPrettyName()));
		piles.put(KCPile.SOUTH_EAST_PILE.getKey(), new Pile(KCPile.SOUTH_EAST_PILE.getPrettyName()));
		piles.put(KCPile.SOUTH_WEST_PILE.getKey(), new Pile(KCPile.SOUTH_WEST_PILE.getPrettyName()));
	}
	
	/**
	 * Get only piles that all players should be allowed to see.
	 * Used for an AI to generate a move.
	 * @return
	 */
	public Map<Integer, Pile> getVisiblePiles() {
		Map<Integer, Pile> tablePiles = new HashMap<Integer, Pile>();
		tablePiles.put(KCPile.EAST_PILE.ordinal(), piles.get(KCPile.EAST_PILE.getKey()));
		tablePiles.put(KCPile.NORTH_PILE.ordinal(), piles.get(KCPile.NORTH_PILE.getKey()));
		tablePiles.put(KCPile.WEST_PILE.ordinal(), piles.get(KCPile.WEST_PILE.getKey()));
		tablePiles.put(KCPile.SOUTH_PILE.ordinal(), piles.get(KCPile.SOUTH_PILE.getKey()));
		tablePiles.put(KCPile.NORTH_EAST_PILE.ordinal(), piles.get(KCPile.NORTH_EAST_PILE.getKey()));
		tablePiles.put(KCPile.NORTH_WEST_PILE.ordinal(), piles.get(KCPile.NORTH_WEST_PILE.getKey()));
		tablePiles.put(KCPile.SOUTH_EAST_PILE.ordinal(), piles.get(KCPile.SOUTH_EAST_PILE.getKey()));
		tablePiles.put(KCPile.SOUTH_WEST_PILE.ordinal(), piles.get(KCPile.SOUTH_WEST_PILE.getKey()));
		return tablePiles;
	}

}
