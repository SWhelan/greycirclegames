package cardswithfriends;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KCGameState extends GameState {
	private static final long serialVersionUID = 1L;
	public Map<Integer, Pile> piles;
	
	public KCGameState(KCGameStateGenerator kc){
		//Initialize to a pre-existing game
	}

	//Initialize to new gamestate
	public KCGameState() {
		//Initialize to a new game
		super();
	}

	public Map<Player, Pile> getPlayerHands() {
		return userHands;
	}

	public void setPlayerHands(Map<Player, Pile> userHands) {
		this.userHands = userHands;
	}

	public static class KCGameStateGenerator{
	}
	
	public static KingsCorner getKCGame(int gameID) {
		return DBHandler.getKCGame(gameID);
	}

	@Override
	protected void initializeToNewGameState(List<Player> players) {
		initializePiles();
		userHands = new HashMap<Player, Pile>();
		for(Player p : players){
			userHands.put(p, new Pile(p.getUserName()+"'s Pile"));
		}
		
		Pile drawPile = piles.get(PileIds.DRAW_PILE.ordinal());
		
		//Deal cards to users
		for(int i = 0; i < GlobalConstants.MAX_PLAYERS; i++){
			for(Pile p : userHands.values()){
				p.add(drawPile.removeTop());
			}
		}
		
		//Add cards to initial piles
		addCardTo(piles.get(PileIds.NORTH_PILE.ordinal()), piles.get(PileIds.NORTH_WEST_PILE.ordinal()), drawPile);
		addCardTo(piles.get(PileIds.EAST_PILE.ordinal()), piles.get(PileIds.NORTH_EAST_PILE.ordinal()), drawPile);
		addCardTo(piles.get(PileIds.SOUTH_PILE.ordinal()), piles.get(PileIds.SOUTH_WEST_PILE.ordinal()), drawPile);
		addCardTo(piles.get(PileIds.WEST_PILE.ordinal()), piles.get(PileIds.SOUTH_EAST_PILE.ordinal()), drawPile);
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

	private void initializePiles() {
		piles = new HashMap<Integer, Pile>();
		Pile drawPile = Pile.makeDeck("Draw Pile");
		Pile.shuffle(drawPile);
		piles.put(PileIds.DRAW_PILE.ordinal(), drawPile);
		piles.put(PileIds.EAST_PILE.ordinal(), new Pile("East Pile"));
		piles.put(PileIds.NORTH_PILE.ordinal(), new Pile("North Pile"));
		piles.put(PileIds.SOUTH_PILE.ordinal(), new Pile("South Pile"));
		piles.put(PileIds.WEST_PILE.ordinal(), new Pile("West Pile"));
		piles.put(PileIds.NORTH_EAST_PILE.ordinal(), new Pile("Northeast Pile"));
		piles.put(PileIds.NORTH_WEST_PILE.ordinal(), new Pile("Northwest Pile"));
		piles.put(PileIds.SOUTH_EAST_PILE.ordinal(), new Pile("Southeast Pile"));
		piles.put(PileIds.SOUTH_WEST_PILE.ordinal(), new Pile("Southwest Pile"));
	}
	
	public class Test{
		public void testInitializeToNewGameState(List<Player> players){
			initializeToNewGameState(players);
		}
	}
}
