package cardswithfriends;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mongodb.BasicDBObject;

public class KCGameState extends GameState {
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
	protected void initializeToNewGameState(List<Player> players) {
		//Initialize game piles
		initializePiles();
		//Initialize user hands
		userHands = new HashMap<String, Pile>();
		for(Player p : players){
			userHands.put(Integer.toString(p.get_id()), new Pile(p.getUserName()+"'s Pile"));
		}
		
		Pile drawPile = piles.get(Integer.toString(PileIds.DRAW_PILE.ordinal()));
		
		//Deal cards to users
		for(int i = 0; i < GlobalConstants.INITIAL_NUM_CARDS; i++){
			for(Pile p : userHands.values()){
				p.add(drawPile.removeTop());
			}
		}
		
		//Add cards to initial piles
		addCardTo(piles.get(Integer.toString(PileIds.NORTH_PILE.ordinal())), piles.get(Integer.toString(PileIds.NORTH_WEST_PILE.ordinal())), drawPile);
		addCardTo(piles.get(Integer.toString(PileIds.EAST_PILE.ordinal())), piles.get(Integer.toString(PileIds.NORTH_EAST_PILE.ordinal())), drawPile);
		addCardTo(piles.get(Integer.toString(PileIds.SOUTH_PILE.ordinal())), piles.get(Integer.toString(PileIds.SOUTH_WEST_PILE.ordinal())), drawPile);
		addCardTo(piles.get(Integer.toString(PileIds.WEST_PILE.ordinal())), piles.get(Integer.toString(PileIds.SOUTH_EAST_PILE.ordinal())), drawPile);
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
		piles.put(Integer.toString(PileIds.DRAW_PILE.ordinal()), drawPile);
		piles.put(Integer.toString(PileIds.EAST_PILE.ordinal()), new Pile("East Pile"));
		piles.put(Integer.toString(PileIds.NORTH_PILE.ordinal()), new Pile("North Pile"));
		piles.put(Integer.toString(PileIds.SOUTH_PILE.ordinal()), new Pile("South Pile"));
		piles.put(Integer.toString(PileIds.WEST_PILE.ordinal()), new Pile("West Pile"));
		piles.put(Integer.toString(PileIds.NORTH_EAST_PILE.ordinal()), new Pile("Northeast Pile"));
		piles.put(Integer.toString(PileIds.NORTH_WEST_PILE.ordinal()), new Pile("Northwest Pile"));
		piles.put(Integer.toString(PileIds.SOUTH_EAST_PILE.ordinal()), new Pile("Southeast Pile"));
		piles.put(Integer.toString(PileIds.SOUTH_WEST_PILE.ordinal()), new Pile("Southwest Pile"));
	}
	
	/**
	 * Get only piles that all players should be allowed to see.
	 * Used for an AI to generate a move.
	 * @return
	 */
	public Map<Integer, Pile> getVisiblePiles() {
		Map<Integer, Pile> tablePiles = new HashMap<Integer, Pile>();
		tablePiles.put(PileIds.EAST_PILE.ordinal(), piles.get(Integer.toString(PileIds.EAST_PILE.ordinal())));
		tablePiles.put(PileIds.NORTH_PILE.ordinal(), piles.get(Integer.toString(PileIds.NORTH_PILE.ordinal())));
		tablePiles.put(PileIds.WEST_PILE.ordinal(), piles.get(Integer.toString(PileIds.WEST_PILE.ordinal())));
		tablePiles.put(PileIds.SOUTH_PILE.ordinal(), piles.get(Integer.toString(PileIds.SOUTH_PILE.ordinal())));
		tablePiles.put(PileIds.NORTH_EAST_PILE.ordinal(), piles.get(Integer.toString(PileIds.NORTH_EAST_PILE.ordinal())));
		tablePiles.put(PileIds.NORTH_WEST_PILE.ordinal(), piles.get(Integer.toString(PileIds.NORTH_WEST_PILE.ordinal())));
		tablePiles.put(PileIds.SOUTH_EAST_PILE.ordinal(), piles.get(Integer.toString(PileIds.SOUTH_EAST_PILE.ordinal())));
		tablePiles.put(PileIds.SOUTH_WEST_PILE.ordinal(), piles.get(Integer.toString(PileIds.SOUTH_WEST_PILE.ordinal())));
		return tablePiles;
	}
	
	public class Test{
		public void testInitializeToNewGameState(List<Player> players){
			initializeToNewGameState(players);
		}
	}
}
